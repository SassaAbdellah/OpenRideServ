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

import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideEntity;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author pab
 */
@Entity
@Table(name = "customer")
@NamedQueries({
    @NamedQuery(name = "CustomerEntity.findAll", query = "SELECT c FROM CustomerEntity c"),
    @NamedQuery(name = "CustomerEntity.findByCustId", query = "SELECT c FROM CustomerEntity c WHERE c.custId = :custId"),
    @NamedQuery(name = "CustomerEntity.findByCustAddrZipcode", query = "SELECT c FROM CustomerEntity c WHERE c.custAddrZipcode = :custAddrZipcode"),
    @NamedQuery(name = "CustomerEntity.findByCustAddrCity", query = "SELECT c FROM CustomerEntity c WHERE c.custAddrCity = :custAddrCity"),
    @NamedQuery(name = "CustomerEntity.findByCustNickname", query = "SELECT c FROM CustomerEntity c WHERE c.custNickname = :custNickname"),
    @NamedQuery(name = "CustomerEntity.findByCustDriverprefAge", query = "SELECT c FROM CustomerEntity c WHERE c.custDriverprefAge = :custDriverprefAge"),
    @NamedQuery(name = "CustomerEntity.findByCustFirstname", query = "SELECT c FROM CustomerEntity c WHERE c.custFirstname = :custFirstname"),
    @NamedQuery(name = "CustomerEntity.findByCustDriverprefGender", query = "SELECT c FROM CustomerEntity c WHERE c.custDriverprefGender = :custDriverprefGender"),
    @NamedQuery(name = "CustomerEntity.findByCustDateofbirth", query = "SELECT c FROM CustomerEntity c WHERE c.custDateofbirth = :custDateofbirth"),
    @NamedQuery(name = "CustomerEntity.findByCustDriverprefMusictaste", query = "SELECT c FROM CustomerEntity c WHERE c.custDriverprefMusictaste = :custDriverprefMusictaste"),
  //  @NamedQuery(name = "CustomerEntity.findByCustMobilephoneno", query = "SELECT c FROM CustomerEntity c WHERE c.custMobilephoneno = :custMobilephoneno"),
    @NamedQuery(name = "CustomerEntity.findByCustRiderprefAge", query = "SELECT c FROM CustomerEntity c WHERE c.custRiderprefAge = :custRiderprefAge"),
    @NamedQuery(name = "CustomerEntity.findByCustEmail", query = "SELECT c FROM CustomerEntity c WHERE c.custEmail = :custEmail"),
    @NamedQuery(name = "CustomerEntity.findByCustRiderprefGender", query = "SELECT c FROM CustomerEntity c WHERE c.custRiderprefGender = :custRiderprefGender"),
    @NamedQuery(name = "CustomerEntity.findByCustIssmoker", query = "SELECT c FROM CustomerEntity c WHERE c.custIssmoker = :custIssmoker"),
    @NamedQuery(name = "CustomerEntity.findByCustRiderprefMusictaste", query = "SELECT c FROM CustomerEntity c WHERE c.custRiderprefMusictaste = :custRiderprefMusictaste"),
    @NamedQuery(name = "CustomerEntity.findByCustPostident", query = "SELECT c FROM CustomerEntity c WHERE c.custPostident = :custPostident"),
    @NamedQuery(name = "CustomerEntity.findByCustBankAccount", query = "SELECT c FROM CustomerEntity c WHERE c.custBankAccount = :custBankAccount"),
    @NamedQuery(name = "CustomerEntity.findByCustBankCode", query = "SELECT c FROM CustomerEntity c WHERE c.custBankCode = :custBankCode"),
    @NamedQuery(name = "CustomerEntity.findByCustLastname", query = "SELECT c FROM CustomerEntity c WHERE c.custLastname = :custLastname"),
    @NamedQuery(name = "CustomerEntity.findByCustPresencemssg", query = "SELECT c FROM CustomerEntity c WHERE c.custPresencemssg = :custPresencemssg"),
    @NamedQuery(name = "CustomerEntity.findByCustFixedphoneno", query = "SELECT c FROM CustomerEntity c WHERE c.custFixedphoneno = :custFixedphoneno"),
    @NamedQuery(name = "CustomerEntity.findByCustRegistrdate", query = "SELECT c FROM CustomerEntity c WHERE c.custRegistrdate = :custRegistrdate"),
    @NamedQuery(name = "CustomerEntity.findByCustLicensedate", query = "SELECT c FROM CustomerEntity c WHERE c.custLicensedate = :custLicensedate"),
    @NamedQuery(name = "CustomerEntity.findByCustAccountBalance", query = "SELECT c FROM CustomerEntity c WHERE c.custAccountBalance = :custAccountBalance"),
    @NamedQuery(name = "CustomerEntity.findByCustPasswd", query = "SELECT c FROM CustomerEntity c WHERE c.custPasswd = :custPasswd"),
    @NamedQuery(name = "CustomerEntity.findByCustSessionId", query = "SELECT c FROM CustomerEntity c WHERE c.custSessionId = :custSessionId"),
    @NamedQuery(name = "CustomerEntity.findByCustProfilepic", query = "SELECT c FROM CustomerEntity c WHERE c.custProfilepic = :custProfilepic"),
    @NamedQuery(name = "CustomerEntity.findByIsLoggedIn", query = "SELECT c FROM CustomerEntity c WHERE c.isLoggedIn = :isLoggedIn"),
    @NamedQuery(name = "CustomerEntity.findByCustGender", query = "SELECT c FROM CustomerEntity c WHERE c.custGender = :custGender"),
    @NamedQuery(name = "CustomerEntity.findByCustAddrStreet", query = "SELECT c FROM CustomerEntity c WHERE c.custAddrStreet = :custAddrStreet"),
    @NamedQuery(name = "CustomerEntity.customerCountAll", query = "SELECT COUNT(c.custId) FROM CustomerEntity c")})
