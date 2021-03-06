package peers;

import java.util.Scanner;

/**
 * Klasa klienta.
 * 
 * @author lukasz
 * 
 */
public class Client extends Thread {
	private Host host;

	public Client(Host host) {
		this.host = host;
	}

	@Override
	public void run() {
		Logger.write("Starting client");
		TranslateDocument translateDocument=null;

		System.out.println("Enter the file name to translate:");
		Scanner in = new Scanner(System.in);
		boolean listen=true;
		while (listen) {
			String filename = in.nextLine();
			translateDocument = new TranslateDocument(filename, host);
			translateDocument.start();
		}
		in.close();

		try {
			translateDocument.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
