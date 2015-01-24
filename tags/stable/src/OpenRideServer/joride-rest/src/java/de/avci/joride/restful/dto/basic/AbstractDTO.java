package de.avci.joride.restful.dto.basic;

/** Abstract Class for all DTOs
 * 
 * @author jochen
 *
 */
public abstract class AbstractDTO {
	
	/** Every DTO should have a nonfunctional Id
	 */
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	
	/** Bean constructor
	 */
	
	public AbstractDTO(){}
}
