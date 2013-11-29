
public class ServerId {
	int num;
	ServerId Sk;
	int Tki;

	public ServerId(ServerId Sk, int Tki, int num){
		this.Sk = Sk;
		this.Tki = Tki;
		this.num = num;
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
		return "ServerId(" + Tki + ", " + Sk + ")";
	}
}
