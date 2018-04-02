package de.frittenburger.music.impl;

import java.io.File;

import de.frittenburger.music.interfaces.NameService;

public class NameServiceImpl implements NameService {

	@Override
	public File createFilename(String dataPath, String artist, String album, String extension) {
		return 	 new File(dataPath + (artist+"_"+album).toLowerCase().replaceAll("\\W+", "_") +"."+extension);
	}

}
