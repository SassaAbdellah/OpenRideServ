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
package de.fhg.fokus.openride.rating;

import de.fhg.fokus.openride.customerprofile.CustomerControllerLocal;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.helperclasses.ControllerBean;
import de.fhg.fokus.openride.rating.helperclasses.OpenRatingInfo;
import de.fhg.fokus.openride.rating.helperclasses.Rating;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author pab
 */
@Stateless

public class RatingBean extends ControllerBean implements RatingLocal {

    @EJB
    private CustomerControllerLocal customerControllerBean;
    @EJB
    private RiderUndertakesRideControllerLocal riderUndertakesRideControllerBean;
    @PersistenceContext
    private EntityManager em;
    private UserTransaction u;

    /*
     * Driver rates rider.
     */
    
    public void rateRider(int riderRouteId, Integer rating, String ratingComment) {
        startUserTransaction();
        RiderUndertakesRideEntity entity = riderUndertakesRideControllerBean.getRideByRiderRouteId(riderRouteId);
        entity.setReceivedrating(rating);
        if (ratingComment == null) {
            ratingComment = "";
        }
        entity.setReceivedratingComment(ratingComment);
        em.merge(entity);
        commitUserTransaction();
    }

    /*
     * Rider rates driver.
     */
 
    public void rateDriver(int riderRouteId, Integer rating, String ratingComment) {
        startUserTransaction();
        RiderUndertakesRideEntity entity = riderUndertakesRideControllerBean.getRideByRiderRouteId(riderRouteId);
        entity.setGivenrating(rating);
        if (ratingComment == null) {
            ratingComment = "";
        }
        entity.setGivenratingComment(ratingComment);
        em.merge(entity);
        commitUserTransaction();
    }

   
    public Rating getRatingForDriver(int riderRouteId) {
        startUserTransaction();
        RiderUndertakesRideEntity entity = riderUndertakesRideControllerBean.getRideByRiderRouteId(riderRouteId);
        int r = entity.getGivenrating();
        String ratingComment = entity.getGivenratingComment();
        Rating rating = new Rating(r, ratingComment, entity.getGivenratingDate());
        commitUserTransaction();
        return rating;
    }


    /** Get rider rating for a single ride
     * 
     * @param riderRouteId
     * @return 
     */
    public Rating getRatingForRider(int riderRouteId) {
        startUserTransaction();
        RiderUndertakesRideEntity entity = riderUndertakesRideControllerBean.getRideByRiderRouteId(riderRouteId);
        int ratingStarsNo = entity.getReceivedrating();
        String ratingComment = entity.getReceivedratingComment();
        Rating rating = new Rating(ratingStarsNo, ratingComment, entity.getReceivedratingDate());
        commitUserTransaction();
        return rating;
    }

    /**
     * This method scans the DB for rides that the customer has undertaken as
     * Driver.
     *
     * @param cust_id
     * @return List of rides that the customer has undertaken as driver.
     */

    public List<Rating> getRatingsAsDriver(int cust_id) {
        startUserTransaction();
        CustomerEntity e = customerControllerBean.getCustomer(cust_id);
        List<RiderUndertakesRideEntity> rides = riderUndertakesRideControllerBean.getRatedRidesByDriver(e);
        ArrayList<Rating> ratings = new ArrayList<Rating>();
        Rating rating;
        for (RiderUndertakesRideEntity entity : rides) {
            rating = new Rating(entity.getReceivedrating(), entity.getReceivedratingComment(), entity.getReceivedratingDate());
            ratings.add(rating);
        }
        commitUserTransaction();
        return ratings;
    }

    /**
     * This method scans the DB for rides that the customer has undertaken as
     * rider.
     *
     * @param cust_id
     * @return List of rides that the customer has undertaken as rider.
     * 
     * TODO: remove transaction handling, this is readonly
     */
 
    public List<Rating> getRatingsAsRider(int cust_id) {
        startUserTransaction();
        CustomerEntity e = customerControllerBean.getCustomer(cust_id);
        List<RiderUndertakesRideEntity> rides = riderUndertakesRideControllerBean.getRatedRidesByRider(e);
        ArrayList<Rating> ratings = new ArrayList<Rating>();
        Rating rating;
        for (RiderUndertakesRideEntity entity : rides) {
            rating = new Rating(entity.getReceivedrating(), entity.getReceivedratingComment(), entity.getReceivedratingDate());
            ratings.add(rating);
        }
        commitUserTransaction();
        return ratings;
    }

    /**
    * TODO: remove transaction handling, this is readonly
    */
  
    public List<OpenRatingInfo> getOpenRatingsAsDriver(int cust_id) {
        startUserTransaction();
        CustomerEntity ce = customerControllerBean.getCustomer(cust_id);
        // get Unrated rides between beginning of time and now!
        Date startDate=new Date(0);
        Date endDate=new Date(System.currentTimeMillis());
        
        List<RiderUndertakesRideEntity> rides = riderUndertakesRideControllerBean.getUnratedRidesForDriver(ce, startDate, endDate);
        ArrayList<OpenRatingInfo> ratings = new ArrayList<OpenRatingInfo>();
        OpenRatingInfo rating;
        for (RiderUndertakesRideEntity entity : rides) {
            // TODO: (pab) What shall this Timestamp be?
            rating = new OpenRatingInfo(entity.getRiderrouteId(), cust_id, ce.getCustNickname(), new Date());
            ratings.add(rating);
        }
        commitUserTransaction();
        return ratings;
    }

   
    public List<OpenRatingInfo> getOpenRatingsAsRider(int cust_id) {
        startUserTransaction();
        CustomerEntity ce = customerControllerBean.getCustomer(cust_id);
        
        // get Unrated rides between beginning of time and now!
        Date startDate=new Date(0);
        Date endDate=new Date(System.currentTimeMillis());
        List<RiderUndertakesRideEntity> rides = riderUndertakesRideControllerBean.getUnratedRidesForDriver(ce, startDate, endDate);
        ArrayList<OpenRatingInfo> ratings = new ArrayList<OpenRatingInfo>();
        OpenRatingInfo rating;
        for (RiderUndertakesRideEntity entity : rides) {
            // TODO: (pab) What shall this Timestamp be?
            rating = new OpenRatingInfo(entity.getRiderrouteId(), cust_id, ce.getCustNickname(), new Date());
            ratings.add(rating);
        }
        commitUserTransaction();
        return ratings;
    }

    public void persist(Object object) {
        startUserTransaction();
        em.persist(object);
        commitUserTransaction();
    }
}
