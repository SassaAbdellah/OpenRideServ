/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.matching;

import de.avci.joride.constants.JoRideConstants;
import de.avci.joride.jbeans.customerprofile.JCustomerEntity;
import de.avci.joride.jbeans.customerprofile.JPublicCustomerProfile;
import de.avci.joride.jbeans.driverundertakesride.JDriverUndertakesRideEntity;
import de.avci.joride.jbeans.messages.JMessage;
import de.avci.joride.jbeans.messages.JMessageService;
import de.avci.joride.jbeans.riderundertakesride.JRiderUndertakesRideEntity;
import de.avci.joride.jbeans.riderundertakesride.JRiderUndertakesRideEntityService;
import de.avci.joride.utils.HTTPUtil;
import de.avci.joride.utils.PropertiesLoader;
import de.fhg.fokus.openride.matching.MatchEntity;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

/**
 * Wrapper making MatchingEntity available as a CDI Bean for use in JSF
 * frontend.
 *
 * @author jochen
 *
 */
@Named("match")
@SessionScoped
public class JMatchingEntity implements Serializable {

    /**
     * HTTPRequest Parameter to tell JMatchingEntity the rider Id (id of offer)
     * of this request.
     */
    protected static String PARAM_NAME_rideId = "rideId";

    /**
     * Accessor for the PARAM_NAME_riderId parameter
     *
     * @return
     */
    public String getParamRideID() {
        return this.PARAM_NAME_rideId;
    }
    /**
     * HTTPRequest Parameter to tell JMatchingEntity the ridererrouteId (id of
     * request) of this request.
     *
     */
    protected static String PARAM_NAME_riderrouteId = "riderrouteid";

    /**
     * Accessor for the PARAM_NAME_riderrouteId parameter
     *
     * @return
     */
    public String getParamRiderrouteId() {
        return this.PARAM_NAME_riderrouteId;
    }
    /**
     * The Match Entity that this Object is build around.
     */
    private MatchEntity matchEntity = null;

    public MatchEntity getMatchEntity() {
        return this.matchEntity;
    }

    /**
     * Non trivial setter! -- In addition to setting the MatchEntity property,
     * it does also blank out the Drive and Ride properties, so that lazy
     * instantiation will renew them.
     *
     * @param arg
     */
    public void setMatchEntitiy(MatchEntity arg) {

        this.matchEntity = arg;
    }

    /**
     * Accessor with lazy instantiation
     *
     * @return
     */
    public JRiderUndertakesRideEntity getRide() {

        JRiderUndertakesRideEntity jride = new JRiderUndertakesRideEntity();
        jride.updateFromRiderUndertakesRideEntity(matchEntity.getRiderUndertakesRideEntity());
        
        return jride;
    }

    /**
     * Accessor with lazy instantiation
     *
     * @return
     */
    public JDriverUndertakesRideEntity getDrive() {

        JDriverUndertakesRideEntity jdrive = new JDriverUndertakesRideEntity();
        jdrive.updateFromDriverUndertakesRideEntity(matchEntity.getDriverUndertakesRideEntity());

        return jdrive;
    }

    /**
     * Get Driver State in it's integer representation.
     *
     * @return the driver state
     *
     */
    public Integer getDriverState() {
        return this.getMatchEntity().getDriverState();
    }

    /**
     * Get Rider State in it's integer representation.
     *
     * @return the rider state
     *
     */
    public Integer getRiderState() {
        return this.getMatchEntity().getRiderState();
    }

    /**
     * Accept Driver for this match. This methods attempts to be save, i.e
     * checks if the caller is in role to accept match.
     */
    public void acceptDriver(ActionEvent evt) {
        new JMatchingEntityService().acceptDriverSafely(this);
    }

    /**
     * Reject Driver for this match. This methods attempts to be save, i.e
     * checks if the caller is in role to accept match.
     */
    public void rejectDriver(ActionEvent evt) {
        new JMatchingEntityService().rejectDriverSafely(this);
    }

    /**
     * Accept Rider for this match. This methods attempts to be save, i.e checks
     * if the caller is in role to accept match.
     */
    public void acceptRider(ActionEvent evt) {
        new JMatchingEntityService().acceptRiderSafely(this);
    }

    /**
     * Reject Rider for this match. This methods attempts to be save, i.e checks
     * if the caller is in role to accept match.
     */
    public void rejectRider(ActionEvent evt) {
        new JMatchingEntityService().rejectRiderSafely(this);
    }
    

