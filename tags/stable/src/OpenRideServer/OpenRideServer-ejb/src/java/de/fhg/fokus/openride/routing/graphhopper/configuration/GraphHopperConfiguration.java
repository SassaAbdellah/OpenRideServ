package de.fhg.fokus.openride.routing.graphhopper.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class providing configured GraphHopperConfiguration
 * 
 * @author jochen
 * 
 */
public class GraphHopperConfiguration {
	
	/** Filename of the ghroute.properties when
	 *  installed inside the sourcetree 
	 *  within de.avci.joride.graphhopper.configuration package
	 * 
	 */
	public static final String FILENAME_GRAPHHOPPER_PROPERTIES="ghrouter.properties";
	
	
	
	/**  Name of the property that contains the filename 
	 *   (without directory name)
	 *   of the PBF file. 
	 *  
	 */
	public static final String PROPERTYNAME_PBFFILE="ghroute.pbf";
			
			
	/**  Name of the directory that contains the PBF file.
	 *   Also, the artifacts used by the graphhopper are 
	 *   created below this directory.
	 *  
	 */			
	public static final String PROPERTYNAME_GHROUTE_BASEDIR="ghroute.basedir";
	
	
	/** Load the properties from properties file installed inside sourcetree
	 *  within de.avci.joride.graphhopper.configuration package
	 * 
	 * @return
	 * @throws IOException
	 */
	public Properties getProperties() {

		try{ 
			InputStream is = GraphHopperConfiguration.class
				.getResourceAsStream("ghrouter.properties");
			Properties props = new Properties();
			props.load(is);
			return props;
		} catch(Exception exc){
			throw new Error("Error when loading Graphhopper Properties : ",exc);
		}
	}
	
	
	/** 
	 * @return Fully qualified name of the Graphhopper Basedir
	 */
	public String getGHRouteBasedirFQN(){
		return this.getProperties().getProperty(PROPERTYNAME_GHROUTE_BASEDIR);
	}
	
	
	/** 
	 * @return Name of the PBF file inside the Graphhopper Basedir
	 */
	public String getGHRoutePBF(){
		
		
		return 
				
		 this.getProperties().getProperty(PROPERTYNAME_GHROUTE_BASEDIR)
		 +"/"
		 +this.getProperties().getProperty(PROPERTYNAME_PBFFILE);
	}
	

}

