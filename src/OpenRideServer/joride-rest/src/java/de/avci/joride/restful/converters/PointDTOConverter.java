package de.avci.joride.restful.converters;

import org.postgis.Point;

import de.avci.joride.restful.dto.basic.PointDTO;




/** Converts Points from Entity and 
 * 
 * @author jochen
 *
 */
public class PointDTOConverter {

	
	/** Create PointDTO from longitude and latitude coordinates.
	 * 
	 * @param lon
	 * @param lat
	 * @return 
	 */
	public PointDTO pointDTO(double lon, double lat){
		
		PointDTO res=new PointDTO();
		res.setLon(lon);
		res.setLat(lat);
		return res;
		
	}
	
	
	/** Create PointDTO from Postgis Point
	 *  point.x is interpreted as latitude,
	 *  point.y is interpreted as longitude
	 *  
	 * 
	 * @param point
	 * @return
	 */
	public PointDTO pointDTO(Point point){
		
		PointDTO res=new PointDTO();
		res.setLon(point.getX());
		res.setLat(point.getY());
		return res;
	}
	

	/** Convert (Postgis) Point to PointDTO
	 * 
	 *  point.x is interpreted as latitude,
	 *  point.y is interpreted as longitude
	 * 
	 * @param pointDTO
	 * @return
	 */
	public Point point(PointDTO pointDTO){
		
		Point point=new Point();
		point.setX(pointDTO.getLon());
		point.setY(pointDTO.getLat());
		
		return point;
	} 
	
	
}
