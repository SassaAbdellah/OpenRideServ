package de.avci.joride.jbeans.customerprofile;

import de.avci.joride.session.HTTPUser;
import de.avci.joride.utils.PropertiesLoader;
import java.util.Collection;

import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Small Wrapper class making Entity Bean CustomerEntity availlable as a CDI
 * Bean for Use in JSF Frontend.
 *
 * @author jochen
 *
 */
@Named("jprofile")
@RequestScoped
public class JCustomerEntity extends CustomerEntity {

    Logger logger = Logger.getLogger("" + this.getClass());
    /**
     * Normalizer for normalizing input
     */
    private CustomerDataNormalizer normalizer = new CustomerDataNormalizer();
    /**
     * A character signifying Nonsmoker in prefrerences and personal data
     *
     */
    protected static final char NONSMOKER_CHAR = 'n';

    /**
     * @return char used to mark nonsmoker in database
     */
    public char getNonsmokerChar() {
        return NONSMOKER_CHAR;
    }
    /**
     * A character signifying Smoker in prefrerences and personal data
     *
     */
    protected static final char SMOKER_CHAR = 'y';

    /**
     * @return char used to mark smoker in database
     */
    public char getSmokerChar() {
        return SMOKER_CHAR;
    }

    /**
     * Empty public bean constructor.
     */
    public JCustomerEntity() {
        super();
    }

    /**
     * Update Data in the JCustomerEntityBean from Database
     */
    public void updateFromDB() {

        CustomerEntity ce = (new JCustomerEntityService()).getCustomerEntitySafely();
        this.updateFromCustomerEntity(ce);
    }
    
    
    /** Update data from a customerEntity
     * 
     * @param ce 
     */
    public void updateFromCustomerEntity(CustomerEntity ce){

        this.setCustAccountBalance(ce.getCustAccountBalance());
        this.setCustAddrCity(ce.getCustAddrCity());
        this.setCustAddrStreet(ce.getCustAddrStreet());
        this.setCustAddrZipcode(ce.getCustAddrZipcode());
        this.setCustBankAccount(ce.getCustBankAccount());
        this.setCustBankCode(ce.getCustBankCode());
        this.setCustDateofbirth(ce.getCustDateofbirth());
        this.setCustDriverprefAge(ce.getCustDriverprefAge());
        this.setCustDriverprefGender(ce.getCustRiderprefGender());
        this.setCustDriverprefMusictaste(ce.getCustDriverprefMusictaste());
        this.setCustDriverprefSmoker(ce.getCustDriverprefIssmoker());
        this.setCustEmail(ce.getCustEmail());
        this.setCustFirstname(ce.getCustFirstname());
        this.setCustFixedphoneno(ce.getCustFixedphoneno());
        this.setCustGender(ce.getCustGender());
        this.setCustGroup(ce.getCustGroup());
        this.setCustId(ce.getCustId());
        this.setCustIssmoker(ce.getCustIssmoker());
        this.setCustLastname(ce.getCustLastname());
        this.setCustLicensedate(ce.getCustLicensedate());
        this.setCustMobilephoneno(ce.getCustMobilephoneno());
        this.setCustNickname(ce.getCustNickname());
        this.setCustPasswd(ce.getCustPasswd());
        this.setCustPostident(ce.getCustPostident());
        this.setCustPresencemssg(ce.getCustPresencemssg());
        this.setCustProfilepic(ce.getCustProfilepic());
        this.setCustRegistrdate(ce.getCustRegistrdate());
        //
        // Rider Preferences
        //
        this.setCustRiderprefAge(ce.getCustRiderprefAge());
        this.setCustRiderprefGender(ce.getCustRiderprefGender());
        this.setCustRiderprefSmoker(ce.getCustRiderprefIssmoker());

        // Driver Preferences
        this.setCustDriverprefAge(ce.getCustDriverprefAge());
        this.setCustDriverprefGender(ce.getCustDriverprefGender());
        this.setCustDriverprefSmoker(ce.getCustDriverprefIssmoker());
        //
        // Car Details Collection
        //
        this.setCarDetailsEntityCollection(ce.getCarDetailsEntityCollection());

        // Session ID
        //
        this.setCustSessionId(ce.getCustSessionId());

    } // update from DB

    /**
     * Superclass CustomerEntity has a name mismatch with the
     * custDriverPrefSmoke Property. Getter is called getDriverPrefIssmoker,
     * while Setter is calld setDriverPrefSmoker.
     *
     * Since we adopted the policy not to change the OpenRide interfaces until
     * the jORideFrontend is fully operable, we fix this by adding a wrapper.
     *
     * FIXME: rename methods in EJB as soon as the frontend is production ready.
     */
    public char getCustDriverprefSmoker() {

        try {
            return this.getCustDriverprefIssmoker();
        } catch (java.lang.NullPointerException exc) {
            return '?';
        }
    }

