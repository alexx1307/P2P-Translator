package peers;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;



public class PeerServer extends Thread {
	private int port;
	private String host;
	private int trackerport;
	private String trackerhost;

	public PeerServer(String host, int port, int trackerport, String trackerhost) {
		this.host=host;
		this.port = port;
		this.trackerhost=trackerhost;
		this.trackerport=trackerport;
		
		register();
	}

	public void register() {
		try {
			Socket socket=new Socket(trackerhost,trackerport);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			
			out.println("REGISTER "+host+" "+port);
			System.out.println(in.readLine());
			socket.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

	@Override
	public void run() {
		boolean listen=true;
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (listen) {
				Socket socket = serverSocket.accept();
				ServerConnection serverconnection = new ServerConnection(socket);
				serverconnection.start();
			}
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
