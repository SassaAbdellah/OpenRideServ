package de.avci.joride.restful.temporary;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

import de.avci.openrideshare.restful.client.RestClient;

public class TestClient {

	/**
	 * @param args
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 */
	public static void main(String[] args) throws ClientProtocolException,
			URISyntaxException, IOException {

		RestClient restClient = new RestClient();
		restClient.setHostname("localhost");
		restClient.setPort(8080);
		restClient.setBasepath("/joride-rest/jax-rs/");

		// ping
		System.out.println("===================================");
		System.out.println(restClient.getJSON("ping/", null));
		System.out.println("===================================");

		// list of all ride offers
		/*
		System.out.println("===================================");
		System.out.println(restClient.getJSON("offer/allRideOffers", null));
		System.out.println("===================================");
		*/
		
		// ride offer by id
		
		System.out.println("===================================");
		System.out.println(restClient.getJSONObjectById("offer", 53951));
		System.out.println("===================================");
		
		
		// ride request by id
	
		/*
		System.out.println("===================================");
		System.out.println(restClient.getJSONObjectById("request", 53965));
		System.out.println("===================================");
		*/
		
		
		String offerJSON = "{\"id\":null,\"customerId\":24852,\"acceptableDetourKM\":10,\"startTime\":1498858780000,\"startLocation\":{\"lon\":11.3666579,\"lat\":53.2764929,\"name\":null,\"address\":\"Forsthof Glaisin, LindenstraÃŸe, Ludwigslust, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, EuropÃ¤ische Union\"},\"endLocation\":{\"lon\":11.4969985748317,\"lat\":53.4097714,\"name\":null,\"address\":\"UFAT Bildungswerk, WÃ¶bbelin, Ludwigslust-Land, Ludwigslust-Parchim, Mecklenburg-Vorpommern, 19288, Deutschland, EuropÃ¤ische Union\"},\"routePoints\":null,\"drivePoints\":null,\"wayPoints\":[],\"offeredSeatsNo\":1,\"comment\":\"Ein Kommentar zum Angebot\"}";
		System.out
				.println("=== posting offer ================================");
		restClient.postJSON("offer", offerJSON);
		System.out
				.println("=== done with posting offer ======================");

	
		// list of all users
		System.out.println("===================================");
		System.out.println(restClient.getJSON("customer/findAll", null));
		System.out.println("===================================");
	   
	}

}
