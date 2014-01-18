package peers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Klasa klienta.
 * 
 * @author lukasz
 * 
 */
public class Client extends Thread {

	private int trackerPort;
	private String trackerHost;
	private LinkedList<Peer> peers;
	private LinkedList<Peer> activeTranslators;
	private Socket socket;
	private Host host;

	public Client(Host host) {
		this.trackerHost = host.getTrackerHost();
		this.trackerPort = host.getTrackerPort();
		peers = new LinkedList<>();
		this.host = host;
	}

	@Override
	public void run() {
		System.out.println("Starting client");
		Scanner in = new Scanner(System.in);
		while (true) {
			in.next();
			translate();
		}
	}

	public void translate() {
		System.out.println("Starting translate");
		activeTranslators = new LinkedList<>();
		HashSet<Peer> peers = host.getHostsUpdaterManager().getActivePeers();
		for (Peer peer : peers) {
			connectToPeer(peer.getHost(), peer.getPort());

		}
		System.out.println("Translate end");
	}

	public void connectToPeer(String host, int port) {
		try {

			System.out
					.println("Connecting to peer: " + host + " port: " + port);

			socket = new Socket(host, port);

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			String fromServer;

			out.println("TRANSLATE");
			while ((fromServer = in.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(fromServer);
				String shost = null;
				int sport = 0;
				if (st.hasMoreTokens())
					st.nextToken();
				if (st.hasMoreTokens())
					shost = st.nextToken();
				if (st.hasMoreTokens())
					sport = Integer.parseInt(st.nextToken());

				System.out.println("adding host: " + shost + " port: " + sport
						+ " to activeTrans");
				// activeTranslators.add(new Peer(sport, shost));

				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
					System.out.println("Connection end.");
				} catch (IOException ex) {
				}
			}
		}

	}
	/*
	 * public void getPeers() { peers.clear(); try { socket = new
	 * Socket(trackerHost, trackerPort); PrintWriter out = new
	 * PrintWriter(socket.getOutputStream(), true); BufferedReader in = new
	 * BufferedReader(new InputStreamReader( socket.getInputStream()));
	 * 
	 * out.println("GET_PEERS"); String fromServer = in.readLine(); if
	 * (fromServer.equals("GET_PEERS OK")) { fromServer = in.readLine(); while
	 * (!fromServer.equals("END")) { System.out.println(fromServer);
	 * StringTokenizer st = new StringTokenizer(fromServer); String host = null;
	 * int port = 0; if (st.hasMoreTokens()) st.nextToken(); if
	 * (st.hasMoreTokens()) host = st.nextToken(); if (st.hasMoreTokens())
	 * st.nextToken(); if (st.hasMoreTokens()) port =
	 * Integer.parseInt(st.nextToken());
	 * 
	 * System.out.println("Adding peer: " + host + " " + port); peers.add(new
	 * Peer(port, host)); fromServer = in.readLine(); } } } catch (IOException
	 * e) { e.printStackTrace(); } finally { if (socket != null) { try {
	 * socket.close(); } catch (IOException ex) { } } }
	 * 
	 * }
	 */
}
