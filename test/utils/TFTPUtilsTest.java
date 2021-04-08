package utils;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TFTPUtilsTest {
	byte[] packet;
	String filename = "test.txt";

	
	@BeforeEach
	void beforeEach() {
		 packet = new byte[20];
		 byte[] nameBytes = filename.getBytes();
		 System.arraycopy(nameBytes, 0, packet, 2, nameBytes.length);
	}

	
	@Test
	void getFileName_shouldReturnFilename_whenFilenameSupplied() {
		assertEquals("test.txt", TFTPUtils.getFileName(packet));
	}
	
	@Test
	void getFileName_shouldReturnEmptyString_whenSmallPacketSupplied() {
		packet = new byte[3];
		assertEquals("", TFTPUtils.getFileName(packet));
	}
	
	@Test
	void getFileName_shouldReturnEmptyString_whenSuppliedNull() {
		packet = null;
		assertEquals("", TFTPUtils.getFileName(packet));
	}
	
	@Test
	void getErrorMsg_shouldReturnEmptyString_whenSmallPacketSupplied() {
		packet = new byte[3];
		assertEquals("", TFTPUtils.getFileName(packet));
	}
	
	@Test
	void getErrorMsg_shouldReturnEmptyString_whenSuppliedNull() {
		packet = null;
		assertEquals("", TFTPUtils.getFileName(packet));
	}
	
}