    /**
     * wether or not to disable the link that allows the requester to accept a
     * ride offer.
     *
     * Rules herein follow the document
     * "OpenRide-Buttons_Stati_Abstimmungsprozess-02-03-11" from original OR
     * distribution. (Unfortunately in availlable in German only)
     *
     *
     * @return true if link should be shown, else false
     */
    public boolean enableAcceptOfferLink() {


        // Rider can always accept if Rider State is yet undecided
        if (this.getRiderState() == null) {
            return true;
        }

        // Rider can always accept if Rider State is yet undecided
        if (this.getRiderState() == MatchEntity.NOT_ADAPTED) {
            return true;
        }


        // Rider can correct an Erroneous Accept
        if (MatchEntity.REJECTED.equals(this.getRiderState())) {
            return true;
        }

        return false;
    }

    /**
     * Convenience method
     *
     * @return negated value of {@link enableAcceptOfferLink()}
     *
     */
    public boolean disableAcceptOfferLink() {
        return !enableAcceptOfferLink();
    }

    /**
     * whether or not to disable the link that allows the requester to reject a
     * ride offer.
     *
     * Rules herein follow the document
     * "OpenRide-Buttons_Stati_Abstimmungsprozess-02-03-11" from original OR
     * distribution. (Unfortunately availlable in German only)
     *
     *
     * @return true if link should be shown, else false
     */
    public boolean enableRejectOfferLink() {


        //  If rider State is yet undecided,
        //  rider can accept or reject
        if (this.getRiderState() == null) {
            return true;
        }

        if (this.getRiderState() == MatchEntity.NOT_ADAPTED) {
            return true;
        }

        return false;
    }

    /**
     * Convenience method
     *
     * @return negated value of {@link enableRejectOfferLink()}
     *
     */
    public boolean disableRejectOfferLink() {
        return !enableRejectOfferLink();
    }

    /**
     * whether or not to disable the link that allows the driver to reject a
     * ride request.
     *
     * Rules herein follow the document
     * "OpenRide-Buttons_Stati_Abstimmungsprozess-02-03-11" from original OR
     * distribution. (Unfortunately availlable in German only)
     *
     *
     * @return true if link should be shown, else false
     */
    public boolean enableAcceptRequestLink() {

        // if Driver State is yet undecided, 
        // driver can both accept or reject
        if (this.getDriverState() == null) {
            return true;
        }

        if (this.getDriverState() == MatchEntity.NOT_ADAPTED) {
            return true;
        }


        // if Driver has rejected by Error,  
        // driver he can accept to correct
        if (MatchEntity.REJECTED.equals(this.getDriverState())) {
            return true;
        }

        return false;
    }
    
    
    /** True, if driver  can either accept or reject this match
     *  (i.e: wether or not link should be shown in driveMatchingsList)
     */
    public boolean enableDriverAcceptOrRejectLink(){
    	return (enableAcceptRequestLink() || enableRejectRequestLink());	
    }
    
    
    /** True, if rider  can either accept or reject this match
     *  (i.e: wether or not link should be shown in rideMatchingsList)
     */
    public boolean enableRiderAcceptOrRejectLink(){
    	return (enableAcceptOfferLink() || enableRejectOfferLink());	
    }
    
    
    

    /**
     * Convenience method
     *
     * @return negated value of {@link enableRejectOfferLink()}
     *
     */
    public boolean disableAcceptRequestLink() {
        return !enableAcceptRequestLink();
    }

    /**
     * whether or not to disable the link that allows the driver to reject a
     * ride request.
     *
     * Rules herein follow the document
     * "OpenRide-Buttons_Stati_Abstimmungsprozess-02-03-11" from original OR
     * distribution. (Unfortunately availlable in German only)
     *
     *
     * @return true if link should be shown, else false
     */
    public boolean enableRejectRequestLink() {


        if (this.getDriverState() == null) {
            return true;
        }

        if (this.getDriverState() == MatchEntity.NOT_ADAPTED) {
            return true;
        }

        return false;
    }

    /**
     * Convenience method
     *
     * @return negated value of {@link enableRejectOfferLink()}
     *
     */
    public boolean disableRejectRequestLink() {
        return !enableRejectRequestLink();
    }

    /**
     * Create a new JMatchingEntity from a real matchingEntity
     *
     * @param arg
     */
    public JMatchingEntity(MatchEntity arg) {
        this.matchEntity = arg;
    }

