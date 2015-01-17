package de.avci.joride.jbeans.customerprofile;

import de.avci.joride.constants.JoRideConstants;
import de.avci.joride.constants.NavigationKeys;
import de.avci.joride.jbeans.auxiliary.JRatingBean;
import de.avci.joride.jbeans.auxiliary.JRatingService;
import de.avci.joride.jbeans.auxiliary.RideSearchParamsBean;
import de.avci.joride.utils.CRUDConstants;
import de.avci.joride.utils.HTTPUtil;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.NavigationCase;
import javax.inject.Named;

/**
 * **Public** Customer Profile.
 *
 * This is the subset of personal data from {@link JCustomerEntity} that may
 * safely be displayed to other customers.
 *
 * Consequently, this gets created from JCustomerProfile and has only Getter
 * methods.
 *
 *
 *
 *
 * @author jochen
 */
@Named("publicProfile")
@SessionScoped
public class JPublicCustomerProfile implements Serializable {

    protected static String PUBLIC_PROFILE_DISPLAY_PAGE = "displayPublicProfile";
    transient Logger log = Logger.getLogger("" + this.getClass());
    /**
     * CustomerId
     */
    private Integer custId;

    public Integer getCustId() {
        return this.custId;
    }
    /**
     * Customer Nickname
     */
    private String custNickname;

    public String getCustNickname() {
        return this.custNickname;
    }

    public void setCustNickname(String arg) {
        this.custNickname = arg;
    }
    /**
     * Gender should be known to Riders a priori, since it may effect accept
     * decline decision
     */
    private char custGender;

    public char getCustGender() {
        return custGender;
    }
    /**
     * Since when is this user allowed to drive. Another important information
     * before deciding on whether or not to acceppt a ride offer.
     *
     */
    private Date custLicensedate;

    public Date getCustLicensedate() {
        return custLicensedate;
    }
    /**
     * Whether Customer is smoker, respectively whether or not smoking is
     * allowed during rides.
     *
     */
    private Boolean custIssmoker;

    public Boolean getCustIssmoker() {
        return custIssmoker;
    }
    
    /** preferred language of this customer */
    
    private String preferredLanguage;
    
    public String getPreferredLanguage(){
    	return this.preferredLanguage;
    }
    
    /** Human readable version of preferred language to display
     * 
     */
    public String getPreferredLanguageDisplayString(){
    	
    	try{
    		Locale locale=new Locale(this.getPreferredLanguage());
    		if(locale==null) return("");
    		return locale.getDisplayName();
    	} catch (Exception exc){
    		return("");
    	}
    }
    

    /** Wether or not the user wants his phonenumber to be shown to others
     */
    private boolean showMobilePhoneno;
    
    public boolean getShowMobilePhoneno(){
    	return this.showMobilePhoneno;
    }
    
    public void setShowMobilePhoneno(boolean arg){
    	this.showMobilePhoneno=arg;
    }
    
    
    /** Wether or not the user wants his email to be shown to others
     */
    private boolean showEmail;
    
    
    public boolean getShowEmail(){
    	return this.showEmail;
    }
    
    public void setShowEmail(boolean arg){
    	this.showEmail=arg;
    }
    
    
    
    /** User's mobile phonenumber
     */
    private String mobilePhoneno;
    
    public String getMobilePhoneno(){
    	return this.mobilePhoneno;
    }
    
    public void setMobilePhoneno(String arg){
    	this.mobilePhoneno=arg;
    }
    
    
    /** user's email
     */
    private String email;
    
    
    public String getEmail(){
    	return this.email;
    }
    
    public void setEmail(String arg){
    	this.email=arg;
    }
    

