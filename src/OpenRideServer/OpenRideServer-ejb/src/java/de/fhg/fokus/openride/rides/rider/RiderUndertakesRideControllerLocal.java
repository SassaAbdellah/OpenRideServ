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
package de.fhg.fokus.openride.rides.rider;

import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.matching.MatchEntity;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Local;
import org.postgis.Point;

/**
 *
 * @author pab
 */
@Local
public interface RiderUndertakesRideControllerLocal {

    Point getStartPoint(int rideId);

    Point getEndPoint(int rideId);

    Timestamp getStartTime(int rideId);

    Object getTolerance(int rideId);

    int addRiderToRide(int riderRouteId, int rideId);

    void setReceivedRating(int riderRouteId, int rating, String ratingComment);

    void setGivenRating(int riderRouteId, int rating, String ratingComment);

    void addPaymentReference(int rideId);

    @Deprecated
    int addRideRequest(int cust_id, Date starttime_earliest, Date starttimeLatest, int noPassengers, Point startpt, Point endpt, double price, String comment);

    List<RiderUndertakesRideEntity> getRides(String nickname);

    List<RiderUndertakesRideEntity> getActiveRideRequests(String nickname);

    RiderUndertakesRideEntity getRideByRiderRouteId(int riderRouteId);

    LinkedList<RiderUndertakesRideEntity> getAllRides();

    /**
     * get all rides/requests for customer ce, newest first
     */
    List<RiderUndertakesRideEntity> getRidesForCustomer(CustomerEntity ce);

    List<RiderUndertakesRideEntity> getActiveRideRequestsByCustId(String custId);

    List<RiderUndertakesRideEntity> getRidesWithoutRatingByRider(CustomerEntity rider);

    List<RiderUndertakesRideEntity> getRidesWithoutGivenRatingByRider(CustomerEntity rider);

    List<RiderUndertakesRideEntity> getRidesWithoutReceivedRatingByRider(CustomerEntity rider);

    List<RiderUndertakesRideEntity> getRidesWithoutRatingByDriver(CustomerEntity driver);

    List<RiderUndertakesRideEntity> getRidesWithoutGivenRatingByDriver(CustomerEntity driver);

    List<RiderUndertakesRideEntity> getRidesWithoutReceivedRatingByDriver(CustomerEntity driver);

    List<RiderUndertakesRideEntity> getRatedRidesByRider(CustomerEntity rider);

    List<RiderUndertakesRideEntity> getRatedRidesByDriver(CustomerEntity driver);

    /** Count Ratings for Customer
     * 
     * @param customer customer Entity to count number of ratings
     * @return Overall Number of all Ratings for this user
     */
    Integer getRatingsCountByCustomer(CustomerEntity customer);
    
    int getRatingsTotalByCustomer(CustomerEntity customer);

    float getRatingsRatioByCustomerAndDate(CustomerEntity customer, Date fromDate);

    float getRatingsRatioByCustomer(CustomerEntity customer);

    int getPositiveRatingsTotalByCustomerAndDate(CustomerEntity customer, Date fromDate);

    int getPositiveRatingsTotalByCustomer(CustomerEntity customer);

    int getNeutralRatingsTotalByCustomerAndDate(CustomerEntity customer, Date fromDate);

    int getNeutralRatingsTotalByCustomer(CustomerEntity customer);

    int getNegativeRatingsTotalByCustomerAndDate(CustomerEntity customer, Date fromDate);

    int getNegativeRatingsTotalByCustomer(CustomerEntity customer);

    boolean removeRide(int rideId);

    int updateRide(int rideId,
            Date starttime_earliest,
            Date starttimeLatest,
            int noPassengers,
            Point startpt,
            Point endpt,
            double price,
            String comment,
            String startptAddress,
            String endptAddress);

    public int addRideRequest(int cust_id, Date starttime_earliest, Date starttimeLatest, int noPassengers, Point startpt, Point endpt, double price, String comment, String startptAddress, String endptAddress);

    /**
     * True, if RiderUndertakesRide Entity with given riderrouteId is deletable,
     * else false.
     *
     * @param riderrouteId
     * @return
     */
    public boolean isDeletable(int riderrouteId);

    void removeRiderFromRide(int riderrouteid, int rideid);

    public List<MatchEntity> getMatches(int riderrouteId, boolean setRiderAccess);

    boolean isRideUpdated(int riderrouteId);

    List<RiderUndertakesRideEntity> getInactiveRideRequests(String nickname);

    List<RiderUndertakesRideEntity> getActiveOpenRides(String nickname);

    void setMatchCountermand(Integer rideId, Integer riderrouteId);

    MatchEntity getMatch(Integer rideId, Integer riderrouteId);

    /**
     * Invalidate this ride and associated objects, typically if a customer is
     * removed
     *
     * @param riderrouteId
     *
     * @return true if ride can be successfully invalidated
     */
    public boolean invalidateRide(Integer riderrouteId);

    public List<RiderUndertakesRideEntity> getRidesForCustomer(CustomerEntity ce, Date startDate, Date endDate);

    public List<RiderUndertakesRideEntity> getRealizedRidesForRider(CustomerEntity ce, Date startDate, Date endDate);

    public List<RiderUndertakesRideEntity> getUnratedRidesForRider(CustomerEntity ce, Date startDate, Date endDate);

    public List<RiderUndertakesRideEntity> getRidesForDriver(CustomerEntity ce, Date startDate, Date endDate);
    
    
   /** Get the sum over all Ratings that the customer received when acting as rider.
    * 
    * @param custId  id of customer for whom ratings will be summed up
    * @return  sum over all driver ratings the customer has received
    */
    public Integer getTotalOfRatingsForDriver(CustomerEntity customer);
    
    /** Count all rides where the customer has acted as a driver
     * 
     * @param customer customer for whom ratings will be counted
     * @return  number of rated rides where the customer acted as driver
     */
    public Integer getCountOfRatingsForDriver(CustomerEntity customer);
    
      /** Get the sum over all Ratings that the customer received when acting as rider.
    * 
    * @param customer customer for whom ratings will be summed up
    * @return  sum over all rider ratings the customer has received
    */
    public Integer getTotalOfRatingsForRider(CustomerEntity customer);
    
    /** Count all rides where the customer has acted as a rider
     * 
     * @param customer  customer for whom ratings will be counted
     * @return  number of rated rides where the customer acted as rider
     */
    public Integer getCountOfRatingsForRider(CustomerEntity customer);
    
    
    
    
    
    
}
