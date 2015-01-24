/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.auxiliary;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import de.avci.joride.jbeans.customerprofile.JCustomerEntityService;
import de.avci.joride.jbeans.driverundertakesride.JDriverUndertakesRideEntity;
import de.avci.joride.jbeans.driverundertakesride.JDriverUndertakesRideEntityService;
import de.avci.joride.jbeans.messages.JMessageService;
import de.avci.joride.jbeans.riderundertakesride.JRiderUndertakesRideEntity;
import de.avci.joride.jbeans.riderundertakesride.JRiderUndertakesRideEntityService;
import de.avci.joride.utils.HTTPUtil;
import de.avci.joride.utils.PropertiesLoader;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideEntity;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;

/**
 * Service to provide Information about updated drives and rides for given
 * users.
 *
 * @author jochen
 */
public class UpdateService {

    /**
     * Return a localized message informing the user that there are updates.
     *
     */
    public String getUpdateMessage() {



        int updatedoffersCount = this.getUpdatedDrives().size();
        int updatedsearchesCount = this.getUpdatedRides().size();


        if (updatedsearchesCount > 0 || updatedoffersCount > 0) {

        	java.util.Locale locale=new HTTPUtil().detectBestLocale();
            return PropertiesLoader.getMessageProperties(locale).getProperty("updates.updateNotification");


        } else {
            return ""; // if there are no updates, then return an empty String
        }

    }

    /**
     * Return a list of all updated rides for given customer
     *
     */
    public List<JDriverUndertakesRideEntity> getUpdatedDrives() {


        HttpServletRequest request = new HTTPUtil().getHTTPServletRequest();
        
        CustomerEntity ce = (new JCustomerEntityService()).getCustomerEntityFromRequest(request);


        LinkedList<JDriverUndertakesRideEntity> updatedDrives = new LinkedList<JDriverUndertakesRideEntity>();

        // return empty list if customer cannot be determined 
        // e.g during login phase
        if (ce == null) {
            return updatedDrives;
        }


        JDriverUndertakesRideEntityService driverUndertakesRideEntityService = new JDriverUndertakesRideEntityService();

        List<DriverUndertakesRideEntity> openoffers =
                driverUndertakesRideEntityService.getActiveDrivesForDriver(request);

        // Updated offers for (DriverUndertakesRideEntity drive : openoffers)

        for (DriverUndertakesRideEntity drive : openoffers) {
            if (driverUndertakesRideEntityService.isDriveUpdated(drive.getRideId())) {
                
                JDriverUndertakesRideEntity jdrive=new JDriverUndertakesRideEntity();
                jdrive.updateFromDriverUndertakesRideEntity(drive);
                updatedDrives.add(jdrive);
            }
        }

        return updatedDrives;
    }

    /**
     * Get a list of all the updated rides
     *
     * @return
     */
    public List<JRiderUndertakesRideEntity> getUpdatedRides() {


        HttpServletRequest request = new HTTPUtil().getHTTPServletRequest();

  
        CustomerEntity ce = (new JCustomerEntityService()).getCustomerEntityFromRequest(request);

        LinkedList<JRiderUndertakesRideEntity> res = new LinkedList<JRiderUndertakesRideEntity>();

        // return empty list if calling user cannot be determined
        if (ce == null) { return res; }


        JRiderUndertakesRideEntityService jRiderUndertakesRideEntityService =
                new JRiderUndertakesRideEntityService();


        // Open searches FIXME: maybe Opensearches should be theses, which
        // have not been started or where no partner has yet been found?
        List<RiderUndertakesRideEntity> opensearches =
                jRiderUndertakesRideEntityService.getActiveOpenRides(request);

        // Updated searches for (RiderUndertakesRideEntity r : opensearches)


        for (RiderUndertakesRideEntity ride : opensearches) {
            if (jRiderUndertakesRideEntityService.isRideUpdated(ride.getRiderrouteId())) {
                
                JRiderUndertakesRideEntity jride=new JRiderUndertakesRideEntity();
                jride.updateFromRiderUndertakesRideEntity(ride);
                res.add(jride);
            }
        }

        return res;
        
    } // getUpdatedRides
    
    
    
    /** 
     *   @return  true, if calling customer has updated matches, else false.
     */
    boolean isMatchUpdated(){
        return new JCustomerEntityService().isMatchUpdated();
    }
    
    
    /**  @return  true, if there are unread messages for this customer, else false
     * 
     */
    public boolean getHasUnreadMessages(){
    	return new JMessageService().hasUnreadMessages();
    }
    
    
    
    
} // class 
