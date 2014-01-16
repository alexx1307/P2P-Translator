package peers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.TimerTask;

public class HostsUpdaterTask extends TimerTask {
	HashSet<Peer> sourcePeers;
	Host host;
	HostsUpdaterManager manager;
	public HostsUpdaterTask(HostsUpdaterManager hostsUpdaterManager, Host host) {
		manager = hostsUpdaterManager;
		this.host = host;
	}

	@Override
	public void run() {
		System.out.println("Looking for active peers");
		sourcePeers= manager.getSourcePeers();
		for(Peer peer: sourcePeers){
			SendBFSRequest(peer);
		
		}
	}

	private void SendBFSRequest(Peer peer) {
		Socket socket = null;
		PrintWriter out = null;
		try {
			socket = new Socket(peer.getHost(),peer.getPort(), InetAddress.getLocalHost(), host.getBFSServerPort());
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println("GET PEERS");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
