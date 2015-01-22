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
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author tku
 */
@Entity
@Table(name = "registration_pass")
@NamedQueries({
    @NamedQuery(name = "RegistrationPassEntity.findAll", query = "SELECT r FROM RegistrationPassEntity r"),
    @NamedQuery(name = "RegistrationPassEntity.findAllValid", query = "SELECT r FROM RegistrationPassEntity r WHERE r.usageDate is null ORDER BY r.id ASC"),
    @NamedQuery(name = "RegistrationPassEntity.findAllInvalid", query = "SELECT r FROM RegistrationPassEntity r WHERE r.usageDate is not null ORDER BY r.id DESC"),
    @NamedQuery(name = "RegistrationPassEntity.findById", query = "SELECT r FROM RegistrationPassEntity r WHERE r.id = :id"),
    @NamedQuery(name = "RegistrationPassEntity.findByPasscode", query = "SELECT r FROM RegistrationPassEntity r WHERE r.passcode = :passcode"),
    @NamedQuery(name = "RegistrationPassEntity.findByCreationDate", query = "SELECT r FROM RegistrationPassEntity r WHERE r.creationDate = :creationDate"),
    @NamedQuery(name = "RegistrationPassEntity.findByUsageDate", query = "SELECT r FROM RegistrationPassEntity r WHERE r.usageDate = :usageDate"),
    @NamedQuery(name = "RegistrationPassEntity.findByCustId", query = "SELECT r FROM RegistrationPassEntity r WHERE r.custId = :custId"),
    @NamedQuery(name = "RegistrationPassEntity.countAfterDate", query = "SELECT COUNT(r) FROM RegistrationPassEntity r WHERE r.usageDate >= :date")
})
public class RegistrationPassEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "passcode")
    private String passcode;
    @Basic(optional = false)
    @Column(name = "creation_date")
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    @Column(name = "usage_date")
    @Temporal(TemporalType.DATE)
    private Date usageDate;
    @JoinColumn(name = "cust_id", referencedColumnName = "cust_id")
    @ManyToOne
    private CustomerEntity custId;

    public RegistrationPassEntity() {
    }

    public RegistrationPassEntity(Integer id) {
        this.id = id;
    }

    public RegistrationPassEntity(String passcode, Date creationDate) {
        this.passcode = passcode;
        this.creationDate = creationDate;
    }

    public RegistrationPassEntity(Integer id, String passcode, Date creationDate) {
        this.id = id;
        this.passcode = passcode;
        this.creationDate = creationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(Date usageDate) {
        this.usageDate = usageDate;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegistrationPassEntity)) {
            return false;
        }
        RegistrationPassEntity other = (RegistrationPassEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.fhg.fokus.openride.customerprofile.RegistrationPassEntity[id=" + id + "]";
    }

}
