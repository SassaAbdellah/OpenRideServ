package de.avci.joride.restful.dto.customers;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

import de.fhg.fokus.openride.customerprofile.CarDetailsEntity;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideEntity;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;


/** DTO to describe a CustomerEntity
 * 
 * @author jochen
 *
 */
public class CustomerDTO {

	
    private Integer custId;
 
    private String custAddrZipcode;
 
    private String custAddrCity;
 
    private String custNickname;
    
    private String custFirstname;
   
    private Date custDateofbirth;
 
    private String custMobilephoneno;
  
    private String custEmail;
   
    private Boolean custIssmoker;
 
    private Boolean custPostident;
 
    private Integer custBankAccount;
  
    private Integer custBankCode;
 
    private String custLastname;
  
    private String custPresencemssg;
    
    private String custPreferredLanguage;
 
    /** Landline Phone -- for whatever reason
     */
    private String custFixedphoneno;
   
   
    private Date custRegistrdate;

  
    private Date custLicensedate;
  
    private Double custAccountBalance;
  
    private String custPasswd;
 
    /** Profile Picture. Currently unused
     */
    private String custProfilepic;

    private Character custGender;
  
    private Boolean isLoggedIn;
  
    private String custAddrStreet;
  
    private int custSessionId;

    private String custGroup;
 
    private Timestamp custLastCheck;
  
    private Timestamp custLastMatchingChange;
    /* PREFERENCES */
  
    private Character custRiderprefIssmoker;
   
    private Character custDriverprefIssmoker;
 
    private Character custRiderprefGender;
   

    private Character custDriverprefGender;
  
    private String custRiderprefMusictaste;
  
    private String custDriverprefMusictaste;
  
    private Integer custRiderprefAge;
 
    private Integer custDriverprefAge;
 
  
   
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

	public String getCustFirstname() {
		return custFirstname;
	}

	public void setCustFirstname(String custFirstname) {
		this.custFirstname = custFirstname;
	}

	public Date getCustDateofbirth() {
		return custDateofbirth;
	}

	public void setCustDateofbirth(Date custDateofbirth) {
		this.custDateofbirth = custDateofbirth;
	}

	public String getCustMobilephoneno() {
		return custMobilephoneno;
	}

	public void setCustMobilephoneno(String custMobilephoneno) {
		this.custMobilephoneno = custMobilephoneno;
	}

	public String getCustEmail() {
		return custEmail;
	}

	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}

	public Boolean getCustIssmoker() {
		return custIssmoker;
	}

	public void setCustIssmoker(Boolean custIssmoker) {
		this.custIssmoker = custIssmoker;
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

	public Boolean getIsLoggedIn() {
		return isLoggedIn;
	}

	public void setIsLoggedIn(Boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public String getCustAddrStreet() {
		return custAddrStreet;
	}

	public void setCustAddrStreet(String custAddrStreet) {
		this.custAddrStreet = custAddrStreet;
	}

	public int getCustSessionId() {
		return custSessionId;
	}

	public void setCustSessionId(int custSessionId) {
		this.custSessionId = custSessionId;
	}

	public String getCustGroup() {
		return custGroup;
	}

	public void setCustGroup(String custGroup) {
		this.custGroup = custGroup;
	}

	public Timestamp getCustLastCheck() {
		return custLastCheck;
	}

	public void setCustLastCheck(Timestamp custLastCheck) {
		this.custLastCheck = custLastCheck;
	}

	public Timestamp getCustLastMatchingChange() {
		return custLastMatchingChange;
	}

	public void setCustLastMatchingChange(Timestamp custLastMatchingChange) {
		this.custLastMatchingChange = custLastMatchingChange;
	}

	public Character getCustRiderprefIssmoker() {
		return custRiderprefIssmoker;
	}

	public void setCustRiderprefIssmoker(Character custRiderprefIssmoker) {
		this.custRiderprefIssmoker = custRiderprefIssmoker;
	}

	public Character getCustDriverprefIssmoker() {
		return custDriverprefIssmoker;
	}

	public void setCustDriverprefIssmoker(Character custDriverprefIssmoker) {
		this.custDriverprefIssmoker = custDriverprefIssmoker;
	}

	public Character getCustRiderprefGender() {
		return custRiderprefGender;
	}

	public void setCustRiderprefGender(Character custRiderprefGender) {
		this.custRiderprefGender = custRiderprefGender;
	}

	public Character getCustDriverprefGender() {
		return custDriverprefGender;
	}

	public void setCustDriverprefGender(Character custDriverprefGender) {
		this.custDriverprefGender = custDriverprefGender;
	}

	public String getCustRiderprefMusictaste() {
		return custRiderprefMusictaste;
	}

	public void setCustRiderprefMusictaste(String custRiderprefMusictaste) {
		this.custRiderprefMusictaste = custRiderprefMusictaste;
	}

	public String getCustDriverprefMusictaste() {
		return custDriverprefMusictaste;
	}

	public void setCustDriverprefMusictaste(String custDriverprefMusictaste) {
		this.custDriverprefMusictaste = custDriverprefMusictaste;
	}

	public Integer getCustRiderprefAge() {
		return custRiderprefAge;
	}

	public void setCustRiderprefAge(Integer custRiderprefAge) {
		this.custRiderprefAge = custRiderprefAge;
	}

	public Integer getCustDriverprefAge() {
		return custDriverprefAge;
	}

	public void setCustDriverprefAge(Integer custDriverprefAge) {
		this.custDriverprefAge = custDriverprefAge;
	}



	public String getCustPreferredLanguage() {
		return custPreferredLanguage;
	}

	public void setCustPreferredLanguage(String custPreferredLanguage) {
		this.custPreferredLanguage = custPreferredLanguage;
	}


	
	

}
