/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.joride.jbeans.auxiliary;

import de.avci.joride.constants.JoRideConstants;
import de.avci.joride.jbeans.customerprofile.JPublicCustomerProfile;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import javax.inject.Named;

/**
 * Make Driverratings/Riderratings availlable in form of a JSF-Bean
 *
 * @author jochen
 */
@Named("rating")
public class JRatingBean implements Serializable {

    /// GIVEN Rating Stuff (where rider has rated)
    /**
     * Public data of the rider that rated this ride.
     */
    protected JPublicCustomerProfile rider;

    public JPublicCustomerProfile getRider() {
        return this.rider;
    }

    protected void setRider(JPublicCustomerProfile arg) {
        this.rider = arg;
    }
    /**
     * "GivenRating" for this ride (may be null if ride is not yet rated!)
     */
    protected Integer givenRating = null;

    public Integer getGivenRating() {
        return this.givenRating;
    }

    public void setGivenRating(Integer ratingArg) {
        this.givenRating = ratingArg;
    }
    /**
     * Ratings Comment
     */
    protected String givenComment = null;

    public String getGivenComment() {
        return this.givenComment;
    }

    public void setGivenComment(String commentArg) {
        this.givenComment = commentArg;
    }
    /**
     * Date of GivenRating, May be null, if there was no rating
     */
    protected Date givenRatingDate = null;

    public Date getGivenRatingDate() {
        return givenRatingDate;
    }

    public void setGivenRatingDate(Date arg) {
        this.givenRatingDate = arg;
    }

    /**
     * True if there is a rider rating date, else false
     */
    public boolean getIsRiderRated() {
        return this.getGivenRating() != null;
    }

    /**
     * Nicely formatted version of the given rating date
     *
     * @param arg
     */
    public String getGivenRatingDateFormatted() {

        DateFormat df = new JoRideConstants().createDateFormat();

        if (this.getGivenRatingDate() != null) {
            return df.format(givenRatingDate);
        }

        return "";
    }
    /// GIVEN Rating Stuff (where rider has rated)
    /**
     * Public data of the rider that rated this ride.
     */
    protected JPublicCustomerProfile driver;

    public JPublicCustomerProfile getDriver() {
        return this.driver;
    }

    protected void setDriver(JPublicCustomerProfile arg) {
        this.driver = arg;
    }
    /**
     * "ReceivedRating" for this ride (may be null if ride is not yet rated!)
     */
    protected Integer receivedRating = null;

    public Integer getReceivedRating() {
        return this.receivedRating;
    }

    public void setReceivedRating(Integer ratingArg) {
        this.receivedRating = ratingArg;
    }
    /**
     * Ratings Comment
     */
    protected String receivedComment = null;

    public String getReceivedComment() {
        return this.receivedComment;
    }

    public void setReceivedComment(String commentArg) {
        this.receivedComment = commentArg;
    }
    /**
     * Date of ReceivedRating, May be null, if there was no rating
     */
    protected Date receivedRatingDate = null;

    public Date getReceivedRatingDate() {
        return receivedRatingDate;
    }

    public void setReceivedRatingDate(Date arg) {
        this.receivedRatingDate = arg;
    }

    /**
     * Nicely formatted version of the received rating date
     *
     * @param arg
     */
    public String getReceivedRatingDateFormatted() {

        DateFormat df = new JoRideConstants().createDateFormat();

        if (this.getReceivedRatingDate() != null) {
            return df.format(receivedRatingDate);
        }

        return "";
    }

    /**
     * True if there is a rider rating date, else false
     */
    public boolean getIsDriverRated() {
        return this.getReceivedRating() != null;
    }

    ///// end of properties
    /**
     * Extract RatingData from JRiderUndertakesRideEntity
     *
     * @param jrure
     * @return
     */
    public static JRatingBean extractRating(RiderUndertakesRideEntity rure) {

        JRatingBean res = new JRatingBean();


        // rider stuff

        CustomerEntity riderCe = rure.getCustId();

        res.setRider(new JPublicCustomerProfile());
        res.getRider().updateFromCustomerEntity(riderCe);

        // Rating
        res.setGivenRating(rure.getGivenrating());
        // Comment
        res.setGivenComment(rure.getGivenratingComment());
        // Rating Date
        res.setGivenRatingDate(rure.getGivenratingDate());


        // Driver stuff 
        // note, that there may not be a driver if the ride has not been realized!

        if (rure.getRideId() != null) {


            CustomerEntity driverCe = rure.getRideId().getCustId();

            res.setDriver(new JPublicCustomerProfile());
            res.getDriver().updateFromCustomerEntity(driverCe);

            // Rating
            res.setGivenRating(rure.getGivenrating());
            // Comment
            res.setGivenComment(rure.getGivenratingComment());
            // Rating Date
            res.setGivenRatingDate(rure.getGivenratingDate());

        }

        return res;
    }
} // class
