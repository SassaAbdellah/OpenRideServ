/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.restful;

import de.avci.joride.constants.JoRideConstants;
import de.avci.joride.jbeans.customerprofile.JCustomerEntityService;
import de.avci.joride.jbeans.driverundertakesride.JDriverUndertakesRideEntityService;
import de.avci.joride.jbeans.riderundertakesride.JRiderUndertakesRideEntityService;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideEntity;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 * Simple Update Service providing information about updated requests and offers
 * to the Webclient.
 * 
 * 
 *
 * @author jochen
 */
@Path("protected/update")
@Produces("text/json")
public class UpdateService {

    @GET
    
    /** Produces a JSON Object describing the updated requests
     *  for the user given as remote user.
     * 
     *  Example for JSON comes here:
     * 
     *  {"UpdateResponse":{"updatedoffers":0,"updatedsearches":0}}
     *  TODO: get better Documentation for JSON Object (WADL?)
     * 
     * 
     *  Service is to be found at an URL like that:
     *  http://host:port/joride/jax-rs/update/
     * 
     * 
     */
    public String getUpdateText(@Context HttpServletRequest request) {
        
        
        if(request.getRemoteUser()==null){
            throw new Error("Cannot determine update, request's remoteuser is null");
        }
     
        CustomerEntity ce = (new JCustomerEntityService()).getCustomerEntityFromRequest(request);

        int updatedoffersCount = 0;
        int updatedsearchesCount = 0;


        JDriverUndertakesRideEntityService driverUndertakesRideEntityService = new JDriverUndertakesRideEntityService();

        List<DriverUndertakesRideEntity> openoffers =
                driverUndertakesRideEntityService.getActiveDrivesForDriver(request);

        // Updated offers for (DriverUndertakesRideEntity drive : openoffers)

        for (DriverUndertakesRideEntity drive : openoffers) {
            if (driverUndertakesRideEntityService.isDriveUpdated(drive.getRideId())) {
                updatedoffersCount++;
            }
        }


        JRiderUndertakesRideEntityService jRiderUndertakesRideEntityService =
                new JRiderUndertakesRideEntityService();


        // Open searches FIXME: maybe Opensearches should be theses, which
        // have not been started or where no partner has yet been found?
        List<RiderUndertakesRideEntity> opensearches =
                jRiderUndertakesRideEntityService.getActiveOpenRides(request);

        // Updated searches for (RiderUndertakesRideEntity r : opensearches)


        for (RiderUndertakesRideEntity ride : opensearches) {
            if (jRiderUndertakesRideEntityService.isRideUpdated(ride.getRiderrouteId())) {
                updatedsearchesCount++;
            }
        }
        
        
        String UpdateResponse=JoRideConstants.PARAM_NAME_UPDATE_RESPONSE;
        String NoUpdateOffers=JoRideConstants.PARAM_NAME_NO_UPDATED_OFFERS;
        String NoUpdateRequests=JoRideConstants.PARAM_NAME_NO_UPDATED_REQUESTS;
        
        

        // For simplicity, we build the JSON Response "byHand" here //
        String result = "{\""+UpdateResponse+"\":{\""+NoUpdateOffers+"\":" +updatedoffersCount + ",\""+NoUpdateRequests+"\":" + updatedsearchesCount + "}}";

        return result;

    }

    /**
     * Root webservices must have a default constructor!
     *
     */
    public UpdateService() {
    }
}
