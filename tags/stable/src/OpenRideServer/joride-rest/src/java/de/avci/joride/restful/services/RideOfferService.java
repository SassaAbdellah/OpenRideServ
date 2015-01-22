package de.avci.joride.restful.services;

import java.io.IOException;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.postgis.Point;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.avci.joride.restful.converters.LocationDTOConverter;
import de.avci.joride.restful.converters.RideOfferDTOConverter;
import de.avci.joride.restful.converters.WaypointDTOConverter;
import de.avci.joride.restful.dto.offers.RideOfferDTO;
import de.avci.joride.restful.dto.offers.WaypointDTO;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideControllerLocal;
import de.fhg.fokus.openride.rides.driver.DriverUndertakesRideEntity;
import de.fhg.fokus.openride.rides.driver.WaypointEntity;
import de.fhg.fokus.openride.rides.rider.RiderUndertakesRideEntity;

/**
 * Restful Service for manipulating ride offers
 * 
 * @author jochen
 * 
 */

@Path("offer")
@Produces("application/json")
@Consumes("application/json")
public class RideOfferService extends AbstractRestService {

	private Logger logger = Logger
			.getLogger(this.getClass().getCanonicalName());

	private ObjectMapper jacksonMapper = new ObjectMapper();

	private RideOfferDTOConverter offerConverter = new RideOfferDTOConverter();

	private LocationDTOConverter locationConverter = new LocationDTOConverter();

	/**
	 * Get a list of all ride Offers for respective user
	 * 
	 * @param request
	 * @return
	 */

	@GET
	@Path("findAll/")
	public String listRideOffers(@Context HttpServletRequest request) {

		List<DriverUndertakesRideEntity> entities = this
				.lookupDriverUndertakesRideControllerBean().getAllDrives();

		LinkedList<RideOfferDTO> res = new LinkedList<RideOfferDTO>();

		// convert list of entities to list of DTOs
		for (DriverUndertakesRideEntity entity : entities) {
			res.add(offerConverter.rideOfferDTO(entity));
		}

		// return list as json
		try {
			return jacksonMapper.writeValueAsString(res);
		} catch (JsonProcessingException exc) {
			throw new Error(exc);
		}
	}

	/**
	 * Get an individual offer with given id
	 * 
	 * @param request
	 * @return
	 */

	@GET
	@Path("{id}")
	public String getRideOffer(@PathParam("id") String id) {

		try {
			Integer idInt = new Integer(id);
			DriverUndertakesRideEntity entity = this
					.lookupDriverUndertakesRideControllerBean()
					.getDriveByDriveId(idInt);
			RideOfferDTO dto = offerConverter.rideOfferDTO(entity);
			// object as json
			return jacksonMapper.writeValueAsString(dto);
		} catch (JsonProcessingException exc) {
			throw new Error(exc);
		}
	}

	/**
	 * TODO: insecure! -- only here for doing bulk test! TODO: bad impl. Relies
	 * on now deprecated DriverUndertakesRideControllerBeanLocal.updateRide
	 * method
	 * 
	 * probably, this should be replace with something completel different.
	 * 
	 * @param json
	 *            JSON encoding a single RideOfferDTO Object.
	 * @return
	 */

	@POST
	public Response addOffer(String json) {

		DriverUndertakesRideControllerLocal durcl = lookupDriverUndertakesRideControllerBean();
		RideOfferDTO dto;
		try {
			dto = jacksonMapper.readValue(json, RideOfferDTO.class);
		} catch (IOException e) {

			// TODO: log error!
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		int updateResult = durcl.addRide(

		dto.getCustomerId().intValue(),
				locationConverter.point(dto.getStartLocation()),
				locationConverter.point(dto.getEndLocation()),
				// intermediate Points currently left out...
				new Point[0], // Point[] intermediate route
				null, // Have to know DriveId to add waypoints!
				new Date(dto.getStartTime().getTime()), dto.getComment(), null, // no
																				// acceptable
																				// Detour
																				// Minutes
				dto.getAcceptableDetourKM(), null, // no Acceptable Detour In
													// Percent(),
				dto.getOfferedSeatsNo(),
				// StringEscapeUtils.unescapeHtml(r.getStartptAddress())
				dto.getStartLocation().getAddress(),
				// StringEscapeUtils.unescapeHtml(r.getEndptAddress())))
				dto.getEndLocation().getAddress());

		if (updateResult == -1) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		// to add waypoints, we'll have to get hold of the rideId
		Integer rideId = updateResult;
		DriverUndertakesRideEntity ride = durcl.getDriveByDriveId(rideId);
		WaypointDTOConverter waypointDTOConverter = new WaypointDTOConverter();

		// add waypoints
		for (WaypointDTO wDTO : dto.getWayPoints()) {
			WaypointEntity wEntity = waypointDTOConverter.waypointEntity(wDTO,
					ride);
			durcl.addWaypoint(rideId, wEntity, wDTO.getRouteIdx());
		}

		return Response.status(Response.Status.ACCEPTED).build();

	}

	
	
	
	/** 
	 *  Create a number of ride offers from list of  DTOs.
	 *  (Bulkadd)
	 *  This is used for performance test, when we do not want to
	 *  measure http overhead when adding multiple requests
	 * 
	 * 
	 * @param request
	 * @return
	 * 
	 */

	@POST
	@Path("bulkadd")
	public Response addOffers(String json) {

		DriverUndertakesRideControllerLocal durcl = lookupDriverUndertakesRideControllerBean();
		RideOfferDTO[] dtos;
		try {
			dtos = jacksonMapper.readValue(json, RideOfferDTO[].class);
		} catch (IOException e) {

			// TODO: log error!
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		for (RideOfferDTO dto : dtos) {

			int updateResult = durcl.addRide(

					dto.getCustomerId().intValue(),
					locationConverter.point(dto.getStartLocation()),
					locationConverter.point(dto.getEndLocation()),
					// intermediate Points currently left out...
					new Point[0], // Point[] intermediate route
					null, // Have to know DriveId to add waypoints!
					new Date(dto.getStartTime().getTime()), dto.getComment(),
					null, // no acceptable Detour Minutes
					dto.getAcceptableDetourKM(), null, // no Acceptable Detour
														// In Percent(),
					dto.getOfferedSeatsNo(),
					// StringEscapeUtils.unescapeHtml(r.getStartptAddress())
					dto.getStartLocation().getAddress(),
					// StringEscapeUtils.unescapeHtml(r.getEndptAddress())))
					dto.getEndLocation().getAddress());

			if (updateResult == -1) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}

			// to add waypoints, we'll have to get hold of the rideId
			Integer rideId = updateResult;
			DriverUndertakesRideEntity ride = durcl.getDriveByDriveId(rideId);
			WaypointDTOConverter waypointDTOConverter = new WaypointDTOConverter();

			// add waypoints
			for (WaypointDTO wDTO : dto.getWayPoints()) {
				WaypointEntity wEntity = waypointDTOConverter.waypointEntity(
						wDTO, ride);
				durcl.addWaypoint(rideId, wEntity, wDTO.getRouteIdx());
			}

		}
		return Response.status(Response.Status.ACCEPTED).build();
	}

}
