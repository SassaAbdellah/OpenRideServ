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

import java.sql.Timestamp;


/**
 * A coordinate on a Route, along with travelDistance from source of the route,
 * and estimated arrival time.
 */
public class RoutePoint 
{
    private Coordinate coordinate;
    private Timestamp timeAt;
    private double distance;

    /**
     *
     * @param coordinate
     * @param timeAt estimated arrival time.
     * @param distance distance from to start of the route.
     */
    public RoutePoint(Coordinate coordinate, Timestamp timeAt, double distance) {
        this.coordinate = coordinate;
        this.timeAt = timeAt;
        this.distance = distance;
    }

    /**
     * The Coordinate of the route point in lat /lon.
     * @return
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate c) {
        this.coordinate = c;
    }

    /**
     * @return estimated arrival time.
     */
    public Timestamp getTimeAt() {
        return timeAt;
    }

    /**
     * Distance in meters from start of the route.
     * @return
     */
    public double getDistance(){
        return distance;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString(){
        return "RoutePoint(coord=" + coordinate + ", EstTimeAt=" + timeAt + ")";
    }
}
