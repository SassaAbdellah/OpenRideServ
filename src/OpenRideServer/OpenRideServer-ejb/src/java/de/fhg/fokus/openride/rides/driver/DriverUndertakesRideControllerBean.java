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

import de.fhg.fokus.openride.customerprofile.CustomerControllerLocal;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.helperclasses.ControllerBean;
import de.fhg.fokus.openride.matching.MatchEntity;
import de.fhg.fokus.openride.matching.RouteMatchingBean;
import de.fhg.fokus.openride.matching.RouteMatchingBeanLocal;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;
import de.fhg.fokus.openride.routing.RoutePoint;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import org.postgis.Point;

/**
 *
 * @author pab, jochen
 */
@Stateless
public class DriverUndertakesRideControllerBean extends ControllerBean implements DriverUndertakesRideControllerLocal {

    
    UserTransaction u;
    
    @EJB
    private RouteMatchingBeanLocal routeMatchingBean;
    @EJB
    private RiderUndertakesRideControllerLocal riderUndertakesRideControllerBean;
    @EJB
    private CustomerControllerLocal customerControllerBean;
    @PersistenceContext
    private EntityManager em;
    public static final long ACTIVE_DELAY_TIME = 60 * 60 * 1000;

    // FIXME: these methods are not needed. They should be written in DriverUndertakesRideEntity Class
    public Point getStartPoint() {
        startUserTransaction();
        commitUserTransaction();
        return null;
    }

    public Point getEndPoint() {
        startUserTransaction();
        commitUserTransaction();
        return null;
    }

    public String viaPoints() {
        startUserTransaction();
        commitUserTransaction();
        return null;
    }

    public void persist(Object object) {
        startUserTransaction();
        commitUserTransaction();
        em.persist(object);
    }

    @Override
    @Deprecated
    public int addRide(int cust_id, Point ridestartPt, Point rideendPt,
            Date ridestartTime, String rideComment, Integer acceptableDetourInMin,
            Integer acceptableDetourKm, Integer acceptableDetourPercent,
            int offeredSeatsNo, RoutePoint[] routePoints, String startptAddressStreet, String startptAddressZipcode, String startptAddressCity,
            String endptAddressStreet, String endptAddressZipcode, String endptAddressCity) {
        logger.log(Level.INFO, "ridestartPt: " + ridestartPt + " rideendPt: " + rideendPt + " ridestartTime: " + ridestartTime
                + "offeredSeatsNo: " + offeredSeatsNo + " routePoints: " + routePoints + " routePoints.length: " + (routePoints != null ? routePoints.length : -1)
                + "acceptableDetourInMin: " + acceptableDetourInMin + " acceptableDetourKm: " + acceptableDetourKm + " acceptableDetourPercent: " + acceptableDetourPercent);
        //check parameters
        if (ridestartPt == null
                || rideendPt == null
                || ridestartTime == null
                || offeredSeatsNo <= 0
                || routePoints == null
                || routePoints.length < 2
                || (acceptableDetourInMin == null && acceptableDetourKm == null && acceptableDetourPercent == null)) {
            logger.log(Level.INFO, "could not add drive: invalid params ::\n");
            return -1;
        }
        startUserTransaction();
        CustomerEntity customer = customerControllerBean.getCustomer(cust_id);

        //add drive
        DriverUndertakesRideEntity de = new DriverUndertakesRideEntity(
                ridestartTime,
                ridestartPt,
                rideendPt,
                offeredSeatsNo,
                acceptableDetourInMin,
                acceptableDetourKm,
                acceptableDetourPercent,
                0d);
        de.setRideComment(rideComment);
        de.setRideOfferedseatsNo(offeredSeatsNo);
        de.setRideAcceptableDetourInMin(acceptableDetourInMin);
        de.setCustId(customer);
        de.setEndptAddress(endptAddressStreet);
        de.setStartptAddress(startptAddressStreet);
        em.persist(de);

        //add route point entities
        for (int i = 0; i < routePoints.length; i++) {
            DriveRoutepointEntity drp = new DriveRoutepointEntity(
                    de.getRideId(),
                    i,
                    new Point(
                    routePoints[i].getCoordinate().getLongitude(),
                    routePoints[i].getCoordinate().getLatititude()),
                    routePoints[i].getTimeAt(),
                    offeredSeatsNo,
                    routePoints[i].getDistance());
            em.persist(drp);
        }
        commitUserTransaction();

        return de.getRideId();
    }