    /**
     * Update from parameters given in HTTPRequest, i.e evaluate riderId and
     * riderrouteId parameter, get match (if possible) and update data from
     * match.
     *
     */
    public void smartUpdate() {

        HTTPUtil hru = new HTTPUtil();
        String rideIdStr = hru.getParameterSingleValue(PARAM_NAME_rideId);
        Integer rideIdArg = new Integer(rideIdStr);

        String riderrouteIdStr = hru.getParameterSingleValue(PARAM_NAME_riderrouteId);
        Integer riderrouteIdArg = new Integer(riderrouteIdStr);


        MatchEntity me = new JMatchingEntityService().getMatchSafely(rideIdArg, riderrouteIdArg);

        this.setMatchEntitiy(me);

    }

    /**
     * Provides rider's data visible before a ride had been accepted upon
     */
    public JPublicCustomerProfile getPublicRiderData() {

        JPublicCustomerProfile res = new JPublicCustomerProfile();
        res.updateFromCustomerEntity(this.getRide().getCustId());
        return res;
    }

    /**
     * Provides driver's data visible before a ride had been accepted upon
     */
    public JPublicCustomerProfile getPublicDriverData() {

        JPublicCustomerProfile res = new JPublicCustomerProfile();
        res.updateFromCustomerEntity(this.getDrive().getCustId());
        return res;
    }

    /**
     * Bean constructor
     */
    public JMatchingEntity() {
    }

    /**
     * Mnemonic message describing the rider state.
     *
     * @return
     */
    public String getRiderStateMessage() {


        Integer rs = this.getRiderState();
        
        Locale locale=new HTTPUtil().detectBestLocale();
    

        if (rs == null) {
            return PropertiesLoader.getMessageProperties(locale).getProperty("matchRiderStateNoRiderRequest");
        }

        if (rs.equals(MatchEntity.ACCEPTED)) {
            return PropertiesLoader.getMessageProperties(locale).getProperty("matchRiderStateACCEPTED");
        }

        if (rs.equals(MatchEntity.RIDER_COUNTERMANDED)) {
            return PropertiesLoader.getMessageProperties(locale).getProperty("matchRiderStateRIDERCOUNTERMANDED");
        }
        
        if (rs.equals(MatchEntity.DRIVER_COUNTERMANDED)) {
            return PropertiesLoader.getMessageProperties(locale).getProperty("matchRiderStateDRIVERCOUNTERMANDED");
        }

        if (rs.equals(MatchEntity.NOT_ADAPTED)) {
            return PropertiesLoader.getMessageProperties(locale).getProperty("matchRiderStateNOT_ADAPTED");

        }

        if (rs.equals(MatchEntity.NO_MORE_AVAILABLE)) {
            return PropertiesLoader.getMessageProperties(locale).getProperty("matchRiderStateNO_MORE_AVAILLABLE");
        }

        if (rs.equals(MatchEntity.REJECTED)) {
            return PropertiesLoader.getMessageProperties(locale).getProperty("matchRiderStateREJECTED");
        }

        return "Cannot find state message for riderstate : " + rs;
    }

    /**
     * Mnemonic message describing the driver state.
     *
     * @return
     */
    public String getDriverStateMessage() {


        Integer ds = this.getDriverState();

        Locale locale=new HTTPUtil().detectBestLocale();
        
       

        if (ds == null) {
            return PropertiesLoader.getMessageProperties(locale).getProperty("matchDriverStateNoDriverOffer");
        }

        if (ds.equals(MatchEntity.ACCEPTED)) {
            return PropertiesLoader.getMessageProperties(locale).getProperty("matchDriverStateACCEPTED");
        }

        if (ds.equals(MatchEntity.RIDER_COUNTERMANDED)) {
            return PropertiesLoader.getMessageProperties(locale).getProperty("matchDriverStateRIDER_COUNTERMANDED");
        }
        
        if (ds.equals(MatchEntity.DRIVER_COUNTERMANDED)) {
            return PropertiesLoader.getMessageProperties(locale).getProperty("matchDriverStateDRIVER_COUNTERMANDED");
        }
        

        if (ds.equals(MatchEntity.NOT_ADAPTED)) {
            return PropertiesLoader.getMessageProperties(locale).getProperty("matchDriverStateNOT_ADAPTED");

        }

        if (ds.equals(MatchEntity.NO_MORE_AVAILABLE)) {
            return PropertiesLoader.getMessageProperties(locale).getProperty("matchDriverStateNO_MORE_AVAILLABLE");
        }


        if (ds.equals(MatchEntity.REJECTED)) {
            return PropertiesLoader.getMessageProperties(locale).getProperty("matchDriverStateREJECTED");
        }

        return "Cannot find state message for driver state : " + ds;

    }

    /**
     * Create label for gender from single shortcut character stored in DB.
     *
     * @param arg
     * @return
     */
    public String getDriverGenderLabel() {

        JCustomerEntity jce = new JCustomerEntity();
        jce.setCustGender(this.getDrive().getCustId().getCustGender());
        return jce.getGenderLabel();
    }

