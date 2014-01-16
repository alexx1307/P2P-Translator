package peers;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.TimerTask;

public class HostsUpdaterTask extends TimerTask {
	HashSet<Peer> sourcePeers;
	
	HostsUpdaterManager manager;
	public HostsUpdaterTask(HostsUpdaterManager hostsUpdaterManager) {
		manager = hostsUpdaterManager;
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
		try {
			Socket socket = new Socket(peer.getHost(),peer.getPort());
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

}
