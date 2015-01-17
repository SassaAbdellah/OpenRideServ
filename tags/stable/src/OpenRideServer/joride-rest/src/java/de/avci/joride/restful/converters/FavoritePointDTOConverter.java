package de.avci.joride.restful.converters;

import de.avci.joride.restful.dto.favoritepoints.FavoritePointDTO;
import de.fhg.fokus.openride.customerprofile.FavoritePointEntity;


/** Converter to convert Favorite
 * 
 * @author jochen
 *
 */


public class FavoritePointDTOConverter {

	
	/** Dump entity data into a FavoritePointDTO  
	 * 
	 * @param entity
	 * @return
	 */	
	
	public FavoritePointDTO favoritePointDTO(FavoritePointEntity entity){
				
		FavoritePointDTO res=new FavoritePointDTO();
		res.setCustId(entity.getCustId().getCustId());
		res.setAddress(entity.getFavptAddress());
		res.setName(entity.getFavptDisplayname());
		
		// For obscure reasons, lon/lat coordinates come encoded in a single string
		// so, until we get rid of that string, we have to split and convert
		// the entity.getFavptPoint String:
		String[] strs= entity.getFavptPoint().split(",");
		res.setLat(new Double(strs[0]));
		res.setLon(new Double(strs[1]));	
		
		return res;
	}
	
	

	
}
