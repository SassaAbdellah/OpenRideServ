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


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mapsforge.core.GeoCoordinate;


/** "Edge" interface in mapsforge. 
 *   Before updating to mapsforge.0.3.x
 *   org.mapsforge.server.routing.Edge was used.
 */
import org.mapsforge.core.Edge;
/**
 * "Router" Interface in mapsforge.
 *  Before updating to mapsforge.0.3.x 
 *  org.mapsforge.server.routing.IRouter was used.
 */
import org.mapsforge.core.Router;


/** "Vertex" interface in mapsforge.
 *  Before updating to mapsforege.0.3.x
 *  org.mapsforge.server.routing.IVertex was used.
 */
import org.mapsforge.core.Vertex;


/** Router implementation in mapsforge. 
 *  Before updating to mapsforge.0.3.x
 *  org.mapsforge.server.routing.highwayHierarchies.RouterImpl was used.
 */

import org.mapsforge.routing.server.hh.HHRouterServerside;


/**
 * Wraps the mapsforge router. It may be possible that there will be some
 * changes in futur versions of the mapsforger router interface. The
 * wrapper can be used to hide this changes to the openride projekt.
 * Additionally the wrapper hanles automated reloading, if the routing graph
 * is updated on the file system.
 * It is stored under %domain_root%/routingGraph.hh.
 * This file is checked for updates every 30 seconds.
 *
 * @author fvi
 */
public class RouterWrapper {

    private static final Logger logger = Logger.getLogger(RouterWrapper.class.getName());

    private static final File F_ROUTING_GRAPH = new File("routingGraph.hh").getAbsoluteFile();
    private static final long FILE_SYSTEM_CHECK_INTERVALL = 30000;
    private static RouterWrapper instance = null;

    private volatile Router router;
    private volatile long lastModified;

    public static RouterWrapper getInstance() {
        synchronized(RouterWrapper.class) {
            if(instance == null) {
                instance = new RouterWrapper();
            }
        }
        return instance;
    }

    private RouterWrapper() {
        loadRouter();

        // check for changes of the routing graph file (and reload if changed)
        Thread listenerThread = new Thread(){
            @Override
            public void run() {
                // polling loop
                while(true) {
                    try {
                        Thread.sleep(FILE_SYSTEM_CHECK_INTERVALL);
                    } catch (InterruptedException ex) {
                        logger.log(Level.SEVERE, null, ex);
                    }
                    if(F_ROUTING_GRAPH.exists() && lastModified != F_ROUTING_GRAPH.lastModified()) {
                        loadRouter();
                    }
                }
            }
        };
        listenerThread.start();
    }

