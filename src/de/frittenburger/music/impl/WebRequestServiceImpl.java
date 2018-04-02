package de.frittenburger.music.impl;


import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.music.interfaces.WebRequestService;


public class WebRequestServiceImpl implements WebRequestService {

	
	private final String url;


	public WebRequestServiceImpl(String url)
	{
		this.url = url;
	}
	
	@Override
	public JsonNode getJson(String path, String[] key, String[] value) throws IOException, GeneralSecurityException, URISyntaxException {

		
		CloseableHttpClient httpclient = getHttpClient();
		
		
		URIBuilder builder = new URIBuilder(url + path);
		for(int i = 0;i < key.length;i++)
			builder.setParameter(key[i], value[i]);
		URI uri = builder.build();
		System.out.println(uri);

		
		HttpGet httpget = new HttpGet(uri);
		
		
		httpget.setHeader("User-Agent", "MBGA/1.0 ( www.frittenburger.de )");
		
		
		CloseableHttpResponse response = httpclient.execute(httpget);

		ObjectMapper mapper = new ObjectMapper();
		
		
		try {

			// Display status code
			System.out.println(response.getStatusLine());

			// Display response
			HttpEntity entity = response.getEntity();
			String body = EntityUtils.toString(entity);
			System.out.println(body);

			JsonNode tree = mapper.readTree(body);
			//System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree));
			return tree;

		} finally {
			response.close();
		}
		
	}
	
	
	@Override
	public BufferedImage getImage(String path, String[] key, String[] value) throws IOException, GeneralSecurityException, URISyntaxException {
		
		CloseableHttpClient httpclient = getHttpClient();
		
		
		URIBuilder builder = new URIBuilder(url + path);
		for(int i = 0;i < key.length;i++)
			builder.setParameter(key[i], value[i]);
		URI uri = builder.build();
		System.out.println(uri);

		
		HttpGet httpget = new HttpGet(uri);
		
		
		httpget.setHeader("User-Agent", "MBGA/1.0 ( www.frittenburger.de )");
		
		
		CloseableHttpResponse response = httpclient.execute(httpget);	
		
		
		try {

			// Display status code
			System.out.println(response.getStatusLine());

			// Display response
			HttpEntity entity = response.getEntity();
			
			return ImageIO.read(entity.getContent());
			
		} finally {
			response.close();
		}
	}
	
	
	private CloseableHttpClient getHttpClient() throws GeneralSecurityException {

		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
			public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				return true;
			}
		}).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);

		// return HttpClients.createDefault();
		return HttpClients.custom().setSSLSocketFactory(sslsf).build();
	}

	


	

	
}
