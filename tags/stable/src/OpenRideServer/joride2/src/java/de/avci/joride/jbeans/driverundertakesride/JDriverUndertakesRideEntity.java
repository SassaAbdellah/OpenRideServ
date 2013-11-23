/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.driverundertakesride;

import de.avci.joride.constants.JoRideConstants;
import de.avci.joride.jbeans.auxiliary.RideSearchParamsBean;
import de.avci.joride.jbeans.matching.JMatchingEntity;
import de.avci.joride.jbeans.matching.JMatchingEntityService;
import de.avci.joride.utils.CRUDConstants;
import de.avci.joride.utils.HTTPUtil;
import de.avci.joride.utils.PropertiesLoader;
import de.avci.joride.utils.WebflowPoint;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideEntity;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import org.postgis.Point;

/**
 * Small Wrapper class making Entity Bean CustomerEntity availlable as a CDI
 * Bean for Use in JSF Frontend.
 *
 * @author jochen
 *
 */
@Named
@SessionScoped
public class JDriverUndertakesRideEntity extends de.fhg.fokus.openride.rides.driver.DriverUndertakesRideEntity {

    transient Logger log = Logger.getLogger(this.getClass().getCanonicalName());
    /**
     * Default Value for Acceptable Detour in Km. May be changed by the user in
     * Frontends.
     */
    private Integer ACCEPTABLE_DETOUR_KM_DEFAULT = 10;
    /**
     * Default Value for Acceptable Detour in Min. May be changed by the user in
     * Frontends.
     */
    private Integer ACCEPTABLE_DETOUR_MIN_DEFAULT = 15;
    /**
     * Default Value for Acceptable Detour in Percent. May be changed by the
     * user in Frontends.
     */
    private Integer ACCEPTABLE_DETOUR_PERCENT_DEFAULT = 20;
    /**
     * Default Value for Number of offered seats. May be changed by the user in
     * Frontends.
     */
    private Integer NUMBER_SEATS_OFFERED_DEFAULT = 1;
    private PropertiesLoader propertiesLoader = new PropertiesLoader();

    /**
     * Get a list of active drives for this driver.
     *
     * @return
     */
    public List<DriverUndertakesRideEntity> getActiveDrivesforDriver() {
        return (new JDriverUndertakesRideEntityService()).getActiveDrivesForDriver();
    }

    /**
     * Get a list with all Open Drives for this driver.
     *
     * TODO: what actually is an "open" Drive.
     *
     * @return
     */
    public List<DriverUndertakesRideEntity> getOpenDrivesForDriver() {
        return (new JDriverUndertakesRideEntityService()).getOpenDrivesForDriver();
    }

    /**
     * Get a list with all Drives for this driver.
     *
     * TODO: what actually is an "open" Drive.
     *
     * @return
     */
    public List<JDriverUndertakesRideEntity> getDrivesForDriver() {

        List<DriverUndertakesRideEntity> inlist = (new JDriverUndertakesRideEntityService()).getDrivesForDriver();

        return this.castList(inlist);
    }

    /**
     * Cast a list of DriverUndertakesRideEntity Objects into a list of
     * JDriverUndertakesRideEntity Objects, for easy display.
     *
     *
     * @param inlist
     * @return
     */
    protected List<JDriverUndertakesRideEntity> castList(List<DriverUndertakesRideEntity> inlist) {


        List<JDriverUndertakesRideEntity> res = new LinkedList<JDriverUndertakesRideEntity>();

        Iterator<DriverUndertakesRideEntity> it = inlist.iterator();

        while (it.hasNext()) {

            JDriverUndertakesRideEntity jdure = new JDriverUndertakesRideEntity();
            jdure.updateFromDriverUndertakesRideEntity(it.next());

            res.add(jdure);
        }

        return res;
    }
    /**
     * A date format for formatting start and end date. Created via lazy
     * instantiation.
     *
     * @deprecated should be done centrally in utils* class
     *
     */
    protected DateFormat dateFormat;

