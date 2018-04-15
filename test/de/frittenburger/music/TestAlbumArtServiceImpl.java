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
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.frittenburger.music.bo.BatchingException;
import de.frittenburger.music.bo.FrontCover;
import de.frittenburger.music.bo.MP3Info;
import de.frittenburger.music.bo.MusicBrainzReleaseEntry;
import de.frittenburger.music.impl.AlbumArtServiceImpl;
import de.frittenburger.music.interfaces.AlbumArtService;
import de.frittenburger.music.interfaces.CoverArtService;
import de.frittenburger.music.interfaces.MusicBrainzService;
import de.frittenburger.music.interfaces.MP3FileReader;
import de.frittenburger.music.interfaces.ImageFileService;
import de.frittenburger.music.interfaces.NameService;
import de.frittenburger.music.interfaces.StringDistance;

public class TestAlbumArtServiceImpl {

	@Test
	public void testImageInMP3File() {
		
		
		AlbumArtService albumArtService = new AlbumArtServiceImpl(
				null,
				new MP3FileReader() {

					@Override
					public MP3Info readInfo(File file) {
						if(!file.getName().equals("testsong.mp3"))
							throw new RuntimeException("unknown mp3file "+file.getName());		
							MP3Info info = new MP3Info();
							info.setImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
							return info;
										
					}},
				null,
				null,
				null,
				null,
				null	
		);
		
		BufferedImage image = albumArtService.resolveImageForMp3(new File("testsong.mp3"));
		assertNotNull(image);		
		
	}

	@Test
	public void testDownloadImage() {
		
		
		AlbumArtService albumArtService = new AlbumArtServiceImpl(
				"testpath",
				new MP3FileReader() {

					@Override
					public MP3Info readInfo(File file) {
						if(!file.getName().equals("testsong.mp3"))
							throw new RuntimeException("unknown mp3file "+file.getName());						

						
							MP3Info info = new MP3Info();
							info.setAlbum("Testalbum");
							return info;
						
					}},
				new ImageFileService() {

					@Override
					public void write(BufferedImage img, String type, File file) throws BatchingException {
						
						if(img.getWidth() != 47 || img.getHeight() != 11)
							throw new RuntimeException("unknonw image");						

						if(!type.equals("png"))						
							throw new RuntimeException("type not supported "+type);
						
						if(!file.getName().equals("TestAlbumImage.png"))
							throw new RuntimeException("unknonw image" + file.getName());
						
						return;
						
					}

					@Override
					public BufferedImage read(File file) throws BatchingException {
						throw new RuntimeException("not implemented");
					}},
				new MusicBrainzService() {

					@Override
					public List<MusicBrainzReleaseEntry> queryRelease(String album) {
						
						if(!album.equals("Testalbum"))						
							throw new RuntimeException("unknown album "+album);
						
						List<MusicBrainzReleaseEntry> releases = new ArrayList<MusicBrainzReleaseEntry>();
						
						MusicBrainzReleaseEntry e = new MusicBrainzReleaseEntry();
						e.setType("album");
						e.setMbid("123456789");
						e.setTitle("TestalbumOnline");
						
						releases.add(e);
						
						return releases;
						
					}},
				new CoverArtService() {

					@Override
					public List<FrontCover> getFrontCovers(String mbid) {
						if(!mbid.equals("123456789"))						
							throw new RuntimeException("unknown mbid "+mbid);
						List<FrontCover>  list = new ArrayList<FrontCover>();
						FrontCover c = new FrontCover();
						c.setPath("http://coverartarchive.org"+"/cover.jpg");
						list.add(c);
						
						return list;
					}

					@Override
					public BufferedImage downloadImage(String path) {
						if(!path.equals("/cover.jpg"))						
							throw new RuntimeException("unknown path "+path);
						
						return new BufferedImage(47, 11, BufferedImage.TYPE_INT_ARGB);
					}},
				new StringDistance() {

					@Override
					public int calculate(CharSequence lhs, CharSequence rhs) {
						if(!lhs.equals("testalbumonline"))						
							throw new RuntimeException("unknown string "+lhs);
						if(!rhs.equals("testalbum"))						
							throw new RuntimeException("unknown string "+rhs);
						
						return 0;
					}},
				new NameService() {

					@Override
					public File createFilename(String dataPath, String artist, String album, String extension) {
						
						if(!dataPath.equals("testpath"))						
							throw new RuntimeException("unknown path "+dataPath);
						
						if(!album.equals("Testalbum"))						
							throw new RuntimeException("unknown album "+album);
						
						if(!extension.equals("png"))						
							throw new RuntimeException("extension not supported "+extension);
						
						return new File("TestAlbumImage.png");
					}}	
		);
		
		BufferedImage image = albumArtService.resolveImageForMp3(new File("testsong.mp3"));
		assertNotNull(image);		
		
	}
	
	
	
