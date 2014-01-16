package peers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.StringTokenizer;

public class BFSConnection  {
	private Socket socket;
	private Socket respondSocket;
	private Host host;
	private HostsUpdaterManager manager;
	public BFSConnection(Host host, Socket socket) {
		this.socket = socket;
		this.host=host;
		manager = host.getHostsUpdaterManager();
	}

	public void interpretRequest() {
		try {
			
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
            
			respondSocket = new Socket(socket.getInetAddress(),socket.getPort());
			PrintWriter out = new PrintWriter(respondSocket.getOutputStream(), true);
			String inputLine, outputLine;
			int state = 0;
			HashSet<Peer> tempSet = new HashSet<Peer>();
			while ((inputLine = in.readLine()) != null) {
				if(inputLine.equals("GET PEERS")){
					state = 1;
					HashSet<Peer> peers = manager.getActivePeers();
					out.println("PEERS RESPOND");
					for(Peer peer : peers){
						String peerLine = "Peer: "+peer.getHost()+" "+peer.getPort()+" "+peer.getPublicKey();
						out.println(peerLine);
					}
				}else if(inputLine.equals("PEERS RESPOND")){
					state = 2;
					
				}else if(state==2){
					StringTokenizer st = new StringTokenizer(inputLine);
					int _port = 0;
					String _host = null;
					String _key = null;
					int sport = 0;
					if (st.hasMoreTokens())
						st.nextToken();
					if (st.hasMoreTokens())
						_host = st.nextToken();
					if (st.hasMoreTokens())
						_port = Integer.parseInt(st.nextToken());
					if (st.hasMoreTokens())
						_key = st.nextToken();
					Peer peer = new Peer(_port, _host, _key);
					tempSet.add(peer);
				}else{
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