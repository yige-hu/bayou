
public class ServerId {
	ServerId Sk;
	int Tki;

	public ServerId(ServerId Sk, int Tki){
		this.Sk = Sk;
		this.Tki = Tki;
	}

//	public boolean equals(Object other){
//		return compareTo(other) == 0;
//	}
//
//	public int compareTo(Object other){
//		ServerId c = (ServerId) other;
//		if (c.server != this.server) {
//			return c.server - this.server;
//		}
//		return c.cid - this.cid;
//	}

	public String toString(){
		return "ServerId(" + Sk + ", " + Tki + ")";
	}
}
