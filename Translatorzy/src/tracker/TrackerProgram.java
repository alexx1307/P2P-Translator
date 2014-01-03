package tracker;

/**
 * Program do uruchomienia trackera. argument: [numer portu]
 * 
 * @author lukasz
 * 
 */
public class TrackerProgram {
	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		Tracker tracker = new Tracker(port);
		tracker.start();
	}
}
