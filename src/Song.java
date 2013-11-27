

import java.net.URL;

public class Song {

	private String songName;
	private URL url;
	
	public Song(String songName, URL url) {
		this.songName = songName;
		this.url = url;
	}
	
	public void setURL(URL url) {
		this.url = url;
	}
	
}
