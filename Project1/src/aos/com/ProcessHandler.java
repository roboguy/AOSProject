package aos.com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProcessHandler implements Runnable{
	BufferedWriter writer = new Usefulmethods().getWriter("process1.txt");
	BufferedReader reader;
	Socket sock;
	
	public ProcessHandler(Socket client) {
		sock = client;
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
					
			    	writer.write("Computation Message received at time : "+ sdf.format(cal.getTime()) +"\n");
			    	writer.write("Sender of the computation message is : "+ parts[1] +"\n");
			    	writer.flush();
					if(Message.getNoOfAckToBeSent() == 0) {
						Message.setNoOfAckToBeSent(Message.getNoOfAckToBeSent() + 1);
						Message.setParent(parts[1]);
						writer.write("This Comptation Message is the first one" +"\n");
						writer.flush();
					} else {
						writer.write("Sending an Acknowledgement Since already in tree" +"\n");
						Thread th = new Thread(new AckHandler(parts[1]));
						th.start();
						writer.flush();
					}
				} else if(parts[0].contains("Acknowledgement")) {
					
					writer.write("Acknowledgement Message received at time : "+ sdf.format(cal.getTime()) +"\n");
			    	writer.write("Sender of the Acknowledgement message is : "+ parts[1] +"\n");
					
					int ackCount = Message.getNoOfAckToBeReceived();
					if(ackCount != 0) {
						ackCount = ackCount - 1;
						Message.setNoOfAckToBeReceived(ackCount);
						writer.write("Number of ACK message yet to be received: "+ ackCount +"\n");
						writer.flush();
						if(ackCount == 1) {
							if(Message.isIdeal()) {
								writer.write("Detaching child node from Parent : "+ sdf.format(cal.getTime()) +"\n");
								writer.write("Sending ACK to parent and detaching from tree"+"\n");
								writer.flush();
								new Usefulmethods().sendAckToParent();
							} else {
								//Not sure of this
								break; 
							}
						}
					} else {
						//this is a root process
						if(Message.isIdeal()) {
							writer.write("TERMINATE : "+ sdf.format(cal.getTime()) +"\n");
							TerminateWithRoot twr = new TerminateWithRoot(parts[1]);
							twr.go();
							writer.flush();
						} else {
							// Not sure of this
							break;
						}
					}
				} else if(parts[0].contains("TerminateMessage")) {
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
