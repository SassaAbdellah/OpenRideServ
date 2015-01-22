package de.acando.facebooklogin;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * HTTP Client used to fetch tokens and work against the graph Api
 * 
 * TODO: this is by no way production ready. (i.e: no timeouts, no
 * threading,...)
 * 
 * 
 * @author jochen
 * 
 */
public class HTTPClient {

	/**
	 * TODO: Make client detect encoding before producing a string instead....
	 */
	private static String DEFAULT_ENCODING = "UTF-8";

	/**
	 * Fetch a document using http get method. This will return the document
	 * content as string.
	 * 
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * 
	 * 
	 */
	public static String fetchAsStringHttpGet(String path,
			Map<String, String> parameters) throws URISyntaxException,
			ClientProtocolException, IOException {

		URIBuilder ub = new URIBuilder();
		ub.setPath(path);
		for (String paramName : parameters.keySet()) {
			ub.setParameter(paramName, parameters.get(paramName));
		}
		URI uri = ub.build();

		// result to be returned
		String result = null;

		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpGet httpget = new HttpGet(uri);

		CloseableHttpResponse response = httpclient.execute(httpget);

		try {

			HttpEntity entity = response.getEntity();

			if (entity == null) {
				System.err.println("Entity was null, stopping :-( ");
				return null;
			}

			long len = entity.getContentLength();
			System.err.println("ContentLength : " + len);

			byte bytes[] = EntityUtils.toByteArray(entity);

			result = new String(bytes, DEFAULT_ENCODING);

		} finally {
			response.close();
		}

		
		return result;
	}

}
