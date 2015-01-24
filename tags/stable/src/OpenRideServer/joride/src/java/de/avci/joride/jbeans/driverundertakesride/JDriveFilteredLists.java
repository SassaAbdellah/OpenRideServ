/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.driverundertakesride;

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
 * JDriveerundertakesDriveEntities into a number of lists corresponding to the
 * respective rider states
 * (NEW,RIDER_REQUESTED,DRIVER_ACCEPTED,BOTH_ACCEPTED,COUNTERMANDED)
 *
 * Als
 *
 *
 *
 * @author jochen
 */
@Named("filteredDriveLists")
@SessionScoped
public class JDriveFilteredLists implements Serializable {

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
    public List<JDriverUndertakesRideEntity> getListForDisplayMode() {

        int dm = this.getDisplayMode();

        if (dm == DISPLAY_ALL) {// Show all rides
            return this.getAllDrives();
        }

        if (dm == DISPLAY_NEW) { // Show new rides   
            return this.getNewDrives();
        }
        if (dm == DISPLAY_RIDER_REQUESTED) { // Show rider requested rides
            return this.getRiderRequestedDrives();
        }
        if (dm == DISPLAY_DRIVER_ACCEPTED) { // Show driver accepted rides
            return this.getDriverAcceptedDrives();
        }
        if (dm == DISPLAY_BOTH_ACCEPTED) { // Show both accepted rides
            return this.getBothAcceptedDrives();
        }
        if (dm == DISPLAY_RIDER_REJECTED) { // Show rider rejected rides
            return this.getDriverRejectedDrives();
        }
        if (dm == DISPLAY_DRIVER_REJECTED) { // Show driver rejected rides
            return this.getDriverRejectedDrives();
        }
        if (dm == DISPLAY_BOTH_REJECTED) { // Show both rejected rides
            return this.getBothRejectedDrives();
        }
        if (dm == DISPLAY_RIDER_COUNTERMANDED) { // Show rider countermanded rides
            return this.getDriverCountermandedDrives();

        }
        if (dm == DISPLAY_DRIVER_COUNTERMANDED) { // Show driver countermanded rides
            return this.getDriverCountermandedDrives();
        }
        if (dm == DISPLAY_BOTH_COUNTERMANDED) { // Show both countermanded rides
            return this.getBothCountermandedDrives();
        }
        if (dm == DISPLAY_NO_MORE_AVAILLABLE) { // Show no more availlable rides
            return this.getUnavaillableDrives();
        }
        if (dm == DISPLAY_UPDATED) { // Show updated rides
            return this.getUpdatedDrives();
        }
        if (dm == DISPLAY_UNCLEAR) {  // Show rides of uncertain state
            return this.getUnclearDrives();
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
            return msgs.getProperty("driveListAllOffers");
        }

        if (dm == DISPLAY_NEW) { // Show new rides   
            return msgs.getProperty("driveListNewOffers");
        }
        if (dm == DISPLAY_RIDER_REQUESTED) { // Show rider requested rides
            return msgs.getProperty("driveListDriverAcceptedOffers");
        }
        if (dm == DISPLAY_DRIVER_ACCEPTED) { // Show driver accepted rides
            return msgs.getProperty("driveListDriverAcceptedOffers");
        }
        if (dm == DISPLAY_BOTH_ACCEPTED) { // Show both accepted rides
            return msgs.getProperty("driveListBothAcceptedOffers");
        }
        if (dm == DISPLAY_RIDER_REJECTED) { // Show rider rejected rides
            return msgs.getProperty("driveListRiderRejectedOffers");
        }
        if (dm == DISPLAY_DRIVER_REJECTED) { // Show driver rejected rides
            return msgs.getProperty("driveListDriverRejectedOffers");
        }
        if (dm == DISPLAY_BOTH_REJECTED) { // Show both rejected rides
            return msgs.getProperty("driveListBothRejectedOffers");
        }
        if (dm == DISPLAY_RIDER_COUNTERMANDED) { // Show rider countermanded rides
            return msgs.getProperty("driveListDriverCountermandedOffers");
        }
        if (dm == DISPLAY_DRIVER_COUNTERMANDED) { // Show driver countermanded rides
            return msgs.getProperty("driveListDriverCountermandedOffers");
        }
        if (dm == DISPLAY_BOTH_COUNTERMANDED) { // Show both countermanded rides
            return msgs.getProperty("driveListBothCountermandedOffers");
        }
        if (dm == DISPLAY_NO_MORE_AVAILLABLE) { // Show no more availlable rides
            return msgs.getProperty("driveListUnavaillableOffers");
        }
        if (dm == DISPLAY_UPDATED) { // Show updated rides
            return msgs.getProperty("driveListUpdatedOffers");
        }
        if (dm == DISPLAY_UNCLEAR) { // Show rides of uncertain state
            return msgs.getProperty("driveListUnclearOffers");
        }

        throw new Error("Cannot return list for DisplayMode, this should not happen. Display Mode was :" + this.displayMode);
    }
    /**
     * One Comparator to sort whatever Lists of JDriveUndertakesDriveEntity we'll
     * have to handle.
     */
    JDriveFilteredLists.StartTimeFirstSorter sorter = new JDriveFilteredLists.StartTimeFirstSorter();
    /**
     * All Drives that this List manages, no filtering by state
     */
    private List<JDriverUndertakesRideEntity> allDrives;

