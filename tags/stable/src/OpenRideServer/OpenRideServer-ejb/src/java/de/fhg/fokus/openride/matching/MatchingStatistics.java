/*
 OpenRideShare -- Car Sharing 3.0
 Copyright (C) 2014  Jochen Laser

 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License Version 3 as
 published by the Free Software Foundation.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fhg.fokus.openride.matching;

import java.io.Serializable;
import java.util.List;

/**
 * Portmanteau class to contain statistics concerning the matches for a given
 *
 * riderundetakesrideentity or driverundertakesrideentity
 *
 *
 *
 * @author jochen
 */
public class MatchingStatistics implements Serializable {

    /**
     * Overall number of matches that have gone into this statistics
     */
    private int numberOfMatches = 0;
    /**
     * Number of matches which have rider state "NOT_ADAPTED"
     */
    private int notAdaptedRider = 0;
    /**
     * Number of matches which have driver state "NOT_ADAPTED"
     */
    private int notAdaptedDriver = 0;
    /**
     * Number of matches which have state "NOT_ADAPTED" for driver and rider,
     * (aka: new states)
     */
    private int notAdaptedBoth = 0;
    /**
     * Number of matches which have rider state "ACCEPTED"
     */
    private int acceptedRider = 0;
    /**
     * Number of matches which have driver state "ACCEPTED"
     */
    private int acceptedDriver = 0;
    /**
     * Number of matches which have rider state "REJECTED"
     */
    private int rejectedRider = 0;
    /**
     * Number of matches which have driver state "REJECTED"
     */
    private int rejectedDriver = 0;
    /**
     * Number of matches which have rider_state and driver_state "REJECTED"
     */
    private int rejectedBoth = 0;
    /**
     * Number of matches which have rider state "NO_MORE_AVAILLABLE"
     */
    private int noMoreAvaillableRider = 0;
    /**
     * Number of matches which have rider state AND driver state
     * "NO_MORE_AVAILLABLE"
     */
    private int noMoreAvaillableBoth = 0;
    /**
     * Number of matches which have driver state "NO_MORE_AVAILLABLE"
     */
    private int noMoreAvaillableDriver = 0;
    /**
     * Number of matches which have rider state "COUNTERMANDED"
     */
    private int countermandedRider = 0;
    /**
     * Number of matches which have driver state "COUNTERMANDED"
     */
    private int countermandedDriver = 0;
    /**
     * Number of matches which have driver state "COUNTERMANDED_BOTH"
     */
    private int countermandedBoth = 0;
    /**
     * Number of matches which are accepted by both, driver and rider
     */
    private int acceptedBoth = 0;

    public int getNumberOfMatches() {
        return this.numberOfMatches;
    }

    private void setNumberOfMatches(int arg) {
        this.numberOfMatches = arg;
    }

    public int getNotAdaptedRider() {
        return notAdaptedRider;
    }

    public void setNotAdaptedRider(int notAdaptedRider) {
        this.notAdaptedRider = notAdaptedRider;
    }

    public int getNotAdaptedDriver() {
        return notAdaptedDriver;
    }

    public int getNotAdaptedBoth() {
        return notAdaptedBoth;
    }

    public void setNotAdaptedDriver(int notAdaptedDriver) {
        this.notAdaptedDriver = notAdaptedDriver;
    }

    public int getAcceptedRider() {
        return acceptedRider;
    }

    public void setAcceptedRider(int acceptedRider) {
        this.acceptedRider = acceptedRider;
    }

    public int getAcceptedDriver() {
        return acceptedDriver;
    }

    public void setAcceptedDriver(int acceptedDriver) {
        this.acceptedDriver = acceptedDriver;
    }

    public int getRejectedRider() {
        return rejectedRider;
    }

    public void setRejectedRider(int rejectedRider) {
        this.rejectedRider = rejectedRider;
    }

    public int getRejectedDriver() {
        return rejectedDriver;
    }

    public void setRejectedDriver(int rejectedDriver) {
        this.rejectedDriver = rejectedDriver;
    }

    public int getRejectedBoth() {
        return rejectedBoth;
    }

    public void setRejectedBoth(int rejectedBoth) {
        this.rejectedBoth = rejectedBoth;
    }

    public int getNoMoreAvaillableRider() {
        return noMoreAvaillableRider;
    }

