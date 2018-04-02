package de.frittenburger.music.impl;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import de.frittenburger.music.bo.FrontCover;
import de.frittenburger.music.interfaces.CoverArtService;
import de.frittenburger.music.interfaces.WebRequestService;

public class CoverArtServiceImpl implements CoverArtService {

	private final WebRequestService webRequestService;

	public CoverArtServiceImpl(WebRequestService webRequestService) {
		this.webRequestService = webRequestService;
	}

	@Override
	public List<FrontCover> getFrontCovers(String mbid) {
		try {
			JsonNode tree = webRequestService.getJson("/release/"+mbid,new String[0],new String[0]);
			ArrayNode images = (ArrayNode)tree.get("images");
			List<FrontCover> l = new ArrayList<FrontCover>();
			for (final JsonNode image : images) {
				try
				{
					boolean isfront = image.get("front").asBoolean();
					String thumbnail = image.get("thumbnails").get("large").asText();
					if(isfront) 
					{
						FrontCover fc = new FrontCover();
						fc.setPath(thumbnail);
						l.add(fc);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			return l;
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;
	}

	@Override
	public BufferedImage downloadImage(String path) {
		try
		{
			 return webRequestService.getImage(path,new String[0],new String[0]);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}


}
