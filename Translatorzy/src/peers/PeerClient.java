package peers;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Klasa klienta.
 * 
 * @author lukasz
 * 
 */
public class PeerClient {

	private int trackerport;
	private String trackerhost;
	private LinkedList<PeerInfo> peers;

	public PeerClient(String host, int port) {
		this.trackerport = port;
		this.trackerhost = host;
		peers = new LinkedList<>();
	}

	public void translate() {
		getPeers();
		for (PeerInfo peer : peers) {
			connectToPeer(peer.getHost(), peer.getPort());
		}
	}

	public void connectToPeer(String host, int port) {
		try {

			System.out
					.println("Connecting to peer: " + host + " port: " + port);
			Socket socket = new Socket(host, port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			String fromServer;

			out.println("HELLO");
			while ((fromServer = in.readLine()) != null) {
				System.out.println("Server response: " + fromServer);
				if (fromServer.equals("BYE"))
					break;
				out.println("BYE");
			}

			socket.close();
			System.out.println("Connection end.");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void getPeers() {
		peers.clear();
		try {
			Socket socket = new Socket(trackerhost, trackerport);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			out.println("GET_PEERS");
			String fromServer = in.readLine();
			if (fromServer.equals("GET_PEERS OK")) {
				fromServer = in.readLine();
				while (!fromServer.equals("END")) {
					System.out.println(fromServer);
					StringTokenizer st = new StringTokenizer(fromServer);
					String host = null;
					int port = 0;
					if (st.hasMoreTokens())
						st.nextToken();
					if (st.hasMoreTokens())
						host = st.nextToken();
					if (st.hasMoreTokens())
						st.nextToken();
					if (st.hasMoreTokens())
						port = Integer.parseInt(st.nextToken());

					System.out.println("Adding peer: " + host + " " + port);
					peers.add(new PeerInfo(port, host));
					fromServer = in.readLine();
				}
			}
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
