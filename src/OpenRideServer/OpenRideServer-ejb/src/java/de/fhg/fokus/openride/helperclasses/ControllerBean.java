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
package de.fhg.fokus.openride.helperclasses;

import de.fhg.fokus.openride.customerprofile.CustomerControllerBean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author pab
 */
public abstract class ControllerBean {

    public Logger logger = Logger.getLogger(this.getClass().toString());
    @PersistenceContext
    EntityManager em;
    @Temporal(TemporalType.TIMESTAMP)
    /**
     * TODO: Ugly code.... do something about that, make it a property, or give
     * it another name. Also, user transaction "u" is frequently overwritten in
     * subclasses. Remove any of those usecases.
     *
     */
    UserTransaction u;

    /**
     * This function can be called to print log messages.
     *
     * @param c
     * @param message
     */
    public void log(Class c, String message) {
        logger.log(Level.INFO, c.getName() + " " + message);
    }

    /**
     * This method checks whether an Entitymanager has been generated. And
     * generates it if not. It also generates a Usertransaction to commit
     * changes to the DB.
     */
    public void checkEntityManager() {
        if (em == null) {
            try {
                Context ctx = new InitialContext();
                u = (UserTransaction) ctx.lookup("java:comp/UserTransaction");
                em = javax.persistence.Persistence.createEntityManagerFactory("OpenRideServer-ejbPU").createEntityManager();
            } catch (NamingException ex) {
                Logger.getLogger(CustomerControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This method is used to generate an entitymanager and a valid transaction
     * to use the database connection. And it starts the transaction already.
     */
    public void startUserTransaction() {
        checkEntityManager();
        if (u != null) {
            try {
                u.begin();
            } catch (SystemException ex) {
                Logger.getLogger(CustomerControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (javax.transaction.NotSupportedException ex) {
                Logger.getLogger(ControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This method ends a Usertransaction an deletes the Entitymanager.
     *
     *
     */
    public void commitUserTransaction() {
        if (u != null) {
            try {
                u.commit();
                System.out.println("Commit");
                em.close();
                em = null;
            } catch (HeuristicMixedException ex) {
                Logger.getLogger(CustomerControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicRollbackException ex) {
                Logger.getLogger(CustomerControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(CustomerControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(CustomerControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(CustomerControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (javax.transaction.RollbackException ex) {
                Logger.getLogger(ControllerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /** This method rolls back a Usertransaction
     * 
     */
    public void rollbackUserTransaction() {
        if (u != null) {
            try {
                u.rollback();
            } catch (IllegalStateException ex) {
                Logger.getLogger(ControllerBean.class.getName()).log(Level.SEVERE, "Illegal State Exception while rolling back", ex);
            } catch (SecurityException ex) {
                Logger.getLogger(ControllerBean.class.getName()).log(Level.SEVERE, "Securtity Exception while rolling back", ex);
            } catch (SystemException ex) {
                Logger.getLogger(ControllerBean.class.getName()).log(Level.SEVERE, "System Exception while rolling back", ex);
            }
            System.out.println("rollback");

        }
    }
}
