package peers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnection extends Thread {
	private Socket socket;

	public ServerConnection(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
            		    
			P2PProtocol p2pprotocol=new P2PProtocol();
	        String inputLine,outputLine;
	        
	        while ((inputLine = in.readLine()) != null) {
	        	
                outputLine = p2pprotocol.processServerInput(inputLine);
                out.println(outputLine);
                
                if(inputLine.equals("BYE"))
                	break;
            }

			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
