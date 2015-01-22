package de.avci.joride.restful.converters;

import org.postgis.Point;

import de.avci.joride.restful.dto.basic.LocationDTO;
import de.fhg.fokus.openride.helperclasses.converter.PointConverter;

/** Convert startPoint and Endpoint data from RideOffers to LocationDTOs
 * 
 * @author jochen
 *
 */
public class LocationDTOConverter {
	

	
	/** Convert duple of Postgis Point and description to location DTO
	 * 
	 * @param point        PostgisPoint:  point.x->longitude, point.y->latitude
	 * @param description
	 * @return
	 */
	public LocationDTO locationDTO(Point point, String address){
		
		LocationDTO res=new LocationDTO();
		res.setLon(point.getX());
		res.setLat(point.getY());
		res.setAddress(address);
		return res;
	}
	
	
	/** Extract Postgres Point from Location  
	 * 
	 * @return
	 */
	public Point point(LocationDTO dto){
		
		Point point=new Point();
		point.setX(dto.getLon());
		point.setY(dto.getLat());
		return point;
	}
	
	/** Extract Address from Location  
	 * 
	 * @return
	 */
	public String address(LocationDTO dto){
		return dto.getAddress();
	}
	
	
}
