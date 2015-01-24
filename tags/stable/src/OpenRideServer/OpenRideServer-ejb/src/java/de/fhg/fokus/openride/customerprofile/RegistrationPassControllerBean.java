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

import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Tilo Kussatz <tilo at kussatz.com>
 */
@Stateless
public class RegistrationPassControllerBean implements RegistrationPassControllerLocal {

    @PersistenceContext
    private EntityManager em;

    public int addRegistrationPass(String passCode) {

        // Make sure no RegistrationPassEntity exists for this same passCode
        List<RegistrationPassEntity> passes = (List<RegistrationPassEntity>) em.createNamedQuery("RegistrationPassEntity.findByPasscode").setParameter("passcode", passCode).getResultList();
        if (passes.size() > 0) {
            // No duplicates allowed
            return -1;
        }

        // OK - add it, and return its id
        RegistrationPassEntity rp = new RegistrationPassEntity(passCode, new Date());
        em.persist(rp);
        return rp.getId();

    }

    public boolean removeRegistrationPass(int id) {
        // Get rp corresponding to id
        RegistrationPassEntity rp = em.find(RegistrationPassEntity.class, id);
        if (rp == null) {
            // Can't delete nothing...
            return false;
        }
        // Remove rp
        em.remove(rp);
        return true;
    }

    public boolean isValid(int id) {
        RegistrationPassEntity rp = em.find(RegistrationPassEntity.class, id);
        return (rp != null && rp.getUsageDate() == null);
    }

    public void setInvalid(int id) {
        RegistrationPassEntity rp = em.find(RegistrationPassEntity.class, id);
        if (rp != null) {
            rp.setUsageDate(new Date());
        }
    }

    public RegistrationPassEntity getRegistrationPassByPassCode(String passCode) {
        try {
            RegistrationPassEntity rp = (RegistrationPassEntity) em.createNamedQuery("RegistrationPassEntity.findByPasscode").setParameter("passcode", passCode).getSingleResult();
            System.out.println(rp);
            return rp;
        } catch (NoResultException e) {
            return null;
        }

    }

    public void setInvalidByPassCode(String passCode) {
        RegistrationPassEntity rp = getRegistrationPassByPassCode(passCode);
        if (rp != null) {
            rp.setUsageDate(new Date());
        }
    }

    public void setCustId(int id, CustomerEntity custId) {
        RegistrationPassEntity rp = em.find(RegistrationPassEntity.class, id);
        if (rp != null) {
            rp.setCustId(custId);
        }
    }

    public void setCustIdByPassCode(String passCode, CustomerEntity custId) {
        RegistrationPassEntity rp = getRegistrationPassByPassCode(passCode);
        if (rp != null) {
            rp.setCustId(custId);
        }
        em.merge(rp);
    }

    public RegistrationPassEntity getRegistrationPass(int id) {
        RegistrationPassEntity rp = em.find(RegistrationPassEntity.class, id);
        return rp;
    }

    public String getRandomValidPassCode() {
        try {
            // Get valid passcodes (usageDate == null)
            List<RegistrationPassEntity> entities = em.createNamedQuery("RegistrationPassEntity.findAllValid").getResultList();
            //System.out.println(entities.size() + " valid passes found");
            if (entities.size() > 0) {
                // Shuffle them ..
                Collections.shuffle(entities);
                // .. and return a random, valid passcode
                return entities.get(0).getPasscode();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public List<RegistrationPassEntity> getInvalidPassCodes() {
        // Get invalid passcodes (usageDate != null)
        List<RegistrationPassEntity> entities = em.createNamedQuery("RegistrationPassEntity.findAllInvalid").getResultList();
        return entities;
    }

    public String getNextValidPassCode() {
        try {
            // Get valid passcodes (usageDate == null)
            List<RegistrationPassEntity> entities = em.createNamedQuery("RegistrationPassEntity.findAllValid").getResultList();
            //System.out.println(entities.size() + " valid passes found");
            if (entities.size() > 0) {
                // Return first of the available codes
                return entities.get(0).getPasscode();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RegistrationPassEntity> getRegistrationPassByUsageDate(Date date) {
        List<RegistrationPassEntity> passes = (List<RegistrationPassEntity>) em.createNamedQuery("RegistrationPassEntity.findByUsageDate").setParameter("usageDate", date).getResultList();
        return passes;
    }
}
