package de.avci.joride.restful.services;

import java.io.IOException;
import java.sql.Date;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.avci.joride.restful.converters.LocationDTOConverter;
import de.avci.joride.restful.converters.RideRequestDTOConverter;
import de.avci.joride.restful.dto.offers.RideOfferDTO;
import de.avci.joride.restful.dto.requests.RideRequestDTO;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideEntity;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;


/** Restful Service for manipulating ride offers
 * 
 * @author jochen
 *
 */


@Path("request")
@Produces("text/json")

public class RideRequestService extends AbstractRestService {
	
	
	
	private ObjectMapper jacksonMapper = new ObjectMapper();
	
	private RideRequestDTOConverter requestConverter=new RideRequestDTOConverter();

	private LocationDTOConverter locationConverter=new LocationDTOConverter();
	
	
	/** Get a list of all ride Offers for respective user
	 * 
	 * @param request
	 * @return
	 */
	
	
	@GET
	@Path("findAll")
	public String listRideRequests(@Context HttpServletRequest request){
		
		
		LinkedList<RiderUndertakesRideEntity> entities=this.lookupRiderUndertakesRideControllerBean().getAllRides();
		
		LinkedList<RideRequestDTO>  res =new LinkedList <RideRequestDTO> ();
		
		// convert list of entities to list of DTOs
		for(RiderUndertakesRideEntity entity:entities){
			res.add(requestConverter.rideRequestDTO(entity));
		}
		
		// return list as json
		try { return jacksonMapper.writeValueAsString(res);
		} catch (JsonProcessingException exc) {
			throw new Error(exc);
		}
	}
	
	
	
	/**
	 * Get an individual request with given id
	 * 
	 * @param request
	 * @return
	 */

	@GET
	@Path("{id}")
	public String getRideRequest(@PathParam("id") String id) {

		try {
		Integer idInt=new Integer(id);
		RiderUndertakesRideEntity entity=this.lookupRiderUndertakesRideControllerBean().getRideByRiderRouteId(idInt);
		RideRequestDTO dto=requestConverter.rideRequestDTO(entity);
		// object as json
		return jacksonMapper.writeValueAsString(dto);
		} catch (JsonProcessingException exc) {
			throw new Error(exc);
		}
	}
	
	
	/** Create a new Ride Request from DTO
	 * 
	 * 
	 * @param request
	 * @return
	 */
	@POST	
	public Response addRequest(String json) {

		
		RideRequestDTO dto;
		try {
			dto = jacksonMapper.readValue(json, RideRequestDTO.class);
		} catch ( IOException e) {
			// TODO: log error!
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		RiderUndertakesRideControllerLocal rurcl = lookupRiderUndertakesRideControllerBean();
		
		int updateResult=
		rurcl.addRideRequest(
				//cust_id
				dto.getCustomerId(), 
				//starttime_earliest
				new Date(dto.getStartTimeEarliest().getTime()), 
				//starttimeLatest
				new Date(dto.getStartTimeLatest().getTime()), 
				//noPassengers
				dto.getNumberOfPassengers(), 
				//startpt
				locationConverter.point(dto.getStartLocation()), 
				// endpt
				locationConverter.point(dto.getEndLocation()), 
				// price
				dto.getPrice(),
				// comment
				dto.getComment(),
				// startpt address
				locationConverter.address(dto.getStartLocation()), 
				// endpt address
				locationConverter.address(dto.getEndLocation())
				);
		
		
		if (updateResult == -1) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.status(Response.Status.ACCEPTED).build();
	}
	
	
	
	
	/** 
	 *  Create a number of ride requests from list of  DTOs.
	 *  (Bulkadd)
	 *  This is used for performance test, when we do not want to
	 *  measure http overhead when adding multiple requests
	 *  
	 * 
	 * @param request
	 * @return
	 * 
	 * 
	 */
	@POST	
	@Path("bulkadd")
	public Response addRequests(String json) {

		
		RideRequestDTO[] dtos;
		try {
			dtos = jacksonMapper.readValue(json, RideRequestDTO[].class);
		} catch ( IOException e) {
			// TODO: log error!
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		RiderUndertakesRideControllerLocal rurcl = lookupRiderUndertakesRideControllerBean();
		
		// add dto's galore
		for(RideRequestDTO dto : dtos){
		
			int updateResult=
					rurcl.addRideRequest(
							//cust_id
				dto.getCustomerId(), 
				//starttime_earliest
				new Date(dto.getStartTimeEarliest().getTime()), 
				//starttimeLatest
				new Date(dto.getStartTimeLatest().getTime()), 
				//noPassengers
				dto.getNumberOfPassengers(), 
				//startpt
				locationConverter.point(dto.getStartLocation()), 
				// endpt
				locationConverter.point(dto.getEndLocation()), 
				// price
				dto.getPrice(),
				// comment
				dto.getComment(),
				// startpt address
				locationConverter.address(dto.getStartLocation()), 
				// endpt address
				locationConverter.address(dto.getEndLocation())
				);
		
		
			if (updateResult == -1) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		}
		
		return Response.status(Response.Status.ACCEPTED).build();
	}
	
	
	
	
	
	
	
	

}
