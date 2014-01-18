package peers;

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
		TranslateDocument translateDocument=new TranslateDocument("tekst.txt",host);
		translateDocument.start();
		try {
			translateDocument.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}