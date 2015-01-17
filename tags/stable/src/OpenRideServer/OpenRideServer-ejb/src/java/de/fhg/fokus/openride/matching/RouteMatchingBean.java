/*
 OpenRide -- Car Sharing 2.0
 Copyright (C) 2010  Fraunhofer Institute for Open Communication Systems (FOKUS)

 Fraunhofer FOKUS
 Kaiserin-Augusta-Allee 31
 10589 Berlin
 Tel: +49 30 3463-7000
 info@fokus.fraunhofer.de

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License Version 3 as
 published by the Free Software Foundation.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fhg.fokus.openride.matching;

import de.fhg.fokus.openride.customerprofile.CustomerControllerLocal;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.rides.driver.DriveRoutepointEntity;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideEntity;
import de.fhg.fokus.openride.rides.driver.RoutePointEntity;
import de.fhg.fokus.openride.rides.driver.WaypointEntity;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;
import de.fhg.fokus.openride.routing.Coordinate;
import de.fhg.fokus.openride.routing.Route;
import de.fhg.fokus.openride.routing.RoutePoint;
import de.fhg.fokus.openride.routing.RouterBeanLocal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

/**
 * This class serves the complete RouteMatching functionality as Java EEBean.
 * The functionality comprises computing matches for driver and rider side, and
 * recomputing routes at which is required when a drive is booked.
 * 
 * For computing the Matches, the two Matching algorithms, driver side and rider
 * side, are used to obtain potential matches. Potential Matches are candidates
 * which fit to each other with regard to geographic position and time. These
 * two matching algorithms require a circle radius as well as a maximum distance
 * between a pair of route points as parameter. These values are computed based
 * on the max. detour, the driver accepts but a bounded to a certain range.
 * These bounds as well as parameters for computing the circle overlay can be
 * configured within section 'CONFIG - SEARCH FOR RIDER' and 'CONFIG - SEARCH
 * FOR DRIVER'.
 * 
 * These potential Matches are only candidates which have to meet additional
 * requirements to be a Match, that is to say they are filtered against
 * different criteria. Simple checks are implemented within the inner class
 * MatchFilter which can be configured via the static class variables within the
 * 'CONFIG - MATCH FILTER' section. Additionally a detour check is required,
 * which is the most expensive check and thus is applied at the end. To Check
 * the detour, a new route has to be compupted which involves computation of
 * partial routes, which is incredibly slow because of the bad routing algorithm
 * currently in use.
 * 
 * Candidate Matches having passed through all checks are finally added to the
 * result list together with some additional information. For now this
 * additional information comprises a proposed price as well as the shared
 * distance and required detour. The proposed price is computed within the class
 * 'PriceCalculator' the detour and shared distance computations are based on a
 * RoutingAlgorithm served by 'de.fhg.openride.routing.RouterBean'.
 * 
 * After the Result (list of matches) has been collected, they are ranked by
 * utilizing the class 'ScoringFunction'.
 * 
 * @author fvi
 */
@Stateless
public class RouteMatchingBean implements RouteMatchingBeanLocal {

	@PersistenceContext
	private EntityManager em;
	// CONFIG - DB:
	//
	// TODO: get rid of using JDBC in favour or standard JPA
	//
	private static final String JDBC_RESOURCE_OPENRIDE = "jdbc/openride";
	// CONFIG - MATCH FILTER
	private static final boolean FILTER_CHECK_GENDER = false;
	private static final boolean FILTER_CHECK_SMOKER = false;
	private static final boolean FILTER_CHECK_SEATS = true;
	private static final boolean FILTER_CHECK_SELF_MATCHES = true;
	private static final boolean FILTER_CHECK_ALREADY_BOOKED = true;
	private static final boolean FILTER_CHECK_DETOUR = true;
	// CONFIG - SEARCH FOR RIDER
	// bounds for distances between route points :
	// originally set to 1000d
	private static final double SFR_MIN_ROUTE_POINT_DISTANCE_METERS = 1000d;
	// originally set to 6000d
	// obviously, this is used a "maxDistancePoints" parameter in the
	// getEquiDistantRoutepoints method of the Router interface

	private static final double SFR_MAX_ROUTE_POINT_DISTANCE_METERS = 6000d;
	// bounds for driver detourMeters :
	private static final double SFR_MIN_DETOUR_METERS = 0d;
	private static final double SFR_MAX_DETOUR_METERS = 25000d;

	// circle overlay :
	/**
	 * Not all possible values for detour are allowed, thus a detour is derived
	 * which lies within the defined bounds.
	 * 
	 * @param acceptableDetourMeters
	 * @return acceptableDetourMeters if within bounds, else the min. or max.
	 *         bouondary value.
	 */
	private static double getSfrAcceptableDetourMetersBounded(
			double acceptableDetourMeters) {
		acceptableDetourMeters = Math.max(SFR_MIN_DETOUR_METERS,
				acceptableDetourMeters);
		acceptableDetourMeters = Math.min(SFR_MAX_DETOUR_METERS,
				acceptableDetourMeters);
		return acceptableDetourMeters;
	}

