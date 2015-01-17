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




/** Servlet to digest an OAuth2 Authorization response
 * 
 * 
 *
 */
public class AuthorizationResponseServlet extends HttpServlet {
	
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
	
	
	
	/** Classify HTTPRequest as "AuthorizationErrorResponse"
	 */
	private boolean isAuthorizationErrorResponse (HttpServletRequest req){
		String error=req.getParameter(OAuth2Constants.PARAM_NAME_error);
		return error!=null;
	}
	
	

	/** Classify HTTPRequest as "AuthorizationErrorResponse"
	 */
	private boolean isAuthorizationResponse (HttpServletRequest req){
	
		String code=req.getParameter(OAuth2Constants.PARAM_NAME_code);
		String state=req.getParameter(OAuth2Constants.PARAM_NAME_state);
		String token=req.getParameter(OAuth2Constants.PARAM_NAME_token);
		
		// coarse test, could possibly be done better
		return (code!=null && token==null );
	}
	
	
	
	
	/** Process Request, regardless wether GET or POST request
	 * 
	 * @param request
	 * @param response
	 * @throws java.io.IOException
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException  {
		
		
			// Handle AuthorizationErrorRespnse
			if(isAuthorizationErrorResponse(request)){
			 this.processAuthorizationErrorResponse(request);
			 return;
			}
			
			// HandleAuthorizationResponse
			if(isAuthorizationResponse(request)){
				this.processAuthorizationResponse(request, response);
				return;
			}
			
		
			
			// cannot classify, croak!
			
			throw new Error("Cannot classify request");
			
		
			// things to make it feasible to walk through this using the debugger
			//Map<String, String[]> myParamMap=request.getParameterMap();
			// Set<String> paramNames=myParamMap.keySet();
		
			

		

		
	}
	
	
	/** Handle a request that has been previously classified as Authorization Error Response
	 * 
	 * @param request
	 */
	private void processAuthorizationErrorResponse(HttpServletRequest request){
		String error=request.getParameter(OAuth2Constants.PARAM_NAME_error);
		String errorReason=request.getParameter(OAuth2Constants.PARAM_NAME_error_reason );	
		
		String errormessage="\nAn Error Occured during Handshake : "+
		"\nError Code   : "+error+
		"\nError Reason : "+errorReason;
		
		throw new Error(errormessage);
	}
	
	
	
	
	/** Handle a request that has been previously classified as AuthorizationResponse
	 * 
	 * @param request
	 */
	private void processAuthorizationResponse(HttpServletRequest request, HttpServletResponse response){
	
		
		// currently nothing useful here, just redirecting
		// temporarily redirect to step1.jsp, so we se we are through with Authorization request
		// next should be get the token
		
	
		try {
			response.sendRedirect("/facebooklogin/step1.jsp");
		} catch (IOException exc) {
			throw new Error("error while processing response",exc);
		}
		
		
	}
	
	
	
}
            