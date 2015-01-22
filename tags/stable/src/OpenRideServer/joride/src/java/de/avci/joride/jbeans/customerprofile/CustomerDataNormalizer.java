/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.customerprofile;

import java.io.Serializable;

import de.fhg.fokus.openride.customerprofile.CustomerUtils;



/** Utility Class for normalizing customer data
 *  Implement serializable, as it may be part of 
 *  JSF Beans.
 * 
 * @author jochen
 */
public class CustomerDataNormalizer  implements Serializable {
    
    /** Normalizes an email address, i.e: converts to Lowercase
     *  and trims it.
     *  Throws an Error if given email address is not well formed.
     * 
     * @param emailAddress
     * @return  normalized Email Address
     */
    protected String normalizeEmailAddress(String emailAddress){
    
       if(emailAddress==null) {throw new Error("Email Address is null");}
       
       if(!(CustomerUtils.isValidEmailAdress(emailAddress))){
           throw new Error("Email address is not well-formed :"+emailAddress);
       }
      
       return emailAddress.toLowerCase().trim();
    }
    
    
    /** Normalizes a nickaname address, i.e: converts to Lowercase
     *  and trims it.
     *  Throws an Error if given nickname is null or empty.
     * 
     * @param emailAddress
     * @return  normalized Email Address
     */
    protected String normalizeNickname(String nickname){
    
       if(nickname==null) {throw new Error("nickname is null");}
        
       if(nickname.equals("")) {throw new Error("nickname is empty");}
       
       return nickname.toLowerCase().trim();
    }
    
    
    
    
} // class
