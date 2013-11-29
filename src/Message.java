

import java.util.*;

public class Message {
	int src;
}

class ClientWriteMessage extends Message {
	Command command;
	public ClientWriteMessage(int src, Command command){
		this.src = src; this.command = command;
	}
}

class ClientReadOnlyMessage extends Message {
	Command command;
	public ClientReadOnlyMessage(int src, Command command){
		this.src = src; this.command = command;
	}
}

class ClientWriteOnlyMessage extends Message {
	Command command;
	public ClientWriteOnlyMessage(int src, Command command){
		this.src = src; this.command = command;
	}
}

class CommitNotification extends Message {
	Command command;
	public CommitNotification(int src, Command command){
		this.src = src; this.command = command;
	}
}

class WriteNotification extends Message {
	Command command;
	public WriteNotification(int src, Command command){
		this.src = src; this.command = command;
	}
}

class TSResponseMessage extends Message {
	int TS;
	public TSResponseMessage(int src, int TS){
		this.src = src; this.TS = TS;
	}
}

class CreationWriteMessage extends Message {
	Command command;
	public CreationWriteMessage(int src, Command command){
		this.src = src; this.command = command;
	}
}

class CreationWriteResponse extends Message {
	int TS; Set<Integer> connected_servers;
	public CreationWriteResponse(int src, int TS, Set<Integer> connected_servers){
		this.src = src; this.TS = TS; this.connected_servers = connected_servers;
	}
}