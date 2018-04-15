/*
 * MP3-Album-Art - Get album art from mp3 (or alternative poll from musicbrainz)
 * Copyright (c) 2018 Dirk Friedenberger <projekte@frittenburger.de>
 * 
 * For reading mp3 tags the jaudiotagger library (http://www.jthink.net/jaudiotagger) 
 * from paultaylor@jthink.net is used.
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

import java.io.ByteArrayInputStream;
import java.io.File;

import javax.imageio.ImageIO;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.images.Artwork;

import de.frittenburger.music.bo.BatchingException;
import de.frittenburger.music.bo.MP3Info;
import de.frittenburger.music.interfaces.MP3FileReader;

public class MP3FileReaderImpl implements MP3FileReader {

	@Override
	public MP3Info readInfo(File mp3file) throws BatchingException  {
		
		
		if(mp3file == null)
			throw new NullPointerException("mp3file");
		
		
		try
		{
			
			MP3Info info = new MP3Info();
	
			AudioFile f = AudioFileIO.read(mp3file);
	
			Tag tag = f.getTag();
	
			info.setArtist(tag.getFirst(FieldKey.ARTIST));
			info.setAlbum(tag.getFirst(FieldKey.ALBUM));
	
			Artwork artwork = tag.getFirstArtwork();
	
			if (artwork != null) {
	
				ByteArrayInputStream bais = new ByteArrayInputStream(artwork.getBinaryData());
				info.setImage(ImageIO.read(bais));
			}
			return info;

		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new BatchingException("couldn't read mp3info for "+mp3file.getPath());
		}
	}

}
