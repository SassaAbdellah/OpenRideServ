/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.auxiliary;

import de.avci.joride.jbeans.riderundertakesride.JRiderUndertakesRideEntity;
import de.fhg.fokus.openride.customerprofile.CustomerControllerLocal;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Service to fetch classified information about rides and cast them into public
 * information on ratings
 *
 *
 * @author jochen
 */
public class JRatingService {

    transient Logger log = Logger.getLogger(this.getClass().getCanonicalName());

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

    private CustomerControllerLocal lookupCustomerControllerBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerControllerLocal) c.lookup("java:global/OpenRideServer/OpenRideServer-ejb/CustomerControllerBean!de.fhg.fokus.openride.customerprofile.CustomerControllerLocal");
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
     * Get a list of all ride request for customer in between startdate and an
     * enddate. StartDate and EndDate are read in from http parameters using
     * TimeIntervalBean
     *
     * This is sensitive data, and may not be presented to unprivileged users.
     * This is here only to assist in creating a list of all *ratings*. Hence,
     * this is private and shall never be made public.
     *
     *
     *
     * @return
     * 
     */
    private List<JRiderUndertakesRideEntity> getRidesForRiderInInterval(CustomerEntity ce) {


        if (ce == null) {
            throw new Error("Cannot determine Rides, customerEntity is null");
        }

        if (ce.getCustNickname() == null) {
            throw new Error("Cannot determine Rides, customerNickname is null");
        }



        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();


        // retrieve startDateAndEndDate

        String param = new RideSearchParamsBean().getBeanNameRidesearchparam();
        RideSearchParamsBean tb = new RideSearchParamsBean().retrieveCurrentTimeInterval(param);

        log.log(Level.WARNING, "Updated Time Interval " + tb.getStartDateFormatted() + " -> " + tb.getEndDateFormatted());


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
     * Get a list of all rides for the actual Customer in between startdate and
     * an enddate
     *
     * StartDate and EndDate are read in from http parameters using
     * TimeIntervalBean
     *
     *
     * This is sensitive data, and may not be presented to unprivileged users.
     * This is here only to assist in creating a list of all *ratings*. Hence,
     * this is private and shall never be made public.
     *
     *
     *
     * @return
     */
    public List<JRiderUndertakesRideEntity> getRidesForDriverInInterval(CustomerEntity ce) {



        if (ce == null) {
            throw new Error("Cannot determine Rides, customerEntity is null");
        }


        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();


        if (ce.getCustNickname() == null) {
            throw new Error("Cannot determine Rides, customerNickname is null");
        }


        // retrieve startDateAndEndDate

        String param = new RideSearchParamsBean().getBeanNameRidesearchparam();
        RideSearchParamsBean tb = new RideSearchParamsBean().retrieveCurrentTimeInterval(param);

        log.log(Level.FINE, "Updated Time Interval " + tb.getStartDateFormatted() + " -> " + tb.getEndDateFormatted());


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


        log.log(Level.FINE, "" + this.getClass() + "getRidesForDriverInInterval returning " + res.size() + " results");
        return res;

    } // getRidesForRiderInInterval

    /**
     * Return a list of all ratings the user given by custId has received in
     * timespan transported seperately by TimeIntervalBean.
     *
     * @param custId customerId of customer to get Ratings for
     * @param startDate beginning of interval
     * @param endData end of interval
     * @return
     */
    public List<JRatingBean> getRatingsAsDriver(Integer custId, Date startDate, Date endDate) {


        CustomerControllerLocal ccl = this.lookupCustomerControllerBeanLocal();


        CustomerEntity ce = ccl.getCustomer(custId);

        if (ce == null) {
            throw new Error("Cannot determine Identity for customerId " + custId);
        }

        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();

        // get all rides related to this customer
        List<RiderUndertakesRideEntity> res1 =
                rurcl.getRidesForDriver(
                ce,
                startDate,
                endDate);

        // cast rideinfo to ratings and return list of ratings
        List<JRatingBean> res = new LinkedList<JRatingBean>();

        for (RiderUndertakesRideEntity rue : res1) {
            JRatingBean jrb = JRatingBean.extractRating(rue);
            res.add(jrb);
        }

        return res;

    } // getRatings as Driver

    /**
     * Return a list of all ratings the user given by custId has received in
     * timespan transported seperately by TimeIntervalBean.
     *
     * @param custId customerId of customer to get Ratings for
     * @param startDate beginning of interval
     * @param endData end of interval
     * @return
     */
    public List<JRatingBean> getRatingsAsRider(Integer custId, Date startDate, Date endDate) {


        CustomerControllerLocal ccl = this.lookupCustomerControllerBeanLocal();


        CustomerEntity ce = ccl.getCustomer(custId);

        if (ce == null) {
            throw new Error("Cannot determine Identity for customerId " + custId);
        }

        RiderUndertakesRideControllerLocal rurcl = this.lookupRiderUndertakesRideControllerBeanLocal();

        // get all rides related to this customer
        List<RiderUndertakesRideEntity> res1 =
                rurcl.getRidesForCustomer(
                ce,
                startDate,
                endDate);

        // cast rideinfo to ratings and return list of ratings
        List<JRatingBean> res = new LinkedList<JRatingBean>();

        for (RiderUndertakesRideEntity rue : res1) {
            JRatingBean jrb = JRatingBean.extractRating(rue);
            res.add(jrb);
        }

        return res;

    } // getRatings as Rider
} // class

