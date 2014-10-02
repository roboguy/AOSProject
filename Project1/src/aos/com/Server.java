package aos.com;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

public class Server {
	ArrayList listOfProcess;
	ServerSocket serverSock;
	
	public void go() {
		int processNumber = new Client().thisProcessNumber;
		Properties ServerPort = new Usefulmethods().getPropertiesFile("serverport.properties");
		String portString = ServerPort.getProperty("process"+processNumber+"Port");//Integer.parseInt(args[1]);
		int port = Integer.parseInt(portString);
		
		try{
			serverSock = new ServerSocket(port);
			while(true) {
				Socket client = serverSock.accept();
				
				Thread t = new Thread(new ProcessHandler(client));
				t.start();				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
    public static void main(String[] args) {
    	Server ss = new Server();
    	ss.go();
    }
}
