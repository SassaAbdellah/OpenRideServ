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

package de.fhg.fokus.openride.matching;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;

import org.postgis.Point;

import de.fhg.fokus.openride.routing.Coordinate;
import de.fhg.fokus.openride.routing.RoutePoint;

import java.sql.PreparedStatement;

/**
 * This class implements the driver side, of the circle overlay algorithm,
 * that is to say it search riders for a given driver. The implementation
 * is based on a postgis database, thus circle overlays are computed by
 * utilizing postis' gist index, which indexes geographic positions.
 *
 * As parameters for computing the circle overlay, drivers equi-distant
 * route points and the radius of the circles are required. Note that
 * the radius is in some way related to the distance of the route points.
 * Runtime of the algorithm, the number of fals positivs and false negativs
 * heavily depends on these two parameters.
 * 
 * @author fvi
 */
final class RiderSearchAlgorithm {

    /* spatial reference systems for postgis geometries */
    public static final  int SRID_CARTHESIAN = 3068;  // projection to the plane, most accurate within germany
    public static final  int SRID_POLAR = 4326;       // the wgs84 reference system (lon, lat)
    /* default circle radius */
    public static final double DEFAULT_D = 2000.d;

    // we need to create a single temp table
    private static final String TEMP_TABLE_NAME = "passpoints_tmp";
    private static final  String SQL_CREATE_TEMP_PASSPOINT_TABLE =
            "CREATE TEMP TABLE " + TEMP_TABLE_NAME + " "
            + "("
                    + "route_idx INTEGER, "
                    + "coordinate GEOMETRY, "
                    + "time_at TIMESTAMP, "
                    + "distance_to_source DOUBLE PRECISION "
            + ");";

    private static final String SQL_DROP_TEMP_PASSPOINT_TABLE =
            "DROP TABLE IF EXISTS " + TEMP_TABLE_NAME + ";";

