package peers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashSet;
import java.util.StringTokenizer;

public class BFSConnection {
	private Socket socket;
	private Socket respondSocket;
	private Host host;
	private HostsUpdaterManager manager;

	public BFSConnection(Host host, Socket socket) {
		this.socket = socket;
		this.host = host;
		manager = host.getHostsUpdaterManager();
		interpretRequest();
	}

	public void interpretRequest() {
		try {

			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			String inputLine, outputLine;
			int state = 0;
			HashSet<Peer> tempSet = new HashSet<Peer>();
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.equals("GET PEERS")) {
					inputLine = in.readLine();
					int bfsPort = Integer.parseInt(inputLine);
					inputLine = in.readLine();
					int controlPort = Integer.parseInt(inputLine);

					PublicKey pubKey = null;
					try {
						pubKey = Encrypter.makePublicKey(
								new BigInteger(in.readLine()), new BigInteger(
										in.readLine()));
					} catch (InvalidKeySpecException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					manager.addPeer(new Peer(new BasePeer(bfsPort, socket
							.getInetAddress().getHostAddress()), controlPort,
							pubKey));
					state = 1;
					respondSocket = new Socket(socket.getInetAddress()
							.getHostAddress(), bfsPort);
					PrintWriter out = new PrintWriter(
							respondSocket.getOutputStream(), true);

					HashSet<Peer> peers = manager.getBFSPeers();
					out.println("PEERS RESPOND");
					for (Peer peer : peers) {
						String peerLine = "Peer: "
								+ peer.getHost()
								+ " "
								+ peer.getBFSPort()
								+ " "
								+ peer.getPort()
								+ " "
								+ ((RSAPublicKey) peer.getPublicKey())
										.getModulus().toString()
								+ " "
								+ ((RSAPublicKey) peer.getPublicKey())
										.getPublicExponent().toString();
						out.println(peerLine);
					}
				} else if (inputLine.equals("PEERS RESPOND")) {
					state = 2;

				} else if (state == 2) {
					StringTokenizer st = new StringTokenizer(inputLine);
					int _port = 0;
					int _bfsPort = 0;
					String _host = null;
					BigInteger _keyM = null;
					BigInteger _keyE = null;
					if (st.hasMoreTokens())
						st.nextToken();
					if (st.hasMoreTokens())
						_host = st.nextToken();
					if (st.hasMoreTokens())
						_bfsPort = Integer.parseInt(st.nextToken());
					if (st.hasMoreTokens())
						_port = Integer.parseInt(st.nextToken());
					if (st.hasMoreTokens())
						_keyM = new BigInteger(st.nextToken());
					if (st.hasMoreTokens())
						_keyE = new BigInteger(st.nextToken());
					Peer peer;
					try {
						peer = new Peer(new BasePeer(_bfsPort, _host), _port,
								Encrypter.makePublicKey(_keyM, _keyE));
						tempSet.add(peer);
					} catch (InvalidKeySpecException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					break;
				}
			}
			manager.addPeers(tempSet);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException ex) {
				}
			}
			if (respondSocket != null) {
				try {
					respondSocket.close();
				} catch (IOException ex) {
				}
			}
		}
	}
}