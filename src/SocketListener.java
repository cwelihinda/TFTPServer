import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SocketListener implements Runnable {
	private static final int PORT = 69;
	private static final int MAX_BUFFER = 4096;
	
	public SocketListener() {

	}

	@Override
	public void run() {
		try (DatagramSocket serverSocket = new DatagramSocket(PORT)){
			while(true) {
				System.out.println("Listening!");
				byte[] buffer = new byte[MAX_BUFFER];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				serverSocket.receive(packet);
				new Thread(new TFTPHandler(packet)).run();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
