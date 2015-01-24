package de.avci.joride.restful.dto.offers;

import java.sql.Timestamp;
import java.util.List;

import de.avci.joride.restful.dto.basic.AbstractDTO;
import de.avci.joride.restful.dto.basic.LocationDTO;


/** Ride Offer
 * 
 * @author jochen
 *
 */
public class RideOfferDTO extends AbstractDTO {
	
	/** Id of driver (or ) whoever offers this ride
	 */
	private Integer customerId;
	
	/** Acceptable Detour in kilometers
	 */
	private Integer acceptableDetourKM;
	
	
	/** Time when to start
	 * 
	 */
	private Timestamp startTime;
	
	/** Location of start
	 */
	private LocationDTO startLocation;
	
	
	/** Location of End of Journey
	 */
	private LocationDTO endLocation;
	
	
	/** SetOfRoutepoints (which carry spatial information of the ride)
	 *  This is to be calculated by the backend, not to be set by User
	 */
	private List <RoutepointDTO> routePoints;
	
	
	/** SetOfRoutepoints with added time of arrival 
	 * (which carry spatial and time information of the ride)
	 *  This is to be calculated by the backend, not to be set by User
	 */
	private List <DriveRoutepointDTO> drivePoints;

	/** Set of waypoints. Waypoints are defined by the driver
	 *  whenever the driver wants to take another way 
	 *  than what is proposed by the routing engine.
	 *  
	 */
	private List <WaypointDTO> wayPoints;
	
	/** Number of availlable seats that the driver has to offer.
	 *  
	 */
	private int offeredSeatsNo;
	
	/** Something the driver wants to tell the riders.
	 *  Before they accept on the ride.
	 */
	private String comment;

	
	
	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public LocationDTO getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(LocationDTO startLocation) {
		this.startLocation = startLocation;
	}

	public LocationDTO getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(LocationDTO endLocation) {
		this.endLocation = endLocation;
	}

	public List <RoutepointDTO> getRoutePoints() {
		return routePoints;
	}

	public void setRoutePoints(List <RoutepointDTO> routePoints) {
		this.routePoints = routePoints;
	}

	public List <DriveRoutepointDTO> getDrivePoints() {
		return drivePoints;
	}

	public void setDrivePoints(List <DriveRoutepointDTO> drivePoints) {
		this.drivePoints = drivePoints;
	}

	public List <WaypointDTO> getWayPoints() {
		return wayPoints;
	}

	public void setWayPoints(List<WaypointDTO> list) {
		this.wayPoints = list;
	}

	public int getOfferedSeatsNo() {
		return offeredSeatsNo;
	}

	public void setOfferedSeatsNo(int arg) {
		this.offeredSeatsNo = arg;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getAcceptableDetourKM() {
		return acceptableDetourKM;
	}

	public void setAcceptableDetourKM(Integer acceptableDetourKM) {
		this.acceptableDetourKM = acceptableDetourKM;
	}
	
}
