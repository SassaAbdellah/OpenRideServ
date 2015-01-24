package de.avci.joride.restful.dto.offers;

import java.sql.Timestamp;







/** Adding expected arrival and number of available seats to Routepoints
 * 
 * @author jochen
 *
 */


public class DriveRoutepointDTO extends RoutepointDTO {
	
	
	/** Timestamp when driver is expected to pass this point
	 */
	private Timestamp expectedArrival;
	
	/** Number of seats still availlable when passing this point.
	 * 
	 */
	private Integer seatsAvaillable;
	
	
	/** Bean constructor
	 */
	public DriveRoutepointDTO(){}

	public Timestamp getExpectedArrival() {
		return expectedArrival;
	}

	public void setExpectedArrival(Timestamp expectedArrival) {
		this.expectedArrival = expectedArrival;
	}

	public Integer getSeatsAvaillable() {
		return seatsAvaillable;
	}

	public void setSeatsAvaillable(Integer seatsAvaillable) {
		this.seatsAvaillable = seatsAvaillable;
	}
	
	

}
