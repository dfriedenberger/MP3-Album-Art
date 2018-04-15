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

import javax.imageio.ImageIO;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.frittenburger.music.bo.FrontCover;
import de.frittenburger.music.impl.CoverArtServiceImpl;
import de.frittenburger.music.interfaces.CoverArtService;
import de.frittenburger.music.interfaces.WebRequestService;

public class TestCoverArtServiceImpl {

	
	@Test
	public void testgetFrontCovers() {
		
		CoverArtService service = new CoverArtServiceImpl(new WebRequestService(){

			@Override
			public JsonNode getJson(String path, String[] key, String[] value)
					throws IOException, GeneralSecurityException, URISyntaxException {
				return new ObjectMapper().readTree(new File("testdata/CoverArtRequest.json"));
			}

			@Override
			public BufferedImage getImage(String path, String[] key, String[] value)
					throws IOException, GeneralSecurityException, URISyntaxException {
				throw new RuntimeException("Not implemented");
			}});
				
		List<FrontCover> list = service.getFrontCovers("1234567");
		assertNotNull(list);
		assertEquals(1,list.size());
		
		
		FrontCover e = list.get(0);
		assertEquals("http://coverartarchive.org/release/7009c093-8db9-381d-a988-9132f28c621c/6218011523-500.jpg",e.getPath());
		
	}

	
	
	
	@Test
	public void testDownloadImage() {
		
		CoverArtService service = new CoverArtServiceImpl(new WebRequestService(){

			@Override
			public JsonNode getJson(String path, String[] key, String[] value)
					throws IOException, GeneralSecurityException, URISyntaxException {
				throw new RuntimeException("Not implemented");
			}

			@Override
			public BufferedImage getImage(String path, String[] key, String[] value)
					throws IOException, GeneralSecurityException, URISyntaxException {
				
				return ImageIO.read(new File("testdata/image.jpg"));
				
			}});
				
		BufferedImage img = service.downloadImage("/image.jpg");
		assertNotNull(img);
	}

}
