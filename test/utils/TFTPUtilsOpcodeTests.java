package utils;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TFTPUtilsOpcodeTests {
	
	byte[] packet;
	String filename = "test.txt";
	
	@BeforeEach
	void beforeEach() {
		 packet = new byte[20];
		 byte[] nameBytes = filename.getBytes();
		 System.arraycopy(nameBytes, 0, packet, 2, nameBytes.length); 
	}
		
	@Test
	void getOPCode_shouldReturnReadRequest_whenSecondIndexIsOne() {
		packet[1] = 1;
		assertEquals(OPCode.RRQ, TFTPUtils.getOPCode(packet));
	}
	@Test
	void getOPCode_shouldReturnWriteRequest_whenSecondIndexIsTwo() {
		packet[1] = 2;
		assertEquals(OPCode.WRQ, TFTPUtils.getOPCode(packet));
	}
	@Test
	void getOPCode_shouldReturnData_whenSecondIndexIsThree() {
		packet[1] = 3;
		assertEquals(OPCode.DATA, TFTPUtils.getOPCode(packet));
	}
	@Test
	void getOPCode_shouldReturnReadAck_whenSecondIndexIsFour() {
		packet[1] = 4;
		assertEquals(OPCode.ACK, TFTPUtils.getOPCode(packet));
	}
	@Test
	void getOPCode_shouldReturnError_whenSecondIndexIsFive() {
		packet[1] = 5;
		assertEquals(OPCode.ERROR, TFTPUtils.getOPCode(packet));
	}
	@Test
	void getOPCode_shouldReturnOPAck_whenSecondIndexIsSix() {
		packet[1] = 6;
		assertEquals(OPCode.OPACK, TFTPUtils.getOPCode(packet));
	}
	@Test
	void getOPCode_shouldReturnInvalid_whenDataIsNull() {
		packet = null;
		assertEquals(OPCode.INVALID, TFTPUtils.getOPCode(packet));
	}
	@Test
	void getOPCode_shouldReturnInvalid_whenDataIsNotCorrectSize() {
		packet = new byte[3];
		assertEquals(OPCode.INVALID, TFTPUtils.getOPCode(packet));
	}
	@Test
	void getOPCode_shouldReturnInvalid_whenDataIsInvalid() {
		packet[1] = 8;
		assertEquals(OPCode.INVALID, TFTPUtils.getOPCode(packet));
	}
}
