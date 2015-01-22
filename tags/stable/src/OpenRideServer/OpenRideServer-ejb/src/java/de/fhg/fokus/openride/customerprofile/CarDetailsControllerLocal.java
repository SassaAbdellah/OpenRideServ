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

import javax.ejb.Local;
import java.util.List;

/**
 *
 * @author pab
 */
@Local
public interface CarDetailsControllerLocal {

    void addCarDetails(CustomerEntity customer, String brand, Short buildYear, String color, String plateNo);

    void updateCarDetails(CustomerEntity customer, String brand, Short buildYear, String color, String plateNo);
    void updateCarDetails(int cardetId, String brand, Short buildYear, String color, String plateNo);
      
    
    
    void removeCarDetails(int carDetid);
    
    CarDetailsEntity getCarDetailsByCardetId(int carDetid);
    
    
    CarDetailsEntity getCarDetails(CustomerEntity customer);
    
    List <CarDetailsEntity> getCarDetailsList(CustomerEntity customer);
    
    
    /** Remove all Cars for given Customer -- this is needed when 
     *  dropping a customerEntry
     * 
     * @param customer 
     */
    void removeCardetailsForCustomer(CustomerEntity customer);
    
    
    
    
}
