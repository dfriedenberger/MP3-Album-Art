# MP3-Album-Art
Get album art from mp3 (or alternative poll from musicbrainz)

```
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
```

# Thanks

# Contact
Dirk Friedenberger, Waldaschaff, Germany

Write me (oder Schreibe mir)
projekte@frittenburger.de

http://www.frittenburger.de 

