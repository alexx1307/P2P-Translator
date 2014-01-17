package peers;

public class BasePeer {
	private String host;
	private int bfsPort;
	
	public BasePeer(int bfsPort,String host) {
		this.bfsPort = bfsPort;
		this.host = host;
	}

	public int getPort() {
		return bfsPort;
	}

	public String getHost() {
		return host;
	}
}
