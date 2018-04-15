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

import java.io.File;

import org.junit.Test;

import de.frittenburger.music.impl.NameServiceImpl;
import de.frittenburger.music.interfaces.NameService;

public class TestNameServiceImpl {

	@Test
	public void testPathWithSlash() {

		NameService nameservice = new NameServiceImpl();
		
		File file = nameservice.createFilename("data/", "testartist", "testalbum", "ext");
		assertEquals("testartist_testalbum.ext", file.getName());
		assertEquals("data", file.getParent());

	}

	@Test
	public void testPathWithOutSlash() {

		NameService nameservice = new NameServiceImpl();
		
		File file = nameservice.createFilename("data", "testartist", "testalbum", "ext");
		assertEquals("testartist_testalbum.ext", file.getName());
		assertEquals("data", file.getParent());
	
	}
	
	@Test
	public void testSpecialChar() {

		NameService nameservice = new NameServiceImpl();
		
		File file = nameservice.createFilename("data", "Aöäü/()?ß§$%&T", "A;:-#+*M", "ext");
		assertEquals("a_t_a_m.ext", file.getName());
		assertEquals("data", file.getParent());
	
	}
	
	
}
