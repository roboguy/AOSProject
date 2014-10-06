package aos.com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProcessHandler implements Runnable{
	Usefulmethods usefulMethods = Usefulmethods.getUsefulmethodsInstance();
	BufferedWriter writer;
	BufferedReader reader;
	Socket sock;
	Message message;
	
	public ProcessHandler(Socket client, Message msg) {
		sock = client;
		message = msg;
		writer = usefulMethods.getWriter("process"+Message.processNumber+".txt");
		try {
			InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(isReader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		String msg;
		try {
			while((msg = reader.readLine()) != null) {
				System.out.println("Server : "+ msg);
				String[] parts = msg.split(":");
				final Calendar cal = Calendar.getInstance();
		    	final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
				
				if(parts[0].contains("ComputationMessage")) {
					
			    	writer.flush();
					if(message.getNoOfAckToBeSent() == 0) {
						message.setNoOfAckToBeSent(message.getNoOfAckToBeSent() + 1);
						writer.write("Computation Message received at time : "+ sdf.format(cal.getTime()) + " Sender : " + parts[1] +
				    			" Count of Ack to send Back : "+  message.getNoOfAckToBeSent() +"\n");
						message.setParent(parts[1]);
						writer.write("This Comptation Message is the first one" +"\n");
						writer.flush();
					} else {
						writer.write("Computation Message received at time : "+ sdf.format(cal.getTime()) + " Sender : " + parts[1] +
				    			" Count of Ack to send Back : "+  message.getNoOfAckToBeSent() +"\n");
						writer.write("Sending an Acknowledgement Since already in tree" +"\n");
						Thread th = new Thread(new AckHandler(parts[1]));
						th.start();
						writer.flush();
					}
				} else if(parts[0].contains("Acknowledgement")) {
					
					int ackCount = message.getNoOfAckToBeReceived();
					System.out.println("Server : Yes i am in Acknowledgement if block : " + ackCount);
					if(ackCount > 1) {
						ackCount = ackCount - 1;
						message.setNoOfAckToBeReceived(ackCount);
						writer.write("Acknowledgement Message received at time : "+ sdf.format(cal.getTime()) + " Sender : " + parts[1] +
								" Count of Ack to be Received : "+ ackCount +"\n");
						writer.flush();
					} else if (ackCount == 1) {
						ackCount = ackCount - 1;
						message.setNoOfAckToBeReceived(ackCount);
						writer.write("Acknowledgement Message received at time : "+ sdf.format(cal.getTime()) + " Sender : " + parts[1] +
								" Count of Ack to be Received : "+ ackCount +"\n");
						writer.flush();
					}
				} else if(parts[0].contains("TerminateMessage")) {
					Message.setTerminate(true);
					writer.write("computation terminated : "+ sdf.format(cal.getTime())+"\n" );
					writer.flush();
					// terminate the process still to be done
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
