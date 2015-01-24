package de.avci.joride.restful.converters;

import de.avci.joride.restful.dto.offers.DriveRoutepointDTO;
import de.fhg.fokus.openride.rides.driver.DriveRoutepointEntity;

/** Converter to convert DriveRoutepoint entities to DriveRoutepointDTOs.
 *  Note, that there is no converstion DTO->Entity, as there should be no need for this.
 * 
 * @author jochen
 *
 */
public class DriveRoutepointDTOConverter extends RoutepointDTOConverter {

	
	/** Convert DriveRoutepointEntity to DriveRoutepointDTO
	 * 
	 * @param entity
	 * @return
	 */
	public DriveRoutepointDTO driveRoutePointDTO(DriveRoutepointEntity entity){
		
		DriveRoutepointDTO res=new DriveRoutepointDTO();
		
		
		res.setExpectedArrival(entity.getExpectedArrival());
		res.setLon(entity.getCoordinate().x);
		res.setLat(entity.getCoordinate().y);
		res.setRideId(entity.getDriveId());
		res.setRouteIdx(entity.getRouteIdx());
		res.setSeatsAvaillable(entity.getSeatsAvailable());
		
		return res;
	}
	
	
	
	
}