    /**
     * Accessor with lazy instantiation
     *
     *
     *
     * @return
     */
    protected DateFormat getDateFormat() {

        if (this.dateFormat == null) {
            dateFormat = (new JoRideConstants()).createDateTimeFormat();
        }

        return dateFormat;
    }

    /**
     * Return a nicely formatted version of the startDate
     *
     * @return
     */
    public String getStartDateFormatted() {
        return getDateFormat().format(this.getRideStarttime());
    }

    /**
     * See, if the CRUDConstants().getParamNameCrudId() parameter is present in
     * HTTPRequest. If the ID parameter is != null, then update data from
     * DriverUndertakesRideEntity in database with rideId given by id parameter.
     * If parameter's value is not null, then leave **this** untouched
     *
     */
    public void update() {

        String idStr = (new HTTPUtil()).getParameterSingleValue(new CRUDConstants().getParamNameCrudId());

        int id = 0;

        try {
            id = new Integer(idStr).intValue();
        } catch (java.lang.NumberFormatException exc) {
            throw new Error("ID Parameter does not contain Numeric Value " + idStr);
        }



        this.updateFromId(id);

    }

    /**
     * Update from a given DriverUndertakesRideEntity object.
     *
     * @param dure DriverUndertakesRideEntityObject to update *this* object.
     */
    public void updateFromDriverUndertakesRideEntity(DriverUndertakesRideEntity dure) {

        this.setCustId(dure.getCustId());
        this.setEndptAddress(dure.getEndptAddress());
        this.setRideAcceptableDetourInKm(dure.getRideAcceptableDetourInKm());
        this.setRideAcceptableDetourInMin(dure.getRideAcceptableDetourInMin());
        this.setRideAcceptableDetourInPercent(dure.getRideAcceptableDetourInPercent());
        this.setRideComment(dure.getRideComment());
        this.setRideCurrpos(dure.getRideCurrpos());
        this.setRideEndpt(dure.getRideEndpt());
        this.setRideId(dure.getRideId());
        this.setRideOfferedseatsNo(dure.getRideOfferedseatsNo());
        this.setRideRoutePointDistanceMeters(dure.getRideRoutePointDistanceMeters());
        this.setRideSeriesId(dure.getRideSeriesId());
        this.setRideStartpt(dure.getRideStartpt());
        this.setRideStarttime(dure.getRideStarttime());
        this.setRideWeekdays(dure.getRideWeekdays());
        this.setRiderUndertakesRideEntityCollection(dure.getRiderUndertakesRideEntityCollection());
        this.setStartptAddress(dure.getStartptAddress());
    }

    /**
     * Update *this* with the Data read in from database for given id, or just
     * do nothing if ID is null.
     *
     * @param id rideId of the DriverUndertakeRide Entity to update from.
     */
    public void updateFromId(Integer id) {

        JDriverUndertakesRideEntityService service = new JDriverUndertakesRideEntityService();
        service.updateJDriverUndertakesRideEntityByIDSafely(id, this);

    }

    /**
     * Get the Route Points for this Drive wrapped in a JRoutPointsEntity Object
     *
     * @return
     */
    public JRoutePointsEntity getRoutePoints() {

        int rideID = this.getRideId();
        return new JDriverUndertakesRideEntityService().getRoutePointsForDrive(rideID);

    }

    /**
     * Get the Route Points for this Drive wrapped in a JRoutPointsEntity Object
     *
     * @return
     */
    public JRoutePointsEntity findRoutePoints() {

        return new JDriverUndertakesRideEntityService().findRoute(this);

    }

    /**
     * Get the RoutePoints for this Drive encoded in a JSONString
     *
     *
     * @return
     *
     */
    public String getRoutePointsAsJSON() {
        return this.getRoutePoints().getRoutePointsAsJSON();
    }

