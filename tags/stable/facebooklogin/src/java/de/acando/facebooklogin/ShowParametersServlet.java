package de.acando.facebooklogin;


import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




/** Servlet to show the parameters of a http request
 * 
 * 
 *
 */
public class ShowParametersServlet extends HttpServlet {
	
    transient Logger log = Logger.getLogger(this.getClass().getCanonicalName());

    
    
      
	
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
	
	
	

	


	
	
	
	
	/** Process Request, regardless wether GET or POST request
	 * 
	 * @param request
	 * @param response
	 * @throws java.io.IOException
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException  {
		

		response.getWriter().println("==== ShowParameterServlet   ======== ");
		for (String parameter : request.getParameterMap().keySet()){
			response.getWriter().print("\n\n Param : "+parameter+"\n");
			response.getWriter().print("\n Value : "+request.getParameter(parameter)+"\n");
			
			
		}
				
	}
	
	

	
	
	
	

	
	
	
}
            