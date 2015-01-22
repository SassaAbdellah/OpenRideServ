/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.riderundertakesride;

import de.avci.joride.utils.HTTPUtil;
import de.avci.joride.utils.PropertiesLoader;
import de.fhg.fokus.openride.matching.MatchingStatistics;
import de.fhg.fokus.openride.matching.RideNegotiationConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * A JBean class that can sort an unordered list of
 * JRideerundertakesRideEntities into a number of lists corresponding to the
 * respective rider states
 * (NEW,RIDER_REQUESTED,DRIVER_ACCEPTED,BOTH_ACCEPTED,COUNTERMANDED)
 *
 * Als
 *
 *
 *
 * @author jochen
 */
@Named("filteredRideLists")
@SessionScoped
public class JRideFilteredLists implements Serializable {

    private Logger log = Logger.getLogger("" + this.getClass());
    /**
     *
     *
     * /** Show all rides
     */
    private static final int DISPLAY_ALL = 0;

    public int getValueDisplayAll() {
        return DISPLAY_ALL;
    }
    /**
     * Show new ride
     */
    private static final int DISPLAY_NEW = 1;

    public int getValueDisplayNew() {
        return DISPLAY_NEW;
    }
    /**
     * Show rider requested rides
     */
    private static final int DISPLAY_RIDER_REQUESTED = 2;

    public int getValueDisplayRiderRequested() {
        return DISPLAY_RIDER_REQUESTED;
    }
    /**
     * Show driver accepted rides
     */
    private static final int DISPLAY_DRIVER_ACCEPTED = 3;

    public int getValueDisplayDriverAccepted() {
        return DISPLAY_DRIVER_ACCEPTED;
    }
    /**
     * Show both accepted rides
     */
    private static final int DISPLAY_BOTH_ACCEPTED = 4;

    public int getValueDisplayBothAccepted() {
        return DISPLAY_BOTH_ACCEPTED;
    }
    /**
     * Show rider rejected rides
     */
    private static final int DISPLAY_RIDER_REJECTED = 5;

    public int getValueDisplayRiderRejected() {
        return DISPLAY_RIDER_REJECTED;
    }
    /**
     * Show driver rejected rides
     */
    private static final int DISPLAY_DRIVER_REJECTED = 6;

    public int getValueDisplayDriverRejected() {
        return DISPLAY_DRIVER_REJECTED;
    }
    /**
     * Show both rejected rides
     */
    private static final int DISPLAY_BOTH_REJECTED = 7;

    public int getValueDisplayBothRejected() {
        return DISPLAY_BOTH_REJECTED;
    }
    /**
     * Show rider countermanded rides
     */
    private static final int DISPLAY_RIDER_COUNTERMANDED = 8;

    public int getValueDisplayRiderCountermanded() {
        return DISPLAY_RIDER_COUNTERMANDED;
    }
    /**
     * Show driver countermanded rides
     */
    private static final int DISPLAY_DRIVER_COUNTERMANDED = 9;

    public int getValueDisplayDriverCountermanded() {
        return DISPLAY_DRIVER_COUNTERMANDED;
    }
    /**
     * Show both countermanded rides
     */
    private static final int DISPLAY_BOTH_COUNTERMANDED = 10;

    public int getValueDisplayBothCountermanded() {
        return DISPLAY_BOTH_COUNTERMANDED;
    }
    /**
     * Show no more availlable rides
     */
    private static final int DISPLAY_NO_MORE_AVAILLABLE = 11;

    public int getValueDisplayUnavaillable() {
        return DISPLAY_NO_MORE_AVAILLABLE;
    }
    /**
     * Show updated rides
     */
    private static final int DISPLAY_UPDATED = 12;

    public int getValueDisplayUpdated() {
        return DISPLAY_UPDATED;
    }
    /**
     * Show rides of uncertain state
     */
    private static final int DISPLAY_UNCLEAR = 13;

