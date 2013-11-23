package de.avci.joride.session;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




/** A simple Servlet that manages HTTP Form login for joride.
 *  Typically, this will check a given email adress against 
 *  customer tables, and so allow login with username or email (alternatively) 
 *
 *
 */
public class LoginServlet extends HttpServlet {
	
    transient Logger log = Logger.getLogger(this.getClass().getCanonicalName());

    
    
        /** The (JEE Standard) URL where the j_security_check lives.
         *  
         * 
         * 
         */
        public static final String J_SECURITY_CHECK_URL="j_security_check";
    
        /** The j_username parameter from HTTP Form Login
         */
        public static final String PARAM_J_USERNAME="j_username"; 
        
        /** The j_password parameter from HTTP Form Login
         */
        public static final String PARAM_J_PASSWORD="j_password"; 
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4907032788978484525L;


	@Override

	public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException {
		this.processRequest(request, response);
	}
	
	@Override

	public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException {
		this.processRequest(request, response);
	}
	
	
	
	
	/** Dummy. Once finished, it is supposed to do the following
         * 
         *  <ol>
         *  <li> Check if j_username parameter is a valid email adress                </li>
         *  <li> If so, look up username from email in database                       </li>
         *  <li> If there is a mapping, then replace j_username with looked up value  </li>
         *  <li> send j_username and j_password to j_security check URL                </li>
         *  </ol>
         * 
         * 
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 * 
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException  {
		
            String targetURL=request.getContextPath()+"/j_security_check";
            log.log(Level.FINE,"target url : "+targetURL);
           
            
            //
            //request.setAttribute(PARAM_J_USERNAME,"jochen");
            // request.setAttribute(PARAM_J_PASSWORD,"jochen");
            //
            // throw new Error(  
            //        "login by smart servlet not yet implemented\n"+
            //       "target url : "+targetURL
            //        );
            
  
        
       
            response.sendRedirect(targetURL);
            
            
            
	} // processRequest
	
	
}  // class