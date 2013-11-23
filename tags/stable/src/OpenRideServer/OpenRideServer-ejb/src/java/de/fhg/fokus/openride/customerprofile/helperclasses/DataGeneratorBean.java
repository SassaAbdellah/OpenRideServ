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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhg.fokus.openride.customerprofile.helperclasses;

import de.fhg.fokus.openride.customerprofile.CarDetailsControllerLocal;
import de.fhg.fokus.openride.customerprofile.CustomerControllerLocal;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.customerprofile.FavoritePointControllerLocal;
import de.fhg.fokus.openride.customerprofile.helperclasses.RandomInfo.RandomCar;
import de.fhg.fokus.openride.customerprofile.helperclasses.RandomInfo.RandomCustomer;
import de.fhg.fokus.openride.customerprofile.helperclasses.RandomInfo.RandomDrive;
import de.fhg.fokus.openride.customerprofile.helperclasses.RandomInfo.RandomRide;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideControllerLocal;
import de.fhg.fokus.openride.routing.RouterBeanLocal;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author fvi
 */
@Stateless
public class DataGeneratorBean implements DataGeneratorBeanLocal {

    private final static double DRIVE_MIN_DISTANCE_METERS = 4000;
    private final static double DRIVE_MAX_DISTANCE_METERS = Double.MAX_VALUE;
    private final static java.util.Date DRIVE_MIN_START_TIME = new java.util.Date(2010 - 1900, 0, 1);
    private final static java.util.Date DRIVE_MAX_START_TIME = new java.util.Date(2010 - 1900, 0, 2);
    private final static int DRIVE_MIN_DETOUR_PERCENT = 15;
    private final static int DRIVE_MAX_DETOUR_PERCENT = 30;

    private final static double RIDE_MIN_DISTANCE_METERS = 2500;
    private final static double RIDE_MAX_DISTANCE_METERS = Double.MAX_VALUE;
    private final static java.util.Date RIDE_MIN_START_TIME = new java.util.Date(2010 - 1900, 0, 1);
    private final static java.util.Date RIDE_MAX_START_TIME = new java.util.Date(2010 - 1900, 0, 2);
    private final static int RIDE_MIN_WAIT_TIME_MINUTES = 15;
    private final static int RIDE_MAX_WAIT_TIME_MINUTES = 120;
    private final static double RIDE_PRICE_PER_KM_AIR_DISTANCE = 50d;

    @EJB
    private RouterBeanLocal routerBean;
    @EJB
    private FavoritePointControllerLocal favoritePointControllerBean;
    @EJB
    private RiderUndertakesRideControllerLocal riderUndertakesRideControllerBean;
    @EJB
    private DriverUndertakesRideControllerLocal driverUndertakesRideControllerBean;
    @EJB
    private CarDetailsControllerLocal carDetailsControllerBean;
    @EJB
    private CustomerControllerLocal customerControllerBean;

