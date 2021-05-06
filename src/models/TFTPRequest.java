package models;

import utils.OPCode;
import utils.TFTPUtils;

public abstract class TFTPRequest {
	OPCode code;
	byte opcode;
	byte[] data;
	
	public TFTPRequest(byte[] data) {
		code = TFTPUtils.getOPCode(data);
		opcode = code.getValue();
	}
	
	public TFTPRequest(OPCode code) {
		this.code = code;
		opcode = code.getValue();
	}
	
	
	
	public abstract TFTPRequest parseRequest(byte[] packet);
	
	public abstract byte[] getBytes();
	
	public byte[] getData() {
		return data;
	}
}