    public int getValueDisplayUnclear() {
        return DISPLAY_UNCLEAR;
    }
    /**
     * Property to tell the list what should be displayed. Should be one of the
     * DISPLAY_XYZ_STATE variables.
     */
    private int displayMode = DISPLAY_ALL;

    public int getDisplayMode() {
        return this.displayMode;
    }

    public void setDisplayMode(int arg) {
        this.displayMode = arg;
    }

    /**
     * Determine and set the display mode from http request
     */
    public void updateDisplayMode() {

        String displayModeStr = (new HTTPUtil()).getParameterSingleValue(getParamNameDisplayMode());

        int displayModeArg = DISPLAY_ALL;

        try {
            displayModeArg = new Integer(displayModeStr).intValue();
            this.setDisplayMode(displayModeArg);
        } catch (java.lang.NumberFormatException exc) {
            log.log(Level.WARNING, "ID Parameter does not contain Numeric Value, value was : " + displayModeStr);
        }

    }
    /**
     * Name of the http parameter that sets the display mode
     */
    private static final String paramNameDisplayMode = "DISPLAYMODE";

    public String getParamNameDisplayMode() {
        return paramNameDisplayMode;
    }

    /**
     * Return one of the Filtered Lists, depending on the state of the
     * displayMode Property.
     *
     */
    public List<JRiderUndertakesRideEntity> getListForDisplayMode() {

        int dm = this.getDisplayMode();

        if (dm == DISPLAY_ALL) {// Show all rides
            return this.getAllRides();
        }

        if (dm == DISPLAY_NEW) { // Show new rides   
            return this.getNewRides();
        }
        if (dm == DISPLAY_RIDER_REQUESTED) { // Show rider requested rides
            return this.getRiderRequestedRides();
        }
        if (dm == DISPLAY_DRIVER_ACCEPTED) { // Show driver accepted rides
            return this.getDriverAcceptedRides();
        }
        if (dm == DISPLAY_BOTH_ACCEPTED) { // Show both accepted rides
            return this.getBothAcceptedRides();
        }
        if (dm == DISPLAY_RIDER_REJECTED) { // Show rider rejected rides
            return this.getRiderRejectedRides();
        }
        if (dm == DISPLAY_DRIVER_REJECTED) { // Show driver rejected rides
            return this.getDriverRejectedRides();
        }
        if (dm == DISPLAY_BOTH_REJECTED) { // Show both rejected rides
            return this.getBothRejectedRides();
        }
        if (dm == DISPLAY_RIDER_COUNTERMANDED) { // Show rider countermanded rides
            return this.getRiderCountermandedRides();

        }
        if (dm == DISPLAY_DRIVER_COUNTERMANDED) { // Show driver countermanded rides
            return this.getDriverCountermandedRides();
        }
        if (dm == DISPLAY_BOTH_COUNTERMANDED) { // Show both countermanded rides
            return this.getBothCountermandedRides();
        }
        if (dm == DISPLAY_NO_MORE_AVAILLABLE) { // Show no more availlable rides
            return this.getUnavaillableRides();
        }
        if (dm == DISPLAY_UPDATED) { // Show updated rides
            return this.getUpdatedRides();
        }
        if (dm == DISPLAY_UNCLEAR) {  // Show rides of uncertain state
            return this.getUnclearRides();
        }

        throw new Error("Cannot return list for DisplayMode, this should not happen. Display Mode was :" + this.displayMode);
    }