    private synchronized boolean loadRouter() {
        logger.info("load routingGraph '" + F_ROUTING_GRAPH.getAbsolutePath() + "' ...");
        try {
            FileInputStream fis = new FileInputStream(F_ROUTING_GRAPH);
            Router tmp = HHRouterServerside.deserialize(fis);
            fis.close();

            this.router = tmp;
            this.lastModified = F_ROUTING_GRAPH.lastModified();
            logger.info("load routingGraph successfull!");
            return true;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        logger.info("load routingGraph not successfull!");
        return false;
    }
    
    public GeoCoordinate[] getMapCoordinates(GeoCoordinate[] coords) {
        // we need to use it as a variable since it might
        //be that the router gets reloaded from file system during computation.
        Router myRouter = this.router;
        if(coords != null && myRouter != null) {
            Vertex[] vertices = getVertices(myRouter, coords);
            GeoCoordinate[] result = new GeoCoordinate[vertices.length];
            for(int i=0;i<result.length;i++) {
                result[i] = vertices[i].getCoordinate();
            }
            return result;
        }
        return null;
    }

    public GeoCoordinate[] getVertexCoordinates(GeoCoordinate[] coords) {
        // we need to use it as a variable since it might
        //be that the router gets reloaded from file system during computation.
        Router myRouter = this.router;
        if(coords != null && myRouter != null) {
            Vertex[] vertices = getVertices(myRouter, coords);
            GeoCoordinate[] result = new GeoCoordinate[vertices.length];
            for(int i=0;i<result.length;i++) {
                result[i] = vertices[i].getCoordinate();
            }
            return result;
        }
        return null;
    }

    public LinkedList<GeoCoordinate> getShortestPath(GeoCoordinate s, GeoCoordinate t) {
        return getShortestPath(new GeoCoordinate[]{s, t});
    }

    public LinkedList<GeoCoordinate> getShortestPath(GeoCoordinate[] coords) {
        // we need to use it as a variable since it might
        //be that the router gets reloaded from file system during computation.
        Router myRouter = this.router;
        if(coords != null && myRouter != null) {
            Vertex[] vertices = getVertices(myRouter, coords);
            LinkedList<Edge> route = getRoute(myRouter, vertices);
            if(route == null || vertices == null) {
                return null;
            }
            LinkedList<GeoCoordinate> result = new LinkedList<GeoCoordinate>();
            if(route.size() == 0 && vertices.length > 0) {
                //source == target
                result.add(vertices[0].getCoordinate());
            } else {
                for(Edge e : route) {
                    int start;
                    if(e.equals(route.getFirst())) {
                        start = 0;
                    } else {
                        start = 1;
                    }
                    GeoCoordinate[] waypoints = e.getAllWaypoints();
                    for(int i=start;i<waypoints.length;i++) {
                        result.add(waypoints[i]);
                    }
                }
            }
            return result;
        } else if(myRouter == null) {
            String msg = "Could not compute shortest path."
                + "Probably the router could not be loaded from file system.";
            logger.info(msg);
        }
        return null;
    }

    public double getShortestPathAndTravelTime(GeoCoordinate s, GeoCoordinate t, LinkedList<GeoCoordinate> buffCoords, LinkedList<Integer> buffTime) {
        return getShortestPathAndTravelTime(new GeoCoordinate[]{s, t}, buffCoords, buffTime);
    }

    public double getShortestPathAndTravelTime(GeoCoordinate[] coords, LinkedList<GeoCoordinate> buffCoords, LinkedList<Integer> buffTime) {
        // we need to use it as a variable since it might
        //be that the router gets reloaded from file system during computation.
        Router myRouter = this.router;
        if(coords != null && myRouter != null) {
            Vertex[] vertices = getVertices(myRouter, coords);
            LinkedList<Edge> route = getRoute(myRouter, vertices);
            double routeLength = 0d;
            if(route == null) {
                return Double.MAX_VALUE;
            }
            if(route.size() == 0 && vertices.length > 0) {
                //source == target
                buffCoords.add(vertices[0].getCoordinate());
                buffTime.add(0);
            } else {
                double timeOffset = 0d;
                for(Edge e : route) {
                    GeoCoordinate[] waypoints = e.getAllWaypoints();
                    double edgeLength = getEdgeLength(waypoints);
                    routeLength += edgeLength;
                    if(e.equals(route.getFirst())) {
                        buffCoords.addLast(waypoints[0]);
                        buffTime.addLast((int)timeOffset);
                    }
                    for(int i=1;i<waypoints.length;i++) {
                        double sectionLength = GeoCoordinate.sphericalDistance(
                            waypoints[i-1].getLongitude(),
                            waypoints[i-1].getLatitude(),
                            waypoints[i].getLongitude(),
                            waypoints[i].getLatitude()
                        );
                        double sectionTime = (sectionLength / edgeLength) * ((double)e.getWeight());
                        timeOffset += (sectionTime * 100);
                        buffCoords.addLast(waypoints[i]);
                        buffTime.addLast((int)timeOffset);
                    }
                }
            }
            return routeLength;
        } else if(myRouter == null) {
            String msg = "Could not compute shortest path."
                + "Probably the router could not be loaded from file system.";
            logger.info(msg);
        }
        return Double.MAX_VALUE;
    }
    
    private static double getEdgeLength(GeoCoordinate[] waypoints) {
        double length = 0;
        for(int i=1;i<waypoints.length;i++) {
            length += GeoCoordinate.sphericalDistance(
                    waypoints[i-1].getLongitude(),
                    waypoints[i-1].getLatitude(),
                    waypoints[i].getLongitude(),
                    waypoints[i].getLatitude()
            );
        }
        return length;
    }

    private static LinkedList<Edge> getRoute(Router router, Vertex[] vertices) {
        LinkedList<Edge> route = new LinkedList<Edge>();
        for(int i=1;i<vertices.length;i++) {
            Edge[] edges = router.getShortestPath(vertices[i-1].getId(), vertices[i].getId());
            if(edges == null || edges.length == 0 && vertices[i-1].getId() != vertices[i].getId()) {
                return null;
            }
            for(int j=0;j<edges.length;j++) {
                route.addLast(edges[j]);
            }
        }
        return route;
    }

    private static Vertex[] getVertices(Router router, GeoCoordinate[] coords) {
        if(coords == null) {
            return new Vertex[0];
        }
        Vertex[] vertices = new Vertex[coords.length];
        for(int i=0;i<coords.length;i++) {
            vertices[i] = router.getNearestVertex(coords[i]);
        }
        return vertices;
    }

}
