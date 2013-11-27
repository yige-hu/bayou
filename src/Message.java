

import java.util.*;

public class Message {
	ProcessId src;
}

class ClientWriteMessage extends Message {
	Command command;
	public ClientWriteMessage(ProcessId src, Command command){
		this.src = src; this.command = command;
	}
}

class ClientReadMessage extends Message {
	Command command;
	public ClientReadMessage(ProcessId src, Command command){
		this.src = src; this.command = command;
	}
}

//class P1aMessage extends Message {
//	BallotNumber ballot_number;
//	ProcessId newLeader;
////	P1aMessage(ProcessId src, BallotNumber ballot_number){
////		this.src = src; this.ballot_number = ballot_number;
////	}
//	P1aMessage(ProcessId src, BallotNumber ballot_number, ProcessId newLeader){
//		this.src = src; this.ballot_number = ballot_number; this.newLeader = newLeader;
//	}
//}
//class P1bMessage extends Message {
//	BallotNumber ballot_number; Set<PValue> accepted;
//	ProcessId newLeader;
////	P1bMessage(ProcessId src, BallotNumber ballot_number, Set<PValue> accepted) {
////		this.src = src; this.ballot_number = ballot_number; this.accepted = accepted;
////	}
//	P1bMessage(ProcessId src, BallotNumber ballot_number, Set<PValue> accepted, ProcessId newLeader) {
//		this.src = src; this.ballot_number = ballot_number; this.accepted = accepted; this.newLeader = newLeader;
//	}
//}
//class P2aMessage extends Message {
//	BallotNumber ballot_number; int slot_number; Command command;
//	ProcessId newLeader;
////	P2aMessage(ProcessId src, BallotNumber ballot_number, int slot_number, Command command){
////		this.src = src; this.ballot_number = ballot_number;
////		this.slot_number = slot_number; this.command = command;
////	}
//	P2aMessage(ProcessId src, BallotNumber ballot_number, int slot_number, Command command, ProcessId newLeader){
//		this.src = src; this.ballot_number = ballot_number;
//		this.slot_number = slot_number; this.command = command; this.newLeader = newLeader;
//	}
//}
//class P2bMessage extends Message {
//	BallotNumber ballot_number; int slot_number;
//	ProcessId newLeader;
////	P2bMessage(ProcessId src, BallotNumber ballot_number, int slot_number){
////		this.src = src; this.ballot_number = ballot_number; this.slot_number = slot_number;
////	}
//	P2bMessage(ProcessId src, BallotNumber ballot_number, int slot_number, ProcessId newLeader){
//		this.src = src; this.ballot_number = ballot_number; this.slot_number = slot_number; this.newLeader = newLeader;
//	}
//}
//class PreemptedMessage extends Message {
//	BallotNumber ballot_number;
//	ProcessId newLeader;
////	PreemptedMessage(ProcessId src, BallotNumber ballot_number){
////		this.src = src; this.ballot_number = ballot_number;
////	}
//	PreemptedMessage(ProcessId src, BallotNumber ballot_number, ProcessId newLeader){
//		this.src = src; this.ballot_number = ballot_number; this.newLeader = newLeader;
//	}
//}
//class AdoptedMessage extends Message {
//	BallotNumber ballot_number; Set<PValue> accepted;
//	AdoptedMessage(ProcessId src, BallotNumber ballot_number, Set<PValue> accepted){
//		this.src = src; this.ballot_number = ballot_number; this.accepted = accepted;
//}	}
//class DecisionMessage extends Message {
//	ProcessId src; int slot_number; Command command;
//	public DecisionMessage(ProcessId src, int slot_number, Command command){
//		this.src = src; this.slot_number = slot_number; this.command = command;
//}	}
//class RequestMessage extends Message {
//	Command command;
//	public RequestMessage(ProcessId src, Command command){
//		this.src = src; this.command = command;
//}	}
//class ProposeMessage extends Message {
//	int slot_number; Command command;
//	public ProposeMessage(ProcessId src, int slot_number, Command command){
//		this.src = src; this.slot_number = slot_number; this.command = command;
//}	}
//
//class RespondMessage extends Message {
//	Command command;
//	String result;
//	public RespondMessage(ProcessId src, Command command, String result){
//		this.src = src; this.command = command; this.result = result;
//}	}
//
//class PingRequestMessage extends Message {
//	ProcessId leader;
//	public PingRequestMessage(ProcessId src, ProcessId leader){
//		this.src = src;
//		this.leader = leader;
//}	}
//class PingRespondMessage extends Message {
//	public PingRespondMessage(ProcessId src){
//		this.src = src;
//}	}
//
//class ClientMessage extends Message {
//	String op;
//	public ClientMessage(String op){
//		this.op = op;
//}	}
//
//class ClientRequestMessage extends Message {
//	Command command;
//	public ClientRequestMessage(ProcessId src, Command command){
//		this.src = src; this.command = command;
//}	}
//
//class ROCClientMessage extends Message {
//	String op;
//	public ROCClientMessage(String op){
//		this.op = op;
//}	}
//
//class ROCClientRequestMessage extends Message {
//	Command command;
//	public ROCClientRequestMessage(ProcessId src, Command command){
//		this.src = src; this.command = command;
//}	}
//
//class ROCRequestMessage extends Message {
//	Command command;
//	public ROCRequestMessage(ProcessId src, Command command){
//		this.src = src; this.command = command;
//}	}
//
//class ROCRespondMessage extends Message {
//	Command command;
//	String result;
//	public ROCRespondMessage(ProcessId src, Command command, String result){
//		this.src = src; this.command = command; this.result = result;
//}	}