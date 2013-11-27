
import java.util.*;

public class Env {
	Map<Integer, Server> servers = new HashMap<Integer, Server>();
	Map<Integer, Client> clients = new HashMap<Integer, Client>();
	//public final static int nAcceptors = 3, nReplicas = 2, nLeaders = 2, nRequests = 10;
	
	public final static int nInitServers = 3;
	
	public static boolean pause = false;
	
	private static boolean TEST_1 = true;

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

		for (int i = 0; i < nInitServers; i++) {
			Server s = new Server(this, i);
		}
		
		for (int i = 0; i < nInitServers; i++) {
			for (int j = i; j < nInitServers; j++) {
				recoverConnection(i, j);
			}
		}
		
		CmdReader reader = new CmdReader(this);
		reader.run();
		
		if (TEST_1) {
//			sendMessage(clients[0], new ClientMessage("addBankClient 1"));
//			sendMessage(clients[1], new ClientMessage("addBankClient 2 Jim"));
//			sendMessage(clients[2], new ClientMessage("addBankClient 3 Lily"));
//			sendMessage(clients[3], new ClientMessage("addBankClient 4"));
//			
//			sendMessage(clients[0], new ClientMessage("createAccount 1 1 100"));
//			sendMessage(clients[0], new ClientMessage("createAccount 1 2"));
//			sendMessage(clients[1], new ClientMessage("createAccount 2 1 200"));
//			sendMessage(clients[2], new ClientMessage("createAccount 3 1 50"));
//			
//			sendMessage(clients[0], new ROCClientMessage("inquiry 1 1"));
//			
//			sendMessage(clients[0], new ClientMessage("deposit 1 1 150"));
//			
//			sendMessage(clients[0], new ROCClientMessage("inquiry 1 1"));
//			sendMessage(clients[0], new ROCClientMessage("inquiry 1 1"));
//			sendMessage(clients[0], new ROCClientMessage("inquiry 1 1"));
//			
//			sendMessage(clients[0], new ClientMessage("deposit 1 2 300"));
//			sendMessage(clients[2], new ClientMessage("deposit 3 1 225"));
//			
//			sendMessage(clients[0], new ClientMessage("withdraw 1 1 30"));
//			sendMessage(clients[1], new ClientMessage("withdraw 2 1 45"));
//			
//			sendMessage(clients[0], new ClientMessage("transfer 1 1 2 30"));
//			sendMessage(clients[1], new ClientMessage("transfer 2 1 1 42 3"));
//			
//			sendMessage(clients[1], new ROCClientMessage("inquiry 1 2"));
//			sendMessage(clients[2], new ROCClientMessage("inquiry 2 1"));
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
	}
	
	void recoverConnection(int i, int j) {
		Server s1 = servers.get(i);
		Server s2 = servers.get(j);
		s1.connect(j);
		s2.connect(i);
	}

	public void printLog() {
		for (Server s : servers.values()) {
			s.printLog();
		}
	}

	public void printLog(int server) {
		Server s = servers.get(servers);
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
