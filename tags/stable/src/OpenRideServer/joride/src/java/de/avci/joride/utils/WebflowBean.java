/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.utils;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Defines Parameters and Values for governing Webflows in JoRide Frontend, such
 * as "next"/"back"/"cancel"/finish.
 * 
 * Use as follows
 * 
 * 
 * To initialize this, set any of the HTTPParameters back/next/finish/cancel to
 * the desired outcome (i.e: the navigation key for the desired
 * next/back/cancel/finish page)
 * 
 * 
 * You can then use the getNext()/getBack()/getCancel()/getFinish() methods to
 * navigate dynamically through any webflow.
 * 
 * use clear() to blank out all parameters
 * 
 * 
 * Note that this has session view to survive several pages. The backdraw is,
 * that there can only be one webflow at a time.
 * 
 * 
 * 
 * @author jochen
 * 
 * 
 * 
 */

@Named("webflow")
@SessionScoped
/**
 * 
 * 
 * 
 */
public class WebflowBean implements Serializable {

	/**
	 * Name of Webflow bean with session scope. This is necessarry
	 * 
	 * @return
	 */
	public static String getSessionBeanName() {

		return "webflow";
	}

	public void setNext(String next) {
		this.next = next;
	}

	public void setBack(String back) {
		this.back = back;
	}

	public void setCancel(String cancel) {
		this.cancel = cancel;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}

	/**
	 * Make PARAM_VALUE_NEXT accessible in the bean fashion.
	 */
	public String getParamNext() {
		return WebflowBeanConstants.PARAM_NAME_NEXT;
	}

	/**
	 * Make PARAM_VALUE_BACK accessible in the bean fashion.
	 */
	public String getParamBack() {
		return WebflowBeanConstants.PARAM_NAME_BACK;
	}

	/**
	 * Make PARAM_NAME_CANCEL accessible in the bean fashion.
	 */
	public String getParamCancel() {
		return WebflowBeanConstants.PARAM_NAME_CANCEL;
	}

	/**
	 * Make PARAM_NAME_FINISH accessible in the bean fashion.
	 */
	public String getParamFinish() {
		return WebflowBeanConstants.PARAM_NAME_FINISH;
	}

	/**
	 * Next Property: Where to go next.
	 */
	protected String next = null;

	public String getNext() {
		return this.next;
	}

	/**
	 * Back Property: where to go back
	 */
	protected String back = null;

	public String getBack() {
		return this.back;
	}

	/**
	 * where to go when cancelling
	 */
	protected String cancel = null;

	public String getCancel() {
		return this.cancel;
	}

	/**
	 * Where to go when finishing the webflow
	 */
	protected String finish;

	public String getFinish() {
		return this.finish;
	}

	/**
	 * Do a smart update, i.e: Check out httprequest, and overwrite any of the
	 * back/next/cancel/finish values with a corresponding http request
	 * parameter, *provided* the request parameter is !=null.
	 * 
	 */
	public void smartUpdate() {

		HTTPUtil hru = new HTTPUtil();

		String vBack = hru.getParameterSingleValue(getParamBack());
		if (vBack != null) {
			this.back = vBack;
		}

		String vNext = hru.getParameterSingleValue(getParamNext());
		if (vNext != null) {
			this.next = vNext;
		}

		String vCancel = hru.getParameterSingleValue(getParamCancel());
		if (vCancel != null) {
			this.cancel = vCancel;
		}

		String vFinish = hru.getParameterSingleValue(getParamFinish());
		if (vFinish != null) {
			this.finish = vFinish;
		}

	} // smartUpdate

	/**
	 * Set next/back/cancel and finish property to null.
	 * 
	 */
	public void clear() {

		this.back = null;
		this.next = null;
		this.cancel = null;
		this.finish = null;
	}

}