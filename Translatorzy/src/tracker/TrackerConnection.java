package tracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

import peers.Peer;

/**
 * Klasa obslugujaca w osobnym watku polaczenie miedzy trackerem a klientem.
 * 
 * @author lukasz
 * 
 */
public class TrackerConnection extends Thread {
	private Socket socket;
	private Tracker tracker;

	public TrackerConnection(Socket socket, LinkedList<Peer> peers,
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
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException ex) {
				}
			}
		}

	}

}