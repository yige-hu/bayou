import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class Server extends Process {
	
	Set<Integer> connected_servers = new HashSet<Integer>();
	
	Set<Command> tentative = new TreeSet<Command>();
	Set<Command> committed = new TreeSet<Command>();
	
	PlayList playList = new PlayList();
	
	Map<Integer, Integer> V = new HashMap<Integer, Integer>();
	Map<Integer, Integer> V_commit = new HashMap<Integer, Integer>();
	int TS = 0;
	int CSN = 0;


	public Server(Env env, int me) {
		this.env = env;
		this.me = me;
		
		env.addServer(me, this);
	}

	public void run(){
		body();
		env.removeServer(me);
	}
	
	@Override
	void body() {
		System.out.println("Here I am: server" + me);
		for (;;) {
			while (Env.pause);
			
			Message msg = getNextMessage();

			if (msg instanceof ClientWriteMessage) {
				
				ClientWriteMessage m = (ClientWriteMessage) msg;
				m.command.server = me;
				m.command.accept_stamp = (++TS);
				V.put(me, TS);
				tentative.add(m.command);
				
				sendClientMessage(m.src, new WidResponseMessage(me, TS));
				
				antiEntropy();
				
				// primary server write stable
				if (me == 0) {
					m.command.CSN = CSN++;
					commit(m.command);
					tentative.remove(m.command);
					committed.add(m.command);
					V_commit.put(m.command.server, m.command.accept_stamp);
					antiEntropy();
				}
			}
			
			else if (msg instanceof ClientWriteOnlyMessage) {
				
				ClientWriteOnlyMessage m = (ClientWriteOnlyMessage) msg;
				m.command.server = me;
				m.command.accept_stamp = (++TS);
				V.put(me, TS);
				tentative.add(m.command);
				
				sendClientMessage(m.src, new WidResponseMessage(me, TS));
				
				antiEntropy();
				
				// primary server write stable
				if (me == 0) {
					m.command.CSN = CSN++;
					commit(m.command);
					tentative.remove(m.command);
					committed.add(m.command);
					V_commit.put(m.command.server, m.command.accept_stamp);
					antiEntropy();
				}
			}
			
			else if  (msg instanceof ClientReadOnlyMessage) {
				ClientReadOnlyMessage m = (ClientReadOnlyMessage) msg;
				commit(m.command);
			}
			
			else if  (msg instanceof CommitNotification) {
				CommitNotification m = (CommitNotification) msg;
				commit(m.command);
				tentative.remove(m.command);
				committed.add(m.command);
				V_commit.put(m.command.server, m.command.accept_stamp);
				antiEntropy();
			}
			
			else if  (msg instanceof WriteNotification) {
				WriteNotification m = (WriteNotification) msg;
				
				if (V.get(m.command.server) != null && V.get(m.command.server) >= m.command.accept_stamp) {
					continue;
				}

				if (Env.DEBUG) {
					System.out
							.println("Server" + me
									+ " get WriteNotification src=" + m.src
									+ " acc_stamp=" + m.command.accept_stamp
									+ " V[" + m.command.server + "]="
									+ V.get(m.command.server));
				}
				
				V.put(m.command.server, m.command.accept_stamp);
				tentative.add(m.command);
				antiEntropy();
				
				// primary server write stable
				if (me == 0) {			
					m.command.CSN = CSN++;
					commit(m.command);
					tentative.remove(m.command);
					committed.add(m.command);
					V_commit.put(m.command.server, m.command.accept_stamp);
					antiEntropy();
				}
			}
			
			else {
				System.err.println("Server: unknown msg type");
			}
		}
		
	}

	private void commit(Command command) {
		try {
			if (command.type.equals("add")) {
				playList.addSong(command.songName, command.url);
			} else if (command.type.equals("delete")) {
				playList.deleteSong(command.songName);
			} else if (command.type.equals("edit")) {
				playList.editSong(command.songName, command.url);
			} else if (command.type.equals("get")) {
				String songInfo = playList.getSongInfo(command.songName);
				System.out.println("Get response: " + songInfo);
			}
		} catch (Exception e) {
			System.out.println("Exception when committing cmd='"
					+ command + ", " + e.toString());
		}
	}
	
	synchronized private void antiEntropy() {
		for (int R : connected_servers) {
			if (R == me) continue;
			
			Server server = env.servers.get(R);
			
			// send committed writes that R does not know about
			if (server.committed.size() < this.committed.size()) {
				if (Env.DEBUG) {
					System.out.println(">>>antiEntropy commit, from " + me
							+ ", to " + R);
				}
				for (Command cmd : committed) {
					if (!server.committed.contains(cmd)) {
						if (server.tentative.contains(cmd)) {
							sendServerMessage(R, new CommitNotification(me,
									cmd));
						} else {
							sendServerMessage(R,
									new WriteNotification(me, cmd));
							sendServerMessage(R, new CommitNotification(me,
									cmd));
						}
					}
				}
			}
			
			// send all the tentative writes
			for (Command cmd : tentative) {
				Integer VC = server.V.get(cmd.server);
				if ((cmd.server != R) && (VC == null || VC < cmd.accept_stamp)) {
					if (Env.DEBUG) {
						System.out.println(">>>antiEntropy tentative, from "
								+ me + ", to " + R + "***** VC=" + VC
								+ ", acc_stp=" + cmd.accept_stamp);
					}
					sendServerMessage(R, new WriteNotification(me, cmd));
				}
			}
		}
	}

	synchronized public void disconnect(int j) {
		if (connected_servers.contains(j)) {
			connected_servers.remove(j);
		}
	}

	synchronized public void connect(int j) {
		connected_servers.add(j);
		antiEntropy();
	}

	public void printLog() {
		System.out.println("Log: server" + me);
		System.out.println("\tcommited:");
		for (Command cmd : committed) {
			System.out.println("\t" + cmd);
		}
		
		System.out.println("\ttentative:");
		for (Command cmd : tentative) {
			System.out.println("\t" + cmd);
		}
	}
}
