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

import java.io.File;

import de.frittenburger.music.impl.AlbumArtServiceImpl;
import de.frittenburger.music.impl.CoverArtServiceImpl;
import de.frittenburger.music.impl.ImageFileServiceImpl;
import de.frittenburger.music.impl.LevenshteinDistance;
import de.frittenburger.music.impl.MP3FileReaderImpl;
import de.frittenburger.music.impl.MusicBrainzServiceImpl;
import de.frittenburger.music.impl.NameServiceImpl;
import de.frittenburger.music.impl.WebRequestServiceImpl;
import de.frittenburger.music.interfaces.AlbumArtService;

public class TestConsole {

	
	public static void main(String args[])
	{
		
		AlbumArtService albumArtService = new AlbumArtServiceImpl(
				"data/",
				new MP3FileReaderImpl(),
				new ImageFileServiceImpl(),
				new MusicBrainzServiceImpl(new WebRequestServiceImpl("https://musicbrainz.org/ws/2")),
				new CoverArtServiceImpl(new WebRequestServiceImpl("https://coverartarchive.org")),
				new LevenshteinDistance(),
				new NameServiceImpl()	
		);
		
		System.out.println(albumArtService.resolveImageForMp3(new File("- song -.mp3")));
	

		
	}
}
