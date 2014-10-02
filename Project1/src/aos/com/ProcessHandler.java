package aos.com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProcessHandler implements Runnable{
	PrintWriter writer = new Client().writer;
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
		    	final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				
				if(parts[0].contains("ComputationMessage")) {
					
			    	writer.println("Computation Message received at time : "+ sdf.format(cal.getTime()) );
			    	writer.println("Sender of the computation message is : "+ parts[1] );
			    	
					if(Message.getNoOfAckMsg() == 0) {
						Message.setNoOfAckMsg(1);
						Message.setParent(parts[1]);
						writer.println("This Comptation Message is the first one" );
					} else {
						writer.println("Sending an Acknowledgement Since already in tree" );
						Thread th = new Thread(new AckHandler(parts[1]));
						th.start();
					}
				} else if(parts[0].contains("Acknowledgement")) {
					
					writer.println("Acknowledgement Message received at time : "+ sdf.format(cal.getTime()) );
			    	writer.println("Sender of the Acknowledgement message is : "+ parts[1] );
					
					int ackCount = Message.getNoOfAckMsg();
					if(ackCount != 0) {
						ackCount = ackCount - 1;
						Message.setNoOfAckMsg(ackCount);
						writer.println("Number of ACK message yet to be received: "+ ackCount );
						if(ackCount == 1) {
							if(Message.isIdeal()) {
								writer.println("Detaching child node from Parent : "+ sdf.format(cal.getTime()) );
								writer.println("Sending ACK to parent and detaching from tree");
								sendAckToParent();
							} else {
								//Not sure of this
								break; 
							}
						}
					} else {
						//this is a root process
						if(Message.isIdeal()) {
							writer.println("TERMINATE : "+ sdf.format(cal.getTime()) );
							TerminateWithRoot twr = new TerminateWithRoot(parts[1]);
							twr.go();
						} else {
							// Not sure of this
							break;
						}
					}
				} else if(parts[0].contains("TerminateMessage")) {
					writer.println("computation terminated : "+ sdf.format(cal.getTime()) );
					// terminate the process still to be done
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendAckToParent() {
		String parent = Message.getParent();
		Message.setNoOfAckMsg(0);
		Message.setParent(null);
		Message.setIdeal(true);
		Thread th = new Thread(new AckHandler(parent));
		th.start();
	}

}
