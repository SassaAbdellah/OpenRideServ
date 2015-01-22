package de.acando.facebooklogin;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;


/** OAuth 2 constants
 * 
 * @author jola2de
 *
 */
public class OAuth2Constants implements Serializable {
	

	/** Properties file to load facebooklogin properties from.
	 */
	public static final String facebookloginPropertiesFile="facebooklogin.properties";
	
	/** Dumb default serialVersionUID 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/** URL on facebook where the OAuth2 stuff is done
	 */
	public  final static String FACEBOOK_OAuth2_URL="https://www.facebook.com/dialog/oauth";
			
	public String getOAuth2URL(){return FACEBOOK_OAuth2_URL;}
	
	
    
	/** String containing the App Secret. Will be loaded from properties file
	 */
	public static final String PropertyNameAppSecret="app.secret";
	
	
	/**  AppSecret in OAuth/Facebook
	 * 
	 */
	private static String appSecret=null;
	
	public static String  getAppSecret(){
		
		if (appSecret==null){
			
			InputStream instream=(new OAuth2Constants()).getClass().getClassLoader().getResourceAsStream(facebookloginPropertiesFile);
			
			Properties props=new Properties();
			try {
				props.load(instream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			appSecret=props.getProperty(PropertyNameAppSecret);
		}
			
		return appSecret;
	}
	
	
	
	   
	/** String containing the App Secret. Will be loaded from properties file
	*/
	public static final String PropertyNameAppBaseURL="app.baseURL";
		
	
	
	/**  base url in OAuth/Facebook.
	 *   
	 *   This is read from local properties file
	 *   This is also the value under which the application is known to facebook.
	 *   
	 */
	private static String baseURL=null;
	
	public static String  getBaseURL(){
		
		if (baseURL==null){
			
			InputStream instream=(new OAuth2Constants()).getClass().getClassLoader().getResourceAsStream(facebookloginPropertiesFile);
			
			Properties props=new Properties();
			try {
				props.load(instream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			baseURL=props.getProperty(PropertyNameAppBaseURL);
		}
			
		return baseURL;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/**  Client Id in OAuth, aka App Id in facebook. 
	 *   Must be sent to FB to receive a  code
	 */
	private static  String clientId=null;
	
	
	/** Name of clientId/applicationId inside property file
	 */
	public static final String PropertyNameAppId="app.id";
	
	
	public static String getClientId(){ 
		
		if (clientId==null){
			
			InputStream instream=(new OAuth2Constants()).getClass().getResourceAsStream(facebookloginPropertiesFile);
			
			Properties props=new Properties();
			try {
				props.load(instream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			clientId=props.getProperty(PropertyNameAppId);
		}
			
		return clientId;
	}
		
		
	
    
    
    
	/** Application Access Token Uri.
	 *  This is where to get an Application Access token Uri
	 *  (which allows us to examinate the access token and the like)
	 * 
	 *  Note also, that client secret must be passed over https!
	 * 
	 */
    public static final String FACEBOOK_app_access_token_uri="https://graph.facebook.com/oauth/access_token?client_id="+getClientId()+"&client_secret="+getAppSecret()+"&grant_type=client_credentials";
    
    public String getFacebookAppAccessTokenUri(){return FACEBOOK_app_access_token_uri;}
    
    
    
    /** Application Access Token Uri.
	 *  This is where to get an Application Access token Uri
	 *  (which allows us to examinate the access token and the like)
	 * 
	 *  Note also, that client secret must be passed over https!
	 *  
	 *  GET graph.facebook.com/debug_token?input_token={token-to-inspect}&access_token={app-token-or-admin-token}
	 * 
	 */
    public static final String FACEBOOK_inspect_access_token_uri="https://graph.facebook.com/debug_token";
    
    public String getFacebookInspectAccessTokenUri(){return FACEBOOK_inspect_access_token_uri;}
    

	
	
	
	
	/** Name of the client_id param, as used by OAuth2 Authrization Response;
	 */
	public static final String  PARAM_NAME_client_id="client_id";
		
	public String getParamNameClientId(){return PARAM_NAME_client_id;}
	
	
	
	/** Name of the client_id param, as used by OAuth2 Authrization Response;
	 */
	public static final String  PARAM_NAME_redirect_uri="redirect_uri";
		
	public String getParamNameRedirectURI(){return PARAM_NAME_redirect_uri;}
	
	
	
	/** Name of the state param, as used by OAuth2 Authrization Response;
	 */
	public static final String  PARAM_NAME_state="state";
		
	public String getParamNameState(){return PARAM_NAME_state;}
	
	
	
	/** Name of the code param, as used by OAuth2 Authorization Response;
	 */
	public static final String  PARAM_NAME_code="code";

	public String getParamNameCode(){return PARAM_NAME_code;}
	
	
	
	/** Non-human friendly error codes as defined by OAuth2
	 * 
	 */
	public static final String PARAM_NAME_error = "error";
	
	public String getParamNameError(){return PARAM_NAME_error;}
	
	

	/** human friendly error message as defined by OAuth2
	 * 
	 */
	public static final String PARAM_NAME_error_reason = "error_reason";
	
	public String getParamNameErrorReason(){return PARAM_NAME_error_reason;}
	
	
	
	/** Name of the token param, as used by OAuth2 Authorization Response;
	 */
	public static final String  PARAM_NAME_token="token";
	
	public String getParamNameToken(){return PARAM_NAME_token;}
	
	
	
	/**  base url in OAuth/Facebook. Base URL for GraphAPI on facebook */
	private static String GRAPH_API_BASE_URL_ME="https://graph.facebook.com/me/";
	
	public static String  getGraphApiBaseURLMe(){
		
		return GRAPH_API_BASE_URL_ME;
	}
	
	
	
	
	
	public OAuth2Constants(){
		super();
	}
	
}
