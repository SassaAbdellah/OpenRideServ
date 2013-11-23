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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


import java.util.List;

/**
 *
 * @author pab
 */
@Stateless
public class CarDetailsControllerBean extends ControllerBean implements CarDetailsControllerLocal {

    @PersistenceContext
    private EntityManager em;


    /**
     * This method adds a <code>CarDetailsEntity</code> to the database.
     * @param custId The CustomerEntity that relates to the car.
     * @param carBrand the brand of the car.
     * @param buildYear
     * @param color
     * @param plateno
     */
    public void addCarDetails(CustomerEntity customer, String brand, Short buildYear, String color, String plateNo) {
        startUserTransaction();

        CarDetailsEntity cd = new CarDetailsEntity();
        cd.setCardetBrand(brand);
        cd.setCardetBuildyear(buildYear);
        cd.setCardetColour(color);
        cd.setCardetPlateno(plateNo);
        cd.setCustId(customer);
        em.persist(cd);

        commitUserTransaction();
    }

    /**
     * This method updates, or adds if none does yet exist, a <code>CarDetailsEntity</code> to the database.
     * @param custId The CustomerEntity that relates to the car.
     * @param carBrand the brand of the car.
     * @param buildYear
     * @param color
     * @param plateno
     */
    public void updateCarDetails(CustomerEntity customer, String brand, Short buildYear, String color, String plateNo) {
        startUserTransaction();

        CarDetailsEntity cd = getCarDetails(customer);
        if (cd != null) {
            cd.setCardetBrand(brand);
            cd.setCardetBuildyear(buildYear);
            cd.setCardetColour(color);
            cd.setCardetPlateno(plateNo);
            em.persist(cd);
        } else {
            addCarDetails(customer, brand, buildYear, color, plateNo);
        }

        commitUserTransaction();
    }

    
    
    /**
     * This method updates an existing <code>CarDetailsEntity</code> to the database.
     * @param cardetId  cardetId identifying the car
     * @param carBrand the brand of the car.
     * @param buildYear
     * @param color
     * @param plateno
     */
    public void updateCarDetails(int cardetId, String brand, Short buildYear, String color, String plateNo) {
        startUserTransaction();

        CarDetailsEntity cd = getCarDetailsByCardetId(cardetId);
        if (cd != null) {
            cd.setCardetBrand(brand);
            cd.setCardetBuildyear(buildYear);
            cd.setCardetColour(color);
            cd.setCardetPlateno(plateNo);
            em.persist(cd);
        } else {
            System.err.println("Attempt to update nonexistent car "+cardetId);
        }

        commitUserTransaction();
    }

    
    
    
    
    /**
     * This method removes one specific car from the DB.
     * @param carDetid This parameter identifies the car that shall be deleted.
     */
    public void removeCarDetails(int carDetid) {
        startUserTransaction();
        em.remove(em.find(CarDetailsEntity.class, carDetid));
        commitUserTransaction();
    }

    public void persist(Object object) {
        startUserTransaction();
        em.persist(object);
        commitUserTransaction();
    }

    /** This method returns the Cardetails of a specific car for a customer. 
     *  FIXME: one customer can only have one car? => For now, yes.
     *  TODO: use method getCarDetailsList instead, then remove 
     *   *this method* (JL)
     * 
     * 
     * @param custId
     * @return
     */
    public CarDetailsEntity getCarDetails(CustomerEntity customer) {
        try {
            return (CarDetailsEntity) em.createNamedQuery("CarDetailsEntity.findByCustId").setParameter("custId", customer).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    
    

    
    
    
    
    
    /** This method returns the Cardetails of a cat with specific ID. 
     * 
     * 
     * @param custId
     * @return
     */
    public CarDetailsEntity getCarDetailsByCardetId ( int cardetId) {
        try {
            return (CarDetailsEntity) em.createNamedQuery("CarDetailsEntity.findByCardetId").setParameter("cardetId", cardetId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    
    
    
    
    
    /** This method returns the list of CarDetails for a customer. 
     *  @param custId
     *  @return
     */
    public List <CarDetailsEntity> getCarDetailsList(CustomerEntity customer) {
        try {
            return   em.createNamedQuery("CarDetailsEntity.findByCustId").setParameter("custId", customer).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    @Override
    
    public void removeCardetailsForCustomer(CustomerEntity customer) {
        
        
        List <CarDetailsEntity> lcde=getCarDetailsList(customer);
    
        if(lcde!=null){
            
            for(CarDetailsEntity cde : lcde){
                em.remove(cde);
            }
        }
    }

  
    
    
    
    
    
} // class CarDetailsControllerBean
