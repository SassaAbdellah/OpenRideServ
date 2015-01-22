/*
    OpenRide -- Car Sharing 2.0
    Copyright (C) 2010  Fraunhofer Institute for Open Communication Systems (FOKUS)

    Fraunhofer FOKUS
    Kaiserin-Augusta-Allee 31
    10589 Berlin
    Tel: +49 30 3463-7000
    info@fokus.fraunhofer.de

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

package de.fhg.fokus.openride.rating.helperclasses;

import java.util.Date;

/**
 *
 * @author akr, tku
 */
public class OpenRatingInfo {

    private Integer riderRoutId;
    private Integer custId;
    private String custNickname;
    private Date timestamp;
    private Integer rating;
    private String ratingComment;

    public OpenRatingInfo(Integer riderRoutId, Integer custId, String custNickname, Date timestamp) {
        this.riderRoutId = riderRoutId;
        this.custId = custId;
        this.custNickname = custNickname;
        this.timestamp = timestamp;
        this.rating = -2;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getCustNickname() {
        return custNickname;
    }

    public void setCustNickname(String custNickname) {
        this.custNickname = custNickname;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getRatingComment() {
        return ratingComment;
    }

    public void setRatingComment(String ratingComment) {
        this.ratingComment = ratingComment;
    }

    public Integer getRiderRoutId() {
        return riderRoutId;
    }

    public void setRiderRoutId(Integer riderRoutId) {
        this.riderRoutId = riderRoutId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }



}

