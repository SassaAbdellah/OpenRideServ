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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author fvi
 */
@Embeddable
public class RoutePointEntityPk implements Serializable{
    
    @Basic(optional = false)
    @Column(name = "ride_id", nullable=false)
    @JoinColumn(name = "driverundertakesride", referencedColumnName = "ride_id")
    @ManyToOne
    private Integer rideId;
    @Basic(optional = false)
    @Column(name = "route_idx, nullable=false")
    private Integer routeIdx;

    public RoutePointEntityPk() {
    }
    
    public RoutePointEntityPk(Integer rideId, Integer routeIdx) {
        this.rideId = rideId;
        this.routeIdx = routeIdx;
    }
    
    public Integer getRideId() {
        return rideId;
    }

    public Integer getRouteIdx() {
        return routeIdx;
    }

    public void setRideId(Integer ride_id) {
        this.rideId = ride_id;
    }

    public void setRouteIdx(Integer routeIdx) {
        this.routeIdx = routeIdx;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) rideId;
        hash += (int) routeIdx;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RoutePointEntityPk)) {
            return false;
        }
        RoutePointEntityPk other = (RoutePointEntityPk) object;
        return rideId == other.rideId && routeIdx == other.routeIdx;
    }


}
