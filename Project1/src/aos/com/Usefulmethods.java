package aos.com;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Random;

public class Usefulmethods {
private static volatile Usefulmethods instance = null;
	
	private Usefulmethods() {
		
	}

	public static Usefulmethods getUsefulmethodsInstance() {
		synchronized (Usefulmethods.class) {
			// Double check
			if (instance == null) {
				System.out.println("Usefulmethods : I am being created");
				instance = new Usefulmethods();
			}
		}
		return instance;
	}
	
	public Properties getPropertiesFile(String filename) {
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

	public double generateRandomValue() {
		double Low = 0;
		double High = 1;
		double randomNum = Low + (Math.random()* High);
		return randomNum;
	}

	public double generateRandomTime() {
		double Low = 0.25;
		double High = 1;
		double randomNum = Low + (Math.random()* High);
		return randomNum;
	}
	
	public BufferedWriter getWriter(String textfile) {
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			//writer = new PrintWriter(textfile, "UTF-8");
			fileWriter = new FileWriter(textfile, true);
			bufferedWriter = new BufferedWriter(fileWriter);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bufferedWriter;
	}
	
	public int getRandomProcessNumber () {
		Random r = new Random();
		int Low = 1;
		int High = 5; // This should be 15
		int randomNum = r.nextInt((High - Low) + 1) + Low;
		return randomNum;
	}
	
	public void sendAckToParent(Message message) {
		String parent = message.getParent();
		System.out.println("parent was : "+ parent);
		message.setNoOfAckToBeSent(0);
		message.setParent(null);
		message.setIdeal(true);
		Thread th = new Thread(new AckHandler(parent));
		th.start();
	}
}
