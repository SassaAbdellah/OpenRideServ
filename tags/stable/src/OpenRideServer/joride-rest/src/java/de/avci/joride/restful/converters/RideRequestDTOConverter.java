package de.avci.joride.restful.converters;

import java.sql.Timestamp;

import de.avci.joride.restful.dto.basic.LocationDTO;
import de.avci.joride.restful.dto.offers.RideOfferDTO;
import de.avci.joride.restful.dto.requests.RideRequestDTO;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideControllerBean;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;

public class RideRequestDTOConverter {
	
	
	LocationDTOConverter locationDTOConverter=new LocationDTOConverter();
	
	public RideRequestDTO rideRequestDTO(RiderUndertakesRideEntity entity){
		
		RideRequestDTO res=new RideRequestDTO();
		
		
		
		res.setId(entity.getRiderrouteId());
		res.setCustomerId(entity.getCustId().getCustId());
		res.setNumberOfPassengers(entity.getNoPassengers());
		res.setPrice(entity.getPrice());
		res.setComment(entity.getComment());
		res.setStartTimeEarliest(new Timestamp(entity.getStarttimeEarliest().getTime()));
		res.setStartTimeLatest(new Timestamp(entity.getStarttimeLatest().getTime()));
		
		// StartLocation
		LocationDTO startDTO=locationDTOConverter.locationDTO(
				entity.getStartpt(), 
				entity.getStartptAddress()
				);
		
		res.setStartLocation(startDTO);
		//
		LocationDTO endDTO=locationDTOConverter.locationDTO(
				entity.getEndpt(), 
				entity.getEndptAddress()
				);		
		res.setEndLocation(endDTO);
		//		
		return res;
	}

}
