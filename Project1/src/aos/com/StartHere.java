package aos.com;

public class StartHere {
	public static void main(String[] args) {
		Message message = Message.getInstance();
		Thread t = new Thread(new Server(message));
		t.start();
		
		try{
			Thread.sleep(40*1000);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Thread th = new Thread(new Client(message));
		th.start();
	}
}
