package de.frittenburger.music.interfaces;

import java.awt.image.BufferedImage;
import java.util.List;

import de.frittenburger.music.bo.FrontCover;

public interface CoverArtService {

	List<FrontCover> getFrontCovers(String mbid);

	BufferedImage downloadImage(String path);

}
