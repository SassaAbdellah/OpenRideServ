package de.fhg.fokus.openride.routing.graphhopper.routing;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.EncodingManager;
import de.fhg.fokus.openride.routing.graphhopper.routing.constants.GraphhopperConstants;
import de.fhg.fokus.openride.routing.Coordinate;
import de.fhg.fokus.openride.routing.Route;
import de.fhg.fokus.openride.routing.RoutePoint;
import de.fhg.fokus.openride.routing.Router;
import de.fhg.fokus.openride.routing.graphhopper.configuration.GraphHopperConfiguration;
import java.sql.Timestamp;
import java.util.ArrayList;



public class GraphhopperRouter implements Router {

	/**
	 * Flag describing wether or not to use
	 * 
	 */
	public static final boolean ENABLE_CH = false;

	/**
	 * Algorithm to be used. May be made configurable once. "astarbi" and
	 * "dijkstrabi" are known to work.
	 * 
	 */
	public static String ALGORITHM = GraphhopperConstants.ALGORITHM_ASTARBI;

	/**
	 * A graphhopper Object to serve requests. to be created via lazy
	 * instantiation.
	 */
	GraphHopper gh;

	{ // initialize th gh object
		gh = new GraphHopper();

		if (!ENABLE_CH) {
			gh.disableCHShortcuts();
		}
		gh.setInMemory(true, false);
		gh.forServer();
		gh.setEncodingManager(new EncodingManager(GraphhopperConstants.CAR));

		// Do NOT use a custom encoding manager
		// gh.setEncodingManager(new EncodingManager());
		// gh.getEncodingManager().register(new CarWayEncoder());

                String ghdir=(new GraphHopperConfiguration()).getGHRouteBasedirFQN();
		String pbffqn=(new GraphHopperConfiguration()).getGHRoutePBF();
		gh.setGraphHopperLocation(ghdir).setOSMFile(pbffqn);
                
		gh.importOrLoad();
	} // end of ghInitialzation

	@Override
        
	public Route findRoute(Coordinate source, Coordinate target,
			Timestamp startTime, boolean fastestPath, double threshold) {

		GHRequest request = new GHRequest(source.getLatititude(), // startpoint
																	// Latitude
				source.getLongitude(), // startpoint Longitude
				target.getLatititude(), // endpoint Latitude
				target.getLongitude() // endpoint Longitude
		);

		// set request to "astarbi", "dijkstrabi" or any other
		// algorithm that works out.
		request.setAlgorithm(ALGORITHM);

		// fetch the response
		GHResponse response = gh.route(request);

		Route route = new GraphhopperRoute(startTime.getTime(), response);

		return route;

	}

	/**
	 * Create set of interpolated, almost equi-distant waypooints by pic
	 * 
	 * 
	 * 
	 */

	@Override
        
	public RoutePoint[] getEquiDistantRoutePoints(Coordinate[] coordinates,
			Timestamp startTime, boolean fastestPath, double threshold,
			double maxDistanceOfPoints) {

		ArrayList<RoutePoint> al = new ArrayList<RoutePoint>();

		Timestamp nextStartTime = startTime;
		for (int i = 0; i <= coordinates.length - 2; i++) {

			ArrayList<RoutePoint> partialResults = this
					.pickUpEquiDistantWaypoints(coordinates[i],
							coordinates[i + 1], nextStartTime, fastestPath,
							threshold, maxDistanceOfPoints);

                        // Note that pickUpEquiDistantWaypoints may return empty lists!
                        if(partialResults.size()>=2){
                    	al.addAll(partialResults);
			nextStartTime = partialResults.get(partialResults.size() - 1)
					.getTimeAt();
                        }
		}

		// recreate array of points so that distances are counting from
		// start to end (as currently they are reset to null at every
		// intermediate waypoint)
		// also, remove duplicate waypoints from result
		ArrayList<RoutePoint> resultList = new ArrayList<RoutePoint>();
		double lastIntermediateDistance = 0;
		double lastIntermediateDistanceCandidate = 0;
                
                if(al.size()>0){ // once again, the list's size may be 0
                    resultList.add(al.get(0));
                    al.remove(0);
                }

		for (RoutePoint rp : al) {

			if (rp.getDistance() == 0) {
				lastIntermediateDistance = lastIntermediateDistanceCandidate;

			} else {
				lastIntermediateDistanceCandidate = lastIntermediateDistance
						+ rp.getDistance();
				resultList.add(new RoutePoint(rp.getCoordinate(), rp
						.getTimeAt(), lastIntermediateDistanceCandidate));
			}
		}

		RoutePoint results[] = new RoutePoint[resultList.size()];
		return resultList.toArray(results);

	}

	/**
	 * 
	 * 
	 * @param start
	 * @param End
	 * @param startTime
	 * @param fastestPath
	 * @param maxDistanceOfPoints
	 * @return
	 */
	private ArrayList<RoutePoint> pickUpEquiDistantWaypoints(Coordinate source,
			Coordinate target, Timestamp startTime, boolean fastestPath,
			double threshold, double maxDistanceOfPoints) {

		// catch some trivialities
		if (source == null)
			throw new Error(
					"pickUpEquiDistantWaypoints called on null start  -- that shouldn't happen");
		if (target == null)
			throw new Error(
					"pickUpEquiDistantWaypoints called on null target -- that shouldn't happen");
		if (maxDistanceOfPoints <= 1000)
			throw new Error(
					"pickUpEquiDistantWaypoints called with trivial maxdist-- that shouldn't happen");

		Route route = this.findRoute(source, target, startTime, fastestPath,
				threshold);

		// arraylist to pick up result points....
		ArrayList<RoutePoint> pivotPoints = new ArrayList<RoutePoint>();

		RoutePoint[] rps = route.getRoutePoints();

                // sometimes (typically when calculating matches)
                // source and target may be close together or identical
                // in that case a route of length 0 is returned.
                //
		if (rps.length <= 2) {
                      ArrayList <RoutePoint> fakeRes=new ArrayList<RoutePoint>();
                      return fakeRes;
		}
		//
		// subroute's distance at last point where we picked up last pivot
		//

		pivotPoints.add(rps[0]);
		RoutePoint lastCheckedPoint = rps[0];
		for (int i = 0; i < rps.length; i++) {
			// if we just
			if (rps[i].getDistance() - lastCheckedPoint.getDistance() >= maxDistanceOfPoints) {
				lastCheckedPoint = rps[i - 1];
				pivotPoints.add(lastCheckedPoint);
			}
		}
		pivotPoints.add(rps[rps.length - 1]);
		// copy pivot points into the results

		return pivotPoints;
	}

}
