package utils;

public enum Mode {
	NETASCII("netascii"), OCTET("octet"), MAIL("mail"), UNKNOWN("unknown");
	String mode;
	private Mode(String mode) {
		this.mode = mode;
	}
}
