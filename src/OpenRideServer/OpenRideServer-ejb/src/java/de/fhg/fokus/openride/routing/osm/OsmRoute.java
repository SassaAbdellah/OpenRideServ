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

package de.fhg.fokus.openride.routing.osm;

import de.fhg.fokus.openride.routing.Coordinate;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

import de.fhg.fokus.openride.routing.Edge;
import de.fhg.fokus.openride.routing.Route;
import de.fhg.fokus.openride.routing.RoutePoint;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OsmRoute implements Route
{
    private LinkedList<Edge> edges;
    private RoutePoint[] routePoints;
   
    /**
     * @param edges sorted from route source to route destination.
     * @param routePoints sorted from route source to route destination.
     * @param computationTime time routing algorithm needed to compute.
     */
    public OsmRoute(LinkedList<Edge> edges, RoutePoint[] routePoints) {
        this.edges = edges;
        this.routePoints = routePoints;
    }

    @Override
    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public RoutePoint[] getRoutePoints() {
        return routePoints;
    }

    @Override
    public double getLength() {
        if(routePoints.length > 0) return routePoints[routePoints.length - 1].getDistance();
        else return 0.0d;
    }

    @Override
    public long getTravelTime() {
        if(routePoints.length > 0) return routePoints[routePoints.length - 1].getTimeAt().getTime()
                -  routePoints[0].getTimeAt().getTime();
        else return 0l;
    }

    @Override
    public void writeAsXml(OutputStream out, boolean includeEstimatedTimes)
    {
        try {
            /* write xml to outputstream */
            out.write("<route>".getBytes());
            out.write("<hasroute>true</hasroute>".getBytes());
            out.write("<coordinates>".getBytes());
            for(RoutePoint rp : routePoints){
                Coordinate c = rp.getCoordinate();
                out.write((c.getLatititude() + "," + c.getLongitude() + " ").getBytes());
            }
            out.write("</coordinates>".getBytes());
            out.write("</route>".getBytes());
        }
        catch (IOException ex) {
            Logger.getLogger(OsmRoute.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void appendAsXml(StringBuffer buffer, boolean includeEstimatedTimes)
    {
            /* write xml to outputstream */
            buffer.append("<route>");
            buffer.append("<hasroute>true</hasroute>");
            buffer.append("<coordinates>");
            for(RoutePoint rp : routePoints){
                Coordinate c = rp.getCoordinate();
                buffer.append((c.getLatititude() + "," + c.getLongitude() + " "));
            }
            buffer.append("</coordinates>");
            buffer.append("</route>");
    }
}
