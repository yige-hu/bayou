import java.net.URL;


public class Command {
	int client;
	int cmd_id;
	
	int server;
	int accept_stamp;
	int CSN = 2147483647;
	
	String type;
	String songName;
	URL url;

	public Command(int client, int cmd_id, String type, String songName, URL url){
		this.client = client;
		this.cmd_id = cmd_id;
		this.type = type;
		this.songName = songName;
		this.url = url;
	}
	
	public Command(int client, int cmd_id, String type, String songName){
		this.client = client;
		this.cmd_id = cmd_id;
		this.type = type;
		this.songName = songName;
	}

	public boolean equals(Object o) {
		Command other = (Command) o;
		return (this.client == other.client) && cmd_id == other.cmd_id;
	}

	public String toString(){
		return "Command(client=" + client + ", cmd_id=" + cmd_id + ", " +
				"server=" + server + " ,accept_stamp=" + accept_stamp + " ,CSN=" + CSN + ")";
	}
}
