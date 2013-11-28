import java.util.HashMap;
import java.util.Map;

public class Client extends Process {
	
	int server;
	int num_cmd = 0;
	
	Map<Integer, Integer> write_vector = new HashMap<Integer, Integer>();
	
	public Client(Env env, int me) {
		this.env = env;
		this.me = me;
		this.server = 0;
		
		env.addClient(me, this);
	}
	
	public Client(Env env, int me, int server) {
		this.env = env;
		this.me = me;
		this.server = server;
		
		env.addClient(me, this);
	}
	
	public void run(){
		body();
		env.removeClient(me);
	}

	@Override
	void body() {
		System.out.println("Here I am: client" + me + " , connected with server" + server);
		for (;;) {	}
		
	}

	public void writeRequest(Command cmd) {
		sendServerMessage(server, new ClientWriteMessage(me, cmd));
		Message msg = getNextMessage();
		if (msg instanceof WidResponseMessage) {
			WidResponseMessage m = (WidResponseMessage) msg;
			write_vector.put(server, m.TS);
		} else {
			System.out.println("Client" + me + ": invalid WidResponseMessage from server" + server);
		}
	}

	public void readOnlyRequest(Command cmd) {
		boolean RYW = true;
		Server s = env.servers.get(server);
		for (int key : write_vector.keySet()) {
			if (s.V.get(key) == null || write_vector.get(key) > s.V.get(key)) {
				RYW = false;
			}
		}
		if (RYW) {
		sendServerMessage(server, new ClientReadOnlyMessage(me, cmd));
		} else {
			System.out.println("RYW=false, please read later.");
		}
	}

	public void writeOnlyRequest(Command cmd) {
		sendServerMessage(server, new ClientWriteOnlyMessage(me, cmd));
		Message msg = getNextMessage();
		if (msg instanceof WidResponseMessage) {
			WidResponseMessage m = (WidResponseMessage) msg;
			write_vector.put(server, m.TS);
		} else {
			System.out.println("Client" + me + ": invalid WidResponseMessage from server" + server);
		}
	}
	
	public void printLog() {
		System.out.println("Status: client" + me + " connected with server" + server);
	}

}
