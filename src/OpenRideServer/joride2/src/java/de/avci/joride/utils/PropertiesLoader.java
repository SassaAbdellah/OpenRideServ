package de.avci.joride.utils;

import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Enumeration;

/**
 * convenience methods to load properties from well-known properties files
 *
 * @author jochen
 */
public class PropertiesLoader {

    /**
     * Load a ressourcebundle from the classpath
     *
     * @param bundlename
     * @return
     */
    private ResourceBundle loadResourceBundleByName(String bundlename) {

        return ResourceBundle.getBundle(bundlename);
    }

    /**
     * Create Properties from a given RessourceBundle
     *
     * @param ResourceBundle rb
     * @return a Properties Object generated from RessourceBundl
     */
    Properties getPropertiesFromRessourceBundle(ResourceBundle rb) {

        Properties res = new Properties();
        Enumeration<String> keys = rb.getKeys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            res.put(key, rb.getObject(key));
        }
        return res;
    }
    /**
     * Where the navigation.properties file is located in the code
     */
    public static final String NAVIGATION_URL = "de.avci.joride.navigation";

    /**
     * Load the Navigation Properties
     *
     * @return the Navigation Properties as
     */
    public Properties getNavigationProps() {

        ResourceBundle rb = loadResourceBundleByName(NAVIGATION_URL);
        Properties props = getPropertiesFromRessourceBundle(rb);
        return props;
    }
    /**
     * Where the messages.properties file is located in the code
     */
    public static final String MESSAGES_URL = "de.avci.joride.messages";

    /**
     * Load the Messages Properties
     *
     * @return the Messages Properties as
     */
    public Properties getMessagesProps() {

        ResourceBundle rb = loadResourceBundleByName(MESSAGES_URL);
        Properties props = getPropertiesFromRessourceBundle(rb);
        return props;
    }
    /**
     * Where the navigation.properties file is located in the code
     */
    public static final String OPERATIONAL_URL = "de.avci.joride.operational";

    /**
     * Load the Operational Properties
     *
     */
    public Properties getOperationalProps() {

        ResourceBundle rb = loadResourceBundleByName(OPERATIONAL_URL);
        Properties props = getPropertiesFromRessourceBundle(rb);
        return props;
    }
    /**
     * Where the update.properties file is located in the code
     */
    public static final String UPDATES_URL = "de.avci.joride.update";

    /**
     * Load the Update Properties
     *
     */
    public Properties getUpdateProps() {

        ResourceBundle rb = loadResourceBundleByName(UPDATES_URL);
        Properties props = getPropertiesFromRessourceBundle(rb);
        return props;
    }
}
