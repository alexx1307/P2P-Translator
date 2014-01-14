package tracker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import peers.Peer;

/**
 * Klasa trackera.
 * 
 * @author lukasz
 *
 */
public class Tracker extends Thread {
	private int port;
	private LinkedList<Peer> peers;

	public Tracker(int port) {
		this.port = port;
		peers = new LinkedList<>();
	}
	
	public LinkedList<Peer> getPeers(){
		return peers;
	}
	
	public void registryPeer(String host, int port){
		peers.add(new Peer(port,host));
	}

	@Override
	public void run() {
		boolean listen = true;
	
		try(ServerSocket serversocket = new ServerSocket(port)){
			while (listen) {
				Socket socket = serversocket.accept();
				System.out.println("address: "+socket.getInetAddress());
				new TrackerConnection(socket, peers, this).start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		super.run();
	}
}
