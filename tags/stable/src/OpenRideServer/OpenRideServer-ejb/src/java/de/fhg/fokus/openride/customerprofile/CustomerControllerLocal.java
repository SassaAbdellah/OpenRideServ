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

import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import javax.ejb.Local;

/**
 *
 * @author pab
 */
@Local
public interface CustomerControllerLocal {

    int addCustomer(String custNickname, String custPasswd, String custFirstname, String custLastname, Date custDateofbirth, char custGender, String custMobilephoneno, String custEmail, boolean custIssmoker, boolean custPostident, String custAddrStreet, String custAddrZipcode, String custAddrCity, String preferredLanguage);

    public int addCustomer(String custNickname, String custPasswd, String custFirstname, String custLastname, char custGender, String custEmail, String custMobilephoneno, String preferredLanguage);

    void removeCustomer(int custId);

    CustomerEntity getCustomer(int custId);

    void setCustomer();

    boolean isRegistered(String username, String password);

    //void updateSessionId(String nickanem, String password, String id);
    //boolean isLoggedIn(String nickname);
    CustomerEntity getCustomerByNickname(String nickname);

    CustomerEntity getCustomerByEmail(String email);

    void setPersonalData(int custId, Date custDateofbirth, String custEmail, boolean showEmailToPartners,  String custMobilePhoneNo, boolean showMobileToPartners, String custFixedPhoneNo, String custAddrStreet, String custAddrZipcode, String custAddrCity, char custIssmoker, Date custLicenseDate, String preferredLanguage);

    public void setBasePersonalData(int custId, java.lang.String custFirstName, java.lang.String custLastName, char custGender, String preferredLanguage);

    public void setRiderPrefs(int custId, int custRiderprefAge, char custRiderprefGender, char custRiderprefIssmoker);

    public void setDriverPrefs(int custId, int custDriverprefAge, char custDriverprefGender, char custDriverprefIssmoker);

    public void setPassword(int custId, String custPasswd);
    
    public void setNickname(int custId, String custNicknameArg);

    public boolean isNicknameAvailable(String custNickname);

    public CustomerEntity getCustomerByCredentials(String custNickname, String custPasswd);
    
    public LinkedList<CustomerEntity> getAllCustomers();
    
    /** Set the timestamp of last write access of the customer 
     *  with the given customerId to actual datetime.
     *  Together with setLastCustomerCheck() 
     *  this allows for determinating updates *fast*,
     *  without having to do large (sub) queries.
     * 
     * @param customerId Id of the customer to query
     * @param transactionRequired if true, setting the property will be enclosed in a transaction
     */
    public void setLastMatchingChange(int customerId, boolean transactionRequired);
    
      /** Set the timestamp of last write access of the customer 
     *  with the given customerId to actual datetime.
     *  Together with setLastMatchingChange
     *  this allows for determinating updates *fast*,
     *  without having to do large (sub) queries.
     * 
     * @param customerId Id of the customer to do the query
     * @param transactionRequired  if true, setting the property will be enclosed in a transaction
     */
    public void setLastCustomerCheck(int customerId, boolean transactionRequired);
    
    /** Set customer's lastMatchingCheck property to current date/time,
     *  so that the customer is not notified from  *old* messages any more.
     *  
     * @param customerId  id of the customer for which notifications are to be resetted
     */
    public void resetLastCustomerCheck(int customerId);
    
    /** return true, if there are updates since last customer check
     * 
     *
     * @param customerId  id of the customer to be checked
     * @return 
     */
    public boolean isMatchUpdated(int customerId);
    
    
    /** 
     * @return  Return the list of supported locales
     */
    public Locale[] getSupportedLocales();
    
}
