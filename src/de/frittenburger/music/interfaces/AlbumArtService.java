package de.frittenburger.music.interfaces;

import java.awt.image.BufferedImage;
import java.io.File;

public interface AlbumArtService {
		
	BufferedImage resolveImageForMp3(File mp3file);
	
}
