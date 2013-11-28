import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Server extends Process {
	
	List<Integer> connected_servers = new ArrayList<Integer>();
	
	List<Command> tentative = new ArrayList<Command>();
	List<Command> committed = new ArrayList<Command>();
	PlayList playList;
	
	Map<Integer, Integer> V = new HashMap<Integer, Integer>();
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
		System.out.println("Here I am: " + me);
		for (;;) {
			while (Env.pause);
			
			Message msg = getNextMessage();

			if (msg instanceof ClientWriteMessage) {

				ClientWriteMessage m = (ClientWriteMessage) msg;
				m.command.server = me;
				m.command.accept_stamp = TS++;
				tentative.add(m.command);
				antiEntropy();
				
				// primary server
				if (me == 0) {
					m.command.CSN = CSN++;
					commitWrite(m.command);
					tentative.remove(m.command);
					committed.add(m.command);
					antiEntropy();
				}
			}
			
			else if  (msg instanceof ClientReadMessage) {
				ClientReadMessage m = (ClientReadMessage) msg;
				String songInfo = playList.getSongInfo(m.command.songName);
				System.out.println(songInfo);
			}
			
			else if  (msg instanceof CommitNotification) {
				CommitNotification m = (CommitNotification) msg;
				commitWrite(m.command);
				tentative.remove(m.command);
				committed.add(m.command);
				antiEntropy();
			}
			
			else if  (msg instanceof WriteNotification) {
				WriteNotification m = (WriteNotification) msg;
				V.put(m.src, m.command.accept_stamp);
				tentative.add(m.command);
				antiEntropy();
				
				// primary server
				if (me == 0) {
					m.command.CSN = CSN++;
					commitWrite(m.command);
					tentative.remove(m.command);
					committed.add(m.command);
					antiEntropy();
				}
			}
			
			else {
				System.err.println("Server: unknown msg type");
			}
		}
		
	}

	private void commitWrite(Command command) {
		if (command.type.equals("add")) {
			playList.addSong(command.songName, command.url);
		}
		
		else if  (command.type.equals("delete")) {
			playList.deleteSong(command.songName);
		}
		
		else if  (command.type.equals("edit")) {
			playList.editSong(command.songName, command.url);
		}
	}
	
	private void antiEntropy() {
		for (int R : connected_servers) {
			Server server = env.servers.get(R);
			// send committed writes that R does not know about
			if (server.committed.size() < this.committed.size()) {
				for (Command cmd : committed) {
					if (!server.committed.contains(cmd)) {
						if (server.tentative.contains(cmd)) {
							sendServerMessage(me, new CommitNotification(me,
									cmd));
						} else {
							sendServerMessage(me,
									new WriteNotification(me, cmd));
							sendServerMessage(me, new CommitNotification(me,
									cmd));
						}
					}
				}
			}

			// send all the tentative writes
			for (Command cmd : tentative) {
				if (server.V.get(cmd.server) < cmd.accept_stamp) {
					sendServerMessage(me, new WriteNotification(me, cmd));
				}
			}
		}
	}

	public void disconnect(int j) {
		connected_servers.add(j);		
	}

	public void connect(int j) {
		connected_servers.remove(j);
	}

	public void printLog() {
		System.out.println("Log: server " + me + "\n");
		System.out.println("commited:\n");
		for (Command cmd : committed) {
			System.out.println(cmd);
		}
		
		System.out.println("tentative:\n");
		for (Command cmd : tentative) {
			System.out.println(cmd);
		}
	}
}