    /**
     * Fill with Data from givenCustomerId.
     *
     *
     */
    public void updateFromCustomerEntity(CustomerEntity ce) {

        this.blankProperties();

        if (ce == null) {

            // a nonexisting ce will result in a PublicCustomer Entity
            // which has both ID and Nickname blanked,
            // and thus fails the "seems to exist" test
            return;
        }


        
        this.custId = ce.getCustId();
        this.custGender = ce.getCustGender();
        this.custLicensedate = ce.getCustLicensedate();
        this.custNickname = ce.getCustNickname();
        this.custIssmoker = ce.getCustIssmoker();
        this.preferredLanguage=ce.getPreferredLanguage();
        // set email only if user wants to display it
        this.showEmail=ce.getShowEmailToPartners();
        if(this.showEmail){ this.setEmail(ce.getCustEmail());}
        // set public profile only if user wants us to
        this.showMobilePhoneno=ce.getShowMobilePhoneToPartners();
        if(this.showMobilePhoneno){this.setMobilePhoneno(ce.getCustMobilephoneno());}
    }

    /**
     * Update this profile from the caller's CustomerEntity.
     *
     */
    private void updateFromCallerPublicProfile() {

        JPublicCustomerProfileService jpcps = new JPublicCustomerProfileService();
        jpcps.updateFromCallerPublicProfile(this);
    }

    /**
     * Update this profile from the CustomerEntity given by Id.
     *
     * @param custId
     */
    public void updateFromCustId() {

        // save until after blanking
        Integer myCustId = this.getCustId();

        // erase all properties, this may be reusing a session scoped bean
        this.blankProperties();
        this.custId = myCustId;

        JPublicCustomerProfileService jpcps = new JPublicCustomerProfileService();

        if (this.getCustId() == null) {
            throw new Error("Cannot update profile,  custId is null");
        }


        JCustomerEntityService jces = new JCustomerEntityService();
        CustomerEntity ce = jces.getCustomerEntityByCustId(custId);
        this.updateFromCustomerEntity(ce);

    }

    /**
     *
     * /**
     * Update *all* data for a profile of which only the nickname is set. This
     * is used to retrieve user information given the nickname
     */
    public void updateFromCustNickname() {

  
        String nick = this.getCustNickname();
        // erase all properties, this may be reusing a session scoped bean
        this.blankProperties();
        this.setCustNickname(nick);

        if (nick == null) {
            throw new Error("Cannot update from nickname, nickname is null");
        }

        JCustomerEntityService jces = new JCustomerEntityService();
        CustomerEntity ce = jces.getCustomerEntityByNickname(this.getCustNickname());



        this.updateFromCustomerEntity(ce);
    }

    /**
     * Coarse method to determine if a customerprofile really exists.
     * customerprofile is said to exist, if both custName and custId are not
     * null.
     *
     *
     */
    public boolean seemsToExists() {

        if ((this.getCustId() != null) && (this.getCustNickname() != null)) {
            return true;
        }

        return false;
    }

    /**
     * @return Total number of Ratings for this customer, or null, if there was
     * an Error
     */
    public Integer getDriverRatingsTotal() {
        return new JPublicCustomerProfileService().getRatingsTotalAsDriver(this.getCustId());
    }

    /**
     * @return Average of Ratings for this customer, or null, if there was an
     * Error
     */
    public Integer getDriverRatingsCount() {
        return new JPublicCustomerProfileService().getRatingsCountAsDriver(this.getCustId());
    }

    /**
     * @return Total number of Ratings for this customer, or null, if there was
     * an Error
     */
    public Integer getRiderRatingsTotal() {
        return new JPublicCustomerProfileService().getRatingsTotalAsRider(this.getCustId());
    }

    /**
     * @return Average of Ratings for this customer, or null, if there was an
     * Error
     */
    public Integer getRiderRatingsCount() {
        return new JPublicCustomerProfileService().getRatingsCountAsRider(this.getCustId());
    }

