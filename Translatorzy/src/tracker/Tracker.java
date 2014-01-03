package tracker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;


import peers.PeerHandler;
import peers.PeerInfo;

public class Tracker extends Thread {
	private int port;
	private LinkedList<PeerInfo> peers;

	public Tracker(int port) {
		this.port = port;
		peers = new LinkedList<>();
	}
	
	public LinkedList<PeerInfo> getPeers(){
		return peers;
	}
	
	public void registryPeer(String host, int port){
		peers.add(new PeerInfo(port,host));
	}

	@Override
	public void run() {

		boolean listen = true;
		ServerSocket serversocket = null;
		try {
			
			serversocket = new ServerSocket(port);
			while (listen) {
				Socket socket = serversocket.accept();
				new PeerHandler(socket, peers, this).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		super.run();
	}
}
