package peers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Tlumaczenie dokumentu w osobnym watku.
 * 
 * @author lukasz
 * 
 */
public class TranslateDocument extends Thread {

	private String filename;
	private TreeMap<Integer, String> original;
	private TreeMap<Integer, String> inProgress;
	private TreeMap<Integer, String> translated;
	private Host host;
	private volatile boolean translateFinished;

	private HashSet<Peer> activePeers;
	private HashSet<Peer> peers;

	public TranslateDocument(String filename, Host host) {
		this.filename = filename;
		this.host = host;
		this.translateFinished = false;
		this.original = new TreeMap<>();
		this.inProgress = new TreeMap<>();
		this.translated = new TreeMap<>();
		this.activePeers = new HashSet<>();
		documentToLines();
	}

	/**
	 * Cykliczne zapytywanie co 5s o aktywynych tlumaczy, dopoki caly tekst nie
	 * jest przetlumaczony. Jesli sa nowi to tworzymy dla nich watki i wysylamy
	 * im tekst do tlumaczenia.
	 */
	@Override
	public void run() {
		Logger.write("Start translating file: " + filename);
		while (!translateFinished) {
			askForTranslate();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (Thread t : host.getThreads()) {
			t.interrupt();
		}
		mergeDocument();
		Logger.write("Translating " + filename + " terminated ");
	}

	/**
	 * Funkcja po zakonczeniu tlumaczenia tworzy nowy plik w ktorym znajduje sie
	 * przetlumaczony tekst.
	 */
	public void mergeDocument() {

		PrintWriter printwriter = null;
		try {
			printwriter = new PrintWriter(new FileWriter("translated"
					+ filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int key : translated.keySet()) {
			String line = translated.get(key);
			printwriter.println(line);
		}
		printwriter.close();
	}

	/**
	 * Pobiera z hosta liste aktywynych peerow, nastepnie wysyla w osobnych
	 * watkach zapytanie o tlumaczenie do wszystkich peerow.
	 */
	public void askForTranslate() {
		peers = host.getHostsUpdaterManager().getActivePeers();
		System.out.println(peers.size());
		/*
		 * Logger.write("askForTranslate size= "+peers.size());
		 * 
		 * for(Peer peer: peers){
		 * Logger.write("PEER "+peer.getHost()+" bfserver: "
		 * +peer.getBFSPort()+" port "+peer.getPort()); }
		 */
		synchronized (peers) {
			for (Peer peer : peers) {
				Logger.write("Asking peer: " + peer.getPort() + " to translate");
				if (peer.getPort() > 0 && (!activePeers.contains(peer))) {
					createNewClientThread(peer);
				}
			}
		}
	}

	/**
	 * Tworzymy dla tlumacza nowy watek ktory rozmawia tylko z nim. Watek dziala
	 * dopoki sa linie do wyslania czyli dokument nie jest jeszcze
	 * przetlumaczony.
	 * 
	 * @param peer
	 */
	public void createNewClientThread(final Peer peer) {
		synchronized (activePeers) {
			activePeers.add(peer);
		}
		Thread t = new Thread() {
			Socket dataSocket = null;

			public void run() {
				int translatorPort = 0;
				Logger.write("Trying conect command to " + peer.getHost() + " "
						+ peer.getPort());
				try (Socket commandSocket = new Socket(peer.getHost(),
						peer.getPort())) {
					PrintWriter commandOut = new PrintWriter(
							commandSocket.getOutputStream(), true);
					BufferedReader commandIn = new BufferedReader(
							new InputStreamReader(
									commandSocket.getInputStream()));

					String commandSend, commandReply;

					commandSend = "TRANSLATE " + filename;
					commandOut.println(commandSend);

					commandReply = commandIn.readLine();
					if (commandReply == null)
						return;

					if (commandReply.startsWith("200 ")) {
						StringTokenizer st = new StringTokenizer(commandReply);
						if (st.hasMoreTokens())
							st.nextToken();
						if (st.hasMoreTokens())
							st.nextToken();
						if (st.hasMoreTokens())
							translatorPort = Integer.parseInt(st.nextToken());

						Logger.write("TRANSLATOR START TRANSLATING ON PORT "
								+ translatorPort);
					} else if (commandReply.startsWith("201 ")) {
						return;
					} else {
						return;
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Logger.write("Trying connect data to " + peer.getHost()
							+ " " + translatorPort);
					dataSocket = new Socket(peer.getHost(), translatorPort);
					PrintWriter dataOut = new PrintWriter(
							dataSocket.getOutputStream(), true);
					BufferedReader dataIn = new BufferedReader(
							new InputStreamReader(dataSocket.getInputStream()));

					int lineCounter = 0;
					while (!translateFinished) {
						Chunk chunk = getLine();
						if (chunk != null) {
							Logger.write("SENDING LINE TO TRANSLATOR:  "
									+ chunk.getLine());
							dataOut.println(host.getEncrypter().code(chunk.getLine(), peer.getPublicKey()));
							String translatedLine = dataIn.readLine();
							Logger.write("REPLY LINE FROM TRANSLTOR: "
									+ translatedLine);
							if (saveLine(translatedLine, chunk.getNumber()))
								lineCounter++;
						}
					}

					commandSend = "RANK " + lineCounter;
					commandOut.println(commandSend);
					commandReply = commandIn.readLine();
					if (!commandReply.startsWith("202 ")) {
						Logger.write("Problem with sending rank to translator");
					}

					commandSend = "END TRANSLATE";
					commandOut.println(commandSend);
					commandReply = commandIn.readLine();
					if (!commandReply.startsWith("203 ")) {
						Logger.write("Problem translator termination");
					}

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (dataSocket != null) {
						try {
							dataSocket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					synchronized (activePeers) {
						activePeers.remove(peer);
					}
				}
			}
		};
		t.start();
	}

	/**
	 * Pobiera jedna linie do przetlumaczenia, jesli nie ma juz lini wolnych to
	 * pobiera linie ktora jest w trakcie tlumaczenia. Jeli caly tekst jest juz
	 * przetlumaczony zwraca null.
	 * 
	 * @return
	 */
	public synchronized Chunk getLine() {
		Chunk chunk = null;
		String line;
		int number;
		if (!original.isEmpty()) {
			line = original.firstEntry().getValue();
			number = original.firstEntry().getKey();
			original.remove(number);
			inProgress.put(number, line);
			chunk = new Chunk(line, number);
		} else if (!inProgress.isEmpty()) {
			line = inProgress.firstEntry().getValue();
			number = inProgress.firstEntry().getKey();
			chunk = new Chunk(line, number);
		} else {
			translateFinished = true;
		}
		return chunk;
	}

	/**
	 * Zapisuje dana linie do translated. Zwraca true jesli to ta linia zostala
	 * wpisana do translated.
	 * 
	 * @param line
	 */
	public synchronized boolean saveLine(String line, int number) {
		if (!translated.containsKey(number)) {
			inProgress.remove(number);
			translated.put(number, line);
			return true;
		}
		return false;
	}

	/**
	 * Otwiera plik tekstowy i kazda linie zapisuje do TreeMap.
	 */
	public void documentToLines() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(filename)));

			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				original.put(i, line);
				i++;
			}
			br.close();
		} catch (IOException e) {
		} finally {
			try {
				br.close();
			} catch (IOException e) {
			}
		}
	}

}