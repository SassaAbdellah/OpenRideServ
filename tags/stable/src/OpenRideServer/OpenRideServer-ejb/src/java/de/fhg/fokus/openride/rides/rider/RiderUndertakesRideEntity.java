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
package de.fhg.fokus.openride.rides.rider;

import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.helperclasses.converter.PointConverter;
import de.fhg.fokus.openride.rides.driver.*;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.postgis.Point;

/**
 *
 * @author pab
 */
@Entity
@Table(name = "riderundertakesride")
@NamedQueries({
    @NamedQuery(name = "RiderUndertakesRideEntity.findAll", query = "SELECT r FROM RiderUndertakesRideEntity r"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findByCustId", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.custId = :custId"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findByRideId", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.rideId = :rideId"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findByRiderroute_id", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.riderrouteId = :riderrouteId"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findByStarttimeEarliest", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.starttimeEarliest = :starttimeEarliest"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findByTimestampbooked", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.timestampbooked = :timestampbooked"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findByTimestamprealized", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.timestamprealized = :timestamprealized"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findByReceivedrating", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.receivedrating = :receivedrating"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findByReceivedratingComment", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.receivedratingComment = :receivedratingComment"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findByReceivedratingDate", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.receivedratingDate = :receivedratingDate"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findByGivenrating", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.givenrating = :givenrating"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findByGivenratingComment", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.givenratingComment = :givenratingComment"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findByGivenratingDate", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.givenratingDate = :givenratingDate"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findByPrice", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.price = :price"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findByNoPassengers", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.noPassengers = :noPassengers"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findByRiderrouteId", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.riderrouteId = :riderrouteId"),
    @NamedQuery(name = "RiderUndertakesRideEntity.countTotalNoRideSearches", query = "SELECT COUNT(r.riderrouteId) FROM RiderUndertakesRideEntity r"),
    @NamedQuery(name = "RiderUndertakesRideEntity.countTotalNoRideSearchesAfterDate", query = "SELECT COUNT(r.riderrouteId) FROM RiderUndertakesRideEntity r WHERE r.starttimeEarliest >= :date"),
    @NamedQuery(name = "RiderUndertakesRideEntity.countTotalNoRideSearchesBetweenDates", query = "SELECT COUNT(r.riderrouteId) FROM RiderUndertakesRideEntity r WHERE r.starttimeEarliest BETWEEN :startdate AND :enddate"),
    @NamedQuery(name = "RiderUndertakesRideEntity.countTotalNoBookings", query = "SELECT COUNT(r.rideId) FROM RiderUndertakesRideEntity r WHERE r.rideId IS NOT NULL"),
    @NamedQuery(name = "RiderUndertakesRideEntity.countTotalNoBookingsAfterDate", query = "SELECT COUNT(r.rideId) FROM RiderUndertakesRideEntity r WHERE r.rideId IS NOT NULL AND r.starttimeEarliest >= :startDate"),
    @NamedQuery(name = "RiderUndertakesRideEntity.countTotalNoBookingsBetweenDates", query = "SELECT COUNT(r.rideId) FROM RiderUndertakesRideEntity r WHERE (r.rideId IS NOT NULL) AND (r.starttimeEarliest BETWEEN :startdate AND :enddate)"),
    // TODO: timestampbooked should be replaced with Timestamprealized once this is set!
    @NamedQuery(name = "RiderUndertakesRideEntity.findRidesWithoutRatingByRider", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.custId = :custId AND (r.givenrating IS NULL OR r.receivedrating IS NULL) AND r.rideId IS NOT NULL AND r.timestampbooked IS NOT NULL ORDER BY r.timestampbooked DESC"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findRidesWithoutGivenRatingByRider", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.custId = :custId AND r.givenrating IS NULL AND r.rideId IS NOT NULL AND r.timestampbooked IS NOT NULL ORDER BY r.timestampbooked DESC"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findRidesWithoutReceivedRatingByRider", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.custId = :custId AND r.receivedrating IS NULL AND r.rideId IS NOT NULL AND r.timestampbooked IS NOT NULL ORDER BY r.timestampbooked DESC"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findRidesBetweenDatesforCustId", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.custId = :custId  AND (r.starttimeEarliest BETWEEN :startdate AND :enddate)"),
    // TODO: timestampbooked should be replaced with Timestamprealized once this is set!
    @NamedQuery(name = "RiderUndertakesRideEntity.findRidesWithoutRatingByDriver", query = "SELECT r FROM DriverUndertakesRideEntity d, RiderUndertakesRideEntity r WHERE d.custId = :custId AND r.rideId = d AND (r.givenrating IS NULL OR r.receivedrating IS NULL) AND r.timestampbooked IS NOT NULL ORDER BY r.timestampbooked DESC"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findRidesWithoutGivenRatingByDriver", query = "SELECT r FROM DriverUndertakesRideEntity d, RiderUndertakesRideEntity r WHERE d.custId = :custId AND r.rideId = d AND r.givenrating IS NULL AND r.timestampbooked IS NOT NULL ORDER BY r.timestampbooked DESC"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findRidesWithoutReceivedRatingByDriver", query = "SELECT r FROM DriverUndertakesRideEntity d, RiderUndertakesRideEntity r WHERE d.custId = :custId AND r.rideId = d AND r.receivedrating IS NULL AND r.timestampbooked IS NOT NULL ORDER BY r.timestampbooked DESC"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findRatedRidesByRider", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.custId = :custId AND r.receivedrating IS NOT NULL ORDER BY r.receivedratingDate DESC"),
    @NamedQuery(name = "RiderUndertakesRideEntity.findRatedRidesByDriver", query = "SELECT r FROM DriverUndertakesRideEntity d, RiderUndertakesRideEntity r WHERE d.custId = :custId AND r.rideId = d AND r.givenrating IS NOT NULL ORDER BY r.givenratingDate DESC"),
    @NamedQuery(name = "RiderUndertakesRideEntity.countPositiveRatingsAsRider", query = "SELECT COUNT(r.riderrouteId) FROM RiderUndertakesRideEntity r WHERE r.custId = :custId AND r.receivedrating = 1 AND r.receivedratingDate > :fromDate"),
    @NamedQuery(name = "RiderUndertakesRideEntity.countNeutralRatingsAsRider", query = "SELECT COUNT(r.riderrouteId) FROM RiderUndertakesRideEntity r WHERE r.custId = :custId AND r.receivedrating = 0 AND r.receivedratingDate > :fromDate"),
    @NamedQuery(name = "RiderUndertakesRideEntity.countNegativeRatingsAsRider", query = "SELECT COUNT(r.riderrouteId) FROM RiderUndertakesRideEntity r WHERE r.custId = :custId AND r.receivedrating = -1 AND r.receivedratingDate > :fromDate"),
    @NamedQuery(name = "RiderUndertakesRideEntity.countPositiveRatingsAsDriver", query = "SELECT COUNT(r.riderrouteId) FROM DriverUndertakesRideEntity d, RiderUndertakesRideEntity r WHERE d.custId = :custId AND r.rideId = d AND r.givenrating = 1 AND r.givenratingDate > :fromDate"),
    @NamedQuery(name = "RiderUndertakesRideEntity.countNeutralRatingsAsDriver", query = "SELECT COUNT(r.riderrouteId) FROM DriverUndertakesRideEntity d, RiderUndertakesRideEntity r WHERE d.custId = :custId AND r.rideId = d AND r.givenrating = 0 AND r.givenratingDate > :fromDate"),
    @NamedQuery(name = "RiderUndertakesRideEntity.countNegativeRatingsAsDriver", query = "SELECT COUNT(r.riderrouteId) FROM DriverUndertakesRideEntity d, RiderUndertakesRideEntity r WHERE d.custId = :custId AND r.rideId = d AND r.givenrating = -1 AND r.givenratingDate > :fromDate"),
    //
    // ride searches used by joride reporting
    // get all rides for rider, regardless wether realized or not
    //
    @NamedQuery(name = "RiderUndertakesRideEntity.findByRidersRidesBetween", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.starttimeLatest  > :startDate AND r.starttimeEarliest < :endDate AND r.custId = :custId "),
    // get all rides for rider that have been realized
    @NamedQuery(name = "RiderUndertakesRideEntity.findByRidersRealizedRidesBetween", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.timestamprealized BETWEEN :startDate AND :endDate AND r.custId = :custId "),
    // get all rides for rider which have not yet been realized
    @NamedQuery(name = "RiderUndertakesRideEntity.findByRidersUnratedRidesBetween", query = "SELECT r FROM RiderUndertakesRideEntity r WHERE r.timestamprealized BETWEEN :startDate AND :endDate AND r.custId = :custId  AND r.givenrating is NULL"),
    // find all rides for driver in this interval
    @NamedQuery(name = "RiderUndertakesRideEntity.findByDriversRidesBetween", query = "SELECT r FROM DriverUndertakesRideEntity d, RiderUndertakesRideEntity r WHERE d.custId = :custId AND r.rideId = d  AND r.starttimeEarliest > :startDate AND r.starttimeLatest < :endDate "),
    //    
    // Queries for rating statistics in jORide   
    //     
    // Count all ratings where customer given by custId acted as driver
    @NamedQuery(name = "RiderUndertakesRideEntity.countRatingsAsDriver", query = "SELECT COUNT(r.riderrouteId) FROM DriverUndertakesRideEntity d, RiderUndertakesRideEntity r WHERE d.custId = :custId AND r.rideId = d "),
    // Sum up all ratings where customer given by custId acted as driver
    @NamedQuery(name = "RiderUndertakesRideEntity.sumUpRatingsAsDriver", query = "SELECT SUM(r.givenrating) FROM DriverUndertakesRideEntity d, RiderUndertakesRideEntity r WHERE d.custId = :custId AND r.rideId = d"),
    // Count all ratings where customer given by custId acted as rider
    @NamedQuery(name = "RiderUndertakesRideEntity.countRatingsAsRider",  query = "SELECT COUNT(r.riderrouteId) FROM RiderUndertakesRideEntity r WHERE r.custId = :custId"),
    // Sum up all ratings where customer given by custId acted as rider
    @NamedQuery(name = "RiderUndertakesRideEntity.sumUpRatingsAsRider",  query = "SELECT SUM(r.receivedrating) FROM RiderUndertakesRideEntity r WHERE r.custId = :custId")
})



@Converter(name = "convert", converterClass = PointConverter.class)
public class RiderUndertakesRideEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "riderroute_id")
    private Integer riderrouteId;
    @Column(name = "givenrating")
    private Integer givenrating;
    @Column(name = "givenrating_comment")
    private String givenratingComment;
    @Column(name = "givenrating_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date givenratingDate;
    @Column(name = "receivedrating_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedratingDate;
    @Column(name = "starttime_latest")
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttimeLatest;
    @Lob
    @Column(name = "startpt")
    @Convert("convert")
    private Point startpt;
    @Column(name = "timestamprealized")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamprealized;
    @Lob
    @Column(name = "endpt")
    @Convert("convert")
    private Point endpt;
    @Column(name = "receivedrating")
    private Integer receivedrating;
    @Column(name = "price")
    private Double price;
    @Column(name = "no_passengers")
    private Integer noPassengers;
    @Column(name = "timestampbooked")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestampbooked;
    @Column(name = "receivedrating_comment")
    private String receivedratingComment;
    @Column(name = "starttime_earliest")
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttimeEarliest;
    /*@JoinColumn(name = "account_timestamp", referencedColumnName = "account_timestamp")
     @ManyToOne
     private AccountHistoryEntity accountHistoryEntity;*/
    @JoinColumn(name = "cust_id", referencedColumnName = "cust_id", insertable = true, updatable = true)
    @ManyToOne(optional = false)
    private CustomerEntity custId;
    @JoinColumn(name = "ride_id", referencedColumnName = "ride_id")
    @ManyToOne
    private DriverUndertakesRideEntity rideId;
    @Column(name = "startpt_addr")
    private String startptAddress;
    @Column(name = "endpt_addr")
    private String endptAddress;
    @Column(name = "comment")
    private String comment;

    public RiderUndertakesRideEntity() {
    }

    public RiderUndertakesRideEntity(CustomerEntity cust_id, Date starttimeEarliest, Date starttimeLatest, int noPassengers, Point startpt, Point endpt, double price) {
        this.custId = cust_id;
        this.starttimeEarliest = starttimeEarliest;
        this.starttimeLatest = starttimeLatest;
        this.startpt = startpt;
        this.endpt = endpt;
        this.price = price;
        this.noPassengers = noPassengers;
    }

    public RiderUndertakesRideEntity(Integer riderrouteId) {
        this.riderrouteId = riderrouteId;
    }

    public void setEndptAddress(String endptAddress) {
        this.endptAddress = endptAddress;
    }

    public void setStartptAddress(String startptAddress) {
        this.startptAddress = startptAddress;
    }

    public String getEndptAddress() {
        return endptAddress;
    }

    public String getStartptAddress() {
        return startptAddress;
    }

    public Integer getRiderrouteId() {
        return riderrouteId;
    }

    public void setRiderrouteId(Integer riderrouteId) {
        this.riderrouteId = riderrouteId;
    }

    public Integer getGivenrating() {
        return givenrating;
    }

    public void setGivenrating(Integer givenrating) {
        this.givenrating = givenrating;
    }

    public String getGivenratingComment() {
        return givenratingComment;
    }

    public void setGivenratingComment(String givenratingComment) {
        this.givenratingComment = givenratingComment;
    }

    public Date getGivenratingDate() {
        return givenratingDate;
    }

    public void setGivenratingDate(Date givenratingDate) {
        this.givenratingDate = givenratingDate;
    }

    public Date getReceivedratingDate() {
        return receivedratingDate;
    }

    public void setReceivedratingDate(Date receivedratingDate) {
        this.receivedratingDate = receivedratingDate;
    }

    public Date getStarttimeLatest() {
        return starttimeLatest;
    }

    public void setStarttimeLatest(Date starttimeLatest) {
        this.starttimeLatest = starttimeLatest;
    }

    public Point getStartpt() {
        return startpt;
    }

    public void setStartpt(Point startpt) {
        this.startpt = startpt;
    }

    public Date getTimestamprealized() {
        return timestamprealized;
    }

    public void setTimestamprealized(Date timestamprealized) {
        this.timestamprealized = timestamprealized;
    }

    public Point getEndpt() {
        return endpt;
    }

    public void setEndpt(Point endpt) {
        this.endpt = endpt;
    }

    public Integer getReceivedrating() {
        return receivedrating;
    }

    public void setReceivedrating(Integer receivedrating) {
        this.receivedrating = receivedrating;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNoPassengers() {
        return noPassengers;
    }

    public void setNoPassengers(Integer noPassengers) {
        this.noPassengers = noPassengers;
    }

    public Date getTimestampbooked() {
        return timestampbooked;
    }

    public void setTimestampbooked(Date timestampbooked) {
        this.timestampbooked = timestampbooked;
    }

    public String getReceivedratingComment() {
        return receivedratingComment;
    }

    public void setReceivedratingComment(String receivedratingComment) {
        this.receivedratingComment = receivedratingComment;
    }

    public Date getStarttimeEarliest() {
        return starttimeEarliest;
    }

    public void setStarttimeEarliest(Date starttimeEarliest) {
        this.starttimeEarliest = starttimeEarliest;
    }
    /*
     public AccountHistoryEntity getAccountHistoryEntity() {
     return accountHistoryEntity;
     }

     public void setAccountHistoryEntity(AccountHistoryEntity accountHistoryEntity) {
     this.accountHistoryEntity = accountHistoryEntity;
     }
     */

    public CustomerEntity getCustId() {
        return custId;
    }

    public void setCustId(CustomerEntity custId) {
        this.custId = custId;
    }

    public DriverUndertakesRideEntity getRideId() {
        return rideId;
    }

    public void setRideId(DriverUndertakesRideEntity rideId) {
        this.rideId = rideId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (riderrouteId != null ? riderrouteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RiderUndertakesRideEntity)) {
            return false;
        }
        RiderUndertakesRideEntity other = (RiderUndertakesRideEntity) object;
        if ((this.riderrouteId == null && other.riderrouteId != null) || (this.riderrouteId != null && !this.riderrouteId.equals(other.riderrouteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.fhg.fokus.openride.rides.driver.RiderUndertakesRideEntity[riderrouteId=" + riderrouteId + "]";
    }
}
