/**
 * Service to get and Put CustomerEntityBeans the EJB Way.
 *
 */
package de.avci.joride.jbeans.favoritepoints;

import de.avci.joride.jbeans.customerprofile.JCustomerEntityService;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.customerprofile.FavoritePointControllerLocal;
import de.fhg.fokus.openride.customerprofile.FavoritePointEntity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Service to get and put JFavoritePointEntityBeans to the System.
 *
 *
 *
 * @author jochen
 *
 */
public class JFavoritePointsService {

   transient Logger log = Logger.getLogger(this.getClass().getCanonicalName());
    
    FavoritePointControllerLocal favouritePointControllerLocal = lookupFavoritePointControllerLocal();

    private FavoritePointControllerLocal lookupFavoritePointControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (FavoritePointControllerLocal) c.lookup("java:global/OpenRideServer/OpenRideServer-ejb/FavoritePointControllerBean!de.fhg.fokus.openride.customerprofile.FavoritePointControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    /**
     * Get a customerEntity from the current request
     *
     * @return
     */
    public CustomerEntity getCustomerEntity() {
        return (new JCustomerEntityService()).getCustomerEntitySafely();
    }

    /**
     * Returns the list of favourite points for the current customer. Current
     * customer is determined safely from HTTPRequest's Auth Principal
     *
     *
     * @return list
     */
    public List<JFavoritePointEntity> getFavoritePointList() {

        CustomerEntity ce = this.getCustomerEntity();

        FavoritePointControllerLocal fpc = this.lookupFavoritePointControllerLocal();

        List<FavoritePointEntity> fpl = fpc.getFavoritePointsByCustomer(ce);


        List<JFavoritePointEntity> jfpl = new ArrayList<JFavoritePointEntity>();

        Iterator<FavoritePointEntity> it = fpl.iterator();

        while (it.hasNext()) {
            jfpl.add(new JFavoritePointEntity(it.next()));
        }

        return jfpl;


    }

    /**
     * Add a new favorite Point. The new favorite point is attributed to the
     * current user determined from the HTTPAuthPrincipal.
     *
     * @param jfpe
     */
    public void addFavoritePoint(JFavoritePointEntity jfpe) {


        CustomerEntity ce = this.getCustomerEntity();

        FavoritePointControllerLocal fpc = this.lookupFavoritePointControllerLocal();

        fpc.addFavoritePoint(
                jfpe.getFavptAddress(),
                jfpe.getFavptPoint(),
                jfpe.getFavptDisplayname(),
                ce);

    }

    /**
     * Savely delete the favorite point given as argument. "Savely delete"
     * means, that the owner of the Favpoint is checked against the caller
     * determined from HTTPAuthPrincipal. If owner and AuthPrincipal do not
     * match, an Error is thrown.
     *
     *
     * @param favpointId
     */
    public void deleteFavoritePointSavely(int favpointId) {


        // try to get the favpoint Id from database
        // if we can *safely* get it, we can be sure that 
        // we can *safely* delete it.

        this.getFavoritePointEntitySafely(favpointId);


        FavoritePointControllerLocal fpc = this.lookupFavoritePointControllerLocal();
        fpc.removeFavoritePoint(favpointId);

    }

    /**
     * Savely retrieve the FavoritePoint from the Database.
     *
     * "Savelymeans, that the owner of the Favpoint is checked against the
     * caller determined from HTTPAuthPrincipal. If owner and AuthPrincipal do
     * not match, an Error is thrown.
     *
     * @param favPointId
     * @return
     */
    public JFavoritePointEntity getFavoritePointEntitySafely(int favpointId) {


        CustomerEntity ce = this.getCustomerEntity();

        if (ce == null) {
            throw new Error("Cannot determine HTTPAuthPrincipal when removing FavPoint");
        }

        FavoritePointControllerLocal fpc = this.lookupFavoritePointControllerLocal();


        FavoritePointEntity fpe = fpc.getFavoritePoint(favpointId);

        // nothing to do!
        if (fpe == null) {
            return null;
        }

        if (fpe.getCustId().getCustId() != ce.getCustId()) {
            throw new Error("Refusing to return Favpoints, Customer ID does not match HTTPAuthPrincipal");
        }


        return new JFavoritePointEntity(fpe);

    }
}
