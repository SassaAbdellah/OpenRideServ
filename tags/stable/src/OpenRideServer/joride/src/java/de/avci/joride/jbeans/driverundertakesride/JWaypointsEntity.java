/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.driverundertakesride;

import java.util.ArrayList;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

/** Class making a list of {@link JWaypointEntity} s availlable
 *  to the frontend as a java bean.
 *  (notice the plural in the classname!)
 * 
 * @author jochen
 * 
 * 
 */


@Named("waypoints")
@RequestScoped

public class JWaypointsEntity extends ArrayList<JWaypointEntity> {
    
    /** Encode this as a JSON Array
     * 
     */
    public StringBuffer getJSON(){
        
        StringBuffer buf=new StringBuffer();
        buf.append('[');
        for(JWaypointEntity jw : this){
            buf.append(jw.getJSON());
            buf.append(',');
        }
        // replace the closing comma with a closig bracket
        int lastIndex=buf.length()-1;
        if( buf.charAt(lastIndex)==',') { 
            buf.setCharAt(lastIndex,']');
        } else { // this may happen if the list is empty
            buf.append(']');
        }
        return buf;
    }
    
     
}