    private static final String SQL_INSERT_TEMP_PASSPOINTS =
          "INSERT INTO " + TEMP_TABLE_NAME + " "
        + "("
            + "route_idx, "
            + "coordinate, "
            + "time_at, "
            + "distance_to_source"
        + ") "
        + "VALUES "
        + "( "
            + "?, "  //routeIdx             //lon       //lat
            + "st_transform(st_setsrid(st_makepoint(?, ?), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "), "
            + "?, " //timestamp at passpoint
            + "? " //distance to source
        + ");";

    // create two indices on the temp table
    private static final String SQL_CREATE_TEMP_PASSPOINTS_INDEX_COORDS =
            "CREATE INDEX idx__" + TEMP_TABLE_NAME + "__pass_point ON " + TEMP_TABLE_NAME + " USING gist(coordinate);";
    private static final String SQL_CREATE_TEMP_PASSPOINTS_INDEX_TIMEAT =
            "CREATE INDEX idx__" + TEMP_TABLE_NAME + "__time_at_pass_point ON " + TEMP_TABLE_NAME + " (time_at);";

    // the query to select matches - pre : drivers route points reside in the temp table
    private static final String SQL_SELECT_MATCHES =
            "SELECT "
            + "s.riderroute_id, "
            + "l.route_idx as lift_idx, "
            + "d.route_idx as drop_idx, "
            + "st_distance(l.coordinate, s.startpt_c) as lift_detour, "
            + "st_distance(d.coordinate, s.endpt_c) as drop_detour, "
            + "d.time_at as time_at_on_route_drop_point, "
            + "l.time_at as time_at_on_route_lift_point, "
            + "s.starttime_earliest as time_at_lift_point, "
            + "st_AsText(st_transform(s.startpt_c, " + SRID_POLAR + ")) as lift_point, "
            + "st_AsText(st_transform(s.endpt_c, " + SRID_POLAR + ")) as drop_point, "
            + "st_AsText(st_transform(l.coordinate, " + SRID_POLAR + ")) as on_route_lift_point, "
            + "st_AsText(st_transform(d.coordinate, " + SRID_POLAR + ")) as on_route_drop_point, "
            + "(d.distance_to_source - l.distance_to_source) AS shared_distance "
        + "FROM  "
            + "riderundertakesride s, "
            + TEMP_TABLE_NAME + " l,  "
            + TEMP_TABLE_NAME + " d "
        + "WHERE  "
                + "( "
                     /* big circle around center of source and destination */
                     + "( "
                        + "s.startpt_c && st_expand "
                        + "( "                              //center.getLongitude()           //center.getLatititude()
                            + "st_transform(st_setsrid(st_makepoint(?, ?), " + SRID_POLAR + "), "+SRID_CARTHESIAN + "),  "
                            + "? " //radius
                        + ") "
                        + "AND st_distance "
                        + "( "                             //center.getLongitude()           //center.getLatititude()
                            + "st_transform(st_setsrid(st_makepoint(?, ?), " + SRID_POLAR + "), "+SRID_CARTHESIAN + "),  "
                            + "s.startpt_c "
                        + ") <= ? " //radius
                    + ") "
                    + "OR "
                    /* small circle arround source */
                    + "( "
                        + "s.startpt_c && st_expand "
                        + "( "                                  //source.getCoordinate().getLongitude()         //source.getCoordinate().getLatititude()
                            + "st_transform(st_setsrid(st_makepoint(?, ?), " + SRID_POLAR + "), "+SRID_CARTHESIAN + "),  "
                            + "? " //d
                        + ") "
                         + "AND st_distance "
                        + "( "                                  //source.getCoordinate().getLongitude()         //source.getCoordinate().getLatititude()
                            + "st_transform(st_setsrid(st_makepoint(?, ?), " + SRID_POLAR + "), "+SRID_CARTHESIAN + "),  "
                            + "s.startpt_c "
                        + ") <= ? " //d
                    + ") "
                + ") "
                /* time between start time and expected finish time */
                                                  //source.getTimeAt().toString()
                                                 //source.getTimeAt()
                + "AND s.starttime_latest >= ? "
                                                    //destination.getTimeAt()
                + "AND s.starttime_earliest <= ? "
                + "AND "
                + "( "
                    /* big circle through center of liftpoint and route destination overlay with drop points */
                    + "( "
                        + "s.endpt_c && st_expand"
                        + "( "                                                                 //destination.getCoordinate().getLongitude() destination.getCoordinate().getLatititude()
                            + "st_Centroid(st_makeline(s.startpt_c, st_transform(st_setsrid(st_makepoint(?, ?), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "))),   "
                                                                                       //destination.getCoordinate().getLongitude() destination.getCoordinate().getLatititude()
                            + "st_distance(s.startpt_c, st_transform(st_setsrid(st_makepoint(?, ?), " + SRID_POLAR + "), " + SRID_CARTHESIAN + ")) / 2  "
                        + ")  "
                        + "AND st_distance "
                        + "( "
                            + "s.endpt_c,  "                                                      //destination.getCoordinate().getLongitude()    destination.getCoordinate().getLatititude()
                            + "st_Centroid(st_makeline(s.startpt_c, st_transform(st_setsrid(st_makepoint(?, ?), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "))) "
                                                                                    //destination.getCoordinate().getLongitude() destination.getCoordinate().getLatititude()
                        + ") <= st_distance(s.startpt_c, st_transform(st_setsrid(st_makepoint(?, ?), " + SRID_POLAR + "), " + SRID_CARTHESIAN + ")) / 2  "
                    + ") "
                    + "OR "
                    /* small circle around route destination */
                    + "( "
                        + "s.endpt_c && st_expand "
                        + "( "                              //destination.getCoordinate().getLongitude() destination.getCoordinate().getLatititude()
                            + "st_transform(st_setsrid(st_makepoint(?, ?), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "), "
                            + "? " //d
                        + ") "
                         + "AND st_distance "
                        + "( "                                  //destination.getCoordinate().getLongitude()  destination.getCoordinate().getLatititude()
                            + "st_transform(st_setsrid(st_makepoint(?, ?), " + SRID_POLAR + "),  "  + SRID_CARTHESIAN + "), "
                            + "s.endpt_c "
                        + ") <= ? " //d
                    + ") "
                + ") "
                /* choose on route lift point from all passpoints having minimal distance to s.lift_point */
                + "AND l.coordinate && st_expand(s.startpt_c, ?) " // d
                + "AND st_distance(l.coordinate, s.startpt_c) <= ? " //d
                + "AND d.coordinate && st_expand(s.endpt_c, ?) " //d
                + "AND st_distance(d.coordinate, s.endpt_c) <= ? " //d
                + "AND st_distance(l.coordinate, s.startpt_c) = "
                + "( "
                + "SELECT   "
                    + "MIN(st_distance(s.startpt_c, pp.coordinate))  "
                + "FROM  "
                    + TEMP_TABLE_NAME + " pp  "
                + "WHERE  "
                    + "pp.coordinate && st_expand(s.startpt_c, ?)  " //d
                    //CHANGE
                    + "AND pp.time_at <= s.starttime_latest "
                    + "AND pp.time_at >= s.starttime_earliest "
                    //END CHANGE TODO: check if time check is correct!
                + ") "
                /* choose on route drop point from all passpoints (l.drop_point) having minimal distance to s.drop_point */
                + "AND st_distance(d.coordinate, s.endpt_c) =   "
                + "(  "
                    + "SELECT   "
                        + "MIN(st_distance(s.endpt_c, pp.coordinate))  "
                    + "FROM  "
                        + TEMP_TABLE_NAME + " pp  "
                    + "WHERE  "
                        + "pp.coordinate && st_expand(s.endpt_c, ?)  " //d
                + ") "
                + "ORDER BY l.route_idx - d.route_idx; ";

    private final Connection conn;
    private final PreparedStatement pstmtCreateTempPasspointTable,
        pstmtDropTempPasspointTable, pstmtInsertTempPasspoint,
        pstmtCreateTempPasspointIndexCoords, pstmtCreateTempPasspointIndexTimeAt,
        pstmtSelectMatches;

    /**
     * @param conn must be open.
     * @throws SQLException
     */
    public RiderSearchAlgorithm(Connection conn) throws SQLException {
        this.conn = conn;
        this.conn.setAutoCommit(true);
        this.pstmtCreateTempPasspointTable = conn.prepareStatement(SQL_CREATE_TEMP_PASSPOINT_TABLE);
        this.pstmtDropTempPasspointTable = conn.prepareStatement(SQL_DROP_TEMP_PASSPOINT_TABLE);
        this.pstmtInsertTempPasspoint = conn.prepareStatement(SQL_INSERT_TEMP_PASSPOINTS);
        this.pstmtCreateTempPasspointIndexCoords = conn.prepareStatement(SQL_CREATE_TEMP_PASSPOINTS_INDEX_COORDS);
        this.pstmtCreateTempPasspointIndexTimeAt = conn.prepareStatement(SQL_CREATE_TEMP_PASSPOINTS_INDEX_TIMEAT);
        this.pstmtSelectMatches = conn.prepareStatement(SQL_SELECT_MATCHES);
    }

    /**
     * Open Ride Route-Matching by geometric circle overlay and time.
     * @param rideId only used for constructing the PotentialMatch class.
     * @param decomposedRoute get this from RouterBean.
     * @param d circle radius.
     * @return list of potential matches, empty list if no match found.
     * @throws IllegalArgumentException if supplied parameters are wrong.
     * @throws SQLException on jdbc related problems.
     */
    public LinkedList<PotentialMatch> findRiders(int rideId, RoutePoint[] decomposedRoute, double d)
            throws IllegalArgumentException, SQLException {
        
        //check parameters
        if(decomposedRoute == null || decomposedRoute.length < 2 || d < 0) {
            throw new IllegalArgumentException();
        }


        //do all work within one transaction
        conn.setAutoCommit(false);

        //create temporary table for driver's route points :
        pstmtDropTempPasspointTable.executeUpdate();
        pstmtCreateTempPasspointTable.executeUpdate();
        for(int i=0;i<decomposedRoute.length;i++){
            pstmtInsertTempPasspoint.setInt(1, i);
            pstmtInsertTempPasspoint.setDouble(2, decomposedRoute[i].getCoordinate().getLongitude());
            pstmtInsertTempPasspoint.setDouble(3, decomposedRoute[i].getCoordinate().getLatititude());
            pstmtInsertTempPasspoint.setTimestamp(4, decomposedRoute[i].getTimeAt());
            pstmtInsertTempPasspoint.setDouble(5, decomposedRoute[i].getDistance());
            pstmtInsertTempPasspoint.executeUpdate();
        }
        pstmtCreateTempPasspointIndexCoords.executeUpdate();
        pstmtCreateTempPasspointIndexTimeAt.executeUpdate();


        //get matches
        RoutePoint source = decomposedRoute[0];
        RoutePoint destination = decomposedRoute[decomposedRoute.length - 1];
        Coordinate center = Coordinate.interpolate(
            source.getCoordinate(),
            destination.getCoordinate(),
            0.5d
        );
        double radius = center.distanceSphere(source.getCoordinate());
        pstmtSelectMatches.setDouble(1, center.getLongitude());
        pstmtSelectMatches.setDouble(2, center.getLatititude());
        pstmtSelectMatches.setDouble(3, radius);
        pstmtSelectMatches.setDouble(4, center.getLongitude());
        pstmtSelectMatches.setDouble(5, center.getLatititude());
        pstmtSelectMatches.setDouble(6, radius);
        pstmtSelectMatches.setDouble(7, source.getCoordinate().getLongitude());
        pstmtSelectMatches.setDouble(8, source.getCoordinate().getLatititude());
        pstmtSelectMatches.setDouble(9, d);
        pstmtSelectMatches.setDouble(10, source.getCoordinate().getLongitude());
        pstmtSelectMatches.setDouble(11, source.getCoordinate().getLatititude());
        pstmtSelectMatches.setDouble(12, d);
        pstmtSelectMatches.setTimestamp(13, source.getTimeAt());
        pstmtSelectMatches.setTimestamp(14, destination.getTimeAt());
        pstmtSelectMatches.setDouble(15, destination.getCoordinate().getLongitude());
        pstmtSelectMatches.setDouble(16, destination.getCoordinate().getLatititude());
        pstmtSelectMatches.setDouble(17, destination.getCoordinate().getLongitude());
        pstmtSelectMatches.setDouble(18, destination.getCoordinate().getLatititude());
        pstmtSelectMatches.setDouble(19, destination.getCoordinate().getLongitude());
        pstmtSelectMatches.setDouble(20, destination.getCoordinate().getLatititude());
        pstmtSelectMatches.setDouble(21, destination.getCoordinate().getLongitude());
        pstmtSelectMatches.setDouble(22, destination.getCoordinate().getLatititude());
        pstmtSelectMatches.setDouble(23, destination.getCoordinate().getLongitude());
        pstmtSelectMatches.setDouble(24, destination.getCoordinate().getLatititude());
        pstmtSelectMatches.setDouble(25, d);
        pstmtSelectMatches.setDouble(26, destination.getCoordinate().getLongitude());
        pstmtSelectMatches.setDouble(27, destination.getCoordinate().getLatititude());
        pstmtSelectMatches.setDouble(28, d);
        pstmtSelectMatches.setDouble(29, d);
        pstmtSelectMatches.setDouble(30, d);
        pstmtSelectMatches.setDouble(31, d);
        pstmtSelectMatches.setDouble(32, d);
        pstmtSelectMatches.setDouble(33, d);
        pstmtSelectMatches.setDouble(34, d);
        ResultSet result = pstmtSelectMatches.executeQuery();

        //fetch required data from ResultSet
        LinkedList<PotentialMatch> matches = new LinkedList<PotentialMatch>();
        while(result.next()){
            int ridersRouteId = result.getInt("riderroute_id");
            int liftIndex = result.getInt("lift_idx");
            int dropIndex = result.getInt("drop_idx");
            Point liftPoint = new Point(result.getString("lift_point"));
            Point dropPoint = new Point(result.getString("drop_point"));
            Point onRouteLiftPoint = new Point(result.getString("on_route_lift_point"));
            Point onRouteDropPoint = new Point(result.getString("on_route_drop_point"));
            Timestamp timeAtLiftPoint = result.getTimestamp("time_at_lift_point");
            Timestamp timeAtOnRouteLiftPoint = result.getTimestamp("time_at_on_route_lift_point");
            double sharedDistance = result.getDouble("shared_distance");

            PotentialMatch m = new PotentialMatch
            (
                ridersRouteId,
                rideId,
                liftIndex,
                dropIndex,
                new Coordinate(liftPoint.getY(),liftPoint.getX()),
                new Coordinate(dropPoint.getY(),dropPoint.getX()),
                new Coordinate(onRouteLiftPoint.getY(),onRouteLiftPoint.getX()),
                new Coordinate(onRouteDropPoint.getY(),onRouteDropPoint.getX()),
                timeAtOnRouteLiftPoint,
                timeAtLiftPoint,
                sharedDistance
            );
            matches.add(m);
        }


        //free resources and end transaction
        pstmtDropTempPasspointTable.executeUpdate();
        result.close();
        conn.commit();
        
        // don't release Autocommit status!
        // conn.setAutoCommit(true);

        return matches;
    }

