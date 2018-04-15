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
import java.io.IOException;

import javax.imageio.ImageIO;

import de.frittenburger.music.bo.BatchingException;
import de.frittenburger.music.interfaces.ImageFileService;

public class ImageFileServiceImpl implements ImageFileService {

	@Override
	public void write(BufferedImage img, String type, File file) throws BatchingException {
		
		try {
			ImageIO.write(img, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BatchingException("Couldn't write image "+file.getPath());
		}

	}

	@Override
	public BufferedImage read(File file) throws BatchingException {
		
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BatchingException("Couldn't read image "+file.getPath());
		}
	}

}
