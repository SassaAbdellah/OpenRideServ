/* To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.constants;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Named;

import de.avci.joride.utils.PropertiesLoader;

/** This class centrally defines a number of Constants for joride Frontend
 *  some of these may eventually be made configurable,
 *  so it's good to have them in one spot.
 * 
 *
 * @author jochen
 */

@ApplicationScoped
@Named

public class JoRideConstants implements Serializable{
    
    /**
	 * default serial
	 */
	private static final long serialVersionUID = 1L;


    
	
	/**   @return  timestamp format, defined as "tsformat" property in datetime.properties
	 */
	public static String getDateTimeFormatString(){
		return PropertiesLoader.getDatetimeProperties().getProperty("tsformat");
	}
	
	// @return  date format, defined as "dateformat" property in datetime.properties
	public static String getDateFormatString(){
		return PropertiesLoader.getDatetimeProperties().getProperty("dateformat");
	}
	
	
    
    /** Creates a new DateTimeFormat 
     *  (Datetime=Date+Time of Day)
     *  using Timezone retrieved with "getTimezone"
     *  
     * @return  
     */
    public static DateFormat createDateFormat(){
        DateFormat res=new SimpleDateFormat(getDateFormatString());
        res.setTimeZone(getTimeZone());
        return res;
    }
    
    
    
    
    /** Creates a new DateTimeFormat 
     *  (Datetime=Date+Time of Day)
     * @return  
     */
    public static DateFormat createDateTimeFormat(){
       DateFormat res=new SimpleDateFormat(getDateTimeFormatString());
       res.setTimeZone(getTimeZone());
       return res;
    }
    
    
    /** TODO: make Timezone configurable, currently it is default timezone.
     * 
     * @return
     */
    public static TimeZone getTimeZone() {
		return TimeZone.getTimeZone(PropertiesLoader.getDatetimeProperties().getProperty("defaulttimezone"));
	}

	/** Decimal Format for displaying ratings 
     */
    public DecimalFormat createRatingAverageFormat(){
        return new DecimalFormat("#0.00");
    }
    
            
    /** JSON Parameter describing the name of the JSON object describing the update count.
     *  This is used with JSON Update Service
     */
    public static String PARAM_NAME_UPDATE_RESPONSE="UpdateResponse";
    
    
    /** Make  PARAM_NAME_UPDATE_RESPONSE availlable 
     *  as a JSF Property
     * 
     * @return  PARAM_NAME_UPDATE_RESPONSE
     */
    public static String getParamNameUpdateResponse(){
        return PARAM_NAME_UPDATE_RESPONSE;
    }
    

    /** JSON Parameter describing the number of updated offers
     *  for this user.
     *  This is used with JSON Update Service.
     */
    public static String PARAM_NAME_NO_UPDATED_OFFERS="NoOfUpdatedOffers";
    
    
    /** Make PARAM_NAME_NO_UPDATED_OFFERS availlable as a JSF Property.
     * 
     * @return  PARAM_NAME_NO_UPDATED_OFFERS
     */
    public String getParamNameNoUpdatedOffers(){
        return PARAM_NAME_NO_UPDATED_OFFERS;
    }
    
    
        
    /** JSON Parameter describing the number of updated requests for this user.
     *  This is used with JSON Update Service
     */
    public static String PARAM_NAME_NO_UPDATED_REQUESTS="NoOfUpdatedRequests";
    
    
    /** Make PARAM_NAME_NO_UPDATED_OFFERS availlable 
     *  as a JSF Property
     * 
     * @return  PARAM_NAME_NO_UPDATED_OFFERS
     */
    public String getParamNameNoUpdatedRequests(){
        return PARAM_NAME_NO_UPDATED_REQUESTS;
    }
    
    
    
    /** HTTP Parameter transporting Customer's Nickname
     * 
     */
    protected static final String PARAM_NAME_NICKNAME="nickname";
    
    /** Make Nickname parameter available to JSF Beans
     */
    public String getParamNameNickname(){
        return PARAM_NAME_NICKNAME;
    }
    
    
     
} // class
