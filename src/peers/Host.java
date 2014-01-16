package peers;

import java.net.ServerSocket;
import java.util.Collection;
import java.util.LinkedList;

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
	private int BFSServerPort;
	Encrypter encrypter;
	
	private Client client;
	private Translator translator;
	private Server server;
	private BFSServer bfsServer;
	private HostsUpdaterManager hostsUpdater;
	
	private boolean isTranslator;

	public Host(boolean isTranslator) {
		this.isTranslator=isTranslator;
		hostName = "localhost";
		trackerHost = "localhost";
		trackerPort = 8000;
		serverPort = findFreePort();
		
		encrypter = new Encrypter();
		
		server = new Server(this);
		BFSServerPort = findFreePort();
		
		bfsServer = BFSServer(this);
		
		client = new Client(this);
		hostsUpdater = new HostsUpdaterManager(this,bfsServer);
		
	    server.start();
	    client.start();
	    hostsUpdater.init();
	    
	    if(isTranslator)
			translator = new Translator(this);
	}

	private BFSServer BFSServer(Host host) {
		// TODO Auto-generated method stub
		return null;
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

	public int getBFSServerPort() {
		return BFSServerPort;
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

	public Collection<Peer> getTrackers() {
		LinkedList<Peer> peers = new LinkedList<Peer>();
		peers.add(new Peer(getTrackerPort(), getTrackerHost(), "public key trackera"));
		return peers;
	}

	public HostsUpdaterManager getHostsUpdaterManager() {
		return hostsUpdater;
	}

}
