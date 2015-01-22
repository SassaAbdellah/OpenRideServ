package de.avci.joride.restful.dto.offers;


/** Waypoints are defined by the driver when offering a ride.
 * 
 * @author jochen
 *
 */
public class WaypointDTO extends RoutepointDTO {

	
	
	
	/** Userdefined description of the waypoint
	 */
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	} 

	
	
	
}
