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
import javax.ejb.Local;

/**
 *
 * @author pab
 */
@Local
public interface CustomerControllerLocal {

    int addCustomer(String custNickname, String custPasswd, String custFirstname, String custLastname, Date custDateofbirth, char custGender, String custMobilephoneno, String custEmail, boolean custIssmoker, boolean custPostident, String custAddrStreet, String custAddrZipcode, String custAddrCity);

    public int addCustomer(String custNickname, String custPasswd, String custFirstname, String custLastname, char custGender, String custEmail, String custMobilephoneno);

    void removeCustomer(int custId);

    CustomerEntity getCustomer(int custId);

    void setCustomer();

    boolean isRegistered(String username, String password);

    //void updateSessionId(String nickanem, String password, String id);
    //boolean isLoggedIn(String nickname);
    CustomerEntity getCustomerByNickname(String nickname);

    CustomerEntity getCustomerByEmail(String email);

    void setPersonalData(int custId, Date custDateofbirth, String custEmail, String custMobilePhoneNo, String custFixedPhoneNo, String custAddrStreet, String custAddrZipcode, String custAddrCity, char custIssmoker, Date custLicenseDate);

    public void setBasePersonalData(int custId, java.lang.String custFirstName, java.lang.String custLastName, char custGender);

    public void setRiderPrefs(int custId, int custRiderprefAge, char custRiderprefGender, char custRiderprefIssmoker);

    public void setDriverPrefs(int custId, int custDriverprefAge, char custDriverprefGender, char custDriverprefIssmoker);

    public void setPassword(int custId, String custPasswd);
    
    public void setNickname(int custId, String custNicknameArg);

    public boolean isNicknameAvailable(String custNickname);

    public CustomerEntity getCustomerByCredentials(String custNickname, String custPasswd);

    public LinkedList<CustomerEntity> getAllCustomers();
}
