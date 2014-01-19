package peers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.interfaces.RSAPublicKey;
import java.util.HashSet;
import java.util.TimerTask;
/*
 * Klasa odpowiada za nawiazanie polaczen i wyslanie zapytan do peerow
 * 
 */
public class HostsUpdaterTask extends TimerTask {
	HashSet<BasePeer> sourcePeers;
	Host host;
	HostsUpdaterManager manager;
	public HostsUpdaterTask(HostsUpdaterManager hostsUpdaterManager, Host host) {
		manager = hostsUpdaterManager;
		this.host = host;
	}

	@Override
	public void run() {
		sourcePeers= manager.getSourcePeers();
		for(BasePeer peer: sourcePeers){
			SendBFSRequest(peer);
		}
		manager.flipBuffers();
	}

	private void SendBFSRequest(BasePeer peer) {
		Socket socket = null;
		PrintWriter out = null;
		try {
			socket = new Socket(peer.getHost(),peer.getPort());
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println("GET PEERS");
			out.println(host.getBFSServerPort());
			out.println(host.getServerPort());
			out.println(((RSAPublicKey)host.getPublicKey()).getModulus().toString());
			out.println(((RSAPublicKey)host.getPublicKey()).getPublicExponent().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error while connecting to source peer");
			//e.printStackTrace();
		}finally {
			if (socket != null) {
				try {
					socket.close();
					
				} catch (IOException ex) {
				}
			}
			if (out != null) {
				out.close();
			}
		}
		
		
	}
	

}
