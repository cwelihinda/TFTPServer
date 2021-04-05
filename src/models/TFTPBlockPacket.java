package models;

public class TFTPBlockPacket extends TFTPRequest{
	int blockNumber;
	public TFTPBlockPacket(byte[] data) {
		super(data);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public TFTPRequest parseRequest(byte[] packet) {
		// TODO Auto-generated method stub
		return null;
	}
}
