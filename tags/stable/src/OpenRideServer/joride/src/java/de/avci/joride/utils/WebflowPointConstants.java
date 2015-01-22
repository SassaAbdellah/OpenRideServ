/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.utils;

/**
 *
 * @author jochen
 */
public class WebflowPointConstants {
    
    
    
    /** Name of the http request parameter that contains the targetURL. 
     * The targetURL determines, where the information of this Point
     * is finally sent.
     * 
     * Note the different between targetURL and target parameter:
     * <ul>
     * <li> 
     * The target parameter is used to determine *what* has to 
     * be done with a given point *inside* a given page
     * </li>
     * <li>
     * The targetURL Parameter determines where the 
     * point should be sent after it is picked.
     * </li>
     * </ul>
     *
     */
    public static final String PARAM_NAME_TARGETURL = "targeturl";
    
    

    /** Name of the http request parameter that contains the target. The target
     * is used by the target page to determine which point should be updated.
     * (For ex: a RideRequest contains start and endpoint, and the target
     * parameter can be used to determin wether start or endpoint should be set)
     *
     * Note the different between targetURL and target parameter:
     * <ul>
     * <li> 
     * The target parameter is used to determine *what* has to 
     * be done with a given point *inside* a given page
     * </li>
     * <li>
     * The targetURL Parameter determines where the 
     * point should be sent after it is picked.
     * </li>
     * </ul>
     * 
     * 
     */
    public static final String PARAM_NAME_TARGET = "target";
    
    
    /** Name of the http request parameter that contains the Displaystring
     */
    public static final String PARAM_NAME_DISPLAYSTRING = "displaystring";
    
    
    /** Name of the http request parameter that contains the latitude
     */
    public static final String PARAM_NAME_LAT = "lat";
    
    /** Name of the http request parameter that contains the longitude
     */
    public static final String PARAM_NAME_LON = "lon";
    
    
    
    
    
}
