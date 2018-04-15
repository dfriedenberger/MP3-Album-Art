/*
 * MP3-Album-Art - Get album art from mp3 (or alternative poll from musicbrainz)
 * Copyright (c) 2018 Dirk Friedenberger <projekte@frittenburger.de>
 * 
 * This file is part of MP3-Album-Art.
 *
 * MP3-Album-Art is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MP3-Album-Art is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MP3-Album-Art.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
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

	
	private final static String UserAgent = "MP3-Album-Art/1.0 ( https://github.com/dfriedenberger/MP3-Album-Art )";
	
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
		
		
		httpget.setHeader("User-Agent", UserAgent);
		
		
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
		
		
		httpget.setHeader("User-Agent", UserAgent);
		
		
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
