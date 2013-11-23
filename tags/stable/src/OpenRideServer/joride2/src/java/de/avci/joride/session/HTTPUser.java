package de.avci.joride.session;

import de.avci.joride.utils.HTTPUtil;
import de.avci.joride.utils.PropertiesLoader;
import java.io.IOException;

import java.io.Serializable;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

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
            return (new PropertiesLoader()).getMessagesProps().getProperty("login");
        }
        return null;
    }
    
    public String getLoginURL() {
     
            PropertiesLoader pl=new PropertiesLoader();
         
            String urlBase=pl.getNavigationProps().getProperty("urlBase");
            String urlLogin=pl.getNavigationProps().getProperty("urlLogin");
            
            return urlBase+urlLogin;
            
    }
    

   
     public String getLogoutLabel() {
        if (getUserPrincipal() != null) {
            return (new PropertiesLoader()).getMessagesProps().getProperty("logout");
        }
        return null;
    }
    
    
    public String getLogoutURL() {
        
            PropertiesLoader pl=new PropertiesLoader();
            
            String urlBase=pl.getNavigationProps().getProperty("urlBase");
            String urlLogout=pl.getNavigationProps().getProperty("urlLogout");
            
            return urlBase+urlLogout;
    } 
     
     
    
    
      public String getLoggedOutURL() {
        
            PropertiesLoader pl=new PropertiesLoader();
            
            String urlBase=pl.getNavigationProps().getProperty("urlBaseJoridePublic");
            String urlLogout=pl.getNavigationProps().getProperty("urlLoggedOut");
            
            return urlBase+urlLogout;
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
    
     
    
} // class
