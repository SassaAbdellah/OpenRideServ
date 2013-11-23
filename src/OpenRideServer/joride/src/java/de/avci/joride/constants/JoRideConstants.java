/* To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.constants;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Named;

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
    
    
       
    /** Format to be used for dates.
     *  (day in month in year, without time of day)
     * 
     *  FIXME: eventually this must be made confingurable as part 
     *         of internationalization. Currently this is eurocentric.
     */
    public static String JORIDE_DATE_FORMAT_STR= "dd.MM.yyyy";
    
    
    /** Creates a new DateTimeFormat 
     *  (Datetime=Date+Time of Day)
     * @return  
     */
    public DateFormat createDateFormat(){
        return new SimpleDateFormat(JORIDE_DATE_FORMAT_STR);
    }
    
    
    
    
    
    
    
    /** Format to be used for timestamps.
     * 
     *  FIXME: eventually this must be made confingurable as part 
     *         of internationalization. Currently this is eurocentric.
     */
    public static String JORIDE_TIMESTAMP_FORMAT_STR= "dd.MM.yyyy HH:mm";
    
    
    /** Creates a new DateTimeFormat 
     *  (Datetime=Date+Time of Day)
     * @return  
     */
    public DateFormat createDateTimeFormat(){
        return new SimpleDateFormat(JORIDE_TIMESTAMP_FORMAT_STR);
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
    public String getParamNameUpdateResponse(){
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
