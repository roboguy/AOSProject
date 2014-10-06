package aos.com;

public class Message {
	private static String root = "process1";
	public static int processNumber = 1;
	private static boolean isIdeal = false;
	private static String parent = null;
	private static boolean terminate = false;
	private volatile int NoOfAckToBeReceived = 0;
	private volatile int NoOfAckToBeSent = 0;	
	private static volatile Message instance = null;
	
	private Message() {
		
	}

	public static Message getInstance() {
		synchronized (Message.class) {
			// Double check
			if (instance == null) {
				System.out.println("Message : I am being created");
				instance = new Message();
			}
		}
		return instance;
	}
	
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		Message.root = root;
	}
	
	public boolean isIdeal() {
		return isIdeal;
	}
	public void setIdeal(boolean isIdeal) {
		Message.isIdeal = isIdeal;
	}
	public synchronized String getParent() {
		return parent;
	}
	public synchronized void setParent(String parent) {
		Message.parent = parent;
	}
	public synchronized int getNoOfAckToBeReceived() {
		return NoOfAckToBeReceived;
	}
	public synchronized void setNoOfAckToBeReceived(int noOfComputationMsg) {
		NoOfAckToBeReceived = noOfComputationMsg;
	}
	public synchronized int getNoOfAckToBeSent() {
		return NoOfAckToBeSent;
	}
	public synchronized void setNoOfAckToBeSent(int noOfAckMsg) {
		NoOfAckToBeSent = noOfAckMsg;
	}

	public static boolean isTerminate() {
		return terminate;
	}

	public static void setTerminate(boolean terminate) {
		Message.terminate = terminate;
	}
}
