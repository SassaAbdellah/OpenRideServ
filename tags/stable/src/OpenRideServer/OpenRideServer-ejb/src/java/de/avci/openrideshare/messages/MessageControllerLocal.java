/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.avci.openrideshare.messages;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.matching.MatchEntity;

/**
 *
 * @author jochen
 */
@Local
public interface MessageControllerLocal {
	


	/** Create a message from Rider to driver of a given match
	 * 
	 * @param match
	 * @param subject
	 * @param message
	 * @return
	 */
    public boolean createRiderMessageFromMatch(
            MatchEntity match,
            String subject,
            String message);
    
	/** Create a message from Driver to Rider of a given match
	 * 
	 * @param match
	 * @param subject
	 * @param message
	 * @return
	 */
    public boolean createDriverMessageFromMatch(
            MatchEntity match,
            String subject,
            String message);

    
    
    /** Send a message from the system
     * 
     * @param recipient 
     * @param subject
     * @param message
     * @return  true if message had been sucessfully sent, else false
     */
    public boolean createSystemMessage(
            CustomerEntity recipient,
            String subject,
            String message);


    /** All messages. This should only be used when debugging.
     * 
     * @return List of all messages.
     */
	public List<Message> findAllMessages();


	/** All unread messages for given customer
	 * 
	 * @param ce
	 * @return
	 */
	public List<Message> findUnreadMessages(CustomerEntity ce);


	/** Return the number of unread messages 
	 * (which, for performance reasons, should have a ejb query of it's own instead 
	 * of just counting the number of elements in a list...
	 * 
	 * 
	 * @param ce
	 * @return
	 */
	public long getNumberOfUnreadMessages(CustomerEntity ce);


	/** True, if there are unread messages for this user, else false.
	 */
	boolean hasUnreadMessages(CustomerEntity ce);

	
	/** Set message with given id to read (if it exists)
	 * 
	 * @param messageId
	 */
	public void setRead(Integer messageId);

	
	/** find single message by id
	 * 
	 * @param id
	 * @return
	 */
	public Message getMessageById(Integer id);
	
	/** Find all messages for given user in given interval.
	 *  (i.e: all messages for which user is either sender or recipient)
	 * 
	 * @param ce          CustomerEntity/User
	 * @param startDate   Start of Interval
	 * @param endDate     End of Interval
	 * @return            
	 */
	public List <Message> findMessagesForUserInInterval(CustomerEntity ce, Date startDate, Date endDate); 



	/** Return a list of messsages refering match given by rideId (offer) and riderrouteId (request)
	 * 
	 * @param rideId
	 * @param riderrouteId
	 * 
	 * @return list of all messages refering match with given rideId (offer) and riderrouteId (request)
	 */
	public List <Message> findMessagesForMatch(Integer rideId, Integer riderrouteId);


	
	/** Create a set of messages for rider and driver,
	 *  telling them that a match has been countermanded. 
	 * 
	 * 
	 * @param m
	 */
	public void createMessageOnCountermand(MatchEntity m );
	
	
	/** React to one party accepting a match with creating 
	 *  and sending the appropriate messages.
	 */
	public void createMessagesOnAcceptance(MatchEntity m);

	
	/** React to one party accepting a match with creating 
	 *  and sending the appropriate messages.
	 */
	public void createMessagesOnNewMatch(MatchEntity m);

	
	
	
	
	
}