    public List<JDriverUndertakesRideEntity> getAllDrives() {
        return this.allDrives;
    }

    /**
     * Nontrivial setter. In addition to setting the list, it will also take
     * care to sort it by ride.startTimeFirst.
     *
     * @param argList
     */
    public void setAllDrives(List<JDriverUndertakesRideEntity> argList) {
        this.allDrives = argList;
        this.update();
    }

    /**
     * @return size of the "allDrives" list.
     */
    public int getNumberOfAllDrives() {
        return this.getAllDrives().size();
    }

    /**
     * @return true, if there are rides at all
     */
    public boolean hasDrives() {
        return this.getAllDrives().size() > 0;
    }

    /**
     * Resort all the rides into respective Lists by Category this is called
     * after setAllDrives(...) and may be called programmatically, if some states
     * are likely to have changed.
     *
     */
    private void update() {

        this.newDrives = new ArrayList<JDriverUndertakesRideEntity>();
        this.riderRequestedDrives = new ArrayList<JDriverUndertakesRideEntity>();
        this.driverAcceptedDrives = new ArrayList<JDriverUndertakesRideEntity>();
        this.bothAcceptedDrives = new ArrayList<JDriverUndertakesRideEntity>();

        this.riderRejectedDrives = new ArrayList<JDriverUndertakesRideEntity>();
        this.driverRejectedDrives = new ArrayList<JDriverUndertakesRideEntity>();
        this.bothRejectedDrives = new ArrayList<JDriverUndertakesRideEntity>();

        this.riderCountermandedDrives = new ArrayList<JDriverUndertakesRideEntity>();
        this.driverCountermandedDrives = new ArrayList<JDriverUndertakesRideEntity>();
        this.bothCountermandedDrives = new ArrayList<JDriverUndertakesRideEntity>();

        this.unavaillableDrives = new ArrayList<JDriverUndertakesRideEntity>();
        this.unclearDrives = new ArrayList<JDriverUndertakesRideEntity>();
        this.updatedDrives = new ArrayList<JDriverUndertakesRideEntity>();

        //sorting list here means that all the sublists will be sorted too
        Collections.sort(this.getAllDrives(), sorter);

        for (JDriverUndertakesRideEntity ride : this.getAllDrives()) {

            MatchingStatistics ms = ride.getMatchingStatistics();
            
            if ( ms.getNotAdaptedBoth() ==  ms.getNumberOfMatches()) {
                newDrives.add(ride);
            }

            // *********** Requested/Accepted/Confirmed

            if (ms.getAcceptedRider()>0) {
                riderRequestedDrives.add(ride);
            }

            if (ms.getAcceptedDriver()>0) {
                driverAcceptedDrives.add(ride);
            }

            if (ms.getAcceptedBoth()>0) {
                bothAcceptedDrives.add(ride);
            }


            // *********** Rejected rides *******

            if (ms.getRejectedRider()>0) {
                riderRejectedDrives.add(ride);
            }

            if (ms.getRejectedDriver()>0) {
                driverRejectedDrives.add(ride);
            }

            if (ms.getRejectedBoth()>0) {
                bothRejectedDrives.add(ride);
            }


            // ********* Countermanded Drives  ******
            
            if (ms.getCountermandedRider()>0) {
                riderCountermandedDrives.add(ride);
            }

            if (ms.getCountermandedDriver()>0) {
                driverCountermandedDrives.add(ride);
            }


            //********** ugly states ******************

            // TODO do something to determine unavaillable rides!
           

            if (ride.getDriveUpdated()) {
                this.updatedDrives.add(ride);
            }


        } // for 

    }
    /**
     * all new rides, i.e those that have both rider and DriverState nonAdapted
     */
    private List<JDriverUndertakesRideEntity> newDrives = null;

