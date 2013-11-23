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

/**
 * An Edge represents a street from source coordinate to target coordinate.
 * The driver will enter at source and leave at target.
 */
public class Edge 
{
    private String name;
    private RoutePoint[] waypoints;

    /**
     * @param name street name, must not be null.
     * @param source start point longitude/latitude.
     * @param target end point longitude/latitude.
     * @param length in meters.
     * @param estimatedTime in seconds.
     */
    public Edge(String name, RoutePoint[] waypoints) {
        this.name = name;
        this.waypoints = waypoints;
    }

    /**
     * @return name of the street.
     */
    public String getName() {
        return name;
    }

    /**
     * @return start of the edge.
     */
    public RoutePoint getSource() {
        return waypoints[0];
    }

    /**
     * @return end of the edge.
     */
    public RoutePoint getTarget() {
        return waypoints[waypoints.length - 1];
    }

    /**
     * @return length in meters.
     */
    public double getLength() {
        return waypoints[waypoints.length - 1].getDistance()
            - waypoints[0].getDistance();
    }

    /**
     * Estimated time to travel along the edge in milliseconds.
     * @return
     */
    public long getTravelTime() {
        return waypoints[waypoints.length - 1].getTimeAt().getTime()
            - waypoints[0].getTimeAt().getTime();
    }

    /**
     * Routepoints along the edge including source and target.
     * @return
     */
    public RoutePoint[] getWayPoints(){
        return waypoints;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return "Edge(name='" + name + "' length=" + getLength() + "m ttime=" + (getTravelTime() / 1000) + "s)";
    }
}
