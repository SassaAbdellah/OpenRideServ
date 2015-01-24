package de.avci.joride.session;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.avci.joride.jbeans.customerprofile.JCustomerEntityService;
import de.avci.joride.utils.PropertiesLoader;
import de.fhg.fokus.openride.customerprofile.CustomerUtils;

public class SmartLoginServlet extends HttpServlet {

	/** Standard j_username parameter for logging in
	 */
	public static final String j_username="j_username";
	
	/** Standard j_password parameter for logging in
	 */
	public static final String j_password="j_password";
	
	
	
	/** Base Url  for joride application
	 */
	public static final String baseURL=PropertiesLoader.getNavigationProperties().getProperty("urlBase");
	
	
	/** Noauth URL for joride application. This is where failed requests are sent.
	 */
	public static  final String urlNoauth=baseURL+PropertiesLoader.getNavigationProperties().getProperty("urlNoauth");
	
	
	
	
	/** Initial servlet configuration
	 * 
	 */
	
	private ServletConfig servletConfig;
	
	@Override
	
	public void init(ServletConfig arg){
			this.servletConfig=arg;
	}

	
	
	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	public void setServletConfig(ServletConfig servletConfig) {
		this.servletConfig = servletConfig;
	}
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response){
		// delegate to processRequest
		this.processRequest(request, response);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response){
		// delegate to processRequest
		this.processRequest(request, response);
	}
	
	
	
	
	
	/** Do stuff with request and response
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response){
		
		
			
		String usernameOriginal=request.getParameter(j_username);
		String password=request.getParameter(j_password);
		
		//
		boolean login_success=false;
		
		//
		//
		//
		try{
			
			String username=usernameOriginal;
			
			// exchange email address to local nickname
			if(CustomerUtils.isValidEmailAdress(username)){
				
				String nickname=(new JCustomerEntityService()).getNicknameByEmail(username);
				if(nickname!=null){
					username=nickname;
				}
			}
			
			request.login(username, password);
			login_success=true;
		} catch (ServletException servletExc){
			
			// TODO: do something more decent here
			
			
		}
		
		try {
			
			
			
			
			if(login_success){
				//
				// programmatic login successful, 
				//
				//
				response.sendRedirect(baseURL);
			} else {
				//
				// login failed, send response to noauthURL
				//	
				response.sendRedirect(urlNoauth);
			}
		} catch (IOException exc) {
			// TODO: do something more decent here
			throw new Error("Error while sending redirect", exc);
		}
		
	}
	
	
	
	
	
	
	

}
