package de.avci.joride.restful.dto.favoritepoints;

import de.avci.joride.restful.dto.basic.LocationDTO;


/** DTO for a customer's favoritepoint
 * 
 * @author jochen
 *
 */
public class FavoritePointDTO extends LocationDTO {

	/** Favoritepoints are attached customers via custIds
	 */	
	private Integer custId;
	
	/** Dumb defaut constructor
	 */
	public FavoritePointDTO(){
		super();
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	/** Storing a "Point" as a string instead as of a postgres point type
	 *  enforces conversion methods here.
	 *  This should be removed as soon as FavoritePointEntity
	 *  is straightened out, as it is a step away from propert DTOs
	 * 
	 * 
	 * @return
	 * 
	 * @deprecated remove as soon as the favoint property has been converted to Point
	 */
	public String calculateFavpointString() {
		return ""+this.getLon()+","+this.getLat();
	}
	
	
}
