package peers;

/**
 * Program klienta. Argumenty: [host] [numer portu]
 * 
 * @author lukasz
 * 
 */
public class Program {
	public static void main(String[] args) {
		String host = args[0];
		int serverport = Integer.parseInt(args[1]);

		int trackerport = 7777;
		String trackerhost = "localhost";

		PeerClient client = new PeerClient(trackerhost, trackerport);
		PeerServer server = new PeerServer(host, serverport, trackerport,
				trackerhost);
		server.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		client.translate();
		System.out.println("Translation completed now only server is running.");
	}
}
