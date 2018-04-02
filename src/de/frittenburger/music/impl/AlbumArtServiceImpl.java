package de.frittenburger.music.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

import de.frittenburger.music.bo.FrontCover;
import de.frittenburger.music.bo.MusicBrainzReleaseEntry;
import de.frittenburger.music.interfaces.AlbumArtService;
import de.frittenburger.music.interfaces.CoverArtService;
import de.frittenburger.music.interfaces.MusicBrainzService;
import de.frittenburger.music.interfaces.NameService;
import de.frittenburger.music.interfaces.StringDistance;

public class AlbumArtServiceImpl implements AlbumArtService {

	
	private final String dataPath;
	private final MusicBrainzService mbservice = new MusicBrainzServiceImpl(new WebRequestServiceImpl("https://musicbrainz.org/ws/2"));
	private final CoverArtService caservice = new CoverArtServiceImpl(new WebRequestServiceImpl("https://coverartarchive.org"));
	
	private final StringDistance distance = new LevenshteinDistance();
	private final NameService nameService = new NameServiceImpl();
	private final Set<String> processed = new HashSet<String>(); //cache
	
	
	public AlbumArtServiceImpl(String dataPath, MusicBrainzService mbservice, CoverArtService caservice,
			StringDistance distance, NameService nameService)
	{
		this.dataPath = dataPath;
		this.mbservice = mbservice;
		this.caservice = caservice;
		this.distance = distance;
		this.nameService = nameService;
	}
	
	
	@Override
	public BufferedImage resolveImageForMp3(File mp3file) {
		
		
		try
		{
		//MP3 Tag Info ???
		
		AudioFile f = AudioFileIO.read(mp3file);
	
		Tag tag = f.getTag();
		
		String artist = tag.getFirst(FieldKey.ARTIST);
		String album = tag.getFirst(FieldKey.ALBUM);
		
		Artwork artwork = tag.getFirstArtwork();
		
		if(artwork != null)
		{
			 ByteArrayInputStream bais = new ByteArrayInputStream(artwork.getBinaryData());
			 return ImageIO.read(bais);
		}
		
		//try to get Image from music brainz	
		
		File file = nameService.createFilename(dataPath,artist,album,"png");
		

	    if(file.exists())
	    {
			return ImageIO.read(file);	
	    }
	    
	    //ohne Erfolg bereits versucht (reduce requests)
	    if(processed.contains(file.getName())) return null;
	    processed.add(file.getName());
	    
	    
		List<MusicBrainzReleaseEntry> entries = mbservice.queryRelease(album);
		
		//filter and reorder 
		String mbid = null;
		int d = 1000;
		for(MusicBrainzReleaseEntry e : entries)
		{
			if(!e.getType().toLowerCase().equals("album")) continue;
			System.out.println(e);
			int dist = distance.calculate(e.getTitle().toLowerCase(),album.toLowerCase());
			if(dist < d)
			{
				mbid = e.getMbid();
				d = dist;
			}
		}
		
		if(mbid == null)
		{
			System.out.println("No cover found for "+album);
			return null;
		}
		
					
		List<FrontCover> covers = caservice.getFrontCovers(mbid);
		for(FrontCover c : covers)
			System.out.println(c);
		
		String imgpath = covers.get(0).getPath();
		
		//cut path
		imgpath = imgpath.substring("http://coverartarchive.org".length());
		
		
		
		
		BufferedImage img = caservice.downloadImage(imgpath);
		
		if(img != null)
		{
			ImageIO.write(img, "png", file);
		}
		return img;

		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}

}