    public void setNoMoreAvaillableRider(int noMoreAvaillableRider) {
        this.noMoreAvaillableRider = noMoreAvaillableRider;
    }

    public int getNoMoreAvaillableDriver() {
        return noMoreAvaillableDriver;
    }

    public void setNoMoreAvaillableDriver(int noMoreAvaillableDriver) {
        this.noMoreAvaillableDriver = noMoreAvaillableDriver;
    }

    public int getNoMoreAvaillableBoth() {
        return noMoreAvaillableBoth;
    }

    public void setNoMoreAvaillableBoth(int noMoreAvaillableBoth) {
        this.noMoreAvaillableBoth = noMoreAvaillableBoth;
    }

    public int getCountermandedBoth() {
        return countermandedBoth;
    }

    public void setCountermandedBoth(int arg) {
        this.countermandedBoth = arg;
    }

    public int getCountermandedRider() {
        return countermandedRider;
    }

    public void setCountermandedRider(int countermandedRider) {
        this.countermandedRider = countermandedRider;
    }

    public int getCountermandedDriver() {
        return countermandedDriver;
    }

    public void setCountermandedDriver(int countermandedDriver) {
        this.countermandedDriver = countermandedDriver;
    }

    public int getAcceptedBoth() {
        return acceptedBoth;
    }

    public void setAcceptedBoth(int acceptedBoth) {
        this.acceptedBoth = acceptedBoth;
    }

    /**
     * Add data from argument to statitstics
     *
     * @param ,
     */
    public void addMatchingToStatistics(MatchEntity m) {

        // count the number of matchings processed
        this.numberOfMatches++;


        // Driver State
        int d = MatchEntity.NOT_ADAPTED;
        d = m.getDriverState();
        // Rider State
        int r = MatchEntity.NOT_ADAPTED;
        r = m.getRiderState();

        
        // No more availlable : only one of "nma. both", "nma. rider", "nma. driver"
        // will be counted 
        // Matches that has gone nma for either side will not be counted
        // anywhere else

        if (MatchEntity.NO_MORE_AVAILABLE.equals(d) && MatchEntity.NO_MORE_AVAILABLE.equals(r)) {
            this.noMoreAvaillableBoth++;
            return;
        } else {

            // if not nma for both, the following conditions will
            // be mutually exclusive

            if (MatchEntity.NO_MORE_AVAILABLE.equals(d)) {
                this.noMoreAvaillableDriver++;
                return;
            }

            if (MatchEntity.NO_MORE_AVAILABLE.equals(r)) {
                this.noMoreAvaillableRider++;
                return;
            }
        }

        
        // COUNTERMANDED. only one of "countermanded both", "countermanded rider", countermanded driver
        // will be counted 
         // Matches that have been countermanded by one side 
        // will not be counted anywhere else

        if (MatchEntity.DRIVER_COUNTERMANDED.equals(d)
                && MatchEntity.RIDER_COUNTERMANDED.equals(r)) {
            this.countermandedBoth++;
            return;
        } else {
            // if not countermanded by both, the following conditions will
            // be mutually exclusive
            if (MatchEntity.DRIVER_COUNTERMANDED.equals(d)) {
                this.countermandedDriver++;
                return;
            }
            if (MatchEntity.RIDER_COUNTERMANDED.equals(r)) {
                this.countermandedRider++;
                return;
            }
        }

        

        // REJECTED:  only one of "rej. both", "rej. rider", "rej. driver"
        // will be counted 
         // Matches that have been rejected by one side will not be counted
        // anywhere els

        if (MatchEntity.REJECTED.equals(r) && MatchEntity.REJECTED.equals(d)) {
            this.rejectedBoth++;
            return;
        } else {
            // if not rejected by both, the following conditions will
            // be mutually exclusive

            if (MatchEntity.REJECTED.equals(r)) {
                this.rejectedRider++;
                return;
            }
            if (MatchEntity.REJECTED.equals(d)) {
                this.rejectedDriver++;
                return;
            }
        }        
        


        // ACCEPTED. only one of "accepted both", "accepted rider", accepted driver
        // will be counted

        if (MatchEntity.ACCEPTED.equals(r) && MatchEntity.ACCEPTED.equals(d)) {
            this.acceptedBoth++;

        } else {
            // if not accepted by both, the following conditions will
            // be mutually exclusive

            if (MatchEntity.ACCEPTED.equals(d)) {
                this.acceptedDriver++;
            }

            if (MatchEntity.ACCEPTED.equals(r)) {
                this.acceptedRider++;
            }
        }


        // NOT_ADAPTED. only one of "na both", "na rider", "na driver"
        // will be counted 

        if (MatchEntity.NOT_ADAPTED.equals(r) && MatchEntity.NOT_ADAPTED.equals(d)) {
            this.notAdaptedBoth++;
        } else {
            // if not countermanded by both, the following conditions will
            // be mutually exclusive
            if (MatchEntity.NOT_ADAPTED.equals(d)) {
                this.notAdaptedDriver++;
            }

            if (MatchEntity.NOT_ADAPTED.equals(r)) {
                this.notAdaptedRider++;
            }
        }

    }

