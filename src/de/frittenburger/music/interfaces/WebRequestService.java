package de.frittenburger.music.interfaces;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

import com.fasterxml.jackson.databind.JsonNode;

public interface WebRequestService {

	public JsonNode getJson(String path, String[] key, String[] value) throws IOException, GeneralSecurityException, URISyntaxException;
	
	public BufferedImage getImage(String path, String[] key, String[] value) throws IOException, GeneralSecurityException, URISyntaxException;
		
}
