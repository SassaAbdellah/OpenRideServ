/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.backing.messages;

import de.avci.joride.jbeans.customerprofile.JRegistrationRequest;
import de.avci.joride.utils.PropertiesLoader;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/** Message to be sent when registrating/creating a new account
 * 
 * 
 *
 * @author jochen
 */

@Named
@RequestScoped

public class RegistrationMessage extends MailMessage implements Serializable{
    
        
    
    /** Send message after successful Registration 
     * 
     * 
     */
    public String sendRegistrationMail(JRegistrationRequest jrr, String password) {

        PropertiesLoader pl = new PropertiesLoader();

        this.setSender(pl.getOperationalProps().getProperty(PROPERTY_NAME_WEBMASTER_EMAIL_RECIPIENT));
        this.setRecipient(jrr.getEmailAddress());

        this.setSubject(pl.getMessagesProps().getProperty("registrationMailSubject"));

        String message="\n"+
        pl.getMessagesProps().getProperty("registrationMailText")+"\n"+
        "\n"+
        pl.getMessagesProps().getProperty("custNickname")+":"+
        "\n"+
        "\n"+
        jrr.getNickName()+
        "\n"+
        "\n"+        
        pl.getMessagesProps().getProperty("custPassword")+":"+
        "\n"+
        "\n"+        
        password+
        "\n"+            
        "\n"+
        pl.getMessagesProps().getProperty("registrationMailPasswordHint")+"\n";        
        this.setMessage(message);
        
        
        // finally, send the message...
        
        return this.send();
    }
    
}
