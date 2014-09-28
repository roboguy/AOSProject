package aos.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;

public class Process1 {
	ArrayList processes;
	
	public class processHandler implements Runnable {
		BufferedReader reader;
		Socket sock;
		
		public processHandler(Socket processSocket) {
			sock = processSocket;
			try {
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			String message;
			try {
				while((message = reader.readLine()) != null) {
					System.out.println("read" + message);
					tellEveryone(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void tellEveryone(String message) {
		Iterator it = processes.iterator();
		while(it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void go() {
		processes = new ArrayList();
		try {
			ServerSocket server = new ServerSocket(5000);
			
			while(true) {
				Socket processSock = server.accept();
				PrintWriter writer = new PrintWriter(processSock.getOutputStream());
				processes.add(writer);
				
				Thread t = new Thread(new processHandler(processSock));
				t.start();
				System.out.println("got connection");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new Process1().go();
	}
}
