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
package de.frittenburger.music.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.frittenburger.music.bo.BatchingException;
import de.frittenburger.music.bo.FrontCover;
import de.frittenburger.music.bo.MP3Info;
import de.frittenburger.music.bo.MusicBrainzReleaseEntry;
import de.frittenburger.music.interfaces.AlbumArtService;
import de.frittenburger.music.interfaces.CoverArtService;
import de.frittenburger.music.interfaces.ImageFileService;
import de.frittenburger.music.interfaces.MP3FileReader;
import de.frittenburger.music.interfaces.MusicBrainzService;
import de.frittenburger.music.interfaces.NameService;
import de.frittenburger.music.interfaces.StringDistance;

public class AlbumArtServiceImpl implements AlbumArtService {

	private final String dataPath;
	private final MusicBrainzService mbservice;
	private final CoverArtService caservice;

	private final StringDistance distance;
	private final NameService nameService;
	private final Set<String> processed = new HashSet<String>(); // cache
	private final MP3FileReader mp3FileReader;
	private final ImageFileService imageFileService;

	public AlbumArtServiceImpl(String dataPath, MP3FileReader mp3FileReader, ImageFileService imageFileService,
			MusicBrainzService mbservice, CoverArtService caservice, StringDistance distance, NameService nameService) {
		this.dataPath = dataPath;
		this.mp3FileReader = mp3FileReader;
		this.imageFileService = imageFileService;
		this.mbservice = mbservice;
		this.caservice = caservice;
		this.distance = distance;
		this.nameService = nameService;
	}

	@Override
	public BufferedImage resolveImageForMp3(File mp3file) {

		try {

			MP3Info mp3info = mp3FileReader.readInfo(mp3file);
			if (mp3info.getImage() != null)
				return mp3info.getImage();

			File file = nameService.createFilename(dataPath, mp3info.getArtist(), mp3info.getAlbum(), "png");

			if (file.exists()) {
				return imageFileService.read(file);
			}

			// ohne Erfolg bereits versucht (reduce requests)
			if (processed.contains(file.getName()))
				return null;
			processed.add(file.getName());

			// try to get Image from music online service
			List<MusicBrainzReleaseEntry> entries = mbservice.queryRelease(mp3info.getAlbum());

			// filter and reorder
			String mbid = null;
			int d = 1000;
			for (MusicBrainzReleaseEntry e : entries) {
				if (!e.getType().toLowerCase().equals("album"))
					continue;
				System.out.println(e);
				int dist = distance.calculate(e.getTitle().toLowerCase(), mp3info.getAlbum().toLowerCase());
				if (dist < d) {
					mbid = e.getMbid();
					d = dist;
				}
			}

			if (mbid == null) {
				System.out.println("No cover found for " + mp3info.getAlbum());
				return null;
			}

			List<FrontCover> covers = caservice.getFrontCovers(mbid);
			for (FrontCover c : covers)
				System.out.println(c);

			
			if (covers.size() == 0) {
				System.out.println("No cover found for " + mbid);
				return null;
			}
			
			String imgpath = covers.get(0).getPath();

			// cut path
			imgpath = imgpath.substring("http://coverartarchive.org".length());

			BufferedImage img = caservice.downloadImage(imgpath);

			if (img != null) {
				imageFileService.write(img, "png", file);
			}
			return img;

		} catch (BatchingException e) {
			e.printStackTrace();
			return null;
		}

	}

}
