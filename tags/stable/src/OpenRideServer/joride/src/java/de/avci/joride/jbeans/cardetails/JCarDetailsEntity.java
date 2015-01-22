/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.cardetails;

import de.avci.joride.utils.CRUDConstants;
import de.avci.joride.utils.HTTPUtil;
import de.fhg.fokus.openride.customerprofile.CarDetailsEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

/**
 * Small Wrapper class making Entity Bean CustomerEntity availlable as a CDI
 * Bean for Use in JSF Frontend.
 *
 * @author jochen
 *
 */
@Named("jcardetails")
@RequestScoped
public class JCarDetailsEntity extends CarDetailsEntity {

    transient Logger log = Logger.getLogger(this.getClass().getCanonicalName());

    @Override
    public void setCardetBrand(String cardetBrand) {
        super.setCardetBrand(cardetBrand);
    }

    /**
     * the color property is missspelled as "colour" in OpenRide Backend. So we
     * make a humble attempt to fix that with a wrapper -- at least in the
     * frontend
     */
    public String getCardetColor() {
        return this.getCardetColour();
    }

    /**
     * the color property is missspelled as "colour" in OpenRide Backend. So we
     * make a humble attempt to fix that with a wrapper -- at least in the
     * frontend
     */
    public void setCardetColor(String arg) {
        this.setCardetColour(arg);
    }

    public void create() {
        (new JCardetailsEntityService()).create(this);

    }

    public List<CarDetailsEntity> getCarDetailsList() {

        return (new JCardetailsEntityService()).getCarDetailsList();
    }

    public void doCrudAction(ActionEvent evt) {

        HTTPUtil hru = new HTTPUtil();

        log.log(Level.FINE, "doCrudAction Event : " + evt.toString());

        String action = hru.getParameterSingleValue((new CRUDConstants()).getParamNameCrudAction());
        log.log(Level.FINE, "Param Action : " + action);

        String id = hru.getParameterSingleValue((new CRUDConstants()).getParamNameCrudId());
        log.log(Level.FINE, "Param ID     : " + id);



        if (CRUDConstants.PARAM_VALUE_CRUD_DELETE.equals(action)) {
            this.delete(new Integer(id).intValue());
        }

        if (CRUDConstants.PARAM_VALUE_CRUD_UPDATE.equals(action)) {
            this.updateToDB();
        }

        if (CRUDConstants.PARAM_VALUE_CRUD_CREATE.equals(action)) {
            this.create();
        }

    }

    /**
     * Load the Cardetails Entity from database with the id given as in http
     * request parameter. Will do extensive checking on the backside to ensure
     * that the current User (identified by http Auth principal) has permissions
     * to do so.
     *
     */
    public void updateFromDB() {

        int cardetID = 0;

        String cardetIdStr = (new HTTPUtil()).getParameterSingleValue(CRUDConstants.PARAM_NAME_CRUD_ID);
        int cardetId = (new Integer(cardetIdStr)).intValue();

        log.log(Level.FINE, "============ Loading Cardet :" + cardetId + " ");

        CarDetailsEntity cde = (new JCardetailsEntityService()).getCarDetailsEntitySavely(cardetId);



        this.setCardetBrand(cde.getCardetBrand());
        this.setCardetBuildyear(cde.getCardetBuildyear());
        this.setCardetColor(cde.getCardetColour());
        this.setCardetPlateno(cde.getCardetPlateno());
        this.setCardetId(cardetId);
        this.setCustId(cde.getCustId());

    }

    /**
     * Delete the Cardetails Entity with the id given as in http request
     * parameter. Will do extensive checking on the backside to ensure that the
     * current User (identified by http Auth principal) has permissions to do
     * so.
     *
     */
    public void delete(int cardetId) {

        log.log(Level.FINE, "============ Deleting Cardet :" + cardetId + " ");

        (new JCardetailsEntityService()).deleteSavely(cardetId);

    }

    /**
     * Updates to DB. Evaluates
     *
     * @return
     */
    public void updateToDB() {

        log.log(Level.FINE, "============ Updating Car Details :" + this.getCardetId() + " ");

        (new JCardetailsEntityService()).updateToDBSavely(this);

    }
}
