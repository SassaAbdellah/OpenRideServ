/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.customerprofile;

import de.avci.joride.backing.messages.PasswordResetMesssage;
import de.avci.joride.utils.Messagekeys;
import de.avci.joride.utils.PropertiesLoader;
import java.io.Serializable;
import java.util.Properties;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * JSF Bean modelling a request to change a user password. Comes with a second
 * input property to check for spelling errors. Eventually it can provide a hook
 * to add password policies.
 *
 * @author jochen
 */
@Named("passwordChangeRequest")
@RequestScoped
public class JPasswordChangeRequest implements Serializable {

    /**
     * String to be returned when a password request is successful;
     *
     */
    protected final String PASSWORD_CHANGE_OK = "password_change_ok";
    /**
     * An errormessage, if things do not work out as expected.
     */
    private String errorStatus;

    public String getErrorStatus() {
        return this.errorStatus;
    }

    public void setErrorStatus(String arg) {
        this.errorStatus = arg;
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
     * new Password to be set by the user
     */
    private String password = null;

    public String getPassword() {
        return password;
    }

    public void setPassword(String arg) {
        this.password = arg;
    }
    /**
     * Second password input, to provide spelling check
     */
    private String verification = null;

    public String getVerification() {
        return verification;
    }

    public void setVerification(String arg) {
        this.verification = arg;
    }

    /**
     * Check if passwords are nonempty, and the two inputs do match each other.
     *
     * This is also where eventually password policies may be implemented.
     *
     * @return
     */
    public boolean checkPassword() {

        if (this.getPassword() == null) {

            this.setErrorStatus("passwordIsEmpty");
            return false;
        }


        if (this.getPassword().trim().equals("")) {

            this.setErrorStatus("passwordIsEmpty");
            return false;
        }


        if (this.getVerification() == null) {

            this.setErrorStatus("passwordVerificationFieldIsEmpty");
            return false;
        }


        if (this.getVerification().trim().equals("")) {

            this.setErrorStatus("passwordVerificationFieldIsEmpty");
            return false;
        }



        String pw = this.getPassword().trim();
        String ve = this.getVerification().trim();

        if (!(pw.equals(ve))) {
            this.setErrorStatus("passwordVerificationFieldIsEmpty");
            return false;
        }

        return true;
    }

    /**
     * Reset the password, send message to the customer
     *
     * @return "resetPassword_OK" if the password has been reset, null else
     */
    public String changePassword() {

        JCustomerEntityService jces = new JCustomerEntityService();

        if (!this.checkPassword()) {
            this.addErrorMessage();
            return null;
        }

        try {
            jces.setPasswordSafely(this.getPassword());
            return PASSWORD_CHANGE_OK;
        } catch (Exception exc) {            
            this.setErrorStatus("Unexpected Exception while setting Password\n"+exc.getLocalizedMessage());
            this.addErrorMessage();
            return null;
        }
        
    } // change Password
    
} // class
