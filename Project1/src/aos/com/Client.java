package aos.com;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class Client implements Runnable {
	Usefulmethods usefulMethods = Usefulmethods.getUsefulmethodsInstance();
	BufferedWriter writer;
	Socket client = null;
	int thisProcessNumber;
	Message message;
	
	public Client(Message msg) {
		thisProcessNumber = Message.processNumber;
		System.out.println("process number is : "+ thisProcessNumber);
		writer = usefulMethods.getWriter("process"+thisProcessNumber+".txt");
		message = msg;
	}
	public void run() {
		
		//Should be set to 26
		for(int i=0; i< 10; i++) {
			double time = usefulMethods.generateRandomTime();
			
			double value = usefulMethods.generateRandomValue();
			if(value >= 0 && value < 0.1) {
				//Put the process on idle state
				handleIdealState();
				break;
			} else {
				// For computation message count
				int compMsg = message.getNoOfAckToBeReceived();
				compMsg = compMsg + 1;
				message.setNoOfAckToBeReceived(compMsg);
				System.out.println("process"+thisProcessNumber + ": Computation message # : "+ message.getNoOfAckToBeReceived());
				
				sendComputationMessage(time);
			}
		}
		handleIdealState();
		System.out.println("outside for loop in main");	
		detachFromTree();
		
		while(true) {
			System.out.println("inside root about to terminate");
			if(("process"+Message.processNumber).equals(message.getRoot())) {
				if(message.getNoOfAckToBeReceived() == 0) {
					final Calendar cal = Calendar.getInstance();
			    	final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
					try {
						writer.write("TERMINATE : "+ sdf.format(cal.getTime()) +"\n");
						writer.flush();
						Thread t = new Thread(new TerminateWithRoot());
						t.start();
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("breaking out of terminate");
					break;
				}
			} else {
				System.out.println("These are not root process");
				break;
			}
			
		}
		
		if(!("process"+Message.processNumber).equals(message.getRoot())) {
			System.out.println("waiting for termination");
			while(!Message.isTerminate()) {
				if(message.getNoOfAckToBeSent() > 0) {
					detachFromTree();
				}
			}
		}
		System.out.println("done with everything");
	}
	
	private void detachFromTree() {
		while(true) {
			System.out.println("inside detach from tree");
			if(!("process"+Message.processNumber).equals(message.getRoot())) {
				if(message.getNoOfAckToBeSent() == 1 && message.getNoOfAckToBeReceived() == 0) {
					final Calendar cal = Calendar.getInstance();
			    	final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
					try {				
						writer.write("Detaching child node from Parent : "+ sdf.format(cal.getTime()) +"\n");
						writer.flush();
						usefulMethods.sendAckToParent(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("breaking out from detaching from tree");
					break;
				}
			} else {
				System.out.println("This is a root process so breaking");
				break;
			}
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
    	
    	message.setIdeal(true);
	}

	private void SetUpNetworking(int randomNum) {
		
		//randomNum = 1;
		Properties ServerPort = usefulMethods.getPropertiesFile("serverport.properties");
		
		String serverName = ServerPort.getProperty("process"+randomNum);
		String portString = ServerPort.getProperty("process"+randomNum+"Port");//Integer.parseInt(args[1]);
		int port = Integer.parseInt(portString.trim());
		System.out.println("port is : "+ port);
		
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			
			System.out.println("process"+thisProcessNumber +   " : Connecting to " + serverName + " on port "+ port);
			client = new Socket(serverName, port);
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
		
		int randomNum = usefulMethods.getRandomProcessNumber();
		while(randomNum == thisProcessNumber) {
			randomNum = usefulMethods.getRandomProcessNumber();
		}
		String randomprocess = "process"+randomNum;
		if(randomprocess.equals(message.getRoot())) {
			message.setNoOfAckToBeReceived(message.getNoOfAckToBeReceived() - 1);
			System.out.println("process"+randomNum + " : Its the root so i am not sending the computation message");
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
    		writer.write("Sending Computation Message at time : "+ sdf.format(cal.getTime()) + "Sent to process : " +randomNum+ 
    				"Count of ack to be received : " + message.getNoOfAckToBeReceived() +"\n");
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