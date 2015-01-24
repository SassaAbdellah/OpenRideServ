/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.driverundertakesride;

import de.avci.joride.utils.HTTPUtil;
import de.avci.joride.utils.WebflowPoint;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideEntity;
import de.fhg.fokus.openride.rides.driver.WaypointEntity;
import java.io.Serializable;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

/**
 * Java Class making Waypoints accessible to frontend as a JSFBean
 *
 * This is session scoped, since it is supposed to model a newly created
 * waypoint, which live over several pages.
 *
 * @author jochen
 */
@Named
@SessionScoped
/**
 * Frontend to Waypoint Entity as a JSF Bean.
 *
 */
public class JWaypointEntity extends WaypointEntity implements Serializable, Comparable<JWaypointEntity> {

    /**
     * Parametername for rideId. Used when doing a smart update.
     */
    public static final String PARAM_NAME_RIDEID = "rideId";

    /**
     * @return parameter name for rideId Parameter
     */
    public String getParamNameRideId() {
        return PARAM_NAME_RIDEID;
    }
    /**
     * Parametername for position. Used when doing a smart update.
     */
    public static final String PARAM_NAME_POSITION = "position";

    /**
     * @return parameter name position
     */
    public String getParamNamePosition() {
        return PARAM_NAME_POSITION;
    }
    /**
     * Parametername for description. Used when doing a smart update.
     */
    public static final String PARAM_NAME_DESCRIPTION = "description";

    /**
     * @return parameter name position
     */
    public String getParamNameDescription() {
        return PARAM_NAME_DESCRIPTION;
    }

    /**
     * Clear all fields, set parameters longitude, latitude, description,
     * position and rideId from parameters.
     */
    public void smartUpdate() {

        try {


            this.setDescription("smartUpdat has been called on this");
            
            

            HTTPUtil hru = new HTTPUtil();
            String positionS = hru.getParameterSingleValue(getParamNamePosition());

            if (positionS != null) {
                try {
                    this.setPosition(new Integer(positionS));
                } catch (Exception exc) {
                    System.err.println(exc);
                }
            }

            String rideIdS = hru.getParameterSingleValue(getParamNameRideId());

            if (rideIdS != null) {
                try {
                	Integer rideId=new Integer(rideIdS);
                	DriverUndertakesRideEntity drive=new JDriverUndertakesRideEntityService().getDriveByIdSafely(rideId);
                    this.setRideId(drive);
                } catch (Exception exc) {
                    System.err.println(exc);
                }
            }

            // retrieve point coordinates and descriptions
            // via webflow Point

            WebflowPoint webflowPoint = new WebflowPoint();
            webflowPoint.smartUpdate();

            this.setLongitude(webflowPoint.getLon());
            this.setLatitude(webflowPoint.getLat());
            this.setDescription(webflowPoint.getDisplaystring());

            // set position parameter and rideIDs 

        } catch (Exception exc) {
            System.err.println(exc);
        }


    } // smartUpdate
    /**
     * JWaypoint Entity adds a volatile position parameter to WaypointEntity,
     *
     * This is used *only* for finding the routeIdx of a newly created waypoint.
     *
     * I.e a newly created waypoint "W_new" is added just before the first
     * existing waypoint having routeIdx larger than w.position.
     *
     *
     * Position defaults to Integer.MAX_VALUE so that newly created waypoints
     * without other information are added to the end of the ridepoints list.
     *
     */
    private Integer position = new Integer(Integer.MAX_VALUE);

