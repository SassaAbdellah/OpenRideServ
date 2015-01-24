/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.matching;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.avci.joride.jbeans.customerprofile.JCustomerEntityService;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.matching.MatchEntity;
import de.fhg.fokus.openride.matching.MatchingStatistics;
import de.fhg.fokus.openride.matching.RouteMatchingBeanLocal;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideControllerLocal;



/**
 * Service for delivering JMatchingEntities.
 *
 *
 * @author jochen
 */
public class JMatchingEntityService {

    static final Logger log = Logger.getLogger("" + JMatchingEntity.class);

    /**
     * Lookup MatchingBeanLocal that controls my requests.
     *
     * @return
     */
    protected RouteMatchingBeanLocal lookupRouteMatchingBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (RouteMatchingBeanLocal) c.lookup("java:global/OpenRideServer/OpenRideServer-ejb/RouteMatchingBean!de.fhg.fokus.openride.matching.RouteMatchingBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    

    /**
     * Lookup DriverUndertakesRideControllerLocal Bean that controls my offers.
     *
     * @return
     */
    protected DriverUndertakesRideControllerLocal lookupDriverUndertakesRideControllerBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (DriverUndertakesRideControllerLocal) c.lookup("java:global/OpenRideServer/OpenRideServer-ejb/DriverUndertakesRideControllerBean!de.fhg.fokus.openride.rides.driver.DriverUndertakesRideControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }

    }

    /**
     * Lookup RiderUndertakesRideControllerLocal Bean that controls my requests.
     *
     * @return
     */
    protected RiderUndertakesRideControllerLocal lookupRiderUndertakesRideControllerBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (RiderUndertakesRideControllerLocal) c.lookup("java:global/OpenRideServer/OpenRideServer-ejb/RiderUndertakesRideControllerBean!de.fhg.fokus.openride.rides.rider.RiderUndertakesRideControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }

    }

    /**
     * Get a customerEntity from the current request
     *
     * @return
     */
    public CustomerEntity getCustomerEntity() {
        return (new JCustomerEntityService()).getCustomerEntitySafely();
    }

    /**
     * Returns a list of Matches for a rideRequest
     *
     * @param rideId Id of the ride request for which we have to find matching
     * offers
     *
     *
     * @return list of matching offers for given request
     * 
     * 
     * @deprecated  uses RouteMatchingBean.searchForDrivers, which is costly, and should only be called when creatingRides
   		*
     */
    public List<JMatchingEntity> getJMatchesForRide(int riderrouteId) {

        List<MatchEntity> mel = this.lookupRouteMatchingBeanLocal().searchForDrivers(riderrouteId);

        Iterator<MatchEntity> it = mel.iterator();

        List<JMatchingEntity> res = new LinkedList<JMatchingEntity>();

        while (it.hasNext()) {
            res.add(new JMatchingEntity(it.next()));
        }

        return res;

    } // getJMatchesForRide

    /**
     * Returns a list of Matches for a rideRequest
     *
     * @param rideId Id of the ride request for which we have to find matching
     * offers
     *
     *
     * @return list of matching offers for given request
     * 
     * 
     * @deprecated  uses RouteMatchingBean.searchForDrivers, which is costly, and should only be called when creatingRides
     * 
     */
    public List<MatchEntity> getMatchesForRide(int riderrouteId) {

        List<MatchEntity> mel = this.lookupRouteMatchingBeanLocal().searchForDrivers(riderrouteId);
        return mel;

    } // getMatchesForRide

    /**
     * Return a MatchingStatistics Object for Request given by riderrouteId
     *
     *
     * @param riderrouteId
     * @return
     */
    public MatchingStatistics getMatchingStatisticsForRide(int riderrouteId) {
        return this.lookupRouteMatchingBeanLocal().getStatisticsForRide(riderrouteId);
    }

    /**
     * Return a MatchingStatistics Object for Offer given by rideId
     *
     * @param rideId
     * @return
     */
    public MatchingStatistics getMatchingStatisticsForOffer(int rideId) {
        return this.lookupRouteMatchingBeanLocal().getStatisticsForDrive(rideId);
    }

    /**
     * Get All Matches for a given Offer Friendly Frontend for
     * DriverUndertakesRideController.getMatches(rideId, true);
     *
     *
     * @param rideId Id of the ride for which to detect matches
     * @return lists of matches
     * 
     * 
     * 
     * @deprecated  uses RouteMatchingBean.searchForDrivers, which is costly, and should only be called when creatingRides
     * 
     * 
     */
    public List<JMatchingEntity> getJMatchesForOffer(int rideId) {


        RouteMatchingBeanLocal rmbl = this.lookupRouteMatchingBeanLocal();

        List<JMatchingEntity> res = new LinkedList<JMatchingEntity>();
        List<MatchEntity> mel = rmbl.searchForRiders(rideId);

        Iterator<MatchEntity> it = mel.iterator();

        while (it.hasNext()) {
            MatchEntity me = it.next();
            res.add(new JMatchingEntity(me));
        }

        return res;

    }

    /**
     * Get All Matches for a given Offer Friendly Frontend for
     * DriverUndertakesRideController.getMatches(rideId, true);
     *
     *
     * @param rideId Id of the ride for which to detect matches
     * @return lists of matches
     * 
     * 
     * @deprecated  uses RouteMatchingBean.searchForDrivers, which is costly, and should only be called when creatingRides
     * 
     */
    public List<MatchEntity> getMatchesForOffer(int rideId) {


        RouteMatchingBeanLocal rmbl = this.lookupRouteMatchingBeanLocal();
        List<MatchEntity> mel = rmbl.searchForRiders(rideId);
        return mel;
    }

    /**
     * Accept the rider for this match savely.
     *
     * As it is up to the driver of a ride to accept the rider, we do some
     * extensive checking about wether or not the driver is allowed to call
     *
     * @param jme -- the Matching entity to be updated
     * @return true, if setting the matching has works. Else false.
     */
    public boolean acceptRiderSafely(JMatchingEntity jme) {


        log.log(Level.FINE, "" + this.getClass() + " acceptRiderSafely beeing called");


        // see who calls us!      
        CustomerEntity caller = this.getCustomerEntity();

        
        if (caller == null) {
            throw new Error("Cannot proceed to accept rider, Caller is null");
        }

        if (caller.getCustId() == null) {
            throw new Error("Cannot proceed to accept rider, CallerId is null");
        }


        // determine the driver of the match entity's trip

        Integer driverId = jme.getMatchEntity().getDriverUndertakesRideEntity().getCustId().getCustId();
        // see who calls us!
     
        if (!(caller.getCustId().equals(driverId))) {
            throw new Error("Cannot proceed to accept rider, driver Id " + driverId + " does not match caller id " + caller.getCustId());
        }


        //
        // With all the checking done, we can possibly proceed to 
        // finally accept the rider
        //

        Integer rideId = null;
        try {
            rideId = jme.getMatchEntity().getDriverUndertakesRideEntity().getRideId();
        } catch (Exception exc) {
            throw new Error("Cannot proceed to accept rider, Unexpected Error while determining ride Id ", exc);
        }

        Integer riderrouteId = null;
        try {
            riderrouteId = jme.getMatchEntity().getRiderUndertakesRideEntity().getRiderrouteId();
        } catch (Exception exc) {
            throw new Error("Cannot proceed to accept rider, Unexpected Error while determining riderroute Id ", exc);
        }


        MatchEntity me = this.lookupDriverUndertakesRideControllerBeanLocal().acceptRider(rideId, riderrouteId);

        // bad case may happen, if so return false
        if (me == null) {
            return false;
        }
        // update the Match Entity
        jme.setMatchEntitiy(me);

        // add drive to ride if both parties have agreed
        this.addRiderToRideSafely(me);

        return true;
    }

    /**
     * Accept the driver for this match savely.
     *
     * As it is up to the rider of a ride to accept the driver, we do some
     * extensive checking about whether or not the driver is allowed to call
     *
     * @param jme -- the Matching entity to be updated
     * @return true, if setting the matching has works. Else false.
     */
    public boolean acceptDriverSafely(JMatchingEntity jme) {


        log.log(Level.FINE, "" + this.getClass() + " acceptDriverSafely beeing called");

        // see who calls us!
        CustomerEntity caller = this.getCustomerEntity();

        if (caller == null) {
            throw new Error("Cannot proceed to accept driver, Caller is null");
        }

        if (caller.getCustId() == null) {
            throw new Error("Cannot proceed to accept driver, CallerId is null");
        }


        // determine the rider of the match entity's trip

        Integer riderId = jme.getMatchEntity().getRiderUndertakesRideEntity().getCustId().getCustId();


        if (!(caller.getCustId().equals(riderId))) {
            throw new Error("Cannot proceed to accept Driver, rider Id " + riderId + " does not match caller id " + caller.getCustId());
        }


        //
        // With all the checking done, we can possibly proceed to 
        // finally accept the rider
        //

        Integer rideId = null;
        try {
            rideId = jme.getMatchEntity().getDriverUndertakesRideEntity().getRideId();
        } catch (Exception exc) {
            throw new Error("Cannot proceed to accept driver, Unexpected Error while determining ride Id ", exc);
        }

        Integer riderrouteId = null;
        try {
            riderrouteId = jme.getMatchEntity().getRiderUndertakesRideEntity().getRiderrouteId();
        } catch (Exception exc) {
            throw new Error("Cannot proceed to accept driver, Unexpected Error while determining riderroute Id ", exc);
        }


        MatchEntity me = this.lookupDriverUndertakesRideControllerBeanLocal().acceptDriver(rideId, riderrouteId);

        // bad case may happen, if so return false
        if (me == null) {

            log.log(Level.SEVERE, "" + this.getClass() + " acceptDriver failed ");
            return false;
        }

        log.log(Level.FINE, "" + this.getClass() + " acceptDriver succeded ");

        // update the Match Entity
        jme.setMatchEntitiy(me);
        // add drive to ride if both parties have agreed
        this.addRiderToRideSafely(me);

        return true;
    }

    /**
     * reject the driver for this match savely.
     *
     * As it is up to the rider of a ride to accept the driver, we do some
     * extensive checking about whether or not the driver is allowed to call
     *
     * @param jme -- the Matching entity to be updated
     * @return true, if setting the matching has works. Else false.
     */
    public boolean rejectDriverSafely(JMatchingEntity jme) {


        log.log(Level.FINE, "" + this.getClass() + " rejectDriverSafely beeing called");

        // see who calls us!
        CustomerEntity caller = this.getCustomerEntity();

        if (caller == null) {
            throw new Error("Cannot proceed to reject driver, Caller is null");
        }

        if (caller.getCustId() == null) {
            throw new Error("Cannot proceed to reject driver, CallerId is null");
        }


        // determine the rider of the match entity's trip

        Integer riderId = jme.getMatchEntity().getRiderUndertakesRideEntity().getCustId().getCustId();


        if (!(caller.getCustId().equals(riderId))) {
            throw new Error("Cannot proceed to reject Driver, rider Id " + riderId + " does not match caller id " + caller.getCustId());
        }


        //
        // With all the checking done, we can possibly proceed to 
        // finally accept the rider
        //

        Integer rideId = null;
        try {
            rideId = jme.getMatchEntity().getDriverUndertakesRideEntity().getRideId();
        } catch (Exception exc) {
            throw new Error("Cannot proceed to reject driver, Unexpected Error while determining ride Id ", exc);
        }

        Integer riderrouteId = null;
        try {
            riderrouteId = jme.getMatchEntity().getRiderUndertakesRideEntity().getRiderrouteId();
        } catch (Exception exc) {
            throw new Error("Cannot proceed to reject driver, Unexpected Error while determining riderroute Id ", exc);
        }


        MatchEntity me = this.lookupDriverUndertakesRideControllerBeanLocal().rejectDriver(rideId, riderrouteId);

        // bad case may happen, if so return false
        if (me == null) {

            log.log(Level.SEVERE, "" + this.getClass() + " rejectDriver failed ");
            return false;
        }

        log.log(Level.FINE, "" + this.getClass() + " rejectDriver succeded ");

        // update the Match Entity
        jme.setMatchEntitiy(me);

        return true;
    } // reject driver safely

    /**
     * Check, if both rider and driver have accepted, and if so, link
     * RideruntertakesRideEntity to DriverUndertakesRideEntity.
     *
     *
     * @param me MatchEntity linking drive to ride
     */
    protected void addRiderToRideSafely(MatchEntity me) {


        if (MatchEntity.ACCEPTED.equals(me.getRiderState())
                && MatchEntity.ACCEPTED.equals(me.getDriverState())) {

            RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();

            rurcl.addRiderToRide(
                    me.getRiderUndertakesRideEntity().getRiderrouteId(),
                    me.getDriverUndertakesRideEntity().getRideId());

        }

    } // add rider to ride

    /**
     * Reject the rider for this match savely.
     *
     * As it is up to the driver of a ride to accept the rider, we do some
     * extensive checking about wether or not the driver is allowed to call
     *
     * @param jme -- the Matching entity to be updated
     * @return true, if setting the matching has works. Else false.
     */
    public boolean rejectRiderSafely(JMatchingEntity jme) {

        log.log(Level.FINE, "" + this.getClass() + " rejectRiderSafely beeing called");


        // see who calls us!
        CustomerEntity caller = this.getCustomerEntity();

        if (caller == null) {
            throw new Error("Cannot proceed to reject rider, Caller is null");
        }

        if (caller.getCustId() == null) {
            throw new Error("Cannot proceed to reject rider, CallerId is null");
        }


        // determine the driver of the match entity's trip

        Integer driverId = jme.getMatchEntity().getDriverUndertakesRideEntity().getCustId().getCustId();


        if (!(caller.getCustId().equals(driverId))) {
            throw new Error("Cannot proceed to reject rider, driver Id " + driverId + " does not match caller id " + caller.getCustId());
        }


        //
        // With all the checking done, we can possibly proceed to 
        // finally accept the rider
        //

        Integer rideId = null;
        try {
            rideId = jme.getMatchEntity().getDriverUndertakesRideEntity().getRideId();
        } catch (Exception exc) {
            throw new Error("Cannot proceed to reject rider, Unexpected Error while determining ride Id ", exc);
        }

        Integer riderrouteId = null;
        try {
            riderrouteId = jme.getMatchEntity().getRiderUndertakesRideEntity().getRiderrouteId();
        } catch (Exception exc) {
            throw new Error("Cannot proceed to reject rider, Unexpected Error while determining riderroute Id ", exc);
        }


        MatchEntity me = this.lookupDriverUndertakesRideControllerBeanLocal().rejectRider(rideId, riderrouteId);

        // bad case may happen, if so return false
        if (me == null) {
            return false;
        }
        // update the Match Entity
        jme.setMatchEntitiy(me);

        return true;


    } //reject rider safely

    /**
     * Savely update match for the Drive/Ride combination given by driveId,
     * rideId. Savely means, that the caller is checked to be either driver or
     * rider of the respective rideOffer. If the caller is not the driver, an
     * Error will be thrown.
     *
     *
     * @param rideId the ride Id of the respective rideOffer
     * @param riderrouteId the id of the matching drive
     *
     */
    public MatchEntity getMatchSafely(int rideId, int riderrouteId) {

        CustomerEntity caller = this.getCustomerEntity();

        log.log(Level.FINE, "" + this.getClass() + " getMatchSavely rideId " + rideId + " riderrouteId : " + riderrouteId);

        MatchEntity me = this.lookupRiderUndertakesRideControllerBeanLocal().getMatch(rideId, riderrouteId);

        // there  a r e  no matches... can finish here
        if (me == null) {
            log.log(Level.FINE, "" + this.getClass() + " returning null prematuralely ");
            return null;
        }


        // check if either caller is either driver or rider
        boolean callerMatch = false;


        // retrieve and catch the rider
        try {
            if (caller.getCustId().equals(me.getRiderUndertakesRideEntity().getCustId().getCustId())) {
                callerMatch = true;
            }
        } catch (java.lang.NullPointerException exc) {
            log.severe("Error while determining rider for MatchEntity " + exc);
        }


        // retrieve the driver

        try {
            if (caller.getCustId().equals(me.getDriverUndertakesRideEntity().getCustId().getCustId())) {
                callerMatch = true;
            }
        } catch (java.lang.NullPointerException exc) {
            log.severe("Error while determining driver for MatchEntity " + exc);
        }

        if (!(callerMatch)) {
            throw new Error("Caller is neither driver nor rider for this ride, will not return matchEntity");
        }


        return me;

    } // getMatchSafely

    void setDriverMessageSafely(MatchEntity meArg, String message) {

        Integer rideId = meArg.getDriverUndertakesRideEntity().getRideId();
        Integer riderRouteId = meArg.getRiderUndertakesRideEntity().getRiderrouteId();
        // retrive a matchEntity safely
        MatchEntity me = this.getMatchSafely(rideId, riderRouteId);
        // see, if caller is driver
        CustomerEntity caller = this.getCustomerEntity();
        Integer matchingDriverId=me.getDriverUndertakesRideEntity().getCustId().getCustId();
       
        if (!matchingDriverId.equals(caller.getCustId())) {
            throw new Error("Only Driver may change Driver's Message!");
        }

        // now, set the message savely...
        this.lookupDriverUndertakesRideControllerBeanLocal().setDriverMessage(rideId, riderRouteId, message);
    }

    void setRiderMessageSafely(MatchEntity meArg,String message) {


        Integer rideId = meArg.getDriverUndertakesRideEntity().getRideId();
        Integer riderRouteId = meArg.getRiderUndertakesRideEntity().getRiderrouteId();
        // retrive a matchEntity safely
        MatchEntity me = this.getMatchSafely(rideId, riderRouteId);
        Integer matchingRiderId=me.getRiderUndertakesRideEntity().getCustId().getCustId();
       
        // see, if caller is rider
        CustomerEntity caller = this.getCustomerEntity();
        if (! (matchingRiderId.equals(caller.getCustId()))) {
            throw new Error("Only Rider may change Rider's Message!");
        }
        // now, set the message safely
        this.lookupDriverUndertakesRideControllerBeanLocal().setRiderMessage(rideId, riderRouteId, message);
       
    }
    
    
    
    
      void countermandSafely(MatchEntity meArg) {


        Integer rideId = meArg.getDriverUndertakesRideEntity().getRideId();
        Integer riderRouteId = meArg.getRiderUndertakesRideEntity().getRiderrouteId();
        // retrive a matchEntity safely
        MatchEntity me = this.getMatchSafely(rideId, riderRouteId);
        Integer matchingRiderId=me.getRiderUndertakesRideEntity().getCustId().getCustId();
      
        Integer matchingDriverId=me.getDriverUndertakesRideEntity().getCustId().getCustId();
        
        RiderUndertakesRideControllerLocal rurcl=this.lookupRiderUndertakesRideControllerBeanLocal();
      
         // see, if caller is rider, and if so, countermand as rider
        CustomerEntity caller = this.getCustomerEntity();
        if (matchingRiderId.equals(caller.getCustId())) {
            rurcl.countermandRider(
                    me.getRiderUndertakesRideEntity().getRideId().getRideId(),
                    me.getRiderUndertakesRideEntity().getRiderrouteId()
                    );
            return;
        }
        
        
          // see, if caller is rider, and if so, countermand as rider
        if (matchingDriverId.equals(caller.getCustId())) {
            rurcl.countermandDriver(
                    me.getRiderUndertakesRideEntity().getRideId().getRideId(),
                    me.getRiderUndertakesRideEntity().getRiderrouteId()
                    );
            return;
        }
        
      }

  
      
 
} //class