    /**
     * @return a formatted version of getRiderRatingsTotalA/getRiderRatingsCount
     *
     */
    public String getRiderRatingsRatioFormatted() {

        Integer riderRatingsCountI = getRiderRatingsCount();
        Integer riderRatingsTotalI = getRiderRatingsTotal();

        if (riderRatingsTotalI == null) {
            return "--";
        }
        if (riderRatingsCountI == null) {
            return "--";
        }
        // do not divide by zero!
        if (riderRatingsTotalI == 0) {
            return "--";
        }


        float totalF = riderRatingsTotalI.floatValue();
        float countF = riderRatingsCountI.floatValue();

        return new JoRideConstants().createRatingAverageFormat().format(totalF / countF);
    }

    /**
     * @return a formatted version of
     * getDriverRatingsTotalA/getDriverRatingsCount
     *
     */
    public String getDriverRatingsRatioFormatted() {

        Integer driverRatingsCountI = getDriverRatingsCount();
        Integer driverRatingsTotalI = getDriverRatingsTotal();

        if (driverRatingsTotalI == null) {
            return "--";
        }
        if (driverRatingsCountI == null) {
            return "--";
        }
        // do not divide by zero!
        if (driverRatingsTotalI == 0) {
            return "--";
        }


        float totalF = driverRatingsTotalI.floatValue();
        float countF = driverRatingsCountI.floatValue();

        return new JoRideConstants().createRatingAverageFormat().format(totalF / countF);
    }

    /**
     * Driver Rating ration rounded to Integer, so it can be displayed with a
     * Star Rating widget
     *
     * @return Math.round(getDriverRatingsTotal/getDriverRatingsCount), or null
     * if the ratio cannot be computed
     *
     */
    public Integer getDriverStarRating() {

        Integer driverRatingsCountI = getDriverRatingsCount();
        Integer driverRatingsTotalI = getDriverRatingsTotal();

        if (driverRatingsTotalI == null) {
            return null;
        }
        if (driverRatingsCountI == null) {
            return null;
        }
        // do not divide by zero!
        if (driverRatingsTotalI == 0) {
            return null;
        }

        float totalF = driverRatingsTotalI.floatValue();
        float countF = driverRatingsCountI.floatValue();

        return new Integer(Math.round(totalF / countF));
    }

    /**
     * Rider Rating ration rounded to Integer, so it can be displayed with a
     * Star Rating widget
     *
     * @return Math.round(getRiderRatingsTotal/getRiderRatingsCount), or null if
     * the ratio cannot be computed
     *
     */
    public Integer getRiderStarRating() {

        Integer riderRatingsCountI = getRiderRatingsCount();
        Integer riderRatingsTotalI = getRiderRatingsTotal();

        if (riderRatingsTotalI == null) {
            return null;
        }
        if (riderRatingsCountI == null) {
            return null;
        }
        // do not divide by zero!
        if (riderRatingsTotalI == 0) {
            return null;
        }

        float totalF = riderRatingsTotalI.floatValue();
        float countF = riderRatingsCountI.floatValue();

        return new Integer(Math.round(totalF / countF));
    }

    /**
     * @return true, if this customer is driver rated, else false
     */
    public boolean getDriverRated() {

        Integer count = this.getDriverRatingsCount();
        if (count == null) {
            return false;
        }
        if (count < 1) {
            return false;
        }
        return true;
    }

    /**
     * @return true, if this customer is rider rated, else false
     */
    public boolean getRiderRated() {

        Integer count = this.getRiderRatingsCount();
        if (count == null) {
            return false;
        }
        if (count < 1) {
            return false;
        }
        return true;
    }

    /**
     * Load caller's profile, then move to displayPublicProfile page
     *
     * @return
     */
    public String displayMyPublicProfile() {

        this.blankProperties();
        this.updateFromCallerPublicProfile();
        return PUBLIC_PROFILE_DISPLAY_PAGE;

    }

    /**
     * Load profile given by nickname, then move to displayPublicProfile page
     *
     * @return
     */
    public String displayProfileForNickname() {
        String nick = this.getCustNickname();
        this.blankProperties();
        this.setCustNickname(nick);
        this.updateFromCustNickname();
        return PUBLIC_PROFILE_DISPLAY_PAGE;
    }