	/**
	 * Derive distance of route points (for matching) from the acceptable
	 * detour. This was suggested by the Bachlor thesis of Martin, i'm not
	 * really shure if this is a good choice. The choice was done with regard to
	 * performance, but it seems it makes it harder to control the accuracy and
	 * maybe degrades the performance of the searchForDrivers algorithm.
	 * 
	 * @param acceptableDetourMeters
	 * @return maximum distance the route points may have.
	 */
	public static double getSfrRoutePointDistance(double acceptableDetourMeters) {
		acceptableDetourMeters = getSfrAcceptableDetourMetersBounded(acceptableDetourMeters);
		double routePointDistance = acceptableDetourMeters * 2;
		routePointDistance = Math.max(routePointDistance,
				SFR_MIN_ROUTE_POINT_DISTANCE_METERS);
		routePointDistance = Math.min(routePointDistance,
				SFR_MAX_ROUTE_POINT_DISTANCE_METERS);
		return routePointDistance;
	}

	/**
	 * Derive the circle radius from the acceptable detour. This is closly
	 * coupled with getSfrRoutePointDistance.
	 * 
	 * @param acceptableDetourMeters
	 * @return
	 */
	private static double getSfrCircleRadius(double acceptableDetourMeters) {
		acceptableDetourMeters = getSfrAcceptableDetourMetersBounded(acceptableDetourMeters);
		double routePointDistance = getSfrRoutePointDistance(acceptableDetourMeters);
		double radius = Math.sqrt(Math.pow(acceptableDetourMeters, 2)
				+ Math.pow(routePointDistance / 2, 2));
		return radius;
	}

	// CONFIG - SEARCH FOR DRIVER
	// circle radius :
	private static final double SFD_MAX_CIRCLE_RADIUS = Math.sqrt(Math.pow(
			SFR_MAX_ROUTE_POINT_DISTANCE_METERS / 2, 2)
			+ Math.pow(SFR_MAX_DETOUR_METERS, 2));
	// END CONFIG
	private final Logger logger = Logger.getLogger(RouteMatchingBean.class
			.getName());
	@Resource(name = JDBC_RESOURCE_OPENRIDE)
	DataSource ds;
	@EJB
	CustomerControllerLocal customerControllerBean;
	@EJB
	DriverUndertakesRideControllerLocal driverUndertakesRideControllerBean;
	@EJB
	RiderUndertakesRideControllerLocal riderUndertakesRideControllerBean;
	@EJB
	RouterBeanLocal routerBean;

	/**
	 * Computes a set of Matches for the given drive offer.
	 * 
	 * This is issued any time a new drive is created. Note that this method
	 * also applies filtering.
	 * 
	 * If that is not what You want, have a look at {@see
	 * computeRawMatchingsForRide}
	 * 
	 * 
	 * @param driveId
	 *            Identifier for a driver's offer.
	 * @return list of matches sorted descending by score.
	 */
	@Override
	public LinkedList<MatchEntity> searchForRiders(int driveId) {
		logger.info("searchForRiders(driveId = " + driveId + ")");
		try {
			// get the parameter for the algorithm computing potential matches
			DriverUndertakesRideEntity drive = driverUndertakesRideControllerBean
					.getDriveByDriveId(driveId);
			List<DriveRoutepointEntity> routepoints = driverUndertakesRideControllerBean
					.getDriveRoutePoints(driveId);

			RoutePoint[] decomposedRoute = toRoutePointArray(routepoints);
			double r = getSfrCircleRadius(drive.getRideAcceptableDetourInKm() * 1000);

			// compute potential matches based on geographical position and time
			Connection conn = ds.getConnection();
			conn.setAutoCommit(false);
			RiderSearchAlgorithm algorithm = new RiderSearchAlgorithm(conn);
			LinkedList<PotentialMatch> potentialMatches = algorithm.findRiders(
					driveId, decomposedRoute, r);
			conn.commit();
			conn.close();

			// iterate over potential matches, and apply filtercriteria.
			// Matches passing all criteria are added to the 'matches' list.
			CustomerEntity driver = drive.getCustId();
			LinkedList<MatchEntity> matches = new LinkedList<MatchEntity>();
			for (Iterator<PotentialMatch> iter = potentialMatches.iterator(); iter
					.hasNext();) {
				PotentialMatch pm = iter.next();
				RiderUndertakesRideEntity ride = riderUndertakesRideControllerBean
						.getRideByRiderRouteId(pm.getRidersRouteId());
				CustomerEntity rider = ride.getCustId();

				// apply simple, less expensive filter criteria
				if (MatchFilter.filterAccepts(driver, rider, drive, ride, pm,
						routepoints)) {
					// check the detour :

					// compute adapted route (new driver route containing riders
					// start and endpoint as via points)
					LinkedList<DriveRoutepointEntity> decomposedRoute_ = new LinkedList<DriveRoutepointEntity>();
					LinkedList<RoutePointEntity> route_ = new LinkedList<RoutePointEntity>();
					double sharedDistanceMeters = computeAdaptedRoute(
							pm.getRideId(), pm.getRidersRouteId(),
							decomposedRoute_, route_);
					logger.info("sharedDistance = " + sharedDistanceMeters
							+ "m");

					// if no route can be computed we have to skip this match
					if (sharedDistanceMeters == -1) {
						logger.info("no route found or no seats lelft!");
						continue;
					}

					// detour is the difference of travel distance beteen
					// previous driver route and the new adapted route
					double detourMeters = decomposedRoute_.get(
							decomposedRoute_.size() - 1)
							.getDistanceToSourceMeters()
							- routepoints.get(routepoints.size() - 1)
									.getDistanceToSourceMeters();

					// check the detour
					logger.info("detour : "
							+ detourMeters
							+ "m / "
							+ getSfrAcceptableDetourMetersBounded(drive
									.getRideAcceptableDetourInKm() * 1000)
							+ "m");
					if (FILTER_CHECK_DETOUR
							&& detourMeters > getSfrAcceptableDetourMetersBounded(drive
									.getRideAcceptableDetourInKm() * 1000)) {
						continue;
					}

					// passed through the filter, add new match instance to
					// result list
					MatchEntity nextMatch = new MatchEntity(
							pm.getRidersRouteId(), pm.getRideId(),
							sharedDistanceMeters, detourMeters,
							pm.getTimeAtOnRouteLiftPoint(), decomposedRoute_
									.get(decomposedRoute_.size() - 1)
									.getDistanceToSourceMeters(),
							PriceCalculator.getInstance().getPriceCents(
									sharedDistanceMeters, detourMeters));

					nextMatch.setRiderUndertakesRideEntity(ride);
					nextMatch.setDriverUndertakesRideEntity(drive);

					matches.add(nextMatch);
				}
			}
			logger.info("searchForRidematches : " + matches.size() + " / "
					+ potentialMatches.size() + " (passed through filter)");

			// sort matches by score
			ScoringFunction.getInstance().sortDescending(matches);

			// note: matches are not persisted here, but in the calling
			// RiderUndertakesRideController.callMatchingAlgoritm()  instead!
			return matches;

		} catch (SQLException ex) {
			logger.log(Level.SEVERE, null, ex);
			return new LinkedList<MatchEntity>();
		}
	}