    /**
     * Return an appropriate headline for the list display, depending on the
     * state of the "displayMode" Property.
     *
     */
    public String getHeaderForDisplayMode() {

      
        Locale locale=new HTTPUtil().detectBestLocale();
        Properties msgs = PropertiesLoader.getMessageProperties(locale);

        int dm = this.getDisplayMode();

        if (dm == DISPLAY_ALL) {// Show all rides
            return msgs.getProperty("rideListAllRequests");
        }

        if (dm == DISPLAY_NEW) { // Show new rides   
            return msgs.getProperty("rideListNewRequests");
        }
        if (dm == DISPLAY_RIDER_REQUESTED) { // Show rider requested rides
            return msgs.getProperty("rideListRiderAcceptedRequests");
        }
        if (dm == DISPLAY_DRIVER_ACCEPTED) { // Show driver accepted rides
            return msgs.getProperty("rideListDriverAcceptedOffers");
        }
        if (dm == DISPLAY_BOTH_ACCEPTED) { // Show both accepted rides
            return msgs.getProperty("rideListBothAcceptedOffers");
        }
        if (dm == DISPLAY_RIDER_REJECTED) { // Show rider rejected rides
            return msgs.getProperty("rideListRiderRejectedRequests");
        }
        if (dm == DISPLAY_DRIVER_REJECTED) { // Show driver rejected rides
            return msgs.getProperty("rideListDriverRejectedOffers");
        }
        if (dm == DISPLAY_BOTH_REJECTED) { // Show both rejected rides
            return msgs.getProperty("rideListBothRejectedOffers");
        }
        if (dm == DISPLAY_RIDER_COUNTERMANDED) { // Show rider countermanded rides
            return msgs.getProperty("rideListRiderCountermandedRequests");
        }
        if (dm == DISPLAY_DRIVER_COUNTERMANDED) { // Show driver countermanded rides
            return msgs.getProperty("rideListDriverCountermandedOffers");
        }
        if (dm == DISPLAY_BOTH_COUNTERMANDED) { // Show both countermanded rides
            return msgs.getProperty("rideListBothCountermandedOffers");
        }
        if (dm == DISPLAY_NO_MORE_AVAILLABLE) { // Show no more availlable rides
            return msgs.getProperty("rideListUnavaillableRides");
        }
        if (dm == DISPLAY_UPDATED) { // Show updated rides
            return msgs.getProperty("rideListUpdatedRequests");
        }
        if (dm == DISPLAY_UNCLEAR) { // Show rides of uncertain state
            return msgs.getProperty("rideListUnclearRides");
        }

        throw new Error("Cannot return list for DisplayMode, this should not happen. Display Mode was :" + this.displayMode);
    }
    /**
     * One Comparator to sort whatever Lists of JRideUndertakesRideEntity we'll
     * have to handle.
     */
    StartTimeFirstSorter sorter = new StartTimeFirstSorter();
    /**
     * All Rides that this List manages, no filtering by state
     */
    private List<JRiderUndertakesRideEntity> allRides;

    public List<JRiderUndertakesRideEntity> getAllRides() {
        return this.allRides;
    }

    /**
     * Nontrivial setter. In addition to setting the list, it will also take
     * care to sort it by ride.startTimeFirst.
     *
     * @param argList
     */
    public void setAllRides(List<JRiderUndertakesRideEntity> argList) {
        this.allRides = argList;
        this.update();
    }

    /**
     * @return size of the "allRides" list.
     */
    public int getNumberOfAllRides() {
        return this.getAllRides().size();
    }

    /**
     * @return true, if there are rides at all
     */
    public boolean hasRides() {
        return this.getAllRides().size() > 0;
    }

