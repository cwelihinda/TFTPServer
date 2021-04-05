package utils;

//https://tools.ietf.org/html/rfc1350
public enum OPCode {
	RRQ((byte) 1), WRQ((byte) 2), DATA((byte) 3), ACK((byte)4), ERROR((byte)5), OPACK((byte)6), INVALID((byte) 7);
	byte opcode;
	
	private OPCode(byte opcode) {
		this.opcode = opcode;
	}
}
