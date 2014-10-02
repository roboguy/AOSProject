package aos.com;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
* A simple socket server
* @author faheem
*
*/
public class Server {
	ArrayList listOfProcess;
	ServerSocket serverSock;
	
	public void go() {
		try{
			serverSock = new ServerSocket(5000);
			while(true) {
				Socket client = serverSock.accept();
				
				Thread t = new Thread(new ProcessHandler(client));
				Thread th = new Thread(new AckHandler(client));
				t.start();
				th.start();
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
    * Creates a SocketServer object and starts the server.
    *
    * @param args
    */
    public static void main(String[] args) {
    	Server ss = new Server();
    	ss.go();
    }
}
