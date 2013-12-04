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
	
	ServerId serverId;
	boolean active = true;


	public Server(Env env, int me) {
		this.env = env;
		this.me = me;
		this.connected_servers.add(me);
		this.V.put(me, TS);
		
		this.serverId = new ServerId(null, TS, me);
		
		env.addServer(me, this);
	}
	
	public Server(Env env, int me, int creator) {
		this.env = env;
		this.me = me;
		connected_servers.add(me);
		
		env.addServerCreation(me, this, creator);
	}

	public void run(){
		body();
		env.removeServer(me);
	}

	@Override
	void body() {
		System.out.println("Here I am: server" + me);
		for (;;) {
			
			if (!active) {
				System.out.println("server" + me + ": active=false, enter retirement.");
				
				Message msg;
				while (! ((msg = getNextMessage()) instanceof RetireWriteResponse));
				RetireWriteResponse m = (RetireWriteResponse) msg;
				
				// primary server write stable
				if (me == 0) {
					m.command.CSN = (++CSN);
					commit(m.command);
					tentative.remove(m.command);
					committed.add(m.command);
					V_commit.put(m.command.server, m.command.accept_stamp);
					antiEntropy();
				}
				
				env.removeServer(me);
				
				break;
			}
						
			while (Env.pause);
			
			if (Env.SLOW_MODE) {
				try {
				    Thread.sleep(1000);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			}
			
			Message msg = getNextMessage();

			if (msg instanceof ClientWriteMessage) {
				
				ClientWriteMessage m = (ClientWriteMessage) msg;
				m.command.server = me;
				m.command.serverId = serverId;
				m.command.accept_stamp = (++TS);
				V.put(me, TS);
				tentative.add(m.command);
				
				sendClientMessage(m.src, new TSResponseMessage(me, TS));
				
				antiEntropy();
				
				// primary server write stable
				if (me == 0) {
					m.command.CSN = (++CSN);
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
				m.command.serverId = serverId;
				m.command.accept_stamp = (++TS);
				V.put(me, TS);
				tentative.add(m.command);
				
				sendClientMessage(m.src, new TSResponseMessage(me, TS));
				
				antiEntropy();
				
				// primary server write stable
				if (me == 0) {
					m.command.CSN = (++CSN);
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
                
                if (Env.DEBUG_RETIREMENT) {
                    if (m.command.type.equals("retire")) {
                            System.out.println("server" + me
                                            + " get retirement serverId="
                                            + m.command.serverId + ", acc_stp="
                                            + m.command.accept_stamp + ", V[Si]="
                                            + V.get(m.command.server) + " CompleteV[Sk]="
                                            + getCompleteV(m.command.serverId.Sk));
                    }
                }
				
				// original version vector: V
				//if (V.get(m.command.server) != null && V.get(m.command.server) >= m.command.accept_stamp) {
				
				if (Env.DEBUG_RETIREMENT) {
					if (m.command.type.equals("retire")) {
						System.out.println("server" + me
								+ " get retirement serverId="
								+ m.command.serverId + ", acc_stp="
								+ m.command.accept_stamp + ", V[Si]="
								+ V.get(m.command.server) + " CompleteV[Sk]="
								+ getCompleteV(m.command.serverId.Sk));
					}
				}
				
				// complete version vector: CompleteV
				if (getCompleteV(m.command.serverId) >= m.command.accept_stamp) {
					if (Env.DEBUG) {
						System.out.println("Server" + me
								+ " skip WriteNotification src=" + m.src + " cmd.server=" + m.command.server
								+ " acc_stamp=" + m.command.accept_stamp
								+ " V[" + m.command.server + "]="
								+ V.get(m.command.server)
								+ " completeV=" + getCompleteV(m.command.serverId) + " cmd=" + m.command);
					}
					continue;
				}

				if (Env.DEBUG) {
					System.out.println("Server" + me + " accept WriteNotification " +  " V[" + m.command.server + "]="
							+ V.get(m.command.server) + " completeV=" + getCompleteV(m.command.serverId) + " cmd=" + m.command);
				}
				
				V.put(m.command.server, m.command.accept_stamp);
				tentative.add(m.command);
				
				antiEntropy();
				
				// primary server write stable
				if (me == 0) {			
					m.command.CSN = (++CSN);
					commit(m.command);
					tentative.remove(m.command);
					committed.add(m.command);
					V_commit.put(m.command.server, m.command.accept_stamp);
					antiEntropy();
				}
				
				// if retirement request, respond
                if (m.command.type.equals("retire")) {
                        sendServerMessage(m.command.server, new RetireWriteResponse(me, m.command));
                }
			}
			
			else if (msg instanceof CreationWriteMessage) {
				
				CreationWriteMessage m = (CreationWriteMessage) msg;
				m.command.accept_stamp = (++TS);
				V.put(me, TS);
				V.put(m.src, 0);
				m.command.serverId = this.serverId;
				tentative.add(m.command);
				
				this.connected_servers.add(m.src);
				sendServerMessage(m.src, new CreationWriteResponse(me, serverId, TS, connected_servers, this.V));
				
				antiEntropy();
				
				// primary server write stable
				if (me == 0) {
					m.command.CSN = (++CSN);
					commit(m.command);
					tentative.remove(m.command);
					committed.add(m.command);
					V_commit.put(m.command.server, m.command.accept_stamp);
					antiEntropy();
				}
				
			}
			
			else if (msg instanceof RetireWriteResponse) {
				RetireWriteResponse m = (RetireWriteResponse) msg;
				
				// primary server write stable
				if (me == 0) {
					m.command.CSN = (++CSN);
					commit(m.command);
					tentative.remove(m.command);
					committed.add(m.command);
					V_commit.put(m.command.server, m.command.accept_stamp);
					antiEntropy();
				}
				
				env.removeServer(me);
				
				break;
			}
			
			else {
				System.err.println("Server" + me + ": unknown msg type - " + msg.getClass());
			}
		}
		
	}

	private void commit(Command command) {
		try {
			if (command.type.equals("create")) {
				this.connected_servers.add(command.server);
				this.V.put(command.client, 0);
			} else if (command.type.equals("retire")) {
				this.connected_servers.remove(command.server);
				this.V.remove(command.server);
			} else if (command.type.equals("add")) {
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
			
			if (server == null) continue;
			
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
								+ me + ", to " + R + " ----- VC=" + VC
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
		System.out.println("Log: server" + me + ", ServerId=" + serverId);
		System.out.println("\tcommited:");
		for (Command cmd : committed) {
			System.out.println("\t" + cmd);
		}
		
		System.out.println("\ttentative:");
		for (Command cmd : tentative) {
			System.out.println("\t" + cmd);
		}
	}
	
	public void printList() {
		System.out.println("PlayList: server" + me + ", ServerId=" + serverId);
		playList.print();
	}
	
	public void printV() {
		System.out.println("PlayList: server" + me + ", ServerId=" + serverId + ", TS=" + TS);
		for (int s : V.keySet()) {
			System.out.println("\tV[" + s + "]=" + V.get(s));
		}
	}

	public void creationWrite(int creator) {
		Command cmd = new Command("create", creator, me);
		sendServerMessage(creator, new CreationWriteMessage(me, cmd));
		Message msg = getNextMessage();
		if (msg instanceof CreationWriteResponse) {
			CreationWriteResponse m = (CreationWriteResponse) msg;
			this.connected_servers.addAll(m.connected_servers);
			this.serverId = new ServerId(m.creatorId, m.TS, me);
			this.V.putAll(m.V);
			this.TS = m.TS;
			
			this.start();
		} else {
			System.out.println("Server" + me
					+ ": invalid CreationWriteResponse from server" + msg.src
					+ " msg type=" + msg.getClass());
		}
	}

	public void retire() {
		this.active = false;
		retirementWrite();
	}
	
	
	private void retirementWrite() {
		Command cmd = new Command("retire", me, serverId);
		cmd.server = me;
		cmd.serverId = serverId;
		cmd.accept_stamp = (++TS);
		
		V.put(me, TS);
		tentative.add(cmd);
		
		antiEntropy();
	}
	
	private int getCompleteV(ServerId serverId) {
		if (Env.DEBUG) {
			if (serverId != null) {
				System.out.println("Server" + me + ": V[" + serverId.num + "]=" + V.get(serverId.num) + ", Tki=" + serverId.Tki);
			}
		}
		if (serverId != null && V.containsKey(serverId.num)) {
			if (Env.DEBUG) {
				System.out.println("case 1");
			}
			return V.get(serverId.num);
		} else if (serverId == null) {
			if (Env.DEBUG) {
				System.out.println("case 2");
			}
			return Integer.MAX_VALUE;
		} else if (getCompleteV(serverId.Sk)>=serverId.Tki) {
			if (Env.DEBUG) {
				System.out.println("case 3");
			}
			return Integer.MAX_VALUE;
		} else {
			if (Env.DEBUG) {
				System.out.println("case 4");
			}
			return Integer.MIN_VALUE;
		}
	}
}
