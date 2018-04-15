/*
 * MP3-Album-Art - Get album art from mp3 (or alternative poll from musicbrainz)
 * Copyright (c) 2018 Dirk Friedenberger <projekte@frittenburger.de>
 * 
 * For getting infos the  REST-based webservice API for direct access to MusicBrainz data  is used
 * (https://musicbrainz.org/doc/Developer_Resources)
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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import de.frittenburger.music.bo.MusicBrainzReleaseEntry;
import de.frittenburger.music.interfaces.MusicBrainzService;
import de.frittenburger.music.interfaces.WebRequestService;

public class MusicBrainzServiceImpl implements MusicBrainzService {

	private final WebRequestService webRequestService;

	public MusicBrainzServiceImpl(WebRequestService webRequestService) {
		this.webRequestService = webRequestService;
	}

	@Override
	public List<MusicBrainzReleaseEntry> queryRelease(String text) {

		try {
			JsonNode tree = webRequestService.getJson("/release",new String[]{"query","fmt"},new String[]{text,"json"});
			ArrayNode releases = (ArrayNode)tree.get("releases");
			
			List<MusicBrainzReleaseEntry> l = new ArrayList<MusicBrainzReleaseEntry>();
			for (final JsonNode release : releases) {
				try
				{
					System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(release));

					MusicBrainzReleaseEntry e = new MusicBrainzReleaseEntry();
					e.setMbid(release.get("id").asText());
					e.setScore(release.get("score").asInt());
					e.setTitle(release.get("title").asText());
					JsonNode type = release.get("release-group").get("primary-type");
					if(type == null) // no type found
						continue;
					e.setType(type.asText());
					
					String artist = release.get("artist-credit").get(0).get("artist").get("name").asText();
					e.setArtist(artist);
					l.add(e);
				}
				catch(Exception e)
				{
					System.err.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(release));
					e.printStackTrace();
				}
		    }
			
			return l;
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		return null;
	}

}
