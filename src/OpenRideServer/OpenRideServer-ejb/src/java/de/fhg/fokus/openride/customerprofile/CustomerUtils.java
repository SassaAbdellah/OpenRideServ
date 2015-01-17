package de.fhg.fokus.openride.customerprofile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



/** Method to support login with technologies other than User/password
 * 
 * 
 *       
 * 
 * @author jochen
 *
 */
public class CustomerUtils {


	
	/** "ErrorCode" to be returned if customer creation failed.
	 *   Because of terms&conditions not acceppted.
	 *   
	 */
	public static final int CUSTCREATION_TERMS_NOT_ACCEPTED=-1;
	
	/** "ErrorCode" to be returned if customer creation failed.
	 *   Because of nickname not compliant to sysntax rules
	 */
	public static final int CUSTCREATION_NICKNAME_SYNTAX=-2;
	
	/** "ErrorCode" to be returned if customer creation failed.
	 *   Because of email not compliant to syntax rules
	 */
	public static final int CUSTCREATION_EMAIL_SYNTAX=-3;
	
	/** "ErrorCode" to be returned if customer creation failed.
	 *   Because of nickname already exists
	 */
	public static final int CUSTCREATION_NICKNAME_EXISTS=-4;
	
	/** "ErrorCode" to be returned if customer creation failed.
	 *   Because of email already exists.
	 */
	public static final int CUSTCREATION_EMAIL_EXISTS=-5;
	


	
	/** Check if argument is an email adress.
	 *  For the time beeing, an email address is everything that 
	 *  does contain an ''
	 * 
	 * @param arg
	 * @return
	 */
	public static boolean isValidEmailAdress(String arg){
		
		
		if(arg!=null && (arg.contains("@"))) {
			return true;
		}
		
		return false;
	}
	
	
	/** Nicknames must begin with a (non-national) 
	 *  character [a-z] and add 
	 *  at least 4 and at most 11 alphanumerical characters,
	 *  so that there may at least 5 and at most 12 characters.  
	 * 
	 */
	public static String NICKNAME_PATTERN="[a-z]+[a-z0-9]{4,11}";
	
	/** Precompiled pattern for nicknames
	 */
	private static Pattern nicknamePattern=Pattern.compile(NICKNAME_PATTERN);
	
	/** Check if nickname is valid according to the rule set up before.
	 * 
	 * @param arg
	 * @return
	 */
	public static boolean isValidNickname(String arg){
	
        Matcher matcher = nicknamePattern.matcher(arg);
        boolean found = nicknamePattern.matcher(arg).matches();
        return found;
	}
	
	
	

	
	

}
