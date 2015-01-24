package de.avci.joride.jbeans.riderundertakesride;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideControllerLocal;


/** Counts the number of unrated rides
 *  for either driver and rider role
 * 
 * @author jochen
 *
 */

@Named("unrated")
@SessionScoped

public class UnratedRideStatistics implements Serializable {
	
	/** Number of unrated rides for this user as rider
	 */
	private int unratedRidesForRider=0;
	
	public int getUnratedRidesForRider(){
		return this.unratedRidesForRider;
	}
	
	/** True, if user has unrated rides as rider, else false.
	 */
	public boolean hasUnratedRidesForRider(){
		return this.getUnratedRidesForRider()>0;
	}
	
	
	/** Number of unrated rides for this user as driver
	 */
	private int unratedRidesForDriver=0;
	
	public int getUnratedRidesForDriver(){
		return this.unratedRidesForDriver;
	}
	
	/** True, if user has unrated rides as driver, else false.
	 */
	public boolean hasUnratedRidesForDriver(){
		return this.getUnratedRidesForDriver()>0;
	}
	

	/** Re-calculate the number of unrated rides for this rider
	 */
	public void update(){
		
		JRiderUndertakesRideEntityService service=new JRiderUndertakesRideEntityService();
		this.unratedRidesForRider=service.getUnratedRidesForRider().size();	
		this.unratedRidesForDriver=service.getUnratedRidesForDriver().size();	
	}
	

	
	

}
