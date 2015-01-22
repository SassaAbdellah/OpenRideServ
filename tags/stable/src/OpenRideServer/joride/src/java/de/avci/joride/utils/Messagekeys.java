package de.avci.joride.utils;


import javax.faces.bean.RequestScoped;
import javax.inject.Named;



/** Bean making a set of  keys availlable as JSF bean property
 * 
 * 
 *
 * @author jochen
 */

@Named
@RequestScoped
public class Messagekeys {
    
	/** key for error messages
	 */
    protected static final String errormessagekey="errormessagekey";
    
   /** key for normal messages
    *  
    * @return
    */
    protected static final String normalMessagekey="messagekey";
   
    public String getErrormessagekey(){
        return errormessagekey;
    }
    
    
    public String getNormalmessagekey(){
        return normalMessagekey;
    }
    
}