	@Test
	public void testNoCoverFound1() {
		
		
		AlbumArtService albumArtService = new AlbumArtServiceImpl(
				"testpath",
				new MP3FileReader() {

					@Override
					public MP3Info readInfo(File file) {
						if(!file.getName().equals("testsong.mp3"))
							throw new RuntimeException("unknown mp3file "+file.getName());						

						
							MP3Info info = new MP3Info();
							info.setAlbum("Testalbum");
							return info;
						
					}},
				null,
				new MusicBrainzService() {

					@Override
					public List<MusicBrainzReleaseEntry> queryRelease(String album) {
						
						if(!album.equals("Testalbum"))						
							throw new RuntimeException("unknown album "+album);
						
						return new ArrayList<MusicBrainzReleaseEntry>();
						
					}},
				null,
				null,
				new NameService() {

					@Override
					public File createFilename(String dataPath, String artist, String album, String extension) {
						
						if(!dataPath.equals("testpath"))						
							throw new RuntimeException("unknown path "+dataPath);
						
						if(!album.equals("Testalbum"))						
							throw new RuntimeException("unknown album "+album);
						
						if(!extension.equals("png"))						
							throw new RuntimeException("extension not supported "+extension);
						
						return new File("TestAlbumImage.png");
					}}	
		);
		
		BufferedImage image = albumArtService.resolveImageForMp3(new File("testsong.mp3"));
		assertNull(image);		
		
	}
	
	@Test
	public void testNoCoverFound2() {
		
		
		AlbumArtService albumArtService = new AlbumArtServiceImpl(
				"testpath",
				new MP3FileReader() {

					@Override
					public MP3Info readInfo(File file) {
						if(!file.getName().equals("testsong.mp3"))
							throw new RuntimeException("unknown mp3file "+file.getName());						

						
							MP3Info info = new MP3Info();
							info.setAlbum("Testalbum");
							return info;
						
					}},
				null,
				new MusicBrainzService() {

					@Override
					public List<MusicBrainzReleaseEntry> queryRelease(String album) {
						
						if(!album.equals("Testalbum"))						
							throw new RuntimeException("unknown album "+album);
						
						List<MusicBrainzReleaseEntry> releases = new ArrayList<MusicBrainzReleaseEntry>();
						
						MusicBrainzReleaseEntry e = new MusicBrainzReleaseEntry();
						e.setType("album");
						e.setMbid("123456789");
						e.setTitle("TestalbumOnline");
						
						releases.add(e);
						
						return releases;
						
					}},
				new CoverArtService() {

					@Override
					public List<FrontCover> getFrontCovers(String mbid) {
						if(!mbid.equals("123456789"))						
							throw new RuntimeException("unknown mbid "+mbid);
						return new ArrayList<FrontCover>();
					}

					@Override
					public BufferedImage downloadImage(String path) {
										
							throw new RuntimeException("not implemented");
					}},
				new StringDistance() {

					@Override
					public int calculate(CharSequence lhs, CharSequence rhs) {
						if(!lhs.equals("testalbumonline"))						
							throw new RuntimeException("unknown string "+lhs);
						if(!rhs.equals("testalbum"))						
							throw new RuntimeException("unknown string "+rhs);
						
						return 0;
					}},
				new NameService() {

					@Override
					public File createFilename(String dataPath, String artist, String album, String extension) {
						
						if(!dataPath.equals("testpath"))						
							throw new RuntimeException("unknown path "+dataPath);
						
						if(!album.equals("Testalbum"))						
							throw new RuntimeException("unknown album "+album);
						
						if(!extension.equals("png"))						
							throw new RuntimeException("extension not supported "+extension);
						
						return new File("TestAlbumImage.png");
					}}	
		);
		
		BufferedImage image = albumArtService.resolveImageForMp3(new File("testsong.mp3"));
		assertNull(image);		
		
	}
	
	
	
}