    @Override
    @Deprecated
    public int addRide(int cust_id, Point ridestartPt, Point rideendPt,
            Date ridestartTime, String rideComment, Integer acceptableDetourInMin,
            Integer acceptableDetourKm, Integer acceptableDetourPercent,
            int offeredSeatsNo, RoutePoint[] routePoints) {
        logger.log(Level.INFO, "ridestartPt: " + ridestartPt + " rideendPt: " + rideendPt + " ridestartTime: " + ridestartTime
                + "offeredSeatsNo: " + offeredSeatsNo + " routePoints: " + routePoints + " routePoints.length: " + (routePoints != null ? routePoints.length : -1)
                + "acceptableDetourInMin: " + acceptableDetourInMin + " acceptableDetourKm: " + acceptableDetourKm + " acceptableDetourPercent: " + acceptableDetourPercent);
        //check parameters
        if (ridestartPt == null
                || rideendPt == null
                || ridestartTime == null
                || offeredSeatsNo <= 0
                || routePoints == null
                || routePoints.length < 2
                || (acceptableDetourInMin == null && acceptableDetourKm == null && acceptableDetourPercent == null)) {
            logger.log(Level.INFO, "could not add drive: invalid params ::\n");
            return -1;
        }
        startUserTransaction();
        CustomerEntity customer = customerControllerBean.getCustomer(cust_id);
        /*if(customer == null || e.size() != 0) {
         logger.log(Level.INFO, "could not add drive: customer does not exists or drive-name not unique");
         commitUserTransaction();
         return -1;
         }
         */




        // Only add a DriverUndertakesRideEntity if a Ride with the same name is not(!) in the DB.

        /*List vec = em.createNativeQuery("SELECT d FROM DriverUndertakesRide d WHERE d.ride_id ="+0+";").getResultList();
         logger.log(Level.INFO, "Vec "+vec.get(0));
         */
//             logger.log(Level.INFO, "addRide 4");
//             String str = "SELECT min(d.ride_id) + 1 FROM driverundertakesride d WHERE (d.ride_id + 1) NOT IN (SELECT ride_id FROM riderundertakesride);";
//             List l = em.createNativeQuery(str).getResultList();
//             if(l.size()==1){
//                 logger.log(Level.INFO, " INDEX "+l.get(0));
//                 index = (Integer)((Vector)l.get(0)).get(0);
//             }

        //em.createNativeQuery("SELECT d FROM driverundertakesride d WHERE d.ride_id ="+index+" ;").getResultList() != null){
        //logger.log(Level.INFO, "addRide 5");
                /*while ((em.find(DriverUndertakesRideEntity.class, index)) != null) {
         index++;
         }
         */

        //add drive
        DriverUndertakesRideEntity de = new DriverUndertakesRideEntity(
                ridestartTime,
                ridestartPt,
                rideendPt,
                offeredSeatsNo,
                acceptableDetourInMin,
                acceptableDetourKm,
                acceptableDetourPercent,
                0d);
        de.setRideComment(rideComment);
        de.setRideOfferedseatsNo(offeredSeatsNo);
        de.setRideAcceptableDetourInMin(acceptableDetourInMin);
        de.setCustId(customer);
        em.persist(de);

        //add route point entities
        for (int i = 0; i < routePoints.length; i++) {
            DriveRoutepointEntity drp = new DriveRoutepointEntity(
                    de.getRideId(),
                    i,
                    new Point(
                    routePoints[i].getCoordinate().getLongitude(),
                    routePoints[i].getCoordinate().getLatititude()),
                    routePoints[i].getTimeAt(),
                    offeredSeatsNo,
                    routePoints[i].getDistance());
            em.persist(drp);
        }
        commitUserTransaction();

        return de.getRideId();
    }

    @Override
    public boolean isDeletable(int rideId) {

        startUserTransaction();

        List<MatchEntity> states = (List<MatchEntity>) em.createNamedQuery("MatchEntity.findByRideId").setParameter("rideId", rideId).getResultList();
        boolean deletable = true;

        if (states.size() > 0) {
            // state already exists
            for (MatchEntity entity : states) {
                if (entity.getDriverState() != null || entity.getRiderState() != null) {
                    deletable = false;
                }
            }
        }

        return deletable;
    }

    public boolean removeRide(int rideId) {
        startUserTransaction();

        List<MatchEntity> states = (List<MatchEntity>) em.createNamedQuery("MatchEntity.findByRideId").setParameter("rideId", rideId).getResultList();

        boolean deletable = this.isDeletable(rideId);


        if (deletable) {
            // entity can be changed


            //TODO (03/09/10): Matches & Ride need to be removed in one transaction....!

            for (Iterator<MatchEntity> it = states.iterator(); it.hasNext();) {
                // delete Matches
                em.remove(it.next());
                //it.remove();
            }

            setDriveRoutePoints(rideId, new LinkedList());

            // remove related RoutePointEntities
            setRoutePoints(rideId, new LinkedList<RoutePointEntity>());

            List<DriverUndertakesRideEntity> entity = em.createNamedQuery("DriverUndertakesRideEntity.findByRideId").setParameter("rideId", rideId).getResultList();
            System.out.println("size entity: " + entity.size());
            for (DriverUndertakesRideEntity ente : entity) {
                // delete ride
                em.remove(ente);
            }
            System.out.println("entity removed: " + rideId);

        } else {
            // all related states have to be adapted
            for (Iterator<MatchEntity> it = states.iterator(); it.hasNext();) {
                // mark Matches as countermanded
                MatchEntity matchEntity = it.next();
                matchEntity.setDriverState(MatchEntity.COUNTERMANDED);
                matchEntity.setDriverChange(new java.util.Date());
                em.merge(matchEntity);
            }
        }
        commitUserTransaction();
        return deletable;
    }

    public void updateDriverPosition() {
        startUserTransaction();
        commitUserTransaction();
    }

    public List<DriveRoutepointEntity> getDriveRoutePoints(int rideId) {
        startUserTransaction();
        Query q = em.createNamedQuery("DriveRoutepointEntity.findByDriveId");
        q.setParameter("rideId", rideId);
        List<DriveRoutepointEntity> routePoints = q.getResultList();
        commitUserTransaction();
        return routePoints;
    }

