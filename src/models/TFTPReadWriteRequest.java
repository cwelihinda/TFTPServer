package models;

import utils.Mode;
import utils.TFTPUtils;

public class TFTPReadWriteRequest extends TFTPRequest {
	String filename;
	Mode mode;
	public TFTPReadWriteRequest(byte[] data) {
		super(data);
		parseRequest(data);
		
	}

	@Override
	public TFTPRequest parseRequest(byte[] packet) {
		filename = TFTPUtils.getFileName(packet);
		mode = TFTPUtils.getMode(packet, filename);
		return this;
	}

}
