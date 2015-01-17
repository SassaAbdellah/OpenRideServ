package de.avci.joride.restful.services;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.avci.joride.restful.converters.CustomerDTOConverter;
import de.avci.joride.restful.converters.FavoritePointDTOConverter;
import de.avci.joride.restful.dto.favoritepoints.FavoritePointDTO;
import de.fhg.fokus.openride.customerprofile.CustomerControllerLocal;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;
import de.fhg.fokus.openride.customerprofile.FavoritePointControllerLocal;
import de.fhg.fokus.openride.customerprofile.FavoritePointEntity;

/**
 * Restful service to query/add/delete CustomerEntity
 * 
 * @author jochen
 * 
 */

@Path("favoritePoint")
@Produces("text/json")
public class FavoritePointService extends AbstractRestService {

	Logger log=Logger.getLogger(this.getClass().getCanonicalName());
	
	private ObjectMapper jacksonMapper = new ObjectMapper();

	private CustomerDTOConverter customerConverter = new CustomerDTOConverter();

	
	
	
	@GET
	@Path("byCustomerId/{customerId}")
	
	public String findByCustomerId( @PathParam("customerId") Integer customerId){
		
	
		/* See, if we can find a customer for id
		 */
		
		CustomerControllerLocal customerController=this.lookupCustomerControllerBean();;
		CustomerEntity customerEntity=customerController.getCustomer(customerId);
	
		
		
		/** No use going on when there is no such customer
		 */
		
		
		if(customerEntity==null){
			log.log(Level.SEVERE,"Cannot find favoritePoints for customer, no customer for id : "+customerId);
			return null;
		}
		
		FavoritePointControllerLocal favoritePointController = this.lookupFavoritePointControllerBean();
		List <FavoritePointEntity> entities=favoritePointController.getFavoritePointsByCustomer(customerEntity);
		
		FavoritePointDTOConverter converter=new FavoritePointDTOConverter();
		List <FavoritePointDTO> resList=new LinkedList<FavoritePointDTO> ();
		
		for(FavoritePointEntity entity: entities){
			resList.add(converter.favoritePointDTO(entity));
		}
		
		try { return jacksonMapper.writeValueAsString(resList);
		} catch (JsonProcessingException exc) {
			log.log(Level.SEVERE,"Error while marshalling favoritePoints to JSOM ", exc);
			return null;
		}
	}
	
	
	
	
	/**
	 * Create new favorite point
	 * 
	 */
	@POST
	
	public Response addFavoritePoint(String json) {

		FavoritePointControllerLocal favoritePointController = this.lookupFavoritePointControllerBean();
	
		FavoritePointDTO dto;
		
		try {
			dto = jacksonMapper.readValue(json, FavoritePointDTO.class);
		} catch (IOException exc) {
			log.log(Level.SEVERE,"Error while marshalling FavoritePointDTO", exc);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		
		/* See, if we can find a customer ID
		 */
		CustomerControllerLocal customerController=this.lookupCustomerControllerBean();;
		CustomerEntity customerEntity=customerController.getCustomer(dto.getCustId());
		
		/** No use going on when there is no such customer
		 */
		if(customerEntity==null){
			log.log(Level.SEVERE,"Cannot add favoritePoint, no customer for id : "+dto.getCustId());
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
	
		try{
			
		int updateResult=	
		favoritePointController.addFavoritePoint(
				dto.getAddress(), 
				dto.calculateFavpointString(), 
				dto.getName(), 
				customerEntity
				);
		
		
		if (updateResult == -1) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		return Response.status(Response.Status.ACCEPTED).build();
		
		} catch(Exception exc){
			log.log(Level.SEVERE,"Unexpected Error while adding favoritePoint" ,exc);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			
		}
	}

}
