package peers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Klasa reprezentujaca serwer uruchamiany na kazdym hoscie.
 * 
 * @author lukasz
 *
 */
public class Server extends Thread {

	private int port;
	private Host host;

	public Server(Host host) {
		this.host = host;
		this.port = host.getServerPort();
	}

	@Override
	public void run() {
		Logger.write("Server is running on port: " + port);
		boolean listen = true;
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (listen) {
				Socket socket = serverSocket.accept();
				
				ClientTranslatorConnection ctConnection = new ClientTranslatorConnection(
						host, socket);
				ctConnection.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
