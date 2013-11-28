import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.StringTokenizer;

public class CmdReader extends Thread {
	
	private Env env;

	public CmdReader(Env env) {
		this.env = env;
	}

	public void run() {
		
		Reader r = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(r);
		String command = null;
		
		for (;;) {
			System.out.println("Input command:\n");
			try {
				command = br.readLine();
				break;
			} catch (IOException e1) {
				System.out.println("Readline error: " + e1);
			}
			
			try {
				StringTokenizer t = new StringTokenizer(command, " ");
				
				String type = t.nextToken();
				
				if (type.equals("pause")) {
					Env.pause = true;
				}
				
				else if (type.equals("continue")) {
					Env.pause = false;
				}
				
				else if (type.equals("printLog")) {
					if (t.hasMoreTokens()) {
						int server = Integer.parseInt(t.nextToken());
						env.printLog(server);
					} else {
						env.printLog();
					}
				}
				
				else if (type.equals("isolate")) {
					int server = Integer.parseInt(t.nextToken());
					env.isolate(server);
				}
				
				else if (type.equals("reconnect")) {
					int server = Integer.parseInt(t.nextToken());
					env.reconnect(server);
				}
				
				else if (type.equals("breakConnection")) {
					int server1 = Integer.parseInt(t.nextToken());
					int server2 = Integer.parseInt(t.nextToken());
					env.breakConnection(server1, server2);
				}
				
				else if (type.equals("recoverConnection")) {
					int server1 = Integer.parseInt(t.nextToken());
					int server2 = Integer.parseInt(t.nextToken());
					env.recoverConnection(server1, server2);
				}
				
				// server management
				else if (type.equals("join")) {
					int server = Integer.parseInt(t.nextToken());
					env.join(server);
				}
				
				else if (type.equals("leave")) {
					int server = Integer.parseInt(t.nextToken());
					env.leave(server);
				}
				
				// client management
				else if (type.equals("clientCreate")) {
					if (t.hasMoreTokens()) {
						int server = Integer.parseInt(t.nextToken());
						Client c = new Client(env, env.clients.size(), server);
					} else {
						Client c = new Client(env, env.clients.size());
					}
				}
				
				else if (type.equals("clientConnect")) {
					int client = Integer.parseInt(t.nextToken());
					int server = Integer.parseInt(t.nextToken());
					env.clients.get(client).server = server;
				}
				
				// commands to client
				else if (type.equals("client")) {
					int client = Integer.parseInt(t.nextToken());
					
					String cmdType = t.nextToken();
					String songName = t.nextToken();
					
					if (cmdType.equals("add")) {
						String url = t.nextToken();
						Command cmd = new Command(cmdType, songName, url);
						Client c = env.clients.get(client);
						c.writeRequest(cmd);
					}
					
					else if (cmdType.equals("delete")) {
						Command cmd = new Command(cmdType, songName);
						Client c = env.clients.get(client);
						c.readRequest(cmd);
					}
					
					else if (cmdType.equals("edit")) {
						String url = t.nextToken();
						Command cmd = new Command(cmdType, songName, url);
						Client c = env.clients.get(client);
						c.readRequest(cmd);
					}
				}
				
				
//				if (type.equals("addBankClient")) {
//					
//					int clientID = Integer.parseInt(t.nextToken());
//					
//					if (t.hasMoreTokens()) {
//						String name = t.nextToken();
//						addBankClient(clientID, name);
//					} else {
//						addBankClient(clientID);
//					}
//					
//				} else if (type.equals("createAccount")) {
//					
//					int clientID = Integer.parseInt(t.nextToken());
//					int accountNum = Integer.parseInt(t.nextToken());
//					
//					if (t.hasMoreTokens()) {
//						long balance = Long.parseLong(t.nextToken());
//						createAccount(clientID, accountNum, balance);
//					} else {
//						createAccount(clientID, accountNum);
//					}
//					
//				} else if (type.equals("deposit")) {
//					
//					int clientID = Integer.parseInt(t.nextToken());
//					int accountNum = Integer.parseInt(t.nextToken());
//					long amount = Long.parseLong(t.nextToken());
//					
//					deposit(clientID, accountNum, amount);
//					
//				} else if (type.equals("withdraw")) {
//					
//					int clientID = Integer.parseInt(t.nextToken());
//					int accountNum = Integer.parseInt(t.nextToken());
//					long amount = Long.parseLong(t.nextToken());
//					
//					withdraw(clientID, accountNum, amount);
//					
//				} else if (type.equals("transfer")) {
//					
//					int fromClient = Integer.parseInt(t.nextToken());
//					int fromAcc = Integer.parseInt(t.nextToken());
//					int toAcc = Integer.parseInt(t.nextToken());
//					long amount = Long.parseLong(t.nextToken());
//					
//					if (t.hasMoreTokens()) {
//						int toClient = Integer.parseInt(t.nextToken());
//						transfer(fromClient, fromAcc, toAcc, amount, toClient);
//					} else {
//						transfer(fromClient, fromAcc, toAcc, amount);
//					}
//				}
			} catch (Exception e) {
				System.out.println("Invalid command: " + command + ", ");
			}
		}
		
	}
}
