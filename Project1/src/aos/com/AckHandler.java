package aos.com;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class AckHandler implements Runnable{
	
	Socket sock;
	PrintWriter writer;
	
	public AckHandler(Socket client) {
		sock = client;
		try {
			writer = new PrintWriter(sock.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		writer.println("This is message from the server");
		writer.flush();
	}

}
