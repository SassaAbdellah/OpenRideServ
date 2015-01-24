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

import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideEntity;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "match")
@NamedQueries({
    // TODO: find all should possibly be removed, no value added
    @NamedQuery(name = "MatchEntity.findAll", query = "SELECT m FROM MatchEntity m"),
    @NamedQuery(name = "MatchEntity.findByRiderIdAndRiderrouteId", query = "SELECT m FROM MatchEntity m WHERE m.matchEntityPK.riderrouteId = :riderrouteId AND m.matchEntityPK.rideId =:rideId"),
    @NamedQuery(name = "MatchEntity.findByRiderrouteId", query = "SELECT m FROM MatchEntity m WHERE m.matchEntityPK.riderrouteId = :riderrouteId"),
    @NamedQuery(name = "MatchEntity.findByRideId", query = "SELECT m FROM MatchEntity m WHERE m.matchEntityPK.rideId = :rideId"),
    @NamedQuery(name = "MatchEntity.findByRideIdAndStates", query = "SELECT m FROM MatchEntity m WHERE m.matchEntityPK.rideId = :rideId AND m.riderState = :riderState AND m.driverState = :driverState"),
    @NamedQuery(name = "MatchEntity.findByRideIdRiderrouteId", query = "SELECT m FROM MatchEntity m WHERE m.matchEntityPK.rideId = :rideId AND m.matchEntityPK.riderrouteId = :riderrouteId"),
    @NamedQuery(name = "MatchEntity.findByDriverState", query = "SELECT m FROM MatchEntity m WHERE m.driverState = :driverState"),
    @NamedQuery(name = "MatchEntity.findByRiderState", query = "SELECT m FROM MatchEntity m WHERE m.riderState = :riderState"),
    @NamedQuery(name = "MatchEntity.findByMatchSharedDistancEmeters", query = "SELECT m FROM MatchEntity m WHERE m.matchSharedDistancEmeters = :matchSharedDistancEmeters"),
    @NamedQuery(name = "MatchEntity.findByMatchDetourMeters", query = "SELECT m FROM MatchEntity m WHERE m.matchDetourMeters = :matchDetourMeters"),
    @NamedQuery(name = "MatchEntity.findByMatchExpectedStartTime", query = "SELECT m FROM MatchEntity m WHERE m.matchExpectedStartTime = :matchExpectedStartTime"),
    @NamedQuery(name = "MatchEntity.findByRideIdRejected", query = "SELECT m FROM MatchEntity m WHERE m.matchEntityPK.rideId = :rideId AND (m.driverState = FALSE OR m.riderState = FALSE)"),
    @NamedQuery(name = "MatchEntity.findByRideIdOpen", query = "SELECT m FROM MatchEntity m WHERE m.matchEntityPK.rideId = :rideId AND (m.driverState = TRUE OR m.riderState IS NULL) AND (m.riderState = TRUE OR m.driverState IS NULL)"),
    @NamedQuery(name = "MatchEntity.findByRideIdSuccessful", query = "SELECT m FROM MatchEntity m WHERE m.matchEntityPK.rideId = :rideId AND (m.driverState = TRUE AND m.riderState = TRUE)"),
    @NamedQuery(name = "MatchEntity.findByRiderrouteIdSuccessful", query = "SELECT m FROM MatchEntity m WHERE m.matchEntityPK.riderrouteId = :riderrouteId AND (m.driverState = TRUE AND m.riderState = TRUE)"),
    @NamedQuery(name = "MatchEntity.findChangesSinceAccessByDriverByRideId", query = "SELECT m FROM MatchEntity m WHERE m.matchEntityPK.rideId = :rideId AND (m.driverAccess IS NULL OR m.riderChange > m.driverAccess)"),
    @NamedQuery(name = "MatchEntity.findChangesSinceAccessByRiderByRiderrouteId", query = "SELECT m FROM MatchEntity m WHERE m.matchEntityPK.riderrouteId = :riderrouteId AND (m.riderAccess IS NULL OR m.driverChange > m.riderAccess)"),
    @NamedQuery(name = "MatchEntity.countTotalNoMatches", query = "SELECT COUNT(m.matchSharedDistancEmeters) FROM MatchEntity m"),
    @NamedQuery(name = "MatchEntity.countTotalNoMatchesAfterDate", query = "SELECT COUNT(m.matchSharedDistancEmeters) FROM MatchEntity m WHERE m.matchExpectedStartTime >= :date"),
    @NamedQuery(name = "MatchEntity.countTotalNoMatchesBetweenDates", query = "SELECT COUNT(m.matchSharedDistancEmeters) FROM MatchEntity m WHERE m.matchExpectedStartTime BETWEEN :startDate AND :endDate")
})
/**
 *
 * @author pab
 *
 * This class has information about the match and the state within the booking
 * process.s
 *
 */
