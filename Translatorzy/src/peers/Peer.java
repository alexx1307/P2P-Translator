package peers;

/**
 * Klasa informacyjna.
 * 
 * @author lukasz
 * 
 */
public class Peer {
	private int port;
	private String host;

	public Peer(int port, String host) {
		this.port = port;
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}
}
