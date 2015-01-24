package de.acando.facebooklogin;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class TestClient {
	
	
	
	 public static void main(String[] args) throws URISyntaxException, ClientProtocolException, IOException{
		
		URIBuilder ub=new URIBuilder();
		
		String path="http://localhost:8080/facebooklogin/showParameters/";
		
		ub.setParameter("ParamName","Param Value");
		
		ub.setPath(path);
		
		URI uri=ub.build();
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
	
		HttpGet httpget = new HttpGet(uri);
		
		CloseableHttpResponse response = httpClient.execute(httpget);
		try {
			
			

			System.out.println( "doing stuff with the response..." );
				
			HttpEntity entity = response.getEntity();
		
			 if(entity==null){
				 System.err.println("Entity was null, stopping :-( ");
				 return;
			 }
			 
			 long len = entity.getContentLength();
			 System.err.println("ContentLength : "+len);
			 
			// if (len != -1 && len < 204800000) {
			            
		
			 System.out.println(EntityUtils.toString(entity));
			 
			 
			 
			 System.out.println(uri.toASCIIString());
			 //      } else {
			            // Stream content out
			   //     }
			 
				   
		} finally {
		    response.close();
		    
		}
		
		
	}
	
	

	
}
