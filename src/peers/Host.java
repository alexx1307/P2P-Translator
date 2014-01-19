package peers;

import java.net.ServerSocket;
import java.security.PublicKey;
import java.util.ArrayList;
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

	private LinkedList<BasePeer> trackers;
	
	private String hostName;
	private int serverPort;
	private int BFSServerPort;
	private Encrypter encrypter;
	
	private Client client;
	private Translator translator;
	private Server server;
	private BFSServer bfsServer;
	private HostsUpdaterManager hostsUpdater;
	
	private ArrayList<Thread> threads; 
	
	private boolean isTranslator;

	//konstruktor trackera
	public Host(){
		this.isTranslator=false;
		trackers = new LinkedList<BasePeer>();
		BFSServerPort = 8000;
		bfsServer = new BFSServer(this);
		encrypter = new Encrypter();
		hostsUpdater = new HostsUpdaterManager(this, bfsServer,5000);
		bfsServer.start();
		hostsUpdater.init();
	}
	
	public Host(boolean isTranslator) {
		
		
		this.isTranslator=isTranslator;
		hostName = "127.0.0.1";
		trackerHost = "127.0.0.1";
		trackerPort = 8000;
		trackers = new LinkedList<BasePeer>();
		trackers. add(new BasePeer(trackerPort, trackerHost));
		serverPort = findFreePort();
		
		encrypter = new Encrypter();
		
		server = new Server(this);
		BFSServerPort = findFreePort();
		
		bfsServer = new BFSServer(this);
		
		client = new Client(this);
		hostsUpdater = new HostsUpdaterManager(this,bfsServer,5000);
		
	    server.start();
	    client.start();
	    bfsServer.start();
	    hostsUpdater.init();
	    
	    if(isTranslator)
			translator = new Translator(this);
	    
	    threads = new ArrayList<Thread>();
	}
	
	public Encrypter getEncrypter(){
		return encrypter;
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

	public Collection<BasePeer> getTrackers() {
		return trackers;
	}

	public HostsUpdaterManager getHostsUpdaterManager() {
		return hostsUpdater;
	}
	
	
	public Peer getSelfPeer() {
		return new Peer(new BasePeer(getBFSServerPort(), getHostName()), getServerPort(), getPublicKey());
	}

	public PublicKey getPublicKey() {
		return (PublicKey) encrypter.getPublicKey();
	}
	
	public void addThread(Thread t){
		threads.add(t);
	}
	
	public ArrayList<Thread> getThreads(){
		return threads;
	}
	
	public static void main(String[] args) {
		int isTranslator = Integer.parseInt(args[0]);
		System.out.println("Starting new host");
		Host host = new Host(isTranslator==1);
	}

}
