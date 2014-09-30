package aos.com;

import java.io.IOException;
import java.net.*;

public class Server {
	int port = 6000;
	
	public static void main(String[] args) {
		new Server().go();
	}
	
	public void go() {
		ServerSocket sock = null;
		try{
			sock = new ServerSocket(port);
			
			while(true) {
				Socket processSock = sock.accept();
				
				Thread t = new Thread(new ProcessHandler(processSock));
				t.start();
				System.out.println("got connection");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
	       sock.close();
	    } 
	    catch (IOException e) {
	       System.out.println(e);
	    }
	}
}