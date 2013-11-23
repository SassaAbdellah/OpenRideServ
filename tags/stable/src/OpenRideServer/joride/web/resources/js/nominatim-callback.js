
/** A JSONP Callback Function to process results of the  nominatim geocoding service.
 *   
 *  This will process a result returned from nominatim webservice 
 *  and display it at an element with CSS ID "geocoding_out".
 * 
 *  To work Properly, a number of variables (better say constants)
 *  must be defined *outside* of the function.
 *  Thus, we mimick passing parameter to a JSONP callback.
 *  
 *  Javascript variables may be defined inside the page that
 *  includes the callback function, but before the 
 *  JSONP is explicitely called.
 *  See how it is done in jsonp.xml:
 * 
 *             <script type="text/javascript"> 
 *              var geocodeResultHeader='#{msgs.geocodeResultHeader}';
 *               var geocodeAcceptLabel='#{msgs.geocodeAcceptLabel}';
 *                 : 
 *               (more variable definitions...)
 *               </script>
 *
 *     <!-- and then, with all parameters defined, include the nominatim -->       
 *
 *     <script type="text/javascript" src="#{nominatimq.queryString}"></script>
 * 
 * 
 *   
 * 
 * 
 * 
 * 
 */


 function callback(response){

     "use strict";

    // M$ IE Browser can't cope with the folloing two lines :-( 
    //   
    // var jsontext=(JSON.stringify(response));
    // console.log(jsontext); 
 
     var numberOfResults=response.length;
    
   // make a nice header 
   $('#geosearch_out').append('serial: 13');
   
   $('#geosearch_out').append('\<h3\>'+geocodeResultHeader+' '+numberOfResults+'\<\/h3\>');   
   
   var i=0;
   while(i<numberOfResults){
        
        
        var display=response[i].display_name;
        var lat=response[i].lat;
        var lon=response[i].lon;
        
        
        // build the url for calling the mapper
        // note that early webkit browsers want to have ampersant escaped as '&aml;
        // which is not done by the encodeURI funtion
        
        var mapperURL=mapURL+('\?'+lonP+'\='+lon+'&amp;'+latP+'\='+lat+'&amp;'+displayP+'\='+display); 

        mapperURL=encodeURI(mapperURL);
        
        // dropped as not to annoy M$ IIE
        // 
        //console.log('mapperURL : '+mapperURL);
        
           // build the URL for acceppting the place
        var targetedURL=targetURL+('\?'+lonP+'\='+lon+'&amp;'+latP+'\='+lat+'&amp;'+displayP+'\='+display+'&amp;'+targetP+'\='+target); 
  
        // dropped as not to annoy M$ IIE
        // 
        // console.log('targetedURL : '+targetedURL);
        
        
        // since webkit has trouble displaying divs, we had to add 
        // tons of ugly br tags here.
        // TODO: find out what's wrong with webkit on android 2.2'
        
	$('#geosearch_out').append('\<br\/\>')
        $('#geosearch_out').append(response[i].display_name);  
        $('#geosearch_out').append('\<br\/\>');
        $('#geosearch_out').append(geocodeAcceptLabel.link(targetedURL));
        $('#geosearch_out').append('........................');
        $('#geosearch_out').append(geocodeShowInMapLabel.link(mapperURL));
        $('#geosearch_out').append('\<br\/\>')
        
        
	i++;
   } // while i< numberOfResults

 
   

  }

