package models;

public class TFTPBlockPacket extends TFTPRequest{
	int blockNumber;
	byte[] blockBytes;
	public TFTPBlockPacket(byte[] data) {
		super(data);
		parseRequest(data);
	}
	
	
	@Override
	public TFTPRequest parseRequest(byte[] packet) {
		blockBytes = new byte[2];
		System.arraycopy(packet, 2, blockBytes, 0, 2);
		blockNumber = TFTPBlockPacket.fromByteArray(blockBytes);
		return this;
	}
	
	public static byte[] toByteArray(int value) {
	    return new byte[] { 
	            (byte)(value >> 24),
	            (byte)(value >> 16),
	            (byte)(value >> 8),
	            (byte)value };
	    }

	
	public static int fromByteArray(byte[] bytes) {
		if (bytes.length == 4) {
	     return ((bytes[0] & 0xFF) << 24) | 
	             ((bytes[1] & 0xFF) << 16) | 
	             ((bytes[2] & 0xFF) << 8 ) | 
	             ((bytes[3] & 0xFF) << 0 );
		} else  {
			return   ((bytes[0] & 0xFF) << 8 ) | 
		             ((bytes[1] & 0xFF) << 0 );
		}
	 }
	
}
