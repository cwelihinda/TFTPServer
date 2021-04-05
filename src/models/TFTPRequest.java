package models;

import utils.OPCode;
import utils.TFTPUtils;

public abstract class TFTPRequest {
	OPCode code;
	
	public TFTPRequest(byte[] data) {
		code = TFTPUtils.getOPCode(data);
	}
	
	
	public abstract TFTPRequest parseRequest(byte[] packet);
}
