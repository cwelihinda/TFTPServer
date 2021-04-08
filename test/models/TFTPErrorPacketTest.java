package models;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.ErrorCode;
import utils.OPCode;

public class TFTPErrorPacketTest {
	byte[] packet;
	String errorMsg = "Something";

	@BeforeEach
	void beforeEach() {
		packet = new byte[50];
		byte[] nameBytes = errorMsg.getBytes();
		packet[1] = 5;
		packet[3] = 1;
		System.arraycopy(nameBytes, 0, packet, 4, nameBytes.length);
	}

	@Test
	void constructor_shouldParseReadRequest() {
		TFTPErrorPacket request = new TFTPErrorPacket(packet);
		assertEquals(request.code, OPCode.ERROR);
		assertEquals(request.errorCode, ErrorCode.FILE_NOT_FOUND);
		assertEquals(request.errorMsg, errorMsg);
	}
}
