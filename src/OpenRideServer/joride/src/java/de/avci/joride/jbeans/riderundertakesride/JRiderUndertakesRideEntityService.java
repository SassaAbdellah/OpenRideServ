/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.riderundertakesride;

import de.avci.joride.jbeans.auxiliary.RideSearchParamsBean;
import de.avci.joride.jbeans.customerprofile.JCustomerEntityService;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

/**
 * Wrapper for RiderUndertakesRideEntityService in OpenRideServer-ejb.
 *
 * @author jochen
 */
public class JRiderUndertakesRideEntityService {

   transient Logger log = Logger.getLogger(this.getClass().getCanonicalName());

    /**
     * Get a customerEntity from the current request
     *
     * @return
     */
    public CustomerEntity getCustomerEntity() {
        return (new JCustomerEntityService()).getCustomerEntitySafely();
    }

    /**
     * Lookup RiderUndertakesRideControllerLocal Bean that controls my requests.
     *
     * @return
     */
    public RiderUndertakesRideControllerLocal lookupRiderUndertakesRideControllerBeanLocal() {
        try {

            javax.naming.Context c = new InitialContext();
            Object o = c.lookup("java:global/OpenRideServer/OpenRideServer-ejb/RiderUndertakesRideControllerBean!de.fhg.fokus.openride.rides.rider.RiderUndertakesRideControllerLocal");
            if (o == null) {
                throw new Error("Error while looking up RiderUndertakesRideControllerLocal, lookup result is null. ");
            }


            return (RiderUndertakesRideControllerLocal) o;
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }

    }

    /**
     * Get a list all Drives of this driver. Current user/customer is determined
     * from HTTPRequest's AuthPrincipal.
     *
     * @return
     */
    public List<JRiderUndertakesRideEntity> getRidesForRider() {


        CustomerEntity ce = this.getCustomerEntity();
        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();


        if (ce == null) {
            throw new Error("Cannot determine Rides, customerEntity is null");
        }

        if (ce.getCustNickname() == null) {
            throw new Error("Cannot determine Rides, customerNickname is null");
        }


        // get all rides related to this customer
        List<RiderUndertakesRideEntity> res1 = rurcl.getRidesForCustomer(ce);

        // cast them to JRiderUntertakesRideEntity
        List<JRiderUndertakesRideEntity> res = new LinkedList<JRiderUndertakesRideEntity>();

        for (RiderUndertakesRideEntity rure : res1) {

            JRiderUndertakesRideEntity jrure = new JRiderUndertakesRideEntity();
            jrure.updateFromRiderUndertakesRideEntity(rure);

            res.add(jrure);
        }

        return res;


    }

    /**
     * Get a list of all rides for the actual Rider in between startdate and an
     * enddate
     *
     * StartDate and EndDate are read in from http parameters using
     * TimeIntervalBean
     *
     *
     * @return
     */
    public List<JRiderUndertakesRideEntity> getRidesForRiderInInterval() {

        CustomerEntity ce = this.getCustomerEntity();
        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();

        if (ce == null) {
            throw new Error("Cannot determine Rides, customerEntity is null");
        }

        if (ce.getCustNickname() == null) {
            throw new Error("Cannot determine Rides, customerNickname is null");
        }


        // retrieve startDateAndEndDate

        String param = new RideSearchParamsBean().getBeanNameRidesearchparam();
        RideSearchParamsBean tb = new RideSearchParamsBean().retrieveCurrentTimeInterval(param);

        log.log(Level.WARNING,"Updated Time Interval " + tb.getStartDateFormatted() + " -> " + tb.getEndDateFormatted());


        // get all rides related to this customer
        List<RiderUndertakesRideEntity> res1 =
                rurcl.getRidesForCustomer(                                                                                                                                            
                ce,
                tb.getStartDate(),
                tb.getEndDate());

        // cast them to JRiderUntertakesRideEntity
        List<JRiderUndertakesRideEntity> res = new LinkedList<JRiderUndertakesRideEntity>();

        for (RiderUndertakesRideEntity rure : res1) {

            JRiderUndertakesRideEntity jrure = new JRiderUndertakesRideEntity();
            jrure.updateFromRiderUndertakesRideEntity(rure);

            res.add(jrure);
        }

        return res;

    } // getRidesForRiderInInterval

