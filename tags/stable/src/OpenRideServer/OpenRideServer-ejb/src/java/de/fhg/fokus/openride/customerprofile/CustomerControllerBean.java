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
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideEntity;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.transaction.UserTransaction;

/**
 *
 * @author pab
 */
@Stateless
public class CustomerControllerBean extends ControllerBean implements CustomerControllerLocal {

    @PersistenceContext
    EntityManager em;
    @Temporal(TemporalType.TIMESTAMP)
    UserTransaction u;
    private static String one = "ich";
    @EJB
    private FavoritePointControllerLocal favoritePointControllerBean;
    // will be needed when savely removing rides
    @EJB
    private RiderUndertakesRideControllerLocal riderUndertakesRideControllerBean;
    // will be needed when savely removing rides
    @EJB
    private DriverUndertakesRideControllerLocal driverUndertakesRideControllerBean;
    Logger logger = Logger.getLogger("" + this.getClass());
    final String TEMPLATE_USER = "template_user";

    /*@Override
     public void init() {
     super.init();
     log(this.getClass(), "Init Testing");
     }*/
    /**
     * *********************Businessmethods start*************************
     */
    public CustomerEntity getCustomerByCredentials(String custNickname, String custPasswd) {
        logger.info("getCustomerByCredentials");
        startUserTransaction();
        CustomerEntity c = getCustomerByNickname(custNickname);
        if (c != null && c.getCustPasswd().equals(getMD5Hash(custPasswd))) {
            commitUserTransaction();
            return c;
        }
        commitUserTransaction();
        return null;
    }

    public boolean isNicknameAvailable(String custNickname) {
        startUserTransaction();
        logger.info("isNicknameAvailable");
        CustomerEntity c = getCustomerByNickname(custNickname);
        if (c == null) {
            commitUserTransaction();
            return true;
        }
        commitUserTransaction();
        return false;
    }

    public int addCustomer(String custNickname, String custPasswd, String custFirstname, String custLastname, char custGender, String custEmail, String custMobilephoneno) {
        startUserTransaction();
        logger.info("addCustomer");
        // Make sure no Customer exists for this same nickname
        List<CustomerEntity> customers = (List<CustomerEntity>) em.createNamedQuery("CustomerEntity.findByCustNickname").setParameter("custNickname", custNickname).getResultList();
        if (customers.size() > 0) {
            // No duplicates allowed
            return -1;
        }

        // OK - add them, and return their id
        CustomerEntity c = new CustomerEntity();
        c.setCustNickname(custNickname);
        c.setCustPasswd(getMD5Hash(custPasswd));
        c.setCustFirstname(custFirstname);
        c.setCustLastname(custLastname);
        c.setCustGender(custGender);
        c.setCustEmail(custEmail);
        c.setCustMobilephoneno(custMobilephoneno);
        c.setCustRegistrdate(new Date()); // the current date (timestamp)
        c.setCustGroup("customer");

        em.persist(c);
        commitUserTransaction();

        if (this.getCustomerByNickname(this.TEMPLATE_USER) != null) {
            for (FavoritePointEntity fav : favoritePointControllerBean.getFavoritePointsByCustomer(this.getCustomerByNickname(this.TEMPLATE_USER))) {
                favoritePointControllerBean.addFavoritePoint(fav.getFavptAddress(), fav.getFavptPoint(), fav.getFavptDisplayname(), c);
            }
        }

        return c.getCustId();

    }

    /**
     * This method adds a customer to the database
     */
    public int addCustomer(String custNickname, String custPasswd, String custFirstname, String custLastname, Date custDateofbirth, char custGender, String custMobilephoneno, String custEmail, boolean custIssmoker, boolean custPostident, String custAddrStreet, String custAddrZipcode, String custAddrCity) {
        logger.info("addCustomer");
        startUserTransaction();
        System.out.println("Username " + custNickname);
        boolean exists = false;
        List<CustomerEntity> entities = (List<CustomerEntity>) em.createNamedQuery("CustomerEntity.findByCustNickname").setParameter("custNickname", custNickname).getResultList();

        if (entities.size() > 0) {
            exists = true;
        } else {
            logger.log(Level.INFO, "Entity Customer " + custNickname + "does not exist");
        }

        if (!exists) {
            logger.log(Level.INFO, "So persist it!");
            //Query q = em.createNativeQuery("select * from \"customer\";");
            //System.out.println("[INFO Philipp]The Query: " + q.getResultList().size() + " " + dateFrom.toLocaleString());
            //int index = q.getResultList().size();
            //int index = 0;
            //while ((em.find(CustomerEntity.class, index)) != null) {
            //    index++;
            //}
            //int customer_Id = index;
            CustomerEntity e = new CustomerEntity(custNickname, getMD5Hash(custPasswd), custFirstname, custLastname, custDateofbirth, custGender, custMobilephoneno, custEmail, custIssmoker, custPostident, custAddrStreet, custAddrZipcode, custAddrCity);
            e.setCustGroup("customer");
            e.setCustDriverprefGender(CustomerEntity.PREF_GENDER_DEFAULT);
            e.setCustDriverprefSmoker(CustomerEntity.PREF_SMOKER_DEFAULT);
            e.setCustRiderprefGender(CustomerEntity.PREF_GENDER_DEFAULT);
            e.setCustRiderprefSmoker(CustomerEntity.PREF_SMOKER_DEFAULT);

            em.persist(e);
            commitUserTransaction();
            return e.getCustId();
        } else {
            commitUserTransaction();
            return -1;
        }
    }

