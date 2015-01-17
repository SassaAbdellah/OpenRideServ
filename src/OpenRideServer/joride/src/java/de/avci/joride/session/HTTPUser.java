package de.avci.joride.session;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import de.avci.joride.constants.JoRideConstants;
import de.avci.joride.jbeans.customerprofile.JCustomerEntityService;
import de.avci.joride.utils.HTTPUtil;
import de.avci.joride.utils.PropertiesLoader;

/**
 * A simplistic bean allowing access to HTTPAuthData
 *
 *
 */
@Named("HTTPUser")
@SessionScoped
public class HTTPUser implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2141634023951952843L;

    public String getCurrentUser() {

        Principal principal = getUserPrincipal();
        if (principal == null) {
            return null;
        }

        return principal.getName();
    }

    public Principal getUserPrincipal() {

        if (getFacesContext() != null) {
            return getFacesContext().getExternalContext().getUserPrincipal();
        }

        return null;
    }

    public FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    
    
    /** 
     * 
     * @return  true, if httprequest has a non-null user principal, else false
     */
    public boolean isLoggedIn(){
        return getUserPrincipal()!=null;
    }
    
    public String getLoginLabel() {
        if (getUserPrincipal() == null) {
        	java.util.Locale myLocale=new HTTPUtil().detectBestLocale();
            return (PropertiesLoader.getMessageProperties(myLocale).getProperty("login"));
        }
        return null;
    }
    
    
    /**  URLBase is the base of the webapplication,
     *   i,e "/joride" for joride webapp,
     *   "/joride-public/" for joride-public webapp, etc
     *   It is defined in navigation.properties file.
     * 
     * 
     * @return 
     */
    public String getURLBase(){
         
           return PropertiesLoader.getNavigationProperties().getProperty("urlBase");
    }
    
    
    /**  LoginURL is the URL where Users should be sent to log in.
     * 
     *   It is defined in navigation.properties file.
     * 
     * 
     * @return 
     */
    
    public String getLoginURL() {
             
            String urlLogin=PropertiesLoader.getNavigationProperties().getProperty("urlLogin");
            return getURLBase()+urlLogin;
            
    }
    

   
     public String getLogoutLabel() {
        if (getUserPrincipal() != null) {
        	java.util.Locale myLocale=new HTTPUtil().detectBestLocale();
            return PropertiesLoader.getMessageProperties(myLocale).getProperty("logout");
        }
        return null;
    }
    
    
    public String getLogoutURL() {
        
            String urlLogout=PropertiesLoader.getNavigationProperties().getProperty("urlLogout");        
            return urlLogout;
    } 
     
     
  
    public String getLoggedOutURL() {
            
            String urlLoggedOut=PropertiesLoader.getNavigationProperties().getProperty("urlLoggedOut");
            return urlLoggedOut;
    } 

    /** URL where the smartLoginServlet lives
     * 
     * @return
     */
    public String getSmartLoginURL() {
        
        String smartLoginUrl1=PropertiesLoader.getNavigationProperties().getProperty("urlSmartLogin");
        return getURLBase()+smartLoginUrl1;
} 
      
    
      /** Invalidate Session, then forward to "loggedOutURL"
       * 
       * @return 
       */
    public String logOut(){
    
        HTTPUtil hpu=new HTTPUtil();
        HttpSession session=hpu.getHTTPServletRequest().getSession();
        session.invalidate();
        try { hpu.getHTTPServletResponse().sendRedirect(getLoggedOutURL());
        } catch (IOException exc) {
            throw new Error("Unexpected Error", exc);
        }
        
        return "logout";
       
    }
    
    
        /** URL from which the OpenLayers library should be included
     *  this is now configurable via the urlOpenLayers property
     *  in navigation.properties
     *  
     */
    public String getOpenLayersURL() {


        PropertiesLoader pl = new PropertiesLoader();

        System.err.println("TODO: navigation props: " + PropertiesLoader.getNavigationProperties());

        String urlLoggedOut = PropertiesLoader.getNavigationProperties().getProperty("urlOpenLayers");
        return urlLoggedOut;
    }
    
     
    /** Flag signifying wether or not maps should be shown.
     *  I.e: Maps are cool and should always be shown, unless there
     *  is not enough bandwidth.
     *  
     *  Since such conditions can be expected to last for
     *  
     * 
     */
    private boolean showMap=false;

	public boolean isShowMap() {
		return showMap;
	}

	public void setShowMap(boolean showMap) {
		this.showMap = showMap;
	}
    
	
	public void toggleMapVisibility(ActionEvent evt){
		this.setShowMap(!this.isShowMap());
		
		System.err.println("TODO: toggled showMapsProperty, current property is "+this.isShowMap());
	}
    
    /** return inversion of isShowMap
     */
    public boolean isHideMap(){
    	return !this.isShowMap();
    }
    
    /** Return the timezone for this session
     * 
     */
    public TimeZone getTimeZone(){
    	return JoRideConstants.getTimeZone();
    }
    
    
    /** Return the list of supported locales
     */
    public Locale[] getSupportedLocales(){
    		
    	return new JCustomerEntityService().getSupportedLocales();
    }
    
    
    /**
     * 
     * @return list of supported languages as Select Items
     */
    public List <SelectItem> getSupportedLanguages(){
    	
    	
    	Locale[] supportedLocales=new JCustomerEntityService().getSupportedLocales();
    	
    	ArrayList <SelectItem> res = new ArrayList<SelectItem> ();
    	
    	for (Locale l: supportedLocales){
    		SelectItem s=new SelectItem(l, l.getDisplayLanguage(l));
    		res.add(s);    
    	}
    	
    	return res;
    } 
    
    
    
    
    
    
} // class
