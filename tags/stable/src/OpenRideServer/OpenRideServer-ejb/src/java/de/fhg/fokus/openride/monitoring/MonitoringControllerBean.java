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

import de.fhg.fokus.openride.customerprofile.RegistrationPassControllerLocal;
import de.fhg.fokus.openride.helperclasses.ControllerBean;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

/**
 *
 * @author akr
 */
@Stateless
public class MonitoringControllerBean extends ControllerBean implements MonitoringControllerBeanLocal {

    @PersistenceContext
    private EntityManager em;
    @EJB
    private RegistrationPassControllerLocal registrationPassControllerBean;
    private UserTransaction u;

    /*
     * Returns total count of all stored drive offers in the database.
     */
    @Override
    public Long getTotalDrivesCount(Date date) {
        return (Long) em.createNamedQuery("DriverUndertakesRideEntity.countTotalNoDrivesAfterDate").setParameter("date", date).getSingleResult();
    }

    /*
     * Returns total count of all stored drive offers in the database which starts between startdate and enddate.
     */
    @Override
    public Long getDrivesCountBetweenDates(Date startDate, Date endDate) {
        Query query = em.createNamedQuery("DriverUndertakesRideEntity.countTotalNoDrivesBetweenDates");
        query.setParameter("startdate", startDate);
        query.setParameter("enddate", endDate);
        return (Long) query.getSingleResult();
    }

    /*
     * Returns total count of all stored ride searches in the database.
     */
    @Override
    public Long getTotalRideSearchCount(Date date) {
        return (Long) em.createNamedQuery("RiderUndertakesRideEntity.countTotalNoRideSearchesAfterDate").setParameter("date", date).getSingleResult();
    }

    /*
     * Returns total count of all stored drive offers in the database between startdate and enddate.
     */
    @Override
    public Long getRideSearchCountBetweenDates(Date startDate, Date endDate) {
        Query query = em.createNamedQuery("RiderUndertakesRideEntity.countTotalNoRideSearchesBetweenDates");
        query.setParameter("startdate", startDate);
        query.setParameter("enddate", endDate);
        return (Long) query.getSingleResult();
    }

    /*
     * Returns total count of all bookings in the database.
     */
    @Override
    public Long getTotalBookingsCount(Date date) {
        return (Long) em.createNamedQuery("RiderUndertakesRideEntity.countTotalNoBookingsAfterDate").setParameter("startDate", date).getSingleResult();
    }

    /*
     * Returns count of all bookings in the database between startdate and enddate.
     */
    @Override
    public Long getBookingsCountBetweenDates(Date startDate, Date endDate) {
        Query query = em.createNamedQuery("RiderUndertakesRideEntity.countTotalNoBookingsBetweenDates");
        query.setParameter("startdate", startDate);
        query.setParameter("enddate", endDate);
        return (Long) query.getSingleResult();
    }

    /*
     * Returns count of all drives in the database that have a length less or equal to
     * the parameter length.
     */
    @Override
    public long getCountForDriveLenghtLessThenOrEqual(double length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
     * Returns count of all ride searches in the database that have a length less or equal to
     * the parameter length.
     */
    @Override
    public long getCountForRideSearchLenghtLessThenOrEqual(double length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Returns count of all customers registrated at the given date.
     *
     * @param date Date to search customers.
     * @return Count of all customers registrated at the given date.
     */
    @Override
    public Integer getCustomerRegistratedAtDate(Date date) {
        return (Integer) em.createNamedQuery("CustomerEntity.findByCustRegistrdate").setParameter("custRegistrdate", date).getResultList().size();
    }

    /**
     * Returns count of all customers registrated after the given date.
     * @return Count of all customers.
     */
    @Override
    public Long getCountForTotalCustomers(Date date) {
        return (Long) em.createNamedQuery("RegistrationPassEntity.countAfterDate").
                setParameter("date", date).
                getSingleResult();
    }

    @Override
    public Integer getRegistratedUsersCountByDate(Date date) {
        return registrationPassControllerBean.getRegistrationPassByUsageDate(date).size();
    }

    @Override
    public Long getTotalMatchesCountAfterDate(Date date) {
        return (Long) em.createNamedQuery("MatchEntity.countTotalNoMatchesAfterDate").
                setParameter("date", date).
                getSingleResult();
    }

    @Override
    public Long getMatchesCountBetweenDates(Date startDate, Date endDate) {
        return (Long) em.createNamedQuery("MatchEntity.countTotalNoMatchesBetweenDates").
                setParameter("startDate", startDate).
                setParameter("endDate", endDate).
                getSingleResult();
    }
}
