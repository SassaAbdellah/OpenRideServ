package de.fhg.fokus.openride.routing.graphhopper.routing;

import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.graphhopper.GHResponse;
import com.graphhopper.util.DistanceCalc;
import com.graphhopper.util.DistanceCalcEarth;
import com.graphhopper.util.GPXEntry;
import com.graphhopper.util.InstructionList;
import de.fhg.fokus.openride.routing.Coordinate;
import de.fhg.fokus.openride.routing.Route;
import de.fhg.fokus.openride.routing.RoutePoint;


public class GraphhopperRoute implements Route {

	private RoutePoint[] routePoints;

	
	private DistanceCalc distanceCalc;
	
	private DistanceCalc getDistanceCalc(){
		
		if(this.distanceCalc==null){
			this.distanceCalc=new DistanceCalcEarth();
		}
		
		return distanceCalc;
		
	}
	
	
	@Override
	public RoutePoint[] getRoutePoints() {
		return this.routePoints;
	}

	/**
	 * Length of track in meters. To be computed on initialization.
	 */
	private double length = 0;

	@Override
	public double getLength() {
		return this.length;
	}

	/**
	 * Estimated travel time in millis. To be computed on initialisation
	 */
	private long travelTime = 0;

	@Override
	public long getTravelTime() {
		return travelTime;
	}

	/**
	 * GHResponse from which this was constructed
	 */
	private GHResponse response;

	/**
	 * Time when the ride is started.
	 */
	private long startTime;

	public long getStartTime() {
		return this.startTime;
	}

	/**
	 * Initialize data from response
	 * 
	 * i.e: travelTime, routePoints,
	 * 
	 */
	private void initialize() {

		this.length = response.getDistance();
		this.travelTime = response.getMillis();

		InstructionList instructionList = response.getInstructions();
		
		
		List<GPXEntry> gpxList=null;
		try  { 
			gpxList = instructionList.createGPXList();
			this.routePoints = new RoutePoint[gpxList.size()];
			
		} catch(java.lang.ArrayIndexOutOfBoundsException exc){
			// this might happen, if requested startpoints come close to waypoints
			this.routePoints=new RoutePoint[0];
		}
		
		
		

		
		double distance = 0;
		
		if(gpxList==null || gpxList.isEmpty()){
			return; // shouldn't normally happen...
		}
		
		GPXEntry previousEntry = gpxList.get(0);
		// create pointlist (TODO: distances will still have to be calculated)
		for (int i = 0; i < gpxList.size(); i++) {

			GPXEntry gpxEntry = gpxList.get(i);

			// partial distance
			double partialdist = this.getDistanceCalc().calcDist( //

					previousEntry.getLon(), // fromY
					previousEntry.getLat(), // fromX
				
					gpxEntry.getLon(),// toX
					gpxEntry.getLat()// toY
					);

			distance += partialdist;

			RoutePoint rp = new RoutePoint(//
					new Coordinate(gpxEntry.getLat(), //
							gpxEntry.getLon()), //
					new Timestamp(startTime + gpxEntry.getMillis()), //
					distance);

			routePoints[i] = rp;
			previousEntry = gpxEntry;

		}

		// (coordinate, timeAt, distance);

	} // initialize

	/**
	 * Probably currently unused... should go away from interface.
	 * 
	 * TODO: should go away from interface.
	 */
	@Override
	public void writeAsXml(OutputStream out, boolean includeEstimatedTimes) {
		// TODO Auto-generated method stub

	}

	/**
	 * probably currently unused... TODO should go away from interfa:ce.
	 */

	@Override
	public void appendAsXml(StringBuffer buffer, boolean includeEstimatedTimes) {
		// TODO Auto-generated method stub

	}

	/**
	 * Create new OpenRide Route from startTime and GraphHopperResponse
	 * 
	 * @param startTime
	 * @param response
	 */
	protected GraphhopperRoute(long startTime, GHResponse response) {

		super();
		this.startTime = startTime;
		this.response = response;
		this.initialize();

	}

	/**
	 * Do some kind of
	 * 
	 */
	public String toString() {

		StringBuffer buff = new StringBuffer();

		buff.append("\n=== " + this.getClass().getCanonicalName() + "=======");
		buff.append("\n===  StartTime       : " + new Date(this.getStartTime()));
		buff.append("\n===  TravelTime(min) : " + (this.travelTime / 60000));
		buff.append("\n===  Length (m)      : " + (this.length));
		buff.append("\n===");
		buff.append("\n===  RoutePoints     : \n");
		for (RoutePoint rp : this.routePoints) {
			Coordinate coo = rp.getCoordinate();
			buff.append("\n  <" + coo.getLongitude() + ","
					+ coo.getLatititude() + "> ");
			buff.append("\n  Time     : " + rp.getTimeAt());
			buff.append("\n  Distance : " + rp.getDistance());

		}

		return buff.toString();

	}

}
