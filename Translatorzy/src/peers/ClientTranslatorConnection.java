package peers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
				if(inputLine.equals("TRANSLATE")){
					int port=host.getTranslator().createNewTranslatorThread();
					outputLine="OK "+host.getHostName()+" "+port;
					out.println(outputLine);
				}else if(inputLine.equals("DATA")){
					
				}else{
					break;
				}
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
