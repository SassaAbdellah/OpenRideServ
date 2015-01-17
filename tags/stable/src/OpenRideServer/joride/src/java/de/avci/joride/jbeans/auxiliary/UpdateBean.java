package de.avci.joride.jbeans.auxiliary;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import de.avci.joride.constants.JoRideConstants;
import de.avci.joride.jbeans.customerprofile.JCustomerEntityService;
import de.avci.joride.jbeans.driverundertakesride.JDriverUndertakesRideEntity;
import de.avci.joride.jbeans.riderundertakesride.JRiderUndertakesRideEntity;
import de.avci.joride.utils.HTTPUtil;
import de.avci.joride.utils.Messagekeys;
import de.avci.joride.utils.PropertiesLoader;

/**
 *
 * @author jochen
 */
@Named("updatemsg")
@SessionScoped
/**
 * JSF Bean to deliver Information about updated rides; this is to be called
 * from a poll in the toolbar
 *
 */
public class UpdateBean {

    /**
     * Standard nonstatic log
     */
    Logger log = Logger.getLogger("" + this.getClass());
    
    
    
   
    
    
    /**
     * Parameter Name for the parameter describing the number of miliseconds
     *
     */
    protected static final String ParamNameUpdateInterval = "updateInterval";
    
    /**
     * Parameter Name for the parameter describing the number of miliseconds
     * for which to display the growl notifications
     *
     */
    protected static final String ParamNameGrowlInterval = "growlInterval";
    
    
    /**
     * By default, update Interval is 60*1000 Milliseconds = 1 minute.
     *
     */
    private static long updateIntervalDefault = 6 * 1000;
    
    
    /**
     * By default, growl Interval is 6*1000 Milliseconds = 6 seconds.
     *
     */
    private static long growlIntervalDefault = 6 * 1000;
    
    
    
    /**
     * Number of milliseconds between update calls
     */
    protected Long updateInterval = null;

    /**
     * Number of milliseconds between for which to display
     * the growl notification
     */
    protected Long growlInterval = null;
    
    
    
    /**
     * Initialize update period from properties
     *
     */
    public UpdateBean() {

        super();


    } // static initialization

    /**
     * Accessor with lazy instantiation
     *
     * @return
     */
    public Long getUpdateInterval() {


        if (this.updateInterval == null) {
            this.updateInterval = new Long(updateIntervalDefault);
            try {
                String updateStr = "" + PropertiesLoader.getUpdateProperties().get(ParamNameUpdateInterval);
                this.updateInterval = new Long(updateStr);
                log.info("loaded update Interval : " + updateStr);
            } catch (Exception exc) {
                log.log(
                        Level.SEVERE,
                        "Unable to load updateInterval from Properties, using default " + updateIntervalDefault,
                        exc);
            }
        } // if (this.updateInterval == null) 


        return this.updateInterval;
    }
    
    
    /**
     * Accessor with lazy instantiation
     *
     * @return
     */
    public Long getGrowlInterval() {


        if (this.growlInterval == null) {
            this.growlInterval = new Long(growlIntervalDefault);
            try {
                String growlStr = "" + PropertiesLoader.getUpdateProperties().get(ParamNameGrowlInterval);
                this.growlInterval = new Long(growlStr);
                log.info("loaded growl Interval : " + growlStr);
            } catch (Exception exc) {
                log.log(
                        Level.SEVERE,
                        "Unable to load growlInterval from Properties, using default " + growlIntervalDefault,
                        exc);
            }
        } // if (this.updateInterval == null) 


        return this.growlInterval;
    }
    
    
    

    /**
     * Make updateInterval in seconds availlable as a JSF Prop
     */
    public long getUpdateIntervalSec() {

        double d = (getUpdateInterval());
        return Math.round((d / 1000d));
    }
    
    /**
     * Make growlInterval in seconds availlable as a JSF Prop
     */
    public long getGrowlIntervalSec() {

        double d = (getUpdateInterval());
        return Math.round((d / 1000d));
    }
    
    
    
    private UpdateService updateService = new UpdateService();

  

    public List<JDriverUndertakesRideEntity> updatedDrives() {
        return updateService.getUpdatedDrives();
    }

    public List<JRiderUndertakesRideEntity> updatedRides() {
        return updateService.getUpdatedRides();
    }

    
    
    
    
    /** Returns true, if update bean is match updated, else false.
     */
    public boolean getMatchUpdated(){
        return (new JCustomerEntityService()).isMatchUpdated();
    }
    
    /** Returns true, if there are unread messages, else false.
     */
    public boolean getHasUnreadMessages(){
    	 return new UpdateService().getHasUnreadMessages();
    }
    
    
    /** 
     * @return true, if number of updated rides > 0, else false
     */
    public boolean hasUpdatedRides(){
       return (new UpdateService()).getUpdatedRides().size()>0;
    }
    
     /** 
     * @return true, if number of updated drives > 0, else false
     */
     public boolean hasUpdatedDrives(){
       return (new UpdateService()).getUpdatedDrives().size()>0;
    }
     
     

    /**
     * @return a formatted String for current datetime
     */
    public String getTimestampFormatted() {
        DateFormat sdf = new JoRideConstants().createDateTimeFormat();
        return sdf.format(new Date());
    }
    
    
    /**  Check if there are unread messages, add messages to 
     *   message queue if there are unread messages.
     * 
     * @return
     */
    public boolean pollMessages(){
    	
    	
    	
    	boolean result=this.getHasUnreadMessages();
    	
    	System.err.println("Has unread messages : "+result);
    	
    	// if there are unread messages, add messages to queue
    	if(result){
    		Locale locale=new HTTPUtil().detectBestLocale();
    		
    		String messageSubject=PropertiesLoader.getMessageProperties(locale).getProperty("msg_unread");
    		String messageText=PropertiesLoader.getMessageProperties(locale).getProperty("msg_hasUnread");
    				
    		FacesContext context = FacesContext.getCurrentInstance();
    		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, messageSubject, messageText);
    		context.addMessage(null, facesMessage);
    	}
    	
    	return result;
    	
    }
    
    
    
} // class