    /**
     * Get a list of all rides for the actual Rider in between startdate and an
     * enddate
     *
     * StartDate and EndDate are read in from http parameters using
     * TimeIntervalBean
     *
     *
     * @return
     */
    public List<JRiderUndertakesRideEntity> getRidesForDriverInInterval() {

        CustomerEntity ce = this.getCustomerEntity();
        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();

        if (ce == null) {
            throw new Error("Cannot determine Rides, customerEntity is null");
        }

        if (ce.getCustNickname() == null) {
            throw new Error("Cannot determine Rides, customerNickname is null");
        }


        // retrieve startDateAndEndDate

        String param = new RideSearchParamsBean().getBeanNameRidesearchparam();
        RideSearchParamsBean tb = new RideSearchParamsBean().retrieveCurrentTimeInterval(param);

        log.log(Level.FINE,"Updated Time Interval " + tb.getStartDateFormatted() + " -> " + tb.getEndDateFormatted());


        // get all rides related to this customer
        List<RiderUndertakesRideEntity> res1 =
                rurcl.getRidesForDriver(
                ce,
                tb.getStartDate(),
                tb.getEndDate());

        // cast them to JRiderUntertakesRideEntity
        List<JRiderUndertakesRideEntity> res = new LinkedList<JRiderUndertakesRideEntity>();

        for (RiderUndertakesRideEntity rure : res1) {

            JRiderUndertakesRideEntity jrure = new JRiderUndertakesRideEntity();
            jrure.updateFromRiderUndertakesRideEntity(rure);

            res.add(jrure);
        }


        log.log(Level.FINE,"" + this.getClass() + "getRidesForDriverInInterval returning " + res.size() + " results");

        return res;

    } // getRidesForRiderInInterval

    /**
     * Get a list of all **realized** rides for the actual Rider in between
     * startdate and an enddate
     *
     * StartDate and EndDate are read in from http parameters using
     * TimeIntervalBean
     *
     *
     * @return
     */
    public List<JRiderUndertakesRideEntity> getRealizedRidesForRiderInInterval() {

        CustomerEntity ce = this.getCustomerEntity();
        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();

        if (ce == null) {
            throw new Error("Cannot determine Rides, customerEntity is null");
        }

        if (ce.getCustNickname() == null) {
            throw new Error("Cannot determine Rides, customerNickname is null");
        }


        // retrieve startDateAndEndDate

        String param = new RideSearchParamsBean().getBeanNameRidesearchparam();
        RideSearchParamsBean tb = new RideSearchParamsBean().retrieveCurrentTimeInterval(param);

       log.log(Level.FINE,"Updated Time Interval " + tb.getStartDateFormatted() + " -> " + tb.getEndDateFormatted());


        // get all rides related to this customer
        List<RiderUndertakesRideEntity> res1 =
                rurcl.getRealizedRidesForRider(
                ce,
                tb.getStartDate(),
                tb.getEndDate());

        // cast them to JRiderUntertakesRideEntity
        List<JRiderUndertakesRideEntity> res = new LinkedList<JRiderUndertakesRideEntity>();

        for (RiderUndertakesRideEntity rure : res1) {

            JRiderUndertakesRideEntity jrure = new JRiderUndertakesRideEntity();
            jrure.updateFromRiderUndertakesRideEntity(rure);

            res.add(jrure);
        }

        return res;

    } // getRealizedRidesForRiderInInterval
    
    
    