    /**
     * Add statistics for all the MatchEntities in the list. Typically think of
     * the list as the list of matchings for a given offer or request.
     *
     *
     *
     * @param mList List of MatchEntity for which the statistics should be
     * updated
     */
    public void statisticsFromList(List<MatchEntity> mList) {

        for (MatchEntity m : mList) {
            this.addMatchingToStatistics(m);
        }
    }

    /**
     * @return true, if any rider has already accepted this match
     */
    boolean getIsRiderAccepted() {
        return this.getAcceptedRider() > 0;
    }

    /**
     * @return true, if any rider has already accepted this match
     */
    boolean getIsDriverAccepted() {
        return this.getAcceptedDriver() > 0;
    }

    /**
     * @return true, if for at least one match both rider and driver have
     * already accepted this match
     */
    boolean getIsBothAccepted() {
        return this.getAcceptedBoth() > 0;
    }

    /**
     * Human readable debug output for Matching statistics
     *
     * @return
     */
    public String toString() {

        StringBuilder buf = new StringBuilder();
        buf.append("\n===MatchingStatistics========================");
        buf.append("\nNumber of Matches  : " + this.getNumberOfMatches());
        buf.append("\nAccepted from both : " + this.getAcceptedBoth());
        buf.append("\nState              :  Driver , Rider ");
        buf.append("\nNOT_ADAPTED        : " + getNotAdaptedDriver() + " , " + this.getNotAdaptedDriver());
        buf.append("\nACCEPTED           : " + getAcceptedDriver() + " , " + this.getAcceptedDriver());
        buf.append("\nREJECTED           : " + getRejectedDriver() + " , " + this.getRejectedDriver());
        buf.append("\nCOUNTERMANDED      : " + getCountermandedDriver() + " , " + this.getCountermandedDriver());
        buf.append("\nNO_MORE_AVAILLABLE : " + getRejectedDriver() + " , " + this.getRejectedDriver());
        buf.append("\n=============================================");

        return buf.toString();
    }

    /**
     * Calculate the state of negotians for a ride. This is done by evaluating
     * the matches
     *
     * @return calculated State, see above
     *
     */
    public RideNegotiationConstants getRideMatchingState() {

        // no matchings-> NEW
        if (this.getNumberOfMatches() == 0) {
            return RideNegotiationConstants.STATE_NEW;
        }

        // Only new Matches -> New
        if (this.getNotAdaptedBoth() == this.getNumberOfMatches()) {
            return RideNegotiationConstants.STATE_NEW;
        }

        // determine ACCEPTED rides

        if (this.getAcceptedBoth() > 0) {
            return RideNegotiationConstants.STATE_CONFIRMED_BOTH;
        }

        if (this.getAcceptedDriver() > 0) {
            return RideNegotiationConstants.STATE_DRIVER_ACCEPTED;
        }

        if (this.getAcceptedRider() > 0) {
            return RideNegotiationConstants.STATE_RIDER_REQUESTED;
        }

        // determine COUNTERMANDED rides
        if (this.getCountermandedBoth() > 0) {
            return RideNegotiationConstants.STATE_COUNTERMANDED_BOTH;
        }

        if (this.getCountermandedRider() > 0) {
            return RideNegotiationConstants.STATE_COUNTERMANDED_RIDER;
        }

        if (this.getCountermandedDriver() > 0) {
            return RideNegotiationConstants.STATE_COUNTERMANDED_DRIVER;
        }





        return RideNegotiationConstants.STATE_UNCLEAR;
    }
}
