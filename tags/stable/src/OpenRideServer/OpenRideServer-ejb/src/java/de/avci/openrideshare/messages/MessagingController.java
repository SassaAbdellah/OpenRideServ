/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.openrideshare.messages;

import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 * Controller to finally implement a sending messages 
 * currently this is a dummy which only writes 
 * the messages to the log.
 * 
 *
 * @author jochen
 */
@Stateless
public class MessagingController implements MessagingControllerLocal {

    /**
     * Message to be sent from sender to recipient if a ride request is to be
     * cancelled
     *
     * Currently, this is just a dummy.
     *
     *
     * @param sender
     * @param recipient
     * @param subject
     * @param message
     * @return
     */
    @Override
    public boolean sendMessage(CustomerEntity sender, CustomerEntity recipient, String subject, String message) {

        Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

        String logstr =
                "Sending Message:\n"
                + "\n"
                + "Sender: " + sender + "\n"
                + "\n" 
                + "Recipient: "+recipient+"\n"
                +"\n"
                +"Subject: "+subject+"\n"
                +"\n"
                +"Message: "+message;
               
        
        logger.warning("Sending messages is not yet implemented : "+logstr);
        
            
        return true;
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    
    /** Currently placeholder dummy, writes the message to the logs 
     * 
     */
    
    public boolean sendSystemMessage(CustomerEntity recipient, String subject, String message) {
        
        
         Logger logger = Logger.getLogger(this.getClass().getCanonicalName());

        String logstr =
                "Sending System Message:\n"
                + "\n" 
                + "Recipient: "+recipient+"\n"
                +"\n"
                +"Subject: "+subject+"\n"
                +"\n"
                +"Message: "+message;
               
        
        logger.warning("Sending system messages is not yet implemented : "+logstr);
        

        return true;
     
    }
    
    
    
    
    
    
    
    
}
