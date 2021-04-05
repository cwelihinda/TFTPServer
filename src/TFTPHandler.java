import java.net.DatagramPacket;

public class TFTPHandler implements Runnable{
	private DatagramPacket initialPacket;
	public TFTPHandler(DatagramPacket packet) 
	{
		this.initialPacket = packet;
	}
	
	public byte[] attemptBlockRead(int block) {
		return new byte[0];
	}
	
	
	
	public void doReadRequest() {
		
	}
	
	public void doWriteRequest() {
		
	}
	
	public void handleRequest() {
		if(TFTPUtils.isReadRequest(initialPacket.getData())) {
			doReadRequest();
		} else if (TFTPUtils.isWriteRequest(initialPacket.getData())) {
			doWriteRequest();
		} else {
			System.out.println("INVALID REQUEST!");
		}
	}


	@Override
	public void run() {
		handleRequest();		
	}
	 
	
	
	
	
}
