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

import de.fhg.fokus.openride.routing.osm.HHRouter;
import de.fhg.fokus.openride.routing.osm.OsmRouter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author fvi
 */
@Stateless
public class RouterBean implements RouterBeanLocal, Router {

    private Router router;

    public RouterBean(){
        router = new HHRouter();
    }

    public Route findRoute(Coordinate source, Coordinate target,
            Timestamp startTime, boolean fastestPath, double threshold,
            boolean includeWaypoints){

        return router.findRoute(source, target, startTime, fastestPath, threshold, includeWaypoints);

        /*
        Route route = null;
        Connection conn = null;
        try {
            conn = lookupConnection();
            Router router = new OsmRouter(conn);
            route = router.findRoute(source, target, startTime, fastestPath, threshold, includeWaypoints);
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(RouterBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch(NamingException ex) {
            Logger.getLogger(RouterBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RouterBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return route;
        */

    }

    public RoutePoint[] getEquiDistantRoutePoints(Coordinate[] coordinates,
            Timestamp startTime, boolean fastestPath, double threshold,
            double maxDistanceOfPoints){
        return router.getEquiDistantRoutePoints(coordinates, startTime, fastestPath, threshold, maxDistanceOfPoints);

        /*
        RoutePoint[] rp = null;
        Connection conn = null;
        try {
            conn = lookupConnection();
            Router router = new OsmRouter(conn);
            rp = router.getEquiDistantRoutePoints(coordinates, startTime, fastestPath, threshold, maxDistanceOfPoints);
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(RouterBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch(NamingException ex) {
            Logger.getLogger(RouterBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RouterBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return rp;
         * */

    }

    private Connection lookupConnection() throws NamingException, SQLException {
         InitialContext ic = new InitialContext();
         DataSource ds = (DataSource) ic.lookup("jdbc/routing_osm");
         return ds.getConnection();
    }


}
