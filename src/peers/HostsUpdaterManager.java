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
	private HashSet<Peer> tempPeers; //oniekty ktore sie z nami kontaktowaly
	private HashSet<Peer> nextPeerSet; //zbior ktory zastapi activePeers (podwojny bufor)
	private HashSet<Peer> activePeers; //peery aktualnie aktywne
	private BFSServer bfsServer;
	private Host host;
	
	public HostsUpdaterManager(Host host, BFSServer bfsServer) {
		activePeers = new HashSet<Peer>();
		nextPeerSet = new HashSet<Peer>();
		sourcePeers = new HashSet<BasePeer>();
		tempPeers = new HashSet<Peer>();
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

		for(Peer p : tempPeers){
			res.add(p.getBasePeer());			
		}
		tempPeers.clear();
		res.remove(host.getSelfPeer());
		System.out.println("Number of source peers="+res.size());

		
		return res;
	}
	public HashSet<Peer> getActivePeers(){
		HashSet<Peer> res = activePeers;
		res.addAll(tempPeers);
		Peer self = host.getSelfPeer();
		res.remove(self);
		System.out.println("Self peer = "+self.getHost()+" "+self.getPort()+" "+self.getBFSPort());
		System.out.println("Active peers number ="+res.size());
		for(Peer p : res){
			System.out.println("peer: "+p.getHost()+" "+p.getPort()+" "+p.getBFSPort()+" "+p.equals(self));
		}
		return res;
	}

	public void addPeers(HashSet<Peer> tempSet) {
		nextPeerSet.addAll(tempSet);
	}

	public void addPeer(Peer peer) {
		tempPeers.add(peer);
		
	}
	
	public void flipBuffers(){
		nextPeerSet.remove(host.getSelfPeer());
		activePeers = nextPeerSet;
		nextPeerSet.clear();
	}
	
}
