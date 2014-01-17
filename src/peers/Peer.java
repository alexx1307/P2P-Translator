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
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((basePeer == null) ? 0 : basePeer.hashCode());
		result = prime * result + controlPort;
		result = prime * result
				+ ((publicKey == null) ? 0 : publicKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Peer))
			return false;
		Peer other = (Peer) obj;
		if (basePeer == null) {
			if (other.basePeer != null)
				return false;
		} else if (!basePeer.equals(other.basePeer))
			return false;
		if (controlPort != other.controlPort)
			return false;
		if (publicKey == null) {
			if (other.publicKey != null)
				return false;
		} else if (!publicKey.equals(other.publicKey))
			return false;
		return true;
	};

	
	public BasePeer getBasePeer() {
		// TODO Auto-generated method stub
		return basePeer;
	}
	
}
