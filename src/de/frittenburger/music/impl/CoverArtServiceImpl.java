/*
 * MP3-Album-Art - Get album art from mp3 (or alternative poll from musicbrainz)
 * Copyright (c) 2018 Dirk Friedenberger <projekte@frittenburger.de>
 * 
 * For getting covers the rest webservice of Cover Art Archive is used.
 * (http://coverartarchive.org/)
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
