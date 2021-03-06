import java.net.URL;


public class Command implements Comparable {
	int client;
	int cmd_id;
	
	int server;
	int accept_stamp;
	int CSN = Integer.MAX_VALUE;
	
	String type;
	String songName;
	URL url;
	
	ServerId serverId;

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
	
	public Command(String type, int server, ServerId serverId) {
		this.type = type;
		this.server = server;
		this.serverId = serverId;
	}

	public boolean equals(Object o) {
		Command other = (Command) o;
		return (this.client == other.client) && cmd_id == other.cmd_id;
	}

	public String toString(){
		String s = "Command(";
		if (type.equals("create") || type.equals("retire")) {
			s += "'" + type + " server" + server + "', ServerId=" + serverId + ", accept_stamp="
					+ accept_stamp + " ,CSN=" + CSN + ")";
		} else {
			s += "client=" + client + ", cmd_id=" + cmd_id + ", server="
					+ server + ", ServerId=" + serverId + ", accept_stamp=" + accept_stamp + ", CSN="
					+ CSN + " '" + type + " " + songName;
			if (url != null)
				s += " " + url.toString();
			s += "')";
		}
		return s;
	}

	@Override
	public int compareTo(Object o) {
		Command other = (Command) o;
		if (this.CSN != other.CSN) {
			return this.CSN - other.CSN;
		} else if (this.server != other.server) {
			return this.server - other.server;
		} else {
			return this.accept_stamp - other.accept_stamp;
		}
	}
}
