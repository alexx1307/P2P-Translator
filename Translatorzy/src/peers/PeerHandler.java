package peers;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

import tracker.Tracker;
import tracker.TrackerProtocol;

/**
 * Klasa watku w ktorym obslugiwane jest polaczenia trackera z peerem.
 * 
 * @author lukasz
 * 
 */
public class PeerHandler extends Thread {
	private Socket socket;
	private Tracker tracker;

	public PeerHandler(Socket socket, LinkedList<PeerInfo> peers,
			Tracker tracker) {
		this.socket = socket;
		this.tracker = tracker;
	}

	@Override
	public void run() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			TrackerProtocol tprotocol = new TrackerProtocol(tracker);
			String inputLine, outputLine;

			while ((inputLine = in.readLine()) != null) {

				outputLine = tprotocol.processServerInput(inputLine);
				out.println(outputLine);

				if (inputLine.equals("BYE"))
					break;
			}

			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
