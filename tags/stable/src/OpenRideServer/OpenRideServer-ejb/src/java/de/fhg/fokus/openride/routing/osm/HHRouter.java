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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhg.fokus.openride.routing.osm;

import de.fhg.fokus.openride.routing.Coordinate;
import de.fhg.fokus.openride.routing.Route;
import de.fhg.fokus.openride.routing.RoutePoint;
import de.fhg.fokus.openride.routing.Router;
import de.fhg.fokus.openride.routing.RouterWrapper;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;
import org.mapsforge.core.GeoCoordinate;

/**
 *
 * @author fvi
 */
public class HHRouter implements Router {

    public Route findRoute(Coordinate source, Coordinate target, Timestamp startTime, boolean fastestPath, double threshold, boolean includeWaypoints) {
        GeoCoordinate s = new GeoCoordinate(source.getLatititude(), source.getLongitude());
        GeoCoordinate t = new GeoCoordinate(target.getLatititude(), target.getLongitude());

        LinkedList<GeoCoordinate> coords = new LinkedList<GeoCoordinate>();
        LinkedList<Integer> times = new LinkedList<Integer>();
        double distance = RouterWrapper.getInstance().getShortestPathAndTravelTime(s, t, coords, times);

        if(distance == Double.MAX_VALUE) {
            return null;
        }

        RoutePoint[] rp = toRoutePoint(coords, times, startTime);
        return new OsmRoute(null, rp);
    }

    public RoutePoint[] getEquiDistantRoutePoints(Coordinate[] coordinates, Timestamp startTime, boolean fastestPath, double threshold, double maxDistanceOfPoints) {
        GeoCoordinate[] waypoints = new GeoCoordinate[coordinates.length];
        for(int i=0;i<waypoints.length;i++) {
            waypoints[i] = new GeoCoordinate(coordinates[i].getLatititude(), coordinates[i].getLongitude());
        }

        LinkedList<GeoCoordinate> coords = new LinkedList<GeoCoordinate>();
        LinkedList<Integer> times = new LinkedList<Integer>();
        double distance = RouterWrapper.getInstance().getShortestPathAndTravelTime(waypoints, coords, times);

        if(distance == Double.MAX_VALUE) {
            return null;
        }

        LinkedList<GeoCoordinate> interpolatedCoords = new LinkedList<GeoCoordinate>();
        LinkedList<Integer> interpolatedTimes = new LinkedList<Integer>();

        interpolateRoute(coords, times, interpolatedCoords, interpolatedTimes, maxDistanceOfPoints);

        RoutePoint[] rp = toRoutePoint(interpolatedCoords, interpolatedTimes, startTime);
        return rp;
    }

    private static RoutePoint[] toRoutePoint(LinkedList<GeoCoordinate> coords, LinkedList<Integer> times, Timestamp startTime) {
        RoutePoint[] rp = new RoutePoint[coords.size()];
        Iterator<GeoCoordinate> iterC = coords.iterator();
        Iterator<Integer> iterT = times.iterator();
        {
            int i = 0;
            double d = 0;
            GeoCoordinate last = null;
            GeoCoordinate coord = null;
            while(iterC.hasNext()) {
                last = coord;
                coord = iterC.next();
                int time = iterT.next();
                rp[i++] = new RoutePoint(new Coordinate(coord.getLatitude(), coord.getLongitude()), new Timestamp(startTime.getTime() + time), d);
                if(last != null) {
                    d += last.sphericalDistance(coord);
                }
            }
        }
        return rp;
    }


    /**
     * Computes an interpolated route based on the given route
     * and adds the result to the given buffers. The distance between each
     * suceeding points (along the route edges) is determined by distanceMeters.
     * The lists waypoints and times must have same size. All parameters must not be null.
     * @param waypoints coordinates along the route, from start to end.
     * @param times travel times in milli seconds to the i-th coordinate.
     * @param coordsBuff buffer to add the interpolated coordinates, should be empty.
     * @param timesBuff buffer to add the interpolated times, should be empty.
     * @param distanceMeters desired distance along the route edges between two succeeding interpolated points.
     */
    private static void interpolateRoute(LinkedList<GeoCoordinate> coords, LinkedList<Integer> times,
            LinkedList<GeoCoordinate> coordsBuff, LinkedList<Integer> timesBuff, double distanceMeters) {
        if(coords == null || times == null || coordsBuff == null || timesBuff == null || distanceMeters < 0
                || coords.size() != times.size()) {
            throw new IllegalArgumentException("");
        }

        if(coords.size() == 0) {
            return;
        }
        if (coords.size() == 1) {
            coordsBuff.add(coords.getFirst());
            timesBuff.add(times.getFirst());
            return;
        }

        int n = coords.size();
        GeoCoordinate[] coordsArr = new GeoCoordinate[n];
        Integer[] timesArr = new Integer[n];
        double[] d = new double[n];
        coords.toArray(coordsArr);
        times.toArray(timesArr);
        // compute distances to the i-th coordinate
        d[0] = 0;
        for(int i=1;i<d.length;i++) {
            d[i] = d[i-1] + coordsArr[i -1].sphericalDistance(coordsArr[i]);
        }

        // interpolate route
        double dNext = 0; // distance along the route where next interpolated coordinate is added
        int i = 0;
        while(i < coordsArr.length - 1) {
            while(d[i] <= dNext && d[i + 1] > dNext) {
                // factor for linear interpolation
                double a = dNext - d[i];
                double b = d[i + 1] - d[i];
                double fac = a / b;

                // interpolate coordinate and time
                double lat = ((1 - fac) * coordsArr[i].getLatitude()) + (fac * coordsArr[i+1].getLatitude());
                double lon = ((1 - fac) * coordsArr[i].getLongitude()) + (fac * coordsArr[i+1].getLongitude());
                int time = (int)Math.rint(((1 - fac) * timesArr[i]) + (fac * timesArr[i+1]));

                // add to result
                GeoCoordinate c = new GeoCoordinate(lat, lon);
                coordsBuff.add(c);
                timesBuff.add(time);

                // update insertion criteria
                dNext += distanceMeters;
            }
            // next iteration
            i++;
        }
        if(!coordsBuff.getLast().equals(coordsArr[coordsArr.length - 1])){
            coordsBuff.add(coordsArr[coordsArr.length - 1]);
            timesBuff.add(timesArr[timesArr.length - 1]);
        }
    }

    

}
