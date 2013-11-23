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

/**
 *
 * @author tku
 */
@Entity
@Table(name = "cardetails")
@NamedQueries({
    @NamedQuery(name = "CarDetailsEntity.findAll", query = "SELECT c FROM CarDetailsEntity c"),
    @NamedQuery(name = "CarDetailsEntity.findByCardetId", query = "SELECT c FROM CarDetailsEntity c WHERE c.cardetId = :cardetId"),
    @NamedQuery(name = "CarDetailsEntity.findByCardetColour", query = "SELECT c FROM CarDetailsEntity c WHERE c.cardetColour = :cardetColour"),
    @NamedQuery(name = "CarDetailsEntity.findByCardetBrand", query = "SELECT c FROM CarDetailsEntity c WHERE c.cardetBrand = :cardetBrand"),
    @NamedQuery(name = "CarDetailsEntity.findByCardetBuildyear", query = "SELECT c FROM CarDetailsEntity c WHERE c.cardetBuildyear = :cardetBuildyear"),
    @NamedQuery(name = "CarDetailsEntity.findByCardetPlateno", query = "SELECT c FROM CarDetailsEntity c WHERE c.cardetPlateno = :cardetPlateno"),
    @NamedQuery(name = "CarDetailsEntity.findByCustId", query = "SELECT c FROM CarDetailsEntity c WHERE c.custId = :custId")
})
public class CarDetailsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "cardet_id")
    private Integer cardetId;
    @Column(name = "cardet_colour")
    private String cardetColour;
    @Column(name = "cardet_brand")
    private String cardetBrand;
    @Column(name = "cardet_buildyear")
    private Short cardetBuildyear;
    @Column(name = "cardet_plateno")
    private String cardetPlateno;
    @JoinColumn(name = "cust_id", referencedColumnName = "cust_id")
    @ManyToOne(optional = false)
    private CustomerEntity custId;

    public CarDetailsEntity() {
    }

    public CarDetailsEntity(Integer cardetId) {
        this.cardetId = cardetId;
    }

    public Integer getCardetId() {
        return cardetId;
    }

    public void setCardetId(Integer cardetId) {
        this.cardetId = cardetId;
    }

    public String getCardetColour() {
        return cardetColour;
    }

    public void setCardetColour(String cardetColour) {
        this.cardetColour = cardetColour;
    }

    public String getCardetBrand() {
        return cardetBrand;
    }

    public void setCardetBrand(String cardetBrand) {
        this.cardetBrand = cardetBrand;
    }

    public Short getCardetBuildyear() {
        return cardetBuildyear;
    }

    public void setCardetBuildyear(Short cardetBuildyear) {
        this.cardetBuildyear = cardetBuildyear;
    }

    public String getCardetPlateno() {
        return cardetPlateno;
    }

    public void setCardetPlateno(String cardetPlateno) {
        this.cardetPlateno = cardetPlateno;
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
        hash += (cardetId != null ? cardetId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CarDetailsEntity)) {
            return false;
        }
        CarDetailsEntity other = (CarDetailsEntity) object;
        if ((this.cardetId == null && other.cardetId != null) || (this.cardetId != null && !this.cardetId.equals(other.cardetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.fhg.fokus.openride.customerprofile.CarDetailsEntity[cardetId=" + cardetId + "]";
    }
}
