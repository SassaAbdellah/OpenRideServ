/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.utils;

import javax.faces.context.FacesContext;

/** Utility that can retrieve a bean from JSFSession given Bean Name and Session Context
 *
 * @author jochen
 */
public class BeanRetriever {
    
    
    /** Retrieve JSF Bean name "beanName" of class "beanClass" from facesContext.
     *  
     *  This is likely to throw Errors if there is no such thing in the faces 
     *  context, so use this with utmost care.
     *  (You have been warned...) 
     * 
     * @param beanName   name of the bean to be retrieved
     * @param beanClass  class of the bean to be retrieved
     * 
     * 
     * @return   SF Bean name "beanName" of class "beanClass" from facesContext
     */
    public Object retrieveBean(String beanName, Class beanClass){
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Object o = context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", beanClass);
            return o;
        } catch (Exception exc) {
            throw new Error("Unexpected Error while retrieving jsf bean named " + beanName, exc);
        }
    
    }
    
    
}
