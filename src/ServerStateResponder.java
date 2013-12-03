
public class ServerStateResponder extends Process {
	
	Server server;

	public ServerStateResponder(Env env, int me, Server server) {
		this.env = env;
		this.me = me;
		this.server = server;
		
		env.addServerStateResponder(me, this);
	}
	
	public void run(){
		body();
		env.removeServerStateResponder(me);
	}
	
	@Override
	void body() {
		System.out.println("Here I am: responder" + me);
		for (;;) {
			Message msg = getNextMessage();

			if (msg instanceof StateRequestMessage) {
				StateRequestMessage m = (StateRequestMessage) msg;
				if (Env.DEBUG) {
					System.out.println("StateResponder" + me + ": get req from server" + m.src);
				}
				sendServerMessage(m.src, new StateResponseMessage(me, server.V, server.committed, server.tentative));
			}
			else {
				System.err.println("Server" + me + ": unknown msg type" + msg.getClass());
			}
		}
	}

}