    public static String getMD5Hash(String input) {
        StringBuffer stringBuffer = new StringBuffer(1000);
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input.getBytes());
            Formatter f = new Formatter(stringBuffer);
            for (byte b : md5.digest()) {
                f.format("%02x", b);
            }
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return stringBuffer.toString();
    }

    /**
     * Remove (or rather invalidate) a customer. Removing a customer means, that
     * his personal data will be overwritten.
     *
     * Note that this will be called inside a transaction explicitely.
     *
     * @param custId
     */
    public void removeCustomer(int custId) {

        logger.info("removeCustomer : " + custId);
        startUserTransaction();



        //TODO: make sure that all related entities are deleted, too.
        CustomerEntity ce = em.find(CustomerEntity.class, custId);

        // avoid trivialities
        if (ce == null) {
            logger.warning("Unable to retrieve user for custId " + custId + " cannot remove customer");
            return;
        }

        // TODO: remove all those ride request that can still be removed,
        // and invalidate the rest
        List<RiderUndertakesRideEntity> allRides = riderUndertakesRideControllerBean.getRidesForCustomer(ce);

        for (RiderUndertakesRideEntity re : allRides) {

            if (riderUndertakesRideControllerBean.isDeletable(re.getRiderrouteId())) {
                // delete all rides that can be deleted
                logger.info("Deleting Ride " + re.getRiderrouteId());
                riderUndertakesRideControllerBean.removeRide(re.getRiderrouteId());
            } else {
                // invalidate all rides that cannot be deleted
                logger.info("Invalidating Ride " + re.getRiderrouteId());
                riderUndertakesRideControllerBean.invalidateRide(re.getRiderrouteId());
            }
        } //      for (RiderUndertakesRideEntity re: allRides) 

        // TODO: remove all those ride request that can still be removed,
        // and invalidate the rest
        List<DriverUndertakesRideEntity> allDrives = driverUndertakesRideControllerBean.getDrivesForDriver(ce.getCustNickname());

        for (DriverUndertakesRideEntity due : allDrives) {

            if (driverUndertakesRideControllerBean.isDeletable(due.getRideId())) {
                // delete all drives that can be deleted
                logger.info("Deleting Ride " + due.getRideId());
                driverUndertakesRideControllerBean.removeRide(due.getRideId());
            } else {
                // invalidate all rides that cannot be deleted
                logger.info("Invalidating Drive " + due.getRideId());
                driverUndertakesRideControllerBean.invalidateRide(due.getRideId());
            }
        } //      for (RiderUndertakesRideEntity re: allRides) 


        // TODO: 
        //overwrite valid data for this user with randomized data

        String random = "" + Math.random();
        String ts = "" + System.currentTimeMillis();
        String seed = "deleted_user" + random + ":" + ts;

        // set firstname, lastname and gender
        this.setBasePersonalData(custId, seed, seed, '-');
        // invalidate dob, email , cellphone, landline phone
        // address data, smoker data, licensedate
        this.setPersonalData(custId,
                null, // mock date of birth
                seed, //  mock email
                seed, // mock mobile phone
                seed, // mock landline
                seed, // mock cust_addr street
                seed, // mock zipcode
                seed, // mock City
                'n', // mock smokerprefs
                null // mock licensedate
                );

        // invalidate nickname and password
        this.setNickname(custId, seed);
        this.setPassword(custId, seed);

        em.merge(ce);
        em.persist(ce);
        //
        // do not remove the customer, only overwrite the data
        //
        // em.remove(e);
        commitUserTransaction();
    }

    /**
     *
     * @param custId
     * @return
     */
    public CustomerEntity getCustomer(int custId) {
        logger.info("getCustomer with custId: " + custId);
        startUserTransaction();
        //CustomerEntity e = em.find(CustomerEntity.class, custId);
        List<CustomerEntity> e = (List<CustomerEntity>) em.createNamedQuery("CustomerEntity.findByCustId").setParameter("custId", custId).getResultList();
        commitUserTransaction();
        if (e != null && e.size() > 0) {
            return e.get(0);
        } else {
            return null;
        }
    }

    /**
     * Returns a
     * <code>CustomerEntity</code> for a given
     * <code>nickname</code>.
     *
     * @param nickname The nickname of the requested Customer.
     * @return
     */
    public CustomerEntity getCustomerByNickname(String nickname) {
        logger.info("getCustomerByNickname");
        startUserTransaction();
        List<CustomerEntity> q = (List<CustomerEntity>) em.createNamedQuery("CustomerEntity.findByCustNickname").setParameter("custNickname", nickname).getResultList();
        if (q.size() > 0) {
            // should only be one result, since customernicknames are unique in DB
            CustomerEntity v = (CustomerEntity) q.get(0);
            if (v != null) {
                return v;
            }
        }
        commitUserTransaction();
        return null;
    }

    /**
     * Returns a
     * <code>CustomerEntity</code> for a given
     * <code>email</code>.
     *
     * @param email The email of the requested Customer.
     * @return
     */
    public CustomerEntity getCustomerByEmail(String email) {
        logger.info("getCustomerByEmail");
        startUserTransaction();
        List<CustomerEntity> q = (List<CustomerEntity>) em.createNamedQuery("CustomerEntity.findByCustEmail").setParameter("custEmail", email).getResultList();
        if (q.size() > 0) {
            // should only be one result, since email addresses are unique in DB
            CustomerEntity v = (CustomerEntity) q.get(0);
            if (v != null) {
                return v;
            }
        }
        commitUserTransaction();
        return null;
    }

    public LinkedList<CustomerEntity> getAllCustomers() {
        startUserTransaction();

        List<CustomerEntity> l = em.createNamedQuery("CustomerEntity.findAll").getResultList();
        LinkedList<CustomerEntity> ll = new LinkedList<CustomerEntity>(l);

        commitUserTransaction();
        return ll;
    }

    /**
     *
     */
    public void setCustomer() {
        startUserTransaction();
        //TODO: what shall this method do?
        commitUserTransaction();
    }

    /**
     * This method can be used to check whether a Customer with the
     * <code>username</code> and
     * <code>password</code> exists.
     *
     * @param username
     * @param password
     * @return true if a customer exists, false if not.
     */
    public boolean isRegistered(String username, String password) {
        logger.info("isRegistered");
        startUserTransaction();
        //Query q = em.createNativeQuery("SELECT * FROM customer c WHERE c.cust_nickname = '"+username+"';");
        Query q = em.createNamedQuery("CustomerEntity.findByCustNickname").setParameter("custNickname", username);
        if (q.getResultList().size() > 0) {

            CustomerEntity v = (CustomerEntity) q.getResultList().get(0);
            if (v.getCustPasswd().equals(getMD5Hash(password))) {
                return true;
            }
        }
        commitUserTransaction();

        return false;
    }

    /**
     * This method updates a sessionId for a User related to his nickname and
     * password. This is needed if Sessions shall be supported by the
     * application.
     *
     * @param nickname
     * @param password
     * @param id
     */
    /*public void updateSessionId(String nickname, String password, String id) {
     init();
     CustomerEntity e = (CustomerEntity)em.createNamedQuery("CustomerEntity.findByCustNickname").setParameter("cust_nickname", nickname).getSingleResult();
     if(e.getCustPasswd().equals(password)){
     e.setCustSessionId(id);
     em.merge(e);
     }
     //else throw an exception? Error Management.
     finish();
     }*/
    /**
     * This method can be called to check whether someone with
     * <code>nickname</code> is currently logged in. Therefore the field has to
     * be set after the customer correctly logged in.
     *
     * @param nickname
     * @return
     */
    /*
     FIXME: this seems not to be needed!
     public boolean isLoggedIn(String nickname) {

     boolean isLoggedIn = false;

     init();
     //Query q = em.createNativeQuery("SELECT * FROM customer c WHERE c.cust_nickname = '"+username+"';");
     Query q = em.createNamedQuery("CustomerEntity.findByCustNickname").setParameter("custNickname", "nickname2");
     if(q.getResultList().size()>0){

     CustomerEntity v = (CustomerEntity)q.getResultList().get(0);
     isLoggedIn = v.getIsLoggedIn();
     }
     finish();

     return isLoggedIn;
     }*/
    /**
     * *********************Businessmethods end**************************
     */
    public void persist(Object object) {
        startUserTransaction();
        em.persist(object);
        commitUserTransaction();
    }

    public void setPersonalData(int custId, Date custDateofbirth, String custEmail, String custMobilePhoneNo, String custFixedPhoneNo, String custAddrStreet, String custAddrZipcode, String custAddrCity, char custIssmoker, Date custLicenseDate) {
        startUserTransaction();
        logger.info("setPersonalData");
        CustomerEntity c = getCustomer(custId);

        c.setCustDateofbirth(custDateofbirth);

        c.setCustEmail(custEmail);

        c.setCustMobilephoneno(custMobilePhoneNo);

        c.setCustFixedphoneno(custFixedPhoneNo);

        c.setCustAddrStreet(custAddrStreet);

        c.setCustAddrZipcode(custAddrZipcode);

        c.setCustAddrCity(custAddrCity);

        if (custIssmoker == "y".charAt(0)) {
            c.setCustIssmoker(true);
        } else if (custIssmoker == "n".charAt(0)) {
            c.setCustIssmoker(false);
        } else {
            c.setCustIssmoker(null);
        }

        c.setCustLicensedate(custLicenseDate);

        em.persist(c);
        commitUserTransaction();
    }

    public void setBasePersonalData(int custId, String custFirstName, String custLastName, char custGender) {
        startUserTransaction();
        logger.info("setBasePersonalData");
        CustomerEntity c = getCustomer(custId);

        c.setCustFirstname(custFirstName);

        c.setCustLastname(custLastName);

        c.setCustGender(custGender);

        em.persist(c);
        commitUserTransaction();
    }

    @Override
    public void setPassword(int custId, String custPasswd) {
        startUserTransaction();
        logger.info("setPassword for custId : " + custId);
        CustomerEntity c = getCustomer(custId);
        c.setCustPasswd(getMD5Hash(custPasswd));
        commitUserTransaction();
    }
    
    @Override
     public void setNickname(int custId, String custNicknameArg) {
        startUserTransaction();
        logger.info("setNickname for custId : " + custId);
        CustomerEntity c = getCustomer(custId);
        c.setCustNickname(custNicknameArg);
        commitUserTransaction();
    }
    
    

    public void setDriverPrefs(int custId, int custDriverprefAge, char custDriverprefGender, char custDriverprefSmoker) {
        startUserTransaction();
        logger.info("setDriverPrefs");
        CustomerEntity c = getCustomer(custId);
        //age
        c.setCustDriverprefAge(custDriverprefAge);
        //gender
        if (custDriverprefGender == CustomerEntity.PREF_GENDER_GIRLS_ONLY
                || custDriverprefGender == CustomerEntity.PREF_GENDER_DONT_CARE) {
            c.setCustDriverprefGender(custDriverprefGender);
        } else {
            c.setCustDriverprefGender(CustomerEntity.PREF_GENDER_DEFAULT);
            logger.info("invalid gender pref - set to default (" + custDriverprefGender + ")");
        }
        //smoker
        if (custDriverprefSmoker == CustomerEntity.PREF_SMOKER_DESIRED
                || custDriverprefSmoker == CustomerEntity.PREF_SMOKER_DONT_CARE
                || custDriverprefSmoker == CustomerEntity.PREF_SMOKER_NOT_DESIRED) {
            c.setCustDriverprefSmoker(custDriverprefSmoker);
        } else {
            c.setCustDriverprefSmoker(CustomerEntity.PREF_SMOKER_DEFAULT);
            logger.info("invalid smoker pref - set to default (" + custDriverprefSmoker + ")");
        }
        commitUserTransaction();
    }

    public void setRiderPrefs(int custId, int custRiderprefAge, char custRiderprefGender, char custRiderprefSmoker) {
        logger.info("setRiderPrefs");
        startUserTransaction();
        CustomerEntity c = getCustomer(custId);
        //age
        c.setCustRiderprefAge(custRiderprefAge);
        //gender
        if (custRiderprefGender == CustomerEntity.PREF_GENDER_GIRLS_ONLY
                || custRiderprefGender == CustomerEntity.PREF_GENDER_DONT_CARE) {
            c.setCustRiderprefGender(custRiderprefGender);
        } else {
            c.setCustRiderprefGender(CustomerEntity.PREF_GENDER_DEFAULT);
            logger.info("invalid gender pref - set to default (" + custRiderprefGender + ")");
        }
        //smoker
        if (custRiderprefSmoker == CustomerEntity.PREF_SMOKER_DESIRED
                || custRiderprefSmoker == CustomerEntity.PREF_SMOKER_DONT_CARE
                || custRiderprefSmoker == CustomerEntity.PREF_SMOKER_NOT_DESIRED) {
            c.setCustRiderprefSmoker(custRiderprefSmoker);
        } else {
            c.setCustRiderprefSmoker(CustomerEntity.PREF_SMOKER_DEFAULT);
            logger.info("invalid smoker pref - set to default (" + custRiderprefSmoker + ")");
        }
        commitUserTransaction();
    }
}
