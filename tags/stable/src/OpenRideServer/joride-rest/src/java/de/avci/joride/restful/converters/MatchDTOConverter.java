package de.avci.joride.restful.converters;

import java.sql.Timestamp;
import java.util.Date;

import de.avci.joride.restful.dto.basic.LocationDTO;
import de.avci.joride.restful.dto.matches.MatchDTO;
import de.avci.joride.restful.dto.offers.RideOfferDTO;
import de.avci.joride.restful.dto.offers.RoutepointDTO;
import de.fhg.fokus.openride.matching.MatchEntity;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideEntity;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;

/**
 * Converts DriverUndertakesRideEntities to DriverundertakesRideEntities and
 * vice versa
 * 
 * @author jochen
 * 
 */
public class MatchDTOConverter {

	/**
	 * 
	 * @param entity
	 * @return
	 */
	public MatchDTO matchDTO(MatchEntity entity) {

		MatchDTO dto = new MatchDTO();

		//
		// TODO: what happens to the state?
		//
		// match.setState(entity.get);

		dto.setDriverState(entity.getDriverState());
		dto.setRiderState(entity.getRiderState());
		dto.setMatchExpectedStartTime(entity.getMatchExpectedStartTime());
		// why happen nullpointerexceptions here?
		dto.setRideRideRequestId(entity.getRiderUndertakesRideEntity()
				.getRiderrouteId());
		// why happen nullpointerexceptions here?
		dto.setRideOfferId(entity.getDriverUndertakesRideEntity().getRideId());
		dto.setRideRideRequestId(entity.getRiderUndertakesRideEntity()
				.getRiderrouteId());
		//
		if (entity.getDriverChange() != null) {dto.setDriverChange(new Timestamp(entity.getDriverChange().getTime()));}
		if (entity.getRiderChange() != null)  {dto.setRiderChange(new Timestamp(entity.getRiderChange().getTime()));}
		if (entity.getRiderAccess()  != null) {dto.setDriverAccess(new Timestamp(entity.getDriverAccess().getTime()));}
		if (entity.getDriverAccess() != null) {dto.setRiderAccess(new Timestamp(entity.getRiderAccess().getTime()));}
		//
		dto.setRiderMessage(entity.getRiderMessage());
		dto.setDriverMessage(entity.getDriverMessage());

		return dto;

	}

}
