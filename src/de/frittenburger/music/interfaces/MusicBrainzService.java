package de.frittenburger.music.interfaces;

import java.util.List;

import de.frittenburger.music.bo.MusicBrainzReleaseEntry;

public interface MusicBrainzService {

	List<MusicBrainzReleaseEntry> queryRelease(String album);

}
