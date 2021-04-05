package utils;

//https://tools.ietf.org/html/rfc1350
public enum ErrorCode {
	UNDEFINED((byte) 0, "Not defined, see error message (if any)"), FILE_NOT_FOUND((byte) 1, "File not found"), ACCESS_VIOLATION((byte) 2, "Access violation"), DISK_FULL((byte)3, "Disk full or allocation exceeded"), ILLEGAL_OP((byte)4, "Illegal TFTP operation"), UNKNOWN_ID((byte)5, "Unknown transfer ID" ), FILE_EXISTS((byte) 6, "File already exists"), NO_USER((byte) 7, "No such user");
	byte code;
	String msg;
	private ErrorCode(byte code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