    public List<JDriverUndertakesRideEntity> getNewDrives() {
        return this.newDrives;
    }

    /**
     * @return size of the newDrives list
     */
    public int getNumberOfNewDrives() {
        return this.getNewDrives().size();
    }

    /**
     * @return true, if there are rides of state "new", else false
     */
    public boolean hasNewDrives() {
        return this.getNewDrives().size() > 0;
    }
    
    
    /**
     * all rider-accepted rides, i.e those that have been accepted by rider, but
     * not by driver
     */
    private List<JDriverUndertakesRideEntity> riderRequestedDrives = null;

    public List<JDriverUndertakesRideEntity> getRiderRequestedDrives() {
        return this.riderRequestedDrives;
    }

    /**
     * @return size of the acceptedDrives list
     */
    public int getNumberOfRiderRequestedDrives() {
        return this.getRiderRequestedDrives().size();
    }

    /**
     * @return true, if there are rides of state "rider requested" else false
     */
    public boolean hasRiderRequestedDrives() {
        return this.getRiderRequestedDrives().size() > 0;
    }
    
    
    /**
     * all driver-accepted rides, i.e those that have been accepted by driver,
     * but not by rider
     */
    private List<JDriverUndertakesRideEntity> driverAcceptedDrives = null;

    public List<JDriverUndertakesRideEntity> getDriverAcceptedDrives() {
        return this.driverAcceptedDrives;
    }

    /**
     * @return size of the acceptedDrives list
     */
    public int getNumberOfDriverAcceptedDrives() {
        return this.getDriverAcceptedDrives().size();
    }

    /**
     * @return true, if there are rides of state unclear, else false
     */
    public boolean hasDriverAcceptedDrives() {
        return this.getDriverAcceptedDrives().size() > 0;
    }
    /**
     * all Drives accepted by both, rider and driver
     */
    private List<JDriverUndertakesRideEntity> bothAcceptedDrives = null;

    public List<JDriverUndertakesRideEntity> getBothAcceptedDrives() {
        return this.bothAcceptedDrives;
    }

    /**
     * @return size of the bothAcceptedDrives list
     */
    public int getNumberOfBothAcceptedDrives() {
        return this.getBothAcceptedDrives().size();
    }

    /**
     * @return true, if there are rides of state "accepted both", else false
     */
    public boolean hasBothAcceptedDrives() {
        return this.getBothAcceptedDrives().size() > 0;
    }
    /**
     * all rider-rejected Drives, i.e those that have been accepted by rider, but
     * not by driver
     */
    private List<JDriverUndertakesRideEntity> riderRejectedDrives = null;

    public List<JDriverUndertakesRideEntity> getRiderRejectedDrives() {
        return this.riderRejectedDrives;
    }

    /**
     * @return size of the rejectedRides list
     */
    public int getNumberOfRiderRejectedDrives() {
        return this.getRiderRejectedDrives().size();
    }

    /**
     * @return true, if there are rides of state "rider rejected" else false
     */
    public boolean hasRiderRejectedDrives() {
        return this.getRiderRejectedDrives().size() > 0;
    }
    /**
     * all driver-accepted rides, i.e those that have been accepted by driver,
     * but not by rider
     */
    private List<JDriverUndertakesRideEntity> driverRejectedDrives = null;

    public List<JDriverUndertakesRideEntity> getDriverRejectedDrives() {
        return this.driverRejectedDrives;
    }

    /**
     * @return size of the acceptedDrives list
     */
    public int getNumberOfDriverRejectedDrives() {
        return this.getDriverRejectedDrives().size();
    }

    /**
     * @return true, if there are rides of state unclear, else false
     */
    public boolean hasDriverRejectedDrives() {
        return this.getDriverRejectedDrives().size() > 0;
    }
    /**
     * all Drives accepted by both, rider and driver
     */
    private List<JDriverUndertakesRideEntity> bothRejectedDrives = null;

    public List<JDriverUndertakesRideEntity> getBothRejectedDrives() {
        return this.bothRejectedDrives;
    }

    /**
     * @return size of the bothAcceptedDrives list
     */
    public int getNumberOfBothRejectedDrives() {
        return this.getBothRejectedDrives().size();
    }

