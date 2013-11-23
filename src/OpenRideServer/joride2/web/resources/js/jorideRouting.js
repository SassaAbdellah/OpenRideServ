
     


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
//  routepoints : list of waypoint coordinates, i.e two dimensional numerical matrix of the form
//                [[longitude_1,latitude_1],[longitude_2,latitude_2],...[longitude_n, latitude_n]]
//  
//  divID       :  id of the html div object where the map is to be displayed
//  





function createMap(divId, routepoints) { 

    map = new OpenLayers.Map(divId);
    map.addLayer(new OpenLayers.Layer.OSM());
    
     // //////////////////////////////////////////////	
     //  marker to mark the start of the coordiates
     // //////////////////////////////////////////////
     markersLayer = new OpenLayers.Layer.Markers("markers", {'calculateInRange': function() { return true; }});           
     
     markersLayer.clearMarkers();    
     map.addLayer(markersLayer);
     
     // console log should normally disabled because it blocks braindamaged IE
     //
     // console.log("longitude : "+routepoints[0][0]);
     // console.log("latitude  : "+routepoints[0][1]);
     
                                                
     startMarker=new OpenLayers.Marker(getScaledPoint(routepoints[0][0],routepoints[0][1],map));
     markersLayer.addMarker(startMarker);
    
    
    

    // add vector layer for drawing lines	
    lineLayer = new OpenLayers.Layer.Vector("Line Layer"); 
    map.addLayer(lineLayer);                    
    map.addControl(new OpenLayers.Control.DrawFeature(lineLayer, OpenLayers.Handler.Path));   


    line = createLinestring(routepoints,map);
    
    // dropped as not to annoy M$ IIE
    // 
    //  console.log("linestring : "+line);

    style = { 
        strokeColor: '#0000FF', 
        strokeOpacity: 1,
        strokeWidth: 5
    };

    lineFeature = new OpenLayers.Feature.Vector(line, null, style);
    lineLayer.addFeatures([lineFeature]);

   
    jorideMarkersLayer = new OpenLayers.Layer.Markers("markers", {'calculateInRange': function() { return true; }});      
    map.addLayer(jorideMarkersLayer);

    var pLongitudeEnd = routepoints[routepoints.length-1][0]; 
    var pLatitudeEnd  = routepoints[routepoints.length-1][1];

    llEnd=new OpenLayers.LonLat(pLongitudeEnd,pLatitudeEnd).transform(
                                        new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
                                        map.getProjectionObject() // to current map's projection
                                        );


               
    markerEnd=new OpenLayers.Marker(llEnd);
    jorideMarkersLayer.addMarker(markerEnd);


   
    var pLongitudeStart = routepoints[0][0]; 
    var pLatitudeStart  = routepoints[0][1];

    llStart=new OpenLayers.LonLat(pLongitudeStart,pLatitudeStart).transform(
                                        new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
                                        map.getProjectionObject() // to current map's projection
                                        );


               
    markerStart=new OpenLayers.Marker(llStart);
    jorideMarkersLayer.addMarker(markerStart);





    autocenterMap(routepoints,map);
   
} // map




