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
import java.sql.PreparedStatement;

/**
 * This class implements the rider side of the circle overlay algorithm, that is
 * to say it computes matching drivers for a given rider. As basis the postgis
 * database in particular its gist index ist used.
 *
 * This part of the algorithm is the more simple. As parameters, riders' start and endpoint
 * as well as a circle radius must be given. Additionally, the time the rider wants' to start
 * his ride must be specified. Each route crossing the two circles around start -
 * and end point yields a match, if and only if the time constraints are met.
 *
 * @author fvi
 */
final class DriverSearchAlgorithm
{
    /* spatial reference systems for postgis geometries */
    public static final int SRID_CARTHESIAN = RiderSearchAlgorithm.SRID_CARTHESIAN;
    public static final int SRID_POLAR = RiderSearchAlgorithm.SRID_POLAR;

    public static final double DEFAULT_D = 2000.d;

    /* only for adding comments in pstmt */
    private static int debug_i = 1;
    /* matching query */
    private static final String sqlSelectMatches =
            "SELECT "
             + "l.drive_id,  "
             + "l.route_idx AS lift_idx, "
             + "d.route_idx AS drop_idx, "
             + "st_AsText(st_transform(l.coordinate_c, 4326)) AS on_route_lift_point, "
             + "st_AsText(st_transform(d.coordinate_c, 4326)) AS on_route_drop_point, "
             + "l.expected_arrival AS time_at_on_route_lift_point, "
             + "d.distance_to_source - l.distance_to_source AS shared_distance "
         + "FROM  "
             + "drive_route_point l, "
             + "drive_route_point d, "
             + "( "
             + "SELECT  "
                 + "drive_id,  "
                 + "min(st_distance(l.coordinate_c, st_transform(st_setsrid(st_makepoint(? /* [" + (debug_i++) + "] rideStartPt.x */, ? /* [" + (debug_i++) + "] rideStartPt.y */), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "))) AS  lift_point_distance "
             + "FROM  "
                 + "drive_route_point l "
             + "WHERE  "
                 + "l.coordinate_c && st_expand "
                 + "(  "
                     + "st_transform(st_setsrid(st_makepoint(? /* [" + (debug_i++) + "] rideStartPt.x */, ? /* [" + (debug_i++) + "] rideStartPt.y */), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "),  "
                     + "? /* [" + (debug_i++) + "] d */  "
                 + ")  "
                 + "AND st_distance "
                 + "(  "
                     + "st_transform(st_setsrid(st_makepoint(? /*  [" + (debug_i++) + "]rideStartPt.x */, ? /* [" + (debug_i++) + "] rideStartPt.y */), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "),  "
                     + "l.coordinate_c  "
                 + ") <= ? /* [" + (debug_i++) + "] d */ "
                 + "AND l.expected_arrival >= ? /* [" + (debug_i++) + "] startTimeEarliest */ "
                 + "AND l.expected_arrival <= ? /* [" + (debug_i++) + "] startTimeLatest.toString() */ "
                 + "GROUP BY drive_id "
             + ") AS x, "
             + "( "
                 + "SELECT  "
                     + "drive_id,  "
                     + "min(st_distance(d.coordinate_c, st_transform(st_setsrid(st_makepoint(? /* [" + (debug_i++) + "] rideEndPt.x */, ? /* [" + (debug_i++) + "] rideEndPt.y */ ), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "))) as drop_point_distance "
                 + "FROM   "
                     + "drive_route_point d "
                 + "WHERE  "
                     + "d.coordinate_c && st_expand "
                     + "( "
                         + "st_transform(st_setsrid(st_makepoint(? /* [" + (debug_i++) + "] rideEndPt.x */, ? /* [" + (debug_i++) + "] rideEndPt.y */ ), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "),  "
                         + "? /* [" + (debug_i++) + "] d */ "
                     + ")  "
                     + "AND st_distance "
                     + "(  "
                         + "	st_transform(st_setsrid(st_makepoint(? /* [" + (debug_i++) + "] rideEndPt.x */, ? /* [" + (debug_i++) + "] rideEndPt.y */ ), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "),  "
                         + "	d.coordinate_c  "
                     + ")  <= ? /* [" + (debug_i++) + "] d */ "
                 + "GROUP BY  "
                     + "drive_id "
             + ") AS y "
         + "WHERE  "
         + "l.drive_id = x.drive_id "
         + "and l.drive_id = y.drive_id "
         + "and l.drive_id = d.drive_id  "
         + "and l.expected_arrival >= ? /* [" + (debug_i++) + "] startTimeEarliest */ "
         + "and l.expected_arrival <= ? /* [" + (debug_i++) + "] startTimeLatest */ "
         + "and d.expected_arrival >= ? /* [" + (debug_i++) + "] startTimeEarliest */ "
         + "and d.route_idx > l.route_idx "
         + "and st_distance(l.coordinate_c, st_transform(st_setsrid(st_makepoint(? /* [" + (debug_i++) + "] rideStartPt.x */, ? /* [" + (debug_i++) + "] rideStartPt.y */), " + SRID_POLAR + "), " + SRID_CARTHESIAN + ")) = x.lift_point_distance "
         + "and st_distance(d.coordinate_c, st_transform(st_setsrid(st_makepoint(? /* [" + (debug_i++) + "] rideEndPt.x */, ? /* [" + (debug_i++) + "] rideEndPt.y */ ), " + SRID_POLAR + "), " + SRID_CARTHESIAN + ")) = y.drop_point_distance "
         + "ORDER BY shared_distance DESC;";

    private final PreparedStatement pstmtSelectMatches;
    private final Connection conn;

    /**
     * @param openRideDbConnection connection to openride database.
     */
    public DriverSearchAlgorithm(Connection conn) throws SQLException {
        this.conn = conn;
        this.pstmtSelectMatches = conn.prepareStatement(sqlSelectMatches);
    }

    /**
     * Comppute all matches for a given ride based on geographical position and time.
     * @param riderrouteId identifier of riders' offer.
     * @param startPt lon/lat coordinate of riders' start point.
     * @param endPt lon/lat coordinate of riders' end point.
     * @param startTimeEarliest earliest possible time to  pick up the rider.
     * @param startTimeLatest latest possible time to pick up the rider.
     * @param d radius of the two circles around riders' start and end point in meters.
     * @return List of matches with regard to geographical position and time constraints.
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    public LinkedList<PotentialMatch> findDriver(int riderrouteId, Point startPt,
            Point endPt, Timestamp startTimeEarliest,
            Timestamp startTimeLatest, double d) throws SQLException, IllegalArgumentException {

        //verirfy parameters
        if(startPt == null || endPt == null || startTimeEarliest == null
                || startTimeLatest == null || d < 0
                || startTimeEarliest.compareTo(startTimeLatest) > 0) {
            throw new IllegalArgumentException();
        }
       
         //initialize prepared statement :
        pstmtSelectMatches.setDouble(1, startPt.x);
        pstmtSelectMatches.setDouble(2, startPt.y);
        pstmtSelectMatches.setDouble(3, startPt.x);
        pstmtSelectMatches.setDouble(4, startPt.y);
        pstmtSelectMatches.setDouble(5, d);
        pstmtSelectMatches.setDouble(6, startPt.x);
        pstmtSelectMatches.setDouble(7, startPt.y);
        pstmtSelectMatches.setDouble(8, d);
        pstmtSelectMatches.setTimestamp(9, startTimeEarliest);
        pstmtSelectMatches.setTimestamp(10, startTimeLatest);
        pstmtSelectMatches.setDouble(11, endPt.x);
        pstmtSelectMatches.setDouble(12, endPt.y);
        pstmtSelectMatches.setDouble(13, endPt.x);
        pstmtSelectMatches.setDouble(14, endPt.y);
        pstmtSelectMatches.setDouble(15, d);
        pstmtSelectMatches.setDouble(16, endPt.x);
        pstmtSelectMatches.setDouble(17, endPt.y);
        pstmtSelectMatches.setDouble(18, d);
        pstmtSelectMatches.setTimestamp(19, startTimeEarliest);
        pstmtSelectMatches.setTimestamp(20, startTimeLatest);
        pstmtSelectMatches.setTimestamp(21, startTimeEarliest);
        pstmtSelectMatches.setDouble(22, startPt.x);
        pstmtSelectMatches.setDouble(23, startPt.y);
        pstmtSelectMatches.setDouble(24, endPt.x);
        pstmtSelectMatches.setDouble(25, endPt.y);

        //get result
        ResultSet rs = pstmtSelectMatches.executeQuery();

        //fetch potential matches from Result set
        LinkedList<PotentialMatch> matches = new LinkedList<PotentialMatch>();
        int driveId, liftIndex, dropindex;
        Point onRouteLiftPoint, onRouteDropPoint;
        Timestamp timeAtOnRouteLiftPoint;
        double sharedDistance;

        while(rs.next()){
            driveId = rs.getInt("drive_id");
            liftIndex = rs.getInt("lift_idx");
            dropindex = rs.getInt("drop_idx");
            onRouteLiftPoint = new Point(rs.getString("on_route_lift_point"));
            onRouteDropPoint = new Point(rs.getString("on_route_drop_point"));
            timeAtOnRouteLiftPoint = rs.getTimestamp("time_at_on_route_lift_point");
            sharedDistance = rs.getDouble("shared_distance");

            PotentialMatch match = new PotentialMatch
            (
                riderrouteId,
                driveId,
                liftIndex,
                dropindex,
                new Coordinate(startPt.y, startPt.x),
                new Coordinate(endPt.y, endPt.x),
                new Coordinate(onRouteLiftPoint.getY(), onRouteLiftPoint.getX()),
                new Coordinate(onRouteDropPoint.getY(), onRouteLiftPoint.getX()),
                timeAtOnRouteLiftPoint,
                startTimeEarliest,
                sharedDistance
            );
            matches.add(match);
        }
        rs.close();
        return matches;
    }

//    private static String getSQLStringSelectMatches(Point rideStartPt, Point rideEndPt, Timestamp startTimeEarliest, Timestamp startTimeLatest, double d) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT ");
//            sb.append("l.drive_id,  ");
//            sb.append("l.route_idx AS lift_idx, ");
//            sb.append("d.route_idx AS drop_idx, ");
//            sb.append("st_AsText(st_transform(l.coordinate_c, 4326)) AS on_route_lift_point, ");
//            sb.append("st_AsText(st_transform(d.coordinate_c, 4326)) AS on_route_drop_point, ");
//            sb.append("l.expected_arrival AS time_at_on_route_lift_point, ");
//            sb.append("d.distance_to_source - l.distance_to_source AS shared_distance ");
//        sb.append("FROM  ");
//            sb.append("drive_route_point l, ");
//            sb.append("drive_route_point d, ");
//            sb.append("( ");
//            sb.append("SELECT  ");
//                sb.append("drive_id,  ");
//                sb.append("min(st_distance(l.coordinate_c, st_transform(st_setsrid(st_makepoint(" + rideStartPt.x + ", " + rideStartPt.y + "), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "))) AS  lift_point_distance ");
//            sb.append("FROM  ");
//                sb.append("drive_route_point l ");
//            sb.append("WHERE  ");
//                sb.append("l.coordinate_c && st_expand ");
//                sb.append("(  ");
//                    sb.append("st_transform(st_setsrid(st_makepoint(" + rideStartPt.x + ", " + rideStartPt.y + "), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "),  ");
//                    sb.append(" " + d + "  ");
//                sb.append(")  ");
//                sb.append("AND st_distance ");
//                sb.append("(  ");
//                    sb.append("st_transform(st_setsrid(st_makepoint(" + rideStartPt.x + ", " + rideStartPt.y + "), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "),  ");
//                    sb.append("l.coordinate_c  ");
//                sb.append(") <= " + d + "  ");
//                sb.append("AND l.expected_arrival >= '" + startTimeEarliest.toString() + "'  ");
//                sb.append("AND l.expected_arrival <= '" + startTimeLatest.toString() + "'  ");
//                sb.append("GROUP BY drive_id ");
//            sb.append(") AS x, ");
//            sb.append("( ");
//                sb.append("SELECT  ");
//                    sb.append("drive_id,  ");
//                    sb.append("min(st_distance(d.coordinate_c, st_transform(st_setsrid(st_makepoint(" + rideEndPt.x + ", " + rideEndPt.y + "), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "))) as drop_point_distance ");
//                sb.append("FROM   ");
//                    sb.append("drive_route_point d ");
//                sb.append("WHERE  ");
//                    sb.append("d.coordinate_c && st_expand ");
//                    sb.append("(  ");
//                        sb.append("	st_transform(st_setsrid(st_makepoint(" + rideEndPt.x + ", " + rideEndPt.y + "), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "),  ");
//                        sb.append("	" + d + "  ");
//                    sb.append(")  ");
//                    sb.append("AND st_distance ");
//                    sb.append("(  ");
//                        sb.append("	st_transform(st_setsrid(st_makepoint(" + rideEndPt.x + ", " + rideEndPt.y + "), " + SRID_POLAR + "), " + SRID_CARTHESIAN + "),  ");
//                        sb.append("	d.coordinate_c  ");
//                    sb.append(")  <= " + d + "  ");
//                sb.append("GROUP BY  ");
//                    sb.append("drive_id ");
//            sb.append(") AS y ");
//        sb.append("WHERE  ");
//        sb.append("l.drive_id = x.drive_id ");
//        sb.append("and l.drive_id = y.drive_id ");
//        sb.append("and l.drive_id = d.drive_id  ");
//        sb.append("and l.expected_arrival >= '" + startTimeEarliest.toString() + "' ");
//        sb.append("and l.expected_arrival <= '" + startTimeLatest.toString() + "' ");
//        sb.append("and d.expected_arrival >= '" + startTimeEarliest.toString() + "' ");
//        sb.append("and d.route_idx > l.route_idx ");
//        sb.append("and st_distance(l.coordinate_c, st_transform(st_setsrid(st_makepoint(" + rideStartPt.x + ", " + rideStartPt.y + "), " + SRID_POLAR + "), " + SRID_CARTHESIAN + ")) = x.lift_point_distance ");
//        sb.append("and st_distance(d.coordinate_c, st_transform(st_setsrid(st_makepoint(" + rideEndPt.x + ", " + rideEndPt.y + "), " + SRID_POLAR + "), " + SRID_CARTHESIAN + ")) = y.drop_point_distance ");
//        sb.append("ORDER BY shared_distance DESC;");
//        return sb.toString();
//    }
//
//    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        Point startPt = new Point(13.6898529846591, 52.4432798849768);
//        Point endPt = new Point(13.2902495491963, 52.5755355915678);
//        Timestamp startTimeEarliest = new Timestamp(2010 - 1900, 0, 1, 12, 46, 25, 143);
//        Timestamp startTimeLatest = new Timestamp(2011 - 1900, 0, 2, 0, 0, 0, 0);
//        double d = 2000.0d;
//
//        Class.forName("org.postgresql.Driver");
//
//        String url = "jdbc:postgresql://localhost:5432/openride";
//        Connection conn = DriverManager.getConnection(url, "postgres", "admin");
//        PreparedStatement pstmt = conn.prepareStatement(sqlSelectMatches);
//
//
//        DriverSearchAlgorithm a = new DriverSearchAlgorithm(conn);
//        LinkedList<PotentialMatch> m = a.findDriver(0, startPt, endPt, startTimeEarliest, startTimeLatest, d);
//        System.out.println(m.get(0));
//    }
}