    public void insertRandomData(int numCustomers, int maxCarsPercustomer, int maxFavPointsPerCustomer, int numDrives, int numRides) {
        RandomInfo rnd = null;
        try {
            rnd = new RandomInfo(RandomInfo.getNewConnection(), routerBean);
            //insert customers
            Logger.getLogger(DataGeneratorBean.class.getName()).info("inserting " + numDrives + " random customers : ");
            int[] customerIds = new int[numCustomers];
            for (int i = 0; i < numCustomers; i++) {
                int custId = -1;
                while (custId == -1) {
                    custId = addCustomer(rnd);
                }
                Logger.getLogger(DataGeneratorBean.class.getName()).info("inserted customer " + (i + 1) + " / " + numCustomers);
                customerIds[i] = custId;
            }
            //insert car details
            Logger.getLogger(DataGeneratorBean.class.getName()).info("inserting random cardetails : ");
            for (int i = 0; i < customerIds.length; i++) {
                int numCars = rnd.randomInt(1, maxCarsPercustomer);
                for (int j = 0; j < numCars; j++) {
                    addCarDetails(rnd, customerIds[i]);
                    Logger.getLogger(DataGeneratorBean.class.getName()).info("inserted cardetails for cust: " + (i + 1) + " / " + numCustomers + " car: " + (j + 1) + " / " + numCars);
                }
            }
            //insert rides
            Logger.getLogger(DataGeneratorBean.class.getName()).info("inserting " + numDrives + " random rides : ");
            int insertedRides = 0;
            while(insertedRides < numRides) {
                int i = rnd.randomInt(0, customerIds.length - 1);
                 if(addRide(rnd, customerIds[i])){
                    insertedRides++;
                    Logger.getLogger(DataGeneratorBean.class.getName()).info("inserted drive " + insertedRides + " / " + numRides);
                }
            }
            //insert drives
            Logger.getLogger(DataGeneratorBean.class.getName()).info("inserting " + numDrives + " random drives : ");
            int insertedDrives = 0;
            while(insertedDrives < numDrives) {
                int i = rnd.randomInt(0, customerIds.length - 1);
                if(addDrive(rnd, customerIds[i])){
                    insertedDrives++;
                    Logger.getLogger(DataGeneratorBean.class.getName()).info("inserted drive " + insertedDrives + " / " + numDrives);
                }
            }
            rnd.getConnection().close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataGeneratorBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DataGeneratorBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            if(rnd != null) {
                try {
                    rnd.getConnection().close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataGeneratorBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private int addCustomer(RandomInfo rnd) {
        RandomCustomer customer = rnd.getRandomCustomer();
        int custId = customerControllerBean.addCustomer(
                customer.getNickName(),
                customer.getPassword(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getDateOfBirth(),
                customer.getGender(),
                customer.getMobilPhoneNumber(),
                customer.getEmail(),
                customer.isIsSmoker(),
                customer.isPostIdent(),
                customer.getStreet(),
                customer.getZipCode(),
                customer.getCity()
        );
        //set prefs.:
        if(custId != -1) {
            customerControllerBean.setRiderPrefs(
                    custId,
                    customer.getRiderPrefAge(),
                    customer.getRiderPrefGender(),
                    customer.getRiderPrefSmoker()
            );
            customerControllerBean.setDriverPrefs(
                    custId,
                    customer.getDriverPrefAge(),
                    customer.getDriverPrefGender(),
                    customer.getDriverPrefSmoker()
            );
        }
        return custId;
    }

    private boolean addCarDetails(RandomInfo rnd, int custId) {
        RandomCar car = rnd.getRandomCar();
        CustomerEntity c = customerControllerBean.getCustomer(custId);
        if(c != null) {
            carDetailsControllerBean.addCarDetails(
                  c,    
                  car.getBrand(),
                  car.getBuildYear(),
                  car.getColor(),
                  car.getPlateNo()
            );
        return true;
        } else {
            return false;
        }
    }
    
    private boolean addDrive(RandomInfo rnd, int custId) {
        RandomDrive drive = rnd.getRandomDrive(
                DRIVE_MIN_DISTANCE_METERS,
                DRIVE_MAX_DISTANCE_METERS,
                DRIVE_MIN_START_TIME,
                DRIVE_MAX_START_TIME,
                DRIVE_MIN_DETOUR_PERCENT,
                DRIVE_MAX_DETOUR_PERCENT
        );
        return driverUndertakesRideControllerBean.addRide(
                custId, 
                drive.getStartPt(),
                drive.getEndPt(),
                drive.getStartTime(),
                drive.getComment(),
                drive.getDetourMinutes(),
                drive.getDetourKm(),
                drive.getDetourPercent(),
                drive.getOfferedSeatsNo(),
                drive.getRoutePoints()
        ) != -1;
    }

    private boolean addRide(RandomInfo rnd, int custId) {
        RandomRide ride = rnd.getRandomRide(
            RIDE_MIN_DISTANCE_METERS,
            RIDE_MAX_DISTANCE_METERS,
            RIDE_MIN_START_TIME,
            RIDE_MAX_START_TIME,
            RIDE_MIN_WAIT_TIME_MINUTES,
            RIDE_MAX_WAIT_TIME_MINUTES,
            RIDE_PRICE_PER_KM_AIR_DISTANCE
        );
        return riderUndertakesRideControllerBean.addRideRequest(
            custId,     
            ride.getStartTimeEarlies(),      
            ride.getStartTimeLatest(),
            ride.getNoPassengers(),
            ride.getStartPt(),
            ride.getEndPt(),
            ride.getPrice(),
            null/*FIXME: here a comment should be added!*/
        ) != -1;
    }
}
