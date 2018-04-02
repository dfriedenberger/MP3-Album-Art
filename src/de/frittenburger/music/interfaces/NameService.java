package de.frittenburger.music.interfaces;

import java.io.File;

public interface NameService {

	File createFilename(String dataPath, String artist, String album, String extension);

}
