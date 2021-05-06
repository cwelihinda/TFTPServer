package utils;

public enum Mode {
	NETASCII("netascii"), OCTET("octet"), MAIL("mail"), UNKNOWN("unknown");
	String mode;
	private Mode(String mode) {
		this.mode = mode;
	}
	
	public String getValue() {
		return this.mode;
	}
	
	public byte[] getByteValue() {
		return this.mode.getBytes();
	}
}
