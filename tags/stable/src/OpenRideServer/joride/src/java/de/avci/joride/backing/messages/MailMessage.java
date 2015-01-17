package de.avci.joride.backing.messages;

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.avci.joride.utils.EmailCheck;
import de.avci.joride.utils.HTTPUtil;
import de.avci.joride.utils.Messagekeys;
import de.avci.joride.utils.PropertiesLoader;
import de.fhg.fokus.openride.customerprofile.CustomerUtils;

/**
 * Generic class for sending mail messages.
 * This may be subclassed for special purposes,
 * such as sending messages from contact form,
 * sending messages from rider to driver
 * and vice versa, 
 * sending messages for password reset and
 * registration, etc....
 *
 * @author jochen
 */

public class MailMessage implements Serializable {

    /**
     * Sender of this message
     * 
     */
    protected String sender = null;
   
    /**
     * Recipient of Message to be sent by this bean
     */
    protected String recipient = null;
  
    
    /**
     * A status that may be displayed after sending mail.
     */
    protected String errorstatus = null;

    public String getErrorstatus() {
        return errorstatus;
    }

  

    /**
     * lookup MailService from JNDI Adress
     */
    public Session lookupMailSession() throws NamingException {
        Context ctx = new InitialContext();
        return (Session) ctx.lookup(loadMailServiceJNDI());
    }
    /**
     * Outcome from send method if everything seemed to work OK
     */
    private String SENT_OK = "SENT_OK";

    /**
     * Add an errormessage to faces context, so it can be evaluated by a growl
     * or message component.
     *
     */
    public void addErrorMessage() {


        String generalError = "TODO: generalError";
        String errorDetails = this.getErrorstatus();

        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorDetails, errorDetails);
        context.addMessage(new Messagekeys().getErrormessagekey(), facesMessage);

    }
    
   
    
    public String getSender(){
        return this.sender;
    }
    
    public void setSender(String arg){
        this.sender=arg;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public void setRecipient(String arg) {
        this.recipient = arg;
    }
    /**
     * Subject of Message to be sent by this bean
     */
    protected String subject = null;

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String arg) {
        this.subject = arg;
    }
    /**
     * Message to be sent by this bean
     */
    protected String message = null;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String arg) {
        this.message = arg;
    }

    /** Generic send method. 
     * 
     * Send this message. Returns "sent_ok" or "sent_error" depending on wether
     * an error happened or not. a more detailled error record may be obtained
     * by evaluating the errorstatus property
     *
     *
     * @return "sent_ok" or "sent_error" depending on wether an error happened
     * or not
     *
     */
    protected String send() {

        Logger logger = Logger.getLogger(MailMessage.class.getName());

        MimeMessage msg;
        // lookup Mail service

        try {
            Session mailSession = this.lookupMailSession();
            msg = new MimeMessage(mailSession);
        } catch (Exception exc) {
            errorstatus = exc.getLocalizedMessage();
            addErrorMessage();
            return null;
        }

        //
        EmailCheck echeck = new EmailCheck();
        
        Properties messages = PropertiesLoader.getMessageProperties(new HTTPUtil().detectBestLocale());

        //
        // Checking sender
        //
        if (!(CustomerUtils.isValidEmailAdress(this.getSender()))) {
            errorstatus = messages.getProperty("mailEmailAdressInvalid") + " : \n" + sender;
            addErrorMessage();
            return null;
        }

        InternetAddress senderAddr = null;
        try {
            senderAddr = InternetAddress.parse(sender)[0];
        } catch (AddressException ex) {
            logger.log(
                    Level.SEVERE,
                    "error while sending mail (sender address invalid) " + sender,
                    ex);
            errorstatus = messages.getProperty("mailEmailAdressInvalid") + " : \n" + sender;
            addErrorMessage();
            return null;
        }

        try {
            msg.setSender(senderAddr);
        } catch (MessagingException ex) {
            logger.log(
                    Level.SEVERE,
                    "error while sending mail (sender address invalid) " + sender,
                    ex);
            errorstatus = messages.getProperty("mailEmailAdressInvalid") + " : " + sender;
            addErrorMessage();
            return null;
        }

        //  
        // done with checking sender
        // 
        // Checking recipients
        //
        InternetAddress[] recAddresses = echeck.getEmailAddresses(recipient);

        if (recAddresses.length == 0) {
            logger.log(
                    Level.SEVERE,
                    "error while sending mail (sender address invalid) " + recipient);
            errorstatus = messages.getProperty("mailRecipientInvalid") + " : " + recipient;
            addErrorMessage();
            return null;
        }

        try {
            msg.setRecipients(Message.RecipientType.TO, recAddresses);
        } catch (MessagingException exc) {
            logger.log(
                    Level.SEVERE,
                    "error while sending mail (sender address invalid) " + recipient,
                    exc);
            errorstatus = messages.getProperty("mailRecipientInvalid") + " : " + recipient;
            addErrorMessage();
            return null;
        }


        try {
            msg.setSubject(this.getSubject());
            msg.setText(""+this.getMessage());
            msg.setSentDate(new Date());
            Transport.send(msg);

        } catch (MessagingException exc) {

            logger.log(
                    Level.SEVERE,
                    "generic mail error while setting subject, text or date on mail to " + recipient,
                    exc);

            errorstatus = messages.getProperty("mailFailure");
            addErrorMessage();
            return null;
        }

        // if we made it here, all went well!
        return SENT_OK;
    } //send()

    /**
     * Load the JNDI Name of the Mail Service
     *
     * @return
     */
    private String loadMailServiceJNDI() {

    	 return PropertiesLoader.getOperationalProperties().getProperty(PROPERTY_NAME_MAIL_SERVICE_JNDI);
    }
    
    
    
    /**
     * Property Name under which the "noreply email recipient" 
     * is to be found in the Properties
     * 
     */
    protected String PROPERTY_NAME_NOREPLY_EMAIL_RECIPIENT = "noreplyEmailRecipient";
    

        
    /**
     * Property Name under which the "webmaster email recipient" 
     * is to be found in the Properties
     */
    protected String PROPERTY_NAME_WEBMASTER_EMAIL_RECIPIENT = "webmasterEmailRecipient";
    
    /**
     * Property Name under which the "business email recipient" 
     * is to be found in the Properties
     */
    protected String PROPERTY_NAME_BUSINESS_EMAIL_RECIPIENT = "businessEmailRecipient";
    
    
    
    /**
     * Property Name for properties which gives the jndi name 
     * of the mail service
     * 
     */
    protected String PROPERTY_NAME_MAIL_SERVICE_JNDI = "mailServiceJNDI";

    
    

} // class