    /**
     * Get the RoutePoints for this Drive encoded in a JSONString
     *
     * @return
     */
    public String findRoutePointsAsJSON() {
        return this.findRoutePoints().getRoutePointsAsJSON();
    }
    /**
     * Value for point.target parameters. If "Startpoint" ist set, then
     * smartUpdate will set the startpoint
     */
    private static final String paramValueTargetStartpoint = "STARTPOINT";

    /**
     * Trivial Accessor making paramValueStartpoint accessible with JSF Methods
     *
     * @return
     */
    public String getParamValueTargetStartpoint() {
        return paramValueTargetStartpoint;
    }
    /**
     * Value for point.target parameters. If "Endpoint" ist set, then
     * smartUpdate will set the startpoint
     */
    private static final String paramValueTargetEndpoint = "ENDPOINT";

    /**
     * Trivial Accessor making paramValueStartpoint accessible with JSF Methods
     *
     * @return
     */
    public String getParamValueTargetEndpoint() {
        return paramValueTargetEndpoint;
    }

    /**
     * Return the Longitude of the rideStartpt , or null if the rideStartpt is
     * null;
     */
    public double getLongitudeStart() {

        if (this.getRideStartpt() == null) {
            return new Double(0);
        }
        return new Double(getRideStartpt().getX());
    }

    /**
     * Return the Latitude of the rideStartpt , or null if the rideStartpt is
     * null;
     */
    public double getLatitudeStart() {

        if (this.getRideStartpt() == null) {
            return new Double(0);
        }
        return new Double(getRideStartpt().getY());
    }

    /**
     * Return the Longitude of the rideEndpt , or null if the rideEndpt is null;
     */
    public double getLongitudeEnd() {

        if (this.getRideEndpt() == null) {
            return new Double(0);
        }
        return new Double(getRideEndpt().getX());
    }

    /**
     * Return the Latitude of the rideStartpt , or null if the rideStartpt is
     * null;
     */
    public double getLatitudeEnd() {

        if (this.getRideEndpt() == null) {
            return new Double(0);
        }
        return new Double(getRideEndpt().getY());
    }

    /**
     * set the latitude of the rideStartpt
     */
    public void setLongitudeStart(double arg) {

        if (this.getRideStartpt() == null) {
            this.setRideStartpt(new Point(arg, 0));
        }

        this.getRideStartpt().setX(arg);
    }

    /**
     * set the latitude of the rideStartpt
     */
    public void setLatitudeStart(double arg) {

        if (this.getRideStartpt() == null) {
            this.setRideStartpt(new Point(0, arg));
        }

        this.getRideStartpt().setY(arg);
    }

    /**
     * set the latitude of the rideStartpt
     */
    public void setLongitudeEnd(double arg) {

        if (this.getRideEndpt() == null) {
            this.setRideEndpt(new Point(arg, 0));
        }

        this.getRideEndpt().setX(arg);
    }

    /**
     * set the latitude of the rideStartpt
     */
    public void setLatitudeEnd(double arg) {

        if (this.getRideEndpt() == null) {
            this.setRideEndpt(new Point(0, arg));
        }

        this.getRideEndpt().setY(arg);
    }

    /**
     * Update bean, thereby evaluating the HTTPRequest and update startpoint or
     * endpoint data depending on params present in HTTPRequest
     */
    public void smartUpdate() {

        WebflowPoint webflowPoint = new WebflowPoint();
        webflowPoint.smartUpdate();


        //   
        // see, if we should update the startpoints
        // 

        if (paramValueTargetStartpoint.equals(webflowPoint.getTarget())) {

            if (webflowPoint.getParamAddress() != null) {
                this.setStartptAddress(webflowPoint.getAddress());
            }

            // Set Start/End Latitude depending on target param
            if (webflowPoint.getLat() != null) {
                this.setLatitudeStart(webflowPoint.getLat());
            }

            // Set Start/End Longitude depending on target param
            if (webflowPoint.getLon() != null) {
                this.setLongitudeStart(webflowPoint.getLon());
            }

        } //   if(paramValueTargetStartpoint.equals(webflowPoint.getTarget()))




        //   
        // see, if we should update the endpoints
        // 

        if (paramValueTargetEndpoint.equals(webflowPoint.getTarget())) {

            if (webflowPoint.getParamAddress() != null) {
                this.setEndptAddress(webflowPoint.getAddress());
            }

            // Set Start/End Latitude depending on target param
            if (webflowPoint.getLat() != null) {
                this.setLatitudeEnd(webflowPoint.getLat());
            }

            // Set Start/End Longitude depending on target param
            if (webflowPoint.getLon() != null) {
                this.setLongitudeEnd(webflowPoint.getLon());
            }

        } //   if(paramValueTargetStartpoint.equals(webflowPoint.getTarget()))
    }