    /**
     * Get a list of all  rides for the actual Rider in that 
     * have starttime latest after NOW
     *
     *
     *
     * @return
     */
    public List<JRiderUndertakesRideEntity> getFutureRidesForRider() {

        CustomerEntity ce = this.getCustomerEntity();
        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();

        if (ce == null) {
            throw new Error("Cannot determine Rides, customerEntity is null");
        }

        if (ce.getCustNickname() == null) {
            throw new Error("Cannot determine Rides, customerNickname is null");
        }

        // get all rides related to this customer
        List<RiderUndertakesRideEntity> res1 =
                rurcl.getRidesForRiderAfterDate(ce, new Date());
                

        // cast them to JRiderUntertakesRideEntity
        List<JRiderUndertakesRideEntity> res = new LinkedList<JRiderUndertakesRideEntity>();

        for (RiderUndertakesRideEntity rure : res1) {

            JRiderUndertakesRideEntity jrure = new JRiderUndertakesRideEntity();
            jrure.updateFromRiderUndertakesRideEntity(rure);

            res.add(jrure);
        }

        return res;

    } // getRidesForRiderAfter
    
    /** Create a filtered ride list of all future rides for this rider
     * 
     * @param jrfl typically empty object to be initiallized by this method
     */
    public void updateJFilteredRideList(JRideFilteredLists jrfl){
        jrfl.setAllRides(this.getFutureRidesForRider());
    }
   
    
    /** List unrated rides for Rider with pickup time earlier then now (System.currentTimeMillis())
     */
    public List<JRiderUndertakesRideEntity> getUnratedRidesForRider() {
  
    	return this.getUnratedRidesForRiderInInterval(new Date(0), new Date(System.currentTimeMillis()));
    }
    
    
    /** List rides that are not rated by rider in interval given by 
     *  rideSearchParamBean.
     * 
     * @return
     */
 
    public List<JRiderUndertakesRideEntity> getUnratedRidesForRiderInInterval() {

    	// retrieve startDateAndEndDate

        String param = new RideSearchParamsBean().getBeanNameRidesearchparam();
        RideSearchParamsBean tb = new RideSearchParamsBean().retrieveCurrentTimeInterval(param);

        log.log(Level.FINE,"Updated Time Interval " + tb.getStartDateFormatted() + " -> " + tb.getEndDateFormatted());
    
        return getUnratedRidesForRiderInInterval(tb.getStartDate(), tb.getEndDate());
    
    }


    /** List rides that are not rated by rider in interval given by 
     *  startDate...endDate
     * 
     * @return
     */
   
    private List<JRiderUndertakesRideEntity> getUnratedRidesForRiderInInterval(Date startDate, Date endDate) {
    	
        CustomerEntity ce = this.getCustomerEntity();
        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();

        if (ce == null) {
            throw new Error("Cannot determine Rides, customerEntity is null");
        }

        if (ce.getCustNickname() == null) {
            throw new Error("Cannot determine Rides, customerNickname is null");
        }

        // get all rides related to this customer
        List<RiderUndertakesRideEntity> res1 =
                rurcl.getUnratedRidesForRider(
                ce,
                startDate,
                endDate
                );

        // cast them to JRiderUntertakesRideEntity
        List<JRiderUndertakesRideEntity> res = new LinkedList<JRiderUndertakesRideEntity>();

        for (RiderUndertakesRideEntity rure : res1) {

            JRiderUndertakesRideEntity jrure = new JRiderUndertakesRideEntity();
            jrure.updateFromRiderUndertakesRideEntity(rure);

            res.add(jrure);
        }

        return res;

    }
    
    
    
    /** List unrated rides for Driver with pickup time earlier then now (System.currentTimeMillis())
     */
    public List<JRiderUndertakesRideEntity> getUnratedRidesForDriver() {
  
    	return this.getUnratedRidesForDriverInInterval(new Date(0), new Date(System.currentTimeMillis()));
    }
    