    /**
     * @return true, if there are rides of state "accepted both", else false
     */
    public boolean hasBothRejectedDrives() {
        return this.getBothRejectedDrives().size() > 0;
    }
    /**
     * all Drives countermanded by rider.
     */
    private List<JDriverUndertakesRideEntity> riderCountermandedDrives = null;

    public List<JDriverUndertakesRideEntity> getRiderCountermandedDrives() {
        return this.riderCountermandedDrives;
    }

    /**
     * @return size of the acceptedDrives list
     */
    public int getNumberOfRiderCountermandedDrives() {
        return this.getRiderCountermandedDrives().size();
    }

    /**
     * @return true, if there are rides of state countermanded, else false
     */
    public boolean hasRiderCountermandedDrives() {
        return this.getRiderCountermandedDrives().size() > 0;
    }
    /**
     * all Drives countermanded by driver.
     */
    private List<JDriverUndertakesRideEntity> driverCountermandedDrives = null;

    public List<JDriverUndertakesRideEntity> getDriverCountermandedDrives() {
        return this.driverCountermandedDrives;
    }

    /**
     * @return size of the acceptedDrives list
     */
    public int getNumberOfDriverCountermandedDrives() {
        return this.getDriverCountermandedDrives().size();
    }

    /**
     * @return true, if there are rides of state countermanded, else false
     */
    public boolean hasDriverCountermandedDrives() {
        return this.getDriverCountermandedDrives().size() > 0;
    }
    /**
     * all Drives countermanded by both, rider and driver.
     */
    private List<JDriverUndertakesRideEntity> bothCountermandedDrives = null;

    public List<JDriverUndertakesRideEntity> getBothCountermandedDrives() {
        return this.bothCountermandedDrives;
    }

    /**
     * @return size of the acceptedDrives list
     */
    public int getNumberOfBothCountermandedDrives() {
        return this.getBothCountermandedDrives().size();
    }

    /**
     * @return true, if there are rides of state countermanded, else false
     */
    public boolean hasBothCountermandedDrives() {
        return this.getBothCountermandedDrives().size() > 0;
    }
    /**
     * all unavaillable rides, probably rarely displayed to outside, but maybe
     * useful for debugging.
     *
     */
    private List<JDriverUndertakesRideEntity> unavaillableDrives;

    public List<JDriverUndertakesRideEntity> getUnavaillableDrives() {
        return this.unavaillableDrives;
    }

    /**
     * @return size of the acceptedDrives list
     */
    public int getNumberOfUnavaillableDrives() {
        return this.getUnavaillableDrives().size();
    }

    /**
     * @return true, if there are rides of state unclear, else false
     */
    public boolean hasUnavaillableDrives() {
        return this.getUnavaillableDrives().size() > 0;
    }
    /**
     * all rides with state unclear, probably seldom displayed to outside, but
     * maybe useful for debugging.
     *
     */
    private List<JDriverUndertakesRideEntity> unclearDrives;

    public List<JDriverUndertakesRideEntity> getUnclearDrives() {
        return this.unclearDrives;
    }

    /**
     * @return size of the acceptedDrives list
     */
    public int getNumberOfUnclearDrives() {
        return this.getUnclearDrives().size();
    }

    /**
     * @return true, if there are rides of state unclear, else false
     */
    public boolean hasUnclearDrives() {
        return this.getUnclearDrives().size() > 0;
    }
    /**
     * List of all rides that got an update.
     *
     *
     */
    private List<JDriverUndertakesRideEntity> updatedDrives;

    public List<JDriverUndertakesRideEntity> getUpdatedDrives() {
        return this.updatedDrives;
    }

    /**
     * @return size of the acceptedDrives list
     */
    public int getNumberOfUpdatedDrives() {
        return this.getUpdatedDrives().size();
    }

    /**
     * @return true, if there are rides of state unclear, else false
     */
    public boolean hasUpdatedDrives() {
        return this.getUpdatedDrives().size() > 0;
    }