public class CustomerEntity implements Serializable {

    public static final char PREF_SMOKER_DESIRED = 'y';
    public static final char PREF_SMOKER_NOT_DESIRED = 'n';
    public static final char PREF_SMOKER_DONT_CARE = '-';
    public static final char PREF_SMOKER_DEFAULT = PREF_SMOKER_DONT_CARE;

    public static final char PREF_GENDER_GIRLS_ONLY = 'f';
    public static final char PREF_GENDER_DONT_CARE = '-';
    public static final char PREF_GENDER_DEFAULT = PREF_GENDER_DONT_CARE;

    public static final char GENDER_MALE = 'm';
    public static final char GENDER_FEMALE = 'f';


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "cust_id")
    private Integer custId;
    @Column(name = "cust_addr_zipcode")
    private String custAddrZipcode;
    @Column(name = "cust_addr_city")
    private String custAddrCity;
    @Column(name = "cust_nickname")
    private String custNickname;
    @Column(name = "cust_firstname")
    private String custFirstname;

    @Column(name = "cust_dateofbirth")
    @Temporal(TemporalType.DATE)
    private Date custDateofbirth;
    @Column(name = "cust_mobilephoneno")
    private String custMobilephoneno;
    @Column(name = "cust_email")
    private String custEmail;
    @Column(name = "cust_issmoker")
    private Boolean custIssmoker;
    @Column(name = "cust_postident")
    private Boolean custPostident;
    @Column(name = "cust_bank_account")
    private Integer custBankAccount;
    @Column(name = "cust_bank_code")
    private Integer custBankCode;
    @Column(name = "cust_lastname")
    private String custLastname;
    @Column(name = "cust_presencemssg")
    private String custPresencemssg;
    @Column(name = "cust_fixedphoneno")
    private String custFixedphoneno;
    @Column(name = "cust_registrdate")
    @Temporal(TemporalType.TIME)
    private Date custRegistrdate;
    @Column(name = "cust_licensedate")
    @Temporal(TemporalType.DATE)
    private Date custLicensedate;
    @Column(name = "cust_account_balance")
    private Double custAccountBalance;
    @Column(name = "cust_passwd")
    private String custPasswd;
    @Column(name = "cust_profilepic")
    private String custProfilepic;
    @Column(name = "cust_gender")
    private Character custGender;
    @Column(name = "is_logged_in")
    private Boolean isLoggedIn;
    @Column(name = "cust_addr_street")
    private String custAddrStreet;
    @Column(name = "cust_session_id")
    private int custSessionId;
    @Column(name = "cust_group")
    private String custGroup;

    /* PREFERENCES */

    @Column(name = "cust_riderpref_smoker")
    private Character custRiderprefIssmoker;
    @Column(name = "cust_driverpref_smoker")
    private Character custDriverprefIssmoker;

    @Column(name = "cust_riderpref_gender")
    private Character custRiderprefGender;
    @Column(name = "cust_driverpref_gender")
    private Character custDriverprefGender;

    @Column(name = "cust_riderpref_musictaste")
    private String custRiderprefMusictaste;
    @Column(name = "cust_driverpref_musictaste")
    private String custDriverprefMusictaste;

    @Column(name = "cust_riderpref_age")
    private Integer custRiderprefAge;
    @Column(name = "cust_driverpref_age")
    private Integer custDriverprefAge;


    @OneToMany(mappedBy = "custId")
    private Collection<CarDetailsEntity> carDetailsEntityCollection;
    @OneToMany(mappedBy = "custId")
    private Collection<DriverUndertakesRideEntity> driverUndertakesRideEntityCollection;
    @OneToMany(mappedBy = "custId")
    private Collection<RiderUndertakesRideEntity> riderUndertakesRideEntityCollection;

    public Collection<RiderUndertakesRideEntity> getRiderUndertakesRideEntityCollection() {
        return riderUndertakesRideEntityCollection;
    }

    public void setRiderUndertakesRideEntityCollection(Collection<RiderUndertakesRideEntity> riderUndertakesRideEntityCollection) {
        this.riderUndertakesRideEntityCollection = riderUndertakesRideEntityCollection;
    }

    public CustomerEntity() {
    }

    public CustomerEntity(String custNickname, String custPasswd, String custFirstname, String custLastname, Date custDateofbirth, char custGender, String custMobilephoneno, String custEmail, boolean custIssmoker, boolean custPostident, String custAddrStreet, String custAddrZipcode, String custAddrCity) {
        this.custAddrZipcode = custAddrZipcode;
        this.custAddrCity = custAddrCity;
        this.custNickname = custNickname;
        this.custFirstname = custFirstname;
        this.custDateofbirth = custDateofbirth;
        this.custMobilephoneno = custMobilephoneno;
        this.custEmail = custEmail;
        this.custIssmoker = custIssmoker;
        this.custPostident = custPostident;
        this.custLastname = custLastname;
        this.custPasswd = custPasswd;
        this.custGender = custGender;
        this.custAddrStreet = custAddrStreet;
    }


    public int getCustSessionId() {
        return custSessionId;
    }

    public void setCustSessionId(int custSessionId) {
        this.custSessionId = custSessionId;
    }

    public Boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
    public Character getCustDriverprefIssmoker() {
        return custDriverprefIssmoker;
    }

    public void setCustDriverprefSmoker(Character custDriverprefIssmoker) {
        this.custDriverprefIssmoker = custDriverprefIssmoker;
    }

    public Character getCustRiderprefIssmoker() {
        return custRiderprefIssmoker;
    }

    public void setCustRiderprefSmoker(Character custRiderprefIssmoker) {
        this.custRiderprefIssmoker = custRiderprefIssmoker;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getCustAddrZipcode() {
        return custAddrZipcode;
    }

    public void setCustAddrZipcode(String custAddrZipcode) {
        this.custAddrZipcode = custAddrZipcode;
    }

    public String getCustAddrCity() {
        return custAddrCity;
    }

    public void setCustAddrCity(String custAddrCity) {
        this.custAddrCity = custAddrCity;
    }

    public String getCustNickname() {
        return custNickname;
    }

    public void setCustNickname(String custNickname) {
        this.custNickname = custNickname;
    }

    public Integer getCustDriverprefAge() {
        return custDriverprefAge;
    }

    public void setCustDriverprefAge(Integer custDriverprefAge) {
        this.custDriverprefAge = custDriverprefAge;
    }

    public String getCustFirstname() {
        return custFirstname;
    }

    public void setCustFirstname(String custFirstname) {
        this.custFirstname = custFirstname;
    }

    public Character getCustDriverprefGender() {
        return custDriverprefGender;
    }

    public void setCustDriverprefGender(Character custDriverprefGender) {
        this.custDriverprefGender = custDriverprefGender;
    }

    public Date getCustDateofbirth() {
        return custDateofbirth;
    }

    public void setCustDateofbirth(Date custDateofbirth) {
        this.custDateofbirth = custDateofbirth;
    }

    public String getCustDriverprefMusictaste() {
        return custDriverprefMusictaste;
    }

    public void setCustDriverprefMusictaste(String custDriverprefMusictaste) {
        this.custDriverprefMusictaste = custDriverprefMusictaste;
    }

    public String getCustMobilephoneno() {
        return custMobilephoneno;
    }

    public void setCustMobilephoneno(String custMobilephoneno) {
        this.custMobilephoneno = custMobilephoneno;
    }

    public Integer getCustRiderprefAge() {
        return custRiderprefAge;
    }

    public void setCustRiderprefAge(Integer custRiderprefAge) {
        this.custRiderprefAge = custRiderprefAge;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public Character getCustRiderprefGender() {
        return custRiderprefGender;
    }

    public void setCustRiderprefGender(Character custRiderprefGender) {
        this.custRiderprefGender = custRiderprefGender;
    }

    public Boolean getCustIssmoker() {
        return custIssmoker;
    }

    public void setCustIssmoker(Boolean custIssmoker) {
        this.custIssmoker = custIssmoker;
    }

    public String getCustRiderprefMusictaste() {
        return custRiderprefMusictaste;
    }

    public void setCustRiderprefMusictaste(String custRiderprefMusictaste) {
        this.custRiderprefMusictaste = custRiderprefMusictaste;
    }

    public Boolean getCustPostident() {
        return custPostident;
    }

    public void setCustPostident(Boolean custPostident) {
        this.custPostident = custPostident;
    }

    public Integer getCustBankAccount() {
        return custBankAccount;
    }

    public void setCustBankAccount(Integer custBankAccount) {
        this.custBankAccount = custBankAccount;
    }

    public Integer getCustBankCode() {
        return custBankCode;
    }

    public void setCustBankCode(Integer custBankCode) {
        this.custBankCode = custBankCode;
    }

    public String getCustLastname() {
        return custLastname;
    }

    public void setCustLastname(String custLastname) {
        this.custLastname = custLastname;
    }

    public String getCustPresencemssg() {
        return custPresencemssg;
    }

    public void setCustPresencemssg(String custPresencemssg) {
        this.custPresencemssg = custPresencemssg;
    }

    public String getCustFixedphoneno() {
        return custFixedphoneno;
    }

    public void setCustFixedphoneno(String custFixedphoneno) {
        this.custFixedphoneno = custFixedphoneno;
    }

    public Date getCustRegistrdate() {
        return custRegistrdate;
    }

    public void setCustRegistrdate(Date custRegistrdate) {
        this.custRegistrdate = custRegistrdate;
    }

    public Date getCustLicensedate() {
        return custLicensedate;
    }

    public void setCustLicensedate(Date custLicensedate) {
        this.custLicensedate = custLicensedate;
    }

    public Double getCustAccountBalance() {
        return custAccountBalance;
    }

    public void setCustAccountBalance(Double custAccountBalance) {
        this.custAccountBalance = custAccountBalance;
    }

    public String getCustPasswd() {
        return custPasswd;
    }

    public void setCustPasswd(String custPasswd) {
        this.custPasswd = custPasswd;
    }

    public String getCustProfilepic() {
        return custProfilepic;
    }

    public void setCustProfilepic(String custProfilepic) {
        this.custProfilepic = custProfilepic;
    }

    public Character getCustGender() {
        return custGender;
    }

    public void setCustGender(Character custGender) {
        this.custGender = custGender;
    }

    public String getCustAddrStreet() {
        return custAddrStreet;
    }

    public void setCustAddrStreet(String custAddrStreet) {
        this.custAddrStreet = custAddrStreet;
    }

    public Collection<CarDetailsEntity> getCarDetailsEntityCollection() {
        return carDetailsEntityCollection;
    }

    public void setCarDetailsEntityCollection(Collection<CarDetailsEntity> carDetailsEntityCollection) {
        this.carDetailsEntityCollection = carDetailsEntityCollection;
    }

    public Collection<DriverUndertakesRideEntity> getDriverUndertakesRideEntityCollection() {
        return driverUndertakesRideEntityCollection;
    }

    public void setDriverUndertakesRideEntityCollection(Collection<DriverUndertakesRideEntity> driverUndertakesRideEntityCollection) {
        this.driverUndertakesRideEntityCollection = driverUndertakesRideEntityCollection;
    }

    public String getCustGroup() {
        return custGroup;
    }

    public void setCustGroup(String cust_group) {
        this.custGroup = cust_group;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (custId != null ? custId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerEntity)) {
            return false;
        }
        CustomerEntity other = (CustomerEntity) object;
        if ((this.custId == null && other.custId != null) || (this.custId != null && !this.custId.equals(other.custId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.fhg.fokus.openride.customerprofile.CustomerEntity[custId=" + custId + "]";
    }

}
