package de.frittenburger.music.bo;

public class MusicBrainzReleaseEntry {
	
	private String mbid;
	private int score;
	private String title;
	private String type;
	private String artist;

	
	
	public String getMbid() {
		return mbid;
	}
	public void setMbid(String mbid) {
		this.mbid = mbid;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	@Override
	public String toString() {
		return "MusicBrainzReleaseEntry [mbid=" + mbid + ", score=" + score + ", title=" + title + ", type=" + type
				+ ", artist=" + artist + "]";
	}
	
}
