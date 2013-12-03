
public abstract class Process extends Thread {
	int me;
	Queue<Message> inbox = new Queue<Message>();
	Queue<Message> inbox_state = new Queue<Message>();
	Env env;

	abstract void body();

	Message getNextMessage(){
		return inbox.bdequeue();
	}
	
	Message getNextStateMessage(){
		return inbox_state.bdequeue();
	}

	void sendServerMessage(int dst, Message msg){
		env.sendServerMessage(dst, msg);
	}
	
	void sendServerStateMessage(int dst, Message msg){
		env.sendServerMessage(dst, msg);
	}
	
	void sendServerCreateRespMessage(int dst, Message msg){
		env.sendServerCreateRespMessage(dst, msg);
	}
	
	void sendClientMessage(int dst, Message msg){
		env.sendClientMessage(dst, msg);
	}
	
	void sendStateReqMessage(int dst, Message msg){
		env.sendStateReqMessage(dst, msg);
	}

	void deliver(Message msg){
		inbox.enqueue(msg);
	}
	
	void deliverState(Message msg){
		inbox_state.enqueue(msg);
	}
}
