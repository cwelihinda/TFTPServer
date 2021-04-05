package utils;

public class TFTPUtils {
	private static final int PACKET_MIN_LENGTH = 4;
	public static OPCode getOPCode(byte[] data) {
		OPCode code = null;
		if (!TFTPUtils.isValid(data)) {
			return OPCode.INVALID;
		} else {
			switch (data[1]) {
				case 1:
					code = OPCode.RRQ;
					break;
				case 2:
					code = OPCode.WRQ;
					break;
				case 3:
					code = OPCode.DATA;
					break;
				case 4:
					code = OPCode.ACK;
					break;
				case 5:
					code = OPCode.ERROR;
					break;
				case 6:
					code = OPCode.OPACK;
					break;
				default:
					code = OPCode.INVALID;
					break;
			}
		}
		return code;
	}
	
	public static Mode getMode(byte[] data, String filename) {
		Mode code = Mode.UNKNOWN;
		if (!TFTPUtils.isValid(data) || filename == null || filename.length() == 0) {
			return Mode.UNKNOWN;
		} else if(isReadRequest(data) || isWriteRequest(data)){
			switch (getTransferModeString(data, filename).toLowerCase()) {
				case "netascii":
					code = Mode.NETASCII;
					break;
				case "octet":
					code = Mode.OCTET;
					break;
				case "mail":
					code = Mode.MAIL;
					break;
				default:
					code = Mode.UNKNOWN;
					break;
			}
		}
		return code;
	}

	private static boolean isValid(byte[] data) {
		if (data == null) {
			System.out.println("Data is null");
			return false;
		} else if (data.length < PACKET_MIN_LENGTH) {
			System.out.println("Invalid data");
			return false;
		}
		return true;
	}

	public static String getFileName(byte[] data) {
		if (!TFTPUtils.isValid(data)) {
			return "";
		}
		//first 2 bytes are reserved for opcode and end of filename is 0
		return TFTPUtils.bytesToString(data, 2);
	}
	
	public static String getTransferModeString(byte[] data, String filename) {
		if (!TFTPUtils.isValid(data)) {
			return "";
		}
		//first 2 bytes are reserved for opcode and end of filename is 0
		return TFTPUtils.bytesToString(data, filename.length() + 3);
	}
	
	public static String bytesToString(byte[] data, int startIndex) {
		if(data == null || data.length < startIndex) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		//first 2 bytes are reserved for opcode and end of filename is 0
		for (int i = startIndex; i < data.length && data[i] != 0; i++) {
			sb.append((char)data[i]);
		}
		return sb.toString();
	}

	public static boolean isReadRequest(byte[] data) {
		return OPCode.RRQ.equals(getOPCode(data));
	}

	public static boolean isWriteRequest(byte[] data) {
		return OPCode.WRQ.equals(getOPCode(data));
	}

}
