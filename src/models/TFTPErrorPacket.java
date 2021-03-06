package models;

import utils.ErrorCode;
import utils.OPCode;
import utils.TFTPUtils;

public class TFTPErrorPacket extends TFTPRequest {
	String errorMsg;
	ErrorCode errorCode;
	public TFTPErrorPacket(byte[] data) {
		super(data);
		parseRequest(data);
	}
	
	public TFTPErrorPacket(ErrorCode errorCode) {
		super(OPCode.ERROR);
		initPacket(errorCode);
	}

	public void initPacket(ErrorCode errorCode) {
		byte[] message = errorCode.getErrorMessagBytes();
		this.data = new byte[5 + message.length];
		
		this.data[1] = opcode;
		this.data[3] = errorCode.getValue();
		System.arraycopy(message, 0, this.data, 4, message.length);
	}
	
	@Override
	public TFTPRequest parseRequest(byte[] packet) {
		errorCode = TFTPUtils.getErrorCode(packet);
		errorMsg = TFTPUtils.getErrorMessage(packet);
		return this;
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return this.data;
	}

}
