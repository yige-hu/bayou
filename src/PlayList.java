import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class PlayList {
	
	Map<String, Song> songs = new HashMap<String, Song>();
	
	public void addSong(String songName, URL url) {
		Song s = new Song(songName, url);
		songs.put(songName, s);
	}
	
	public void deleteSong(String songName) {
		if (songs.remove(songName) == null) {
			System.out.println("Error: '" + songName + "' not exists");
		}
	}
	
	public void editSong(String songName, URL url) {
		Song s= songs.get(songName);
		if (s == null) {
			System.out.println("Error: '" + songName + "' not exists");
		}
		s.setURL(url);
	}
	
	public String getSongInfo(String songName) {
		Song s= songs.get(songName);
		if (s == null) {
			System.out.println("Error: '" + songName + "' not exists");
		}
		return s.toString();
	}

}
