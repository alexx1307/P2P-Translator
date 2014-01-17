package peers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BFSServer extends Thread {

	private int port;
	private Host host;

	public BFSServer(Host host) {
		this.host = host;
		this.port = host.getBFSServerPort();
	}

	@Override
	public void run() {
		System.out.println("ServerBFS is running on port: " + port);
		boolean listen = true;
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (listen) {
				Socket socket = serverSocket.accept();
				
				BFSConnection bfsConnection = new BFSConnection(
						host, socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}