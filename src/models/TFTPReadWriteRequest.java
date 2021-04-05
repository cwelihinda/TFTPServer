package models;

import utils.Mode;

public class TFTPReadWriteRequest extends TFTPRequest {
	String filename;
	Mode mode;
	public TFTPReadWriteRequest(byte[] data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TFTPRequest parseRequest(byte[] packet) {
		// TODO Auto-generated method stub
		return null;
	}

}
