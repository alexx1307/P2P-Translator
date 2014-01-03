package peers;

/**
 * Protokol komunikacyjny pomiedzy peerami.
 * @author lukasz
 *
 */
public class P2PProtocol {
	
	public String processServerInput(String input){
		String result=null;
		
		
		if (input.length()>8 && input.substring(0, 9).equals("TRANSLATE")) {
			result="TRANSLATE_OK "+input.substring(9);
		} else if (input.equals("BYE")) {
			result = "BYE";
		} else if (input.equals("HELLO")) {
			result = "HELLO";
		} else {
			result = "ERROR";
		}
		return result;
	}
}
