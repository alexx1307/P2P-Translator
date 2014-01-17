package peers;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Timer;

/**
 * Klasa odpowiadajaca za aktualizaje peerow.
 * 
 * @author lukasz
 * 
 */
public class HostsUpdaterManager{
	private HashSet<BasePeer> sourcePeers; //peery podane na wejsciu (np tracker)
	private HashSet<BasePeer> tempPeers; //oniekty ktore sie z nami kontaktowaly
	private HashSet<Peer> nextPeerSet; //zbior ktory zastapi activePeers (podwojny bufor)
	private HashSet<Peer> activePeers; //peery aktualnie aktywne
	private BFSServer bfsServer;
	private Host host;
	
	public HostsUpdaterManager(Host host, BFSServer bfsServer) {
		activePeers = new HashSet<Peer>();
		nextPeerSet = new HashSet<Peer>();
		sourcePeers = new HashSet<BasePeer>();
		tempPeers = new HashSet<BasePeer>();
		sourcePeers.addAll(host.getTrackers());
		this.bfsServer = bfsServer;
		this.host = host;
	}
	
	public void init() {
		System.out.println("Starting looking for peers");
		Timer timer = new Timer();
		timer.schedule(new HostsUpdaterTask(this,host),10,5000);
		
	}

	public HashSet<BasePeer> getSourcePeers() {
		HashSet<BasePeer> res = sourcePeers;
		for(Peer p : activePeers){
			res.add(p.getBasePeer());			
		}

		for(BasePeer p : tempPeers){
			res.add(p);			
		}
		tempPeers.clear();
		res.remove(host.getSelfPeer());
		System.out.println("Number of source peers="+res.size());

		
		return res;
	}
	public HashSet<Peer> getActivePeers(){
		return activePeers;
	}

	public void addPeers(HashSet<Peer> tempSet) {
		nextPeerSet.addAll(tempSet);
	}

	public void addPeer(BasePeer basePeer) {
		tempPeers.add(basePeer);
	}
	
	public void flipBuffers(){
		nextPeerSet.remove(host.getSelfPeer());
		activePeers = nextPeerSet;
		nextPeerSet.clear();
	}
	
}
