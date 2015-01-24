 


//
// Create and return map with an osm layer
// for div named "mapdiv
//
 function createOSMMap(){
	xMap = new OpenLayers.Map("mapdiv");
        xMap.addLayer(new OpenLayers.Layer.OSM());
        return xMap;
    }
                    


//
// Create a Click control that moves the marker
//
//
function createClickControlForMap( yMap){
	
	// ////////////////////////////////////////////////////////////
        // create openlayers control object
        // //////////////////////////////////////////////////////////////
    
        OpenLayers.Control.Click = OpenLayers.Class(OpenLayers.Control, {
                
		defaultHandlerOptions: {
                            'single': true, // enable zoom on single click
                            'double': true, // enable moving the marker on dblclick
                            'pixelTolerance': 0,
                            'stopSingle': false,
                            'stopDouble': false
                  },

                     
		initialize: function(options) {
                            this.handlerOptions = OpenLayers.Util.extend(
                            {}, this.defaultHandlerOptions
                );
                         
		 OpenLayers.Control.prototype.initialize.apply(
                            this, arguments
                        );
                      
		this.handler = new OpenLayers.Handler.Click(
                            this, {
                                // react to double mouse click only,
                                // ignore single mouseclic
                                'dblclick': this.moveMarker
                            }, this.handlerOptions
                      	);
                        },
                
                        // move the marker to doubleclicked coordinates
                        // read coordinates, and set/lon/lat inputs to
                        // the new lon/lat values
                        moveMarker: function(e) {
                            


			    coord = yMap.getLonLatFromViewPortPx(e.xy);

                            // ///////////////////////////////////	
                            //  move the marker to current click position 
                            // ///////////////////////////////////
                            jorideMarkersLayer.clearMarkers();    
                            nextMarker=new OpenLayers.Marker(coord); 
                            jorideMarkersLayer.addMarker(nextMarker);
                           
                            
                  
                            // /////////////////////////////////////////////////
                            // transform from map's coordinate 
                            // back to lat/lon
                            // /////////////////////////////////////////////////

                            lonlat=coord.transform(map.getProjectionObject(), new OpenLayers.Projection("EPSG:4326"));

                            // set longitude and latitude in input controls	
                            // (i.e: input controls that have class lat or lon) 
                            $('#lat').val(lonlat.lat); 
                            $('#lon').val(lonlat.lon); 
                            
                            

                            $('#latSpan').html(lonlat.lat)
                            $('#lonSpan').html(lonlat.lon); 
                            
                         
                   
                            // no more actions on doubleclick (i.e: do not zoom in)
                            this.handler.stopDouble = true;
                        }

                    });

                    } // end of function createClickControlForMap()
		
                    // 
                    // Done with creating the map 
                   

                //
                // crete a new OpenLayers.Control.Click object for the given map
                //
                function activateClick(zMap){  
                    	click = new OpenLayers.Control.Click();
                    	zMap.addControl(click);
                    	click.activate();
		}



		


                   
               // add markers layer. Overwrite calculate in Range function to 
               // ensure that markers are visible at any zoom level
                 
               function createMarkersLayer(pLongitude,pLatitude,pZoom,pMap){ 

                   	jorideMarkersLayer = new OpenLayers.Layer.Markers("markers", {'calculateInRange': function() { return true; }});           
                     	pMap.addLayer(jorideMarkersLayer);


			ll=new OpenLayers.LonLat(pLongitude,pLatitude).transform(
            				new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
            				pMap.getProjectionObject() // to current map's projection
           				);
		   




                    	marker=new OpenLayers.Marker(ll);
                    	jorideMarkersLayer.addMarker(marker);

		
                    	map.setCenter (ll, pZoom );

               } // end of function createMarkersLayer




	