    /**
     * Create label for gender from single shortcut character stored in DB.
     *
     * @param arg
     * @return
     */
    public String getRiderGenderLabel() {

        try {
            JCustomerEntity jce = new JCustomerEntity();
            jce.setCustGender(this.getRide().getCustId().getCustGender());
            return jce.getGenderLabel();
        } catch (Exception exc) {
            return null;
        }
    }
    
    
    
    /** 
     * @return a message that the rider has left 
     */
    public String getRiderMessage(){
        return this.getMatchEntity().getRiderMessage();
    }
    
    public void setRiderMessage(String message){
        new JMatchingEntityService().setRiderMessageSafely(this.getMatchEntity(), message);
    }
    
    
   
   
    
    /** 
     * @return a message that the driver has left 
     */
    public String getDriverMessage(){
        return this.getMatchEntity().getDriverMessage();
    }
    
      public void setDriverMessage(String message){
        new JMatchingEntityService().setDriverMessageSafely(this.getMatchEntity(), message);
    }
    
    
      /** return true, if either rider or driver countermanded this ride, else false 
       * 
       * @return true, if either rider or driver countermanded this ride, else false
       */
    public boolean getCountermanded(){
    
        if(this.getRiderState()==MatchEntity.RIDER_COUNTERMANDED){
            return true;
        }
        
        if(this.getRiderState()==MatchEntity.DRIVER_COUNTERMANDED){
            return true;
        }
        
         if(this.getDriverState()==MatchEntity.RIDER_COUNTERMANDED){
            return true;
        }
        
        if(this.getDriverState()==MatchEntity.DRIVER_COUNTERMANDED){
            return true;
        }
           
        return false;
    }
    
       
      /** return true, if neither rider nor driver countermanded this ride, else false 
       * 
       * @return true, if neither rider mor driver countermanded this ride, else false
       */
    public boolean getNotCountermanded(){
    
      return ! getCountermanded();
    }
    
    
       /**
     * Update Rider's rating and comment for this ride
     *
     */
    public void doCountermand(javax.faces.event.ActionEvent evt) {

       new JMatchingEntityService().countermandSafely(this.getMatchEntity());
    }

    /** common date format
     */
    private DateFormat dateFormat;

	/**
	 * Accessor with lazy instantiation
	 * 
	 * 
	 * @return
	 */
	protected DateFormat getDateFormat() {

		if (this.dateFormat == null) {
			dateFormat = (new JoRideConstants()).createDateTimeFormat();
		}

		return dateFormat;
	}
    
    protected String getDatetimeFormatted(Date date){ 
    		return getDateFormat().format(date);
    
    }
    
    /**
     * @return nicely formatted version of expected pickup time
     */
    public String getMatchExpectedStartTimeFormatted(){
    
    	if(this.getMatchEntity()==null) return "--";
    	if(this.getMatchEntity().getMatchExpectedStartTime()==null) return "--";
    	return getDatetimeFormatted(this.getMatchEntity().getMatchExpectedStartTime());
    }
    
    
    
    
    /** Send message from Driver to Rider, where message payload is
     *  the "message" property.
     *  
     * 
     */
    public void sendDriverMessage(){
    	new JMessageService().createDriverMessageFromMatch(this);
    }
    
    /** Send message from Rider to Driver, where message payload is
     *  the "message" property.
     *  
     * 
     */
    public void sendRiderMessage(){
    	new JMessageService().createRiderMessageFromMatch(this);
    }
    
    
    /** Get list of messages for this match
     * 
     */
    public List <JMessage> getMessages(){
    	return new JMessageService().findMessagesForMatch(this);
    }
    
    
       /** return false, if neither rider or driver countermanded this ride, else false 
       * 
       * @return true, if either rider or driver countermanded this ride, else false
       */
    
    

    public String getDebugPrintout() {

        StringBuffer buf = new StringBuffer();
        buf.append("\n");
        buf.append("=== JMatchEntity =====================================================\n");
        buf.append("RideId from PK        :" + this.getMatchEntity().getMatchEntityPK().getRideId() + "\n");
        buf.append("RiderouteId from PK   :" + this.getMatchEntity().getMatchEntityPK().getRiderrouteId() + "\n");
        buf.append("=== Drive ==\n");
        buf.append("RideId from Drive     : " + this.getDrive().getRideId() + "\n");
        buf.append("=== Ride ==\n");
        buf.append("RiderrouteId from Ride: " + this.getRide().getRiderrouteId() + "\n");
        buf.append("============\n");
        return buf.toString();
    }
} // class 
