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

import de.fhg.fokus.openride.matching.Constants;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.postgis.LineString;
import org.postgis.MultiLineString;

import de.fhg.fokus.openride.routing.Coordinate;
import de.fhg.fokus.openride.routing.Edge;
import de.fhg.fokus.openride.routing.Route;
import de.fhg.fokus.openride.routing.RoutePoint;
import de.fhg.fokus.openride.routing.Router;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import org.postgis.Point;



/**
 * @author fvi
 * 
 * Implements the Routing interface using OpenStreetmap Data
 * stored in a postgres / postgis database. For calculating the routes,
 * the pgrouting functions are used. This implementation uses a 
 * modified version of the dijstra_sp_directed function included in pgrouting.
 * For minimizing costs, this function takes the columns of the 'ways' table into account.
 * For calculating estimated arrival times, the colums, cost and reverse_cost are used.
 * For calculation of the length, the length and reverse_length colums are used.
 * 
 * The better the cost values are assigned, the more realistic the results are.
 * No changes to java code needed, only adjust the cost columns of table 'ways'.
 */
public class OsmRouter implements Router
{
    private final Logger logger = Logger.getLogger(OsmRouter.class.getName());

    public final static double DEFAULT_NEAREST_NEIGHBOR_THRESHOLD = 2000.0d;

    private final static int SRID = 3068;
    private final static String SQL_COLUMNNAME_SHORTEST_PATH_COST = "length";
    private final static String SQL_COLUMNNAME_SHORTEST_PATH_REVERSE_COST = "reverse_length";
    private final static String SQL_COLUMNNAME_FASTEST_PATH_COST = "ttime";
    private final static String SQL_COLUMNNAME_FASTEST_PATH_REVERSE_COST = "reverse_ttime";

    private final static String SQL_SELECT_DIJKSTRA_SP =
            "SELECT "
                + "w.ttime as ttime, "
                + "w.length * 1000 as length, "
                + "w.name as name, "
                + "w.source as source, "
                + "st_AsText(sp.the_geom) as geom "
            + "FROM "
                + "dijkstra_sp_directed_modified"
                + "("
                    + "'ways', "
                    + "?, "
                    + "?, "
                    + "true, "
                    + "true, "
                    + "?, "
                    + "?"
                + ") AS sp, "
                + "ways w "
            + "WHERE "
            + "w.gid = sp.gid "
            + "ORDER BY sp.id;";

    private final static String SQL_SELECT_NEAREST_NEIGHBOR =
        "SELECT "
            + "v.id as id "
        + "FROM "
            + "vertices_tmp v "
        + "WHERE "
            + "st_expand(st_transform(st_setsrid(st_makepoint(?,?), 4326), " + SRID + "), ?) && v.the_geom_3068 "
        + "AND st_distance(v.the_geom_3068, st_transform(st_setsrid(st_makepoint(?,?), 4326), " + SRID + ")) <= ? "
        + "ORDER BY st_distance(v.the_geom_3068, st_transform(st_setsrid(st_makepoint(?, ?), 4326), " + SRID + ")) "
        + "LIMIT 1;";

   
    private final PreparedStatement pstmtSelectDijkstraSP,
           pstmtSelectNearestNeighborVertex;

    /**
     * @param conn Connection to pgRouting database.
     * @throws SQLException
     */
    public OsmRouter(Connection conn) throws SQLException
    {
        this.pstmtSelectNearestNeighborVertex = conn.prepareStatement(SQL_SELECT_NEAREST_NEIGHBOR);
        this.pstmtSelectDijkstraSP = conn.prepareStatement(SQL_SELECT_DIJKSTRA_SP);
    }

    public static synchronized Connection getConnection() throws ClassNotFoundException, SQLException {
       Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://"
                + Constants.PGROUTING_DB_HOST
                + ":" + Constants.PGROUTING_DB_PORT
                + "/" + Constants.PGROUTING_DB_NAME;
        Connection conn = DriverManager.getConnection(
                url,
                Constants.PGROUTING_DB_USER,
                Constants.PGROUTING_DB_PASS
        );
        return conn;
    }

