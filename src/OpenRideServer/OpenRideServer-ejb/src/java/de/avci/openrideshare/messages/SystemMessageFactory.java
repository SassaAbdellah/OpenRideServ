package de.avci.openrideshare.messages;

import java.util.Date;
import java.util.Locale;

import de.avci.openrideshare.utils.PropertiesLoader;
import de.fhg.fokus.openride.matching.MatchEntity;

/**
 * A factory to create System messages
 * 
 * @author jochen
 * 
 */
public class SystemMessageFactory {
	
	
	

	
	

	/**
	 * Create a list of matches reacting to either driver or rider accepting a
	 * reuest. That is: If driver has accepted a match, and rider has not yet
	 * accepted, then message is sent do rider If rider has accepted a match,
	 * and driver has not yet accepted, then message is sent to driver.
	 * 
	 * Finally, if both have accepted, then confirmation message is sent to
	 * both.
	 * 
	 * If none of the above cases is true (for ex, if match has already been
	 * rejected, no message is sent)
	 * 
	 * 
	 * 
	 * @return
	 */
	public static Message[] createMessagesOnAcceptance(MatchEntity m) {

		// send message to rider...
		if (m.getRiderState() == MatchEntity.NOT_ADAPTED
				&& m.getDriverState() == MatchEntity.ACCEPTED) {
			Message msgr = SystemMessageFactory
					.createSystemMessageDriverAcceptedNotification(m);
			Message res[] = new Message[1];
			res[0] = msgr;
			return res;
		}

		// send message to driver...
		if (m.getRiderState() == MatchEntity.ACCEPTED
				&& m.getDriverState() == MatchEntity.NOT_ADAPTED) {
			Message res[] = new Message[1];
			res[0] = SystemMessageFactory
					.createSystemMessageRiderAcceptedNotification(m);
			return res;
		}

		// send messages to both...
		if (m.getRiderState() == MatchEntity.ACCEPTED
				&& m.getDriverState() == MatchEntity.ACCEPTED) {
			Message res[] = new Message[2];
			res[0] = createSystemMessageRiderBothAcceptedNotification(m);
			res[1] = createSystemMessageDriverBothAcceptedNotification(m);
			return res;
		}

		// Default...
		return new Message[0];
	}
	
	
	/** Create a set of two messages when a new Match is created.
	 *  One of these messages will inform the driver,
	 *  the other of these messages will inform the rider.
	 * 
	 * 
	 * @return
	 */
	public static Message[] createMessagesOnNewMatch(MatchEntity m) {
	
		Message[] res=new Message[2];
		res[0]=createSystemMessageDriverNewMatch(m);
		res[1]=createSystemMessageRiderNewMatch(m);
		return res;
	}
	
	
	
	/** Create a set of two messages when a new Match is countermanded.
	 *  One of these messages will inform the driver,
	 *  the other of these messages will inform the rider.
	 * 
	 * 
	 * @return
	 */
	public static Message[] createMessagesOnCountermand(MatchEntity m) {
	
		Message[] res=new Message[2];
		res[0]=createSystemMessageDriverCountermandedNotification(m);
		res[1]=createSystemMessageRiderCountermandedNotification(m);
		return res;
	}
	

	/**
	 * Message telling the passenger that there is a new Match matching his
	 * request
	 * 
	 * @param m
	 * 
	 * @return
	 */
	private static Message createSystemMessageRiderNewMatch(MatchEntity m) {
		
		Message res = new Message();
		res.setSender(null); // System message!
		res.setTimeStampCreated(new Date(System.currentTimeMillis()));
		res.setSubject(PropertiesLoader.getMessageProperties(getRiderPrefLocale(m)).getProperty("message_onNewMatchSubject4Rider"));
		res.setMessage(PropertiesLoader.getMessageProperties(getRiderPrefLocale(m)).getProperty("message_onNewMatchMessage4Rider"));
		res.setRecipient(m.getRiderUndertakesRideEntity().getCustId());
		res.setRequest(m.getRiderUndertakesRideEntity());
		res.setOffer(m.getDriverUndertakesRideEntity());

		return res;
	}

