public class Client extends Process {
	
	int server;
	
	public Client(Env env, int me) {
		this.env = env;
		this.me = me;
		
		env.addClient(me, this);
	}
	
	public void run(){
		body();
		env.removeClient(me);
	}

	@Override
	void body() {
		// TODO Auto-generated method stub
		
	}
}
