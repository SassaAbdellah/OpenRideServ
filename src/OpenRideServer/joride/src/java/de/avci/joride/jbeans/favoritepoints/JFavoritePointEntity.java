/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.favoritepoints;

import de.avci.joride.utils.CRUDConstants;
import de.avci.joride.utils.HTTPUtil;
import de.avci.joride.utils.PostGISPointUtil;
import de.avci.joride.utils.WebflowPoint;
import de.fhg.fokus.openride.customerprofile.FavoritePointEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.postgis.Point;

/**
 * JSF Bean Wrapper for FavouritePointEntity
 *
 * @author jochen
 */
@Named("jfavpoint")
@RequestScoped
public class JFavoritePointEntity extends FavoritePointEntity {

    transient Logger log = Logger.getLogger(this.getClass().getCanonicalName());

    /**
     * Returns the list of favourite points for the current customer given.
     * Current customer is determined savely from HTTPRequest's Auth Principal
     *
     *
     * @return list
     */
    public List<JFavoritePointEntity> getFavoritePointList() {

        return (new JFavoritePointsService()).getFavoritePointList();
    }

    public void doCrudAction(ActionEvent evt) {

        HTTPUtil hru = new HTTPUtil();

        log.log(Level.FINE,"doCrudAction Event : " + evt.toString());

        String action = hru.getParameterSingleValue((new CRUDConstants()).getParamNameCrudAction());
         log.log(Level.FINE,"Param Action : " + action);

        String id = hru.getParameterSingleValue((new CRUDConstants()).getParamNameCrudId());
         log.log(Level.FINE,"Param ID     : " + id);



        if (CRUDConstants.PARAM_VALUE_CRUD_DELETE.equals(action)) {
            this.delete(new Integer(id).intValue());
        }



        if (CRUDConstants.PARAM_VALUE_CRUD_CREATE.equals(action)) {
            this.create();
        }

    }

    /**
     * Create a new Favoritepoint for this user
     *
     */
    public void create() {

        (new JFavoritePointsService()).addFavoritePoint(this);

    }

    public void delete(int favpointId) {

        (new JFavoritePointsService()).deleteFavoritePointSavely(favpointId);
    }

    /**
     * Load the FavoritePointsEntity from database with the id given as in http
     * request parameter. Will do extensive checking on the backside to ensure
     * that the current User (identified by http Auth principal) has permissions
     * to do so.
     *
     */
    public void updateFromDB() {

        int favpointId = 0;

        String favpointIdStr = (new HTTPUtil()).getParameterSingleValue(CRUDConstants.PARAM_NAME_CRUD_ID);
        favpointId = (new Integer(favpointIdStr)).intValue();

        log.log(Level.FINE, "============ Loading Favpoint :" + favpointId + " ");

        FavoritePointEntity fpe = (new JFavoritePointsService()).getFavoritePointEntitySafely(favpointId);


        this.setFavptId(fpe.getFavptId());
        this.setFavptDisplayname(fpe.getFavptDisplayname());
        this.setFavptAddress(fpe.getFavptAddress());
        this.setFavptPoint(fpe.getFavptPoint());

    } // updateFromDB

    /**
     * Update Point data from HTTPRequest's Parameter. I.e: if HTTPRequest
     * transports Address/Displaystring or Coordinate points, then update the
     * respective data from http request.
     *
     */
    public void smartUpdatePoints() {

        // create new point
        WebflowPoint p = new WebflowPoint();
        //
        p.smartUpdate();
        // 
        if (p.getAddress() != null) {
            this.setFavptAddress(p.getAddress());
        }

        if (p.getDisplaystring() != null) {
            this.setFavptDisplayname(p.getDisplaystring());
        }

        if (p.getPoint() != null) {
            this.setFavptPoint(p.getDatabaseString());
        }

    }

    /**
     * Get Longitude from point
     *
     * @return
     */
    public Double getLon() {

        PostGISPointUtil pu = new PostGISPointUtil();
        Point p = pu.pointFromDBString(this.getFavptPoint());

        // protect agains null pointer exceptions
        if (pu == null || p == null) {
            return null;
        }

        return pu.getLon(p);
    }

    /**
     * Get Latitude from point
     *
     * @return
     */
    public Double getLat() {

        PostGISPointUtil pu = new PostGISPointUtil();
        Point p = pu.pointFromDBString(this.getFavptPoint());

        // protect agains null pointer exceptions
        if (pu == null || p == null) {
            return null;
        }
        return pu.getLat(p);
    }

    /**
     * Create a new JFavoritePointEntity from a FavoritePointEntity (without
     * leading "J")
     *
     * @param fp
     */
    public JFavoritePointEntity(FavoritePointEntity fp) {

        this.setCustId(fp.getCustId());
        this.setFavptAddress(fp.getFavptAddress());
        this.setFavptDisplayname(fp.getFavptDisplayname());
        this.setFavptFrequency(fp.getFavptFrequency());
        this.setFavptId(fp.getFavptId());
        this.setFavptPoint(fp.getFavptPoint());

    }

    /**
     * Dump default constructor
     *
     * @param fp
     */
    public JFavoritePointEntity() {
    }
} // class
