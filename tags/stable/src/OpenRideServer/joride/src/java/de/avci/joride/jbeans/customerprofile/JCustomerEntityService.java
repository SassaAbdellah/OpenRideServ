/**
 * Service to get and Put CustomerEntityBeans the EJB Way.
 *
 */
package de.avci.joride.jbeans.customerprofile;

import de.avci.joride.utils.HTTPUtil;
import de.fhg.fokus.openride.customerprofile.CustomerControllerBean;
import de.fhg.fokus.openride.customerprofile.CustomerControllerLocal;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

/**
 * Service to get and put JCustomerEntityBeans to the System.
 *
 *
 *
 * @author jochen
 *
 */
public class JCustomerEntityService {
    
    Logger log=Logger.getLogger(this.getClass().getCanonicalName());

    CustomerControllerLocal customerControllerBean = lookupCustomerControllerBeanLocal();

    private CustomerControllerLocal lookupCustomerControllerBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerControllerLocal) c.lookup("java:global/OpenRideServer/OpenRideServer-ejb/CustomerControllerBean!de.fhg.fokus.openride.customerprofile.CustomerControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    /**
     * Initialize the data of an (possibly empty) JCustomerProfile.
     *
     * For safety, it retreives the "Customer.nickname" for updateing the data
     * from the HTTPRequest.userPrincipal property. Thus, no dangerous passing
     * of nicknames in parameters is necessary.
     *
     */
    public CustomerEntity getCustomerEntitySafely() {

        // SecurityMeasure: ensure that the userName is equal to
        // the AuthPrincipal of the Request

        String userName = (new HTTPUtil()).getUserPrincipalName();


        CustomerEntity customerEntity = customerControllerBean.getCustomerByNickname(userName);

        // TODO: do something more sane than just cast
        return customerEntity;

    } // getCustomerEntity

    
    
    
    /**
     * Initialize the data of an (possibly empty) JCustomerProfile,
     * with the data given by nickname.
     *
     * Note that this is not public, as it could be misused to 
     * retrieve personal data for a non-authorized user.
     * This is intended for providing *public* profiles.
     * 
     */
     CustomerEntity getCustomerEntityByNickname(String nick) {

       
        CustomerEntity customerEntity = customerControllerBean.getCustomerByNickname(nick);

       
        return customerEntity;

    } // getCustomerEntity
    
    
    
      /**
     * Initialize the data of an (possibly empty) JCustomerProfile,
     * with the data given by customerID.
     *
     * Note that this is not public, as it could be misused to 
     * retrieve personal data for a non-authorized user.
     * This is intended for providing *public* profiles.
     * 
     */
     CustomerEntity getCustomerEntityByCustId(Integer custId) {

       
        CustomerEntity customerEntity = customerControllerBean.getCustomer(custId);
        return customerEntity;

    } // getCustomerEntity
    
    
    
     
     
    
    
    
    /**
     * Determine a CustomerEntity from a HTTPServletRequest. The user is
     * determined from the request's remoteUser property, and can thus be
     * considered to be safe.
     *
     * @param request request from which the remote user should be read
     * @return
     */
    public CustomerEntity getCustomerEntityFromRequest(HttpServletRequest request) {

        // SecurityMeasure: ensure that the userName is equal to
        // the AuthPrincipal of the Request

        String userName = request.getRemoteUser();


        CustomerEntity customerEntity = customerControllerBean.getCustomerByNickname(userName);

        // TODO: do something more sane than just cast
        return customerEntity;

    } // getCustomerEntity

    /**
     * Read the customer's nickname from current HTTPRequest's Authentication
     * Data, then determine and return the userID. Since it relies on HTTPAuth
     * Information we call it "safe".
     *
     * @return the customer id associated to the current HTTPRequest's Principal
     *
     */
    Integer getCustIDSafely() {

        CustomerEntity ce = getCustomerEntitySafely();

        if (ce == null) {
             log.log(Level.WARNING,"CustomerEntity was null, cannot return custID");
            return null;
        }

        return ce.getCustId();
    } // getCustIdSafely()

    /**
     * Update the personal data persistent data for the given user. As a safety
     * measure, the customer entity's user name is checked against the http
     * request's user name
     *
     */
    public void setCustomerPersonalData(JCustomerEntity jCustomerEntity) {

        // check that nickname of argument bean is equal 
        // to http-request's user name
        String userName = (new HTTPUtil()).getUserPrincipalName();

        if (userName == null) {
            String errmsg = "Refusing to update userdata, httpRequest is not authenticated";
             log.log(Level.SEVERE,errmsg);
            throw new Error(errmsg);
        }

        if (jCustomerEntity == null) {
            String errmsg = "Refusing to update userdata, customerEntity is null";
             log.log(Level.SEVERE,errmsg);
            throw new Error(errmsg);
        }


        String nickname = jCustomerEntity.getCustNickname();

        if (!(userName.equals(nickname))) {
            String errmsg = "Refusing to update userdata, nickname " + nickname + " is not able to http username " + userName;
            log.log(Level.SEVERE,errmsg);
            throw new Error(errmsg);
        }

        // with security checks beeing done,
        // we can proceed to adding data to db        
        CustomerControllerLocal cc = lookupCustomerControllerBeanLocal();

        Integer customerID = this.getCustIDSafely();
        log.log(Level.FINE,"customerID is : " + customerID);

        if (customerID == null) {
            throw new Error("CustomerID is null, cannot save");
        }


        cc.setPersonalData(
                //  int custId
                customerID,
                //Date custDateofbirth 
                jCustomerEntity.getCustDateofbirth(),
                //String custEmail 
                jCustomerEntity.getCustEmail(),
                // data protection for mobile
                jCustomerEntity.getShowEmailToPartners(),
                //String custMobilePhoneNo 
                jCustomerEntity.getCustMobilephoneno(),
                // data protection for mobile
                jCustomerEntity.getShowMobilePhoneToPartners(),
                //String custFixedPhoneNo 
                jCustomerEntity.getCustFixedphoneno(),
                //String custAddrStreet
                jCustomerEntity.getCustAddrStreet(),
                //int custAddrZipcode 
                jCustomerEntity.getCustAddrZipcode(),
                //String custAddrCity 
                jCustomerEntity.getCustAddrCity(),
                //char custIssmoker
                jCustomerEntity.getCustSmoker().charAt(0),
                //Date custLicenseDate);
                jCustomerEntity.getCustLicensedate(),
        		// String preferredLanguage
                jCustomerEntity.getPreferredLanguage()
        		); // end of method call to setCustomerPersonalData
    }

    /**
     * Update the driver preferences for the given user. As a safety measure,
     * the customer entity's user name is checked against the http request's
     * user name
     *
     */
    public void setCustDriverPrefs(JCustomerEntity jCustomerEntity) {

        // check that nickname of argument bean is equal 
        // to http-request's user name
        String userName = (new HTTPUtil()).getUserPrincipalName();

        if (userName == null) {
            String errmsg = "Refusing to update userdata, httpRequest is not authenticated";
            log.log(Level.SEVERE,errmsg);
            throw new Error(errmsg);
        }

        if (jCustomerEntity == null) {
            String errmsg = "Refusing to update userdata, customerEntity is null";
            log.log(Level.SEVERE,errmsg);
            throw new Error(errmsg);
        }




        // with security checks beeing done,
        // we can proceed to adding data to db        
        CustomerControllerLocal cc = lookupCustomerControllerBeanLocal();

        Integer customerID = this.getCustIDSafely();
        log.log(Level.WARNING,"customerID is : " + customerID);

        if (customerID == null) {
            throw new Error("CustomerID is null, cannot save");
        }


        cc.setDriverPrefs(
                //  int custId
                customerID,
                // int custDriverprefAge is set to 0
                0,
                // char custDriverprefGender is set to '-'
                '-',
                // char custDriverprefSmoker) 
                jCustomerEntity.getCustDriverprefSmoker()); // end of method call to setCustomerDriverPrefs

    }

    /**
     * Update the rider preferences for the given user. As a safety measure, the
     * customer entity's user name is checked against the http request's user
     * name
     *
     */
    public void setCustRiderPrefs(JCustomerEntity jCustomerEntity) {

        // check that nickname of argument bean is equal 
        // to http-request's user name
        String userName = (new HTTPUtil()).getUserPrincipalName();

        if (userName == null) {
            String errmsg = "Refusing to update userdata, httpRequest is not authenticated";
            log.log(Level.SEVERE,errmsg);
            throw new Error(errmsg);
        }

        if (jCustomerEntity == null) {
            String errmsg = "Refusing to update userdata, customerEntity is null";
            log.log(Level.SEVERE,errmsg);
            throw new Error(errmsg);
        }




        // with security checks beeing done,
        // we can proceed to adding data to db        
        CustomerControllerLocal cc = lookupCustomerControllerBeanLocal();

        Integer customerID = this.getCustIDSafely();
        log.log(Level.FINE,"customerID is : " + customerID);

        if (customerID == null) {
            throw new Error("CustomerID is null, cannot save");
        }


        cc.setRiderPrefs(
                //  int custId
                customerID,
                // int custRiderprefAge is fixed to '0'
                0,
                // char custRiderprefGender is fixed to '-'
                '-',
                // char custRiderprefSmoker) 
                jCustomerEntity.getCustRiderprefIssmoker()); // end of method call to setCustomerDriverPrefs

    }

    /**
     * Returns true, if an account with that email address already exists in the
     * db, else false.
     *
     * @param email email adress to be checked
     *
     * @return true if account exists for given email address, else false
     */
    public boolean emailExists(String email) {

        // with security checks beeing done,
        // we can proceed to adding data to db        
        CustomerControllerLocal cc = lookupCustomerControllerBeanLocal();
        CustomerEntity ce = cc.getCustomerByEmail(email);

        return ce != null;

    } // email exists
    
    
    
    /**
     * Returns true, if an account with that email address already exists in the
     * db, else false.
     *
     * @param email email adress to be checked
     *
     * @return nickname if account exists for given email address, else null
     */
    public String getNicknameByEmail(String email) {

        // with security checks beeing done,
        // we can proceed to adding data to db        
        CustomerControllerLocal cc = lookupCustomerControllerBeanLocal();
        CustomerEntity ce = cc.getCustomerByEmail(email);

        if(ce!=null){
        	return ce.getCustNickname();
        }

        return null;
    } // email exists
    
    
    
    

    /**
     * Returns true, if an account with that nickname address already exists in
     * the db, else false.
     *
     * @param nickname to be checked
     *
     * @return true if account exists for given email address, else false
     */
    public boolean nicknameExists(String nickname) {

        // with security checks beeing done,
        // we can proceed to adding data to db        
        CustomerControllerLocal cc = lookupCustomerControllerBeanLocal();
        CustomerEntity ce = cc.getCustomerByNickname(nickname);

        return ce != null;

    } // nickname exists

    /**
     * Creates a random password from date and random.
     *
     * @return a random password
     */
    public String createRandomPasswort() {

        // create a random password
        String random1 = "" + Math.random() + new java.util.Date();
        String random2 = CustomerControllerBean.getMD5Hash(random1);
        if (random2.length() > 9) {
            return random2.substring(random2.length() - 8);
        }

        return random2;
    }

    /**
     *
     * @param jrr
     * @param password
     * @return
     */
    public boolean addCustomerEntry(JRegistrationRequest jrr, String password) {


        try {
            CustomerControllerLocal cc = this.lookupCustomerControllerBeanLocal();



            // TODO: Send email to caller's address


            // Create a customer Account
            
            int result=
            cc.addCustomer(
                    //String custNickname
                    jrr.getNickName(),
                    //String custPasswd
                    password,
                    //String custFirstname
                    jrr.getGivenName(),
                    // String custLastname,
                    jrr.getSurName(),
                    // char custGender  -- gender may be set later, or let open
                    (new JCustomerEntity().getGenderOther()),
                    //String custEmail
                    jrr.getEmailAddress(),
                    //String custMobilephoneno mobile phone may be added later
                    null,
                    // String preferredLanguage
                    jrr.getPreferredLanguage()	
            	);
            
            
            
            
            
        } catch (Exception exc) {
            return false;
        }

        return true;
    }

    /**
     * Get the customerId for given email.
     *
     * @return CustomerId for
     */
    Integer getCustomerIdByEmail(String email) {

        CustomerControllerLocal ccl = this.lookupCustomerControllerBeanLocal();

        CustomerEntity ce = ccl.getCustomerByEmail(email);

        if (ce == null) {
            return null;
        }
        return new Integer(ce.getCustId());
    }
    
    
    /** Resets the password for the given password ID
     * 
     * @param custId
     * @return the new password 
     */
    public String resetPassword(Integer custId) throws Exception {
 
       CustomerControllerLocal ccl=this.lookupCustomerControllerBeanLocal();
       String newPassword=this.createRandomPasswort();
       ccl.setPassword(custId, newPassword);
       return newPassword;      
    }

    /** return the Customer Nickname for a given email address.
     *  (this is) needed for the reset password usecase
     * 
     * @param email
     * @return nickname of the customer for the given email, or null if there is no such customer
     */
    String getCustomerNicknameByEmail(String email) {
        
        CustomerControllerLocal ccl=this.lookupCustomerControllerBeanLocal();
        CustomerEntity ce=ccl.getCustomerByEmail(email);
        if(ce==null) {return null;}
     
        return ce.getCustNickname();   
    }

    
    
    /** Safely change password for current user.
     *  Current user is determined from HTTPRequest AuthPrincipal
     * 
     * @param newpassword 
     */
    void setPasswordSafely(String newpassword) {
        
        // determine current customer from http-request
        Integer custId=this.getCustIDSafely();
        
        //
        if(custId==null){
            throw new Error("Cannot determine Id from HTTPRequest");
        }
        
        CustomerControllerLocal ccl=this.lookupCustomerControllerBeanLocal();
        ccl.setPassword(custId, newpassword);
        
        
    }
    
    
    
    /** Invalidate account of the current customer.
     *  Customer is determined using the 
     *  http remote uid
     * 
     * @return true, if all went well.
     * 
     */
    public boolean invalidateCustomerAccount(){
    
    
        CustomerEntity ce=this.getCustomerEntitySafely();
        CustomerControllerLocal ccl=this.lookupCustomerControllerBeanLocal();
        
        if(ce!=null){
            ccl.removeCustomer(ce.getCustId());
        } else { 
        
            throw new Error("Cannot invalidate customer, customerID ist null");
        
        }
        
        return true;
    }
    
    
      
    /** Invalidate account of the current customer.
     *  Customer is determined using the 
     *  http remote uid
     * 
     * @return true, if all went well.
     * 
     */
    public void resetLastCustomerCheck(){
    
    
        CustomerEntity ce=this.getCustomerEntitySafely();
        CustomerControllerLocal ccl=this.lookupCustomerControllerBeanLocal();
        
        if(ce!=null){  ccl.resetLastCustomerCheck(ce.getCustId());
        } else { 
            throw new Error("Cannot resetLastCustomerCheck, customerID ist null");
        }
    }
    
   
    /** 
     *   @return  true, if calling customer has updated matches, else false.
     */
    public boolean isMatchUpdated(){
   
        CustomerEntity ce=this.getCustomerEntitySafely();
        // this may happen before login
        if(ce==null) {return false;}
        
        CustomerControllerLocal ccl=this.lookupCustomerControllerBeanLocal();
       return ccl.isMatchUpdated(ce.getCustId());
    
    }
    
    
    
    /** Return the list of locales supported by the server
     */
    public Locale[] getSupportedLocales(){
    	
    	 CustomerControllerLocal ccl=this.lookupCustomerControllerBeanLocal();
         return ccl.getSupportedLocales();
    }
    
    
} // class
