/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.backing.messages;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import de.avci.joride.utils.PropertiesLoader;
import javax.inject.Named;


/** Class providing functionality for sending messages 
 *  from the contact form.
 * 
 * 
 *
 * @author jochen
 */

@Named("contactFormMessage")
@RequestScoped

public class ContactFormMessage extends MailMessage implements Serializable{
    
        
    
    /** Default serial.
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/** Send a message from the contact form.
     *  
     *  
     * 
     * that is: call generic send message with parameters:
     * 
     * sender=webmaster,
     * recipient=businessmail 
     * subject=standard contact form subject
     * 
     */
    public String sendContactFormMail() {

     

        this.setSender(PropertiesLoader.getOperationalProperties().getProperty(PROPERTY_NAME_WEBMASTER_EMAIL_RECIPIENT));
        this.setRecipient(PropertiesLoader.getOperationalProperties().getProperty(PROPERTY_NAME_WEBMASTER_EMAIL_RECIPIENT));

        
        return this.send();
    }
    
    
    /** Dumb default constructor
     * 
     */
    public ContactFormMessage(){
    	super();
    }
    
}
