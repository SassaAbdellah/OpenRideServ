package de.avci.openrideshare.restful.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

/**
 * HttpClient for restful Services provided by the joride-rest subproject.
 * Currently, this is used mainly for integration tests.
 * 
 * 
 * @author jochen
 * 
 */
public class RestClient {

	/**
	 * My client
	 */
	private  CloseableHttpClient httpclient = HttpClients.createDefault();

	/**
	 * HOST to call for restful services
	 * 
	 * TODO: configure from local properties.
	 * 
	 */
	private  String hostname;

	/**
	 * Port to call for restful services
	 * 
	 * TODO: configure from local properties.
	 * 
	 */
	private Integer port;

	/**
	 * basic path on server where jersey servlet of joride-rest application is
	 * deployed.
	 * 
	 * TODO: this should be made configurable from local properties
	 */

	private String basepath;

	/**
	 * 
	 * @param path
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String getJSON(String path, HttpParams params)
			throws URISyntaxException, ClientProtocolException, IOException {

		try {
			URI uri = new URIBuilder().setScheme("http").setHost(hostname)
					.setPort(port).setPath(basepath + path).build();
			HttpGet httpget = new HttpGet(uri);

			if (params != null) {
				httpget.setParams(params);
			}

			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				public String handleResponse(final HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity)
								: null;
					} else {
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}

			}; // ResponseHandler ends here

			return httpclient.execute(httpget, responseHandler);

		} finally {
			// do not close for repeated calls
			// httpclient.close();
		}

	}

	/**
	 * 
	 * @param path
	 * @param id
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 */
	public  String getJSONObjectById(String path, Integer id)
			throws ClientProtocolException, URISyntaxException, IOException {

		String idPath = path + "/" + id;
		return getJSON(idPath, null);
	}

	/**
	 * Post a JSON String (to create a new Object)
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * 
	 */

	public void postJSON(String path, String json)
			throws URISyntaxException, ClientProtocolException, IOException {

		CloseableHttpResponse response = null;

		try {
			URI uri = new URIBuilder().setScheme("http").setHost(hostname)
					.setPort(port).setPath(basepath + path).build();
			HttpPost httppost = new HttpPost(uri);
			StringEntity strEntity = new StringEntity(json);
			strEntity.setContentType("application/json");
			strEntity.setContentEncoding("utf8");
			httppost.setEntity(strEntity);
			response = httpclient.execute(httppost);
			
			System.out.println("status code : "+response.getStatusLine().getStatusCode());
			System.out.println("reason      : "+response.getStatusLine().getReasonPhrase());

		} finally {
			if (response != null) {
				response.close();
			}
		}
	}



	public CloseableHttpClient getHttpclient() {
		return httpclient;
	}

	public void setHttpclient(CloseableHttpClient httpclient) {
		this.httpclient = httpclient;
	}

	public  Integer getPort() {
		return port;
	}

	public  String getBasepath() {
		return basepath;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setBasepath(String basepath) {
		this.basepath = basepath;
	}

}
