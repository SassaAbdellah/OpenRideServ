package de.avci.joride.restful.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.avci.openrideshare.messages.MessageControllerLocal;
import de.fhg.fokus.openride.customerprofile.CustomerControllerLocal;
import de.fhg.fokus.openride.customerprofile.FavoritePointControllerLocal;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideControllerLocal;


/**  Abstract base class for joride-rest services.
 *   
 *   Basically, it provides methods to look up OpenRideServer-ejb 
 *   services 
 * 
 * @author jochen
 *
 */
public abstract class AbstractRestService {
	
	/** JNDI Address where the controller lives
	 */
	private static final String DriverUndertakesRideControllerAdress="java:global/OpenRideServer/OpenRideServer-ejb/DriverUndertakesRideControllerBean!de.fhg.fokus.openride.rides.driver.DriverUndertakesRideControllerLocal";
	
	/** JNDI Address where the customer controller lives
	 */
	private static final String CustomerControllerAdress="java:global/OpenRideServer/OpenRideServer-ejb/CustomerControllerBean!de.fhg.fokus.openride.customerprofile.CustomerControllerLocal";
	

	/** JNDI Address where the favorite point controller lives
	 */
	private static final String FavoritePointControllerAdress="java:global/OpenRideServer/OpenRideServer-ejb/FavoritePointControllerBean!de.fhg.fokus.openride.customerprofile.FavoritePointControllerLocal";
	
	/** JNDI Address where the RiderUndertakesRide controller lives
	 */
	private static final String RiderUndertakesRideControllerAdress="java:global/OpenRideServer/OpenRideServer-ejb/RiderUndertakesRideControllerBean!de.fhg.fokus.openride.rides.rider.RiderUndertakesRideControllerLocal";
	
	
	
	/** JNDI Address where the Message controller lives
	 */
	private static final String MessageControllerAdress="java:global/OpenRideServer/OpenRideServer-ejb/MessageControllerBean!de.avci.openrideshare.messages.MessageControllerLocal";
	
	
	/** Look up Object (typically service) inside of the glassfish
     *
     * @return
     */
    private Object lookupController(String address) {
        try {
            javax.naming.Context c = new InitialContext();
            return  c.lookup(address);
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught while looking up service at : \n"+address, ne);
            throw new RuntimeException(ne);
        }

    }
	
	
	
    /** Lookup DriverUndertakesRideControllerLocal Bean that controls my offers.
     *
     * @return
     */
    protected DriverUndertakesRideControllerLocal lookupDriverUndertakesRideControllerBean() {    
    	return (DriverUndertakesRideControllerLocal) this.lookupController(DriverUndertakesRideControllerAdress);
    }
	

    /** Lookup CustomerControllerLocal Bean.
     *
     * @return
     */
    protected CustomerControllerLocal lookupCustomerControllerBean() {    
    	return (CustomerControllerLocal) this.lookupController(CustomerControllerAdress);
    }
	
	
    /** Lookup CustomerControllerLocal Bean.
    *
    * @return
    */
   protected FavoritePointControllerLocal lookupFavoritePointControllerBean() {    
   	return (FavoritePointControllerLocal) this.lookupController(FavoritePointControllerAdress);
   }
	
   /** Lookup RiderUndertakesRideControllerLocal Bean.
   *
   * @return
   */
  protected RiderUndertakesRideControllerLocal lookupRiderUndertakesRideControllerBean() {    
  	return (RiderUndertakesRideControllerLocal) this.lookupController(RiderUndertakesRideControllerAdress);
  }
	
   
  /** Lookup RiderUndertakesRideControllerLocal Bean.
  *
  * @return
  */
 protected MessageControllerLocal lookupMessageControllerBean() {    
 	return (MessageControllerLocal) this.lookupController(MessageControllerAdress);
 }
	
   

}
