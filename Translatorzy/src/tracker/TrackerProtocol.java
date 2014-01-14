package tracker;

import java.util.StringTokenizer;

import peers.Peer;

/**
 * Protokol komunikacyjny pomiedzy trackerem a peerem.
 * 
 * @author lukasz
 * 
 */
public class TrackerProtocol {
	Tracker tracker;

	public TrackerProtocol(Tracker tracker) {
		this.tracker = tracker;
	}

	public String processServerInput(String input) {
		StringBuilder result = null;

		if (input.equals("GET_PEERS")) {
			result = new StringBuilder("GET_PEERS OK\n");
			for (Peer peer : tracker.getPeers()) {
				result.append("host: " + peer.getHost() + " port: "
						+ peer.getPort() + "\n");
			}
			result.append("END");
		} else if (input.equals("BYE")) {
			result = new StringBuilder("BYE");
		} else if (input.equals("HELLO")) {
			result = new StringBuilder("HELLO");
		} else if (input.substring(0, 8).equals("REGISTER")) {
			StringTokenizer st = new StringTokenizer(input);
			st.nextToken();

			String host = st.nextToken();
			String port = st.nextToken();
			tracker.registryPeer(host, Integer.parseInt(port));
			result = new StringBuilder("REGISTER OK");
		} else {
			result = new StringBuilder("ERROR");
		}

		return result.toString();
	}

}
