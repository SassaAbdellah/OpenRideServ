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

package de.fhg.fokus.openride.customerprofile;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author pab
 */
@Local
public interface FavoritePointControllerLocal {
    
    int addFavoritePoint(String favptAddress, String favptPoint, String favptDisplayname, CustomerEntity customer);

    boolean removeFavoritePoint(int favptId);
    
    /** Remove all the favorite points for this customer,
     *  typically if the customer is deleted
     * 
     * @param ce 
     */
    public void removeFavoritePointsForCustomer(CustomerEntity ce);

    FavoritePointEntity getFavoritePoint(int favptId);

    public FavoritePointEntity getFavoritePointByDisplayName(String displayName, CustomerEntity customer);

    List<FavoritePointEntity> getFavoritePointsByCustomer(CustomerEntity customer);

    void setFrequency(int favptId, int favptFrequency);

    void setDisplayname(int favptId, String favptDisplayname);

}