    /**
     * Resort all the rides into respective Lists by Category this is called
     * after setAllRides(...) and may be called programmatically, if some states
     * are likely to have changed.
     *
     */
    private void update() {

        this.newRides = new ArrayList<JRiderUndertakesRideEntity>();
        this.riderRequestedRides = new ArrayList<JRiderUndertakesRideEntity>();
        this.driverAcceptedRides = new ArrayList<JRiderUndertakesRideEntity>();
        this.bothAcceptedRides = new ArrayList<JRiderUndertakesRideEntity>();

        this.riderRejectedRides = new ArrayList<JRiderUndertakesRideEntity>();
        this.driverRejectedRides = new ArrayList<JRiderUndertakesRideEntity>();
        this.bothRejectedRides = new ArrayList<JRiderUndertakesRideEntity>();

        this.riderCountermandedRides = new ArrayList<JRiderUndertakesRideEntity>();
        this.driverCountermandedRides = new ArrayList<JRiderUndertakesRideEntity>();
        this.bothCountermandedRides = new ArrayList<JRiderUndertakesRideEntity>();

        this.unavaillableRides = new ArrayList<JRiderUndertakesRideEntity>();
        this.unclearRides = new ArrayList<JRiderUndertakesRideEntity>();
        this.updatedRides = new ArrayList<JRiderUndertakesRideEntity>();

        //sorting list here means that all the sublists will be sorted too
        Collections.sort(this.getAllRides(), sorter);

        for (JRiderUndertakesRideEntity ride : this.getAllRides()) {

            MatchingStatistics ms = ride.getMatchingStatistics();


            RideNegotiationConstants state = ms.getRideMatchingState();


            if (state == RideNegotiationConstants.STATE_NEW) {
                newRides.add(ride);
            }

            // *********** Requested/Accepted/Confirmed

            if (state == RideNegotiationConstants.STATE_RIDER_REQUESTED) {
                riderRequestedRides.add(ride);
            }

            if (state == RideNegotiationConstants.STATE_DRIVER_ACCEPTED) {
                driverAcceptedRides.add(ride);
            }

            if (state == RideNegotiationConstants.STATE_CONFIRMED_BOTH) {
                bothAcceptedRides.add(ride);
            }


            // *********** Rejected rides *******

            if (state == RideNegotiationConstants.STATE_RIDER_REJECTED) {
                riderRequestedRides.add(ride);
            }

            if (state == RideNegotiationConstants.STATE_DRIVER_REJECTED) {
                driverRejectedRides.add(ride);
            }

            if (state == RideNegotiationConstants.STATE_REJECTED_BOTH) {
                bothRejectedRides.add(ride);
            }


            // ********* Countermanded Rides  ******

            if (state == RideNegotiationConstants.STATE_COUNTERMANDED_RIDER) {
                riderCountermandedRides.add(ride);
            }

            if (state == RideNegotiationConstants.STATE_COUNTERMANDED_DRIVER) {
                driverCountermandedRides.add(ride);
            }


            //********** ugly states ******************

            if (state == RideNegotiationConstants.STATE_UNAVAILLABLE) {
                unavaillableRides.add(ride);
            }

            if (state == RideNegotiationConstants.STATE_UNCLEAR) {
                unclearRides.add(ride);
            }

            if (ride.getRideUpdated()) {
                this.updatedRides.add(ride);
            }


        } // for 

    }
    /**
     * all new rides, i.e those that have both rider and DriverState nonAdapted
     */
    private List<JRiderUndertakesRideEntity> newRides = null;

    public List<JRiderUndertakesRideEntity> getNewRides() {
        return this.newRides;
    }

    /**
     * @return size of the newRides list
     */
    public int getNumberOfNewRides() {
        return this.getNewRides().size();
    }

    /**
     * @return true, if there are rides of state "new", else false
     */
    public boolean hasNewRides() {
        return this.getNewRides().size() > 0;
    }
    /**
     * all rider-accepted rides, i.e those that have been accepted by rider, but
     * not by driver
     */
    private List<JRiderUndertakesRideEntity> riderRequestedRides = null;

    public List<JRiderUndertakesRideEntity> getRiderRequestedRides() {
        return this.riderRequestedRides;
    }

    /**
     * @return size of the acceptedRides list
     */
    public int getNumberOfRiderRequestedRides() {
        return this.getRiderRequestedRides().size();
    }

    /**
     * @return true, if there are rides of state "rider requested" else false
     */
    public boolean hasRiderRequestedRides() {
        return this.getRiderRequestedRides().size() > 0;
    }
    /**
     * all driver-accepted rides, i.e those that have been accepted by driver,
     * but not by rider
     */
    private List<JRiderUndertakesRideEntity> driverAcceptedRides = null;

