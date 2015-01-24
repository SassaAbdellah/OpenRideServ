/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.customerprofile;

import de.avci.joride.backing.messages.PasswordResetMesssage;
import de.avci.joride.utils.HTTPUtil;
import de.avci.joride.utils.Messagekeys;
import de.avci.joride.utils.PropertiesLoader;
import java.io.Serializable;
import java.util.Locale;
import java.util.Properties;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * JSF Bean modelling a password reset request. I.e: user requests to reset his
 * password.
 *
 * @author jochen
 */
@Named("passwordResetRequest")
@RequestScoped
public class JPasswordResetRequest implements Serializable {
    
    
    
    /** String to be returned when a password request is successful;
     * 
     */
    protected final String PASSWORD_REQUEST_OK="password_request_ok";
    
    

    /**
     * Email property. Account to be reset will be determined by email.
     *
     */
    private String email;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String arg) {
        this.email = arg;
    }
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
     * Reset the password, send message to the customer
     *
     * @return "resetPassword_OK" if the password has been reset, null else
     */
    public String resetPassword() {

        JCustomerEntityService jces = new JCustomerEntityService();

        // 
        if (!(jces.emailExists(this.getEmail()))) {

        	Locale locale=new HTTPUtil().detectBestLocale();
            this.setErrorStatus(PropertiesLoader.getMessageProperties(locale).getProperty("resetPasswortNoSuchAccountError"));
            this.addErrorMessage();

            return null;
        }


        try {
            Integer custId = jces.getCustomerIdByEmail(email);
            String newPasswd=jces.resetPassword(custId);
            
            String username=jces.getCustomerNicknameByEmail(email);
            
            
            

            PasswordResetMesssage pwm=new PasswordResetMesssage();
            pwm.sendPasswordResetMail(this, username, newPasswd);
            
           
            // return the password_request_ok message to signify 
            // that the password has been reset
            
            return PASSWORD_REQUEST_OK;
           
        } catch (Exception exc) {

            this.setErrorStatus(
                    "An unexpected Error happened : \n"
                    + exc.getLocalizedMessage());
            this.addErrorMessage();
            return null;
        }
    }
}
