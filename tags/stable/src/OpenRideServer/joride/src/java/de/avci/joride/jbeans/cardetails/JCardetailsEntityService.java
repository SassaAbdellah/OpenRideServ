/**
 * Service to get and Put CustomerEntityBeans the EJB Way.
 *
 */
package de.avci.joride.jbeans.cardetails;

import de.avci.joride.jbeans.customerprofile.JCustomerEntityService;
import de.avci.joride.utils.CRUDConstants;
import de.avci.joride.utils.HTTPUtil;

import de.fhg.fokus.openride.customerprofile.CarDetailsControllerLocal;
import de.fhg.fokus.openride.customerprofile.CarDetailsEntity;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Service to get and put JCarDetailEntityBeans to the System.
 *
 *
 *
 * @author jochen
 *
 */
public class JCardetailsEntityService {

    transient Logger log = Logger.getLogger(this.getClass().getCanonicalName());
    CarDetailsControllerLocal carDetailsControllerBean = lookupCarDetailsControllerBeanLocal();

    private CarDetailsControllerLocal lookupCarDetailsControllerBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CarDetailsControllerLocal) c.lookup("java:global/OpenRideServer/OpenRideServer-ejb/CarDetailsControllerBean!de.fhg.fokus.openride.customerprofile.CarDetailsControllerLocal");
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
     * Create a new JCarDetailsEntity in Database.
     *
     * @param jcarDetailsEntity
     */
    public void create(JCarDetailsEntity jcarDetailsEntity) {



        CustomerEntity ce = getCustomerEntity();


        carDetailsControllerBean.addCarDetails(
                ce,
                jcarDetailsEntity.getCardetBrand(),
                jcarDetailsEntity.getCardetBuildyear(),
                jcarDetailsEntity.getCardetColor(),
                jcarDetailsEntity.getCardetPlateno());


    } // create

    /**
     * Get a list of Cars (CarDetailEntity Objects) for this user. As always,
     * user is determined savely from http auth data.
     *
     * @return
     */
    public List<CarDetailsEntity> getCarDetailsList() {

        CustomerEntity ce = getCustomerEntity();

        return carDetailsControllerBean.getCarDetailsList(ce);

    }

    /**
     * Savely retrieve the carDetailsEnttity with the given ID. Savely means:
     * Determine the caller from HTTPRequest (AuthPrincipal) get the CarDetails
     * Object by cardetID Check if the CarDetailsObject belongs to the Caller If
     * so, return the cardetailsObject, else, throw an error.
     *
     * @param cardetID
     */
    public CarDetailsEntity getCarDetailsEntitySavely(int cardetId) {

        CustomerEntity ce = this.getCustomerEntity();

        CarDetailsEntity cde = this.lookupCarDetailsControllerBeanLocal().getCarDetailsByCardetId(cardetId);

        if (ce == null) {
            throw new Error("Cannot retrieve customer from Request, Error thrown");
        }

        if (cde == null) {
            log.log(Level.WARNING, "Cannot retrieve car details for id " + cardetId + " returning null");
            return null;
        }

        if ((cde.getCustId().getCustId()) != ce.getCustId()) {
            throw new Error("CustomerID does not belong to Cardetails to be returned, Error!");
        }

        return cde;
    }

    /**
     * Savely delete the cardetails object with the given cardetails ID. That
     * is: Determine the caller from http request, see, if cardet belongs to the
     * caller, else, fail with Error
     *
     *
     * @param cardetID
     */
    public void deleteSavely(int cardetID) {

        // call getCarDetailsSavely -- this will throw an error if 
        // car details does not match customerID
        CarDetailsEntity cde = this.getCarDetailsEntitySavely(cardetID);

        if (cde != null) {
            this.lookupCarDetailsControllerBeanLocal().removeCarDetails(cardetID);
        }

    }  // 

    /**
     * Write this Object savely to Database. That is: Check that caller in http
     * request is indeed the ownwer of this CardetEntity, if so, then
     * updateData, if not then fail with an error<
     *
     *
     *
     *

     *
     * @param jcde
     */
    public void updateToDBSavely(JCarDetailsEntity jcde) {


        // call getCarDetailsSavely -- this will throw an error if 
        // car details does not match customerID
        CarDetailsEntity testcde = this.getCarDetailsEntitySavely(jcde.getCardetId());


        // if we can get the testce, then we can also update the data
        CarDetailsControllerLocal cdbe = this.lookupCarDetailsControllerBeanLocal();



        // bad code in OpenRide-ejb:
        // method for updating by cardet_ID is missing!

        cdbe.updateCarDetails(
                testcde.getCardetId(),
                jcde.getCardetBrand(),
                jcde.getCardetBuildyear(),
                jcde.getCardetColor(),
                jcde.getCardetPlateno());



    }
} // classc