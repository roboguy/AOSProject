package aos.com;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server implements Runnable {
	ServerSocket serverSock;
	int processNumber;
	Message message;
	
	public Server(Message msg) {
		processNumber = Message.processNumber;
		message = msg;
	}

	public void run() {
		Properties ServerPort = Usefulmethods.getUsefulmethodsInstance().getPropertiesFile("serverport.properties");
		String portString = ServerPort.getProperty("process"+processNumber+"Port");//Integer.parseInt(args[1]);
		int port = Integer.parseInt(portString);
		System.out.println("process"+processNumber + " : server");
		try{
			serverSock = new ServerSocket(port);
			while(true) {
				Socket client = serverSock.accept();
				
				Thread t = new Thread(new ProcessHandler(client, message));
				t.start();				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