    public Integer getPosition() {
        return this.position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * Encode this waypoint as a JSON *Array* compatible with the representation
     * of {@link JRoutePointEntity} that is:
     * [longitude,latitude,id,'description']
     *
     * @return representation of this object as a json array
     *
     */
    public StringBuffer getJSON() {
        StringBuffer buf = new StringBuffer();
        buf.append("[ ");
        buf.append(this.getLongitude());
        buf.append(", ");
        buf.append(this.getLongitude());
        buf.append(", ");
        buf.append(this.getId());
        buf.append(", ");
        buf.append("'");
        buf.append(this.getDescription());
        buf.append("'");
        buf.append(", ");
        buf.append(this.getRideId());
        buf.append(", ");
        buf.append(this.getPosition());
        buf.append(", ");
        buf.append(this.getRouteIdx());
        buf.append(" ]");
        return buf;
    }

    /**
     * Create Waypoint Entity from it's superclass, copying
     * longitude,latitude,rideId,routeIdx and description.
     *
     */
    protected JWaypointEntity(WaypointEntity w) {

        super();
        this.setLongitude(w.getLongitude());
        this.setLatitude(w.getLatitude());
        this.setRideId(w.getRideId());
        this.setRouteIdx(w.getRouteIdx());
        this.setDescription(w.getDescription());
    }

    /**
     * Dumb default constructor
     */
    public JWaypointEntity() {
        super();
        this.setDescription("uninitialized");
    }

    /**
     * True, if location is set for this waypoint. This is "step one" of the
     * waypoint initialisation
     *
     * @return true, if longitude,latitude and description are !=null each
     */
    public boolean getLocationSet() {

        return (this.getLatitude() != null
                && this.getLongitude() != null
                && this.getDescription() != null);

    }

    /**
     * This is step 2 of waypoint initialisation.
     *
     * @return true, iff both rideId and routeIdx are !=null
     */
    public boolean getRideAttached() {

        return (this.getRideId() != null)
                && (this.getRouteIdx() != null);
    }

    /**
     * returns true, iff the initialisation is ready for persisting.
     *
     *
     * @return this.isRideIdAttached() && this.isLocationSet()
     */
    public boolean getReadyForPersisting() {
        return (this.getRideAttached() && this.getLocationSet());
    }

    /**
     * Extract a plain WaypointEntity object from this object.
     *
     * This makes sense when persisting a waypoint, since the entity manager
     * does not now JWaypointEntities.
     *
     *
     */
    public WaypointEntity extractWaypoint() {

        WaypointEntity res = new WaypointEntity();

        res.setDescription(this.getDescription());
        res.setLatitude(this.getLatitude());
        res.setLongitude(this.getLongitude());
        res.setRideId(this.getRideId());
        res.setRouteIdx(this.getRouteIdx());

        return res;

    }

    /**
     * Call driverUndertakesRideService to add this waypoint to it's drive
     */
    public void addToDrive() {

        new JDriverUndertakesRideEntityService().addWaypointToDriveSafely(this);
    }

    /**
     * returns a value for the position parameter to addWaypoint(...) so that a
     * point added with this parameter is placed immediately *before* this
     * waypoint;
     *
     * @return this.getRouteIdx-0.5
     */
    public int getPositionValueBefore() {
        return this.getRouteIdx();
    }

    /**
     * returns a value for the position parameter to addWaypoint(...) so that a
     * point added with this parameter is placed immediately *after* this
     * waypoint;
     *
     * @return this.getRouteIdx+0.5
     */
    public int getPositionValueAfter() {
        return this.getRouteIdx()+1;
    }
    
    
    /** Remove this waypoint from the driverUndertakesRideEntity it is attached to
     * 
     */
    public void removeFromDrive(){
        new JDriverUndertakesRideEntityService().removeWaypointFromDriveSafely(this.getRideId().getRideId(), this.getRouteIdx());
    }
    
    /** Position of waypoint to be displayed.
     *  Since rideIdx property is 0-based, we use this to display a 1-based route index.
     * 
     * @return getRouteIdx+1
     */
    public int getDisplayRouteIndex(){
        return this.getRouteIdx()+1;
    }

    /** Wayponts are naturally compared by comparing the route index.
     *  Lower Ride Index comes firs.
     * 
     * @param o
     * @return
     */
	@Override
	public int compareTo(JWaypointEntity o) {
	
		if(this.getRouteIdx()>o.getRouteIdx()){return 1;}
		if(this.getRouteIdx()<o.getRouteIdx()){return -1;}
		return 0;
	}

	
    
}
