package peers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.StringTokenizer;

/**
 * Klasa obslugujaca polaczenie pomiedzy klientem a tlumaczem.
 * 
 * @author lukasz
 *
 */
public class ClientTranslatorConnection extends Thread {
	private Socket socket;
	private Host host;

	public ClientTranslatorConnection(Host host, Socket socket) {
		this.socket = socket;
		this.host=host;
	}

	@Override
	public void run() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
            
			
			String inputLine, outputLine;

			while ((inputLine = in.readLine()) != null) {
				if(inputLine.startsWith("TRANSLATE ")){
					StringTokenizer st = new StringTokenizer(inputLine);
					String filename=null;
					String langFrom=null;
					String langTo=null;
					String price=null;
					BigInteger keyM = null;
					BigInteger keyE = null;
					
					if (st.hasMoreTokens())
						st.nextToken();
					if (st.hasMoreTokens())
						filename = st.nextToken();
					if (st.hasMoreTokens())
						langFrom = st.nextToken();
					if (st.hasMoreTokens())
						langTo = st.nextToken();
					if (st.hasMoreTokens())
						price = st.nextToken();
					if (st.hasMoreTokens())
						keyM = new BigInteger(st.nextToken());
					if (st.hasMoreTokens())
						keyE = new BigInteger(st.nextToken());
					
					int port=host.getTranslator().createNewTranslatorThread(filename,langFrom,langTo,price,Encrypter.makePublicKey(keyM, keyE));
					outputLine="200 ACCEPT "+port;
					out.println(outputLine);
				}else if(inputLine.startsWith("RANK ")){
					outputLine="202 RANK RECEIVE";
					out.println(outputLine);
				}else if(inputLine.startsWith("END ")){
					outputLine="203 TRANSLATE ENDED CORRECTLY";
					out.println(outputLine);
					break;
				}else{
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
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