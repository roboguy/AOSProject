package aos.com;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class Process1Client {
	PrintWriter writer = getWriter("process1.txt");
	
	public static void main(String[] args) {
		
		Process1Client process1 = new Process1Client();				
		double time = process1.generateRandomTime();
		
		double value = process1.generateRandomValue();
		if(value >= 0 && value < 0.1) {
			//Put the process on idle state
		} else {
			process1.sendComputationMessage(time);
		}
	}
	
	private void SetUpNetworking(int randomNum) {
		Properties ServerPort = getServerAndPort();
		
		/*String serverName = ServerPort.getProperty("process"+randomNum);//args[0];
		String portString = ServerPort.getProperty("process"+randomNum+"Port");//Integer.parseInt(args[1]);
		int port = Integer.parseInt(portString);*/
		String serverName = "localhost";
		int port = 6000;
		Socket client = null;
		PrintWriter out = null;
		BufferedReader in = null;
		
		try {
			
			System.out.println("Connecting to " + serverName + " on port "+ port);
			client = new Socket(serverName, port);
			System.out.println("Just connected to "	+ client.getRemoteSocketAddress());
			out = new PrintWriter(client.getOutputStream(), true);
			in =new BufferedReader(new InputStreamReader(client.getInputStream()));

			out.println("ProcessName: "+ serverName);
			out.println("ProcessPort: "+ port);
			
			System.out.println("Server says " + in.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			out.close();
	        in.close();
	        client.close();
	    } 
	    catch (IOException e) {
	       System.out.println(e);
	    }
	}

	private void sendComputationMessage(double time) {
		
		Random r = new Random();
		int Low = 1;
		int High = 15;
		final int randomNum = Low + r.nextInt(Low + High);
		System.out.println("Random process number : "+randomNum);
		final Calendar cal = Calendar.getInstance();
    	final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		// Filter out the own process
		if(randomNum != 1) {
			System.out.println("time interval is : "+ (long) time);
			try {
				Thread.sleep((long) (time*1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
        	writer.println("Sending time is : "+ sdf.format(cal.getTime()) );
        	writer.println("The process to which the message is sent is : process" +randomNum);
        	SetUpNetworking(randomNum);
			
		} else {
			sendComputationMessage(time);
		}
		writer.close();
	}

	private double generateRandomValue() {
		double Low = 0;
		double High = 1;
		double randomNum = Low + (Math.random()* High);
		return randomNum;
	}

	private double generateRandomTime() {
		double Low = 0.25;
		double High = 1;
		double randomNum = Low + (Math.random()* High);
		return randomNum;
	}
	
	private PrintWriter getWriter(String textfile) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(textfile, "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return writer;
	}
	
	private Properties getServerAndPort() {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("resource/serverport.properties");
			prop.load(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
}