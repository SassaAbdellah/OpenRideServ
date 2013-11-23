/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.customerprofile;

import de.avci.joride.backing.messages.RegistrationMessage;
import de.avci.joride.utils.EmailCheck;
import de.avci.joride.utils.Messagekeys;
import de.avci.joride.utils.PropertiesLoader;
import java.io.Serializable;
import java.util.Properties;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Models a Request for getting registrated as a user in joride.
 *
 *
 * @author jochen
 */
@Named("registrationRequest")
@RequestScoped
public class JRegistrationRequest implements Serializable {

    /**
     * Normalizing data is outsourced to dedicated class
     */
    private CustomerDataNormalizer normalizer = new CustomerDataNormalizer();
    /**
     * Email Adress for the account to be created
     */
    protected String emailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String arg) {
        this.emailAddress = arg;
    }
    /**
     * GivenName for the account to be created
     */
    protected String givenName;

    public String getGivenName() {
        return this.givenName;
    }

    public void setGivenName(String arg) {
        this.givenName = arg;
    }
    /**
     * SurName for the account to be created
     */
    protected String surName;

    public String getSurName() {
        return this.surName;
    }

    public void setSurName(String arg) {
        this.surName = arg;
    }
    /**
     * Gender for the new Account to be created
     */
    protected char gender;

    public char getGender() {
        return this.gender;
    }

    public void setGender(char arg) {
        this.gender = arg;
    }
    /**
     * Nickname/Username for the account to be created
     */
    protected String nickName;

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String arg) {
        this.nickName = arg;
    }

    /**
     * Add an errormessage to faces context, so it can be evaluated by a growl
     * or message component.
     *
     */
    public void addErrorMessage() {


        String generalError = "TODO: generalError";
        String errorDetails = this.getErrorStatus();

        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorDetails, errorDetails);
        context.addMessage(new Messagekeys().getErrormessagekey(), facesMessage);

    }
    /**
     * An Error status message to be displayed to the user if integrity check
     * fails.
     */
    protected String errorStatus;

    public void setErrorStatus(String arg) {
        this.errorStatus = arg;
    }

    public String getErrorStatus() {
        return this.errorStatus;
    }

    /**
     * Check input for Integrity, add an Error message to Queue if necessary.
     *
     */
    public boolean checkIntegrity() {


        Properties msgs = new PropertiesLoader().getMessagesProps();

        if (this.getGivenName() == null || this.getGivenName().trim().equals("")) {
            this.setErrorStatus(msgs.getProperty("registrationGivenNameMissingError"));
            return false;
        }



        if (this.getSurName() == null || this.getSurName().trim().equals("")) {
            this.setErrorStatus(msgs.getProperty("registrationSurnameMissingError"));
            return false;
        }


        // check if email is well-formed

        if (!(new EmailCheck().isEmailAdress(this.getEmailAddress()))) {
            this.setErrorStatus(msgs.getProperty("registrationEmailInvalid"));
            return false;
        }

        // TODO: check Gender symbol for integrity

        // check, if email already exists, or not
        String normalizedemail = normalizer.normalizeEmailAddress(this.getEmailAddress());
        if (new JCustomerEntityService().emailExists(normalizedemail)) {
            this.setErrorStatus(msgs.getProperty("registrationEmailExist"));
            return false;
        }



        // check that nickname is nonempty

        if (this.getNickName() == null || this.getNickName().trim().equals("")) {
            this.setErrorStatus(msgs.getProperty("registrationNicknameMissingError"));
            return false;
        }


        // check, if email already exists, or not
        String normalizedNickname = normalizer.normalizeNickname(this.getNickName());

        if (new JCustomerEntityService().nicknameExists(normalizedNickname)) {
            this.setErrorStatus(msgs.getProperty("registrationNicknameExist"));
            return false;
        }

        return true;

    }
    protected static final String REQUEST_CREATE = "registration_create";

    
    /** Create a registration with a random password
     * 
     * @return 
     */
    public String createRegistration() {

        if (this.checkIntegrity()) {

            // normalize data
            this.setNickName(normalizer.normalizeNickname(this.getNickName()));
            this.setEmailAddress(normalizer.normalizeEmailAddress(this.getEmailAddress()));
            //
            String passwd=new JCustomerEntityService().createRandomPasswort();
            boolean result = new JCustomerEntityService().addCustomerEntry(this,passwd);

            (new RegistrationMessage()).sendRegistrationMail(this, passwd);
            
            
            
            
            if (result) {
                return REQUEST_CREATE;
            } else {
                Properties msgs = new PropertiesLoader().getMessagesProps();
                this.setErrorStatus(msgs.getProperty("registrationErrorWhilePersisting"));
            }
        }

        this.addErrorMessage();
        return null;
    }
} // class
