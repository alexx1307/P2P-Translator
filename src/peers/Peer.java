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
	private String publicKey;
	
	public Peer(int port, String host, String publicKey) {
		this.port = port;
		this.host = host;
		this.publicKey = publicKey;
	}

	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}
	
	public String getPublicKey() {
		return publicKey;
	}
	
	@Override
	public int hashCode() {
		 return publicKey.hashCode();
	}
}
