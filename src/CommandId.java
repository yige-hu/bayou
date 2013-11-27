
public class CommandId implements Comparable {
	int server;
	int cid;

	public CommandId(int server, int cid){
		this.server = server;
		this.cid = cid;
	}

	public boolean equals(Object other){
		return compareTo(other) == 0;
	}

	public int compareTo(Object other){
		CommandId c = (CommandId) other;
		if (c.server != this.server) {
			return c.server - this.server;
		}
		return c.cid - this.cid;
	}

	public String toString(){
		return "CMD(" + server + ", " + cid + ")";
	}
}
