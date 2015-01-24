/*
    OpenRide -- Car Sharing 2.0
    Copyright (C) 2010  Fraunhofer Institute for Open Communication Systems (FOKUS)

    Fraunhofer FOKUS
    Kaiserin-Augusta-Allee 31
    10589 Berlin
    Tel: +49 30 3463-7000
    info@fokus.fraunhofer.de

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License Version 3 as
    published by the Free Software Foundation.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package de.fhg.fokus.openride.rides.driver;

import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.matching.MatchEntity;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;
import java.sql.Date;
import java.util.List;
import javax.ejb.Local;
import org.postgis.Point;

/**
 *
 * @author pab
 */
@Local
public interface DriverUndertakesRideControllerLocal {

    /**
     * 
     * @param cust_id                  Customer/Driver Id
     * @param ridestartPt              Coordinates of starting point 
     * @param rideendPt                Coordinates of endpoint
     * @param intermediatePoints       currently ignored, using waypoints and addWaypoint instead
     * @param WayPoints                Waypoints (driver defined)
     * @param ridestartTime            Date/Time when ride starts
     * @param rideComment              Driver's comment
     * @param acceptableDetourInMin    currently ignored, using acceptable Detour in Km instead
     * @param acceptableDetourKm       Acceptable Detour for picking up/dropping Riders in KM 
     * @param acceptableDetourPercent  currently ignored, using acceptable Detour in Km instead
     * @param offeredSeatsNo           Number of free seats to be offered
     * @param startptAddress           Human Readable address of starting point
     * @param endptAddress             Human Readable address of destination
     * 
     * @return  id of newly created driverundertakesridentity
     */
    
    public int addRide(
            int cust_id,
            Point ridestartPt,
            Point rideendPt,
            Point[] intermediatePoints,
            List <WaypointEntity>  waypoints,
            Date ridestartTime,
            String rideComment,
            Integer acceptableDetourInMin,
            Integer acceptableDetourKm,
            Integer acceptableDetourPercent,
            int offeredSeatsNo,
            String startptAddress,
            String endptAddress);

   
 

    /** Currently ignored. Meant to be implemented with car tracking
     * 
     */
    public void updateDriverPosition();

   
    public List<DriverUndertakesRideEntity> getDrives(String nickname);

    public List<DriverUndertakesRideEntity> getAllDrives();

    public List<DriverUndertakesRideEntity> getActiveDrives(String nickname);
    
    public List<DriverUndertakesRideEntity> getDrivesForDriver(String nickname);
    

    public DriverUndertakesRideEntity getCurrentDrive(String nickname);

    public List<RiderUndertakesRideEntity> getRidersForDrive(int driveId);

    public DriverUndertakesRideEntity getDriveByDriveId(int driveId);

    /**
     *  Found in fokus code. Hopefully not used. 
     *  Horrible implementation in DriverUndertakesRideControlerBean.
     *  Remove unless there is a good reason for having it
     * 
     * @deprecated hopefully not used
     */
    int updateRide(
            int rideId,
            int cust_id,
            Point ridestartPt,
            Point rideendPt,
            Point[] intermediatePoints,
            Date ridestartTime,
            String rideComment,
            Integer acceptableDetourInMin,
            Integer acceptableDetourKm,
            Integer acceptableDetourPercent,
            int offeredSeatsNo,
            String startptAddress,
            String endptAddress);

    MatchEntity rejectRider(int rideId, int riderrouteid);

    MatchEntity acceptRider(int rideid, int riderrouteid);

    MatchEntity acceptDriver(int rideid, int riderrouteid);

    MatchEntity rejectDriver(int rideid, int riderrouteid);

    boolean isDriveUpdated(int rideId);

