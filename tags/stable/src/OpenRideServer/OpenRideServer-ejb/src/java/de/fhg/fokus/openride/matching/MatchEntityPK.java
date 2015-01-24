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

package de.fhg.fokus.openride.matching;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author pab
 * This class implements the composed primary key of
 * a MatchEntity which consists of the two ids of riders
 * offer and drivers offer.
 */
@Embeddable
public class MatchEntityPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "riderroute_id")
    private int riderrouteId;
    @Basic(optional = false)
    @Column(name = "ride_id")
    private int rideId;

    public MatchEntityPK() {

    }

    /**
     *
     * @param riderrouteId id of riders offer.
     * @param rideId id of drivers offer.
     */
    public MatchEntityPK(int riderrouteId, int rideId) {
        this.riderrouteId = riderrouteId;
        this.rideId = rideId;
    }

    /**
     *
     * @return id of riders offer.
     */
    public int getRiderrouteId() {
        return riderrouteId;
    }

    /**
     * Set id of riders offer.
     * @param riderrouteId the new value.
     */
    public void setRiderrouteId(int riderrouteId) {
        this.riderrouteId = riderrouteId;
    }

    /**
     * @return id of drivers offer.
     */
    public int getRideId() {
        return rideId;
    }

    /**
     * Sets the id of drivers offer to the given value 'rideId'.
     * @param rideId
     */
    public void setRideId(int rideId) {
        this.rideId = rideId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) riderrouteId;
        hash += (int) rideId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MatchEntityPK)) {
            return false;
        }
        MatchEntityPK other = (MatchEntityPK) object;
        if (this.riderrouteId != other.riderrouteId) {
            return false;
        }
        if (this.rideId != other.rideId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.fhg.fokus.openride.matching.MatchEntityPK[riderrouteId=" + riderrouteId + ", rideId=" + rideId + "]";
    }

}
