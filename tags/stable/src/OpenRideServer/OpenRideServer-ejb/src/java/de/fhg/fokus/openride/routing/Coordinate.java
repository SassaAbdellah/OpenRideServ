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

import org.postgis.Point;

public class Coordinate {

    private static final double FAC_DOUBLE_TO_INT = 1E7;
    private static final double FAC_INT_TO_DOUBLE = 1 / 1E7;

    public final static double METERS_PER_DEGREE_OF_LAT= (2.0d * Math.PI * Coordinate.EARTH_RADIUS) / 360.0d;

    /* metric is meters */
    public final static double EARTH_RADIUS = 6371E3;

    /* metric is degree */
    private final double latititude, longitude;


    /**
     * @param latititude degree of lat
     * @param longitude degree of lon
     */
    public Coordinate(double latititude, double longitude) {
        this.latititude = latititude;
        this.longitude = longitude;
    }

    public Coordinate(Point p) {
        this.longitude = p.x;
        this.latititude = p.y;
    }

    /**
     * Latitude in degree
     * @return
     */
    public double getLatititude() {
        return latititude;
    }

    public int getLatitudeInt() {
        return dtoi(latititude);
    }

    public int getLongitudeInt() {
        return dtoi(longitude);
    }

    /**
     * Longitude in degree.
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Distance in meters.
     * @param other
     * @return
     */
    public double distanceSphere(Coordinate other){
            double dLat = Math.toRadians(other.latititude - latititude);
        double dLng = Math.toRadians(other.longitude - longitude);
        double a = Math.sin(dLat / 2)
            * Math.sin(dLat / 2)
            + Math.cos(Math.toRadians(latititude))
            * Math.cos(Math.toRadians(other.latititude))
            * Math.sin(dLng / 2)
            * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return EARTH_RADIUS * c;
    }

    public static Point coordinateToPoint(Coordinate c) {
        return new Point(c.longitude, c.latititude);
    }

    public static Coordinate pointToCoordinate(Point p) {
        return new Coordinate(p.y, p.x);
    }

    public static double distanceSphere(double lon1, double lat1, double lon2, double lat2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2)
            * Math.sin(dLat / 2)
            + Math.cos(Math.toRadians(lat1))
            * Math.cos(Math.toRadians(lat2))
            * Math.sin(dLng / 2)
            * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return EARTH_RADIUS * c;
    }

    @Override
    public String toString(){
        return "(" + longitude + ", " + latititude + ")";
    }

    /**
     * translate 1 degree of longitude to meters for given latitude.
     * Its based on a sphere so it is not the exact result for our earth.
     * @param latitude
     * @return
     */
    public static double getMetersPerDegreeOfLongitude(double latitude){
        double radiusLon = Math.cos(Math.toRadians(latitude)) * EARTH_RADIUS;
        return (2.0d * Math.PI * radiusLon) / 360.0d;
    }

    public static Coordinate interpolate(Coordinate a, Coordinate b, double delta){
        return
            new Coordinate(
                (delta * a.getLatititude()) + ((1 - delta) * b.getLatititude()),
                (delta * a.getLongitude()) + ((1 - delta) * b.getLongitude())
            );
    }

    public Point toPoint() {
        return new Point(longitude, latititude);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Coordinate)) {
            return false;
        }
        Coordinate c = (Coordinate) obj;
        return c.latititude == latititude && c.longitude == longitude;
    }

    public static int dtoi(double d) {
        return (int) Math.rint(d * FAC_DOUBLE_TO_INT);
    }

    public static double itod(int i) {
        return FAC_INT_TO_DOUBLE * i;
    }
}
