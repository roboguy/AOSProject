package aos.com;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class Client {
	PrintWriter writer = new Usefulmethods().getWriter("process1.txt");
	Socket client = null;
	int thisProcessNumber = 1;
	
	public static void main(String[] args) {
		
		Client process1 = new Client();
		
		//Should be set to 26
		for(int i=0; i< 10; i++) {
			double time = new Usefulmethods().generateRandomTime();
			
			double value = new Usefulmethods().generateRandomValue();
			if(value >= 0 && value < 0.1) {
				//Put the process on idle state
				process1.handleIdealState();
				break;
			} else {
				// For computation message count
				int compMsg = Message.getNoOfComputationMsg();
				compMsg = compMsg + 1;
				Message.setNoOfComputationMsg(compMsg);
				System.out.println("Computation message # : "+compMsg);
				
				// For acknowledge message count
				int ackCount = Message.getNoOfAckMsg();
				ackCount = ackCount + 1;
				Message.setNoOfAckMsg(ackCount);
				System.out.println("acknowledgement message # : "+ackCount);
				
				process1.sendComputationMessage(time);
			}
		}
		Message.setIdeal(true);
	}
	
	public void handleIdealState() {
		final Calendar cal = Calendar.getInstance();
    	final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	writer.println("Sending time is : "+ sdf.format(cal.getTime()) );
    	writer.println("From Active to Idle");
    	
		Message.setIdeal(true);
	}

	private void SetUpNetworking(int randomNum) {
		
		//randomNum = 1;
		Properties ServerPort = new Usefulmethods().getPropertiesFile("serverport.properties");
		
		String serverName = ServerPort.getProperty("process"+randomNum);
		String portString = ServerPort.getProperty("process"+randomNum+"Port");//Integer.parseInt(args[1]);
		int port = Integer.parseInt(portString);
		
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			
			System.out.println("Connecting to " + serverName + " on port "+ port);
			client = new Socket(serverName, port);
			System.out.println("Just connected to "	+ client.getRemoteSocketAddress());
			out = new PrintWriter(client.getOutputStream(), true);
			in =new BufferedReader(new InputStreamReader(client.getInputStream()));

			out.println("ComputationMessage:"+ "process"+thisProcessNumber);
			
			//readResponse();
			closeEverything(out, in);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendComputationMessage(double time) {
		
		Random r = new Random();
		int Low = 1;
		int High = 3; // This should be 15
		final int randomNum = Low + r.nextInt(Low + High);
		System.out.println("Random process number : "+randomNum);
		
		final Calendar cal = Calendar.getInstance();
    	final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		// Filter out the own process
		if(randomNum != thisProcessNumber) {
			try {
				Thread.sleep((long) (time*1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
        	writer.println("Sending time is : "+ sdf.format(cal.getTime()) );
        	writer.println("The process to which the message is sent is : process" +randomNum);
        	writer.println("Number of process for which ACK's are yet to received" +Message.getNoOfAckMsg());
        	SetUpNetworking(randomNum);
			
		} else {
			sendComputationMessage(time);
		}
		writer.close();
	}
	
	private void closeEverything(PrintWriter out, BufferedReader in) {
		try {
			out.close();
			in.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readResponse() throws IOException {
		String userInput;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				client.getInputStream()));

		System.out.println("Response from server:");
		while ((userInput = stdIn.readLine()) != null) {
			System.out.println(userInput);
		}
	}
}