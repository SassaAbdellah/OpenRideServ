/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.utils.geocoding.osm;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


import java.io.Serializable;

/**
 * Model for a JSONP Query to the nominatim geocoding service.
 *
 *
 *
 *
 *
 *
 * @author jochen
 */
@Named("nominatimq")
@SessionScoped
public class NominatimJSONPQuery  implements Serializable {

    
    
        /** Default value for the service_url property.
     *  This points to the original Nominatim service at:
     * "http://nominatim.openstreetmap.org/search"
     */
    protected static final String DEFAULT_SERVICE_URL = "http://nominatim.openstreetmap.org/search";
    /** Name of the "addressdetails" parameter.
     *  Wether to return verbose address details.
     *  We alway chose 0 here, because we cannot process adressdetaiks anyway.
     *  Currently, we are content with returning the displaystring,
     *  which is always there.
     *
     *  See a typical Nominatim query below:
     *  q=fliederweg&format=json&polygon=1&addressdetails=2&json_callback=callback
     *
     */
    protected static final String PARAM_NAME_addressdetails = "addressdetails";
    /** Nominatim parametername for the "format" parameter.
     *  Format parameter defines the format of the response.
     *  Nominatim allows 'html','xml', json,...
     *  We alway chose json here, because technically we do
     *  cross site scripting with jsonp (aka: padded JSON).
     *
     *
     *  See a typical Nominatim query below:
     *  q=fliederweg&format=json&polygon=1&addressdetails=2&json_callback=callback
     *
     */
    protected static final String PARAM_NAME_format = "format";
    /** Nominatim parametername for the "polygon" parameter.
     *  Wether to return a polygon for boundary or not
     *  We alway chose 0 here, because we cannot process polygons anyway.
     *
     *  See a typical Nominatim query below:
     *  q=fliederweg&format=json&polygon=1&addressdetails=2&json_callback=callback
     *
     */
    protected static final String PARAM_NAME_polygon = "polygon";
    /** Nominatim parametername for the "querystring".
     *  This is simply called 'q' by nominatim.
     *
     *  See a typical Nominatim query below:
     *  q=fliederweg&format=json&polygon=1&addressdetails=2&json_callback=callback
     *
     */
    protected static final String PARAM_NAME_q = "q";
    /** Value of the "addressdetails" parameter.
     *  Wether to return verbose address details.
     *  We alway chose 0 here, because we cannot process adressdetaiks anyway.
     *  Currently, we are content with returning the displaystring,
     *  which is always there.
     *
     *  See a typical Nominatim query below:
     *  q=fliederweg&format=json&polygon=1&addressdetails=2&json_callback=callback
     *
     */
    protected static final String PARAM_VALUE_addressdetails = "0";
    /** Value  for the "polygon" parameter.
     *  Wether to return a polygon for boundary or not
     *  We alway chose 0 here, because we cannot process polygons anyway.
     *
     *  See a typical Nominatim query below:
     *  q=fliederweg&format=json&polygon=1&addressdetails=2&json_callback=callback
     *
     */
    protected static final String PARAM_VALUE_polygon = "0";
    /** Property telling which City to search for.
     *  Note, that we can use either
     *  street and number,   or  poiName.
     *  If poiName is given, then this takes precedence over street/and number
     *
     */
    protected String city = null;
    
      /** Trivial Getter
     */
    public String getCity() {
        return this.city;
    }


    /** Trivial Setter
     */
    public  void setCity(String arg){ 
        this.city=arg;
    }
    
    

    /** Property telling which POI to search for.
     *  Note, that we can use either
     *  street and number,   or  poiName.
     *  If poiName is given, then this takes precedence over street/and number
     *
     */
    protected String poi = null;
    
    /** Trivial Getter
     */
    public String getPoi(){ 
        return this.poi; 
    }
    
     /** Trivial Setter
     */
    public  void setPoi(String arg){ 
        this.poi=arg;
    }
   
    
    
    
    
    
    /** Property telling which URL to use.
     *  Decault is {@link DEFAULT_SERVICE_URL }
     */
    protected String serviceURL = NominatimJSONPQuery.DEFAULT_SERVICE_URL;
    
    
    /** Trivial getter
     */
    public String getServiceURL(){return this.serviceURL;}
    
    /** Trivial Setter
     */
    public void setServiceURL(String arg) {this.serviceURL=arg; }
    
    
    /** Property telling which Street to search for.
     *  Note, that we can use either
     *  street and number or  poiName.
     *  If poiName is given, then this takes precedence over street/and number
     *
     */
    protected String street = null;
    
       /** Trivial Getter
     */
    public String getStreet(){ 
        return this.street; 
    }
    
     /** Trivial Setter
     */
    public  void setStreet(String arg){ 
        this.street=arg;
    }

  

    
   
   
    
    
    
    
    /**
     * Standard Value for the "format" parameter. Format parameter defines the
     * format of the response. Nominatim allows 'html','xml', json,... We alway
     * chose json here, because technically we do cross site scripting with
     * jsonp (aka: padded JSON).
     *
     *
     * See a typical Nominatim query below:
     * q=fliederweg&format=json&polygon=1&addressdetails=2&json_callback=callback
     *
     */
    protected static final String PARAM_VALUE_json = "json";
    /**
     * Nominatim parametername for the "json_callback" parameter. json_callback
     * governs, how a json callback will be padded. With that, we can circumvent
     * the "same origin policy" and do cross site scripting with jsonp (aka:
     * padded JSON)
     *
     * We give "callback" here, because our callback function is simply called
     * "callback".
     *
     *
     * See a typical Nominatim query below:
     * q=fliederweg&format=json&polygon=1&addressdetails=2&json_callback=callback
     *
     */
    protected static final String PARAM__NAME_json_callback = "json_callback";
    /**
     * Nominatim standard value for the "json_callback" parameter. json_callback
     * governs, how a json callback will be padded. With that, we can circumvent
     * the "same origin policy" and do cross site scripting with jsonp (aka:
     * padded JSON)
     *
     * We give "callback" here, because our callback function is simply called
     * "callback".
     *
     *
     * See a typical Nominatim query below:
     * q=fliederweg&format=json&polygon=1&addressdetails=2&json_callback=callback
     *
     */
    protected static final String PARAM_VALUE_json_callback = "callback";

    /**
     * Create the nominatim specific string to be given as a value for the 'q'
     * parameter.
     *
     * (Not th be confused with the full querystring!)
     *
     * @return
     */
    public String getQString() {


        String res = this.PARAM_NAME_q + "=";

        if (this.getPoi() != null && !("".equals(this.getPoi().trim()))) {
            res += this.getPoi();
        }

        if (this.getCity() != null && !("".equals(this.getCity()))) {
            res += "," + this.getCity();
        }

        if (this.getStreet() != null && !("".equals(this.getStreet()))) {
            res += "," + this.getStreet();
        }

        return res;
    }

    /**
     * Create the query string to be sent against nominatim service
     *
     */
    public String getQueryString() {

        String res = getServiceURL();

        // now, create the query part of the url,
        // something like:
        //
        //?#{nominatimq.QString}&format=json&polygon=0;addressdetails=0&json_callback=callback&serial=1"


        res += "?" + this.getQString();
        res += "&" + PARAM_NAME_format + "=" + PARAM_VALUE_json;
        res += "&" + PARAM_NAME_polygon + "=" + PARAM_VALUE_polygon;
        res += "&" + PARAM_NAME_addressdetails + "=" + PARAM_VALUE_addressdetails;
        res += "&" + PARAM__NAME_json_callback + "=" + PARAM_VALUE_json_callback;


        return res;

    }
} // class

