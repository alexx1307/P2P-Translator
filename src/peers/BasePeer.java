package peers;

/**
 * Klasa reprezentujaca BasePeer.
 * 
 * @author lukasz
 *
 */
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bfsPort;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BasePeer))
			return false;
		BasePeer other = (BasePeer) obj;
		if (bfsPort != other.bfsPort)
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		return true;
	}
}