	/**
	 * Message telling the driver that there is a new Match matching his offer
	 * 
	 * @param m
	 * 
	 * @return
	 */

	private static Message createSystemMessageDriverNewMatch(MatchEntity m) {

		Message res = new Message();
		res.setSender(null); // System message!
		res.setTimeStampCreated(new Date(System.currentTimeMillis()));
		res.setSubject(PropertiesLoader.getMessageProperties(getDriverPrefLocale(m)).getProperty("message_onNewMatchSubject4Driver"));
		res.setMessage(PropertiesLoader.getMessageProperties(getDriverPrefLocale(m)).getProperty("message_onNewMatchMessage4Driver"));
		res.setRecipient(m.getDriverUndertakesRideEntity().getCustId());
		res.setRequest(m.getRiderUndertakesRideEntity());
		res.setOffer(m.getDriverUndertakesRideEntity());

		return res;
	}

	/**
	 * Message telling the passenger that a driver accepted his request
	 * 
	 * @param m
	 * 
	 * @return
	 */
	private static Message createSystemMessageDriverAcceptedNotification(
			MatchEntity m) {

		Message res = new Message();
		res.setSender(null); // System message!
		res.setTimeStampCreated(new Date(System.currentTimeMillis()));
		res.setSubject(PropertiesLoader.getMessageProperties(getRiderPrefLocale(m)).getProperty("message_onAcceptanceSubject4Rider"));
		res.setMessage(PropertiesLoader.getMessageProperties(getRiderPrefLocale(m)).getProperty("message_onNewMatchMessage4Rider"));
		res.setRecipient(m.getRiderUndertakesRideEntity().getCustId());
		res.setRequest(m.getRiderUndertakesRideEntity());
		res.setOffer(m.getDriverUndertakesRideEntity());

		return res;
	}

	/**
	 * Message telling the driver that a potential passenger accepted his offer
	 * 
	 * 
	 * 
	 * @param m
	 * 
	 * @return
	 */

	private static Message createSystemMessageRiderAcceptedNotification(
			MatchEntity m) {

		Message res = new Message();
		res.setSender(null); // System message!
		res.setTimeStampCreated(new Date(System.currentTimeMillis()));
		res.setSubject(PropertiesLoader.getMessageProperties(getDriverPrefLocale(m)).getProperty("message_onAcceptanceSubject4Driver"));
		res.setMessage(PropertiesLoader.getMessageProperties(getDriverPrefLocale(m)).getProperty("message_onAcceptanceMessage4Driver"));
		res.setRecipient(m.getDriverUndertakesRideEntity().getCustId());
		res.setRequest(m.getRiderUndertakesRideEntity());
		res.setOffer(m.getDriverUndertakesRideEntity());

		return res;
	}

	/**
	 * Message telling the passenger that both parties accepted a request
	 * 
	 * @param m
	 * 
	 * @return
	 */
	private static Message createSystemMessageRiderBothAcceptedNotification(
			MatchEntity m) {

		Message res = new Message();
		res.setSender(null); // System message!
		res.setTimeStampCreated(new Date(System.currentTimeMillis()));
		res.setSubject(PropertiesLoader.getMessageProperties(getRiderPrefLocale(m)).getProperty("message_onAcceptanceBothSubject4Rider"));
		res.setMessage(PropertiesLoader.getMessageProperties(getRiderPrefLocale(m)).getProperty("message_onAcceptanceBothMessage4Rider"));
		res.setRecipient(m.getRiderUndertakesRideEntity().getCustId());
		res.setRequest(m.getRiderUndertakesRideEntity());
		res.setOffer(m.getDriverUndertakesRideEntity());

		return res;
	}

