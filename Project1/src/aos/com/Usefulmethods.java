package aos.com;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class Usefulmethods {
	
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
}
