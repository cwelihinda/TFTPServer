package models;

import utils.OPCode;

public class TFTPBlockPacket extends TFTPRequest{
	int blockNumber;
	byte[] blockBytes = new byte[2];
	public TFTPBlockPacket(byte[] data) {
		super(data);
		parseRequest(data);
	}
	
	public TFTPBlockPacket(OPCode code, int blockNumber) {
		super(code);
		createPacket(code, blockNumber);
	}
	
	
	@Override
	public TFTPRequest parseRequest(byte[] packet) {
		blockBytes = new byte[2];
		System.arraycopy(packet, 2, blockBytes, 0, 2);
		blockNumber = TFTPBlockPacket.fromByteArray(blockBytes);
		return this;
	}
	
	public  TFTPBlockPacket createPacket(OPCode code, int blockNumber) {
		blockBytes = new byte[2];
		super.opcode = code.getValue();
		this.blockNumber = blockNumber;
		byte[] fullBlockBytes = TFTPBlockPacket.toByteArray(blockNumber);
		System.arraycopy(fullBlockBytes, 2, blockBytes, 0, blockBytes.length);
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

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getBlockNumber() {
		return blockNumber;
	}
	
	@Override
	public byte[] getData() {
		byte[] data = new byte[4];
		data[1] = opcode;
		System.arraycopy(blockBytes, 0, data, 2, blockBytes.length);
		return data;
	}
	
	
}
