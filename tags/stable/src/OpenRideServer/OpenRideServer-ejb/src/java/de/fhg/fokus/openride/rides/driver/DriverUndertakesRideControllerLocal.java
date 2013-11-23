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
import de.fhg.fokus.openride.routing.RoutePoint;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Local;
import org.postgis.Point;

/**
 *
 * @author pab
 */
@Local
public interface DriverUndertakesRideControllerLocal {

    public Point getStartPoint();

    public Point getEndPoint();

    public String viaPoints();

    public int addRide(
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

    /**
     *
     * @param cust_id should exists.
     * @param rideName not null.
     * @param ridestartPt not null.
     * @param rideendPt not null.
     * @param ridestartTime not null.
     * @param rideComment
     * @param acceptableDetourInMin At least one of the three detour thresholds must be specified and valid.
     * @param acceptableDetourKm At least one of the three detour thresholds must be specified and valid.
     * @param acceptableDetourPercent At least one of the three detour thresholds must be specified and valid.
     * @param offeredSeatsNo must be > 0.
     * @param routePoints ordered from startpt to endpt. must not be null.
     * @return if successfull : assigned drive id, else -1.
     */
    @Deprecated
    public int addRide(int cust_id, Point ridestartPt, Point rideendPt,
        Date ridestartTime, String rideComment, Integer acceptableDetourInMin,
        Integer acceptableDetourKm, Integer acceptableDetourPercent,
        int offeredSeatsNo, RoutePoint[] routePoints);

    @Deprecated
    public int addRide(int cust_id,
            Point ridestartPt,
            Point rideendPt,
            Date ridestartTime,
            String rideComment,
            Integer acceptableDetourInMin,
            Integer acceptableDetourKm,
            Integer acceptableDetourPercent,
            int offeredSeatsNo,
            RoutePoint[] routePoints,
            String startptAddressStreet,
            String startptAddressZipcode,
            String startptAddressCity,
            String endptAddressStreet,
            String endptAddressZipcode,
            String endptAddressCity);

    public boolean removeRide(int rideId);

    public void updateDriverPosition();

    public List<DriverUndertakesRideEntity> getDrives(String nickname);

    public LinkedList<DriverUndertakesRideEntity> getAllDrives();

    public List<DriverUndertakesRideEntity> getActiveDrives(String nickname);
    
    public List<DriverUndertakesRideEntity> getDrivesForDriver(String nickname);
    

    public DriverUndertakesRideEntity getCurrentDrive(String nickname);

    public List<RiderUndertakesRideEntity> getRidersForDrive(int driveId);

    public DriverUndertakesRideEntity getDriveByDriveId(int driveId);

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

    void callAlgorithm(int rideId, boolean setDriverAccess);

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
     * 
     * @param rideId
     * @return  true, if countermanding was successful, else false.
     */
    public boolean invalidateRide(Integer rideId);

  

}
