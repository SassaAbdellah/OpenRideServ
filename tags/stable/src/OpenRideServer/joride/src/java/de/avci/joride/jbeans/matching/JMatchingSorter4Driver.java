package de.avci.joride.jbeans.matching;

import java.util.Comparator;

import de.fhg.fokus.openride.matching.MatchEntity;

/**
 * Sort Matchings from driver's view. That is:
 * 
 * 1) matches accepted by Driver 
 * 2) matches accepted by Rider
 * 3) The rest sorted by pickup Date
 * 
 * @author jochen
 * 
 */
public class JMatchingSorter4Driver implements Comparator<JMatchingEntity> {

	@Override
	public int compare(JMatchingEntity o1, JMatchingEntity o2) {

		
		// Driver State comparison comes first
		if (o1.getDriverState() ==  MatchEntity.ACCEPTED) {

			if(o2.getDriverState()==MatchEntity.ACCEPTED){
				return 0;
			} 
			
			return -1;
			
		} 

		if(o2.getDriverState()==MatchEntity.ACCEPTED){
			return 1;
		} // DriverState
		
		// Rider State comparison comes next
		if (o1.getRiderState() ==  MatchEntity.ACCEPTED) {

			if(o2.getRiderState()==MatchEntity.ACCEPTED){
				return 0;
			} 
			return -1;
		} 

		if(o2.getRiderState()==MatchEntity.ACCEPTED){
			return 1;
		}// RiderState
		
		// for the time beeing, sort the rest by pickup time

		long pickupTime1 = o1.getMatchEntity().getMatchExpectedStartTime()
				.getTime();
		long pickupTime2 = o2.getMatchEntity().getMatchExpectedStartTime()
				.getTime();

		if (pickupTime1 > pickupTime2) {
			return 1;
		} else {
			return -1;
		}
	}

}