    public List<JRiderUndertakesRideEntity> getDriverAcceptedRides() {
        return this.driverAcceptedRides;
    }

    /**
     * @return size of the acceptedRides list
     */
    public int getNumberOfDriverAcceptedRides() {
        return this.getDriverAcceptedRides().size();
    }

    /**
     * @return true, if there are rides of state unclear, else false
     */
    public boolean hasDriverAcceptedRides() {
        return this.getDriverAcceptedRides().size() > 0;
    }
    /**
     * all Rides accepted by both, rider and driver
     */
    private List<JRiderUndertakesRideEntity> bothAcceptedRides = null;

    public List<JRiderUndertakesRideEntity> getBothAcceptedRides() {
        return this.bothAcceptedRides;
    }

    /**
     * @return size of the bothAcceptedRides list
     */
    public int getNumberOfBothAcceptedRides() {
        return this.getBothAcceptedRides().size();
    }

    /**
     * @return true, if there are rides of state "accepted both", else false
     */
    public boolean hasBothAcceptedRides() {
        return this.getBothAcceptedRides().size() > 0;
    }
    /**
     * all rider-accepted rides, i.e those that have been accepted by rider, but
     * not by driver
     */
    private List<JRiderUndertakesRideEntity> riderRejectedRides = null;

    public List<JRiderUndertakesRideEntity> getRiderRejectedRides() {
        return this.riderRejectedRides;
    }

    /**
     * @return size of the acceptedRides list
     */
    public int getNumberOfRiderRejectedRides() {
        return this.getRiderRejectedRides().size();
    }

    /**
     * @return true, if there are rides of state "rider requested" else false
     */
    public boolean hasRiderRejectedRides() {
        return this.getRiderRejectedRides().size() > 0;
    }
    /**
     * all driver-accepted rides, i.e those that have been accepted by driver,
     * but not by rider
     */
    private List<JRiderUndertakesRideEntity> driverRejectedRides = null;

    public List<JRiderUndertakesRideEntity> getDriverRejectedRides() {
        return this.driverRejectedRides;
    }

    /**
     * @return size of the acceptedRides list
     */
    public int getNumberOfDriverRejectedRides() {
        return this.getDriverRejectedRides().size();
    }

    /**
     * @return true, if there are rides of state unclear, else false
     */
    public boolean hasDriverRejectedRides() {
        return this.getDriverRejectedRides().size() > 0;
    }
    /**
     * all Rides accepted by both, rider and driver
     */
    private List<JRiderUndertakesRideEntity> bothRejectedRides = null;

    public List<JRiderUndertakesRideEntity> getBothRejectedRides() {
        return this.bothRejectedRides;
    }

    /**
     * @return size of the bothAcceptedRides list
     */
    public int getNumberOfBothRejectedRides() {
        return this.getBothRejectedRides().size();
    }

    /**
     * @return true, if there are rides of state "accepted both", else false
     */
    public boolean hasBothRejectedRides() {
        return this.getBothRejectedRides().size() > 0;
    }
    /**
     * all Rides countermanded by rider.
     */
    private List<JRiderUndertakesRideEntity> riderCountermandedRides = null;

    public List<JRiderUndertakesRideEntity> getRiderCountermandedRides() {
        return this.riderCountermandedRides;
    }

    /**
     * @return size of the acceptedRides list
     */
    public int getNumberOfRiderCountermandedRides() {
        return this.getRiderCountermandedRides().size();
    }

    /**
     * @return true, if there are rides of state countermanded, else false
     */
    public boolean hasRiderCountermandedRides() {
        return this.getRiderCountermandedRides().size() > 0;
    }
    /**
     * all Rides countermanded by driver.
     */
    private List<JRiderUndertakesRideEntity> driverCountermandedRides = null;

    public List<JRiderUndertakesRideEntity> getDriverCountermandedRides() {
        return this.driverCountermandedRides;
    }

