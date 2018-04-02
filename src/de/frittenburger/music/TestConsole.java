package de.frittenburger.music;

import java.io.File;

import de.frittenburger.music.impl.AlbumArtServiceImpl;
import de.frittenburger.music.impl.CoverArtServiceImpl;
import de.frittenburger.music.impl.LevenshteinDistance;
import de.frittenburger.music.impl.MusicBrainzServiceImpl;
import de.frittenburger.music.impl.NameServiceImpl;
import de.frittenburger.music.impl.WebRequestServiceImpl;
import de.frittenburger.music.interfaces.AlbumArtService;

public class TestConsole {

	
	public static void main(String args[])
	{
		
		AlbumArtService albumArtService = new AlbumArtServiceImpl(
				"data/",
				new MusicBrainzServiceImpl(new WebRequestServiceImpl("https://musicbrainz.org/ws/2")),
				new CoverArtServiceImpl(new WebRequestServiceImpl("https://coverartarchive.org")),
				new LevenshteinDistance(),
				new NameServiceImpl()	
		);
		
		System.out.println(albumArtService.resolveImageForMp3(new File("- song -.mp3")));
	

		
	}
}
