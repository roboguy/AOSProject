package aos.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ProcessHandler implements Runnable{

	BufferedReader reader;
	Socket sock;
	
	public ProcessHandler(Socket client) {
		sock = client;
		try {
			InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(isReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		String msg;
		try {
			while((msg = reader.readLine()) != null) {
				System.out.println("Server : "+ msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
