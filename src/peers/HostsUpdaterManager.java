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
	private HashSet<Peer> sourcePeers; //peery podane na wejsciu (np tracker)
	private HashSet<Peer> activePeers; //peery aktualnie aktywne
	public HostsUpdaterManager(Host host) {
		activePeers = new HashSet<Peer>();
		sourcePeers = new HashSet<Peer>();
		sourcePeers.addAll(host.getTrackers());
	}
	
	public void init() {
		System.out.println("Starting looking for peers");
		Timer timer = new Timer();
		timer.schedule(new HostsUpdaterTask(this),10,5000);
		
	}

	public HashSet<Peer> getSourcePeers() {
		HashSet<Peer> res = sourcePeers;
		res.addAll(activePeers);
		System.out.println("Number of source peers:" + res.size());
		return res;
	}
	public HashSet<Peer> getActivePeers(){
		return activePeers;
	}
	
}
