package de.avci.openrideshare.mail;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.avci.openrideshare.utils.PropertiesLoader;

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

	static Log log=LogFactory.getLog(MailMessage.class);


 
    /**
     * lookup MailService from JNDI Adress
     */
    public static Session lookupMailSession() throws NamingException {
        Context ctx = new InitialContext();
        return (Session) ctx.lookup(loadMailServiceJNDI());
    }
  

 
    
   
    
    /** Send a message by email
     * 
     * @param sender      email address of the sender
     * @param recipient   email address of recipient
     * @param subject     subject of the mail
     * @param message     message to be sent
     * 
     * @return true if all went well, else false
     */
    public static boolean send(
    	
    		String sender,
    		String recipient,
    		String subject,
    		String message
    		
    		) {

        Logger logger = Logger.getLogger(MailMessage.class.getName());

        MimeMessage msg;
        // lookup Mail service

        try {
            Session mailSession = lookupMailSession();
            msg = new MimeMessage(mailSession);
        } catch (Exception exc) {
            log.error(exc.getLocalizedMessage());
            return false;
        }

        

        InternetAddress senderAddr = null;
        try {
            senderAddr = InternetAddress.parse(sender)[0];
        } catch (AddressException ex) {
            logger.log(
                    Level.SEVERE,
                    "error while sending mail (sender address invalid) " + sender,
                    ex);
            return false;
        }

        try {
            msg.setSender(senderAddr);
        } catch (MessagingException ex) {
            logger.log(
                    Level.SEVERE,
                    "error while sending mail (problem setting sender : " + sender+")",
                    ex);
            
            return false;
        }

        //  
        // done with checking sender
        // 
        // Checking recipients
        //
        InternetAddress[] recAddresses=null;
		try { recAddresses = InternetAddress.parse(recipient);
		} catch (AddressException exc) {
			log.error("cannot sent mail, error: "+exc.getMessage(), exc);
		}
        if(recAddresses==null){
        	log.error("cannot sent mail to recipient: "+recipient+" address evaluated to null");
        	return false;
        }
        
        if(recAddresses.length==0){
        	log.error("cannot sent mail to recipient: "+recipient+" address evaluated to zero length");
        	return false;
        }
        
        
        try {
            msg.setRecipients(Message.RecipientType.TO, recAddresses);
        } catch (MessagingException exc) {
            logger.log(
                    Level.SEVERE,
                    "error while sending mail (recipient address invalid) " + recipient,
                    exc);
          
            return false;
        }


        try {
            msg.setSubject(subject);
            msg.setText(message);
            msg.setSentDate(new Date());
            Transport.send(msg);

        } catch (MessagingException exc) {

            logger.log(
                    Level.SEVERE,
                    "generic mail error while setting subject, text or date on mail to " + recipient,
                    exc);

           
            return false;
        }

        // if we made it here, all went well!
        return true;
    } //send()

    /**
     * Load the JNDI Name of the Mail Service
     *
     * @return
     */
    private static String loadMailServiceJNDI() {

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
    protected static String PROPERTY_NAME_MAIL_SERVICE_JNDI = "mailServiceJNDI";

    
    

} // class
