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
    
    protected static final String errormessagekey="errormessagekey";
    
    public String getErrormessagekey(){
        return errormessagekey;
    }
    
    
}
