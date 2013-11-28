public class Client extends Process {
	
	int server;
	int num_cmd = 0;
	
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
		for (;;) {
			while (Env.pause);
			
			Message msg = getNextMessage();

			
//			if (msg instanceof ServerResponseMessage) {
//				ClientWriteMessage m = (ClientWriteMessage) msg;
//				performWrite(m.command);
//				
//				antiEntropy(m.command);
//				
//			}

		}
		
	}

	public void writeRequest(Command cmd) {
		sendServerMessage(server, new ClientWriteMessage(me, cmd));
	}

	public void readOnlyRequest(Command cmd) {
		sendServerMessage(server, new ClientReadOnlyMessage(me, cmd));
	}

	public void printLog() {
		System.out.println("Status: client" + me + " connected with server" + server);
	}


}
