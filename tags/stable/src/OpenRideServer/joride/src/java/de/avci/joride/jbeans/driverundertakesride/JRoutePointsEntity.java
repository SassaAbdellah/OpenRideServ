/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.driverundertakesride;


import java.io.Serializable;

import de.fhg.fokus.openride.rides.driver.RoutePointEntity;
import java.util.List;
import java.util.Iterator;


/** Wrapper for list of Routpoints, making them available 
 *  as a JSF Bean, and offering some convenience methods
 *  to view them in OpenLayers and friends.
 *
 * @author jochen
 */
public class JRoutePointsEntity implements Serializable {
    
    /** List of Routepoints for given Ride or Drive 
     * 
     */
     private List <RoutePointEntity> routePoints;
    
     public List <RoutePointEntity> getRoutePoints(){
         return this.routePoints;
     }
     
     public void setRoutePoints(List <RoutePointEntity> arg){

         this.routePoints=arg;
     }
     
   
     
     /** Return reprensentation of the routepoints
      *  as a JSON String, i.e: 
      * 
      * @return 
      */
     public String getRoutePointsAsJSON(){
         
         
         // TODO: this is imperformant!
         // do something with Stringbuffer instead!
         
     
         Iterator <RoutePointEntity> it=routePoints.iterator();
         
         
         StringBuffer  buf=new StringBuffer();
         buf.append("[\n");
             
         while (it.hasNext()){
         
             RoutePointEntity rp=it.next();
             buf.append("\n["+rp.getLongitude()+","+rp.getLatitude()+"]");
             if(it.hasNext()){ buf.append(",");}
         } // while it.hasNext()
     
         buf.append("\n]");       
     
         return buf.toString();
     }
     
     
     
    
     
     
     
     
     
}