    /** List rides that are not rated by driver in interval given by 
     *  rideSearchParamBean.
     * 
     * @return
     */
    public List<JRiderUndertakesRideEntity> getUnratedRidesForDriverInInterval() {

    	  String param = new RideSearchParamsBean().getBeanNameRidesearchparam();
          RideSearchParamsBean tb = new RideSearchParamsBean().retrieveCurrentTimeInterval(param);

          log.log(Level.FINE,"Updated Time Interval " + tb.getStartDateFormatted() + " -> " + tb.getEndDateFormatted());

          return getUnratedRidesForDriverInInterval(tb.getStartDate(), tb.getEndDate());
    }
    
    
    /** List rides that are not rated by driver in interval given by 
     *  startDate...endDate
     * 
     * @return
     */
    private List<JRiderUndertakesRideEntity> getUnratedRidesForDriverInInterval(Date startDate, Date endDate) {
    	
        CustomerEntity ce = this.getCustomerEntity();
        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();

        if (ce == null) {
            throw new Error("Cannot determine Rides, customerEntity is null");
        }

        if (ce.getCustNickname() == null) {
            throw new Error("Cannot determine Rides, customerNickname is null");
        }

        // get all rides related to thisdriver
        List<RiderUndertakesRideEntity> res1 = rurcl.getUnratedRidesForDriver( ce,  startDate, endDate);

        // cast them to JRiderUntertakesRideEntity
        List<JRiderUndertakesRideEntity> res = new LinkedList<JRiderUndertakesRideEntity>();

        for (RiderUndertakesRideEntity rure : res1) {

            JRiderUndertakesRideEntity jrure = new JRiderUndertakesRideEntity();
            jrure.updateFromRiderUndertakesRideEntity(rure);

            res.add(jrure);
        }

        return res;

    }

    
    

    /**
     * Savely get the Ride with given riderRouteId.
     *
     * Current user/customer is determined from HTTPRequest's AuthPrincipal.
     *
     * @return
     */
    public RiderUndertakesRideEntity getRideByRiderRouteIdSavely(int myRiderRouteId) {


        CustomerEntity ce = this.getCustomerEntity();
        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();


        if (ce == null) {
            throw new Error("Cannot determine Ride, customerEntity is null");
        }

        if (ce.getCustNickname() == null) {
            throw new Error("Cannot determine Ride, customerNickname is null");
        }


        RiderUndertakesRideEntity rure = rurcl.getRideByRiderRouteId(myRiderRouteId);


        // check if the caller is either rider if driver for this ride
        boolean stakeholder = false;

        // Rider is stakeholder
        if (rure.getCustId().getCustId() == ce.getCustId()) {
            stakeholder = true;
        }

        // if there is an accepted offer for this ride, then the driver
        // should be considered being a stakeholder true

        if (!stakeholder) {
            if (rure.getRideId() != null) {
                if (ce.getCustId() == (rure.getRideId().getCustId().getCustId())) {
                    stakeholder = true;
                }
            } // (rure.getRideId()!=null)
        } // (!stakeholder


        if (!stakeholder) {
            throw new Error("Cannot retrieve Drive with given ID, object does not belong to user");
        }



        return rure;

    } //  getDriveByIdSavely(int id)

    /**
     * Savely update JRiderUndertakesRideEntity from database
     *
     *
     *
     * @param riderRouteId ride Id from the DriverUndertakesRideEntity providing
     * the data. If this is null, simply no update will be done.
     *
     * @param jdure JDriverUndertakesRideEntity to be updated with data from
     * database
     */
    public void updateJRiderUndertakesRideEntityByRiderRouteIDSavely(Integer riderRouteId, JRiderUndertakesRideEntity jrure) {


        if (riderRouteId == null) {
            // nothing to do in that case!
            return;
        }


        RiderUndertakesRideEntity rure = this.getRideByRiderRouteIdSavely(riderRouteId);
        // update
        jrure.updateFromRiderUndertakesRideEntity(rure);
    }

    /**
     * Add new RideRequest to the Database, generates and returns the ID of the
     * so created ride request.
     */
    public int addRideRequest(JRiderUndertakesRideEntity jrure) {


        CustomerEntity ce = this.getCustomerEntity();


        if (ce == null) {
            throw new Error("Cannot persist Rides, customerEntity is null");
        }

        if (ce.getCustId() == null) {
            throw new Error("Cannot determine Rides, customerId is null");
        }

        // null comments may cause nullpointer trouble, so clean it here
        jrure.cleanseComment();
        // null prices may cause nullpointer trouble, so clean it here
        jrure.cleansePrice();


        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();

        return rurcl.addRideRequest(
                // int cust_id, 
                ce.getCustId(),
                //Date starttime_earliest, 
                jrure.getStarttimeEarliest(),
                //Date starttimeLatest, 
                jrure.getStarttimeLatest(),
                // int noPassengers, 
                jrure.getNoPassengers(),
                //Point startpt, 
                jrure.getStartpt(),
                //Point endpt, 
                jrure.getEndpt(),
                //double price, 
                jrure.getPrice(),
                //String comment, 
                jrure.getComment(),
                //String startptAddress, 
                jrure.getStartptAddress(),
                //String endptAddress
                jrure.getEndptAddress());
    }

