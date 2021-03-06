package peers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;

/**
 * Klasa reprezentujaca tlumacza.
 * 
 * @author lukasz
 * 
 */
public class Translator {
	private int port;
	private Host host;

	public Translator(Host host) {
		this.port = host.getServerPort();
		this.host = host;

		Logger.write("Starting translator on port: " + port);
	}

	/*
	 * Funkcja która tłumaczy podany String.
	 */
	public synchronized String translate(String s) {
		String result = s.toUpperCase();
		return result;
	}

	/**
	 * Funkcja tworzaca watek na ktorym tlumacz oczekuje na dane. Wersja wstepna, tlumacz czeka
	 * 5s, zamienia litery na duze, i wysyla klientowi. Przesyłanie danych jest kodowane.
	 */
	public int createNewTranslatorThread(String filename, String langFrom, String langTo, String price, PublicKey pk) {
		final int port = host.findFreePort();
		final PublicKey publicKey=pk;
		Thread t = new Thread() {
			Socket socket;

			public void run() {
				Logger.write("TranslatorThread is running on port: " + port);
				try (ServerSocket serverSocket = new ServerSocket(port)) {

					socket = serverSocket.accept();
					
					Logger.write("TranslatorThread accept connection from port: "+socket.getPort());

					PrintWriter out = new PrintWriter(socket.getOutputStream(),
							true);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));

					String inputLine, outputLine;

					while ((inputLine = in.readLine()) != null) {
						Thread.sleep(5000);
						outputLine = translate(host.getEncrypter().decode(inputLine, host.getEncrypter().getPrivateKey()));
						out.println(host.getEncrypter().code(outputLine, publicKey));
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
				Logger.write("Translator Thread on port "+port+" ended");
			}
		};
		t.start();
		host.addThread(t);
		
		return port;
	}
}