    /**
     * @return size of the acceptedRides list
     */
    public int getNumberOfDriverCountermandedRides() {
        return this.getDriverCountermandedRides().size();
    }

    /**
     * @return true, if there are rides of state countermanded, else false
     */
    public boolean hasDriverCountermandedRides() {
        return this.getDriverCountermandedRides().size() > 0;
    }
    /**
     * all Rides countermanded by both, rider and driver.
     */
    private List<JRiderUndertakesRideEntity> bothCountermandedRides = null;

    public List<JRiderUndertakesRideEntity> getBothCountermandedRides() {
        return this.bothCountermandedRides;
    }

    /**
     * @return size of the acceptedRides list
     */
    public int getNumberOfBothCountermandedRides() {
        return this.getBothCountermandedRides().size();
    }

    /**
     * @return true, if there are rides of state countermanded, else false
     */
    public boolean hasBothCountermandedRides() {
        return this.getBothCountermandedRides().size() > 0;
    }
    /**
     * all unavaillable rides, probably rarely displayed to outside, but maybe
     * useful for debugging.
     *
     */
    private List<JRiderUndertakesRideEntity> unavaillableRides;

    public List<JRiderUndertakesRideEntity> getUnavaillableRides() {
        return this.unavaillableRides;
    }

    /**
     * @return size of the acceptedRides list
     */
    public int getNumberOfUnavaillableRides() {
        return this.getUnavaillableRides().size();
    }

    /**
     * @return true, if there are rides of state unclear, else false
     */
    public boolean hasUnavaillableRides() {
        return this.getUnavaillableRides().size() > 0;
    }
    /**
     * all rides with state unclear, probably seldom displayed to outside, but
     * maybe useful for debugging.
     *
     */
    private List<JRiderUndertakesRideEntity> unclearRides;

    public List<JRiderUndertakesRideEntity> getUnclearRides() {
        return this.unclearRides;
    }

    /**
     * @return size of the acceptedRides list
     */
    public int getNumberOfUnclearRides() {
        return this.getUnclearRides().size();
    }

    /**
     * @return true, if there are rides of state unclear, else false
     */
    public boolean hasUnclearRides() {
        return this.getUnclearRides().size() > 0;
    }
    /**
     * List of all rides that got an update.
     *
     *
     */
    private List<JRiderUndertakesRideEntity> updatedRides;

    public List<JRiderUndertakesRideEntity> getUpdatedRides() {
        return this.updatedRides;
    }

    /**
     * @return size of the acceptedRides list
     */
    public int getNumberOfUpdatedRides() {
        return this.getUpdatedRides().size();
    }

    /**
     * @return true, if there are rides of state unclear, else false
     */
    public boolean hasUpdatedRides() {
        return this.getUpdatedRides().size() > 0;
    }

