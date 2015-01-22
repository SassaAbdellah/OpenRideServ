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

package de.fhg.fokus.openride.customerprofile;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 *
 * @author pab
 */
@Embeddable
public class FavoritePointsEntityPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "cust_id")
    private int custId;
    @Basic(optional = false)
    @Column(name = "favpt_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer favptId;
    public FavoritePointsEntityPK() {
    }

    public FavoritePointsEntityPK(int custId) {
        this.custId = custId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getFavPtfrequency() {
        return favptId;
    }

    public void setFavPtfrequency(int favptId) {
        this.favptId = favptId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) custId;
        hash += (int) favptId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FavoritePointsEntityPK)) {
            return false;
        }
        FavoritePointsEntityPK other = (FavoritePointsEntityPK) object;
        if (this.custId != other.custId) {
            return false;
        }
        if (this.favptId != other.favptId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.fhg.fokus.openride.customerprofile.FavoritePointsEntityPK[custId=" + custId + ", favPtfrequency=" + favptId + "]";
    }

}