	/**
	 * Compute a list of all availlable matches for the given ride offer. This
	 * is issued any time a new drive is created. Note that this method also
	 * applies filtering.
	 * 
	 * If that is not what You want, have a look at {@see
	 * computeRawMatchingsForDrive}
	 * 
	 * 
	 * @param rideId
	 *            identifiers for rider's offer
	 * @return All matches sorted descending by score.
	 */
	@Override
	public LinkedList<MatchEntity> searchForDrivers(int rideId) {
		logger.info("searchForDrivers(rideId = " + rideId + ")");
		try {
			RiderUndertakesRideEntity ride = riderUndertakesRideControllerBean
					.getRideByRiderRouteId(rideId);

			// compute potential matches based on geographical position and time
			Connection conn = ds.getConnection();
			DriverSearchAlgorithm algorithm = new DriverSearchAlgorithm(conn);
			LinkedList<PotentialMatch> potentialMatches = algorithm.findDriver(
					rideId, ride.getStartpt(), ride.getEndpt(), new Timestamp(
							ride.getStarttimeEarliest().getTime()),
					new Timestamp(ride.getStarttimeLatest().getTime()),
					SFD_MAX_CIRCLE_RADIUS);
			conn.close();

			// iterate over potential matches, and apply filtercriteria.
			// Matches passing all criteria are added to the 'matches' list.
			CustomerEntity rider = ride.getCustId();
			LinkedList<MatchEntity> matches = new LinkedList<MatchEntity>(); // all
																				// accepted
																				// matches
																				// are
																				// put
																				// here
			for (Iterator<PotentialMatch> iter = potentialMatches.iterator(); iter
					.hasNext();) {
				PotentialMatch pm = iter.next();
				DriverUndertakesRideEntity drive = driverUndertakesRideControllerBean
						.getDriveByDriveId(pm.getRideId());
				CustomerEntity driver = drive.getCustId();
				List<DriveRoutepointEntity> routepoints = driverUndertakesRideControllerBean
						.getDriveRoutePoints(drive.getRideId());

				// apply simple, less expensive filter criteria
				if (MatchFilter.filterAccepts(driver, rider, drive, ride, pm,
						routepoints)) {
					// check the detour :

					// compute adapted route (new driver route containing riders
					// start and endpoint as via points)
					LinkedList<DriveRoutepointEntity> decomposedRoute_ = new LinkedList<DriveRoutepointEntity>();
					LinkedList<RoutePointEntity> route_ = new LinkedList<RoutePointEntity>();
					double sharedDistanceMeters = computeAdaptedRoute(
							pm.getRideId(), pm.getRidersRouteId(),
							decomposedRoute_, route_);
					logger.info("sharedDistance = " + sharedDistanceMeters
							+ "m");

					// if no route can be computed we have to skip this match
					if (sharedDistanceMeters == -1) {
						logger.info("no route found or no seats lelft!");
						continue;
					}

					// detour is the difference of travel distance beteen
					// previous driver route and the new adapted route
					double detourMeters = decomposedRoute_.get(
							decomposedRoute_.size() - 1)
							.getDistanceToSourceMeters()
							- routepoints.get(routepoints.size() - 1)
									.getDistanceToSourceMeters();

					// check the detour
					logger.info("detour : "
							+ detourMeters
							+ "m / "
							+ getSfrAcceptableDetourMetersBounded(drive
									.getRideAcceptableDetourInKm() * 1000)
							+ "m");
					if (FILTER_CHECK_DETOUR
							&& detourMeters > getSfrAcceptableDetourMetersBounded(drive
									.getRideAcceptableDetourInKm() * 1000)) {
						continue;
					}

					// passed through the filter, add new match instance to
					// result list
					MatchEntity m = new MatchEntity(pm.getRidersRouteId(),
							pm.getRideId(), sharedDistanceMeters, detourMeters,
							pm.getTimeAtOnRouteLiftPoint(), decomposedRoute_
									.get(decomposedRoute_.size() - 1)
									.getDistanceToSourceMeters(), // TODO:
																	// remaing
																	// distance
																	// - depends
																	// on driver
																	// tracker
							PriceCalculator.getInstance().getPriceCents(
									sharedDistanceMeters, detourMeters));
					m.setDriverUndertakesRideEntity(drive);
					m.setRiderUndertakesRideEntity(ride);
					matches.add(m);
				}
			}
			logger.info("matches : " + matches.size() + " / "
					+ potentialMatches.size() + " (passed through filter)");

			// sort matches by score
			ScoringFunction.getInstance().sortDescending(matches);

			return matches;
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, null, ex);
		} catch (IllegalArgumentException ex) {
			logger.log(Level.SEVERE, null, ex);
		}
		return null;
	}

	/**
	 * Small wrapper allowing to switch between old and new implementations of
	 * computeInitialRoutes while the first is beeing developed
	 * 
	 * @param drive
	 * @param decomposedRouteBuff
	 * @param routeBuff
	 * @return
	 */
	@Override
	public double computeInitialRoutes(DriverUndertakesRideEntity drive,
			LinkedList<DriveRoutepointEntity> decomposedRouteBuff,
			LinkedList<RoutePointEntity> routeBuff) {

		return this.computeInitialRoutesWithWaypoints(drive,
				decomposedRouteBuff, routeBuff);
	}

	/**
	 * Computes a route for a driver which has no associated ride offers.
	 * 
	 * 
	 * TODO: this currently ignores the existence of waypoints. TODO: make it
	 * adhere to waypoints!
	 * 
	 * 
	 * @param drive
	 *            driver's offer.
	 * @param decomposedRouteBuff
	 *            route points suitable for the matching algorithm.
	 * @param routeBuff
	 *            route points suitable for distplaying the route (all map
	 *            coordinates included).
	 * @return length of the route in meters.
	 * 
	 */
	private double computeInitialRoutesWithWaypoints(
			DriverUndertakesRideEntity drive,
			LinkedList<DriveRoutepointEntity> decomposedRouteBuff,
			LinkedList<RoutePointEntity> routeBuff) {

		// This would require just one route computation but
		// due to poor design two calls to the routing algorithm are required.
		// the route point interpolation method should be moved out of the
		// routerBean
		// to this class.

		// determine the coordinates along which we'll calculate
		// the initial route

		Coordinate[] myWaypoints = this.waypoints4InitialRoute(drive);
		logger.info("computeInitialRoutes : passed myWaypoint extraction");
		logger.info("computeInitialRoutes : myWaypoints.size "
				+ myWaypoints.length);

		Timestamp startTime = new Timestamp(drive.getRideStarttime().getTime());
		Double distance = 0d;

		for (int i = 0; i <= myWaypoints.length - 2; i++) {

			logger.info("computeInitialRoutes : computing partialRoute # " + i);

			Coordinate s = myWaypoints[i];
			Coordinate t = myWaypoints[i + 1];

			logger.info("computeInitialRoutes : start  : " + s);
			logger.info("computeInitialRoutes : target : " + t);

			Route partialRoute = this.computeRouteBetweenIntermediatePoints(
					drive, s, t, startTime, routeBuff);

			// If the partial route is pathological, we can end the process here
			if (partialRoute == null || partialRoute.getLength() == 0d) {
				logger.info("computeInitialRoutes : terminating prematurely, route is null");
				return Double.MAX_VALUE;
			}

			if (partialRoute.getLength() == 0d) {
				logger.info("computeInitialRoutes : terminating prematurely, route has no routepoints");
				return Double.MAX_VALUE;
			}

			// sum up over all the partial routes
			distance += partialRoute.getLength();
			logger.info("computeInitialRoutes : partial distance at round " + i
					+ " : " + distance);

			// startTime for next partial Route is end time of previous partial
			// route
			RoutePoint[] rps = partialRoute.getRoutePoints();
			RoutePoint partialEndpoint = rps[rps.length - 1];
			startTime = partialEndpoint.getTimeAt();

			logger.info("computeInitialRoutes : starting time after round " + i
					+ " : " + startTime);
		}
		//
		logger.info("computeInitialRoute : debug1 "
				+ this.routePointsDebugOutput(routeBuff));
		// readjust the routeIdx property
		for (int i = 0; i < routeBuff.size(); i++) {
			routeBuff.get(i).setRouteIdx(i);
		}
		logger.info("computeInitialRoute : debug2 "
				+ this.routePointsDebugOutput(routeBuff));

		// Luckily the implementation of getEquiDistantRoutepoints
		// supports intermediate points :))

		// compute decomposed route (less coordinates, but interpolated)
		RoutePoint[] decomposedRoute = routerBean
				.getEquiDistantRoutePoints(myWaypoints, startTime,
						Constants.ROUTE_FASTEST_PATH_DEFAULT,
						Constants.ROUTER_NEAREST_NEIGHBOR_THRESHOLD,
						getSfrRoutePointDistance(drive
								.getRideAcceptableDetourInKm() * 1000));

		// add it to buffer
		// TODO: this won't work
		int routeIdx = 0;
		for (RoutePoint rp : decomposedRoute) {
			decomposedRouteBuff.add(new DriveRoutepointEntity(
					drive.getRideId() == null ? -1 : drive.getRideId(),
					routeIdx++, rp.getCoordinate().toPoint(), rp.getTimeAt(),
					drive.getRideOfferedseatsNo(), rp.getDistance()));
		}

		logger.info("computeInitialRoutes : terminating :  number of route_points       : "
				+ routeBuff.size());
		logger.info("computeInitialRoutes : terminating :  number of drive_route_points : "
				+ decomposedRouteBuff);
		logger.info("computeInitialRoutes : terminating :  distance : "
				+ distance);

		return distance;
	}

	/**
	 * Compute a route between startpoint s and endpoint t. This is called
	 * iteratively to compute partial routes between given waypoints.
	 * 
	 * 
	 * 
	 * @param s
	 * @param t
	 * @param startTime
	 * @param routeBuff
	 * @return
	 */
	private Route computeRouteBetweenIntermediatePoints(
			DriverUndertakesRideEntity drive, Coordinate s, Coordinate t,
			Timestamp startTime, LinkedList<RoutePointEntity> routeBuff) {

		// compute route containing all map coordinates
		logger.info("RouteMatchingBean calling findRoute (new) s: " + s
				+ " t: " + t + " starttime " + startTime);

		Route route = routerBean.findRoute(s, t, startTime,
				Constants.ROUTE_FASTEST_PATH_DEFAULT,
				Constants.ROUTER_NEAREST_NEIGHBOR_THRESHOLD);
		if (route == null || route.getLength() == 0d) {
			return route;
		}

		// add it to the buffer
		int routeIdx = 0;
		for (RoutePoint rp : route.getRoutePoints()) {
			routeBuff.add(new RoutePointEntity(drive.getRideId() == null ? -1
					: drive.getRideId(), routeIdx, rp.getCoordinate()
					.getLongitude(), rp.getCoordinate().getLatititude(), null,
					routeIdx == 0
							|| routeIdx == route.getRoutePoints().length - 1));
			routeIdx++;
		}

		return route;
	}

	/**
	 * Creates a list of coordinates defining the bounds of the partial routes.
	 * 
	 * @return
	 */
	private Coordinate[] waypoints4InitialRoute(DriverUndertakesRideEntity drive) {

		// add waypoints
		List<WaypointEntity> waypoints = drive.getWaypoints();

		if (waypoints != null) {
			// but, waypoints should be sorted by routeIndex!
			Comparator<WaypointEntity> wpSorter = new Comparator<WaypointEntity>() {

				@Override
				public int compare(WaypointEntity o1, WaypointEntity o2) {
					if (o1.getRouteIdx() > o2.getRouteIdx()) {
						return 1;
					}
					if (o1.getRouteIdx() < o2.getRouteIdx()) {
						return -1;
					}
					return 0;
				}
			};
			Collections.sort(waypoints, wpSorter);
		} // if waypoints != null
			//
			// so now, waypoints are sorted
			//
			//
			// coordinates consist of startpoint, waypoints and endpoint
		Coordinate[] res = null;

		// if there are waypoints, than we need all waypoints+start and end
		if (waypoints != null) {
			res = new Coordinate[2 + waypoints.size()];
		} else {
			res = new Coordinate[2];
		}

		// define startpoint
		res[0] = new Coordinate(drive.getRideStartpt().getY(), drive
				.getRideStartpt().getX());
		logger.info("waypoints4InitialRoute : 5");

		// add waypoints
		if (waypoints != null) {
			for (int i = 0; i < waypoints.size(); i++) {
				res[i + 1] = new Coordinate(waypoints.get(i).getLatitude(),
						waypoints.get(i).getLongitude());
			}
		}

		// add endpoint
		res[res.length - 1] = new Coordinate(drive.getRideEndpt().getY(), drive
				.getRideEndpt().getX());

		logger.info("RouteMatchingBean calculated waypoints : " + res);

		return res;
	}

	/**
	 * Computes the route, the driver should drive if he'd book the given rider
	 * offer. Pre: The given rider offer must not be associated with the driver
	 * offer!
	 * 
	 * @param rideId
	 *            identifier of driver's offer.
	 * @param riderrouteId
	 *            identifier of rider's offer.
	 * @param decomposedRouteBuff
	 *            buffer to put the interpolated route points.
	 * @param routeBuff
	 *            buffer to put the complete path description, containing all
	 *            map coordinates.
	 * @return shared distance in meters. -1 if no adapted route could be
	 *         computed. This could be because of not enough seats available or
	 *         if there is no route found between a pair of coordinates.
	 */

	@Override
	public double computeAdaptedRoute(int rideId, int riderrouteId,
			LinkedList<DriveRoutepointEntity> decomposedRouteBuff,
			LinkedList<RoutePointEntity> routeBuff) {
		List<RoutePointEntity> requiredPoints = driverUndertakesRideControllerBean
				.getRequiredRoutePoints(rideId);
		RiderUndertakesRideEntity ride = riderUndertakesRideControllerBean
				.getRideByRiderRouteId(riderrouteId);
		DriverUndertakesRideEntity drive = driverUndertakesRideControllerBean
				.getDriveByDriveId(rideId);
		Coordinate start = new Coordinate(ride.getStartpt());
		Coordinate end = new Coordinate(ride.getEndpt());

		// The route must pass each coordinate within the list of required
		// points.
		// The question is at which indices to insert rider's start- and end
		// point.

		// compute these indices :
		LinkedList<Coordinate> coordinates = new LinkedList<Coordinate>();
		for (RoutePointEntity rp : requiredPoints) {
			coordinates
					.add(new Coordinate(rp.getLatitude(), rp.getLongitude()));
		}
		int insertIdxStart = getInsertIdx(coordinates, start, 1);
		if (insertIdxStart == -1) {
			return -1;
		}
		coordinates.add(insertIdxStart, start);
		int insertIdxEnd = getInsertIdx(coordinates, end, insertIdxStart + 1);
		if (insertIdxEnd == -1) {
			return -1;
		}
		coordinates.add(insertIdxEnd, end);

		// add the start- and end point of the ride offer
		// at the respective indices.
		requiredPoints.add(
				insertIdxStart,
				new RoutePointEntity(rideId, -1, start.getLongitude(), start
						.getLatititude(), riderrouteId, true));
		requiredPoints.add(
				insertIdxEnd,
				new RoutePointEntity(rideId, -1, end.getLongitude(), end
						.getLatititude(), riderrouteId, true));

		// compute number of seats available for sections starting at a required
		// point
		int[] seatsAvailable = new int[requiredPoints.size()];
		seatsAvailable[0] = drive.getRideOfferedseatsNo();
		{
			HashSet<Integer> seenPassengers = new HashSet<Integer>();
			int i = 0;
			for (Iterator<RoutePointEntity> iter = requiredPoints.iterator(); iter
					.hasNext();) {
				if (i > 0) {
					seatsAvailable[i] = seatsAvailable[i - 1];
				}
				RoutePointEntity rp = iter.next();
				if (rp.getRiderrouteId() != null) {
					if (!seenPassengers.contains(rp.getRiderrouteId())) {
						RiderUndertakesRideEntity passenger = riderUndertakesRideControllerBean
								.getRideByRiderRouteId(rp.getRiderrouteId());
						seatsAvailable[i] -= passenger.getNoPassengers();
						seenPassengers.add(rp.getRiderrouteId());
					} else {
						RiderUndertakesRideEntity passenger = riderUndertakesRideControllerBean
								.getRideByRiderRouteId(rp.getRiderrouteId());
						seatsAvailable[i] += passenger.getNoPassengers();
					}
				}
				i++;
			}
		}

		// check seatsAvailable[i] >= 0
		for (int i = 0; i < seatsAvailable.length; i++) {
			if (seatsAvailable[i] < 0) {
				// the desired rider cannot be picked up by the driver,
				// not enough seats are left.
				logger.info("not enough seats left!");
				return -1;
			}
		}

		// compute the complete route by merging routes of
		// each segment (route between two succeeding required points)
		// and add its route points to routeBuff.
		Timestamp startTime = new Timestamp(drive.getRideStarttime().getTime());
		int routeIdx = 0;
		for (int i = 1; i < requiredPoints.size(); i++) {
			RoutePointEntity s = requiredPoints.get(i - 1);
			RoutePointEntity t = requiredPoints.get(i);
			logger.info("route section[" + i + "] " + "(" + s.getLatitude()
					+ "," + s.getLongitude() + ") -> (" + t.getLatitude() + ","
					+ t.getLongitude() + ")");
			Route route = routerBean.findRoute(new Coordinate(s.getLatitude(),
					s.getLongitude()),
					new Coordinate(t.getLatitude(), t.getLongitude()),
					startTime, Constants.ROUTE_FASTEST_PATH_DEFAULT,
					Constants.ROUTER_NEAREST_NEIGHBOR_THRESHOLD);

			RoutePoint[] rp = route.getRoutePoints();
			logger.info("#routepoints = " + rp.length);
			if (rp.length == 0) {
				// temporary bug fix
				// these two coordinates are no map coordinates,
				// so fix this later!
				rp = new RoutePoint[] {
						new RoutePoint(new Coordinate(s.getLatitude(),
								s.getLongitude()), startTime, 0),
						new RoutePoint(new Coordinate(t.getLatitude(),
								t.getLongitude()), startTime, 0), };
			}

			for (int j = 0; j < rp.length; j++) {
				if (j == 0 && i > 1) {
					continue;
					// first point is last point of previous route,
					// already added!
				}
				if (j == 0) {
					routeBuff.add(new RoutePointEntity(rideId, routeIdx++,
							rp[j].getCoordinate().getLongitude(), rp[j]
									.getCoordinate().getLatititude(), s
									.getRiderrouteId(), true));
				} else if (j == rp.length - 1) {
					routeBuff.add(new RoutePointEntity(rideId, routeIdx++,
							rp[j].getCoordinate().getLongitude(), rp[j]
									.getCoordinate().getLatititude(), t
									.getRiderrouteId(), true));
				} else {
					routeBuff.add(new RoutePointEntity(rideId, routeIdx++,
							rp[j].getCoordinate().getLongitude(), rp[j]
									.getCoordinate().getLatititude(), null,
							false));
				}
			}
			if (route.getRoutePoints().length > 0) {
				startTime = route.getRoutePoints()[route.getRoutePoints().length - 1]
						.getTimeAt();
			}
		}

		// compute the decomposed route analog to the above algorithm
		// and add its route points to decomposedRouteBuff.
		startTime = new Timestamp(drive.getRideStarttime().getTime());
		double distanceOffset = 0;
		int roudeIdx = 0;
		double d1 = -1; // distance to rider's start point
		double d2 = -1; // distance to rider's end point
		for (int i = 1; i < requiredPoints.size(); i++) {
			// compute route for section i (between required points i-1 and i)
			RoutePointEntity s = requiredPoints.get(i - 1);
			RoutePointEntity t = requiredPoints.get(i);
			logger.info("decompose section[" + i + "] " + "(" + s.getLatitude()
					+ "," + s.getLongitude() + ") -> (" + t.getLatitude() + ","
					+ t.getLongitude() + ")" + " distanceOffset="
					+ distanceOffset);

			if (s.getRiderrouteId() != null
					&& s.getRiderrouteId() == riderrouteId && d1 == -1) {
				d1 = distanceOffset;
				logger.info("s = (" + s.getLatitude() + "," + s.getLongitude()
						+ ")" + " d1=" + d1);
			}

			RoutePoint[] decomposedroute = routerBean
					.getEquiDistantRoutePoints(
							new Coordinate[] {
									new Coordinate(s.getLatitude(), s
											.getLongitude()),
									new Coordinate(t.getLatitude(), t
											.getLongitude()) }, startTime,
							Constants.ROUTE_FASTEST_PATH_DEFAULT,
							Constants.ROUTER_NEAREST_NEIGHBOR_THRESHOLD,
							getSfrRoutePointDistance(drive
									.getRideAcceptableDetourInKm() * 1000));

			if (decomposedroute == null) {
				logger.info("FAILURE decomposedroute is NULL (should not happen)");
				return -1;
			}

			for (int j = 0; j < decomposedroute.length - 1; j++) {
				decomposedRouteBuff.add(new DriveRoutepointEntity(rideId,
						roudeIdx++, decomposedroute[j].getCoordinate()
								.toPoint(), decomposedroute[j].getTimeAt(),
						seatsAvailable[i - 1], decomposedroute[j].getDistance()
								+ distanceOffset));
			}

			if ((decomposedroute.length > 0)
					&& (i == requiredPoints.size() - 1)) {
				decomposedRouteBuff
						.add(new DriveRoutepointEntity(rideId, roudeIdx++,
								decomposedroute[decomposedroute.length - 1]
										.getCoordinate().toPoint(),
								decomposedroute[decomposedroute.length - 1]
										.getTimeAt(),
								seatsAvailable[seatsAvailable.length - 1],
								decomposedroute[decomposedroute.length - 1]
										.getDistance() + distanceOffset));
			}
			if (decomposedroute.length > 0) {
				startTime = decomposedroute[decomposedroute.length - 1]
						.getTimeAt();
				distanceOffset += decomposedroute[decomposedroute.length - 1]
						.getDistance();
			}

			if (t.getRiderrouteId() != null
					&& t.getRiderrouteId() == riderrouteId && d1 != -1) {
				d2 = distanceOffset;
				logger.info("t = (" + t.getLatitude() + "," + t.getLongitude()
						+ ") d2=" + d2);
			}
		}

		// maybe this check is not necessary
		if (d1 != -1 && d2 != -1) {
			double sharedDistance = d2 - d1;
			return sharedDistance;
		}
		return -1;
	}

	/**
	 * Computes the list index where to insert the given coordinate into the
	 * list of points. This is done by computing route lengths of segments. The
	 * computed index minimizes the detour.
	 * 
	 * @param points
	 *            list of points the driver route has to pass.
	 * @param c
	 *            coordinate to insert into the list.
	 * @param minIdx
	 *            first insert index to be checked.
	 * @return the index which minimizes detour.
	 */
	private int getInsertIdx(LinkedList<Coordinate> points, Coordinate c,
			int minIdx) {
		Coordinate[] arr = new Coordinate[points.size()];
		points.toArray(arr);

		int insertIdx = -1;
		double minDetour = Double.MAX_VALUE;
		minIdx = Math.max(1, minIdx);

		for (int i = minIdx; i < arr.length; i++) {
			Coordinate s = arr[i - 1];
			Coordinate t = arr[i];

			Route r1 = routerBean.findRoute(s, c, new Timestamp(0),
					Constants.ROUTE_FASTEST_PATH_DEFAULT,
					Constants.ROUTER_NEAREST_NEIGHBOR_THRESHOLD);
			if (r1 == null) {
				return -1;
			}
			Route r2 = routerBean.findRoute(c, t, new Timestamp(0),
					Constants.ROUTE_FASTEST_PATH_DEFAULT,
					Constants.ROUTER_NEAREST_NEIGHBOR_THRESHOLD);
			if (r2 == null) {
				return -1;
			}
			Route r3 = routerBean.findRoute(s, t, new Timestamp(0),
					Constants.ROUTE_FASTEST_PATH_DEFAULT,
					Constants.ROUTER_NEAREST_NEIGHBOR_THRESHOLD);
			if (r3 == null) {
				return -1;
			}

			double detour = (r1.getLength() + r2.getLength()) - r3.getLength();
			if (detour < minDetour) {
				minDetour = detour;
				insertIdx = i;
			}
		}
		return insertIdx;
	}

	/**
	 * Get Array of RoutePoint by list of DriveRoutepointEntity.
	 * 
	 * @param routePoints
	 * @return all route points from given list, same order.
	 */
	private RoutePoint[] toRoutePointArray(
			List<DriveRoutepointEntity> routePoints) {
		RoutePoint[] result = new RoutePoint[routePoints.size()];
		int i = 0;
		for (DriveRoutepointEntity drp : routePoints) {
			result[i++] = new RoutePoint(new Coordinate(drp.getCoordinate()),
					drp.getExpectedArrival(), drp.getDistanceToSourceMeters());
		}
		return result;
	}

	@Override
	public MatchingStatistics getStatisticsForRide(int riderrouteId) {

		MatchingStatistics res = new MatchingStatistics();
		res.statisticsFromList(this.computeRawMatchingsForRide(riderrouteId));
		return res;
	}

	@Override
	public MatchingStatistics getStatisticsForDrive(int rideId) {

		MatchingStatistics res = new MatchingStatistics();
		res.statisticsFromList(this.computeRawMatchingsForDrive(rideId));
		return res;
	}

	/**
	 * This class implements all simple, less expensive checks. It can be
	 * configured by the static class variables within the config section.
	 */
	private static class MatchFilter {

		private static boolean filterAccepts(CustomerEntity driver,
				CustomerEntity rider, DriverUndertakesRideEntity drive,
				RiderUndertakesRideEntity ride, PotentialMatch pm,
				List<DriveRoutepointEntity> routePoints) {

			return (!FILTER_CHECK_GENDER || checkGender(driver, rider))
					&& (!FILTER_CHECK_SMOKER || checkSmoker(driver, rider))
					&& (!FILTER_CHECK_SEATS || checkSeatsAvailable(drive, ride,
							pm.getLiftIndex(), pm.getDropIndex()))
					&& (!FILTER_CHECK_SELF_MATCHES || checkSelfMatches(driver,
							rider))
					&& (!FILTER_CHECK_ALREADY_BOOKED || checkAlreadyBocked(ride));
		}

		private static boolean checkGender(CustomerEntity driver,
				CustomerEntity rider) {
			char driverPref = driver.getCustDriverprefGender();
			char riderPref = rider.getCustRiderprefGender();

			return driverPref == CustomerEntity.PREF_GENDER_DONT_CARE
					|| riderPref == CustomerEntity.PREF_GENDER_DONT_CARE
					|| (driverPref == CustomerEntity.PREF_GENDER_GIRLS_ONLY && rider
							.getCustGender() == CustomerEntity.GENDER_FEMALE)
					|| (riderPref == CustomerEntity.PREF_GENDER_GIRLS_ONLY && driver
							.getCustGender() == CustomerEntity.GENDER_FEMALE);
		}

		private static boolean checkSmoker(CustomerEntity driver,
				CustomerEntity rider) {
			char driverPref = driver.getCustDriverprefIssmoker();
			char riderPref = rider.getCustRiderprefIssmoker();

			return (driverPref == riderPref)
					|| driverPref == CustomerEntity.PREF_SMOKER_DONT_CARE
					|| riderPref == CustomerEntity.PREF_SMOKER_DONT_CARE;
		}

		private static boolean checkSeatsAvailable(
				DriverUndertakesRideEntity drive,
				RiderUndertakesRideEntity ride, int liftIdx, int dropIdx) {
			// DriveRoutepointEntity[] arr = new
			// DriveRoutepointEntity[routePoints.size()];
			// routePoints.toArray(arr);
			// for(int i = liftIdx; i < dropIdx; i++) {
			// if(arr[i].getSeatsAvailable() < ride.getNoPassengers()) {
			// return false;
			// }
			// }
			// return true;
			int seatsAvailable = drive.getRideOfferedseatsNo();
			for (RiderUndertakesRideEntity r : drive
					.getRiderUndertakesRideEntityCollection()) {
				seatsAvailable -= r.getNoPassengers();
			}
			return seatsAvailable - ride.getNoPassengers() >= 0;
		}

		private static boolean checkSelfMatches(CustomerEntity driver,
				CustomerEntity rider) {
			return !driver.getCustId().equals(rider.getCustId());
		}

		private static boolean checkAlreadyBocked(RiderUndertakesRideEntity ride) {
			return ride.getRideId() == null;
		}
	}

	/**
	 * Compute the raw list of matchings that reference the ride via
	 * riderrouteId. (without attaching any kind of filter!)
	 * 
	 * @param riderrouteId
	 * @return
	 */
	private List<MatchEntity> computeRawMatchingsForRide(int riderrouteId) {

		// @NamedQuery(name = "MatchEntity.findByRiderrouteId", query =
		// "SELECT m FROM MatchEntity m WHERE m.matchEntityPK.riderrouteId = :riderrouteId")

		return (List<MatchEntity>) em
				.createNamedQuery("MatchEntity.findByRiderrouteId")
				.setParameter("riderrouteId", riderrouteId).getResultList();
	}

	/**
	 * Compute the raw list of matchings that reference the drive via driveId.
	 * (without attaching any kind of filter!)
	 * 
	 * @param driveId
	 * @return
	 */
	private List<MatchEntity> computeRawMatchingsForDrive(int rideId) {
		return (List<MatchEntity>) em
				.createNamedQuery("MatchEntity.findByRideId")
				.setParameter("rideId", rideId).getResultList();
	}

	/**
	 * TODO: ad-hoc debug output. Remove once create initial
	 * 
	 * @deprecated
	 * 
	 */
	private static String routePointsDebugOutput(List<RoutePointEntity> rps) {

		StringBuilder buf = new StringBuilder();
		buf.append("[");

		for (int i = 0; i < rps.size(); i++) {
			buf.append("<" + (rps.get(i)).getRouteIdx() + "> ");
		}

		buf.append("]");

		return buf.toString();
	}
}
