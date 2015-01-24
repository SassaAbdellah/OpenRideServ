package de.avci.joride.utils;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Enumeration;

/**
 * convenience methods to load properties from well-known properties files
 *
 * @author jochen
 */
public class PropertiesLoader {

	
	/** A locale to use (may or may not be used)
	 */
	Locale locale=null;
	
	
	/** Construct loader without locale
	 * 
	 */
	public PropertiesLoader(){
		super();
	}
	
	/** Construct loader with special locale.
	 * 
	 * @param locale
	 */
    private PropertiesLoader(Locale locale) {
		super();
		this.locale=locale;
	}



    /**
     * Load a ressourcebundle from the classpath
     *
     * @param bundlename name of the bundle to use
     * 
     * @return
     */ 
    private ResourceBundle loadResourceBundleByName(String bundlename) {
    	
    	if(locale!=null){
    		return ResourceBundle.getBundle(bundlename, locale );
    	}else{
    		return ResourceBundle.getBundle(bundlename);
    	}
    }

    
    
    
    
    
    /**
     * Create Properties from a given RessourceBundle
     *
     * @param ResourceBundle rb
     * @return a Properties Object generated from RessourceBundl
     */
    private Properties getPropertiesFromRessourceBundle(ResourceBundle rb) {

        Properties res = new Properties();
        Enumeration<String> keys = rb.getKeys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            res.put(key, rb.getObject(key));
        }
        return res;
    }
   
    
    


    
    
    
    /** Hashtable providing messages for all supported locales
     * 
     */
    private static Hashtable <Locale,Properties> messageCache = new Hashtable <Locale,Properties> ();
  
    
    /**
     * Place where the messages_xx.properties files are located in the code
     */
    private static final String MESSAGES_URL = "de.avci.joride.messages";
    	
    /** getPro
     * 
     * @param locale
     * @return
     */
    private static Properties loadMessageProperties(Locale locale){
    	
    	
    	if(locale==null){
    		
    		throw new Error("Cannot load locale in joride's properties loader --  locale is null");
    	}
    	
    		
    	Properties props=messageCache.get(locale);
    	
    	
    	if(locale == null){
    		
    		throw new Error("Cannot load localized Message in joride, locale is null");
    	}
    	
    	
    	if(props==null){
    		  PropertiesLoader loader=new PropertiesLoader(locale);
    		  ResourceBundle rb =loader.loadResourceBundleByName(MESSAGES_URL);
    	      props = loader.getPropertiesFromRessourceBundle(rb);
    	      messageCache.put(locale, props);
    	      
    	      if (props==null) {
    	    	  throw new Error("Cannot load Properties to Cache for Locale "+locale);
    	      }
    	}
    	return props;
    }
    
    /** Return Messagess from Cache. If no such Properties are in the cache,
     *  load Properties to cache first, then return them.
     * 
     * @param locale
     * @return
     */
    public static Properties getMessageProperties(Locale locale){
    	return loadMessageProperties(locale);
    }
    
    
    /**  Place where the navigation.properties file is located in the code
     */
    private static final String NAVIGATION_URL = "de.avci.joride.navigation";
 
    
    /** Properties for Navigation (independend of locale)
     * 
     */
   private static Properties navigationProperties=null;
  
       	
    /** Get Navigation properties. If necessary, instantiate them first
     * 
     * @param locale
     * @return
     */
    private static Properties loadNavigationProperties(){
    	
    	Properties props=navigationProperties;
    			
    	
    	if(props==null){
    		  PropertiesLoader loader=new PropertiesLoader();
    		  ResourceBundle rb =loader.loadResourceBundleByName(NAVIGATION_URL);
    		  props=loader.getPropertiesFromRessourceBundle(rb);
    	      navigationProperties=props;
    	      
    	      if (props==null) {
    	    	  throw new Error("Cannot load NavigationProperties");
    	      }
    	}
    	return props;
    }
    
    /** Return Navigation Properties from Cache. If no such Properties are in the cache,
     *  load Properties to cache.
     * 
     * @param locale
     * @return
     */
    public static Properties getNavigationProperties(){
    	return loadNavigationProperties();
    }
    
    
    
    
    
    /**
     * Where the navigation.properties file is located in the code
     */
    private static final String OPERATIONAL_URL = "de.avci.joride.operational";

    
    /** Properties for Operations (indepentend of locale)
     * 
     */
   private static Properties operationalProperties=null;
  
       	
    /** get operational properties. If necessary instantiate them first.
     * 
     * @param locale
     * @return
     */
    private static Properties loadOperationalProperties(){
    	
    	Properties props=operationalProperties;
    			
    	
    	if(props==null){
    		  PropertiesLoader loader=new PropertiesLoader();
    		  ResourceBundle rb =loader.loadResourceBundleByName(OPERATIONAL_URL);
    		  props=loader.getPropertiesFromRessourceBundle(rb);
    	      operationalProperties=props;
    	      
    	      if (props==null) {
    	    	  throw new Error("Cannot load OperationalProperties");
    	      }
    	}
    	return props;
    }
    
    /** Return Operational Properties. If no such Properties exist 
     *  create them.
     * 
     * @param locale
     * @return
     */
    public static Properties getOperationalProperties(){
    	return loadOperationalProperties();
    }
    
    
    
    
    /**
     * Place Where the update.properties file is located in the code
     */
    private static final String UPDATES_URL = "de.avci.joride.update";

    
    
    /** Properties for Operations (indepentend of locale)
     * 
     */
   private static Properties updateProperties=null;
  
       	
    /** get update properties. If necessary, instantiate them first
     * 
     * @param locale
     * @return
     */
    private static Properties loadUpdateProperties(){
    	
    	Properties props=updateProperties;
    			
    	
    	if(props==null){
    		  PropertiesLoader loader=new PropertiesLoader();
    		  ResourceBundle rb =loader.loadResourceBundleByName(UPDATES_URL);
    		  props=loader.getPropertiesFromRessourceBundle(rb);
    	      updateProperties=props;
    	      
    	      if (props==null) {
    	    	  throw new Error("Cannot load UpdateProperties");
    	      }
    	}
    	return props;
    }
    
    /** Return Operational Properties. If no such Properties exist 
     *  create them.
     * 
     * @param locale
     * @return
     */
    public static Properties getUpdateProperties(){
    	return loadUpdateProperties();
    }
    
    

    /**
     * Where the update.properties file is located in the code
     */
    private static final String DATETIME_URL = "de.avci.joride.datetime";
    
    
    
    
    /** Properties for Datetimes (indepentend of locale)
     * 
     */
   private static Properties datetimeProperties=null;
  
       	
    /** get operational properties
     * 
     * @param locale
     * @return
     */
    private static Properties loadDateTimeProperties(){
    	
    	Properties props=datetimeProperties;
    			
    	
    	if(props==null){
    		  PropertiesLoader loader=new PropertiesLoader();
    		  ResourceBundle rb =loader.loadResourceBundleByName(DATETIME_URL);
    		  props=loader.getPropertiesFromRessourceBundle(rb);
    	      datetimeProperties=props;
    	      
    	      if (props==null) {
    	    	  throw new Error("Cannot load DatetimeProperties");
    	      }
    	}
    	return props;
    }
    
    /** Return Date/Time Properties for given locale. If no such Properties exist create them.
     * 
     * @param locale
     * @return
     */
    public static Properties getDatetimeProperties(){
    	return loadDateTimeProperties();
    }
    
    
    
}
