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

package de.fhg.fokus.openride.monitoring;

import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author akr
 */
@Local
public interface MonitoringControllerBeanLocal {

    Long getTotalDrivesCount(Date date);
    Long getDrivesCountBetweenDates(Date startDate, Date endDate);

    Long getTotalRideSearchCount(Date date);
    Long getRideSearchCountBetweenDates(Date startDate, Date endDate);

    Long getTotalBookingsCount(Date date);
    Long getBookingsCountBetweenDates(Date startDate, Date endDate);

    //TODO the next two methods
    long getCountForDriveLenghtLessThenOrEqual(double length);
    long getCountForRideSearchLenghtLessThenOrEqual(double length);

    Integer getCustomerRegistratedAtDate(Date date);
    Long getCountForTotalCustomers(Date date);
    Integer getRegistratedUsersCountByDate(Date date);
    Long getTotalMatchesCountAfterDate(Date date);
    Long getMatchesCountBetweenDates(Date startDate, Date endDate);
}
