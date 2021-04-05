import java.net.DatagramPacket;

public class TFTPUtils {

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

	private static boolean isValid(byte[] data) {
		if (data == null) {
			System.out.println("Data is null");
			return false;
		} else if (data.length < 6) {
			System.out.println("Invalid data");
			return false;
		}
		return true;
	}

	public static String getFileName(byte[] data) {
		StringBuilder sb = new StringBuilder();
		if (!TFTPUtils.isValid(data)) {
			return "";
		}
		//first 2 bytes are reserved for opcode and end of filename is 0
		for (int i = 2; i < data.length && data[i] != 0; i++) {
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

	public static byte[] generateAck(int blockNumber) {
		return new byte[0];
	}

	public static byte[] generateDataBlock(byte[] data) {
		return new byte[0];
	}

	public static Mode detectTransferMode(byte[] data) {
		return Mode.NETASCII;
	}

	public static DatagramPacket generatePacket(byte[] data) {
		return new DatagramPacket(null, 1);
	}

	public static byte[] stripHeaders(byte[] data) {
		if (data != null && data.length > 4) {
			System.arraycopy(data, 0, data, 0, data.length);
		}
		return data;
	}

}