    @Override
    public OsmRoute findRoute(Coordinate source, Coordinate target, Timestamp startTime, boolean fastestPath, double threshold, boolean includeWaypoints) {
       if(threshold < 0.0d){
           threshold = DEFAULT_NEAREST_NEIGHBOR_THRESHOLD;
       }
       try {
            /* find a node near source an target, threshold is this.nearestNeighborTolerance */
            Integer sourceId = selectNearestNeighborVertex(source, threshold);
            Integer targetId = selectNearestNeighborVertex(target, threshold);

            if(sourceId == null || targetId == null)
            {
                /* if no node found in given radius, we cannot compute a route */
                if(sourceId == null)
                {
                    //logger.info("[OsmRouter] no source vertex found near" + source);
                }
                else{
                    //logger.info("[OsmRouter] no target vertex found near " + target);
                }
                return null;
            }

            if(sourceId.equals(targetId)){
                /* in this case we don't need to do anything */
                return new OsmRoute(new LinkedList<Edge>(), new RoutePoint[0]);
            }

            /* compute shortest path */
            LinkedList<Edge> edges = selectDijkstraSp(sourceId, targetId, fastestPath, startTime, includeWaypoints, 0.0d);
            if(edges.size() == 0){
                /*  if list is empty no path way found  since sourceId != targetId */
                //logger.info("[OsmRouter] no connection between Vertices: " + sourceId + " " + targetId);
                return null;
            }
         
            return new OsmRoute(edges, getRoutePoints(edges));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public RoutePoint[] getEquiDistantRoutePoints(Coordinate[] coordinates,
            Timestamp startTime, boolean fastestPath, double threshold,
            double maxDistanceOfPoints){
        logger.info("getEquiDistantRoutePoints(" + "(" + coordinates[0].getLatititude() + "," + coordinates[0].getLongitude() + ") -> ... -> (" + coordinates[coordinates.length - 1].getLatititude() + "," + coordinates[coordinates.length - 1].getLongitude() + ")");

        if(coordinates.length < 2) {
             logger.info("must give at least two coordinates, got " + coordinates.length);
             return null;
        }
        if(threshold < 0.0d) threshold = DEFAULT_NEAREST_NEIGHBOR_THRESHOLD;
        
        try {
            /* get vertices for coordinates */
            Integer[] vertexIds = new Integer[coordinates.length];
            for(int i=0;i<coordinates.length;i++){
                vertexIds[i] = selectNearestNeighborVertex(coordinates[i], threshold);
                if (vertexIds[i] == null) {
                    logger.info("no vertex found for coordinate (" + coordinates[i].getLatititude() + "," + coordinates[i].getLongitude() + ")");
                    return null;
                }
            }
            LinkedList<Edge> edges = new LinkedList<Edge>();
            double distanceOffset = 0.0d;
            /* compute partial routes */
            for(int i=1;i<vertexIds.length;i++){
                if(vertexIds[i].equals(vertexIds[i-1])) {
                    logger.info("skip section : vertex@" + vertexIds[i-1] + " -> vertex@" + vertexIds[i]);
                    continue;
                }
                logger.info("compute section : vertex@" + vertexIds[i-1] + " -> vertex@" + vertexIds[i]);
                LinkedList<Edge> list = selectDijkstraSp(
                        vertexIds[i-1],
                        vertexIds[i],
                        fastestPath,
                        startTime,
                        fastestPath,
                        distanceOffset
                );
                /* if no route has been found between two of the vertices, return null */
                if(list == null || list.size() == 0){
                    if(list == null) {
                        logger.info("LIST IS NULL!!!");
                    }
                    if(list.size() == 0) {
                        logger.info("LISTSIZE IS 0!!!");
                    }
                    return null;
                }else{
                    edges.addAll(list);
                }

                startTime = list.getLast().getTarget().getTimeAt();
                distanceOffset = list.getLast().getTarget().getDistance();
            }

            //check
            for(int i = 1;i<edges.size();i++){
                Edge e = edges.get(i - 1);
                Edge e_ = edges.get(i);
                if(e.getTarget().getCoordinate().getLatititude() == e_.getSource().getCoordinate().getLatititude()
                        && e.getTarget().getCoordinate().getLongitude() == e_.getSource().getCoordinate().getLongitude()){
                } else {
                    logger.info("target of edge[" + (i-1) + "] does not match source of edge[" + i + "]");
                }
            }


            //end check
            RoutePoint[] rp = getRoutePoints(edges);
            if(rp.length == 0) {
                logger.info("no routepoints on route!");
                return new RoutePoint[] {
                    new RoutePoint(coordinates[0], startTime, 0),
                    new RoutePoint(coordinates[coordinates.length - 1], startTime, 0),
                };
            }
            double routeLength = rp[rp.length - 1].getDistance();
            int m = (int)Math.ceil(routeLength / maxDistanceOfPoints) + 1;
            RoutePoint[] interpolatedPoints = decomposeRoute(rp, m);
            return interpolatedPoints;
        } catch (SQLException ex) {
            Logger.getLogger(OsmRouter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * calculates m interpolated Routpoints based on given
     * RoutePoints. Coordinates, Times and Distance are interpolated,
     * except the first an last RoutePoints.
     * @param rp basis for interpolation.
     * @param m number of interpolated points.
     * @return interpolated routePoints.
     */
    public RoutePoint[] decomposeRoute(RoutePoint[] rp, int m){
        RoutePoint[] result = new RoutePoint[m];
        double routeLength = rp[rp.length - 1].getDistance();
        double passPointdistance = routeLength / (m - 1);

        /* interpolate */
        result[0] = rp[0];
        result[m - 1] = rp[rp.length - 1];
        int j = 1;
        for(int i=1;i<rp.length;i++){
             while(rp[i].getDistance() > j * passPointdistance){
                double l = (j * passPointdistance) - rp[i-1].getDistance();
                double a = rp[i].getDistance() - rp[i-1].getDistance();
                double delta = 1.0d - (l / a);
                result[j] = interpolateRoutePoints(rp[i-1], rp[i], delta);
                j++;
            }
        }
        return result;
    }


    /**
     * Computes delta * a + (1-delata) * b.
     * @param a first point
     * @param b second point
     * @param delta must be in [0 .. 1]
     * @return
     */
    private RoutePoint interpolateRoutePoints(RoutePoint a, RoutePoint b, double delta){
        return
        new RoutePoint(
            new Coordinate(
                interpolateDouble(
                    a.getCoordinate().getLatititude(),
                    b.getCoordinate().getLatititude(),
                    delta
                ),
                interpolateDouble(
                    a.getCoordinate().getLongitude(),
                    b.getCoordinate().getLongitude(),
                    delta
                )
            ),
            new Timestamp(
                interpolateLong(
                    a.getTimeAt().getTime(),
                    b.getTimeAt().getTime(),
                    delta
                )
            ),
            interpolateDouble(
                a.getDistance(),
                b.getDistance(),
                delta
            )
        );
    }

    /**
     * Computes delta * a + (1-delata) * b.
     * @param a
     * @param b
     * @param delta must be in [0 .. 1]
     * @return
     */
    private double interpolateDouble(double a, double b, double delta){
        return (delta * a) + ((1 - delta) * b);
    }

    /**
     * Computes delta * a + (1-delata) * b.
     * @param a
     * @param b
     * @param delta must be in [0 .. 1]
     * @return
     */
    private long interpolateLong(long a, long b, double delta){
        return (long)((delta * a) + ((1 - delta) * b));
    }

    /**
     * get all routePoints of given edges.
     * @param edges
     * @return
     */
    private RoutePoint[] getRoutePoints(LinkedList<Edge> edges){
        if(edges.size() == 0) {
            return new RoutePoint[0];
        }

        /* count routePoints */
        int numRoutePoints = 1;
        for(Edge e : edges){
            numRoutePoints += e.getWayPoints().length - 1;
        }
        /* write to array */
        RoutePoint[] rp = new RoutePoint[numRoutePoints];
        rp[0] = edges.getFirst().getSource();
        int i = 1;
        for(Edge e : edges){
            RoutePoint[] wp = e.getWayPoints();
            for(int j = 1;j<wp.length;j++){
                rp[i++] = wp[j];
            }
        }
        return rp;
    }


    /**
     * Get edges along the shortest path.
     * @param sourceId id of source vertex.
     * @param targetId id of target vertex.
     * @param fastestPath switches between shortest and fastest path.
     * @param startTime time when driver will leaave source.
     * @param includeWaypoints include non crossing coordinates in result.
     * @param distanceOffset added to each distance.
     * @return edges along the shortest path.
     * @throws SQLException
     */
    private LinkedList<Edge> selectDijkstraSp(int sourceId, int targetId, boolean fastestPath, Timestamp startTime, boolean includeWaypoints, double distanceOffset) throws SQLException{
        /* execute dijkstra query */
        pstmtSelectDijkstraSP.setInt(1, sourceId);
        pstmtSelectDijkstraSP.setInt(2, targetId);
        if(fastestPath){
            pstmtSelectDijkstraSP.setString(3, SQL_COLUMNNAME_FASTEST_PATH_COST);
            pstmtSelectDijkstraSP.setString(4, SQL_COLUMNNAME_FASTEST_PATH_REVERSE_COST);
        }
        else {
            pstmtSelectDijkstraSP.setString(3, SQL_COLUMNNAME_SHORTEST_PATH_COST);
            pstmtSelectDijkstraSP.setString(4, SQL_COLUMNNAME_SHORTEST_PATH_REVERSE_COST);
        }
        ResultSet result = pstmtSelectDijkstraSP.executeQuery();

        /* get edges from resultSet */
        LinkedList<Edge> edges = new LinkedList<Edge>();

        long timeOffset = startTime.getTime();
        while(result.next())
        {
            /* get edge from result set */
            String name = result.getString("name");
            if(name == null) name = "";
            else name = name.trim();
            double length = result.getDouble("length");
            LineString geom =  new MultiLineString(result.getString("geom")).getLine(0);
            long ttime = (long)(result.getDouble("ttime") * 1000);


            /* get routepoints along the edge */
            RoutePoint[] waypoints = getEdgeWaypoints(geom, ttime, length, timeOffset, distanceOffset, includeWaypoints);
            Edge e = new Edge(name, waypoints);

            //check if source matches target of previos edge - if not reverse
            if(edges.size() >= 1 && !(edges.getLast().getTarget().getCoordinate().equals(e.getSource().getCoordinate()))) {
                reverseWayPoints(waypoints);
            }

            edges.add(e);

            //check if source is sourceId - if not reverse
            if(edges.size() == 1) {
                int source = result.getInt("source");
                if(source != sourceId) {
                    reverseWayPoints(waypoints);
                }
            }

            /* adjust offsets for next edge */
            timeOffset += ttime;
            distanceOffset += length;
        }
        return edges;
    }

    private void reverseWayPoints(RoutePoint[] rp) {
        for(int i=0;i<rp.length / 2;i++) {
            int j = rp.length - 1 - i;
            RoutePoint tmp = rp[i];
            rp[i] = rp[j];
            rp[j] = tmp;
        }
    }

    /**
     *
     * @param geom Waypoints of the edge.
     * @param travelTime time to travel along the edge in millis.
     * @param TimeOffset time at start of the edge in millis.
     * @param distanceOffset distance traveled at start of the edge.
     * @param length length of given edge in meters.
     * @param includeWaypoints create RoutePoints for non crossing nodes.
     * @return
     */
    private RoutePoint[] getEdgeWaypoints(LineString geom, long travelTime, double length, long TimeOffset, double distanceOffset, boolean includeWaypoints){
        if(!includeWaypoints){
            RoutePoint[] rp = new RoutePoint[2];
            Point p1 = geom.getFirstPoint();
            Point p2 = geom.getLastPoint();
            rp[0] = new RoutePoint(
                    new Coordinate(
                        p1.getY(),
                        p1.getX()
                    ),
                    new Timestamp(TimeOffset),
                    distanceOffset
            );
            rp[1] = new RoutePoint(
                    new Coordinate(
                        p2.getY(),
                        p2.getX()
                    ),
                    new Timestamp(TimeOffset + travelTime),
                    distanceOffset + length
            );
            return rp;
        }
        /* get the Coordinates of given linestring */
        Coordinate[] c = new Coordinate[geom.numPoints()];
        for(int i=0;i<c.length;i++){
            Point p = geom.getPoint(i);
            c[i] = new Coordinate(
                p.getY(),
                p.getX()
            );
        }
        /* compute distances d[] to each coordinate */
        double[] d = new double[c.length];
        d[0] = 0.0d;
        for(int i=1;i<d.length;i++){
            d[i] = d[i-1] + c[i].distanceSphere(c[i-1]);
        }
        /* compute arrivalTimes t[] for each coordinate */
        double[] t = new double[c.length];
        t[0] = 0.0d;
        for(int i=1;i<t.length;i++){
            t[i] = (d[i] / d[c.length - 1]) * travelTime;
        }
        /* create routepoint array */
        RoutePoint[] rp = new RoutePoint[c.length];
        for(int i=0;i<c.length;i++){
            rp[i] = new RoutePoint(c[i], new Timestamp(((long)t[i]) + TimeOffset), d[i] + distanceOffset);
        }
        return rp;
    }


    /**
     *
     * @param c the coordinate where to look for nearest neighbor.
     * @param threshold max. distance to Coordinate c in meters.
     * @return id of vertex near to c. null if there is no vertex
     * within given threshold
     * @throws SQLException
     */
    private Integer selectNearestNeighborVertex(Coordinate c, double threshold) throws SQLException{
        pstmtSelectNearestNeighborVertex.setDouble(1, c.getLongitude());
        pstmtSelectNearestNeighborVertex.setDouble(2, c.getLatititude());
        pstmtSelectNearestNeighborVertex.setDouble(3, threshold);
        pstmtSelectNearestNeighborVertex.setDouble(4, c.getLongitude());
        pstmtSelectNearestNeighborVertex.setDouble(5, c.getLatititude());
        pstmtSelectNearestNeighborVertex.setDouble(6, threshold);
        pstmtSelectNearestNeighborVertex.setDouble(7, c.getLongitude());
        pstmtSelectNearestNeighborVertex.setDouble(8, c.getLatititude());
        ResultSet rs = pstmtSelectNearestNeighborVertex.executeQuery();
        Integer id = null;
        if(rs.next()){
            id = rs.getInt("id");
        }
        rs.close();
        if(id == null){
            logger.info("no vertex found for coordinate " + c);
        }
        
        return id;
    }

    public static void main(String[] args) {
        try {
            //testRouting1();
            //testRouting2();
            //testRouting3();
            testRouting4();

        } catch (SQLException ex) {
            Logger.getLogger(OsmRouter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testRouting1() throws SQLException{
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/routing_osm";
            Connection conn = DriverManager.getConnection(url, "postgres", "admin");
            OsmRouter router = new OsmRouter(conn);

            System.out.println("\nTEST ROUTING");
            Coordinate source = new Coordinate(52.5257117, 13.3074965)   ;
            Coordinate target = new Coordinate(52.5092499, 13.4572807)   ;

            OsmRoute route = router.findRoute(source, target, new Timestamp(0), true, 2000.0d, true);

            if(route != null){
                for(int i=0;i<route.getEdges().size();i++){
                    System.out.println(route.getEdges().get(i).toString());
                }
                DecimalFormat format = new DecimalFormat("#.#");
                System.out.println("route length: " + format.format(route.getLength()) + "m / " + format.format(route.getTravelTime() / 1000) + " s");
            }
            else System.out.println("no route found!");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OsmRouter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testRouting2() throws SQLException{
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/routing_osm";
            Connection conn = DriverManager.getConnection(url, "postgres", "admin");
            OsmRouter router = new OsmRouter(conn);

            System.out.println("\nTEST ROUTING");
            Coordinate[] c = new Coordinate[]{
                new Coordinate(52.5257117, 13.3074965),
               new Coordinate(52.5092499, 13.4572807)
            };
            RoutePoint[] rp = router.getEquiDistantRoutePoints(c, new Timestamp(0), true,  2000.0d, 1000.0d);
            for(int i=1;i<rp.length;i++){
                System.out.println(
                        rp[i].getCoordinate().distanceSphere(rp[i-1].getCoordinate())
                + " ~ " + (rp[i].getDistance() - rp[i-1].getDistance())

                );
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OsmRouter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testRouting3() throws SQLException{
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/routing_osm";
            Connection conn = DriverManager.getConnection(url, "postgres", "admin");
            OsmRouter router = new OsmRouter(conn);

            System.out.println("\nTEST ROUTING");
            Coordinate source = new Coordinate(52.5257117, 13.3074965)   ;
            Coordinate target = new Coordinate(52.5092499, 13.4572807)   ;

            OsmRoute route = router.findRoute(source, target, new Timestamp(0), true, 2000.0d, true);

            if(route != null){
                double d = 0.0d;
                for(int i=1;i<route.getRoutePoints().length;i++){
                    d += route.getRoutePoints()[i].getCoordinate().distanceSphere(
                        route.getRoutePoints()[i - 1].getCoordinate());
                    System.out.println(
                            route.getRoutePoints()[i].getDistance() + " " + d);
                }
                DecimalFormat format = new DecimalFormat("#.#");
                System.out.println("route length: " + format.format(route.getLength()) + "m / " + format.format(route.getTravelTime() / 1000) + " s");
            }
            else System.out.println("no route found!");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OsmRouter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void testRouting4() throws SQLException{
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/routing_osm";
            Connection conn = DriverManager.getConnection(url, "postgres", "admin");
            OsmRouter router = new OsmRouter(conn);

            System.out.println("\nTEST ROUTING");
            Coordinate[] c = new Coordinate[]{
                new Coordinate(52.5257117, 13.3074965),
                new Coordinate(52.5757117, 13.2874965),
                new Coordinate(52.5092499, 13.4572807)
            };
           
            RoutePoint[] rp = router.getEquiDistantRoutePoints(c, new Timestamp(0), true,  2000.0d, 1000.0d);
            System.out.println(rp[0].getCoordinate() + " " + rp[rp.length - 1].getCoordinate());

            for(int i=1;i<rp.length;i++){
                System.out.println(rp[i].getDistance() + "m "  + rp[i].getTimeAt());
            }
            Route routeA = router.findRoute(c[0], c[1], new Timestamp(0), true, 2000.0d, false);
            Route routeB = router.findRoute(c[1], c[2], new Timestamp(0), true, 2000.0d, false);
            System.out.println("length = " + (routeA.getLength() + routeB.getLength()));
            System.out.println("ttime = " + new Timestamp(routeA.getTravelTime() + routeB.getTravelTime()));

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OsmRouter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}










