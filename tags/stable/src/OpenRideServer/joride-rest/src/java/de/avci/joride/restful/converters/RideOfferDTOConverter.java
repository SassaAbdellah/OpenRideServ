package de.avci.joride.restful.converters;

import java.sql.Timestamp;

import de.avci.joride.restful.dto.basic.LocationDTO;
import de.avci.joride.restful.dto.offers.RideOfferDTO;
import de.avci.joride.restful.dto.offers.RoutepointDTO;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideEntity;

/**
 * Converts DriverUndertakesRideEntities to DriverundertakesRideEntities and
 * vice versa
 * 
 * @author jochen
 * 
 */
public class RideOfferDTOConverter {

	private LocationDTOConverter locationConverter = new LocationDTOConverter();
	private WaypointDTOConverter waypointDTOConverter = new WaypointDTOConverter();
	private RoutepointDTOConverter routepointDTOConverter = new RoutepointDTOConverter();

	/**
	 * 
	 * @param entity
	 * @return
	 */
	public RideOfferDTO rideOfferDTO(DriverUndertakesRideEntity entity) {

		RideOfferDTO dto = new RideOfferDTO();

		if (entity != null) {

			// header data
			dto.setId(entity.getRideId());
			dto.setCustomerId(entity.getCustId().getCustId());
			dto.setComment(entity.getRideComment());
			dto.setStartTime(new Timestamp(entity.getRideStarttime().getTime()));
			dto.setOfferedSeatsNo(entity.getRideOfferedseatsNo());
			dto.setAcceptableDetourKM(entity.getRideAcceptableDetourInKm());
			//
			// Startpoint
			LocationDTO startLocation = locationConverter.locationDTO(
					entity.getRideStartpt(), entity.getStartptAddress());
			dto.setStartLocation(startLocation);
			//
			// Endpoint
			LocationDTO endLocation = locationConverter.locationDTO(
					entity.getRideEndpt(), entity.getEndptAddress());
			dto.setEndLocation(endLocation);
			//
			// waypoints
			dto.setWayPoints(waypointDTOConverter.waypointDTOList(entity
					.getWaypoints()));
			//
		} // entity != null
		return dto;

	}

}
