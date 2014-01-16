package peers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

/**
 * Klasa reprezentujaca tlumacza.
 * 
 * @author lukasz
 *
 */
public class Translator {
	private int port;
	private int trackerPort;
	private String trackerHost;
	private String hostName;
	private Host host;

	public Translator(Host host) {
		this.trackerHost = host.getTrackerHost();
		this.trackerPort = host.getTrackerPort();
		this.port = host.getServerPort();
		this.hostName = host.getHostName();
		this.host = host;

		System.out.println("Starting translator on port: " + port);

		register();
	}

	public synchronized String translate(String s) {
		String result = s.toUpperCase();
		return result;
	}

	public void register() {
		try {
			Socket socket = new Socket(trackerHost, trackerPort);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String inputLine;
			out.println("REGISTER " + hostName + " " + port);
			while((inputLine=in.readLine())!=null){
			  if(inputLine.equals("BYE"))
				  break;
			  out.println("BYE");
			}
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int createNewTranslatorThread() {
		final int port = host.findFreePort();
		Thread t = new Thread() {
			Socket socket;

			public void run() {
				System.out.println("TranslatorThread is running on port: "
						+ port);
				try (ServerSocket serverSocket = new ServerSocket(port)) {

					socket = serverSocket.accept();

					PrintWriter out = new PrintWriter(socket.getOutputStream(),
							true);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));

					String inputLine, outputLine;

					while ((inputLine = in.readLine()) != null) {
						Thread.sleep(5);
						outputLine = translate(inputLine);
						out.println(outputLine);
					}

				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
		return port;
	}
}
