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
package de.frittenburger.music;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.music.bo.MusicBrainzReleaseEntry;
import de.frittenburger.music.impl.MusicBrainzServiceImpl;
import de.frittenburger.music.interfaces.MusicBrainzService;
import de.frittenburger.music.interfaces.WebRequestService;

public class TestMusicBrainzServiceImpl {

	@Test
	public void test() {
		
		
		MusicBrainzService service = new MusicBrainzServiceImpl(new WebRequestService() {

			@Override
			public JsonNode getJson(String path, String[] key, String[] value)
					throws IOException, GeneralSecurityException, URISyntaxException {
				
				return new ObjectMapper().readTree(new File("testdata/MusicBrainzAlbumRequest.json"));
			}

			@Override
			public BufferedImage getImage(String path, String[] key, String[] value)
					throws IOException, GeneralSecurityException, URISyntaxException {
				throw new RuntimeException("not implemented");
			}});
		
		
		
		List<MusicBrainzReleaseEntry> list = service.queryRelease("Im Schatten der Ärzte");
		
		
		assertNotNull(list);
		assertEquals(1,list.size());
		
		
		MusicBrainzReleaseEntry e = list.get(0);
		
		assertEquals("7009c093-8db9-381d-a988-9132f28c621c",e.getMbid());
		assertEquals(100,e.getScore());
		assertEquals("Album",e.getType());
		assertEquals("Im Schatten der Ärzte",e.getTitle());
		assertEquals("Die Ärzte",e.getArtist());


	}

}
