package aos.com;

import java.net.*;
import java.util.*;

public class Server {
	ArrayList listOfProcess;
	int port = 5000;
	
	public static void main(String[] args) {
		new Server().go();
	}
	
	private void go() {
		listOfProcess = new ArrayList();
		try{
			ServerSocket sock = new ServerSocket(port);
			
			while(true) {
				Socket processSock = sock.accept();
				listOfProcess.add(processSock);
				
				Thread t = new Thread(new ProcessHandler(processSock));
				t.start();
				System.out.println("got connection");
				sock.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}