    public String getDebugPrintout() {

        StringBuffer buf = new StringBuffer();

        buf.append("\nRideList Debug  Output : \n");

        buf.append("\n");
        buf.append("\nALL Rides : ");
        buf.append("\nNumber of : " + this.getNumberOfAllRides());
        buf.append("\nExist     : " + this.hasRides());
        buf.append("\n");
        buf.append("\n");
        buf.append("\nNEW Rides : ");
        buf.append("\nNumber of : " + this.getNumberOfNewRides());
        buf.append("\nExist     : " + this.hasNewRides());
        buf.append("\n");

        // Requested/Accepted/Confirmed
        buf.append("\n");
        buf.append("\nRIDER REQUESTED Rides: ");
        buf.append("\nNumber of : " + this.getNumberOfRiderRequestedRides());
        buf.append("\nExist     : " + this.hasRiderRequestedRides());
        buf.append("\n");
        buf.append("\n");
        buf.append("\nDRIVER ACCEPTED Rides: ");
        buf.append("\nNumber of : " + this.getNumberOfDriverAcceptedRides());
        buf.append("\nExist     : " + this.hasDriverAcceptedRides());
        buf.append("\n");
        buf.append("\n");
        buf.append("\nBOTH ACCEPTED Rides: ");
        buf.append("\nNumber of : " + this.getNumberOfBothAcceptedRides());
        buf.append("\nExist     : " + this.hasBothAcceptedRides());
        buf.append("\n");


        // Rejected
        buf.append("\n");
        buf.append("\nRIDER REJECTED Rides: ");
        buf.append("\nNumber of : " + this.getNumberOfRiderRejectedRides());
        buf.append("\nExist     : " + this.hasRiderRejectedRides());
        buf.append("\n");
        buf.append("\n");
        buf.append("\nDRIVER REJECTED Rides: ");
        buf.append("\nNumber of : " + this.getNumberOfDriverRejectedRides());
        buf.append("\nExist     : " + this.hasDriverRejectedRides());
        buf.append("\n");
        buf.append("\n");
        buf.append("\nBOTH REJECTED Rides: ");
        buf.append("\nNumber of : " + this.getNumberOfBothRejectedRides());
        buf.append("\nExist     : " + this.hasBothRejectedRides());
        buf.append("\n");

        buf.append("\n");
        buf.append("\nRIDER COUNTERMANDED Rides: ");
        buf.append("\nNumber of : " + this.getNumberOfRiderCountermandedRides());
        buf.append("\nExist     : " + this.hasRiderCountermandedRides());
        buf.append("\n");
        buf.append("\nDRIVER COUNTERMANDED Rides: ");
        buf.append("\nNumber of : " + this.getNumberOfDriverCountermandedRides());
        buf.append("\nExist     : " + this.hasDriverCountermandedRides());
        buf.append("\n");
        buf.append("\n");
        buf.append("\nBOTH COUNTERMANDED Rides: ");
        buf.append("\nNumber of : " + this.getNumberOfBothCountermandedRides());
        buf.append("\nExist     : " + this.hasBothCountermandedRides());
        buf.append("\n");

        buf.append("\n");
        buf.append("\nUNAVAILLABLE Rides: ");
        buf.append("\nNumber of : " + this.getNumberOfUnavaillableRides());
        buf.append("\nExist     : " + this.hasUnavaillableRides());
        buf.append("\n");
        buf.append("\n");
        buf.append("\nUPDATED Rides: ");
        buf.append("\nNumber of : " + this.getNumberOfUpdatedRides());
        buf.append("\nExist     : " + this.hasUpdatedRides());
        buf.append("\n");
        buf.append("\n");
        buf.append("\nUNCLEAR Rides: ");
        buf.append("\nNumber of : " + this.getNumberOfUnclearRides());
        buf.append("\nExist     : " + this.hasUnclearRides());
        buf.append("\n");

        return buf.toString();
    }

    public void updateFutureRides() {
        new JRiderUndertakesRideEntityService().updateJFilteredRideList(this);




    }

    /**
     * Comparator to sort lists of JRiderUndertakesRideEntities by starttime
     * earliest.
     */
    class StartTimeFirstSorter implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {

            if (!(o1 instanceof JRiderUndertakesRideEntity)) {
                throw new Error("StartTimeFirstSorter cannot sort anything else but Rides!");
            }

            if (!(o2 instanceof JRiderUndertakesRideEntity)) {
                throw new Error("StartTimeFirstSorter cannot sort anything else but Rides!");
            }

            JRiderUndertakesRideEntity j1 = (JRiderUndertakesRideEntity) o1;
            JRiderUndertakesRideEntity j2 = (JRiderUndertakesRideEntity) o2;

            Long t1 = j1.getStarttimeEarliest().getTime();
            Long t2 = j2.getStarttimeEarliest().getTime();

            if (t1 > t2) {
                return 1;
            }
            if (t2 < t1) {
                return -1;
            }
            return 0;
        }
    } // Comparator
}