    /**
     * Initialize a JDriverUndertakeRide Entity to be created as a new Drive
     *
     */
    public void initializeNewDrive() {


        this.setRideId(null);

        if (this.getRideStarttime() == null) {
            this.setRideStarttime(new Date(System.currentTimeMillis()));
        }

        if (this.getRideAcceptableDetourInKm() == null) {
            this.setRideAcceptableDetourInKm(ACCEPTABLE_DETOUR_KM_DEFAULT);
        }

        if (this.getRideAcceptableDetourInMin() == null) {
            this.setRideAcceptableDetourInMin(ACCEPTABLE_DETOUR_MIN_DEFAULT);
        }


        if (this.getRideAcceptableDetourInPercent() == null) {
            this.setRideAcceptableDetourInPercent(ACCEPTABLE_DETOUR_PERCENT_DEFAULT);
        }

        if (this.getRideOfferedseatsNo() == null) {
            this.setRideOfferedseatsNo(NUMBER_SEATS_OFFERED_DEFAULT);
        }
        
    }

    /**
     * DriverUnderTakesRideController already provides support for setting
     * intermediate points on a route.
     *
     * This is currently not supported by the frontend and routing mechs, but to
     * be added soon.
     *
     * Until then, this methods returns an empty Array, to satisfy the
     * DriverUnderTakesRideController interface.
     *
     * @return
     */
    public Point[] getIntermediatePoints() {

        return new Point[0];
    }

    /**
     * Create a new DriverUndertakesRideEntity and save it to the Database. Note
     * that the set of Routepoints will always be created for this entity.
     * Routepoints are not created programmatically by the user.
     *
     *
     *
     *
     * @return id of the newly create DriverUndertakesRideEntity
     */
    public int addToDB() {


        if (this.getRideId() != null) {
            throw new Error("Cannot add Ride to Database, Id already exists");
        }

        JDriverUndertakesRideEntityService jdures = new JDriverUndertakesRideEntityService();

        int newId = jdures.addDriveSafely(this);


        this.setRideId(newId);

        return newId;

    }

    public void doCrudAction(ActionEvent evt) {

        HTTPUtil hru = new HTTPUtil();

        log.log(Level.FINE, "doCrudAction Event : " + evt.toString());

        String action = hru.getParameterSingleValue((new CRUDConstants()).getParamNameCrudAction());
        log.log(Level.FINE, "Param Action : " + action);

        String id = hru.getParameterSingleValue((new CRUDConstants()).getParamNameCrudId());
        log.log(Level.FINE, "Param ID     : " + id);


        // Deleting is not yet implemented,  
        //
        // if (CRUDConstants.PARAM_VALUE_CRUD_DELETE.equals(action)) {
        //    this.delete(new Integer(id).intValue());
        // }


        if (CRUDConstants.PARAM_VALUE_CRUD_CREATE.equals(action)) {
            this.addToDB();
        }

    }

    /**
     * Returns a list of Matching Ride Request for this drive Offers
     *
     * @return Returns a list of Matching Ride Requests for this Offer
     */
    public List<JMatchingEntity> getMatches() {

        if (this.getRideId() == null) {
            log.log(Level.SEVERE, "Cannot return matches, My rideId is null!!, returning empty list.");
            return new LinkedList<JMatchingEntity>();
        }

        return (new JMatchingEntityService()).getMatchesForOffer(this.getRideId());
    }

