/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.backing.messages;

import de.avci.joride.jbeans.customerprofile.JPasswordResetRequest;
import de.avci.joride.jbeans.customerprofile.JRegistrationRequest;
import de.avci.joride.utils.HTTPUtil;
import de.avci.joride.utils.PropertiesLoader;
import java.io.Serializable;
import java.util.Locale;

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

   

        this.setSender(PropertiesLoader.getOperationalProperties().getProperty(PROPERTY_NAME_WEBMASTER_EMAIL_RECIPIENT));
        this.setRecipient(jprr.getEmail());
        
        Locale locale=new HTTPUtil().detectBestLocale();
        this.setSubject(PropertiesLoader.getMessageProperties(locale).getProperty("passwordResetMailSubject"));

        String message="\n"+
        PropertiesLoader.getMessageProperties(locale).getProperty("paswordResetMailText")+"\n"+
        "\n"+
        PropertiesLoader.getMessageProperties(locale).getProperty("custNickname")+":"+
        "\n"+
        "\n"+        
        nickname+
        "\n"+            
        "\n"+       
                
        PropertiesLoader.getMessageProperties(locale).getProperty("custPassword")+":"+
        "\n"+
        "\n"+        
        password+
        "\n"+            
        "\n"+
        PropertiesLoader.getMessageProperties(locale).getProperty("registrationMailPasswordHint")+"\n";        
        this.setMessage(message);
        
        
        // finally, send the message...
        
        return this.send();
    }
    
}
