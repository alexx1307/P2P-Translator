package peers;

/**
 * Klasa informacyjna.
 * 
 * @author lukasz
 * 
 */
public class Peer {
	private BasePeer basePeer;
	private int controlPort;
	private String publicKey;
	public Peer(BasePeer basePeer, int controlPort, String publicKey) {
		this.basePeer = basePeer;
		this.controlPort = controlPort;
		this.publicKey = publicKey;
	}

	public int getBFSPort() {
		return basePeer.getPort();
	}
	public int getPort() {
		return controlPort;
	}

	public String getHost() {
		return basePeer.getHost();
	}
	
	public String getPublicKey() {
		return publicKey;
	}
	
	@Override
	public int hashCode() {
		 return publicKey.hashCode();
	}

	public BasePeer getBasePeer() {
		// TODO Auto-generated method stub
		return basePeer;
	}
}
