/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.utils;

import java.io.Serializable;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

/**
 * JSF Bean providing constants for Joride Navigation.
 *
 *
 *
 * @author jochen
 */
@Named("jrtab")
@SessionScoped
public class JorideNavigation implements Serializable {

    /**
     * Index of the "home" tab in the main Navigation TAB (both joride and
     * joride-public)
     */
    protected static final int JORIDE_TABINDEX_HOME = 0;

    /**
     * Mage JORIDE_TABINDEX_HOME availlable as a JSF Property
     */
    public int getJorideTabindexHome() {
        return JORIDE_TABINDEX_HOME;
    }
    /**
     * Index of the "rider" tab in the main Navigation TAB (joride only )
     */
    protected static final int JORIDE_TABINDEX_RIDER = 1;

    /**
     * Mage JORIDE_TABINDEX_RIDER availlable as a JSF Property
     */
    public int getJorideTabindexRider() {
        return JORIDE_TABINDEX_RIDER;
    }
    /**
     * Index of the "driver" tab in the main Navigation TAB (joride only )
     */
    protected static final int JORIDE_TABINDEX_DRIVER = 2;

    /**
     * Make JORIDE_TABINDEX_DRIVER availlable as a JSF Property
     */
    public int getJorideTabindexDriver() {
        return JORIDE_TABINDEX_DRIVER;
    }
    /**
     * Index of the "Preferences" tab in the main Navigation TAB (joride only )
     */
    protected static final int JORIDE_TABINDEX_PREFERENCES = 3;

    /**
     * Make JORIDE_TABINDEX_DRIVER availlable as a JSF Property
     */
    public int getJorideTabindexPreferences() {
        return JORIDE_TABINDEX_PREFERENCES;
    }
    
    
    /**
     * Index of the "Account" tab in the main Navigation TAB (joride-public only )
     */
    protected static final int JORIDE_TABINDEX_ACCOUNTS = 1;

    /**
     * Make JORIDE_TABINDEX_ACCOUNTS availlable as a JSF Property
     */
    public int getJorideTabindexAccounts() {
        return JORIDE_TABINDEX_ACCOUNTS;
    }
    
    
    
    
}
