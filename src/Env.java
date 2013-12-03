
import java.util.*;

public class Env {
	Map<Integer, Server> servers = new HashMap<Integer, Server>();
	Map<Integer, Client> clients = new HashMap<Integer, Client>();
	Map<Integer, ServerStateResponder> server_state_reponders = new HashMap<Integer, ServerStateResponder>();
	
	public final static int nInitServers = 3;
	
	public static boolean pause = false;
	
	public static final boolean DEBUG = false;
	public static final boolean DEBUG_RETIREMENT = false;
	public static final boolean SLOW_MODE = false;
	
	private static boolean TEST_1 = false;
	

	synchronized void sendServerMessage(int dst, Message msg){
		Process p = servers.get(dst);
		if (p != null) {
			p.deliver(msg);
		} else {
			System.out.println("server not exists: server" + dst);
		}
	}
	
	synchronized void sendClientMessage(int dst, Message msg){
		Process p = clients.get(dst);
		if (p != null) {
			p.deliver(msg);
		}
	}
	
	synchronized void sendStateReqMessage(int dst, Message msg){
		Process p = server_state_reponders.get(dst);
		if (p != null) {
			p.deliver(msg);
		}
	}
	
	synchronized public void addServerCreation(int pid, Server proc, int creator) {
		servers.put(pid, proc);
		proc.creationWrite(creator);
	}
	
	void sendServerCreateRespMessage(int dst, Message msg){
		Process p = servers.get(dst);
		if (p != null) {
			p.deliver(msg);
		} else {
			System.out.println("server not exists: server" + dst);
		}
	}
	
	synchronized void addServer(int pid, Server proc){
		servers.put(pid, proc);
		proc.start();
	}
	
	synchronized void removeServer(int pid){
		servers.remove(pid);
	}
	
	synchronized void addServerStateResponder(int pid, ServerStateResponder proc){
		server_state_reponders.put(pid, proc);
		proc.start();
	}
	
	synchronized void removeServerStateResponder(int pid){
		server_state_reponders.remove(pid);
	}
	
	synchronized void addClient(int pid, Client proc) {
		clients.put(pid, proc);
		proc.start();
	}
	
	synchronized void removeClient(int pid){
		clients.remove(pid);
	}

	void run(String[] args){
		
		// default 3 servers
		Server s0 = new Server(this, 0);

		join(1);

		join(2);
		
		// default 1 client, connected to Server0
		Client c = new Client(this, this.clients.size());
		
		CmdReader reader = new CmdReader(this);
		reader.run();
		
		if (TEST_1) {

		}
		
		
	}

	public static void main(String[] args){
		new Env().run(args);
	}
	
	void isolate(int i) {
		for (int j : servers.keySet()) {
			breakConnection(i, j);
		}
	}
	
	void reconnect(int i) {
		for (int j : servers.keySet()) {
			recoverConnection(i, j);
		}
	}
	
	void breakConnection(int i, int j) {
		Server s1 = servers.get(i);
		Server s2 = servers.get(j);
		s1.disconnect(j);
		s2.disconnect(i);
		System.out.println("breakConnection " + i + " " + j);
	}
	
	void recoverConnection(int i, int j) {
		Server s1 = servers.get(i);
		Server s2 = servers.get(j);
		s1.connect(j);
		s2.connect(i);
		System.out.println("Connect " + i + " " + j);
	}

	public void printLog() {
		for (Server s : servers.values()) {
			s.printLog();
		}
		for (Client c : clients.values()) {
			c.printLog();
		}
	}

	public void printLog(int server) {
		Server s = servers.get(server);
		s.printLog();
	}

	public void join(int server) {
		Server s = new Server(this, server, 0);
	}

	public void leave(int server) {
		Server s = servers.get(server);
		s.retire();
	}

}
