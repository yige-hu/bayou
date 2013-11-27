
import java.util.*;

public class Env {
	Map<ProcessId, Process> procs = new HashMap<ProcessId, Process>();
	//public final static int nAcceptors = 3, nReplicas = 2, nLeaders = 2, nRequests = 10;
public final static int nAcceptors = 5, nReplicas = 5, nLeaders = 5, nClients = 4;
	
	private boolean TEST_1 = true;
	boolean TEST_LEADER01DIE = false;
	boolean TEST_NETWORK_PARTITION = false;

	synchronized void sendMessage(ProcessId dst, PaxosMessage msg){
		Process p = procs.get(dst);
		if (p != null) {
			p.deliver(msg);
		}
	}

	synchronized void addProc(ProcessId pid, Process proc){
		procs.put(pid, proc);
		proc.start();
	}

	synchronized void removeProc(ProcessId pid){
		procs.remove(pid);
	}

	void run(String[] args){
		ProcessId[] acceptors = new ProcessId[nAcceptors];
		ProcessId[] replicas = new ProcessId[nReplicas];
		ProcessId[] leaders = new ProcessId[nLeaders];
		ProcessId[] clients = new ProcessId[nClients];

		for (int i = 0; i < nAcceptors; i++) {
			acceptors[i] = new ProcessId("acceptor:" + i);
			Acceptor acc = new Acceptor(this, acceptors[i]);
		}
		for (int i = 0; i < nReplicas; i++) {
			replicas[i] = new ProcessId("replica:" + i);
			Replica repl = new Replica(this, replicas[i], leaders, clients);
		}
		for (int i = 0; i < nLeaders; i++) {
			leaders[i] = new ProcessId("leader:" + i);
			Leader leader = new Leader(this, leaders[i], acceptors, replicas);
		}
		
		for (int i = 0; i < nClients; i++) {
			clients[i] = new ProcessId("client:" + i);
			Client client = new Client(this, clients[i], replicas, i);
		}
		
		Debugger debugger = new Debugger(this, leaders);
		debugger.start();
		
		if (TEST_1) {
			sendMessage(clients[0], new ClientMessage("addBankClient 1"));
			sendMessage(clients[1], new ClientMessage("addBankClient 2 Jim"));
			sendMessage(clients[2], new ClientMessage("addBankClient 3 Lily"));
			sendMessage(clients[3], new ClientMessage("addBankClient 4"));
			
			sendMessage(clients[0], new ClientMessage("createAccount 1 1 100"));
			sendMessage(clients[0], new ClientMessage("createAccount 1 2"));
			sendMessage(clients[1], new ClientMessage("createAccount 2 1 200"));
			sendMessage(clients[2], new ClientMessage("createAccount 3 1 50"));
			
			sendMessage(clients[0], new ROCClientMessage("inquiry 1 1"));
			
			sendMessage(clients[0], new ClientMessage("deposit 1 1 150"));
			
			sendMessage(clients[0], new ROCClientMessage("inquiry 1 1"));
			sendMessage(clients[0], new ROCClientMessage("inquiry 1 1"));
			sendMessage(clients[0], new ROCClientMessage("inquiry 1 1"));
			
			sendMessage(clients[0], new ClientMessage("deposit 1 2 300"));
			sendMessage(clients[2], new ClientMessage("deposit 3 1 225"));
			
			sendMessage(clients[0], new ClientMessage("withdraw 1 1 30"));
			sendMessage(clients[1], new ClientMessage("withdraw 2 1 45"));
			
			sendMessage(clients[0], new ClientMessage("transfer 1 1 2 30"));
			sendMessage(clients[1], new ClientMessage("transfer 2 1 1 42 3"));
			
			sendMessage(clients[1], new ROCClientMessage("inquiry 1 2"));
			sendMessage(clients[2], new ROCClientMessage("inquiry 2 1"));
		}
		
		
	}

	public static void main(String[] args){
		new Env().run(args);
	}
}