    public String getDebugPrintout() {

        StringBuffer buf = new StringBuffer();

        buf.append("\nDriveList Debug  Output : \n");

        buf.append("\n");
        buf.append("\nALL Drives : ");
        buf.append("\nNumber of : " + this.getNumberOfAllDrives());
        buf.append("\nExist     : " + this.hasDrives());
        buf.append("\n");
        buf.append("\n");
        buf.append("\nNEW Drives : ");
        buf.append("\nNumber of : " + this.getNumberOfNewDrives());
        buf.append("\nExist     : " + this.hasNewDrives());
        buf.append("\n");

        // Requested/Accepted/Confirmed
        buf.append("\n");
        buf.append("\nRIDER REQUESTED Drives: ");
        buf.append("\nNumber of : " + this.getNumberOfRiderRequestedDrives());
        buf.append("\nExist     : " + this.hasRiderRequestedDrives());
        buf.append("\n");
        buf.append("\n");
        buf.append("\nDRIVER ACCEPTED Drives: ");
        buf.append("\nNumber of : " + this.getNumberOfDriverAcceptedDrives());
        buf.append("\nExist     : " + this.hasDriverAcceptedDrives());
        buf.append("\n");
        buf.append("\n");
        buf.append("\nBOTH ACCEPTED Drives: ");
        buf.append("\nNumber of : " + this.getNumberOfBothAcceptedDrives());
        buf.append("\nExist     : " + this.hasBothAcceptedDrives());
        buf.append("\n");


        // Rejected
        buf.append("\n");
        buf.append("\nRIDER REJECTED Drives: ");
        buf.append("\nNumber of : " + this.getNumberOfDriverRejectedDrives());
        buf.append("\nExist     : " + this.hasDriverRejectedDrives());
        buf.append("\n");
        buf.append("\n");
        buf.append("\nDRIVER REJECTED Drives: ");
        buf.append("\nNumber of : " + this.getNumberOfDriverRejectedDrives());
        buf.append("\nExist     : " + this.hasDriverRejectedDrives());
        buf.append("\n");
        buf.append("\n");
        buf.append("\nBOTH REJECTED Drives: ");
        buf.append("\nNumber of : " + this.getNumberOfBothRejectedDrives());
        buf.append("\nExist     : " + this.hasBothRejectedDrives());
        buf.append("\n");

        buf.append("\n");
        buf.append("\nRIDER COUNTERMANDED Drives: ");
        buf.append("\nNumber of : " + this.getNumberOfDriverCountermandedDrives());
        buf.append("\nExist     : " + this.hasDriverCountermandedDrives());
        buf.append("\n");
        buf.append("\nDRIVER COUNTERMANDED Drives: ");
        buf.append("\nNumber of : " + this.getNumberOfDriverCountermandedDrives());
        buf.append("\nExist     : " + this.hasDriverCountermandedDrives());
        buf.append("\n");
        buf.append("\n");
        buf.append("\nBOTH COUNTERMANDED Drives: ");
        buf.append("\nNumber of : " + this.getNumberOfBothCountermandedDrives());
        buf.append("\nExist     : " + this.hasBothCountermandedDrives());
        buf.append("\n");

        buf.append("\n");
        buf.append("\nUNAVAILLABLE Drives: ");
        buf.append("\nNumber of : " + this.getNumberOfUnavaillableDrives());
        buf.append("\nExist     : " + this.hasUnavaillableDrives());
        buf.append("\n");
        buf.append("\n");
        buf.append("\nUPDATED Drives: ");
        buf.append("\nNumber of : " + this.getNumberOfUpdatedDrives());
        buf.append("\nExist     : " + this.hasUpdatedDrives());
        buf.append("\n");
        buf.append("\n");
        buf.append("\nUNCLEAR Drives: ");
        buf.append("\nNumber of : " + this.getNumberOfUnclearDrives());
        buf.append("\nExist     : " + this.hasUnclearDrives());
        buf.append("\n");

        return buf.toString();
    }

    public void updateFutureDrives() {
        new JDriverUndertakesRideEntityService().updateJFilteredDriveList(this);
    }

    /**
     * Comparator to sort lists of JDriverUndertakesDriveEntities by starttime
     * earliest.
     */
    class StartTimeFirstSorter implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {

            if (!(o1 instanceof JDriverUndertakesRideEntity)) {
                throw new Error("StartTimeFirstSorter cannot sort anything else but Drives!");
            }

            if (!(o2 instanceof JDriverUndertakesRideEntity)) {
                throw new Error("StartTimeFirstSorter cannot sort anything else but Drives!");
            }

            JDriverUndertakesRideEntity j1 = (JDriverUndertakesRideEntity) o1;
            JDriverUndertakesRideEntity j2 = (JDriverUndertakesRideEntity) o2;

            Long t1 = j1.getRideStarttime().getTime();
            Long t2 = j2.getRideStarttime().getTime();

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
