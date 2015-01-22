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

package de.fhg.fokus.openride.rides.driver;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author fvi
 */
@NamedQueries({
    @NamedQuery(name = "RoutePointEntity.findByRideId", query = "SELECT rp FROM RoutePointEntity rp WHERE rp.rideId = :rideId ORDER BY rp.routeIdx"),
    @NamedQuery(name = "RoutePointEntity.findRequiredByRideId", query = "SELECT rp FROM RoutePointEntity rp WHERE rp.rideId = :rideId AND rp.isRequired = true ORDER BY rp.routeIdx")
})
@Entity
@Table(name="route_point")
@IdClass(RoutePointEntityPk.class)
public class RoutePointEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ride_id", nullable=false)
    private Integer rideId;

    @Id
    @Column(name = "route_idx", nullable=false)
    private Integer routeIdx;

    @Column(name = "longitude", nullable=false)
    private Double longitude;

    @Column(name = "latitude", nullable=false)
    private Double latitude;

    @Column(name = "riderroute_id")
    private Integer riderrouteId;

    @Column(name = "is_required", nullable=false)
    private Boolean isRequired;

    

    public RoutePointEntity() {}

    public RoutePointEntity(Integer rideId, Integer routeIdx, Double longitude, Double latitude, Integer riderrouteId, boolean isRequired) {
        this.rideId = rideId;
        this.routeIdx = routeIdx;
        this.longitude = longitude;
        this.latitude = latitude;
        this.riderrouteId = riderrouteId;
        this.isRequired = isRequired;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Integer getRideId() {
        return rideId;
    }

    public Integer getRiderrouteId() {
        return riderrouteId;
    }

    public Integer getRouteIdx() {
        return routeIdx;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Boolean isRequired() {
        return isRequired;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public void setRiderrouteId(Integer riderrouteId) {
        this.riderrouteId = riderrouteId;
    }

    public void setRouteIdx(Integer routeIdx) {
        this.routeIdx = routeIdx;
    }

    public void setRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }
}