    public List<RoutePointEntity> getRoutePoints(int rideId) {
        startUserTransaction();
        Query q = em.createNamedQuery("RoutePointEntity.findByRideId");
        q.setParameter("rideId", rideId);
        List<RoutePointEntity> routePoints = q.getResultList();
        commitUserTransaction();
        return routePoints;
    }

    public List<RoutePointEntity> getRequiredRoutePoints(int rideId) {
        startUserTransaction();
        Query q = em.createNamedQuery("RoutePointEntity.findRequiredByRideId");
        q.setParameter("rideId", rideId);
        List<RoutePointEntity> routePoints = q.getResultList();
        commitUserTransaction();
        return routePoints;
    }

    /**
     *
     * @param nickname
     * @return
     */
    public List<DriverUndertakesRideEntity> getInactiveDrives(String nickname) {
        startUserTransaction();
        List<DriverUndertakesRideEntity> returnList = null;
        List<DriverUndertakesRideEntity> past = new ArrayList<DriverUndertakesRideEntity>();
        List<DriverUndertakesRideEntity> future = new ArrayList<DriverUndertakesRideEntity>();

        //TODO: some errorhandling

        // Get the CustomerEntity by <code>nickname</code>
        List<CustomerEntity> c = (List<CustomerEntity>) em.createNamedQuery("CustomerEntity.findByCustNickname").setParameter("custNickname", nickname).getResultList();

        if (c.size() == 1) {
            // Get the Entity. It should be only one, since nicknames are unique by constraint.
            CustomerEntity e = c.get(0);
            // Use the Id of the user to get his rides.
            //returnList = em.createNativeQuery("SELECT d FROM DriverUndertakesRide d WHERE d.cust_id = +"+e.getCustId()+";").getResultList();
            returnList = em.createNamedQuery("DriverUndertakesRideEntity.findByCustId").setParameter("custId", e).getResultList();
            if (returnList == null) {
                returnList = new LinkedList<DriverUndertakesRideEntity>();
            } else {
                java.util.Date now = new java.util.Date();
                now.setTime(now.getTime() - DriverUndertakesRideControllerBean.ACTIVE_DELAY_TIME);
                for (DriverUndertakesRideEntity d : returnList) {
                    if (d.getRideStarttime().before(now)) {
                        past.add(d);
                    }
                }

                Collections.sort(past, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        DriverUndertakesRideEntity ent1 = (DriverUndertakesRideEntity) o1;
                        DriverUndertakesRideEntity ent2 = (DriverUndertakesRideEntity) o2;
                        java.util.Date now = new java.util.Date();
                        // both rides are future rides
                        if (ent1 == null || ent2 == null) {
                            return 0;
                        } else if (ent1.getRideStarttime().before(ent2.getRideStarttime())) {
                            return 1;
                        } else if (ent1.getRideStarttime().after(ent2.getRideStarttime())) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
            }
        }
        future.addAll(past);
        returnList = future;
        commitUserTransaction();
        return returnList;
    }

    /**
     * This method searches for all the rides a user has had.
     *
     * @param nickname The nickname of the user.
     * @return A List containing all <code>RiderUndertakesRideEntity</code>'s
     * refering the user. Otherwise return null, if nothing was found.
     */
    public List<DriverUndertakesRideEntity> getDrives(String nickname) {
        startUserTransaction();
        List<DriverUndertakesRideEntity> returnList = null;
        List<DriverUndertakesRideEntity> past = new ArrayList<DriverUndertakesRideEntity>();
        List<DriverUndertakesRideEntity> future = new ArrayList<DriverUndertakesRideEntity>();

        //TODO: some errorhandling

        // Get the CustomerEntity by <code>nickname</code>
        List<CustomerEntity> c = (List<CustomerEntity>) em.createNamedQuery("CustomerEntity.findByCustNickname").setParameter("custNickname", nickname).getResultList();

        if (c.size() == 1) {
            // Get the Entity. It should be only one, since nicknames are unique by constraint.
            CustomerEntity e = c.get(0);
            // Use the Id of the user to get his rides.
            //returnList = em.createNativeQuery("SELECT d FROM DriverUndertakesRide d WHERE d.cust_id = +"+e.getCustId()+";").getResultList();
            returnList = em.createNamedQuery("DriverUndertakesRideEntity.findByCustId").setParameter("custId", e).getResultList();
            if (returnList == null) {
                returnList = new LinkedList<DriverUndertakesRideEntity>();
            } else {
                java.util.Date now = new java.util.Date();
                now.setTime(now.getTime() - DriverUndertakesRideControllerBean.ACTIVE_DELAY_TIME);
                for (DriverUndertakesRideEntity d : returnList) {
                    if (d.getRideStarttime().after(now)) {
                        future.add(d);
                    }
                }
                // fixme: here should be also these Drives which are obsolete
                Collections.sort(future, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        DriverUndertakesRideEntity ent1 = (DriverUndertakesRideEntity) o1;
                        DriverUndertakesRideEntity ent2 = (DriverUndertakesRideEntity) o2;
                        java.util.Date now = new java.util.Date();
                        // both rides are future rides
                        if (ent1 == null || ent2 == null) {
                            return 0;
                        } else if (ent1.getRideStarttime().before(ent2.getRideStarttime())) {
                            return -1;
                        } else if (ent1.getRideStarttime().after(ent2.getRideStarttime())) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
            }
        }
        returnList = future;
        commitUserTransaction();
        return returnList;

    } // getDrives(nickname)

    /**
     * This method searches for all the rides where the given user acts as
     * driver
     *
     * @param nickname The nickname of the user.
     * @return A List containing all <code>RiderUndertakesRideEntity</code>'s
     * refering the user. Otherwise return null, if nothing was found.
     */
    public List<DriverUndertakesRideEntity> getDrivesForDriver(String nickname) {


        // Get the CustomerEntity by <code>nickname</code>
        List<CustomerEntity> c = (List<CustomerEntity>) em.createNamedQuery("CustomerEntity.findByCustNickname").setParameter("custNickname", nickname).getResultList();

        if (c.size() != 1) {

            throw new Error("Retrieved unexpected Number " + c.size() + "of customers for nickname " + nickname);
        }
        // Get the Entity. It should be only one, since nicknames are unique by constraint.
        CustomerEntity e = c.get(0);

        // Use the Id of the user to get his rides.
        //returnList = em.createNativeQuery("SELECT d FROM DriverUndertakesRide d WHERE d.cust_id = +"+e.getCustId()+";").getResultList();
        List<DriverUndertakesRideEntity> returnList = em.createNamedQuery("DriverUndertakesRideEntity.findByCustId").setParameter("custId", e).getResultList();
        if (returnList == null) {
            returnList = new LinkedList<DriverUndertakesRideEntity>();
        }


        return returnList;

    } // getDrivesforDriver(nickname)

    /**
     * This method can be used to find active drives of a user with
     * <code>nickname</code>. It looks as if "active rides" denotes all those
     * rides, which start within a timespan given by ACTIVE_DELAY_TIME around
     * the current Date/Time.
     *
     * ACTIVE_DELAY_TIME is currently set to
     *
     *
     * @param nickname The nickname of the user.
     * @return active drives of user <code>nickname</code>
     */
    public List<DriverUndertakesRideEntity> getActiveDrives(String nickname) {



        startUserTransaction();

        List<CustomerEntity> c = (List<CustomerEntity>) em.createNamedQuery("CustomerEntity.findByCustNickname").setParameter("custNickname", nickname).getResultList();
        if (c.size() == 1) {
            // customer found
            CustomerEntity customer = c.get(0);

            Query query = em.createNamedQuery("DriverUndertakesRideEntity.findByCustId");
            query.setParameter("custId", customer);
            Date date = new Date(System.currentTimeMillis());
            date.setTime(date.getTime() + DriverUndertakesRideControllerBean.ACTIVE_DELAY_TIME);
            java.util.Date earlier = new java.util.Date();
            earlier.setTime(earlier.getTime() - DriverUndertakesRideControllerBean.ACTIVE_DELAY_TIME);
            List<DriverUndertakesRideEntity> rides = query.getResultList();

            List<DriverUndertakesRideEntity> activeRides = new ArrayList<DriverUndertakesRideEntity>();
            for (Iterator<DriverUndertakesRideEntity> it = rides.iterator(); it.hasNext();) {
                DriverUndertakesRideEntity driverUndertakesRideEntity = it.next();
                if (driverUndertakesRideEntity.getRideStarttime().after(earlier)) {
                    activeRides.add(driverUndertakesRideEntity);
                } else {
                    it.remove();
                }
            }




            Collections.sort(activeRides, new Comparator() {
                public int compare(Object o1, Object o2) {
                    DriverUndertakesRideEntity ent1 = (DriverUndertakesRideEntity) o1;
                    DriverUndertakesRideEntity ent2 = (DriverUndertakesRideEntity) o2;
                    java.util.Date now = new java.util.Date();
                    // both rides are future rides
                    if (ent1 == null || ent2 == null) {
                        return 0;
                    } else if (ent1.getRideStarttime().before(ent2.getRideStarttime())) {
                        return -1;
                    } else if (ent1.getRideStarttime().after(ent2.getRideStarttime())) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }); // end of Comparator

            commitUserTransaction();

            return activeRides;
        } else {
            //customer nickname not found
            return null;
        }
        //return getDrives(nickname);
    }

    /**
     * Returns all drives for a given driver that have startpoints defined after
     * a given date.
     *
     *
     * @param customerId CustomerEntity for which to get drives
     * @param rideStarttime Timestamp that servers as a lower bound for
     * starttime
     *
     * @return active drives of user <code>nickname</code> after <code> rideStarttime
     * </code>
     */
    public List<DriverUndertakesRideEntity> getDrivesAfterTime(CustomerEntity custId, Date rideStarttime) {

        startUserTransaction();

        List<DriverUndertakesRideEntity> res = (List<DriverUndertakesRideEntity>) em.createNamedQuery("DriverUndertakesRideEntity.findCustomerDrivesAfterTime").setParameter("custId", custId).setParameter("time", rideStarttime).getResultList();
        return res;
    }

    public LinkedList<DriverUndertakesRideEntity> getAllDrives() {
        startUserTransaction();

        List<DriverUndertakesRideEntity> l = em.createNamedQuery("DriverUndertakesRideEntity.findAll").getResultList();
        LinkedList<DriverUndertakesRideEntity> ll = new LinkedList<DriverUndertakesRideEntity>(l);

        commitUserTransaction();
        return ll;
    }

    public DriverUndertakesRideEntity getCurrentDrive(String nickname) {
        List<CustomerEntity> c = (List<CustomerEntity>) em.createNamedQuery("CustomerEntity.findByCustNickname").setParameter("custNickname", nickname).getResultList();
        if (c.size() == 1) {
            // customer found
            CustomerEntity customer = c.get(0);
            Query query = em.createNamedQuery("DriverUndertakesRideEntity.findCustomerDrivesBeforeTime");
            query.setParameter("custId", customer);
            query.setParameter("time", new Date(System.currentTimeMillis()));
            List<DriverUndertakesRideEntity> rides = query.getResultList();
            if (rides.size() > 0) {
                return rides.get(0);
            } else {
                return null;
            }
        } else {
            //customer nickname not found
            return null;
        }
    }

    /**
     * This method finds out users (that is passengers)that are already joining
     * a particular drive.
     *
     * @param driveId the drive id, identifying the drive
     * @return list of passengers
     */
    public List<RiderUndertakesRideEntity> getRidersForDrive(int driveId) {
        startUserTransaction();
        logger.getLogger("DriverUndertakesRideControllerBean").info("----------------------DriverUndertakesRideControllerBean getRidersForDrive: " + driveId + ".-------------------------------");
        List<DriverUndertakesRideEntity> driverEntities = (List<DriverUndertakesRideEntity>) em.createNamedQuery("DriverUndertakesRideEntity.findByRideId").setParameter("rideId", driveId).getResultList();
        List<RiderUndertakesRideEntity> list = null;
        for (DriverUndertakesRideEntity entity : driverEntities) {
            list = (List<RiderUndertakesRideEntity>) em.createNamedQuery("RiderUndertakesRideEntity.findByRideId").setParameter("rideId", entity).getResultList();
        }

//        if(c.size()==1){
//            // Use the Id of the user to get his rides.
//            for(RiderUndertakesRideEntity ent: c){
//                if(ent.getRideId().equals(driveId)){
//                    returnList.add(ent);
//                }
//            }
//        }
        commitUserTransaction();
        return list;
    }

    public DriverUndertakesRideEntity getDriveByDriveId(int driveId) {
        startUserTransaction();
        List<DriverUndertakesRideEntity> returnList = null;
        returnList = em.createNamedQuery("DriverUndertakesRideEntity.findByRideId").setParameter("rideId", driveId).getResultList();
        commitUserTransaction();

        DriverUndertakesRideEntity entity;
        if (returnList.size() == 1) {
            return returnList.get(0);
        } else {
            return null;
        }
    }

    /**
     *
     * @param rideId
     * @param cust_id
     * @param ridestartPt
     * @param rideendPt
     * @param intermediatePoints
     * @param ridestartTime
     * @param rideComment
     * @param acceptableDetourInMin
     * @param acceptableDetourKm
     * @param acceptableDetourPercent
     * @param offeredSeatsNo
     * @param startptAddress
     * @param endptAddress
     * @return either new rideId or -1 if update was not possible
     */
    public int updateRide(
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
            String endptAddress) {
        // check whether there already exists a state
        // entity can be changed
        // remove old ride
        if (removeRide(rideId)) {
            // add ride with new informations
            return addRide(cust_id, ridestartPt, rideendPt, intermediatePoints, ridestartTime, rideComment, acceptableDetourInMin, acceptableDetourKm, acceptableDetourPercent, offeredSeatsNo, startptAddress, endptAddress);
        } else {
            return -1;
        }
    }

    @Override
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
            String endptAddress) {
        logger.log(Level.INFO, "ridestartPt: " + ridestartPt + " rideendPt: " + rideendPt + " ridestartTime: " + ridestartTime
                + "offeredSeatsNo: " + offeredSeatsNo
                + "acceptableDetourInMin: " + acceptableDetourInMin + " acceptableDetourKm: " + acceptableDetourKm + " acceptableDetourPercent: " + acceptableDetourPercent
                + "startptAddr: " + startptAddress + " endptAddr: " + endptAddress);
        //check parameters
        if (ridestartPt == null
                || rideendPt == null
                || ridestartTime == null
                || offeredSeatsNo <= 0
                || (acceptableDetourInMin == null && acceptableDetourKm == null && acceptableDetourPercent == null)) {
            logger.log(Level.INFO, "could not add drive: invalid params ::\n");
            return -1;
        }

        //create instance of DriverUndertakesRideEntity
        startUserTransaction();
        CustomerEntity customer = customerControllerBean.getCustomer(cust_id);
        DriverUndertakesRideEntity drive = new DriverUndertakesRideEntity(
                ridestartTime,
                ridestartPt,
                rideendPt,
                offeredSeatsNo,
                acceptableDetourInMin,
                acceptableDetourKm,
                acceptableDetourPercent,
                RouteMatchingBean.getSfrRoutePointDistance(acceptableDetourKm * 1000));
        drive.setRideComment(rideComment);
        drive.setRideOfferedseatsNo(offeredSeatsNo);
        drive.setRideAcceptableDetourInMin(acceptableDetourInMin);
        drive.setCustId(customer);
        drive.setStartptAddress(startptAddress);
        drive.setEndptAddress(endptAddress);

        // compute routes
        LinkedList<DriveRoutepointEntity> decomposedRoute = new LinkedList<DriveRoutepointEntity>();
        LinkedList<RoutePointEntity> route = new LinkedList<RoutePointEntity>();
        double distance = routeMatchingBean.computeInitialRoutes(drive, decomposedRoute, route);

        // if a route has been found, persist drive and routes
        if (distance != Double.MAX_VALUE) {
            em.persist(drive);
            for (DriveRoutepointEntity drp : decomposedRoute) {
                drp.setDriveId(drive.getRideId());
                em.persist(drp);
            }

            for (RoutePointEntity rp : route) {
                rp.setRideId(drive.getRideId());
                em.persist(rp);
            }
            commitUserTransaction();
        } else {
            logger.log(Level.INFO, "could not add drive: no route found ::\n");
            commitUserTransaction();
            return -1;
        }
        callAlgorithm(drive.getRideId(), true);
        return drive.getRideId();
    }

    /**
     * This method can be used to get a certain match from the database.
     *
     * @param rideid
     * @param riderrouteid
     * @return null, if no entity was found; the entity
     */
    private MatchEntity getMatch(int rideid, int riderrouteid) {
        List<MatchEntity> entities = (List<MatchEntity>) em.createNamedQuery("MatchEntity.findByRideIdRiderrouteId").setParameter("rideId", rideid).setParameter("riderrouteId", riderrouteid).getResultList();
        System.out.println("getMatch size: " + entities.size());
        if (entities.size() == 0) {
            return null;
        } else {
            return entities.get(0);
        }
    }

    /**
     *
     * @param rideid
     * @param riderrouteid
     */
    public MatchEntity acceptRider(int rideid, int riderrouteid) {
        MatchEntity match = getMatch(rideid, riderrouteid);
        if (match != null) {
            match.setDriverState(MatchEntity.ACCEPTED);
            match.setDriverChange(new java.util.Date());
            em.merge(match);
        } else {
            // Match does not exist!
            return null;
        }
        return match;
    }

    public MatchEntity rejectRider(int rideid, int riderrouteid) {
        MatchEntity match = getMatch(rideid, riderrouteid);
        if (match != null) {
            if (match.getRiderState() != null && match.getDriverState() != null && match.getRiderState().equals(MatchEntity.ACCEPTED) && match.getDriverState().equals(MatchEntity.ACCEPTED)) {
                // Can't reject when ride has been booked.
                return match;
            }
            match.setDriverState(MatchEntity.REJECTED);
            match.setDriverChange(new java.util.Date());
            em.merge(match);
        } else {
            // Match does not exist!
            return null;
        }
        return match;
    }

    /**
     *
     * @param rideid
     * @param riderrouteid
     */
    public MatchEntity acceptDriver(int rideid, int riderrouteid) {
        MatchEntity match = getMatch(rideid, riderrouteid);
        if (match != null) {
            match.setRiderState(MatchEntity.ACCEPTED);
            match.setRiderChange(new java.util.Date());
            em.merge(match);
        } else {
            // Match does not exist!
            return null;
        }
        return match;
    }

    /**
     *
     * @param rideid
     * @param riderrouteid
     */
    public MatchEntity rejectDriver(int rideid, int riderrouteid) {


        MatchEntity match = getMatch(rideid, riderrouteid);
        if (match != null) {
            if (match.getRiderState() != null && match.getDriverState() != null && match.getRiderState().equals(MatchEntity.ACCEPTED) && match.getDriverState().equals(MatchEntity.ACCEPTED)) {
                // Can't reject when ride has been booked.
                return match;
            }
            match.setRiderState(MatchEntity.REJECTED);
            match.setRiderChange(new java.util.Date());
            em.merge(match);
        } else {
            // Match does not exist!
            return null;
        }
        return match;
    }

    /**
     * This method is called, when a new search or ride is persisted. It updates
     * the Matches table.
     */
    public void callAlgorithm(int rideId, boolean setDriverAccess) {
        // there are still free places
        List<MatchEntity> matches = routeMatchingBean.searchForRiders(rideId);
        matches = filter(matches);
        for (MatchEntity m : matches) {
            // persist match, so it can be found later on!
            em.persist(m);
        }
    }

    /**
     * This method returns a List of riders that have been matched or are
     * already in booking process with the driver.
     *
     * @param rideId
     * @return
     */
    public List<MatchEntity> getMatches(int rideId, boolean setDriverAccess) {

        List<MatchEntity> matches = new ArrayList<MatchEntity>();
        DriverUndertakesRideEntity ride = getDriveByDriveId(rideId);
        List<RiderUndertakesRideEntity> searches = (List<RiderUndertakesRideEntity>) em.createNamedQuery("RiderUndertakesRideEntity.findByRideId").setParameter("rideId", ride).getResultList();

        List<DriverUndertakesRideEntity> drives = getInactiveDrives(ride.getCustId().getCustNickname());
        if (drives.contains(ride)) {
            // The ride is already inactive so just return such matches which are already completed
            List<MatchEntity> entities = (List<MatchEntity>) em.createNamedQuery("MatchEntity.findByRideIdSuccessful").setParameter("rideId", rideId).getResultList();
            matches = entities;

            if (setDriverAccess) {
                // These matches might not be part of getActiveMatches --> setDriverAccess here, separately
                for (MatchEntity m : matches) {
                    m.setDriverAccess(new java.util.Date());
                }
            }

        } else if (searches.size() >= ride.getRideOfferedseatsNo()) {
            // there are no other matches anymore of interest!
            for (RiderUndertakesRideEntity ent : searches) {
                matches.add(getMatch(rideId, ent.getRiderrouteId()));
            }

            if (setDriverAccess) {
                // These matches might not be part of getActiveMatches --> setDriverAccess here, separately
                for (MatchEntity m : matches) {
                    m.setDriverAccess(new java.util.Date());
                }
            }

        } else {
            matches = getActiveMatches(rideId);
        }

        // FIXME: this is only true if seats are added for all subroutes!!!
        // No more match can be added, because all seats are filled

        for (MatchEntity m : getActiveMatches(rideId)) {
            if (!matches.contains(m)) {
                // these Matches ain't available anymore
                m.setDriverState(MatchEntity.NO_MORE_AVAILABLE);
                m.setDriverChange(new java.util.Date());
            }
            if (setDriverAccess) {
                // Mark all possible matches as accessed (otherwise, status update would be displayed indefinitely)
                m.setDriverAccess(new java.util.Date());
            }
            em.merge(m);
        }

        Collections.sort(matches, new Comparator() {
            public int compare(Object o1, Object o2) {
                MatchEntity ent1 = (MatchEntity) o1;
                MatchEntity ent2 = (MatchEntity) o2;
                // both rides are future rides
                if (o1 == null || o2 == null) {
                    return 0;
                } else {
                    return new Integer(ent1.getMatchEntityPK().getRiderrouteId()).compareTo(ent2.getMatchEntityPK().getRiderrouteId());
                }

            }
        });

        // Limit number of unrejected matches to return to `matchCountLimit` (tku 23/08/10)
        int matchCountLimit = ride.getRideOfferedseatsNo() + 2;

        if (matches.size() > matchCountLimit) {
            List<MatchEntity> removedMatches = new ArrayList<MatchEntity>();
            int prevSize = matches.size();
            int unrejectedCount = 0;
            for (MatchEntity m : matches) {
                // Generally don't remove matches with states 0, 2, 3 (rejected, countermanded, no more available),
                // or matches accepted by both parties,
                // or matches accepted by the other party only
                if (!(m.getRiderState() == MatchEntity.ACCEPTED || m.getRiderState() == MatchEntity.REJECTED || m.getRiderState() == MatchEntity.COUNTERMANDED || m.getRiderState() == MatchEntity.NO_MORE_AVAILABLE || m.getDriverState() == MatchEntity.REJECTED || m.getDriverState() == MatchEntity.COUNTERMANDED || m.getDriverState() == MatchEntity.NO_MORE_AVAILABLE) && !(m.getRiderState() == MatchEntity.ACCEPTED && m.getDriverState() == MatchEntity.ACCEPTED)) {
                    if (unrejectedCount < matchCountLimit) {
                        // Keep this match in list
                        unrejectedCount++;
                    } else {
                        // Remove this match from list
                        removedMatches.add(m);
                    }
                }
            }
            matches.removeAll(removedMatches);
            logger.log(Level.INFO, "DURC.getMatches: " + (prevSize - matches.size()) + " of " + prevSize + " matches dropped (limiting unrejected machtes to 3).");
        }

        return matches;
    }

    private List<MatchEntity> getActiveMatches(int rideId) {
        List<MatchEntity> entities = em.createNamedQuery("MatchEntity.findByRideId").setParameter("rideId", rideId).getResultList();
        java.util.Date now = new java.util.Date();
        for (MatchEntity m : entities) {
            //if(m.getDriverUndertakesRideEntity().getRideStarttime().before(now)){
            // this is not active anymore
            //entities.remove(m);
            //}
        }
        return entities;
    }

    /**
     * This method is comparing all the matches found by the algorithm to these
     * which are already persisted in the db and adds each unmatched.
     *
     * @param newMatches
     * @param matches
     * @return
     */
    private List<MatchEntity> filter(List<MatchEntity> matches) {
        boolean remove = false;
        if (matches != null) {
            for (Iterator<MatchEntity> it = matches.iterator(); it.hasNext();) {
                MatchEntity matchEntity = it.next();
                // for every match found by the algorithm
                if (em.createNamedQuery("MatchEntity.findByRideIdRiderrouteId").setParameter("rideId", matchEntity.getMatchEntityPK().getRideId()).setParameter("riderrouteId", matchEntity.getMatchEntityPK().getRiderrouteId()).getResultList().size() > 0) {
                    //TODO: if match has state...
                    // a similar match is already contained in the db
                    it.remove();
                }
            }
        }
        return matches;
    }

    public void setRoutePoints(int rideId, List<RoutePointEntity> routePoints) {
        List<RoutePointEntity> routePointsOld = getRoutePoints(rideId);
        for (RoutePointEntity rpe : routePointsOld) {
            em.remove(rpe);
        }
        em.flush();
        for (RoutePointEntity rpe : routePoints) {
            em.persist(rpe);
        }
        em.flush();
    }

    public void setDriveRoutePoints(int rideId, List<DriveRoutepointEntity> routePoints) {
        List<DriveRoutepointEntity> routePointsOld = getDriveRoutePoints(rideId);
        for (DriveRoutepointEntity rpe : routePointsOld) {
            em.remove(rpe);
        }
        em.flush();
        for (DriveRoutepointEntity rpe : routePoints) {
            em.persist(rpe);
        }
        em.flush();
    }

    public boolean isDriveUpdated(int rideId) {
        if (em.createNamedQuery("MatchEntity.findChangesSinceAccessByDriverByRideId").setParameter("rideId", rideId).getResultList().size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<DriverUndertakesRideEntity> getActiveOpenDrives(String nickname) {
        startUserTransaction();
        List<DriverUndertakesRideEntity> returnList = null;
        List<DriverUndertakesRideEntity> past = new ArrayList<DriverUndertakesRideEntity>();
        List<DriverUndertakesRideEntity> future = new ArrayList<DriverUndertakesRideEntity>();

        //TODO: some errorhandling

        // Get the CustomerEntity by <code>nickname</code>
        List<CustomerEntity> c = (List<CustomerEntity>) em.createNamedQuery("CustomerEntity.findByCustNickname").setParameter("custNickname", nickname).getResultList();

        if (c.size() == 1) {
            // Get the Entity. It should be only one, since nicknames are unique by constraint.
            CustomerEntity e = c.get(0);
            // Use the Id of the user to get his rides.
            //returnList = em.createNativeQuery("SELECT d FROM DriverUndertakesRide d WHERE d.cust_id = +"+e.getCustId()+";").getResultList();
            returnList = em.createNamedQuery("DriverUndertakesRideEntity.findByCustId").setParameter("custId", e).getResultList();
            if (returnList == null) {
                returnList = new LinkedList<DriverUndertakesRideEntity>();
            } else {
                java.util.Date now = new java.util.Date();
                for (DriverUndertakesRideEntity d : returnList) {
                    if (d.getRideStarttime().after(now)) {
                        // FIXME: What could be a reason for a Drive to be no more available?
                        future.add(d);
                    }
                }

                Collections.sort(future, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        DriverUndertakesRideEntity ent1 = (DriverUndertakesRideEntity) o1;
                        DriverUndertakesRideEntity ent2 = (DriverUndertakesRideEntity) o2;
                        java.util.Date now = new java.util.Date();
                        // both rides are future rides
                        if (ent1 == null || ent2 == null) {
                            return 0;
                        } else if (ent1.getRideStarttime().before(ent2.getRideStarttime())) {
                            return -1;
                        } else if (ent1.getRideStarttime().after(ent2.getRideStarttime())) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
            }
        }
        returnList = future;
        commitUserTransaction();
        return returnList;
    }

    @Override
    public boolean invalidateRide(Integer rideId) {


        DriverUndertakesRideEntity dure = this.getDriveByDriveId(rideId);
        // TODO: check if we really want user transactions
        startUserTransaction();
        boolean deletable = this.isDeletable(rideId);

        if (deletable) {
            this.removeRide(rideId);
            commitUserTransaction();
            logger.info("deleting drive with rideId : " + rideId + " the hard way by removing it");
            return true;
        }

        // all related states have to be adapted
        List<MatchEntity> states = (List<MatchEntity>) em.createNamedQuery("MatchEntity.findByRideId").setParameter("rideId", rideId).getResultList();
        for (Iterator<MatchEntity> it = states.iterator(); it.hasNext();) {
            // mark Matches as countermanded
            MatchEntity matchEntity = it.next();
            matchEntity.setDriverState(MatchEntity.COUNTERMANDED);
            matchEntity.setDriverChange(new java.util.Date());
            em.merge(matchEntity);
        }

        // mark ride as invalidated

        this.updateRide(dure.getRideId(), //  rideId
                dure.getCustId().getCustId(), // customerId
                dure.getRideStartpt(), // ridestartPoint
                dure.getRideEndpt(), // rideendPoint
                new Point[0], // intermediatePoints
                new java.sql.Date(dure.getRideStarttime().getTime()), // ridestartTime
                "COUNTERMANDED", // rideComment
                dure.getRideAcceptableDetourInKm(), // acceptableDetourInMin
                dure.getRideAcceptableDetourInMin(), // acceptableDetourKM
                dure.getRideAcceptableDetourInPercent(), // acceptableDetourPercent
                0, // offeredSeatsNo
                dure.getStartptAddress(), // String startPtAddress
                dure.getEndptAddress() // String endPtAddress
                );


        em.merge(this);
        commitUserTransaction();

        return true;
    } // invalidateRide

    @Override
    public List<DriverUndertakesRideEntity> getDrivesInInterval(CustomerEntity custId, Date startDate, Date endDate) {

        List<DriverUndertakesRideEntity> res = (List<DriverUndertakesRideEntity>) em.createNamedQuery("DriverUndertakesRideEntity.findByCustIdBetweenDates").setParameter("custId", custId).setParameter("startdate", startDate).setParameter("enddate", endDate).getResultList();

        return res;
    }
    
} // class