    // -------- MATCHING / ROUTES --------
    /** Get list of matches for given ride with given states
     *  mainly used to get all confirmed matches for a given ride
     * 
     * @param rideId rideId of DriverundertakesrideEntity for which to find matchings
     * @param riderState  riderState which returned matches should have
     * @param driverState driverState which returned matches should have
     * @return 
     */
    public List<MatchEntity> getMatchesByRideIdAndState(int rideId, int riderState, int driverState);
 
    
    /** Fetch list of all Accepted matches. 
     *  Convenience method, should return the same as
     *  this.getMatchesByRideIdAndState(rideId, MatchEntity.ACCEPTED, MatchEntity.ACCEPTED);
     * 
     * @param rideId id of the DriverUndertakesRideEntity to be checked
     * 
     * @return  list of all Matches for this ride upon which a pickup has been agreed.
     * 
     */
    public List<MatchEntity> getAcceptedMatches(int rideId);

    
    // found in ORS without documentation
    List<MatchEntity> getMatches(int rideId, boolean setDriverAccess);
    //points used for computing matches
    List<DriveRoutepointEntity> getDriveRoutePoints(int driveId);
    // points for planning new route
    List<RoutePointEntity> getRequiredRoutePoints(int driveId);
    // all coordinates available in map data -> use for rendering route
    List<RoutePointEntity> getRoutePoints(int driveId);
    // update route - should be called together with setDriveRoutePoints
    void setRoutePoints(int driveId, List<RoutePointEntity> routePoints);
    // update route for matching algorithm - should be called together with setRoutePoints
    void setDriveRoutePoints(int rideId, List<DriveRoutepointEntity> routePoints);

    void callMatchingAlgorithm(int rideId, boolean setDriverAccess);

    List<DriverUndertakesRideEntity> getInactiveDrives(String nickname);

    List<DriverUndertakesRideEntity> getActiveOpenDrives(String nickname);
    
    /** Get all Drives for driver given by custId in time Interval 
     *  given by startDate, endDate
     * 
     * @param   custId      id of the driver
     * @param   startDate   start of interval
     * @param   endDate     end of interval
     * 
     * @return  list of all rides offered between startdate and end date
     * 
     */
    List<DriverUndertakesRideEntity> getDrivesInInterval(CustomerEntity custId,Date startDate,Date endDate );
    
       
    /**  Returns all drives for a given driver that have startpoints defined after a given date.
     * 
     * 
     * @param customerEntity    The Customer for which to get Drives.
     * @param rideStarttime     Timestamp that servers as a lower bound for starttime
     * 
     * @return active drives of user <code>nickname</code> after <code> rideStarttime </code>
     */
    public List<DriverUndertakesRideEntity> getDrivesAfterTime(CustomerEntity ce, Date rideStarttime);

    
    
    /** True, if the ride with given rideId is deletable,
     *  else false.
     * 
     * @param rideId
     * @return 
     */
    boolean isDeletable(int rideId);

    /** Invalidate/countermand Ride with given Id.
     *  
     *  That is: if ride has confirmed or countermanded matches, 
     *  then invalidate the ride (i.e: leave it in the database).
     *  If there are no relevant matching, then 
     *  delete the ride from the database.
     * 
     * 
     * @param rideId
     * @return  true, if countermanding was successful, else false.
     */
    public boolean invalidateRide(Integer rideId);
    
    
    
    /** Get waypoints for this ride.
     *  List returned should be expected to be sorted by routeIdx.
     *  (this is usually archieved by underlying JPA Query)
     * 
     * @param rideId rideId for ride we want to get the waypoints for
     * @return 
     */
    public List<WaypointEntity> getWaypoints(int rideId);
    
        
    /** Get waypoints for this ride.
     * 
     *   List returned should be expected to be sorted by routeIdx.
     *  (this is usually archieved by underlying JPA Query)
     * 
     * @param drive
     * @return 
     */
    public List<WaypointEntity> getWaypoints(DriverUndertakesRideEntity drive);

  
     /** Add Waypoint for given drive
      * add before the smalles position being larger then position parameter
     * 
     * @param rideId    Id of DriverUndertakesRideEntity to which the waypoint should be added
     * @param position  position where to add to list
     * @return 
     */
    public void addWaypoint(int rideId, WaypointEntity waypoint, int position);
    
    
     /** Remove waypoint given by routeIdx from Ride given by rideId
     * 
     * @param rideID
     * @param routeIdx 
     */
    public void removeWaypoint(int rideID, int routeIdx);
    
    /** Set the driver message inside a MatchEntity
     * 
     * @param rideId          rideId of the MatchEntity
     * @param riderRouteId    riderRouteId of the MatchEntity
     * @param message         message to be set
     */
    public void setDriverMessage(int rideId, int riderRouteId, String message);
    
    /** Set the rider message inside a MatchEntity
     * 
     * @param rideId          rideId of the MatchEntity
     * @param riderRouteId    riderRouteId of the MatchEntity
     * @param message         message to be set
     */
    public void setRiderMessage(int rideId, int riderRouteId, String message);
    
    
    
    
     
    /** List of all MatchEntites -- should be handled with CARE only used for automated testing 
     *  
     * @return List of all Matches
     */
    public List <MatchEntity> getAllMatches();
    
    

}
