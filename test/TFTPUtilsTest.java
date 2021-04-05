import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TFTPUtilsTest {
	byte[] data;
	String filename = "test.txt";
	
	@BeforeEach
	void beforeEach() {
		 data = new byte[20];
		 byte[] nameBytes = filename.getBytes();
		 System.arraycopy(nameBytes, 0, data, 2, nameBytes.length); 
	}
	@Test
	void getOPCode_shouldReturnReadRequest_whenSecondIndexIsOne() {
		data[1] = 1;
		assertEquals(OPCode.RRQ, TFTPUtils.getOPCode(data));
	}
	@Test
	void getOPCode_shouldReturnWriteRequest_whenSecondIndexIsTwo() {
		data[1] = 2;
		assertEquals(OPCode.WRQ, TFTPUtils.getOPCode(data));
	}
	@Test
	void getOPCode_shouldReturnData_whenSecondIndexIsThree() {
		data[1] = 3;
		assertEquals(OPCode.DATA, TFTPUtils.getOPCode(data));
	}
	@Test
	void getOPCode_shouldReturnReadAck_whenSecondIndexIsFour() {
		data[1] = 4;
		assertEquals(OPCode.ACK, TFTPUtils.getOPCode(data));
	}
	@Test
	void getOPCode_shouldReturnError_whenSecondIndexIsFive() {
		data[1] = 5;
		assertEquals(OPCode.ERROR, TFTPUtils.getOPCode(data));
	}
	@Test
	void getOPCode_shouldReturnOPAck_whenSecondIndexIsSix() {
		data[1] = 6;
		assertEquals(OPCode.OPACK, TFTPUtils.getOPCode(data));
	}
	@Test
	void getOPCode_shouldReturnInvalid_whenDataIsNull() {
		data = null;
		assertEquals(OPCode.INVALID, TFTPUtils.getOPCode(data));
	}
	@Test
	void getOPCode_shouldReturnInvalid_whenDataIsNotCorrectSize() {
		data = new byte[4];
		assertEquals(OPCode.INVALID, TFTPUtils.getOPCode(data));
	}
	@Test
	void getOPCode_shouldReturnInvalid_whenDataIsInvalid() {
		data[1] = 8;
		assertEquals(OPCode.INVALID, TFTPUtils.getOPCode(data));
	}
	
	@Test
	void getFileName_shouldReturnFilename_whenFilenameSupplied() {
		assertEquals("test.txt", TFTPUtils.getFileName(data));
	}
	
	@Test
	void getFileName_shouldReturnEmptyString_whenSmallPacketSupplied() {
		data = new byte[4];
		assertEquals("", TFTPUtils.getFileName(data));
	}
	
	@Test
	void getFileName_shouldReturnEmptyString_whenSuppliedNull() {
		data = null;
		assertEquals("", TFTPUtils.getFileName(data));
	}
	
	
}
