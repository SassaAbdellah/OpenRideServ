package de.fhg.fokus.openride.routing.graphhopper.routing.constants;

public class GraphhopperConstants {

	/** Mnemonic for A-STAR-BIdirectional routing algoritm
	 */
	public static final String ALGORITHM_ASTARBI="astarbi";
	/** Mnemonic for DIJKSTRA-BIdirectional routing algoritm
	 */
	public static final String ALGORITHM_DIJKSTRABI="dijkstrabi";
	
	
	/** String constant that causes the EncodingManager to load a "CarFlagEncoder"
	 */
	public static final String CAR = "CAR";
	
	
	
	/** MaxDistance of Points parameter as used in Route.getEquiDistantRoutePoints
	 * 
	 *  TODO: this should be made configurable depending on the maximum 
	 *  detour the driver is willing to make.
	 */
	public static final double MAX_DISTANCE_OF_POINTS=6000;
	
	
	

	/** wheter or not the graph should be created with ContructionHierarchies
	 * 
	 *  Note that for doing the routing with CH_ENABLED, the Graph must be created
	 *  with that Option.
	 *  
	 * 
	 */
	public static boolean USE_CH=true;
	
	
}
