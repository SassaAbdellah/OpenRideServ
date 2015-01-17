
     


//
// transform longitude and latitude into coordinates
// of the projection of the respective map.
//
// Returns an Object of class OpenLayers.Geometry.Point
//
// Parameters:
//
//  lon : longitude (numerical)
//  lat : latitude  (numerical)
//  map : the map for which the coordinates will have to be scaled
//
//
//

function getScaledPoint(lon,lat,map){
  
    ll = new OpenLayers.LonLat(lon,lat).transform(
        new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
        map.getProjectionObject() // to current map's projection
        );
		   
		   
    my_point= new OpenLayers.Geometry.Point(ll.lon,ll.lat);
		   
    return my_point;
}


//  Create linestring object from a list of points.
//  
//  Parameters:
//
//  coords : list of coordinates, i.e two dimensional numerical matrix of the form
//           [[longitude_1,latitude_1],[longitude_2,latitude_2],...[longitude_n, latitude_n]]
//          
//  map    : the map for which the coordinates will have to be scaled
//

function createLinestring( coords , map ){

	
    lPoints=[];	

    for(i=0; i<coords.length; i++){
        lPoints[i]=getScaledPoint(coords[i][0],coords[i][1],map);
    }	
	
    return new OpenLayers.Geometry.LineString(lPoints); 

}	
  	
	
//  Set bounds  and zoom for the map, so that the points given by a  list of coordinates fits in
// 
//  Parameters:
//
//  coords : list of coordinates, i.e two dimensional numerical matrix of the form
//           [[longitude_1,latitude_1],[longitude_2,latitude_2],...[longitude_n, latitude_n]]
//  
//  map    : the map for which the coordinates will have to be scaled
//

function autocenterMap( coords , map ){
	
    bounds=new OpenLayers.Bounds();


    for(i=0; i<coords.length; i++){
        bounds.extend(getScaledPoint(coords[i][0],coords[i][1],map));
    }

    map.zoomToExtent(bounds);
}





//  Create a map showing the route (linestring) defined by the routepoints parameter.
//  the map is automagically scaled and centered to show the complete route
//
//  Parameters:
//
//  divID       :  id of the html div object where the map is to be displayed
//  
//  routepoints : list of waypoint coordinates, i.e two dimensional numerical matrix of the form
//                [[longitude_1,latitude_1],[longitude_2,latitude_2],...[longitude_n, latitude_n]]
//   
//  startpoint   : single coordinate for starting point, format as above
//  endpoind     : single coordinate for endpoint, format as above
//  pickuppoints : list of points where riders get picked up, same format as routepoints
//  droppoints   : list of points where riders get dropped, same format as routepoints
//  waypoints    : list of waypoints, in the same format as routepoints
//  




function createMap(
        divId,        
        startpoint, 
        endpoint, 
        routepoints, 
        pickupPoints, 
        dropPoints,
        wayPoints
        ) { 

    map = new OpenLayers.Map(divId);
    map.addLayer(new OpenLayers.Layer.OSM());
    
   
     
     // console log should normally disabled because it blocks braindamaged IE
     //
     // console.log("longitude : "+routepoints[0][0]);
     // console.log("latitude  : "+routepoints[0][1]);
     
                                               

    // add vector layer for drawing lines	
    lineLayer = new OpenLayers.Layer.Vector("Line Layer"); 
    map.addLayer(lineLayer);                    
    map.addControl(new OpenLayers.Control.DrawFeature(lineLayer, OpenLayers.Handler.Path));   


    line = createLinestring(routepoints,map);
    
    // dropped as not to annoy M$ IIE
    // 
    //  console.log("linestring : "+line);

    style = { 
        strokeColor: '#000000', 
        strokeOpacity: 0.5,
        strokeWidth: 5
    };

    lineFeature = new OpenLayers.Feature.Vector(line, null, style);
    lineLayer.addFeatures([lineFeature]);


    jorideMarkersLayer = new OpenLayers.Layer.Markers("markers", {'calculateInRange': function() { return true; }});      
    map.addLayer(jorideMarkersLayer);

   // add marker for startpoint
    llWaypoint=new OpenLayers.LonLat(startpoint[0],startpoint[1]).transform(
                                        new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
                                        map.getProjectionObject() // to current map's projection
                                        );

     iconMarkerGreenURL="/joride/resources/images/marker-green.png";
     icon = new OpenLayers.Icon(iconMarkerGreenURL,new OpenLayers.Size(21,25),new OpenLayers.Pixel(-11,-25));
     markerWaypoint=new OpenLayers.Marker(llWaypoint,icon);
     jorideMarkersLayer.addMarker(markerWaypoint);
  

  
     // add marker for endpoint
    llWaypoint=new OpenLayers.LonLat(endpoint[0],endpoint[1]).transform(
                                        new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
                                        map.getProjectionObject() // to current map's projection
                                        );

     iconMarkerRedURL="/joride/resources/images/marker-red.png";
     icon = new OpenLayers.Icon(iconMarkerRedURL,new OpenLayers.Size(21,25),new OpenLayers.Pixel(-11,-25));
     markerWaypoint=new OpenLayers.Marker(llWaypoint,icon);
     jorideMarkersLayer.addMarker(markerWaypoint);
    
    
   // add markers for pickupPoints
   for (i=0;  i < pickupPoints.length; i++){
 
        llWaypoint=new OpenLayers.LonLat(pickupPoints[i][0],pickupPoints[i][1]).transform(
                                        new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
                                        map.getProjectionObject() // to current map's projection
                                        );

	riderPickupURL="/joride/resources/images/RiderPickup.png";
 	icon = new OpenLayers.Icon(riderPickupURL,new OpenLayers.Size(20,20),new OpenLayers.Pixel(-10,-10));
 	markerWaypoint=new OpenLayers.Marker(llWaypoint,icon);
    	jorideMarkersLayer.addMarker(markerWaypoint);
   }

  


 // add markers for dropPoints
   for (i=0;  i < dropPoints.length; i++){
 
        llWaypoint=new OpenLayers.LonLat(dropPoints[i][0],dropPoints[i][1]).transform(
                                        new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
                                        map.getProjectionObject() // to current map's projection
                                        );

	riderDropURL="/joride/resources/images/RiderDrop.png";
 	icon = new OpenLayers.Icon(riderDropURL,new OpenLayers.Size(20,20),new OpenLayers.Pixel(-10,-10));
 	markerWaypoint=new OpenLayers.Marker(llWaypoint,icon);
    	jorideMarkersLayer.addMarker(markerWaypoint);
   }

 // add markers for wayPoints
   for (i=0;  i < wayPoints.length; i++){
 
        llWaypoint=new OpenLayers.LonLat(wayPoints[i][0],wayPoints[i][1]).transform(
                                        new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
                                        map.getProjectionObject() // to current map's projection
                                        );

	waypointURL="/joride/resources/images/waypoint.png";
 	icon = new OpenLayers.Icon(waypointURL,new OpenLayers.Size(10,10),new OpenLayers.Pixel(-5,-5));
 	markerWaypoint=new OpenLayers.Marker(llWaypoint,icon);
    	jorideMarkersLayer.addMarker(markerWaypoint);
   }




    autocenterMap(routepoints,map);
   
} // map




