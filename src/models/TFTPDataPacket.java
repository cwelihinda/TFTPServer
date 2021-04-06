package models;

public class TFTPDataPacket extends TFTPBlockPacket {
	byte[] data;
	public TFTPDataPacket(byte[] data) {
		super(data);
		parseRequest(data);
	}
	
	@Override
	public TFTPRequest parseRequest(byte[] packet) {
		super.parseRequest(packet);
		int dataLength = packet.length - 4;
		data = new byte[dataLength];
		System.arraycopy(packet, 4, data, 0, dataLength);
		return this;
	}

}
