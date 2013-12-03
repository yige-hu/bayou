

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
	ServerId creatorId; int TS; Set<Integer> connected_servers; Map<Integer, Integer> V;
	public CreationWriteResponse(int src, ServerId creatorId, int TS, Set<Integer> connected_servers, Map<Integer, Integer> V){
		this.src = src; this.creatorId = creatorId; this.TS = TS; this.connected_servers = connected_servers; this.V = V;
	}
}

class RetireWriteResponse extends Message {
	public RetireWriteResponse(int src) {
		this.src = src;
	}
}