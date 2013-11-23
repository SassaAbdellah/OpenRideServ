package de.avci.joride.jbeans.auxiliary;

import de.avci.joride.constants.JoRideConstants;
import de.avci.joride.jbeans.driverundertakesride.JDriverUndertakesRideEntity;
import de.avci.joride.jbeans.riderundertakesride.JRiderUndertakesRideEntity;
import de.avci.joride.utils.PropertiesLoader;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

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
    
    
    /**  Key for messages targeted at update growl
     */
    protected static final String UpdateMessageKey="UpdateMessageKey";
    
    /** Make update message key availlable as JSF Bean
     * 
     * @return 
     */
    public String getUpdateMessageKey(){
        return UpdateMessageKey;
    }
    
    
    /**
     * Parameter Name for the parameter describing the number of miliseconds
     *
     */
    protected static final String ParamNameUpdateInterval = "updateInterval";
    /**
     * By default, update Interval is 60*1000 Milliseconds = 1 minute.
     *
     */
    private static long updateIntervalDefault = 6 * 1000;
    /**
     * Number of milliseconds between
     */
    protected Long updateInterval = null;

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
                PropertiesLoader loader = new PropertiesLoader();
                String updateStr = "" + loader.getUpdateProps().get(ParamNameUpdateInterval);
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
     * Make updateInterval in seconds availlable as a JSF Prop
     */
    public long getUpdateIntervalSec() {

        double d = (getUpdateInterval());
        return Math.round((d / 1000d));
    }
    private UpdateService updateService = new UpdateService();

    /**
     *
     * Get a String describing updates
     *
     * @return
     */
    public String getUpdateNotification() {


        String updateMessage = updateService.getUpdateMessage();

        // if the update message is not empty, add it to message queue,
        // so it can be displayed inside of a queue

       if (!("".equals(updateMessage))) {


            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage fmsg=new FacesMessage(updateMessage);
            
            context.addMessage(this.getUpdateMessageKey(), fmsg);
        }

        return updateMessage;
    }

    public List<JDriverUndertakesRideEntity> updatedDrives() {
        return updateService.getUpdatedDrives();
    }

    public List<JRiderUndertakesRideEntity> updatedRides() {
        return updateService.getUpdatedRides();
    }

    public boolean hasUpdatedDrives() {
        return updateService.hasUpdatedDrives();
    }

    public boolean hasUpdatedRides() {
        return updateService.hasUpdatedRides();
    }

    /**
     * @return a formatted String for current datetime
     */
    public String getTimestampFormatted() {
        DateFormat sdf = new JoRideConstants().createDateTimeFormat();
        return sdf.format(new Date());
    }
} // class
