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
