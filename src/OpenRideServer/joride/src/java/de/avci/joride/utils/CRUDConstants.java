/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.utils;
import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Named;

/** Defines Parameternames and values for triggereing CRUD  
 *  (create/replace/update/delete) actions
 *  in JSF Events, and makes them availlable to both 
 *
 * @author jochen
 */


@Named("crud")
@ApplicationScoped
public class CRUDConstants implements Serializable {
    
    
    /** Name of the Parameter that defines the CRUD Action
     */
    public static final String PARAM_NAME_CRUD_ACTION="CRUD";
    
    /** Access PARAM_NAME_CRUD_ACTION via bean method
     */
    public String getParamNameCrudAction(){ return this.PARAM_NAME_CRUD_ACTION;}
    
    /** Name of the Parameter that defines the CRUD Action "create"
     */
    public static final String PARAM_VALUE_CRUD_CREATE="create";
    
    /** Access PARAM_VALUE_CRUD_CREATE via bean method
     */
    public String getParamValueCrudCreate(){ return this.PARAM_VALUE_CRUD_CREATE;}
    
    
    
    
    /** Name of the Parameter that defines the CRUD Action "update"
     */
    public static final String PARAM_VALUE_CRUD_UPDATE="update";
    
    
    /** Access PARAM_NAME_CRUD_VALUE_UPDATE via bean method
     */
    public String getParamValueCrudUpdate(){ return this.PARAM_VALUE_CRUD_UPDATE;}
    
    
    
    /** Name of the Parameter that defines the CRUD Action "delete"
     */
    public static final String PARAM_VALUE_CRUD_DELETE="delete";
    
    /** Access PARAM_NAME_CRUD_VALUE_DELETE via bean method
     */
    public String getParamValueCrudDelete(){ return this.PARAM_VALUE_CRUD_DELETE;}
    
    
    
    
    /** when it comes to CRUD operations update/delete, an 
     *  id will be needed.
     *  
     *  This Parameter will be transported in the ID Parameter
     * 
     * 
     */
    public static final String PARAM_NAME_CRUD_ID="crudID";
    
    /** Access PARAM_NAME_CRUD_ID via bean method
     */
    public String getParamNameCrudId(){ return this.PARAM_NAME_CRUD_ID;}
    
 
    
    
    
    
} // class
