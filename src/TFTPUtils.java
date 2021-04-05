import java.net.DatagramPacket;

public class TFTPUtils {

	public static OPCode getOPCode(byte[] data) {
		OPCode code = null;
		if (data == null) {
			System.out.println("Data is null");
			return null;
		} else if (data.length < 6) {
			System.out.println("Invalid data");
			return null;
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
				
			}
		}
		return code;
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
