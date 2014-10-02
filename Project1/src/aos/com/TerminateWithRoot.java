package aos.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;

public class TerminateWithRoot implements Runnable {
	PrintWriter writer = new Client().writer;
	String rootProcess;
	int rootProcessNumber;
		
	public TerminateWithRoot(String process) {
		rootProcess = process;
	}

	public void run() {	
	}
	
	public void go() {
		Properties ServerPort = new Usefulmethods().getPropertiesFile("serverport.properties");
		PrintWriter out = null;
		BufferedReader in = null;
		
		int len = rootProcess.length();
		if(len == 8) {
			String root = rootProcess.substring(7, 8);
			rootProcessNumber = Integer.parseInt(root);
		} else if (len == 9) {
			String root = rootProcess.substring(7, 9);
			rootProcessNumber = Integer.parseInt(root);
		}
		
		for(int i=1 ; i < 16; i++) {
			if(i == rootProcessNumber) {
				// do nothing
			} else {
				String serverName = ServerPort.getProperty("process"+i);
				String portString = ServerPort.getProperty("process"+i+"Port");//Integer.parseInt(args[1]);
				int port = Integer.parseInt(portString);
				
				try {	
					System.out.println("Connecting to " + serverName + " on port "+ port);
					Socket client = new Socket(serverName, port);
					System.out.println("Just connected to "	+ client.getRemoteSocketAddress());
					out = new PrintWriter(client.getOutputStream(), true);
					in =new BufferedReader(new InputStreamReader(client.getInputStream()));

					out.println("TerminateMessage:"+ rootProcess);
					
					//readResponse();
					closeEverything(out, in, client);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private void closeEverything(PrintWriter out, BufferedReader in, Socket client) {
		try {
			out.close();
			in.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
