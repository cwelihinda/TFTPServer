package models;

import utils.ErrorCode;
import utils.TFTPUtils;

public class TFTPErrorPacket extends TFTPRequest {
	String errorMsg;
	ErrorCode errorCode;
	public TFTPErrorPacket(byte[] data) {
		super(data);
		parseRequest(data);
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
		return null;
	}

}
