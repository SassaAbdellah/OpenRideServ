/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhg.fokus.openride.matching;

/**
 * A set of constants describing the State of Negotiations for a ride/request.
 *
 * These constants are defined in a seperate class instead of
 * JRiderUndertakesRideEntity as CDIBeans and enums do not go well with each
 * other.
 *
 *
 * @author jochen
 */
// TODO: rename this to "NegotiationConstants" to make clear it works on __rides_ and __drives__
public enum RideNegotiationConstants {

    /**
     * Initial State of an request. STATE_NEW meanst that the request is newly
     * created, and that there are no offers from potential drivers yet.
     *
     * From STATE_NEW, the ride offer may get into state STATE_RIDER_REQUESTED
     * (if a rider requests a ride) or STATE_DRIVER_ACCEPTED (if Driver accepts
     * a matching request ) or STATE_COUNTERMANDED (if Driver needs to
     * invalidate offer for some reason)
     *
     * see also: null     {@link STATE_NEW} 
     *  {@link  STATE_RIDER_REQUESTED}
     *  {@link  STATE_DRIVER_ACCEPTED}
     *  {@link  STATE_CONFIRMED_BOTH}
     *  {@link  STATE_RIDER_REJECTED}
     *  {@link  STATE_DRIVER_REJECTED}
     *  {@link  STATE_REJECTED_BOTH}
     *  {@link  STATE_COUNTERMANDED_DRIVER}
     *  {@link  STATE_COUNTERMANDED_RIDER}
     *  {@link  STATE_COUNTERMANDED_BOTH}
     *  {@link  STATE_UNCLEAR}
     *
     */
    STATE_NEW,
    /**
     * STATE_RIDER_REQUESTED is a state into which an request gets if the rider
     * has requested to be picked up by a driver, but this driver has not (yet)
     * acceppted any one of those requests.
     *
     * From STATE_RIDER_REQUESTED the ride offer may get into state
     *
     * STATE_CONFIRMED_BOTH (if Driver accepts a matching request ) or
     * STATE_COUNTERMANDED (if Driver needs to invalidate offer for some reason)
     *
     * see also: null     {@link STATE_NEW} 
     *  {@link  STATE_RIDER_REQUESTED}
     *  {@link  STATE_DRIVER_ACCEPTED}
     *  {@link  STATE_CONFIRMED_BOTH}
     *  {@link  STATE_RIDER_REJECTED}
     *  {@link  STATE_DRIVER_REJECTED}
     *  {@link  STATE_REJECTED_BOTH}
     *  {@link  STATE_COUNTERMANDED_DRIVER}
     *  {@link  STATE_COUNTERMANDED_RIDER}
     *  {@link  STATE_COUNTERMANDED_BOTH}
     *  {@link  STATE_UNCLEAR}
     *
     */
    STATE_RIDER_REQUESTED,
    /**
     * STATE_DRIVER_ACCEPTED is a state into which a request gets if one (or
     * more) matchings exists and driver has "prematurely" accepted to pick up
     * the rider, while the rider has not (yet) requested to be picked up.
     *
     * From STATE_DRIVER_ACCEPTED the ride offer may get into state
     *
     * STATE_CONFIRMED_BOTH (if one or maore accepted riders acceppt this ride
     * too) STATE_COUNTERMANDED (if Driver needs to invalidate offer for some
     * reason)
     *
     * see also: null     {@link STATE_NEW} 
     *  {@link  STATE_RIDER_REQUESTED}
     *  {@link  STATE_DRIVER_ACCEPTED}
     *  {@link  STATE_CONFIRMED_BOTH}
     *  {@link  STATE_RIDER_REJECTED}
     *  {@link  STATE_DRIVER_REJECTED}
     *  {@link  STATE_REJECTED_BOTH}
     *  {@link  STATE_COUNTERMANDED_DRIVER}
     *  {@link  STATE_COUNTERMANDED_RIDER}
     *  {@link  STATE_COUNTERMANDED_BOTH}
     *  {@link  STATE_UNCLEAR}
     *
     */
    STATE_DRIVER_ACCEPTED,
    /**
     * STATE_CONFIRMED_BOTH is a state into which an request gets if a matching
     * exists for which was requested by rider and accepted by driver.
     *
     * From STATE_CONFIRMED_BOTH, th e ride offer may get into state
     *
     * STATE_COUNTERMANDED (if Driver needs to invalidate offer for some reason)
     *
     *
     *
     */
    STATE_CONFIRMED_BOTH,
    /**
     * TODO: rejecting a match should simply cause the match to dissapear. There
     * is no state associated to this to either ride or drive, so this can
     * possibly dissapear
     *
     */
    STATE_RIDER_REJECTED,
    /**
     * TODO: rejecting a match should simply cause the match to dissapear. There
     * is no state associated to this to either ride or drive, so this can
     * possibly dissapear
     *
     */
    STATE_DRIVER_REJECTED,
    /**
     * TODO: rejecting a match should simply cause the match to dissapear. There
     * is no state associated to this to either ride or drive, so this can
     * possibly dissapear
     *
     */
    STATE_REJECTED_BOTH,
    /**
     * STATE_COUTERMANDED is a state into which an request gets if rider has to
     * cancel the ride for whatever reason (Blizzards, Earthquake, ...etc...)
     *
     * From STATE_COUNTERMANDED_RIDER the ride offer may not get into any other
     * stated.
     *
     * STATE_CONFIRMED_BOTH (if one or maore accepted riders acceppt this ride
     * too) STATE_COUNTERMANDED (if Driver needs to invalidate offer for some
     * reason)
     *
     *
     *
     */
    STATE_COUNTERMANDED_RIDER,
    /**
     * STATE_COUTERMANDED is a state into which an request gets if rider has to
     * cancel the ride for whatever reason (Blizzards, Earthquake, ...etc...)
     *
     * From STATE_COUNTERMANDED_RIDER the ride offer may not get into any other
     * stated.
     *
     * STATE_CONFIRMED_BOTH (if one or maore accepted riders acceppt this ride
     * too) STATE_COUNTERMANDED (if Driver needs to invalidate offer for some
     * reason)
     *
     * see also: null     {@link STATE_NEW} 
     *  {@link  STATE_RIDER_REQUESTED}
     *  {@link  STATE_DRIVER_ACCEPTED}
     *  {@link  STATE_CONFIRMED_BOTH}
     *  {@link  STATE_RIDER_REJECTED}
     *  {@link  STATE_DRIVER_REJECTED}
     *  {@link  STATE_REJECTED_BOTH}
     *  {@link  STATE_COUNTERMANDED_DRIVER}
     *  {@link  STATE_COUNTERMANDED_RIDER}
     *  {@link  STATE_COUNTERMANDED_BOTH}
     *  {@link  STATE_UNCLEAR}
     *
     */
    STATE_COUNTERMANDED_DRIVER,
    /**
     * STATE_COUTERMANDED is a state into which an request gets if rider or
     * driver has to cancel the ride for whatever reason (Blizzards, Earthquake,
     * ...etc...)
     *
     * From STATE_COUNTERMANDED_RIDER the ride offer may not get into any other
     * stated.
     *
     * STATE_CONFIRMED_BOTH (if one or maore accepted riders acceppt this ride
     * too) STATE_COUNTERMANDED (if Driver needs to invalidate offer for some
     * reason)
     *
     * see also: null     {@link STATE_NEW} 
     *  {@link  STATE_RIDER_REQUESTED}
     *  {@link  STATE_DRIVER_ACCEPTED}
     *  {@link  STATE_CONFIRMED_BOTH}
     *  {@link  STATE_RIDER_REJECTED}
     *  {@link  STATE_DRIVER_REJECTED}
     *  {@link  STATE_REJECTED_BOTH}
     *  {@link  STATE_COUNTERMANDED_DRIVER}
     *  {@link  STATE_COUNTERMANDED_RIDER}
     *  {@link  STATE_COUNTERMANDED_BOTH}
     *  {@link  STATE_UNCLEAR}
     *
     */
    STATE_COUNTERMANDED_BOTH,
    /**
     * STATE_UNCLEAR is a state that gets returned if the negotiations of a
     * drive cannot be determined. * see also:      {@link STATE_NEW} 
     *  {@link  STATE_RIDER_REQUESTED}
     *  {@link  STATE_DRIVER_ACCEPTED}
     *  {@link  STATE_CONFIRMED_BOTH}
     *  {@link  STATE_RIDER_REJECTED}
     *  {@link  STATE_DRIVER_REJECTED}
     *  {@link  STATE_REJECTED_BOTH}
     *  {@link  STATE_COUNTERMANDED_DRIVER}
     *  {@link  STATE_COUNTERMANDED_RIDER}
     *  {@link  STATE_COUNTERMANDED_BOTH}
     *  {@link  STATE_UNCLEAR}
     */
    STATE_UNCLEAR,
    /**
     * Drive/Ride is no more availlable, typically in the past * see also:      {@link STATE_NEW} 
     *  {@link  STATE_RIDER_REQUESTED}
     *  {@link  STATE_DRIVER_ACCEPTED}
     *  {@link  STATE_CONFIRMED_BOTH}
     *  {@link  STATE_RIDER_REJECTED}
     *  {@link  STATE_DRIVER_REJECTED}
     *  {@link  STATE_REJECTED_BOTH}
     *  {@link  STATE_COUNTERMANDED_DRIVER}
     *  {@link  STATE_COUNTERMANDED_RIDER}
     *  {@link  STATE_COUNTERMANDED_BOTH}
     *  {@link  STATE_UNCLEAR}
     */
    STATE_UNAVAILLABLE;
}
