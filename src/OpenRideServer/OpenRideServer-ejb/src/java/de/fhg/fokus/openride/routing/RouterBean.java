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

package de.fhg.fokus.openride.routing;

import de.fhg.fokus.openride.routing.graphhopper.routing.GraphhopperRouter;
import java.sql.Timestamp;
import javax.ejb.Stateless;

/**
 *
 * @author fvi
 */
@Stateless
public class RouterBean implements RouterBeanLocal, Router {

    private Router router;

    public RouterBean(){
       
        // Use GrapHopperRouter to enable GraphHopperRouting 
        this.router=new GraphhopperRouter();
    }
                                                       
    @Override
    public Route findRoute(Coordinate source, Coordinate target,
            Timestamp startTime, boolean fastestPath, double threshold){

        return router.findRoute(source, target, startTime, fastestPath, threshold); 
    }

    @Override
    public RoutePoint[] getEquiDistantRoutePoints(Coordinate[] coordinates,
            Timestamp startTime, boolean fastestPath, double threshold,
            double maxDistanceOfPoints){
        return router.getEquiDistantRoutePoints(coordinates, startTime, fastestPath, threshold, maxDistanceOfPoints);
    }
}
