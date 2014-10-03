package aos.com;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class Client {
	BufferedWriter writer = new Usefulmethods().getWriter("process1.txt");
	Socket client = null;
	int thisProcessNumber = 3;
	
	public static void main(String[] args) throws IOException {
		
		Client process1 = new Client();
		
		//Should be set to 26
		for(int i=0; i< 6; i++) {
			double time = new Usefulmethods().generateRandomTime();
			
			double value = new Usefulmethods().generateRandomValue();
			if(value >= 0 && value < 0.1) {
				//Put the process on idle state
				process1.handleIdealState();
				break;
			} else {
				// For computation message count
				int compMsg = Message.getNoOfAckToBeReceived();
				compMsg = compMsg + 1;
				Message.setNoOfAckToBeReceived(compMsg);
				System.out.println("Computation message # : "+compMsg);
				
				process1.sendComputationMessage(time);
			}
		}
		System.out.println("outside for loop in main");
		Message.setIdeal(true);
		if(Message.isIdeal() && (Message.root).equals("process"+process1.thisProcessNumber)) {
			// This is the root process
			if((Message.getNoOfAckToBeReceived() == 0)) {
				System.out.println("inside the root process");

				final Calendar cal = Calendar.getInstance();
		    	final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
				
				(process1.writer).write("TERMINATE : "+ sdf.format(cal.getTime()) +"\n");
				TerminateWithRoot twr = new TerminateWithRoot("process"+process1.thisProcessNumber);
				twr.go();
				(process1.writer).flush();
			}
		} 
		else if(Message.isIdeal() && (Message.getNoOfAckToBeSent() == 1)) {
			System.out.println("detaching child from parent");
			final Calendar cal = Calendar.getInstance();
	    	final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
			
	    	(process1.writer).write("Detaching child node from Parent : "+ sdf.format(cal.getTime()) +"\n");
			(process1.writer).write("Sending ACK to parent and detaching from tree"+"\n");
			(process1.writer).flush();
			new Usefulmethods().sendAckToParent();
		}
	}
	
	public void handleIdealState() {
		final Calendar cal = Calendar.getInstance();
    	final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
    	try {
	    	writer.write("From Active to Idle"+ sdf.format(cal.getTime()) +"\n");
	    	writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		Message.setIdeal(true);
	}

	private void SetUpNetworking(int randomNum) {
		
		//randomNum = 1;
		Properties ServerPort = new Usefulmethods().getPropertiesFile("serverport.properties");
		
		String serverName = ServerPort.getProperty("process"+randomNum);
		String portString = ServerPort.getProperty("process"+randomNum+"Port");//Integer.parseInt(args[1]);
		int port = Integer.parseInt(portString.trim());
		System.out.println("port is : "+ port);
		
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
		
		int randomNum = new Usefulmethods().getRandomProcessNumber(thisProcessNumber);
		String randomprocess = "process"+randomNum;
		if(randomprocess.equals(Message.root)) {
			return;
		}
		
		final Calendar cal = Calendar.getInstance();
    	final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		
    	try {
			Thread.sleep((long) (time*1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
    	try {
    		writer.write("Sending Computation Message at time : "+ sdf.format(cal.getTime()) +"\n");
        	writer.write("The process to which the message is sent is : process" +randomNum+"\n");
			writer.write("Number of process for which ACK's are yet to received" +Message.getNoOfAckToBeReceived()+"\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	SetUpNetworking(randomNum);	
	}
	
	private void closeEverything(PrintWriter out, BufferedReader in) {
		try {
			out.close();
			in.close();
			client.close();
			//writer.close();
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