	/**
	 * Message telling the driver that both parties agreed on his offer
	 * 
	 * @param m
	 * 
	 * @return
	 */
	private static Message createSystemMessageDriverBothAcceptedNotification(
			MatchEntity m) {

		Message res = new Message();
		res.setSender(null); // System message!
		res.setTimeStampCreated(new Date(System.currentTimeMillis()));
		res.setSubject(PropertiesLoader.getMessageProperties(getDriverPrefLocale(m)).getProperty("message_onAcceptanceBothSubject4Driver"));
		res.setMessage(PropertiesLoader.getMessageProperties(getDriverPrefLocale(m)).getProperty("message_onAcceptanceBothMessage4Driver"));
		res.setRecipient(m.getDriverUndertakesRideEntity().getCustId());
		res.setRequest(m.getRiderUndertakesRideEntity());
		res.setOffer(m.getDriverUndertakesRideEntity());

		return res;
	}

	/**
	 * Message telling the  driver  a  ride has been countermanded
	 * 
	 * @param m
	 * 
	 * @return
	 */
	private static Message createSystemMessageDriverCountermandedNotification(
			MatchEntity m) {

		Message res = new Message();
		res.setSender(null); // System message!
		res.setTimeStampCreated(new Date(System.currentTimeMillis()));
		res.setSubject(PropertiesLoader.getMessageProperties(getDriverPrefLocale(m)).getProperty("message_onCountermandSubject4Rider"));
		res.setMessage(PropertiesLoader.getMessageProperties(getDriverPrefLocale(m)).getProperty("message_onCountermandMessage4Rider"));
		res.setRecipient(m.getDriverUndertakesRideEntity().getCustId());
		res.setRequest(m.getRiderUndertakesRideEntity());
		res.setOffer(m.getDriverUndertakesRideEntity());

		return res;
	}
	
	

	
	

	/**
	 * Message telling the rider that a ride has been countermanded
	 * 
	 * @param m
	 * 
	 * @return
	 */

	private static Message createSystemMessageRiderCountermandedNotification(
			MatchEntity m) {

		Message res = new Message();
		res.setSender(null); // System message!
		res.setTimeStampCreated(new Date(System.currentTimeMillis()));
		res.setSubject(PropertiesLoader.getMessageProperties(getRiderPrefLocale(m)).getProperty("message_onCountermandSubject4Rider"));
		res.setMessage(PropertiesLoader.getMessageProperties(getRiderPrefLocale(m)).getProperty("message_onCountermandMessage4Rider"));
		res.setRecipient(m.getRiderUndertakesRideEntity().getCustId());
		res.setRequest(m.getRiderUndertakesRideEntity());
		res.setOffer(m.getDriverUndertakesRideEntity());

		return res;
	}
	
	
	/** Return the Locale preferred by the matche's rider.
	 * 
	 * @param m
	 * @return
	 */
	public static Locale getRiderPrefLocale(MatchEntity m){
	
		String preferredRiderLanguageStr=m.getRiderUndertakesRideEntity().getCustId().getPreferredLanguage();
		if(preferredRiderLanguageStr!=null){	
			Locale riderPreferredLocale=Locale.forLanguageTag(preferredRiderLanguageStr);
			if (riderPreferredLocale!=null){
				return riderPreferredLocale;
			}
		}
		return Locale.getDefault();
	}
	
	/** Return the Locale preferred by the matche's driver.
	 * 
	 * @param m
	 * @return
	 */
	public static Locale getDriverPrefLocale(MatchEntity m){
	
		String preferredDriverLanguageStr=m.getDriverUndertakesRideEntity().getCustId().getPreferredLanguage();
		if(preferredDriverLanguageStr!=null){	
			Locale driverPreferredLocale=Locale.forLanguageTag(preferredDriverLanguageStr);
			if (driverPreferredLocale!=null){
				return driverPreferredLocale;
			}
		}
		return Locale.getDefault();
	}
	
	
	

}
