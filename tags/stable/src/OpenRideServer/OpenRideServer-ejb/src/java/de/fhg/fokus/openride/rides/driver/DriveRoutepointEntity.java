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

import de.fhg.fokus.openride.helperclasses.converter.PointConverter;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.postgis.Point;

/**
 *
 * @author fvi
 */
@NamedQueries({
    @NamedQuery(name = "DriveRoutepointEntity.findByDriveId", query = "SELECT rp FROM DriveRoutepointEntity rp WHERE rp.driveId = :rideId ORDER BY rp.routeIdx")
})
@Entity
@Table(name="drive_route_point")
@IdClass(DriveRoutePointEntityPK.class)
@Converter(name="convert", converterClass=PointConverter.class)

public class DriveRoutepointEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "drive_id", nullable=false)
    private Integer driveId;
    
    @Id
    @Column(name = "route_idx", nullable=false)
    private Integer routeIdx;

    @Column(name = "coordinate", nullable=false)
    @Convert("convert")
    private Point coordinate;

    @Column(name = "expected_arrival", nullable=false)
    private Timestamp expectedArrival;
    
    @Column(name = "seats_available", nullable=false)
    private Integer seatsAvailable;

    @Column(name = "distance_to_source", nullable=false)
    private Double distanceToSourceMeters;
    

    public DriveRoutepointEntity() {}

    public DriveRoutepointEntity(int driveId, int routeIdx, Point coordinate,
            Timestamp expectedArrival, int seatsAvailable, double distanceToSourceMeters) {
        this.driveId = driveId;
        this.routeIdx = routeIdx;
        this.coordinate = coordinate;
        this.expectedArrival = expectedArrival;
        this.seatsAvailable = seatsAvailable;
        this.distanceToSourceMeters = distanceToSourceMeters;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Point coordinate) {
        this.coordinate = coordinate;
    }

    public Integer getDriveId() {
        return driveId;
    }

    public void setDriveId(Integer driveId) {
        this.driveId = driveId;
    }

    public Timestamp getExpectedArrival() {
        return expectedArrival;
    }

    public void setExpectedArrival(Timestamp expectedArrival) {
        this.expectedArrival = expectedArrival;
    }

    public Integer getRouteIdx() {
        return routeIdx;
    }

    public void setRouteIdx(Integer routeIdx) {
        this.routeIdx = routeIdx;
    }

    public Integer getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(Integer seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public Double getDistanceToSourceMeters() {
        return distanceToSourceMeters;
    }

    public void setDistanceToSourceMeters(Double distanceToSourceMeters) {
        this.distanceToSourceMeters = distanceToSourceMeters;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (driveId != null ? driveId.hashCode() : 0);
        hash += (routeIdx != null ? routeIdx.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DriveRoutepointEntity)) {
            return false;
        }
        DriveRoutepointEntity other = (DriveRoutepointEntity) object;
        if ((this.driveId == null && other.driveId != null) || (this.routeIdx != null && !this.routeIdx.equals(other.routeIdx))) {
            return false;
        }
        return true;
    }
}
