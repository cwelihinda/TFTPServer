package models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TFTPBlockPacketTest {
	byte[] packet;
	String fileContents = "Hello, this is demo data";
	int blockNum = 1;
	@BeforeEach
	void beforeEach() {
		packet = new byte[50];
		byte[] nameBytes = fileContents.getBytes();
		System.arraycopy(nameBytes, 0, packet, 4, nameBytes.length);
		packet[1] = 3;
		copyAcsiiData();
	}

	void copyAcsiiData() {
		byte[] nameBytes = fileContents.getBytes();
		System.arraycopy(nameBytes, 0, packet, 4, nameBytes.length);
	}
	
	
	@Test
	void intToByte_shouldReturnAByteOfSizeTwo() {
		assertEquals(blockNum, TFTPDataPacket.toByteArray(blockNum)[3]);
	}
	
	@Test
	void byteToInt_shouldReturnAInteger() {
		byte[] bytes = new byte[4];
		bytes[3] = 1;
		assertEquals(blockNum, TFTPDataPacket.fromByteArray(bytes));
	}
	
	@Test
	void byteToInt_shouldReturnSameInteger_whenPassedThroughintToByte() {
		int test = 5000;
		assertEquals(test, TFTPDataPacket.fromByteArray(TFTPDataPacket.toByteArray(test)));
	}
	
}
