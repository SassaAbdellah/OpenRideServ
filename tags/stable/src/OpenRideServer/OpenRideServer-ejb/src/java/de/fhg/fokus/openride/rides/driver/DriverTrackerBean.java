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

package de.fhg.fokus.openride.rides.driver;

import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.helperclasses.ControllerBean;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.postgis.Point;

/**
 *
 * @author pab
 */
@Stateless
public class DriverTrackerBean extends ControllerBean implements DriverTrackerLocal {
    @PersistenceContext
    private EntityManager em;

    private UserTransaction u;

    /**
     * This method can be called to update the current position of a driver.
     * @param driverId
     * @param currPos
     */
    public void updateDriverPosition(String nickname, Point currPos) {
         startUserTransaction();
         List<DriverUndertakesRideEntity> returnList=null;

        //TODO: some errorhandling

        // Get the CustomerEntity by <code>nickname</code>
        List<CustomerEntity> c = (List<CustomerEntity>)em.createNamedQuery("CustomerEntity.findByCustNickname").setParameter("custNickname", nickname).getResultList();

        if(c.size()==1){
            // Get the Entity. It should be only one, since nicknames are unique by constraint.
            CustomerEntity e = c.get(0);
            // Use the Id of the user to get his rides.
            //returnList = em.createNativeQuery("SELECT d FROM DriverUndertakesRide d WHERE d.cust_id = +"+e.getCustId()+";").getResultList();
            returnList = em.createNamedQuery("DriverUndertakesRideEntity.findByCustId").setParameter("custId", e.getCustId()).getResultList();
            //TODO: check whether getResult returns null or empty List if nothing was found.
            if(returnList.size()>0)
                returnList.get(0).setRideCurrpos(currPos); // TODO open question: is here a postgis geometry data type necessary?
            else{
                //TODO: Exception?
            }
        }
         commitUserTransaction();
    }

    public void persist(Object object) {
         startUserTransaction();
        em.persist(object);
        commitUserTransaction();
    }
}
