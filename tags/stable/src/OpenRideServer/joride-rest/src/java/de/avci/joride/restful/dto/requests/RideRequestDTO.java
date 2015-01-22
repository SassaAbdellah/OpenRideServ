package de.avci.joride.restful.dto.requests;

import java.sql.Timestamp;

import de.avci.joride.restful.dto.basic.AbstractDTO;
import de.avci.joride.restful.dto.basic.LocationDTO;






public class RideRequestDTO extends AbstractDTO {
	
	
	
	/** Customer that requests the ride
	 */
	private Integer customerId;
	
	/** Earlies StartTime defined by Rider
	 */
	private Timestamp startTimeEarliest;
	
	/** Latest StartTime defined by Rider
	 */
	private Timestamp startTimeLatest;
	
	
	/** Location where to start, defined by Rider
	 */
	private LocationDTO startLocation;

	/** goal defined by Rider
	 */
	private LocationDTO endLocation;
	
	
	/** Number of Passengers
	 */
	private Integer numberOfPassengers;
	
	/** Something the rider wants to tell the driver.
	 *  Before they accept on the ride.
	 */
	private String comment;
	
	/** Price that rider is willing to pay
	 */
	private double price;


	public Timestamp getStartTimeEarliest() {
		return startTimeEarliest;
	}


	public void setStartTimeEarliest(Timestamp startTimeEarliest) {
		this.startTimeEarliest = startTimeEarliest;
	}


	public Timestamp getStartTimeLatest() {
		return startTimeLatest;
	}


	public void setStartTimeLatest(Timestamp startTimeLatest) {
		this.startTimeLatest = startTimeLatest;
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


	public int getNumberOfPassengers() {
		return numberOfPassengers;
	}


	public void setNumberOfPassengers(int arg) {
		this.numberOfPassengers=arg;
	}
	
	
	
	/** Bean constructor
	 */
	public RideRequestDTO(){
		
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public int getCustomerId() {
		return customerId;
	}


	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	

}
