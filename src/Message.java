

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