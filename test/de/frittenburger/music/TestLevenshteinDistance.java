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

import org.junit.Test;

import de.frittenburger.music.impl.LevenshteinDistance;
import de.frittenburger.music.interfaces.StringDistance;

public class TestLevenshteinDistance {

	@Test
	public void test() {
		StringDistance distance = new LevenshteinDistance();
		
		assertEquals(0,distance.calculate("abc", "abc"));
		assertTrue(distance.calculate("abc", "GHI") == distance.calculate("GHI", "abc"));
		assertTrue(distance.calculate("abc", "abd") + distance.calculate("abd", "add") >= distance.calculate("abc", "add"));
		

	}

}