    /**
     * Superclass CustomerEntity has a name mismatch with the
     * custDriverPrefSmoke Property. Getter is called getDriverPrefIssmoker,
     * while Setter is calld setDriverPrefSmoker.
     *
     * Since we adopted the policy not to change the OpenRide interfaces until
     * the jORideFrontend is fully operable, we fix this by adding a wrapper.
     *
     * FIXME: rename methods in EJB as soon as the frontend is production ready.
     */
    public void setCustDriverprefSmoker(char arg) {

        super.setCustDriverprefSmoker(arg);
    }

    /**
     * Superclass CustomerEntity has a name mismatch with the custRiderPrefSmoke
     * Property. Getter is called getRiderPrefIssmoker, while Setter is calld
     * setRiderPrefSmoker.
     *
     * Since we adopted the policy not to change the OpenRide interfaces until
     * the jORideFrontend is fully operable, we fix this by adding a wrapper.
     *
     * FIXME: rename methods in EJB as soon as the frontend is production ready.
     */
    public char getCustRiderprefSmoker() {

        try {
            return this.getCustRiderprefIssmoker();
        } catch (java.lang.NullPointerException exc) {
            return '?';
        }
    }

    /**
     * Superclass CustomerEntity has a name mismatch with the custRiderPrefSmoke
     * Property. Getter is called getRiderPrefIssmoker, while Setter is calld
     * setRiderPrefSmoker.
     *
     * Since we adopted the policy not to change the OpenRide interfaces until
     * the jORideFrontend is fully operable, we fix this by adding a wrapper.
     *
     * FIXME: rename methods in EJB as soon as the frontend is production ready.
     */
    public void setCustRiderprefSmoker(char arg) {

        super.setCustRiderprefSmoker(arg);
    }

    /**
     * Returns the CarDetails of this
     */
    public Object[] getCarDetailsArray() {

        Collection con = this.getCarDetailsEntityCollection();

        if (con != null) {
            return con.toArray();
        }

        return new Object[0];

    }

    /**
     * String Wrapper for the (boolean) custIssmoker property
     *
     *
     * @return 's' aka smokerChar is custIssmoker==true, else 'n' aka
     * nonSmokerChar
     */
    public String getCustSmoker() {

        try {
            if (getCustIssmoker()) {
                return "" + this.getSmokerChar();
            }
        } catch (java.lang.NullPointerException exc) {
            return "" + this.getNonsmokerChar();
        }

        return "" + this.getNonsmokerChar();
    }

    /**
     * String Wrapper for the (boolean) custIssmoker property Sets the
     * custIssmoker property to 'true', if argument starts with the "smokerChar"
     * 's'. Sets the custIssmoker property to false else. '
     */
    public void setCustSmoker(String arg) {

        if (arg == null) {
            this.setCustIssmoker(Boolean.FALSE);
            return;
        }

        if (arg.length() == 0) {
            this.setCustIssmoker(Boolean.FALSE);
            return;
        }

        if (arg.startsWith("" + SMOKER_CHAR)) {
            this.setCustIssmoker(Boolean.TRUE);
            return;
        }

        this.setCustIssmoker(Boolean.FALSE);
    }

    /**
     * Update Data in the JCustomerEntityBean from Database
     */
    public void updatePersonalDataToDB() {
        new JCustomerEntityService().setCustomerPersonalData(this);
    } // update to  DB

    /**
     * Update Data in the JCustomerEntityBean from Database
     */
    public void updateDriverPreferencesToDB() {
        new JCustomerEntityService().setCustDriverPrefs(this);
    } // update to  DB

    /**
     * Update Data in the JCustomerEntityBean from Database
     */
    public void updateRiderPreferencesToDB() {
        new JCustomerEntityService().setCustRiderPrefs(this);
    } // update to  DB

