public class Client extends Process {
	
	int server;
	
	public Client(Env env, int me) {
		this.env = env;
		this.me = me;
		
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
		System.out.println("Here I am: " + me);
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
//			
//			else if  (msg instanceof ClientReadMessage) {
//				ClientReadMessage m = (ClientReadMessage) msg;
//				performRead(m.command);
//			}
//			
//			else {
//				System.err.println("Server: unknown msg type");
//			}
		}
		
	}

	public void writeRequest(Command cmd) {
		sendClientMessage(server, new ClientWriteMessage(me, cmd));
	}

	public void readRequest(Command cmd) {
		sendClientMessage(server, new ClientReadMessage(me, cmd));
	}


}
