package peers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Timer;

/**
 * Klasa odpowiadajaca za aktualizaje peerow. Wysyla co okreslony czas zapytania
 * do wszystkich peerow
 * 
 * @author lukasz
 * 
 */
public class HostsUpdaterManager {
	private HashSet<BasePeer> sourcePeers; // peery podane na wejsciu (np
											// tracker)
	private HashSet<Peer> tempPeers; // oniekty ktore sie z nami kontaktowaly
	private HashSet<Peer> nextPeerSet; // zbior ktory zastapi activePeers
										// (podwojny bufor)
	private HashSet<Peer> activePeers; // peery aktualnie aktywne
	private BFSServer bfsServer;
	private Host host;
	private long delay; // czestotliwosc aktualizacji w ms

	public HostsUpdaterManager(Host host, BFSServer bfsServer, long delay) {
		activePeers = new HashSet<Peer>();
		nextPeerSet = new HashSet<Peer>();
		sourcePeers = new HashSet<BasePeer>();
		tempPeers = new HashSet<Peer>();
		sourcePeers.addAll(host.getTrackers());
		this.bfsServer = bfsServer;
		this.host = host;
		this.delay = delay;
	}

	public void init() {
		System.out.println("Starting looking for peers");
		Timer timer = new Timer();
		timer.schedule(new HostsUpdaterTask(this, host), 10, delay);

	}

	public HashSet<BasePeer> getSourcePeers() {
		HashSet<BasePeer> res = new HashSet<BasePeer>(sourcePeers);
		for (Peer p : activePeers) {
			res.add(p.getBasePeer());
		}

		for (Peer p : tempPeers) {
			res.add(p.getBasePeer());
		}
		tempPeers.clear();
		res.remove(host.getSelfPeer());
		return res;
	}

	public HashSet<Peer> getBFSPeers() {
		HashSet<Peer> res;
		synchronized (activePeers) {
			res = new HashSet<Peer>();
		}
		res.addAll(tempPeers);
		Peer self = host.getSelfPeer();
		res.remove(self);

		return res;
	}

	public void addPeers(HashSet<Peer> tempSet) {
		synchronized (nextPeerSet) {
			nextPeerSet.addAll(tempSet);
		}

	}

	public void addPeer(Peer peer) {
		tempPeers.add(peer);

	}

	public void flipBuffers() {
		synchronized (nextPeerSet) {
			nextPeerSet.remove(host.getSelfPeer());
			Iterator<Peer> it = nextPeerSet.iterator();
			while (it.hasNext()) {
				Peer p = it.next();
				if (p.getPort() == 0)
					it.remove();
			}
		}
		synchronized (activePeers) {
			synchronized (nextPeerSet) {
				activePeers = new HashSet<Peer>(nextPeerSet);
			}
		}
		nextPeerSet.clear();
	}

	public HashSet<Peer> getActivePeers() {
		return activePeers;
	}

}
