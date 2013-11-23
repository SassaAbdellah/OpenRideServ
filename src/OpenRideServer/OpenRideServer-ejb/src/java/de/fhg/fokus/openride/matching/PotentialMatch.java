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

package de.fhg.fokus.openride.matching;

import de.fhg.fokus.openride.routing.Coordinate;
import java.sql.Timestamp;
import java.text.DecimalFormat;

import de.fhg.fokus.openride.routing.Route;
import de.fhg.fokus.openride.routing.Router;


/**
 * This class holds information about matches computed by the circle overlay algorithm.
 */
final class PotentialMatch
{
    public PotentialMatch() {
    }
    /* matched rider route identified by: */
    private int ridersRouteId;

    /* matched driver's route identified by: */
    private int rideId;

    /* route index for lift point */
    private int liftIndex;
    /* route index for drop point */
    private int dropIndex;

    /* point to pick up the rider */
    private Coordinate liftPoint;
    /* point drop out the rider */
    private Coordinate dropPoint;
    /* route-point nearest to lift-point */
    private Coordinate onRouteLiftPoint;
    /* route-point nearest to drop-point */
    private Coordinate onRouteDropPoint;
    /* time when driver will pass the on route lift point */
    private Timestamp timeAtOnRouteLiftPoint;
    /* riders Time at the lift Point */
    private Timestamp timeAtLiftPoint;
    /* distance between on route-lift-point and on-route-drop-point (in meters) */
    private double sharedDistanceMeters;

    /* distance(liftpoint, onrouteliftpoint) + distance(droppoint, onrouteDroppoint) */
    private Double detourMeters;
    /* estimated_time(liftpoint, onrouteliftpoint) + estimated_time(droppoint, onrouteDroppoint) */
    private Double detourSeconds;


    /**
     * @param ridersRouteId identifier of riders' offer.
     * @param rideId identifier of drivers' offer.
     * @param liftIndex index to drivers' route point which is nearest to riders' pick up position.
     * @param dropIndex index to drivers' route point nearest to riders drop position.
     * @param liftPoint riders' pick up coordinate.
     * @param dropPoint riders' drop coordinate.
     * @param onRouteLiftPoint coordinate of the drivers route point nearest to riders' pick up position.
     * @param onRouteDropPoint coordinate of the drivers route point nearest to riders' drop position.
     * @param timeAtOnRouteLiftPoint time the driver will probably arrive at the route point nearest to riders pick up position.
     * @param timeAtLiftPoint time the rider will be at his pick up position
     * @param sharedDistanceMeters distance between the onRouteLiftPoint and the onRouteDrop point along the route.
     * This is not the real shared distance (which can only be compute using a routing algorithm).
     */
    public PotentialMatch(int ridersRouteId, int rideId,
        int liftIndex, int dropIndex, Coordinate liftPoint, Coordinate dropPoint,
        Coordinate onRouteLiftPoint, Coordinate onRouteDropPoint,
        Timestamp timeAtOnRouteLiftPoint, Timestamp timeAtLiftPoint,
        double sharedDistanceMeters)
    {
        this.ridersRouteId = ridersRouteId;
        this.rideId = rideId;
        this.liftIndex = liftIndex;
        this.dropIndex = dropIndex;
        this.liftPoint = liftPoint;
        this.dropPoint = dropPoint;
        this.onRouteLiftPoint = onRouteLiftPoint;
        this.onRouteDropPoint = onRouteDropPoint;
        this.timeAtOnRouteLiftPoint = timeAtOnRouteLiftPoint;
        this.timeAtLiftPoint = timeAtLiftPoint;
        this.sharedDistanceMeters = sharedDistanceMeters;

        /* compute detour only when requested by related getter Method */
        /* afterwards store here */
        this.detourMeters = null;
        this.detourSeconds = null;
    }

    /**
     * @return (riderCustId, ridersRouteId) identifies riders route.
     */
    public int getRidersRouteId() {
        return ridersRouteId;
    }