    public String debugPrintout() {


        String res = "\n  ";
        res += "\n  == CustomerPersonalData ============================= ";
        res += "\n  == FirstName              : " + this.getCustFirstname();
        res += "\n  == LastName               : " + this.getCustLastname();
        res += "\n  == UserNickName           : " + this.getCustNickname();
        res += "\n  == CustDateofbirth        : " + this.getCustDateofbirth();
        res += "\n  == CustGender             : " + this.getCustGender();
        res += "\n  == CustGroup              : " + this.getCustGroup();
        res += "\n  == CustId                 : " + this.getCustId();
        res += "\n  == CustIssmoker           : " + this.getCustIssmoker();
        res += "\n  == CustLicensedate        : " + this.getCustLicensedate();
        res += "\n  ";
        res += "\n  == CustomerContactData";
        res += "\n  CustEmail                 : " + this.getCustEmail();
        res += "\n  CustFixedphoneno          : " + this.getCustFixedphoneno();
        res += "\n  CustMobilephoneno         : " + this.getCustMobilephoneno();
        res += "\n ";
        res += "\n  == CustomerAdress      ============================= ";
        res += "\n  CustAddrCity              : " + this.getCustAddrCity();
        res += "\n  CustAddrStreet            : " + this.getCustAddrStreet();
        res += "\n  CustAddrZipcode           : " + this.getCustAddrZipcode();
        res += "\n  ";
        res += "\n  == OpenRideSpecificData ============================= ";
        res += "\n  CustId                    : " + this.getCustId();
        res += "\n  CustPasswd                : " + this.getCustPasswd();
        res += "\n  CustPostident             : " + this.getCustPostident();
        res += "\n  CustPresencemssg          : " + this.getCustPresencemssg();
        res += "\n  CustProfilepic            : " + this.getCustProfilepic();
        res += "\n  CustRegistrdate           : " + this.getCustRegistrdate();
        res += "\n  ";
        res += "\n  == Banking Data are not used!  ============== ";
        res += "\n  CustAccountBalance        : " + this.getCustAccountBalance();
        res += "\n  CustBankAccount           : " + this.getCustBankAccount();
        res += "\n  CustBankCode              : " + this.getCustBankCode();
        res += "\n  ";


        //
        // Driver Preferences
        //
        res += "\n  CustDriverprefAge         : " + this.getCustDriverprefAge();
        res += "\n  CustDriverprefGender      : " + this.getCustRiderprefGender();
        res += "\n  CustDriverprefMusictaste  : " + this.getCustDriverprefMusictaste();
        res += "\n  CustDriverprefSmoker      : " + this.getCustDriverprefIssmoker();
        //
        // Rider Preferences
        //
        res += "\n  CustRiderprefAge          : " + this.getCustRiderprefAge();
        res += "\n  CustRiderprefGender       : " + this.getCustRiderprefGender();
        res += "\n  CustRiderprefMusictaste   : " + this.getCustRiderprefMusictaste();
        res += "\n  CustRiderprefSmoker       : " + this.getCustRiderprefIssmoker();
        //
        // Session ID
        //
        res += "\n  CustSessionId             : " + this.getCustSessionId();

        return res;

    }
    /**
     * Character to mark male gender in customer table
     */
    private static char GENDER_MALE = 'm';

    /**
     * Make constant availlable as JSF property.
     */
    public char getGenderMale() {
        return this.GENDER_MALE;
    }
    /**
     * Character to mark female gender in customer table
     */
    private static char GENDER_FEMALE = 'f';

    /**
     * Make constant availlable as JSF property.
     */
    public char getGenderFemale() {
        return this.GENDER_FEMALE;
    }
    /**
     * Character to mark in customer table the gender of people who do not want
     * to tell, or are neither male nor female
     */
    private static char GENDER_OTHER = '-';

    /**
     * Make constant availlable as JSF property.
     */
    public char getGenderOther() {
        return this.GENDER_OTHER;
    }

    /**
     * Display the gender on a label
     */
    public String getGenderLabel() {

        PropertiesLoader loader = new PropertiesLoader();

        if (this.getCustGender() == this.GENDER_MALE) {
            return loader.getMessagesProps().getProperty("custGenderMale");
        }

        if (this.getCustGender() == this.GENDER_FEMALE) {
            return loader.getMessagesProps().getProperty("custGenderFemale");
        }

        return loader.getMessagesProps().getProperty("custGenderOther");
    }

    
    
    /**
     * Invalidate the existing account
     *
     * Note, that this is protected by a
     *
     *
     * @return logout URL
     */
    public void invalidate(ActionEvent evt) {

        logger.info("Removing account with customerId: " + this.getCustId());

        // remove account
        
        new JCustomerEntityService().invalidateCustomerAccount();
        
        // logout user
        
        try {
  
            String logoutURLStr=new HTTPUser().getLogoutURL();
            FacesContext.getCurrentInstance().getExternalContext().redirect(logoutURLStr);
        } catch (IOException exc) {
            Logger.getLogger(JCustomerEntity.class.getName()).log(
                    Level.SEVERE, 
                    "Unexpected Error while logging out ", 
                    exc);
        }

   
    }
}// class

