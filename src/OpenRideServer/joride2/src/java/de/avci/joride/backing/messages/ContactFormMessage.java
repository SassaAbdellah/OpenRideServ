/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.backing.messages;

import de.avci.joride.utils.PropertiesLoader;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/** Class providing functionality for sending messages 
 *  from the contact form.
 * 
 * 
 *
 * @author jochen
 */

@Named
@RequestScoped

public class ContactFormMessage extends MailMessage implements Serializable{
    
        
    
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

        PropertiesLoader pl = new PropertiesLoader();

        this.setSender(pl.getOperationalProps().getProperty(PROPERTY_NAME_WEBMASTER_EMAIL_RECIPIENT));
        this.setRecipient(pl.getOperationalProps().getProperty(PROPERTY_NAME_WEBMASTER_EMAIL_RECIPIENT));

        
        return this.send();
    }
    
}