    /**
     * Return a list of *recent* rides, of this user i.e: Rides for which the
     * "lastStartTime" value is still in the future. and which are not booked.
     *
     * @return
     */
    public List<RiderUndertakesRideEntity> getActiveOpenRides() {


        CustomerEntity ce = this.getCustomerEntity();
        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();


        if (ce == null) {
            throw new Error("Cannot determine Rides, customerEntity is null");
        }


        if (ce.getCustNickname() == null) {
            throw new Error("Cannot determine Rides, customer's nickname is null");
        }


        return rurcl.getActiveOpenRides(ce.getCustNickname());

    } // getActiveOpenRides

    /**
     * Savely set givenRating for this ride. .
     *
     * Current user/customer is determined from HTTPRequest's AuthPrincipal, and
     * checked again the riderundertakesrideentity's custId.
     *
     * @return true, if the ride has been removed, else false.
     */
    public void setGivenRatingSavely(JRiderUndertakesRideEntity jrure) {




        log.log(Level.FINE,
                "Set givenRating for RiderrouteId : "
                + jrure.getRiderrouteId()
                + " Givenrating : "
                + jrure.getGivenrating()
                + " GivenRatingComment : "
                + jrure.getGivenratingComment());

        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();


        // see, if caller is equal to rider ***in the database**

        if (!(this.callerIsRider(jrure.getRiderrouteId()))) {
            throw new Error("Cannot rate ride, caller is not identical to owner of ride request!");
        }

        rurcl.setGivenRating(
                jrure.getRiderrouteId(),
                jrure.getGivenrating(),
                jrure.getGivenratingComment());

    }

    /**
     * Savely set receivedRating for this ride. .
     *
     * Current user/customer is determined from HTTPRequest's AuthPrincipal, and
     * checked again the riderundertakesrideentity's custId.
     *
     * @return true, if the ride has been removed, else false.
     */
    public void setReceivedRatingSavely(JRiderUndertakesRideEntity jrure) {




       log.log(Level.FINE,
                "Set receivedRating for RiderrouteId : "
                + jrure.getRiderrouteId()
                + " Receivedrating : "
                + jrure.getReceivedrating()
                + " ReceivedRatingComment : "
                + jrure.getReceivedratingComment());

        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();


        // see, if caller is equal to driver ***in the database**

        if (!(this.callerIsDriver(jrure.getRiderrouteId()))) {
            throw new Error("Cannot driver-rate ride, caller is not identical to driver of ride request!");
        }

        rurcl.setReceivedRating(
                jrure.getRiderrouteId(),
                jrure.getReceivedrating(),
                jrure.getReceivedratingComment());

    }

    /**
     * Savely remove the Ride with given riderRouteId. Current user/customer is
     * determined from HTTPRequest's AuthPrincipal.
     *
     * @return true, if the ride has been removed, else false.
     */
    public boolean removeRideSafely(JRiderUndertakesRideEntity jrure) {


        CustomerEntity ce = this.getCustomerEntity();

        if (ce == null) {
            throw new Error("Cannot determine Ride for removal, calling customerEntity is null");
        }


        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();

        if (rurcl == null) {
            throw new Error("Cannot determine RiderUndertakesRideControllerLocal");
        }

        if (jrure == null) {
            throw new Error("Cannot remove ride, argument is null");
        }

        if (jrure.getRiderrouteId() == null) {
            throw new Error("Cannot remove ride, riderrouteId is null");
        }

        int riderrouteId = jrure.getRiderrouteId();
        RiderUndertakesRideEntity rure = rurcl.getRideByRiderRouteId(riderrouteId);

        if (rure == null) {
            throw new Error("Cannot remove ride with id " + jrure.getRiderrouteId() + ", ride is null!");
        }

        if (rure.getCustId().getCustId() != ce.getCustId()) {
            throw new Error("Cannot retrieve Ride with given ID for removal, object does not belong to user");
        }

        return rurcl.removeRide(rure.getRiderrouteId());

    } //  getDriveByIdSavely(int id)

