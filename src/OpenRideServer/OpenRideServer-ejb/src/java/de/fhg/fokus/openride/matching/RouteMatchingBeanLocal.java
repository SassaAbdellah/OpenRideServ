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

import de.fhg.fokus.openride.rides.driver.DriveRoutepointEntity;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideEntity;
import de.fhg.fokus.openride.rides.driver.RoutePointEntity;
import java.util.LinkedList;
import javax.ejb.Local;

/**
 *
 * @author pab
 */
@Local
public interface RouteMatchingBeanLocal {

     /**
     * Computes a set of Matches for the given drive offer.
     * @param driveId Identifier for a driver's offer.
     * @return list of matches sorted descending by score.
     */
    public LinkedList<MatchEntity> searchForRiders(int driveId);

    /**
     * Compute a list of all matches for the given ride offer.
     * @param rideId identifiers for rider's offer
     * @return All matches sorted descending by score.
     */
    public LinkedList<MatchEntity> searchForDrivers(int rideId);

    /**
     * Computes a route for a driver which has no associated ride offers.
     *
     * @param drive driver's offer.
     * @param decomposedRouteBuff route points suitable for the matching algorithm.
     * @param routeBuff route points suitable for distplaying the route (all map coordinates included).
     * @return length of the route in meters.
     */
    public double computeInitialRoutes(DriverUndertakesRideEntity drive, LinkedList<DriveRoutepointEntity> decomposedRouteBuff, LinkedList<RoutePointEntity> routeBuff);

    /**
     * Computes the route, the driver should drive if he'd book
     * the given rider offer. Pre: The given rider offer must not be associated with the driver offer!
     * @param rideId identifier of driver's offer.
     * @param riderrouteId identifier of rider's offer.
     * @param decomposedRouteBuff buffer to put the interpolated route points.
     * @param routeBuff  buffer to put the complete path description, containing all map coordinates.
     * @return shared distance in meters.
     */
    public double computeAdaptedRoute(int rideId, int riderrouteId, LinkedList<DriveRoutepointEntity> decomposedRouteBuff, LinkedList<RoutePointEntity> routeBuff);

}

