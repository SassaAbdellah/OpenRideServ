package de.avci.joride.restful.dto.matches;

import java.sql.Timestamp;
import java.util.Date;

import de.avci.joride.restful.dto.basic.AbstractDTO;
import de.fhg.fokus.openride.matching.MatchEntity;

/**
 * Model for a match between an offer and a request
 * 
 * @author jochen
 * 
 * No abstract DTO, since matches do not have a single id, but a composite PK
 */
public class MatchDTO {

	/**
	 * State of the matching.
	 * 
	 */
	private Integer state;

	/**
	 * State of negotiations, Driver's side. values are defined in MatchEntity
	 * in OpenRideServer-ejb:
	 * 
	 * NOT_ADAPTED, ACCEPTED,D RIVER_COUNTERMANDED, RIDER_COUNTERMANDED,
	 * REJECTED, NO_MORE_AVAILLABLE
	 * 
	 */
	private Integer driverState = MatchEntity.NOT_ADAPTED;

	/**
	 * State of negotiations, rider's side
	 * 
	 * values are defined in MatchEntity in OpenRideServer-ejb:
	 * 
	 * NOT_ADAPTED, ACCEPTED,D RIVER_COUNTERMANDED, RIDER_COUNTERMANDED,
	 * REJECTED, NO_MORE_AVAILLABLE
	 * 
	 */
	private Integer riderState = MatchEntity.NOT_ADAPTED;

	/** Time, when rider is expected to be picked up
	 */
	private Date matchExpectedStartTime;

	/** Id of respective Offer
	 */
	private Integer rideOfferId;
	
	/** Id of respective Request
	 */
	private Integer rideRideRequestId;

	/** When did the driver change Matching last time?
	 */
	private Timestamp driverChange;

	/** When did the rider change Matching last time?
	 */
	private Timestamp riderChange;

	/** When did the driver access Matching last time?
	 */
	private Timestamp driverAccess;

	/** When did the rder access Matching last time?
	 */
	private Timestamp riderAccess;

	/** Message from rider to driver.
	 */
	private String riderMessage;

	/** Message from driver to rider.
	 */
	private String driverMessage;

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getDriverState() {
		return driverState;
	}

	public void setDriverState(Integer driverState) {
		this.driverState = driverState;
	}

	public Integer getRiderState() {
		return riderState;
	}

	public void setRiderState(Integer riderState) {
		this.riderState = riderState;
	}

	public Date getMatchExpectedStartTime() {
		return matchExpectedStartTime;
	}

	public void setMatchExpectedStartTime(Date matchExpectedStartTime) {
		this.matchExpectedStartTime = matchExpectedStartTime;
	}

	public Integer getRideOfferId() {
		return rideOfferId;
	}

	public void setRideOfferId(Integer rideOfferId) {
		this.rideOfferId = rideOfferId;
	}

	public Integer getRideRideRequestId() {
		return rideRideRequestId;
	}

	public void setRideRideRequestId(Integer rideRideRequestId) {
		this.rideRideRequestId = rideRideRequestId;
	}

	public Timestamp getDriverChange() {
		return driverChange;
	}

	public void setDriverChange(Timestamp driverChange) {
		this.driverChange = driverChange;
	}

	public Timestamp getRiderChange() {
		return riderChange;
	}

	public void setRiderChange(Timestamp riderChange) {
		this.riderChange = riderChange;
	}

	public Timestamp getDriverAccess() {
		return driverAccess;
	}

	public void setDriverAccess(Timestamp driverAccess) {
		this.driverAccess = driverAccess;
	}

	public Timestamp getRiderAccess() {
		return riderAccess;
	}

	public void setRiderAccess(Timestamp riderAccess) {
		this.riderAccess = riderAccess;
	}

	public String getRiderMessage() {
		return riderMessage;
	}

	public void setRiderMessage(String riderMessage) {
		this.riderMessage = riderMessage;
	}

	public String getDriverMessage() {
		return driverMessage;
	}

	public void setDriverMessage(String driverMessage) {
		this.driverMessage = driverMessage;
	}
	
	
	/** Bean constructor
	 */
	
	public MatchDTO(){
		
	}

}
