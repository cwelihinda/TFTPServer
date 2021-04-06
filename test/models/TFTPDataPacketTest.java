package models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.Mode;
import utils.OPCode;
import utils.TFTPUtils;

public class TFTPDataPacketTest {

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
	

	void copyBlockNum(int block) {
		byte[] blockBytes = TFTPDataPacket.toByteArray(block);
		System.arraycopy(blockBytes, 2, packet, 2, 2);
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
	
	@Test
	void constructor_shouldHaveAllAttributesSet_givenGoodPacket() {
		int test = 5000;
		copyBlockNum(test);
		TFTPDataPacket data = new TFTPDataPacket(packet);
		assertEquals(data.blockNumber, test);
		assertEquals(TFTPUtils.bytesToString(data.data, 0), fileContents);
		assertEquals(OPCode.DATA, data.code);
		
	}
}