    /**
     * Load profile given by id, then move to displayPublicProfile page.
     *
     * If (Crud)
     *
     *
     *
     * @return
     */
    public String displayProfileForCustId() {

        // check for presence of Id Parameter,
        // and if present, set custId property to 
        // value given in Parameter
        String custIdArgStr = (new HTTPUtil().getParameterSingleValue(new CRUDConstants().getParamNameCrudId()));

        Integer custIdArg = null;
        try {
            custIdArg = Integer.parseInt(custIdArgStr);
        } catch (Exception exc) {
            throw new Error("Error while parsing potential custId " + custIdArgStr + " is non numeric");
        }

        if (custIdArg != null) {
            this.custId = custIdArg;
        }


        this.updateFromCustId();
        return PUBLIC_PROFILE_DISPLAY_PAGE;
    }

    /**
     * Blank out public profile, usually before updating
     *
     */
    protected void blankProperties() {

        this.custGender = (char) 0;
        this.custId = null;
        this.custIssmoker = null;
        this.custLicensedate = null;
        this.custNickname = null;
    }

    /**
     * Intialize Time Interval with 3Months, then return navigation key to
     * Driver ratings page
     *
     * @return
     */
    public String gotoRatingsAsDriver() {

        String param = new RideSearchParamsBean().getBeanNameRatingsearchparam();
        RideSearchParamsBean tb = new RideSearchParamsBean().retrieveCurrentTimeInterval(param);

        tb.setEndDate(new Date(System.currentTimeMillis()));

        // number of milliseconds in ninety days from 
        long ninetyDays = 90l * 24l * 60l * 60l * 1000l;
        long startTime = System.currentTimeMillis() - ninetyDays;
        tb.setStartDate(new Date(startTime));


        return NavigationKeys.driverRatingsDisplay;
    }

    /**
     * Intialize Time Interval with 3Months, then return navigation key to rider
     * ratings page
     *
     * @return
     */
    public String gotoRatingsAsRider() {

        String param = new RideSearchParamsBean().getBeanNameRatingsearchparam();
        RideSearchParamsBean tb = new RideSearchParamsBean().retrieveCurrentTimeInterval(param);

        tb.setEndDate(new Date(System.currentTimeMillis()));

        // number of milliseconds in ninety days 
        long ninetyDays = 90l * 24l * 60l * 60l * 1000l;

        tb.setStartDate(new Date(tb.getEndDate().getTime() - ninetyDays));

        return NavigationKeys.riderRatingsDisplay;
    }

    public List<JRatingBean> getRatingsAsDriverInInterval() {

        String param = new RideSearchParamsBean().getBeanNameRatingsearchparam();
        RideSearchParamsBean tb = new RideSearchParamsBean().retrieveCurrentTimeInterval(param);
        log.log(Level.FINE, "Updated Time Interval " + tb.getStartDateFormatted() + " -> " + tb.getEndDateFormatted());


        JRatingService jrs = new JRatingService();
        return jrs.getRatingsAsDriver(this.getCustId(), tb.getStartDate(), tb.getEndDate());
    }

    public List<JRatingBean> getRatingsAsRiderInInterval() {

        String param = new RideSearchParamsBean().getBeanNameRatingsearchparam();
        RideSearchParamsBean tb = new RideSearchParamsBean().retrieveCurrentTimeInterval(param);
        log.log(Level.FINE, "Updated Time Interval " + tb.getStartDateFormatted() + " -> " + tb.getEndDateFormatted());


        JRatingService jrs = new JRatingService();
        return jrs.getRatingsAsRider(this.getCustId(), tb.getStartDate(), tb.getEndDate());
    }

    /**
     * Do a "smart" update depending what we have.
     * If we have a nickname!= null, then update from nickname,
     * else, if we have a custId, update from Id
     *
     */
    public void smartUpdate() {

        if (this.getCustNickname() != null) {
            this.updateFromCustNickname();
        } else if (this.getCustId() != null) {
            this.updateFromCustId();
        }

    }
} // class