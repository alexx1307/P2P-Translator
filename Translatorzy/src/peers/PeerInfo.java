package peers;

/**
 * Klasa informacyjna.
 * 
 * @author lukasz
 * 
 */
public class PeerInfo {
	private int port;
	private String host;

	public PeerInfo(int port, String host) {
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
