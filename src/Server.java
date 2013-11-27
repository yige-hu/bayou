import java.util.ArrayList;
import java.util.List;


public class Server extends Process {
	
	List<Integer> connected_servers = new ArrayList<Integer>();
	
	PlayList commited, tentative;

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
				performWrite(m.command);
				
				antiEntropy(m.command);
				
			}
			
			else if  (msg instanceof ClientReadMessage) {
				ClientReadMessage m = (ClientReadMessage) msg;
				performRead(m.command);
			}
			
			else {
				System.err.println("Server: unknown msg type");
			}
		}
		
	}

	private void performWrite(Command command) {
		// TODO Auto-generated method stub
		
	}

	private void performRead(Command command) {
		// TODO Auto-generated method stub
		
	}
	
	private void antiEntropy(Command command) {
		// TODO Auto-generated method stub
		
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
		for (Song s : commited.songs.values()) {
			System.out.println("\t" + s + "\n");
		}
		
		System.out.println("tentative:\n");
		for (Song s : tentative.songs.values()) {
			System.out.println("\t" + s + "\n");
		}
	}
}
