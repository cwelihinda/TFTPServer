package utils;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TFTPUtilsModeTest {
	byte[] packet;
	String filename = "test.txt";
	String mode = "mail";
	byte[] modeBytes = new byte[0];

	@BeforeEach
	void beforeEach() {
		packet = new byte[50];
		byte[] nameBytes = filename.getBytes();
		System.arraycopy(nameBytes, 0, packet, 2, nameBytes.length);
		copyMode(mode, filename);
	}

	void copyMode(String mode, String filename) {
		byte[] nameBytes = filename.getBytes();
		byte[] modeBytes = mode.getBytes();
		System.arraycopy(modeBytes, 0, packet, nameBytes.length + 3, modeBytes.length);
	}

	@Test
	void getMode_shouldReturnNetascii_whenNetasciiSuppliedAndIsRead() {
		packet[1] = 1;
		copyMode("netascii", filename);
		assertEquals(Mode.NETASCII, TFTPUtils.getMode(packet, filename));
	}

	@Test
	void getMode_shouldReturnOctet_whenOctetSuppliedAndIsRead() {
		packet[1] = 1;
		copyMode("octet", filename);
		assertEquals(Mode.OCTET, TFTPUtils.getMode(packet, filename));
	}

	@Test
	void getMode_shouldReturnMail_whenMailSuppliedAndIsRead() {
		packet[1] = 1;
		assertEquals(Mode.MAIL, TFTPUtils.getMode(packet, filename));
	}

	@Test
	void getMode_shouldReturnNetascii_whenNetasciiSuppliedAndIsWrite() {
		packet[1] = 2;
		copyMode("netascii", filename);
		assertEquals(Mode.NETASCII, TFTPUtils.getMode(packet, filename));
	}
	
	@Test
	void getMode_shouldReturnUnknown_whenRandomStringSuppliedAndIsWrite() {
		packet[1] = 2;
		copyMode("somethingweird", filename);
		assertEquals(Mode.UNKNOWN, TFTPUtils.getMode(packet, filename));
	}

	@Test
	void getMode_shouldReturnOctet_whenOctetSuppliedAndIsWrite() {
		packet[1] = 2;
		copyMode("octet", filename);
		assertEquals(Mode.OCTET, TFTPUtils.getMode(packet, filename));
	}

	@Test
	void getMode_shouldReturnMail_whenMailSuppliedAndIsWrite() {
		packet[1] = 2;
		assertEquals(Mode.MAIL, TFTPUtils.getMode(packet, filename));
	}

	@Test
	void getMode_shouldReturnUnknown_whenSmallPacketSupplied() {
		assertEquals(Mode.UNKNOWN, TFTPUtils.getMode(packet, filename));
	}

	@Test
	void getMode_shouldReturnUnknown_whenNotReadOrWriteRequest() {
		assertEquals(Mode.UNKNOWN, TFTPUtils.getMode(packet, filename));
	}

	@Test
	void getMode_shouldReturnUnknown_whenInvalidPacketSupplied() {
		assertEquals(Mode.UNKNOWN, TFTPUtils.getMode(packet, filename));
	}

	@Test
	void getMode_shouldReturnUnknown_whenFilenameIsNull() {
		assertEquals(Mode.UNKNOWN, TFTPUtils.getMode(packet, null));
	}

	@Test
	void getMode_shouldReturnUnknown_whenFilenameIsEmpty() {
		assertEquals(Mode.UNKNOWN, TFTPUtils.getMode(packet, ""));
	}
}
