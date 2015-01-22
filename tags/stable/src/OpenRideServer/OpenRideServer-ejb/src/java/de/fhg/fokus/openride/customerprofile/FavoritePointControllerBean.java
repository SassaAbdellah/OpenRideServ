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

import de.fhg.fokus.openride.helperclasses.ControllerBean;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author pab
 */
@Stateless
public class FavoritePointControllerBean extends ControllerBean implements FavoritePointControllerLocal {

    @PersistenceContext
    EntityManager em;

    private UserTransaction u;

    /**
     *
     * @param favptAddress
     * @param favptPoint
     * @param favptDisplayname
     * @param customer
     * @return The added favpt's ID.
     */
    public int addFavoritePoint(String favptAddress, String favptPoint, String favptDisplayname, CustomerEntity customer) {        
        // Make sure no favpt of same Displayname exists for this customer
        if (getFavoritePointByDisplayName(favptDisplayname, customer) != null) {
            // No duplicates allowed
            return -1;
        }
        FavoritePointEntity fp = new FavoritePointEntity();
        fp.setFavptAddress(favptAddress);
        fp.setFavptPoint(favptPoint);
        fp.setFavptDisplayname(favptDisplayname);
        fp.setFavptFrequency(0);
        fp.setCustId(customer);
        em.persist(fp);
        return fp.getFavptId();
    }

    
    
    /**
     *
     * @param custId
     * @return True on success, false if an invalid favptId is encountered.
     */
    public boolean removeFavoritePoint(int favptId) {
        // Get fp corresponding to id
        FavoritePointEntity fp = em.find(FavoritePointEntity.class, favptId);
        if (fp == null) {
            return false;
        }
        // Remove fp
        em.remove(fp);
        return true;
    }
    
   
    
    

    public FavoritePointEntity getFavoritePoint(int favptId) {
        FavoritePointEntity fp = em.find(FavoritePointEntity.class, favptId);
        return fp;
    }

    public List<FavoritePointEntity> getFavoritePointsByCustomer(CustomerEntity customer) {
        try {
            List<FavoritePointEntity> entities = em.createNamedQuery("FavoritePointEntity.findByCustId").setParameter("custId", customer).getResultList();
            return entities;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public FavoritePointEntity getFavoritePointByDisplayName(String displayName, CustomerEntity customer) {
        try {
            FavoritePointEntity fp = (FavoritePointEntity) em.createNamedQuery("FavoritePointEntity.findByFavptDisplaynameCustId").setParameter("favptDisplayname", displayName).setParameter("custId", customer).getSingleResult();
            return fp;
        } catch (NoResultException e) {
            return null;
        }
    }

    public void setFrequency(int favptId, int favptFrequency) {
        FavoritePointEntity fp = getFavoritePoint(favptId);
        if (fp != null) {
            fp.setFavptFrequency(favptFrequency);
            em.persist(fp);
        }
    }

    public void setDisplayname(int favptId, String favptDisplayname) {
        FavoritePointEntity fp = getFavoritePoint(favptId);
        if (fp != null) {
            fp.setFavptDisplayname(favptDisplayname);
            em.persist(fp);
        }
    }

    
    @Override
    public void removeFavoritePointsForCustomer(CustomerEntity ce) {
        
        List <FavoritePointEntity> fpel=getFavoritePointsByCustomer(ce);
        
        for(FavoritePointEntity fpe: fpel){
            em.remove(fpe);
        }
    }

} // class
