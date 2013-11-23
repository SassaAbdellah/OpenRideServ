package de.avci.joride.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * An Utility Class that tries to tell wether or not a given String is a valid
 * email Adress.
 *
 * @author jochen
 */
public class EmailCheck {

  
    
    /** Scans the input, and splits it up into an array of  email Adresses.
     *  If all works well, returns an Array of email adresses
     *  (rsp a single email adress a special case).
     * 
     *  If the input is not well formed, returns an empty array, but 
     *  does not throw an error.
     * 
     * 
     * @param addresslist a string containing one or more internet Adresses
     * @return 
     */
  
    public   InternetAddress[] getEmailAddresses(String addresslist){
        
        InternetAddress[] addresses=new InternetAddress[0];
        try {
            addresses=InternetAddress.parse(addresslist);
        } catch (AddressException ex) {
            Logger.getLogger(EmailCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return addresses;
    }
    
    
    
    /** Checks, if given String is an email adress.
     *  Returns true, if the argument is a single email adress,
     * else false.
     * 
     * @param args
     * @return 
     */
    public boolean isEmailAdress(String arg){
        return getEmailAddresses(arg).length==1;
    }
       
 
} // class