    /**
     * @return identifies drivers route.
     */
    public int getRideId() {
        return rideId;
    }

    /**
     * @return index of route point nearest to liftPoint
     */
    public int getLiftIndex() {
        return liftIndex;
    }

    /**
     * @return index of route point nearest to dropPoint.
     */
    public int getDropIndex() {
        return dropIndex;
    }

    /**
     * @return Lon/Lat Coordinate to pick up passenger.
     */
    public Coordinate getLiftPoint() {
        return liftPoint;
    }

    /**
     * @return Lon/Lat Coordinate  to drop passenger.
     */
    public Coordinate getDropPoint() {
        return dropPoint;
    }

    /**
     * @return Lon/Lat Coordinate of routePoint nearest to liftPoint.
     */
    public Coordinate getOnRouteLiftPoint() {
        return onRouteLiftPoint;
    }

    /**
     * @return Lon/Lat Coordinate of routePoint nearest to dropPoint.
     */
    public Coordinate getOnRouteDropPoint() {
        return onRouteDropPoint;
    }

    /**
     * @return Drivers estimated time at onRouteLiftPoint.
     */
    public Timestamp getTimeAtOnRouteLiftPoint() {
        return timeAtOnRouteLiftPoint;
    }

    /**
     * @return Riders time at liftPoint.
     */
    public Timestamp getTimeAtLiftPoint() {
        return timeAtLiftPoint;
    }

    /**
     * @return meters from onRouteLiftPoint to onRouteDropPoint.
     */
    public double getSharedDistanceMeters() {
        return sharedDistanceMeters;
    }

    /**
     * Distance between liftPoint, OnRouteLift Point and dropPoint, onRouteDropPoint.
     * @param router
     * @return Double.MaxValue if no route exists to liftpoint or droppoint.
     */
    public double getDetourMeters(Router router){
        if(this.detourMeters == null){
            computeDetour(router);
        }
        return this.detourMeters;
    }

    /**
     * Estimated Time between liftPoint, OnRouteLift Point and dropPoint, onRouteDropPoint.
     * @param router
     * @return Double.MaxValue if no route exists to liftpoint or droppoint.
     */
    public double getDetourSeconds(Router router){
        if(this.detourMeters == null){
            computeDetour(router);
        }
        return this.detourSeconds;
    }



    /**
     * calculate route between liftPoint and onRouteLiftPoint and same for dropPoints.
     * store result in private class variables.
     * @param router
     */
    private void computeDetour(Router router){
        Route routeA = router.findRoute(onRouteLiftPoint, liftPoint, timeAtLiftPoint, true, 2000.0d, false);
        Route routeB = router.findRoute(onRouteDropPoint, dropPoint, timeAtLiftPoint, true, 2000.0d, false);

        if(routeA == null || routeB == null){
            this.detourMeters = Double.MAX_VALUE;
            this.detourSeconds = Double.MAX_VALUE;
        }
        else{
            this.detourMeters = routeA.getLength() + routeB.getLength();
            /* translate from milli-seconds to seconds */
            this.detourSeconds = (((double)routeA.getTravelTime()) + ((double)routeB.getTravelTime()))
                    / 1000.0d;
        }
    }

    @Override
    public String toString(){
        DecimalFormat df = new DecimalFormat("#.#");
        return "lift/drop " + liftIndex + "/" + dropIndex + " "
            + "shared " + df.format(sharedDistanceMeters) + "m "
            + "rideId: " + rideId + " "
            + "riderRoute: " + ridersRouteId + " "
            + "timeAtOnRouteLiftPoint: " + timeAtOnRouteLiftPoint.toString() + " "
            + "timeAtLiftPoint: "  + timeAtLiftPoint + " "
            + "liftPoint: " + liftPoint.toString()  + " "
            + "dropPoint: " + dropPoint.toString() + " "
            + "onRouteLiftPoint: " + onRouteLiftPoint.toString() + " "
            + "onRouteDropPoint: " + onRouteDropPoint.toString() + " ";
    }
}
