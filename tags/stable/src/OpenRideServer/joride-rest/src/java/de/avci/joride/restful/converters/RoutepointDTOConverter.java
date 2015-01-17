package de.avci.joride.restful.converters;

import java.util.LinkedList;
import java.util.List;

import de.avci.joride.restful.dto.offers.RoutepointDTO;
import de.fhg.fokus.openride.rides.driver.RoutePointEntity;



/** Converter to convert RoutepointEntities to RoutePointDTOs.
 *  Note, that there is no converstion DTO->Entity, as there should be no need for this.
 * 
 * 
 * @author jochen
 *
 */
public class RoutepointDTOConverter {
	
	
	public RoutepointDTO routepointDTO(RoutePointEntity entity){
		
		RoutepointDTO res=new RoutepointDTO();
		// routepointDTOs do not have ids!
		res.setLat(entity.getLatitude());
		res.setLon(entity.getLongitude());
		res.setRideId(entity.getRideId());
		res.setRouteIdx(entity.getRouteIdx());
		
		return res;
	}
	   
	
	/** Create List of WaypointsDTO from List of Waypoints
	 */

	public List <RoutepointDTO> routepointDTOList(List <RoutePointEntity> entities){
		
		List <RoutepointDTO> res=new LinkedList <RoutepointDTO>();
		
		for(RoutePointEntity entity: entities){
			res.add(routepointDTO(entity));
		}
		return res;
	}
	
	    

}
