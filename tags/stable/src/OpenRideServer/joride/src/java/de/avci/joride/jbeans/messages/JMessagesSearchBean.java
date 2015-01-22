package de.avci.joride.jbeans.messages;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import de.avci.joride.constants.JoRideConstants;
import de.avci.joride.utils.HTTPUtil;

/** JSF Bean to help searching for
 * 
 * @author jochen
 *
 */

@Named("messageSearch")
@SessionScoped

/** JBean to help searching for messages,
 *  keep track of parameters,
 *  and hold a reference to the result
 * 
 * @author jochen
 *
 */
public class JMessagesSearchBean implements Serializable {
	
	
   transient Logger log = Logger.getLogger(this.getClass().getCanonicalName());

	/** Default Id
	 */
	private static final long serialVersionUID = 1L;

	/** Searches go from StartDate to endDate
	 */ 
	private Date startDate=new Date();
	
	/** Searches go from StartDate to endDate
	 */ 
	private Date endDate=new Date();

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	
	/** All unread messages
	 * 
	 * @return
	 */
	public List <JMessage> getUnreadMessages() { return new JMessageService().getUnreadMessages();}
	
	
	
	/** Number of unread messages.
	 *  Since this method will be polled, it should be backed up
	 *  by an ejb method
	 *  
	 */
	public long getNumberOfUnreadMessages(){
		return new JMessageService().getNumberOfUnreadMessages();
	}
	
    /**
     * TimeFormat used for retrieving dates in http requests
     *
     */
    protected String DATE_FORMAT = JoRideConstants.getDateFormatString();

    /**
     * Make Format Strings available in JSF Fashion
     */
    public String getDateformat() {
        return DATE_FORMAT;
    }
    
    /**
     *   date format to encode the start and end dates
     */
    protected DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    
    
    /**
     *
     * @return nicely formatted version of the startDate property.
     */
    public String getStartDateFormatted() {
        return dateFormat.format(this.getStartDate());
    }
     
	
   /**
    *
    * @return nicely formatted version of the endDate property.
    */
   public String getEndDateFormatted() {
       return dateFormat.format(this.getEndDate());
   }
   
   
   /**
    * http parameter to transmit the startDate
    */
   protected static String PARAM_NAME_START_DATE = "TIMEINTERVAL_START_DATE";

   /**
    * Make StartDate parameter name available in JSF Fashion
    */
   public String getParamStartDate() {
       return PARAM_NAME_START_DATE;
   }
   /**
    * http parameter to transmit the endDate
    */
   protected static String PARAM_NAME_END_DATE = "TIMEINTERVAL_END_DATE";

   /**
    * Make StartDate parameter name available in JSF Fashion
    */
   public String getParamEndDate() {
       return PARAM_NAME_END_DATE;
   }
   



   /**
    * 
    * @return
    */
   public List <JMessage> getMessagesInInterval(){
	   
	  return new JMessageService().findMessagesInIntervall(this.getStartDate(), this.getEndDate());
   }
   
   /** Useful for debugging
    * 
    */
   public String debugPrintout(){
	   
	  return "StartDate : "+getStartDateFormatted()+" EndDate : "+getEndDateFormatted();
   }
   

}
