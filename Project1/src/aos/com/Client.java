package aos.com;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

import aos.com.trial.FlagObject;

public class Client {
	PrintWriter writer = getWriter("process1.txt");
	Socket client = null;
	static FlagObject fo = new FlagObject();
	String thisProcess = "process1";
	
	public static void main(String[] args) {
		
		Client process1 = new Client();
		
		for(int i=0; i< 26; i++) {
			double time = process1.generateRandomTime();
			
			double value = process1.generateRandomValue();
			if(value >= 0 && value < 0.1) {
				//Put the process on idle state
				fo.setIdeal(true);
			} else {
				process1.sendComputationMessage(time);
			}
		}
	}
	
	private void SetUpNetworking(int randomNum) {
		
		Properties ServerPort = getPropertiesFile("serverport.properties");
		
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

			out.println("ProcessName: "+ serverName);
			out.println("ProcessPort: "+ port);
			
			readResponse();
			closeEverything(out, in);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	private void sendComputationMessage(double time) {
		
		Random r = new Random();
		int Low = 1;
		int High = 5;
		final int randomNum = Low + r.nextInt(Low + High);
		System.out.println("Random process number : "+randomNum);
		
		if(findInTree(randomNum)) {
			return;
		}
		
		final Calendar cal = Calendar.getInstance();
    	final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		// Filter out the own process
		if(randomNum != 1) {
			try {
				Thread.sleep((long) (time*1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			checkAndSetRoot(thisProcess);
        	writer.println("Sending time is : "+ sdf.format(cal.getTime()) );
        	writer.println("The process to which the message is sent is : process" +randomNum);
        	SetUpNetworking(randomNum);
			
		} else {
			sendComputationMessage(time);
		}
		writer.close();
	}

	private void checkAndSetRoot(String thisProcess2) {
		if(!fo.isROOT()){
			fo.setROOT(true);
			fo.setRootProcess(thisProcess2);
		} else {
			//do nothing
		}
	}

	private boolean findInTree(int processNumber) {
		
		Properties prop = getPropertiesFile("intree.properties");
		String inTree = prop.getProperty("process"+processNumber+"");
		if(inTree.equals("false")) {
			prop.setProperty("process"+processNumber+"", "true");
			return false;
		} else {
			return true;
		}
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
	
	private Properties getPropertiesFile(String filename) {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("resource/"+filename+"");
			prop.load(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
}