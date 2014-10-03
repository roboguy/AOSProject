package aos.com;

public class Message {
	public static String root = "process1";
	public static boolean inTree;
	public static boolean isIdeal = false;
	public static String parent = null;
	public static int NoOfAckToBeReceived = 0;
	public static int NoOfAckToBeSent = 0;
	
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		Message.root = root;
	}
	
	public static boolean isIdeal() {
		return isIdeal;
	}
	public static void setIdeal(boolean isIdeal) {
		Message.isIdeal = isIdeal;
	}
	public static boolean isInTree() {
		return inTree;
	}
	public static void setInTree(boolean inTree) {
		Message.inTree = inTree;
	}
	public static String getParent() {
		return parent;
	}
	public static void setParent(String parent) {
		Message.parent = parent;
	}
	public static int getNoOfAckToBeReceived() {
		return NoOfAckToBeReceived;
	}
	public static void setNoOfAckToBeReceived(int noOfComputationMsg) {
		NoOfAckToBeReceived = noOfComputationMsg;
	}
	public static int getNoOfAckToBeSent() {
		return NoOfAckToBeSent;
	}
	public static void setNoOfAckToBeSent(int noOfAckMsg) {
		NoOfAckToBeSent = noOfAckMsg;
	}
	
	
}
