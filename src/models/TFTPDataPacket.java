package models;

import utils.OPCode;

public class TFTPDataPacket extends TFTPBlockPacket {
	byte[] fileData;
	public TFTPDataPacket(byte[] data) {
		super(data);
		parseRequest(data);
	}
	
	public TFTPDataPacket(byte[] data, int blockNumber, int offset, int bufferSize) {
		super(OPCode.DATA, blockNumber);
		this.fileData = sliceData(data, offset, bufferSize);
		initData();
	}
	
	@Override
	public TFTPRequest parseRequest(byte[] packet) {
		super.parseRequest(packet);
		int dataLength = packet.length - 4;
		fileData = new byte[dataLength];
		System.arraycopy(packet, 4, fileData, 0, dataLength);
		return this;
	}
	
	private byte[] sliceData(byte[] fullData, int offset, int bufferSize) {
		byte[] dataSlice = new byte[bufferSize];
		System.arraycopy(fullData, offset, dataSlice, 0, bufferSize);
		
		return dataSlice;
	}

	public void initData() {
		this.data = new byte[4 + fileData.length];
		this.data[1] = this.opcode;
		System.arraycopy(this.blockBytes, 0, this.data, 2, 2);
		System.arraycopy(this.fileData, 0, this.data, 4, fileData.length);
	}
	
	public byte[] getFileData() {
		return fileData;
	}
	
	public String getFileDataAsString() {
		return new String(fileData);
	}


}