    /**
     * Remove this Offer
     *
     * @return "driver" to jump back to driver's
     */
    public String remove() {


        boolean res = new JDriverUndertakesRideEntityService().safelyRemoveDrive(this);

        if (res) {

            // jump back to driver's startpage
            return "driver";
        } else {

            // Todo: add message why removal was unsuccessful

            return null;
        }



    }

    /**
     * Invalidate this offer
     *
     * @return "driver" to jump back to driver's
     */
    public String invalidate() {


        boolean res = new JDriverUndertakesRideEntityService().invalidateOfferSavely(this.getRideId());

        if (res) {
            // jump back to driver's startpage
            return "driver";
        } else {
            // Todo: add message why removal was unsuccessful      
            return null;
        }

    }

    /**
     * Returns true, if this drive has been updated
     *
     * @return Returns the Number of OpenMatches for this RideRequest
     */
    public boolean getDriveUpdated() {
        return (new JDriverUndertakesRideEntityService()).isDriveUpdated(this.getRideId());
    }

    /**
     * Short message to be displayed if ride has an update
     */
    public String getUpdatedShortcut() {


        if (this.getDriveUpdated()) {
            return " " + propertiesLoader.getMessagesProps().getProperty("updatedRideShort");
        }

        return "  ";
    }

    /**
     * Returns the Number of OpenMatches for this RideRequest
     *
     * @return Returns the Number of OpenMatches for this RideRequest
     */
    public int getNoMatches() {
        return this.getMatches().size();
    }

    /**
     * @return Returns true, if the number of Matches is > 0, else false
     *
     */
    public boolean getHasMatches() {
        return this.getMatches().size() > 0;
    }

    /**
     * Return a list of Drive offers starting after given starttime. Returned
     * list is ordered descending by offer's starttime.
     *
     * @param starttime
     * @return
     */
    public List<JDriverUndertakesRideEntity> getDrivesAfterStarttime(Date starttime) {

        java.sql.Date sqltime = new java.sql.Date(starttime.getTime());
        return new JDriverUndertakesRideEntityService().getDrivesAfterTimeSafely(sqltime);
    }

    /**
     * Return a list of future Drive offers. Returned list is ordered descending
     * by offer's starttime.
     *
     * @return
     */
    public List<JDriverUndertakesRideEntity> getFutureDrives() {

        java.sql.Date sqltime = new java.sql.Date(System.currentTimeMillis());
        return new JDriverUndertakesRideEntityService().getDrivesAfterTimeSafely(sqltime);
    }

    /**
     * Determines the current Instance of RideSearchParamBean, and returns a
     * list of JDriverUndertakesRideEntity Objects realized between startDate
     * and endDate.
     *
     *
     * The list of rides actually returned will currently NOT be based on the
     * value of the RideSearchParamBean's searchType property.
     *
     * I.e, if this property equals:
     *
     *
     *
     *
     * @return List of Entities. See listing above.
     *
     */
    public List<JDriverUndertakesRideEntity> getDriveReport() {



        RideSearchParamsBean rspb0 = new RideSearchParamsBean();
        String beanName = rspb0.getBeanNameRidesearchparam();
        RideSearchParamsBean rspb = new RideSearchParamsBean().retrieveCurrentTimeInterval(beanName);


        if (rspb == null) {
            log.log(Level.SEVERE, this.getClass() + "RideSearchParamsBean is null, returning empty list");
            return new LinkedList<JDriverUndertakesRideEntity>();
        }


        // see, what kind of report we are supposed to show
        // currently not supported, but to come soon

        String reportType = rspb.getSearchType();
        java.sql.Date startDate = new java.sql.Date(rspb.getStartDate().getTime());
        java.sql.Date endDate = new java.sql.Date(rspb.getEndDate().getTime());


        return (new JDriverUndertakesRideEntityService()).getDrivesInInterval(startDate, endDate);



    }
} // class 
