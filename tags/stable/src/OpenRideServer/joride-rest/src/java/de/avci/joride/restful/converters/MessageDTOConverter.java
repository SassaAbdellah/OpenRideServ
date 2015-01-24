package de.avci.joride.restful.converters;

import de.avci.joride.restful.dto.messages.MessageDTO;
import de.avci.openrideshare.messages.Message;


/** Turns ORS Message entities into messageDTOs
 * 
 * @author jochen
 *
 */
public class MessageDTOConverter {

	
	public MessageDTO messageDTO(Message m){
		
		MessageDTO res=new MessageDTO();
		
		res.setMessageId(m.getMessageId());
		res.setSubject(m.getSubject());
		res.setMessage(m.getMessage());
		if(m.getRecipient()!=null){
			res.setRecipientId(m.getRecipient().getCustId());
		}
		if(m.getSender()!=null){
			res.setSenderId(m.getSender().getCustId());
		}
		return res;
		
	}

}
