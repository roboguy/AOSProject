package aos.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ProcessHandler implements Runnable {
	Socket processSocket;
	BufferedReader reader;
	ArrayList listOfProcess = new ArrayList();
	
	public ProcessHandler(Socket processSock) {
		processSocket = processSock;
		try {
			PrintWriter writer = new PrintWriter(processSocket.getOutputStream());
			listOfProcess.add(writer);
			
			InputStreamReader ipReader = new InputStreamReader(processSock.getInputStream());
			reader = new BufferedReader(ipReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void run() {
		String message;
		try {
			while((message = reader.readLine()) != null) {
				System.out.println("Server : " + message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		acknowledgement();
	}

	public void acknowledgement() {
		SetUpNetworking();
		//Acknowledgement code goes here 
		Iterator it = listOfProcess.iterator();
		while(it.hasNext()) {
			PrintWriter wrt = (PrintWriter) it.next();
			wrt.println("This is from server to client"); // Acknowledgment message
			wrt.flush();
		}
	}

	public void SetUpNetworking() {
		//Set up network for each individual process
	}

}
