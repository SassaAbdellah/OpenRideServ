package de.avci.openrideshare.utils;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import de.fhg.fokus.openride.routing.graphhopper.configuration.GraphHopperConfiguration;




/** Class to provide a list of supported languages
 * 
 * @author jochen
 *
 */
public class SupportedLanguagesFactory {

	
	/** List of supported languages. To be loaded lazily.
	 * 
	 */
	private static Locale[] supportedLanguages=null;
	
	/** (Lazily) load the list of supported languages
	 * 
	 * @return
	 */
	private static void loadSupportedLanguages(){
	
		
		try{ 
			InputStream is = SupportedLanguagesFactory.class.getResourceAsStream("supportedLanguages.properties");
			Properties props = new Properties();
			props.load(is);
		
			String csv=props.getProperty("supported.languages");
			String[] supportedLanguagesStr=csv.split("\\,");
		
			supportedLanguages=new Locale[supportedLanguagesStr.length];
			
			for(int i=0; i< supportedLanguagesStr.length; i++){
				String languageTag=supportedLanguagesStr[i];
				Locale locale=Locale.forLanguageTag(languageTag);
				supportedLanguages[i]=locale;
			}
			
		
		} catch(Exception exc){
			throw new Error("Error when loading supported  languagesProperties : ",exc);
		}
		
	}
	
	
	/** Accessor with lazy instantiation
	 * 
	 * @return
	 */
	public static Locale[] getSupportedLanguages(){
		
		if(supportedLanguages==null){
			loadSupportedLanguages();
		}	
		return supportedLanguages;
	}
	
}
