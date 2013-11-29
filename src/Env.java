
import java.util.*;

public class Env {
	Map<Integer, Server> servers = new HashMap<Integer, Server>();
	Map<Integer, Client> clients = new HashMap<Integer, Client>();
	
	public final static int nInitServers = 3;
	
	public static boolean pause = false;
	
	public final static boolean DEBUG = false;
	
	private static boolean TEST_1 = false;
	
	

	synchronized void sendServerMessage(int dst, Message msg){
		Process p = servers.get(dst);
		if (p != null) {
			p.deliver(msg);
		}
	}
	
	synchronized void sendClientMessage(int dst, Message msg){
		Process p = clients.get(dst);
		if (p != null) {
			p.deliver(msg);
		}
	}

	synchronized void addServer(int pid, Server proc){
		servers.put(pid, proc);
		proc.start();
	}

	synchronized void removeServer(int pid){
		servers.remove(pid);
	}
	
	public void addClient(int pid, Client proc) {
		clients.put(pid, proc);
		proc.start();
	}
	
	synchronized void removeClient(int pid){
		clients.remove(pid);
	}

	void run(String[] args){

		// default 3 servers
		for (int i = 0; i < nInitServers; i++) {
			Server s = new Server(this, i);
		}
		
		for (int i = 0; i < nInitServers; i++) {
			for (int j = i; j < nInitServers; j++) {
				recoverConnection(i, j);
			}
		}
		
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
		Server s = new Server(this, server);
		// TODO new server join
	}

	public void leave(int server) {
		// TODO server retirement
		
	}
}
