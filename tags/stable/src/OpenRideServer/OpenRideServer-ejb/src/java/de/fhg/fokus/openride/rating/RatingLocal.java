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

package de.fhg.fokus.openride.rating;

import de.fhg.fokus.openride.rating.helperclasses.OpenRatingInfo;
import de.fhg.fokus.openride.rating.helperclasses.Rating;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author pab
 */
@Local
public interface RatingLocal {

    //rate driver or rider
    void rateRider(int riderRouteId, Integer rating, String ratingComment);
    void rateDriver(int riderRouteId, Integer rating, String ratingComment);

    // get a rating for a particular ride for the driver
    // TOFIX: a driver can have many ratings, because there are perhaps several riders!
    Rating getRatingForDriver(int riderRouteId);

    // get a received rating for a particular ride for the rider
    Rating getRatingForRider(int riderRouteId);

    //get all received ratings of a customer in the role of a driver or a rider
    List<Rating> getRatingsAsDriver(int cust_id);
    List<Rating> getRatingsAsRider(int cust_id);

    // get all open ratings of a customer as driver or as rider
    List<OpenRatingInfo> getOpenRatingsAsDriver(int cust_id);
    List<OpenRatingInfo> getOpenRatingsAsRider(int cust_id);
}
