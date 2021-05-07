public class TFTPServer {
	
	public static void main(String[] args) {
		System.out.println("Hello World!");
		startServer();
	}
	
	
	public static void startServer() {
		new Thread(new SocketListener()).start();
	}
}
