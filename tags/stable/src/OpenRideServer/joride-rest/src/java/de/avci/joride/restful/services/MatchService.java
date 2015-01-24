package de.avci.joride.restful.services;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.avci.joride.restful.converters.MatchDTOConverter;
import de.avci.joride.restful.converters.RideRequestDTOConverter;
import de.avci.joride.restful.dto.matches.MatchDTO;
import de.avci.joride.restful.dto.requests.RideRequestDTO;
import de.fhg.fokus.openride.matching.MatchEntity;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideEntity;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;


/** Restful Service for manipulating ride offers
 * 
 * @author jochen
 *
 */


@Path("match")
@Produces("text/json")

public class MatchService extends AbstractRestService {
	
	
	
	private ObjectMapper jacksonMapper = new ObjectMapper();
	
	private MatchDTOConverter matchConverter=new MatchDTOConverter();
	
	
	
	/** Get a list of all ride Offers for respective user
	 * 
	 * @param request
	 * @return
	 */
	
	
	@GET
	@Path("findAll")
	public String listRideOffers(@Context HttpServletRequest request){
		
		
		List<MatchEntity> entities=this.lookupDriverUndertakesRideControllerBean().getAllMatches();
		
		LinkedList<MatchDTO>  res =new LinkedList <MatchDTO> ();
		
		// convert list of entities to list of DTOs
		for(MatchEntity entity:entities){
			res.add(matchConverter.matchDTO(entity));
		}
		
		// return list as json
		try { return jacksonMapper.writeValueAsString(res);
		} catch (JsonProcessingException exc) {
			throw new Error(exc);
		}
	}
	

}
