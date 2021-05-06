package models;


import java.util.Arrays;
import java.util.List;

import utils.Mode;
import utils.OPCode;
import utils.TFTPUtils;

public class TFTPReadWriteRequest extends TFTPRequest {
	public String filename;
	public Mode mode;

	public TFTPReadWriteRequest(byte[] data) {
		super(data);
		parseRequest(data);
		
	}
	
	public TFTPReadWriteRequest(OPCode code, String filename) {
		this(code, filename, Mode.NETASCII);
	}
	
	public TFTPReadWriteRequest(OPCode code, String filename, Mode mode) {
		super(code);
		this.filename = filename;
		this.mode = mode;
		initRequest(filename, mode);
	}

	@Override
	public TFTPRequest parseRequest(byte[] packet) {
		filename = TFTPUtils.getFileName(packet);
		mode = TFTPUtils.getMode(packet, filename);
		return this;
	}
	
	public void initRequest(String filename, Mode mode) {
		byte[] filenameBytes = filename.getBytes();
		byte[] modeBytes = mode.getByteValue();
		int filenameBytesLength = filenameBytes.length;
		int modeBytesLength = modeBytes.length;
		this.data = new byte[4 + filenameBytesLength + modeBytesLength];
		this.data[1] = opcode;
		System.arraycopy(filenameBytes,0, this.data, 2, filenameBytesLength);
		System.arraycopy(modeBytes, 0, this.data, filenameBytesLength + 3, modeBytesLength);
	}
	
	public String getFilename() {
		return this.filename;
	}

	public Mode getMode() {
		return this.mode;
	}
	
	public String toString() {
		return String.join("\n", List.of(this.code.name(), this.filename, this.mode.getValue(), Arrays.toString(this.data)));// Arrays.toString(this.data);		
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return super.data;
	}
}
