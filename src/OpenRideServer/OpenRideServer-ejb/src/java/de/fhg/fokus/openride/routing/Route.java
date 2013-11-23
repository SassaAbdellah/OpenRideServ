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

import java.io.OutputStream;
import java.util.List;

/**
 * Provides two different representations of the route,
 * by RoutePoint and by edges.
 */
public interface Route 
{
    /**
     * Edges ordered from source to target.
     * @return
     */
    public List<Edge> getEdges();


    /**
     * Routpoints ordered from source to target.
     * @return
     */
    public RoutePoint[] getRoutePoints();

    /**
     * length in meters.
     * @return
     */
    public double getLength();


    /**
     * travel time in milli seconds
     * @return
     */
    public long getTravelTime();


    /**
     * Temporary hack for the IFA.
     * @param out
     * @param includeEstimatedTimes
     */
    public void writeAsXml(OutputStream out, boolean includeEstimatedTimes);

    /**
     * Temporary, can be used to add the xml-scheme to a Stringbuffer.
     * @param buffer
     * @param includeEstimatedTimes
     */
    public void appendAsXml(StringBuffer buffer, boolean includeEstimatedTimes);

}
