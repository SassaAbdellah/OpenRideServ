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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/*
-- Table: favorite_point

-- DROP TABLE favoritepoint;

CREATE TABLE favoritepoint
(
  favpt_id serial NOT NULL,
  favpt_address character varying(255),
  favpt_point character varying(255) NOT NULL,
  favpt_displayname character varying(255),
  favpt_frequency integer NOT NULL,
  cust_id integer NOT NULL,
  CONSTRAINT favpt_pk PRIMARY KEY (favpt_id),
  CONSTRAINT favpt_fk_cust_id FOREIGN KEY (cust_id)
      REFERENCES customer (cust_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE favorite_point OWNER TO openride;
 */

/**
 *
 * @author Tilo Kussatz <tilo at kussatz.com>
 */
@Entity
@Table(name = "favoritepoint")
@NamedQueries({
    @NamedQuery(name = "FavoritePointEntity.findAll", query = "SELECT f FROM FavoritePointEntity f"),
    @NamedQuery(name = "FavoritePointEntity.findByFavptId", query = "SELECT f FROM FavoritePointEntity f WHERE f.favptId = :favptId"),
    @NamedQuery(name = "FavoritePointEntity.findByFavptAddress", query = "SELECT f FROM FavoritePointEntity f WHERE f.favptAddress = :favptAddress"),
    @NamedQuery(name = "FavoritePointEntity.findByFavptPoint", query = "SELECT f FROM FavoritePointEntity f WHERE f.favptPoint = :favptPoint"),
    @NamedQuery(name = "FavoritePointEntity.findByFavptDisplayname", query = "SELECT f FROM FavoritePointEntity f WHERE f.favptDisplayname = :favptDisplayname"),
    @NamedQuery(name = "FavoritePointEntity.findByFavptDisplaynameCustId", query = "SELECT f FROM FavoritePointEntity f WHERE f.favptDisplayname = :favptDisplayname AND f.custId = :custId"),
    @NamedQuery(name = "FavoritePointEntity.findByFavptFrequency", query = "SELECT f FROM FavoritePointEntity f WHERE f.favptFrequency = :favptFrequency"),
    @NamedQuery(name = "FavoritePointEntity.findByCustId", query = "SELECT f FROM FavoritePointEntity f WHERE f.custId = :custId ORDER BY f.favptFrequency DESC")
})
public class FavoritePointEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "favpt_id")
    private Integer favptId;
    @Column(name = "favpt_address")
    private String favptAddress;
    @Basic(optional = false)
    @Column(name = "favpt_point")
    private String favptPoint;
    @Column(name = "favpt_displayname")
    private String favptDisplayname;
    @Basic(optional = false)
    @Column(name = "favpt_frequency")
    private int favptFrequency;
    @JoinColumn(name = "cust_id", referencedColumnName = "cust_id")
    @ManyToOne(optional = false)
    private CustomerEntity custId;

    public FavoritePointEntity() {
    }

    public FavoritePointEntity(Integer favptId) {
        this.favptId = favptId;
    }

    public FavoritePointEntity(Integer favptId, String favptPoint, int favptFrequency) {
        this.favptId = favptId;
        this.favptPoint = favptPoint;
        this.favptFrequency = favptFrequency;
    }

    public Integer getFavptId() {
        return favptId;
    }

    public void setFavptId(Integer favptId) {
        this.favptId = favptId;
    }

    public String getFavptAddress() {
        return favptAddress;
    }

    public void setFavptAddress(String favptAddress) {
        this.favptAddress = favptAddress;
    }

    public String getFavptPoint() {
        return favptPoint;
    }

    public void setFavptPoint(String favptPoint) {
        this.favptPoint = favptPoint;
    }

    public String getFavptDisplayname() {
        return favptDisplayname;
    }

    public void setFavptDisplayname(String favptDisplayname) {
        this.favptDisplayname = favptDisplayname;
    }

    public int getFavptFrequency() {
        return favptFrequency;
    }

    public void setFavptFrequency(int favptFrequency) {
        this.favptFrequency = favptFrequency;
    }

    public CustomerEntity getCustId() {
        return custId;
    }

    public void setCustId(CustomerEntity custId) {
        this.custId = custId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (favptId != null ? favptId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FavoritePointEntity)) {
            return false;
        }
        FavoritePointEntity other = (FavoritePointEntity) object;
        if ((this.favptId == null && other.favptId != null) || (this.favptId != null && !this.favptId.equals(other.favptId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.fhg.fokus.openride.entity.FavoritePoint[favptId=" + favptId + "]";
    }

}
