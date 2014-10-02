package aos.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

public class AckHandler implements Runnable{
	PrintWriter writer = new Client().writer;
	Socket ackAclient;
	String serverName;
	int port;
	String ackProcess = "process"+(new Client().thisProcessNumber);
	
	public AckHandler(String processName) {
		//sock = parts;
		try {
			Properties ServerPort = new Usefulmethods().getPropertiesFile("serverport.properties");
			serverName = ServerPort.getProperty(processName);
			String portString = ServerPort.getProperty(processName+"Port");//Integer.parseInt(args[1]);
			port = Integer.parseInt(portString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			
			System.out.println("Connecting to " + serverName + " on port "+ port);
			ackAclient = new Socket(serverName, port);
			System.out.println("Just connected to "	+ ackAclient.getRemoteSocketAddress());
			out = new PrintWriter(ackAclient.getOutputStream(), true);
			in =new BufferedReader(new InputStreamReader(ackAclient.getInputStream()));

			out.println("Acknowledgement:"+ackProcess);
			
			closeEverything(out, in);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void closeEverything(PrintWriter out, BufferedReader in) {
		try {
			out.close();
			in.close();
			ackAclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
