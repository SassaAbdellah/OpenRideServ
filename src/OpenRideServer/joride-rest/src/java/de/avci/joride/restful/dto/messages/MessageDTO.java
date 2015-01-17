package de.avci.joride.restful.dto.messages;

import java.sql.Timestamp;

import de.avci.joride.restful.dto.basic.AbstractDTO;


/** 
 * 
 * @author jochen
 *
 */
public class MessageDTO extends AbstractDTO {
	
	/** unique id
	 */
	private Integer messageId;
	
	/** Customer id of the sender. If null, then 
	 *  message was sent by system
	 * 
	 */
	private Integer senderId=null;
	
	/** Customer id of the recipient. If null, then 
	 *  message will be received by system
	 * 
	 */
	private Integer recipientId=null;
	
	/**  Timestamp when message was created
	 */
	private Timestamp timeStampCreated=null;
	

	/**  Timestamp when message was received
	 */
	private Timestamp timeStampReceived=null;
	
	/** Message/Payload
	 */
	private String message;

	/** Subject/Payload
	 */
	private String subject;

	
	
	public MessageDTO(){
		
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	public Integer getMessageId() {
		return messageId;
	}



	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}



	public Integer getSenderId() {
		return senderId;
	}



	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}



	public Integer getRecipientId() {
		return recipientId;
	}



	public void setRecipientId(Integer recipientId) {
		this.recipientId = recipientId;
	}



	public Timestamp getTimeStampCreated() {
		return timeStampCreated;
	}



	public void setTimeStampCreated(Timestamp timeStampCreated) {
		this.timeStampCreated = timeStampCreated;
	}



	public Timestamp getTimeStampReceived() {
		return timeStampReceived;
	}



	public void setTimeStampReceived(Timestamp timeStampReceived) {
		this.timeStampReceived = timeStampReceived;
	}



	public String getSubject() {
		return subject;
	}



	public void setSubject(String subject) {
		this.subject = subject;
	}
}
