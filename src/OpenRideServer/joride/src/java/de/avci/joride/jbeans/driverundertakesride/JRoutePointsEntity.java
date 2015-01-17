/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.driverundertakesride;

import java.io.Serializable;

import de.fhg.fokus.openride.rides.driver.RoutePointEntity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;

/**
 * Wrapper for list of Routpoints, making them available as a JSF Bean, and
 * offering some convenience methods to view them in OpenLayers and friends.
 *
 * @author jochen
 */
public class JRoutePointsEntity implements Serializable {

    /**
     * the point classified as the startPoint of the journey.
     */
    private RoutePointEntity startPoint;
    /**
     * the point classified as the endPoint of the journey.
     */
    private RoutePointEntity endPoint;
    /**
     * List of points where riders get picked up
     */
    private List<RoutePointEntity> pickupRiderPoints;
    /**
     * rider defined points which should be called during the trip (aka:
     * waypoints, aka intermediate points)
     */
    private List<RoutePointEntity> wayPoints;
    /**
     * List of points where riders get dropped
     */
    private List<RoutePointEntity> dropRiderPoints;
    /**
     * List of Routepoints for given Ride or Drive
     *
     */
    private List<RoutePointEntity> routePoints;

    public List<RoutePointEntity> getRoutePoints() {
        return this.routePoints;
    }

    /**
     * Nontrivial (!) Setter -- triggers implicite call to {
     *
     * @see initializePoints}
     *
     * @param arg
     */
    public void setRoutePoints(List<RoutePointEntity> arg) {
        this.routePoints = arg;
        initializePoints();
    }

    /**
     * Get a representation of a routepoint as JSON Array I.e:
     * "["+longitude+","+latitude+","+rideId+","+riderRouteId+","+isRequired+"]"
     *
     *
     * @param rpe routepoint to be encoded
     * @return
     * "["+longitude+","+latitude+","+rideId+","+riderRouteId+","+isRequired+"]"
     */
    private StringBuffer getRoutePointAsJSON(RoutePointEntity rp) {

        StringBuffer buf = new StringBuffer();

        buf.append("[");
        buf.append(rp.getLongitude());
        buf.append(",");
        buf.append(rp.getLatitude());
        buf.append(",");
        buf.append(rp.getRideId());
        buf.append(",");
        buf.append(rp.getRiderrouteId());
        buf.append(",");
        buf.append(rp.isRequired());
        buf.append("]");

        return buf;
    }

    /**
     * Return reprensentation of the routepoints as a JSON String, i.e:
     *
     * @return
     */
    private StringBuilder getListOfRoutePointsAsJSON(List<RoutePointEntity> rpl) {

        Iterator<RoutePointEntity> it = rpl.iterator();

        StringBuilder bui = new StringBuilder();
        bui.append("\n[");

        while (it.hasNext()) {
            RoutePointEntity rp = it.next();
            bui.append("\n");
            bui.append(getRoutePointAsJSON(rp));
            if (it.hasNext()) {
                bui.append(",");
            }
        } // while it.hasNext()

        bui.append("\n]");

        return bui;
    }

    /**
     *
     * @return list of all routepoints encoded in a json string
     */
    public String getRoutePointsAsJSON() {
        return this.getListOfRoutePointsAsJSON(this.getRoutePoints()).toString();
    }

    /**
     *
     * @return {@link getDropRiderPoints} converted to json
     */
    public String getDropRiderPointsAsJSON() {
        return this.getListOfRoutePointsAsJSON(this.getDropRiderPoints()).toString();
    }

    /**
     *
     * @return {@link getPickupRiderPoints} converted to json
     */
    public String getPickupRiderPointsAsJSON() {
        return this.getListOfRoutePointsAsJSON(this.getPickupRiderPoints()).toString();
    }

    /**
     *
     * @return {@link getWayPoints} converted to json
     */
    public String getWayPointsAsJSON() {
        return this.getListOfRoutePointsAsJSON(this.getWayPoints()).toString();
    }

    public String getStartPointAsJSON() {
        return this.getRoutePointAsJSON(this.getStartPoint()).toString();
    }

    public String getEndPointAsJSON() {
        return this.getRoutePointAsJSON(this.getEndPoint()).toString();
    }

    public RoutePointEntity getStartPoint() {
        return startPoint;
    }

    public RoutePointEntity getEndPoint() {
        return endPoint;
    }

    public List<RoutePointEntity> getDropRiderPoints() {
        return this.dropRiderPoints;
    }

    public List<RoutePointEntity> getPickupRiderPoints() {
        return this.pickupRiderPoints;
    }

    public List<RoutePointEntity> getWayPoints() {
        return this.wayPoints;
    }

    /**
     * Initialize/Classify the list of route points into
     *
     * startPoint endPoint pickupPoints dropPoints waypoints
     *
     */
    private void initializePoints() {

        List<RoutePointEntity> rpes = this.getRoutePoints();

        if (rpes == null) {
            // TODO: log  error properly 
            System.err.println("cannot initialize routePoints, getRoutePoints returns null");
            return;
        }
        if (rpes.size()<=0) {
            // TODO: log  error  properly
            System.err.println("cannot initialize routePoints, getRoutePoints has size null");
            return;
        }



        this.startPoint = rpes.get(0);
        this.endPoint = rpes.get(rpes.size() - 1);

        this.pickupRiderPoints = new ArrayList<RoutePointEntity>();
        this.dropRiderPoints = new ArrayList<RoutePointEntity>();
        this.wayPoints = new ArrayList<RoutePointEntity>();

        HashSet<Integer> rides = new HashSet<Integer>();

        for (int i = 0; i < rpes.size(); i++) {

            RoutePointEntity rpe = rpes.get(i);

            boolean isRequired = rpe.isRequired();
            Integer riderRouteId = rpe.getRiderrouteId();

            // pickupPoints/dropPoints have a reference to a rideRequest
            if (isRequired && riderRouteId != null) {
                if (!(rides.contains(riderRouteId))) {
                    this.pickupRiderPoints.add(rpe);
                } else {
                    this.dropRiderPoints.add(rpe);
                }
                rides.add(riderRouteId);
            }
            // waypoints have no reference to RideRequest and are different
            // from startpoint and endpoint
            if (isRequired && riderRouteId == null && i != 0 && (i != (rpes.size() - 1))) {
                wayPoints.add(rpe);
            }
        } // for (RoutePointEntity rpe : rpes)
    } // initializePoints
}
