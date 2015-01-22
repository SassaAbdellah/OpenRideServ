package de.avci.joride.restful.dto.offers;

import de.avci.joride.restful.dto.basic.PointDTO;



/** Routepoints  routeIndices to Locations,
 *  
 * 
 * 
 * @author jochen
 *
 */
public class RoutepointDTO extends PointDTO {
	
	/** Integer referring the associated rideOffer (aka: drive)
	 */
	private Integer rideId;
	
	

	/** RouteIdx defines the order in which 
	 *  routepoints constitute a route.
	 * 
	 */
	private Integer routeIdx;
	

	public RoutepointDTO(){}




	public Integer getRouteIdx() {
		return routeIdx;
	}


	public void setRouteIdx(Integer routeIdx) {
		this.routeIdx = routeIdx;
	}



	public Integer getRideId() {
		return rideId;
	}




	public void setRideId(Integer rideId) {
		this.rideId = rideId;
	}
	
}