    /**
     * Return a list of *recent* rides, of this user i.e: Rides for which the
     * "lastStartTime" value is still in the future. and which are not booked.
     * The user gets determined from the HttpServletRequest's remoteUser, thus
     * this method can be considered to be save.
     *
     * @return
     */
    public List<RiderUndertakesRideEntity> getActiveOpenRides(HttpServletRequest request) {

        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();
        return rurcl.getActiveOpenRides(request.getRemoteUser());

    } // getActiveOpenRides

    /**
     * Returns true, if the ride given by riderrouteId has been updated, else
     * false. This is just a small wrapper to
     * RiderUndertakesRideControllerBeanLocal.isRideUpdated(...)
     *
     * @param riderrouteId
     * @return
     */
    public boolean isRideUpdated(Integer riderrouteId) {

        // avoid trivial cases...
        if (riderrouteId == null) {
            return false;
        }

        RiderUndertakesRideControllerLocal rurcl = lookupRiderUndertakesRideControllerBeanLocal();
        return rurcl.isRideUpdated(riderrouteId);
    }

    /**
     * Invalidate/cancel/countermand the ride with given Rideid. The identity is
     * checked from http request.
     *
     * @param rideId id of the ride to be invalidated.
     *
     * @return true if ride was invalidated, else false.
     */
    public boolean invalidateRequestSavely(int riderrouteId) {


        CustomerEntity ce = this.getCustomerEntity();


        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();
        RiderUndertakesRideEntity rue = rurcl.getRideByRiderRouteId(riderrouteId);

        // Sanity check, caller of this method must be owner of this offer

        if (ce.getCustId() != rue.getCustId().getCustId()) {
            throw new Error("Attempt to invalidate request that is not owned by User");
        }

        if (rurcl.isRemovable(riderrouteId)) {
            log.info("Offer " + riderrouteId + " is deleteable, removing it");
            rurcl.removeRide(riderrouteId);
        } else {
            log.info("Request " + riderrouteId + " is not deleteable, invalidating");
            rurcl.invalidateRide(riderrouteId);
        }

        return true;
    }

    /**
     * Determines wether the caller is the rider of this request, and if so,
     * returns true, else false.
     *
     *
     *
     * @param riderrouteId id of the ride to be tested
     * @return true, if HTTP Request's AuthPrincipal is owner of this request,
     * else false
     */
    public boolean callerIsRider(int riderrouteId) {



        CustomerEntity ce = this.getCustomerEntity();
        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();
        RiderUndertakesRideEntity rue = rurcl.getRideByRiderRouteId(riderrouteId);



        if (null == ce) {
            throw new Error("Cannot determine rider, customerEntity is null");
        }

        if (null == rue) {
            throw new Error("Cannot determine rider property, RiderUndertakesRideEntity is null");
        }

        if (null == rue.getCustId()) {
            throw new Error("Cannot determine rider property, RiderUndertakesRideEntity has no custId");
        }


        if (ce.getCustId() == rue.getCustId().getCustId()) {
            return true;
        }

        return false;

    } // callerIsRider

    /**
     * Determines wether the caller is the driver of this request, and if so,
     * returns true, else false.
     *
     *
     *
     * @param riderrouteId id of the ride to be tested
     * @return true, if HTTP Request's AuthPrincipal is driver of this request,
     * else false
     */
    public boolean callerIsDriver(int riderrouteId) {



        CustomerEntity ce = this.getCustomerEntity();
        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();
        RiderUndertakesRideEntity rure = rurcl.getRideByRiderRouteId(riderrouteId);

        if (rure.getRideId() != null) {
            if (ce.getCustId() == (rure.getRideId().getCustId().getCustId())) {
                return true;
            }
        } // (rure.getRideId()!=null)

        return false;


    } // callerIsDriver
} // class
