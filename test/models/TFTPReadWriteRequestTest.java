package models;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.Mode;
import utils.OPCode;

public class TFTPReadWriteRequestTest {

	byte[] packet;
	String filename = "test.txt";
	Mode mode = Mode.NETASCII;

	@BeforeEach
	void beforeEach() {
		packet = new byte[50];
		byte[] nameBytes = filename.getBytes();
		System.arraycopy(nameBytes, 0, packet, 2, nameBytes.length);
		System.arraycopy(nameBytes, 0, packet, 2, nameBytes.length);
		copyMode(mode.toString(), filename);
	}

	void copyMode(String mode, String filename) {
		byte[] nameBytes = filename.getBytes();
		byte[] modeBytes = mode.getBytes();
		System.arraycopy(modeBytes, 0, packet, nameBytes.length + 3, modeBytes.length);
	}

	@Test
	void constructor_shouldParseReadRequest() {
		packet[1] = 1;
		TFTPReadWriteRequest request = new TFTPReadWriteRequest(packet);
		assertEquals(request.code, OPCode.RRQ);
		assertEquals(request.filename, filename);
		assertEquals(request.mode, mode);
	}
	
	@Test
	void constructor_shouldParseWriteRequest() {
		packet[1] = 2;
		TFTPReadWriteRequest request = new TFTPReadWriteRequest(packet);
		assertEquals(request.code, OPCode.WRQ);
		assertEquals(request.filename, filename);
		assertEquals(request.mode, mode);
	}

}
