/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.backing.messages;

import de.avci.joride.jbeans.customerprofile.JPasswordResetRequest;
import de.avci.joride.jbeans.customerprofile.JRegistrationRequest;
import de.avci.joride.utils.PropertiesLoader;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/** Message to be sent when an account's password is reset
 * 
 * 
 *
 * @author jochen
 */

@Named
@RequestScoped

public class PasswordResetMesssage extends MailMessage implements Serializable{
    
        
   
    
    /** Send message after successful Registration 
     * 
     * 
     */
    public String sendPasswordResetMail(JPasswordResetRequest jprr, String nickname, String password) {

        PropertiesLoader pl = new PropertiesLoader();

        this.setSender(pl.getOperationalProps().getProperty(PROPERTY_NAME_WEBMASTER_EMAIL_RECIPIENT));
        this.setRecipient(jprr.getEmail());

        this.setSubject(pl.getMessagesProps().getProperty("passwordResetMailSubject"));

        String message="\n"+
        pl.getMessagesProps().getProperty("paswordResetMailText")+"\n"+
        "\n"+
        pl.getMessagesProps().getProperty("custNickname")+":"+
        "\n"+
        "\n"+        
        nickname+
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
