package peers;

import java.net.ServerSocket;

/**
 * Klasa reprezentujaca hosta. Argument 1-translator jest aktywyny.
 * @author lukasz
 *
 */
public class Host {
	private String trackerHost;
	private int trackerPort;

	private String hostName;
	private int serverPort;

	private Client client;
	private Translator translator;
	private Server server;
	
	private boolean isTranslator;

	public Host(boolean isTranslator) {
		this.isTranslator=isTranslator;
		hostName = "localhost";
		trackerHost = "localhost";
		trackerPort = 8000;
		serverPort = findFreePort();

		server = new Server(this);
		client = new Client(this);
	
	    server.start();
	    client.start();
	    
	    if(isTranslator)
			translator = new Translator(this);
	}

	public String getTrackerHost() {
		return trackerHost;
	}

	public int getTrackerPort() {
		return trackerPort;
	}

	public int getServerPort() {
		return serverPort;
	}

	public String getHostName() {
		return hostName;
	}
	
	public Translator getTranslator(){
		return translator;
	}
	
	public Client getClient(){
		return client;
	}
	
	public Server getServer(){
		return server;
	}

	public synchronized int findFreePort() {
		int port;
		try {
			ServerSocket socket = new ServerSocket(0);
			port = socket.getLocalPort();
			socket.close();
		} catch (Exception e) {
			port = -1;
		}
		return port;
	}

	public static void main(String[] args) {
		int isTranslator = Integer.parseInt(args[0]);
		System.out.println("Starting new host");
		Host host = new Host(isTranslator==1);
	}

}