//    public static void main(String[] args) throws ClassNotFoundException, SQLException, MatchingException {
//
//        Class.forName("org.postgresql.Driver");
//
//        String url = "jdbc:postgresql://localhost:5432/openride";
//        Connection conn = DriverManager.getConnection(url, "postgres", "admin");
//        RiderSearchAlgorithm a = new RiderSearchAlgorithm(conn);
//
//        url = "jdbc:postgresql://localhost:5432/routing_osm";
//        Connection conn2 = DriverManager.getConnection(url, "postgres", "admin");
//        OsmRouter router = new OsmRouter(conn2);
//
//        Coordinate[] nRoutePoints = new Coordinate[]{
//            new Coordinate(52.509769, 13.4567655),
//            new Coordinate(52.5257594, 13.4567655),
//            new Coordinate(52.5257594, 13.3135989)
//        };
//        Timestamp startTime = new Timestamp(2010 - 1900, 02, 16, 10, 13, 14, 0);
//        RoutePoint[] decomposedRoute = router.getEquiDistantRoutePoints(nRoutePoints, startTime, true, 2000d, 600d);
//        LinkedList<PotentialMatch> matches = a.findRiders(0, decomposedRoute, 2000d);
//        System.out.println("matche : " + matches.size() + " start: " + startTime);
//        int i = 0;
//        for(PotentialMatch m : matches) {
//            System.out.println((i++) + " " + m);
//        }
//    }

}
/****************************************************************
 * THE MATCHING QUERY WITHOUT '?' OF PREPARED STATEMENTS        *
 *   keep this for making changes to the query                  *
 ****************************************************************/
