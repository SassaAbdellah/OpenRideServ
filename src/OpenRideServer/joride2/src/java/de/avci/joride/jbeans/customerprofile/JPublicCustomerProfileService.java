/**
 * Service to get and Put CustomerEntityBeans the EJB Way.
 *
 */
package de.avci.joride.jbeans.customerprofile;

import de.avci.joride.jbeans.riderundertakesride.JRiderUndertakesRideEntityService;
import de.fhg.fokus.openride.customerprofile.CustomerControllerLocal;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideControllerLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Service to get *public* CustomerProfiles from the database
 *
 *
 *
 * @author jochen
 *
 */
public class JPublicCustomerProfileService {

    Logger log = Logger.getLogger(this.getClass().getCanonicalName());
    CustomerControllerLocal customerControllerBean = lookupCustomerControllerBeanLocal();

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
     * Update given PublicCustomerProfile from CustomerProfile given by custId
     *
     * @param jcpp PublicCustomerProfile to be updated
     * @param custId CustomerId from which data should be updated.
     */
    public void updatePublicCustomerProfileFromID(JPublicCustomerProfile jcpp, int custIdArg) {

        CustomerControllerLocal ccl = this.lookupCustomerControllerBeanLocal();
        CustomerEntity ce = ccl.getCustomer(custIdArg);

        if (ce == null) {
            log.log(Level.WARNING, "Got empty customer profile when updating from id " + custIdArg);
            return;
        }

        jcpp.updateFromCustomerEntity(ce);
    }

    /**
     * Update given PublicCustomerProfile from CustomerProfile given by custId
     *
     * @param jcpp PublicCustomerProfile to be updated
     * @param nickName of customer for which data should be updated.
     */
    public void updatePublicCustomerProfileFromNickname(JPublicCustomerProfile jcpp, String nickName) {

        CustomerControllerLocal ccl = this.lookupCustomerControllerBeanLocal();
        CustomerEntity ce = ccl.getCustomerByNickname(nickName);

        if (ce == null) {
            return;
        }

        jcpp.updateFromCustomerEntity(ce);
    }

    /**
     * Determine caller from http request, then update given public profile from
     * data so obtained
     *
     *
     * @param jcpp the public profile to be updated.
     *
     */
    public void updateFromCallerPublicProfile(JPublicCustomerProfile jcpp) {

        Integer custId = new JCustomerEntityService().getCustIDSafely();

        if (custId == null) {
            log.log(Level.WARNING, "Got empty customer profile when updating");
            return;
        }

        this.updatePublicCustomerProfileFromID(jcpp, custId);
    }

    /**
     * Get Number of rider ratings for customer
     *
     * @param customerId customerId for customer to be rated
     * @return Average Rating, or null if something ugly happened
     */
    public Integer getRatingsCountAsRider(Integer customerId) {

        CustomerEntity ce = this.lookupCustomerControllerBeanLocal().getCustomer(customerId);

        if (ce == null) {
            log.log(Level.WARNING, "looking up customer for id " + customerId + " failed for getRatingsCountAsRider");
            return null;
        }

        try {
            RiderUndertakesRideControllerLocal rurcl = new JRiderUndertakesRideEntityService().lookupRiderUndertakesRideControllerBeanLocal();
            return rurcl.getCountOfRatingsForRider(ce);
        } catch (Exception exc) {
            log.log(Level.WARNING, "Unexpected Exception while retrieving driverRatingCount for customer " + customerId, exc);
            return null;
        }

    } // getRatingsCountAsRider 

    /**
     * Get Number of driver ratings for customer
     *
     * @param customerId customerId for customer to be rated
     * @return Average Rating, or null if something ugly happened
     */
    public Integer getRatingsCountAsDriver(Integer customerId) {

        CustomerEntity ce = this.lookupCustomerControllerBeanLocal().getCustomer(customerId);

        if (ce == null) {
            log.log(Level.WARNING, "looking up customer for id " + customerId + " failed for getRatingsCountAsDriver");
            return null;
        }

        try {
            RiderUndertakesRideControllerLocal rurcl = new JRiderUndertakesRideEntityService().lookupRiderUndertakesRideControllerBeanLocal();
            return rurcl.getCountOfRatingsForDriver(ce);
        } catch (Exception exc) {
            log.log(Level.WARNING, "Unexpected Exception while retrieving driverRatingCount for customer " + customerId, exc);
            return null;
        }

    } // getRatingsCountAsDriver 

    /**
     * Get sum of all driver ratings for customer
     *
     * @param customerId customerId for customer to be rated
     * @return Average Rating, or null if something ugly happened
     */
    public Integer getRatingsTotalAsDriver(Integer customerId) {

        CustomerEntity ce = this.lookupCustomerControllerBeanLocal().getCustomer(customerId);

        if (ce == null) {
            log.log(Level.WARNING, "looking up customer for id " + customerId + " failed for getRatingsToralAsDriver");
            return null;
        }

        try {
            RiderUndertakesRideControllerLocal rurcl = new JRiderUndertakesRideEntityService().lookupRiderUndertakesRideControllerBeanLocal();
            return rurcl.getTotalOfRatingsForDriver(ce);
        } catch (Exception exc) {
            log.log(Level.WARNING, "Unexpected Exception while retrieving driverRatingTotal for customer " + customerId, exc);
            return null;
        }

    } // getRatingsTotalAsDriver 

    /**
     * Get sum of all rider ratings for customer
     *
     * @param customerId customerId for customer to be rated
     * @return Average Rating, or null if something ugly happened
     */
    public Integer getRatingsTotalAsRider(Integer customerId) {

        CustomerEntity ce = this.lookupCustomerControllerBeanLocal().getCustomer(customerId);


        if (ce == null) {
            log.log(Level.WARNING, "looking up customer for id " + customerId + " failed for getRatingsTotalAsRider");
            return null;
        }

        try {
            RiderUndertakesRideControllerLocal rurcl = new JRiderUndertakesRideEntityService().lookupRiderUndertakesRideControllerBeanLocal();
            return rurcl.getTotalOfRatingsForRider(ce);
        } catch (Exception exc) {
            log.log(Level.WARNING, "Unexpected Exception while retrieving driverRatingCount for customer " + customerId, exc);
            return null;
        }

    } // getRatingsTotalAsRider
} // class
