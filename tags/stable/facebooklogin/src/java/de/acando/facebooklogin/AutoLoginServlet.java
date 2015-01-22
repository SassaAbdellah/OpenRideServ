package de.acando.facebooklogin;


import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;


/**
 * 
 * @author jochen
 *
 */
public class AutoLoginServlet extends HttpServlet {
	
    transient Logger log = Logger.getLogger(this.getClass().getCanonicalName());

    
    
    
    
    
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4907032788978484525L;
	
	
	
	/** Redirect URL pointing back to **this* servlet
	 * 
	 */
	
	private static final String MY_REDIRECT_URL= OAuth2Constants.getBaseURL()+"/autologin/";


	
	
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
	
	
	/** True, if request has parameter given by name, else false
	 * 
	 * @param request
	 * @param paramName
	 * @return
	 */
	private boolean hasParam(HttpServletRequest request, String paramName){
		return request.getParameter(paramName) != null;	
	}
	
	
	/** Wrapping request.getParam("parametername")
	 * 
	 * @param request
	 * @param paramName
	 * @return
	 */
	private String getRequestParam(HttpServletRequest request,String paramName) {
		
		return
		request.getParameter(paramName);
	}
	

	
	
	/** True, if request has neither authencation_token, nor code parameter.
	 *  We call this state0 and initialze an OAuth Request to fb
	 * 
	 * @return
	 */
	private boolean isStep0(HttpServletRequest request){
		
		
		return 
				
				!(hasParam(request, OAuth2Constants.PARAM_NAME_code))
				&
				!(hasParam(request, OAuth2Constants.PARAM_NAME_token));
				
	}

	
	/** True, if request has codeParameter, but no authentication tooken,
	 *  so next step is to exchange code for authentication token.
	 * 
	 * @return
	 */
	private boolean isStep1(HttpServletRequest request){
		
		return
				
		(hasParam(request, OAuth2Constants.PARAM_NAME_code))
		&
		!(hasParam(request, OAuth2Constants.PARAM_NAME_token));
		
	}
	
	
	/** True, if request has  authentication tooken,
	 *  
	 *  Next step might doing stuff with the authentication token
	 * 
	 * @return
	 */
	private boolean isStep2(HttpServletRequest request){
		
		return (hasParam(request, OAuth2Constants.PARAM_NAME_token));
		
	}
	
	
	
	
	/** Process Request, 
	 *  
	 *  This is designed to exchange 
	 * 
	 * @param request
	 * @param response
	 * @throws java.io.IOException
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException  {
		

		
		response.getWriter().print("AutoLogin2!");
		
		
		// step0 : redirect to fratzbuch authorization page, 
		// fetch code
		
		if(this.isStep0(request)){
			
				URIBuilder ub=new URIBuilder();
				ub.setPath(OAuth2Constants.FACEBOOK_OAuth2_URL);
				// add parameters
				ub.addParameter(OAuth2Constants.PARAM_NAME_client_id,OAuth2Constants.getClientId());
				ub.addParameter(OAuth2Constants.PARAM_NAME_redirect_uri,MY_REDIRECT_URL);
				String url;
				try { url = ub.build().toASCIIString();
				} catch (URISyntaxException exc) {
					throw new Error(exc);
				}
				response.sendRedirect(url);
		}
		
		
		
		
		
		// step 1: exchangine the code for an authentication tooken
		//
		if(this.isStep1(request)){
		
			String path="https://graph.facebook.com/oauth/access_token";
			// add parameters
			HashMap<String,String> params=new HashMap<String,String>();
			params.put(OAuth2Constants.PARAM_NAME_client_id,OAuth2Constants.getClientId());
			params.put("client_secret",OAuth2Constants.getAppSecret());
			// todo: what else but authorization code to try here?
			params.put("grant_type","authorization_code");
			params.put(OAuth2Constants.PARAM_NAME_redirect_uri,MY_REDIRECT_URL);
			params.put(OAuth2Constants.PARAM_NAME_code,getRequestParam(request, OAuth2Constants.PARAM_NAME_code));
			
			
			// one of two things to do here:
			
			// 1: send redirect, so that the authentication token is visible in the browser
			// response.sendRedirect(url);
			
			// 2: fetch the token myself, so that we have it availlable locally
			
			String authorization_token="uninitialized";
			
			try {

				authorization_token=HTTPClient.fetchAsStringHttpGet(path,params);
			} catch (URISyntaxException exc) {
				throw new Error(exc);
			}

			// So, we have the authorization tooken.
			// maybe, do something more sophisticated,
			// like sending to debug graph api?
			response.getWriter().print(authorization_token);

		}
				
		response.getWriter().close();
		
	}


	
	
	
	
}
            