public class MatchEntity implements Serializable {

    public static final Integer NOT_ADAPTED = 0;
    public static final Integer REJECTED = 1;
    public static final Integer ACCEPTED = 2;
    public static final Integer RIDER_COUNTERMANDED = 3;
    public static final Integer DRIVER_COUNTERMANDED = 4;
    public static final Integer NO_MORE_AVAILABLE = 5;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MatchEntityPK matchEntityPK;
    @Column(name = "driver_state")
    private Integer driverState = MatchEntity.NOT_ADAPTED;
    @Column(name = "rider_state")
    private Integer riderState = MatchEntity.NOT_ADAPTED;
    @Column(name = "match_shared_distance_meters")
    private Double matchSharedDistancEmeters;
    @Column(name = "match_drive_remaining_distance_meters")
    private Double matchDriveRemainingDistanceMeters;
    @Column(name = "match_price_cents")
    private Integer matchPriceCents;
    @Column(name = "match_detour_meters")
    private Double matchDetourMeters;
    @Column(name = "match_expected_start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date matchExpectedStartTime;
    @JoinColumn(name = "ride_id", referencedColumnName = "ride_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DriverUndertakesRideEntity driverUndertakesRideEntity;
    @JoinColumn(name = "riderroute_id", referencedColumnName = "riderroute_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private RiderUndertakesRideEntity riderUndertakesRideEntity;
    @Column(name = "driver_change")
    @Temporal(TemporalType.TIMESTAMP)
    private Date driverChange;
    @Column(name = "rider_change")
    @Temporal(TemporalType.TIMESTAMP)
    private Date riderChange;
    @Column(name = "driver_access")
    @Temporal(TemporalType.TIMESTAMP)
    private Date driverAccess;
    @Column(name = "rider_access")
    @Temporal(TemporalType.TIMESTAMP)
    private Date riderAccess;
    @Column(name = "rider_message")
    private String riderMessage;
    @Column(name = "driver_message")
    private String driverMessage;

    public MatchEntity() {
    }

    public MatchEntity(int riderrouteId, int rideId, double matchSharedDistancEmeters,
            Double matchDetourMeters, Date matchExpectedStartTime, double matchDriveRemainingDistanceMeters, int matchPriceCents) {
        this.matchEntityPK = new MatchEntityPK(riderrouteId, rideId);
        this.matchSharedDistancEmeters = matchSharedDistancEmeters;
        this.matchDetourMeters = matchDetourMeters;
        this.matchExpectedStartTime = matchExpectedStartTime;
        this.matchDriveRemainingDistanceMeters = matchDriveRemainingDistanceMeters;
        this.matchPriceCents = matchPriceCents;
    }

    public MatchEntity(MatchEntityPK matchEntityPK) {
        this.matchEntityPK = matchEntityPK;
    }

    public MatchEntity(int riderrouteId, int rideId) {
        this.matchEntityPK = new MatchEntityPK(riderrouteId, rideId);
    }

    public MatchEntityPK getMatchEntityPK() {
        return matchEntityPK;
    }

    public void setMatchEntityPK(MatchEntityPK matchEntityPK) {
        this.matchEntityPK = matchEntityPK;
    }

    public Integer getDriverState() {
        return driverState;
    }

    public void setDriverState(Integer driverState) {
        this.driverState = driverState;
    }

    public Integer getRiderState() {
        return riderState;
    }

    public void setRiderState(Integer riderState) {
        this.riderState = riderState;
    }

    public Double getMatchSharedDistancEmeters() {
        return matchSharedDistancEmeters;
    }

    public void setMatchSharedDistancEmeters(Double matchSharedDistancEmeters) {
        this.matchSharedDistancEmeters = matchSharedDistancEmeters;
    }

    public Double getMatchDetourMeters() {
        return matchDetourMeters;
    }

    public void setMatchDetourMeters(Double matchDetourMeters) {
        this.matchDetourMeters = matchDetourMeters;
    }

    public Date getMatchExpectedStartTime() {
        return matchExpectedStartTime;
    }

    public Double getMatchDriveRemainingDistanceMeters() {
        return matchDriveRemainingDistanceMeters;
    }

    public Integer getMatchPriceCents() {
        return matchPriceCents;
    }

    public void setMatchExpectedStartTime(Date matchExpectedStartTime) {
        this.matchExpectedStartTime = matchExpectedStartTime;
    }

    public DriverUndertakesRideEntity getDriverUndertakesRideEntity() {
        return driverUndertakesRideEntity;
    }

    public void setDriverUndertakesRideEntity(DriverUndertakesRideEntity driverUndertakesRideEntity) {
        this.driverUndertakesRideEntity = driverUndertakesRideEntity;
    }

    public RiderUndertakesRideEntity getRiderUndertakesRideEntity() {
        return riderUndertakesRideEntity;
    }

    public void setRiderUndertakesRideEntity(RiderUndertakesRideEntity riderUndertakesRideEntity) {
        this.riderUndertakesRideEntity = riderUndertakesRideEntity;
    }

    public void setMatchDriveRemainingDistanceMeters(Double matchDriveRemainingDistanceMeters) {
        this.matchDriveRemainingDistanceMeters = matchDriveRemainingDistanceMeters;
    }

    public void setMatchPriceCents(Integer matchPriceCents) {
        this.matchPriceCents = matchPriceCents;
    }

    public Date getDriverAccess() {
        return driverAccess;
    }

    public void setDriverAccess(Date driverAccess) {
        this.driverAccess = driverAccess;
    }

    public Date getDriverChange() {
        return driverChange;
    }

    public void setDriverChange(Date driverChange) {
        this.driverChange = driverChange;
    }

    public Date getRiderAccess() {
        return riderAccess;
    }

    public void setRiderAccess(Date riderAccess) {
        this.riderAccess = riderAccess;
    }

    public Date getRiderChange() {
        return riderChange;
    }

    public void setRiderChange(Date riderChange) {
        this.riderChange = riderChange;
    }

    public String getRiderMessage() {
        return this.riderMessage;
    }

    public void setRiderMessage(String arg) {
        this.riderMessage = arg;
    }

    public String getDriverMessage() {
        return this.driverMessage;
    }

    public void setDriverMessage(String arg) {
        this.driverMessage = arg;
    }

    /**
     * @return true, if driver can countermand this request/matching
     */
    public boolean getCanDriverCountermand() {
        return this.getDriverState() == ACCEPTED;
    }

    /**
     * @return true, if driver can countermand this request/matching
     */
    public boolean getCanRiderCountermand() {
        return this.getRiderState() == ACCEPTED;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matchEntityPK != null ? matchEntityPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MatchEntity)) {
            return false;
        }
        MatchEntity other = (MatchEntity) object;
        if ((this.matchEntityPK == null && other.matchEntityPK != null) || (this.matchEntityPK != null && !this.matchEntityPK.equals(other.matchEntityPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.fhg.fokus.openride.matching.MatchEntity[matchEntityPK=" + matchEntityPK + "]";
    }

    /**
     * ToDO: what's this (found in original OpenRide)
     *
     * @deprecated since it it obviously not used, it should go away
     */
    public void reloadAllEntities() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    /** true, if rider state is in ACCEPTED, RIDER_COUNTERMANDED 
     */
    public boolean getRiderRateable(){
    	return ( this.getRiderState()==ACCEPTED || this.getRiderState()==RIDER_COUNTERMANDED);
    }
    
    /** true, if rider state is in ACCEPTED, DRIVER_COUNTERMANDED 
     */
    public boolean getDriverRateable(){
    	return ( this.getDriverState()==ACCEPTED || this.getDriverState()==DRIVER_COUNTERMANDED);
    }
    
}
