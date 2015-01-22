package de.avci.joride.restful.services;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.avci.joride.restful.converters.MessageDTOConverter;
import de.avci.joride.restful.dto.messages.MessageDTO;
import de.avci.openrideshare.messages.Message;
import de.avci.openrideshare.messages.MessageControllerLocal;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;

/**
 * Restful Service for manipulating messages
 * 
 * @author jochen
 * 
 */

@Path("messages")
@Produces("text/json")
public class MessageService extends AbstractRestService {

	private MessageDTOConverter converter = new MessageDTOConverter();

	private ObjectMapper jacksonMapper = new ObjectMapper();

	@GET
	@Path("findAll")
	public String listMessages(@Context HttpServletRequest request) {

		try {
			List<MessageDTO> resList = new LinkedList<MessageDTO>();

			List<Message> inList = this.lookupMessageControllerBean()
					.findAllMessages();

			for (Message m : inList) {
				resList.add(converter.messageDTO(m));
			}

			return jacksonMapper.writeValueAsString(resList);
		} catch (Exception exc) {
			throw new Error(exc);
		}

	}


	
	/**
	 * Create a new Message from DTO
	 * 
	 * TODO: Dangerous: sender and recipient 
	 * can happily be forged as long as 
	 * the rest frontend is not secured!
	 * 
	 * @param request
	 * @return
	 */
	
	/*
	 * currently disabled, as we can only create rider or driver messages
	 * 
	 * 
	@POST
	public Response addMessage(String json) {
		

		MessageDTO dto;

		try {
			dto = jacksonMapper.readValue(json, MessageDTO.class);
		} catch (IOException e) {
			// TODO: log error!
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		MessageControllerLocal mccl = lookupMessageControllerBean();
		
		
		CustomerEntity sender=null;
		
		if(dto.getSenderId()!=null){
			sender=this.lookupCustomerControllerBean().getCustomer(dto.getSenderId());
		}
		
		CustomerEntity recipient=null;
		
		if(dto.getRecipientId()!=null){
			recipient=this.lookupCustomerControllerBean().getCustomer(dto.getRecipientId());
		}
		
	
		boolean updateResult = mccl.createMessage(
				
				
				sender, 
				recipient,
				dto.getSubject(),
				dto.getMessage()
				
				
				
		);

		if (! updateResult ) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		return Response.status(Response.Status.ACCEPTED).build();
				
	}
	*/

}
       