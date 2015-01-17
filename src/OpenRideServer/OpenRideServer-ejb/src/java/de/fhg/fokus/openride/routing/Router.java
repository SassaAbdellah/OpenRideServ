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

package de.fhg.fokus.openride.routing;

import java.sql.SQLException;
import java.sql.Timestamp;

public interface Router {

    /**
     * Computes a route form source to target.
     * @param source longitude/latitude.
     * @param target longitude/latitude.
     * @return the representation of the route, null if no route is found.
     * @throws SQLException may occur if connection to database is lost.
     *  
     *
     * 
     */
    public Route findRoute(
            Coordinate source, 
            Coordinate target,
            Timestamp startTime, 
            boolean fastestPath, 
            double threshold
            );

    /**
     * Computes a route form source to target along given waypoints.
     * @param coordinates Array[]{startpoint,..., waypoint k, ...,endpoint].
     * Array length must be at least two.
     * @param startTime time when driver will start driving the route.
     * @param fastestPath switch between fastest or shortesst path.
     * @param threshold max distance in meters from source to source-vertex and from
     * target to target-vertex. if no source - or target-vertex is found within this
     * threshold, the result is null because it is assumed that no roads exists at these
     * places.
     * @param maxDistanceOfPoints maximum distance betwenn interpolated route-points in meters.
     * @return
     */
    public RoutePoint[] getEquiDistantRoutePoints(Coordinate[] coordinates,
            Timestamp startTime, boolean fastestPath, double threshold,
            double maxDistanceOfPoints);
}

