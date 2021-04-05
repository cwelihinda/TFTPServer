package models;

import utils.ErrorCode;
import utils.Mode;
import utils.OPCode;
import utils.TFTPUtils;

public class TFTPRequest {
	OPCode code;
	int blockNumber;
	String filename;
	Mode mode;
	byte[] data;
	String errorMsg;
	ErrorCode errorCode;
	
	public TFTPRequest(byte[] data) {
		code = TFTPUtils.getOPCode(data);
	}
}