//           Coordinate center = Coordinate.interpolate(
//                source.getCoordinate(),
//                destination.getCoordinate(),
//                0.5d
//        );
//        double radius = center.distanceSphere(source.getCoordinate());
//
//        String sql = "SELECT "
//            + "s.riderroute_id, "
//            + "l.route_idx as lift_idx, "
//            + "d.route_idx as drop_idx, "
//            + "st_distance(l.coordinate, s.startpt) as lift_detour, "
//            + "st_distance(d.coordinate, s.endpt) as drop_detour, "
//            + "d.time_at as time_at_on_route_drop_point, "
//            + "l.time_at as time_at_on_route_lift_point, "
//            + "s.starttime_earliest as time_at_lift_point, "
//            + "st_AsText(st_transform(s.startpt, " + SRID_POLAR + ")) as lift_point, "
//            + "st_AsText(st_transform(s.endpt, " + SRID_POLAR + ")) as drop_point, "
//            + "st_AsText(st_transform(l.coordinate, " + SRID_POLAR + ")) as on_route_lift_point, "
//            + "st_AsText(st_transform(d.coordinate, " + SRID_POLAR + ")) as on_route_drop_point, "
//            + "(d.distance_to_source - l.distance_to_source) AS shared_distance "
//        + "FROM  "
//            + "rider_search s, "
//            + passPointTempTableName + " l,  "
//            + passPointTempTableName + " d "
//        + "WHERE  "
//                + "( "
//                     /* big circle around center of source and destination */
//                     + "( "
//                        + "s.startpt && st_expand "
//                        + "( "
//                            + "st_transform(st_setsrid(st_makepoint(" + center.getLongitude() + ", " + center.getLatititude() + "), " + SRID_POLAR + "), "+SRID_CARTHESIAN + "),  "
//                            + radius + " "
//                        + ") "
//                        + "AND st_distance "
//                        + "( "
//                            + "st_transform(st_setsrid(st_makepoint(" + center.getLongitude() + ", " + center.getLatititude() + "), " + SRID_POLAR + "), "+SRID_CARTHESIAN + "),  "
//                            + "s.startpt "
//                        + ") <= " + radius + " "
//                    + ") "
//                    + "OR "
//                    /* small circle arround source */
//                    + "( "
//                        + "s.startpt && st_expand "
//                        + "( "
//                            + "st_transform(st_setsrid(st_makepoint(" + source.getCoordinate().getLongitude() + ", " + source.getCoordinate().getLatititude() + "), " + SRID_POLAR + "), "+SRID_CARTHESIAN + "),  "
//                            + d + " "
//                        + ") "
//                         + "AND st_distance "
//                        + "( "
//                            + "st_transform(st_setsrid(st_makepoint(" + source.getCoordinate().getLongitude() + ", " + source.getCoordinate().getLatititude() + "), " + SRID_POLAR + "), "+SRID_CARTHESIAN + "),  "
//                            + "s.startpt "
//                        + ") <= " + d + " "
//                    + ") "
//                + ") "
//                /* time between start time and expected finish time */
//                + "AND s.starttime_latest >= '" + source.getTimeAt().toString() + "'  "
//                + "AND s.starttime_earliest <= '" + destination.getTimeAt().toString() + "'  "
//                + "AND "
//                + "( "
//                    /* big circle through center of liftpoint and route destination overlay with drop points */
//                    + "( "
//                        + "s.endpt && st_expand"
//                        + "( "
//                            + "st_Centroid(st_makeline(s.startpt, st_transform(st_setsrid(st_makepoint(" + destination.getCoordinate().getLongitude() + ", " + destination.getCoordinate().getLatititude() + "), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "))),   "
//                            + "st_distance(s.startpt, st_transform(st_setsrid(st_makepoint(" + destination.getCoordinate().getLongitude() + ", " + destination.getCoordinate().getLatititude() + "), " + SRID_POLAR + "), " + SRID_CARTHESIAN + ")) / 2  "
//                        + ")  "
//                        + "AND st_distance "
//                        + "( "
//                            + "s.endpt,  "
//                            + "st_Centroid(st_makeline(s.startpt, st_transform(st_setsrid(st_makepoint(" + destination.getCoordinate().getLongitude() + ", " + destination.getCoordinate().getLatititude() + "), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "))) "
//                        + ") <= st_distance(s.startpt, st_transform(st_setsrid(st_makepoint(" + destination.getCoordinate().getLongitude() + ", " + destination.getCoordinate().getLatititude() + "), " + SRID_POLAR + "), " + SRID_CARTHESIAN + ")) / 2  "
//                    + ") "
//                    + "OR "
//                    /* small circle around route destination */
//                    + "( "
//                        + "s.endpt && st_expand "
//                        + "( "
//                            + "st_transform(st_setsrid(st_makepoint(" + destination.getCoordinate().getLongitude() + ", " + destination.getCoordinate().getLatititude() + "), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "), "
//                            + d + " "
//                        + ") "
//                         + "AND st_distance "
//                        + "( "
//                            + "st_transform(st_setsrid(st_makepoint(" + destination.getCoordinate().getLongitude() + ", " + destination.getCoordinate().getLatititude() + "), " + SRID_POLAR + "),  "  + SRID_CARTHESIAN + "), "
//                            + "s.endpt "
//                        + ") <= " + d + " "
//                    + ") "
//                + ") "
//                /* choose on route lift point from all passpoints having minimal distance to s.lift_point */
//                + "AND l.coordinate && st_expand(s.startpt, " + d + ") "
//                + "AND st_distance(l.coordinate, s.startpt) <= " + d + " "
//                + "AND d.coordinate && st_expand(s.endpt, " + d + ") "
//                + "AND st_distance(d.coordinate, s.endpt) <= " + d + " "
//                + "AND st_distance(l.coordinate, s.startpt) = "
//                + "( "
//                + "SELECT   "
//                    + "MIN(st_distance(s.startpt, pp.coordinate))  "
//                + "FROM  "
//                    + passPointTempTableName + " pp  "
//                + "WHERE  "
//                    + "pp.coordinate && st_expand(s.startpt, " + d + ")  "
//                    //CHANGE
//                    + "AND pp.time_at <= s.starttime_latest "
//                    + "AND pp.time_at >= s.starttime_earliest "
//                    //END CHANGE TODO: check if time check is correct!
//                + ") "
//                /* choose on route drop point from all passpoints (l.drop_point) having minimal distance to s.drop_point */
//                + "AND st_distance(d.coordinate, s.endpt) =   "
//                + "(  "
//                    + "SELECT   "
//                        + "MIN(st_distance(s.endpt, pp.coordinate))  "
//                    + "FROM  "
//                        + passPointTempTableName + " pp  "
//                    + "WHERE  "
//                        + "pp.coordinate && st_expand(s.endpt, " + d + ")  "
//                + ") "
//                + "ORDER BY l.route_idx - d.route_idx; ";
//
//        Statement stmt = openRideDbConnection.createStatement();
//        ResultSet result = stmt.executeQuery(sql);


