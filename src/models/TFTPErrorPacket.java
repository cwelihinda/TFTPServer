package models;

import utils.ErrorCode;

public class TFTPErrorPacket extends TFTPRequest {
	String errorMsg;
	ErrorCode errorCode;
	public TFTPErrorPacket(byte[] data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TFTPRequest parseRequest(byte[] packet) {
		// TODO Auto-generated method stub
		return null;
	}

}
