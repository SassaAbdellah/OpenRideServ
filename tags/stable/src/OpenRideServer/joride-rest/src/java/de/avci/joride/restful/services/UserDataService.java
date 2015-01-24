package de.avci.joride.restful.services;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.avci.joride.restful.converters.CustomerDTOConverter;
import de.avci.joride.restful.converters.MatchDTOConverter;
import de.avci.joride.restful.dto.customers.CustomerDTO;
import de.avci.joride.restful.dto.matches.MatchDTO;
import de.avci.joride.restful.dto.offers.RideOfferDTO;
import de.avci.joride.restful.services.AbstractRestService;
import de.fhg.fokus.openride.customerprofile.CustomerControllerLocal;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.matching.MatchEntity;

/**
 * Restful service to query/add/delete CustomerEntity
 * 
 * @author jochen
 * 
 */

@Path("customer")
@Produces("text/json")
public class UserDataService extends AbstractRestService {

	Logger log=Logger.getLogger(this.getClass().getCanonicalName());
	
	private ObjectMapper jacksonMapper = new ObjectMapper();

	private CustomerDTOConverter customerConverter = new CustomerDTOConverter();

	/**
	 * Get a list of all customers
	 * 
	 * @param request
	 * @return
	 */

	@GET
	@Path("findAll")
	public String listCustomers(@Context HttpServletRequest request) {

		List<CustomerEntity> entities = this.lookupCustomerControllerBean()
				.getAllCustomers();

		LinkedList<CustomerDTO> res = new LinkedList<CustomerDTO>();

		// convert list of entities to list of DTOs
		for (CustomerEntity entity : entities) {
			res.add(customerConverter.customerDTO(entity));
		}

		// return list as json
		try {
			return jacksonMapper.writeValueAsString(res);
		} catch (JsonProcessingException exc) {
			throw new Error(exc);
		}
	}

	/**
	 * Get user with given id
	 * 
	 */
	@GET
	@Path("byId/{id}")
	public String getUserById(@PathParam("id") String id) {

		Integer custId = new Integer(id);

		CustomerEntity entity = this.lookupCustomerControllerBean()
				.getCustomer(custId);

		if (id == null) {
			return "null";
		}

		CustomerDTO dto = customerConverter.customerDTO(entity);
		try {
			return jacksonMapper.writeValueAsString(dto);
		} catch (JsonProcessingException exc) {
			// TODO: do something less insane here...
			throw new Error(exc);
		}
	}

	/**
	 * Get user with given username/nickname
	 * 
	 */
	@GET
	@Path("byName/{name}")
	public String getUserByName(@PathParam("name") String name) {

		CustomerEntity entity = this.lookupCustomerControllerBean()
				.getCustomerByNickname(name);

		if (name == null) {
			return "null";
		}

		CustomerDTO dto = customerConverter.customerDTO(entity);
		try {
			return jacksonMapper.writeValueAsString(dto);
		} catch (JsonProcessingException exc) {
			// TODO: do something less insane here...
			throw new Error(exc);
		}

	}
	
	
	/**
	 * Create new User
	 * 
	 */
	@POST
	
	public Response addCustomer(String json) {

		CustomerControllerLocal customerController = this.lookupCustomerControllerBean();
	
		CustomerDTO dto;
		
		try {
			dto = jacksonMapper.readValue(json, CustomerDTO.class);
		} catch (IOException exc) {
			log.log(Level.SEVERE,"Error while marshalling CustomerDTO", exc);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		
	
		try{
			
		int updateResult=	
		customerController.
			addCustomer(
					dto.getCustNickname(),
					dto.getCustPasswd(),
					dto.getCustFirstname(), 
					dto.getCustLastname(), 
					dto.getCustGender(),
					dto.getCustEmail(), 
					dto.getCustMobilephoneno(),
					dto.getCustPreferredLanguage()
				);
		
		if (updateResult == -1) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		return Response.status(Response.Status.ACCEPTED).build();
		
		} catch(Exception exc){
			log.log(Level.SEVERE,"Unexpected Error while adding user" ,exc);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			
		}
		
	}

}
