package aos.com;

public class Message {
	public static boolean inTree;
	public static String parent;
	public static int NoOfComputationMsg;
	public static int NoOfAckMsg;
	
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
	public static int getNoOfComputationMsg() {
		return NoOfComputationMsg;
	}
	public static void setNoOfComputationMsg(int noOfComputationMsg) {
		NoOfComputationMsg = noOfComputationMsg;
	}
	public static int getNoOfAckMsg() {
		return NoOfAckMsg;
	}
	public static void setNoOfAckMsg(int noOfAckMsg) {
		NoOfAckMsg = noOfAckMsg;
	}
	
	
}
