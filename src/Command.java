
public class Command {
	int client;
	int cmd_id;
	
	String type;
	String songName;
	String url;

	public Command(String type, String songName, String url){
		this.type = type;
		this.songName = songName;
		this.url = url;
	}
	
	public Command(String type, String songName){
		this.type = type;
		this.songName = songName;
	}

	public boolean equals(Object o) {
		Command other = (Command) o;
		return (this.client == other.client) && cmd_id == other.cmd_id;
	}

	public String toString(){
		return "Command(" + client + ", " + cmd_id + ")";
	}
}
