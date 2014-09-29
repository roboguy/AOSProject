package aos.com;

import java.net.Socket;

public class ProcessHandler implements Runnable {
	Socket processSocket;
	
	public ProcessHandler(Socket processSock) {
		processSocket = processSock;
	}

	public void run() {
		acknowledgement();
	}

	private void acknowledgement() {
		SetUpNetworking();
		//Acknowledgement code goes here 
	}

	private void SetUpNetworking() {
		//Set up network for each individual process
	}

}
