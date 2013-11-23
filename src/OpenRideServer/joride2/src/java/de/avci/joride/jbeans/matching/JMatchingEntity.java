/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.matching;

import de.avci.joride.jbeans.customerprofile.JCustomerEntity;
import de.avci.joride.jbeans.customerprofile.JPublicCustomerProfile;
import de.avci.joride.jbeans.driverundertakesride.JDriverUndertakesRideEntity;
import de.avci.joride.jbeans.riderundertakesride.JRiderUndertakesRideEntity;
import de.avci.joride.utils.HTTPUtil;
import de.avci.joride.utils.PropertiesLoader;
import de.fhg.fokus.openride.matching.MatchEntity;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
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
    protected static String PARAM_NAME_rideId = "rideid";

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
        this.drive = null;
        this.ride = null;

    }
    /**
     * Representation of the matchEntities riderUndertakesRideEntity prop. This
     * is created via lazy instantiation.
     *
     */
    private JRiderUndertakesRideEntity ride = null;

    /**
     * Accessor with lazy instantiation
     *
     * @return
     */
    public JRiderUndertakesRideEntity getRide() {

        if (ride == null) {
            ride = new JRiderUndertakesRideEntity();
            ride.updateFromRiderUndertakesRideEntity(matchEntity.getRiderUndertakesRideEntity());
        }

        return ride;
    }
    /**
     * Representation of the matchEntities driverUndertakesRideEntity prop. This
     * is created via lazy instantiation.
     *
     */
    private JDriverUndertakesRideEntity drive = null;

    /**
     * Accessor with lazy instantiation
     *
     * @return
     */
    public JDriverUndertakesRideEntity getDrive() {

        if (drive == null) {
            drive = new JDriverUndertakesRideEntity();
            drive.updateFromDriverUndertakesRideEntity(matchEntity.getDriverUndertakesRideEntity());
        }

        return drive;
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
    public boolean disableAcceptOfferLink(){
        return ! enableAcceptOfferLink();
    }
    
    
    /** 
     * whether or not to disable the link that allows the 
     * requester to reject a ride offer.
     * 
     * Rules herein follow the document
     * "OpenRide-Buttons_Stati_Abstimmungsprozess-02-03-11"
     * from original OR distribution. (Unfortunately availlable in German only)
     * 
     * 
     * @return true if link should be shown, else false 
     */
    public boolean enableRejectOfferLink(){
    
   
        //  If rider State is yet undecided,
        //  rider can accept or reject
        if(this.getRiderState()==null){
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
    public boolean disableRejectOfferLink(){
        return ! enableRejectOfferLink();
    }
    
    
     
    /** 
     * whether or not to disable the link that allows the 
     * driver to reject a ride request.
     * 
     * Rules herein follow the document
     * "OpenRide-Buttons_Stati_Abstimmungsprozess-02-03-11"
     * from original OR distribution. (Unfortunately availlable in German only)
     * 
     * 
     * @return true if link should be shown, else false 
     */
    public boolean enableAcceptRequestLink(){
    
        // if Driver State is yet undecided, 
        // driver can both accept or reject
        if(this.getDriverState()==null){
                return true;  
        }
         
        // if Driver has rejected by Error,  
        // driver he can accept to correct
        if(MatchEntity.REJECTED.equals(this.getDriverState())){
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
    public boolean disableAcceptRequestLink(){
        return ! enableAcceptRequestLink();
    }
      
   
    
            
    /** 
     * whether or not to disable the link that allows the 
     * driver to reject a ride request.
     *  
     * Rules herein follow the document
     * "OpenRide-Buttons_Stati_Abstimmungsprozess-02-03-11"
     * from original OR distribution. (Unfortunately availlable in German only)
     * 
     * 
     * @return true if link should be shown, else false 
     */
    public boolean enableRejectRequestLink(){
    
   
        if(this.getDriverState()==null){
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
    public boolean disableRejectRequestLink(){
        return ! enableRejectRequestLink();
    }
      
       
    
    
    
    

    /**
     * Create a new JMatchingEntity from a real matchingEntity
     *
     * @param arg
     */
    JMatchingEntity(MatchEntity arg) {
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
    
    
    
    /** Mnemonic message describing the rider state.
     * 
     * @return 
     */
    public String getRiderStateMessage(){
    
    
       Integer rs=this.getRiderState();
       
       PropertiesLoader pl=new PropertiesLoader();
       
       if(rs==null){
           return  pl.getMessagesProps().getProperty("matchRiderStateNoRiderRequest");
       }
       
       if(rs.equals(MatchEntity.ACCEPTED)){
             return  pl.getMessagesProps().getProperty("matchRiderStateACCEPTED");
       }
       
        if(rs.equals(MatchEntity.COUNTERMANDED)){
          return  pl.getMessagesProps().getProperty("matchRiderStateCOUNTERMANDED");
       }
       
        if(rs.equals(MatchEntity.NOT_ADAPTED)){
            return  pl.getMessagesProps().getProperty("matchRiderStateNOT_ADAPTED");
        
        }
        
       if(rs.equals(MatchEntity.NO_MORE_AVAILABLE)){
           return  pl.getMessagesProps().getProperty("matchRiderStateNO_MORE_AVAILLABLE");
       }
       
       
       if(rs.equals(MatchEntity.REJECTED)){
            return  pl.getMessagesProps().getProperty("matchRiderStateREJECTED");
       }
    
       return"Cannot find state message for riderstate : "+rs;
       
    }
    
    
    /** Mnemonic message describing the driver state.
     * 
     * @return 
     */
    public String getDriverStateMessage(){
    
    
       Integer ds=this.getDriverState();
       
       PropertiesLoader pl=new PropertiesLoader();
       
       if(ds==null){
           return  pl.getMessagesProps().getProperty("matchDriverStateNoDriverOffer");
       }
       
       if(ds.equals(MatchEntity.ACCEPTED)){
             return  pl.getMessagesProps().getProperty("matchDriverStateACCEPTED");
       }
       
        if(ds.equals(MatchEntity.COUNTERMANDED)){
          return  pl.getMessagesProps().getProperty("matchDriverStateCOUNTERMANDED");
       }
       
        if(ds.equals(MatchEntity.NOT_ADAPTED)){
            return  pl.getMessagesProps().getProperty("matchDriverStateNOT_ADAPTED");
        
        }
        
       if(ds.equals(MatchEntity.NO_MORE_AVAILABLE)){
           return  pl.getMessagesProps().getProperty("matchDriverStateNO_MORE_AVAILLABLE");
       }
       
       
       if(ds.equals(MatchEntity.REJECTED)){
            return  pl.getMessagesProps().getProperty("matchDriverStateREJECTED");
       }
    
       return"Cannot find state message for driver state : "+ds;
    
    }
    
    
    
    
    /** Create label for gender from single shortcut character stored in DB.
     * 
     * @param arg
     * @return 
     */
    public String getDriverGenderLabel(){
    
             JCustomerEntity jce=new JCustomerEntity();
             jce.setCustGender(this.getDrive().getCustId().getCustGender());
             return jce.getGenderLabel();
    }
    
     /** Create label for gender from single shortcut character stored in DB.
     * 
     * @param arg
     * @return 
     */
    public String getRiderGenderLabel(){
    
             JCustomerEntity jce=new JCustomerEntity();
             jce.setCustGender(this.getRide().getCustId().getCustGender());
             return jce.getGenderLabel();
    }
    
    
    
    
    
} // class 
