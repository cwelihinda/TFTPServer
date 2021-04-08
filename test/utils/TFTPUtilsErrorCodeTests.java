package utils;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TFTPUtilsErrorCodeTests {
	
	byte[] packet;
	String errorMessage = "Something";
	
	@BeforeEach
	void beforeEach() {
		 packet = new byte[20];
		 byte[] nameBytes = errorMessage.getBytes();
		 System.arraycopy(nameBytes, 0, packet, 4, nameBytes.length); 
		 packet[1] = 1;
	}
		
	@Test
	void getErrorCode_shouldReturnUnknown_whenFourthIndexIsZero() {
		packet[3] = 0;
		assertEquals(ErrorCode.UNDEFINED, TFTPUtils.getErrorCode(packet));
	}
	@Test
	void getErrorCode_shouldReturnFileNotFound_whenFourthIndexIs1() {
		packet[3] = 1;
		assertEquals(ErrorCode.FILE_NOT_FOUND, TFTPUtils.getErrorCode(packet));
	}
	@Test
	void getErrorCode_shouldReturnAccessViolation_whenFourthIndexIs2() {
		packet[3] = 2;
		assertEquals(ErrorCode.ACCESS_VIOLATION, TFTPUtils.getErrorCode(packet));
	}
	@Test
	void getErrorCode_shouldReturnDiskFull_whenFourthIndexIs3() {
		packet[3] = 3;
		assertEquals(ErrorCode.DISK_FULL, TFTPUtils.getErrorCode(packet));
	}
	@Test
	void getErrorCode_shouldReturnIllegalOp_whenFourthIndexIs4() {
		packet[3] = 4;
		assertEquals(ErrorCode.ILLEGAL_OP, TFTPUtils.getErrorCode(packet));
	}
	@Test
	void getErrorCode_shouldReturnOPAck_whenFourthIndexIs5() {
		packet[3] = 5;
		assertEquals(ErrorCode.UNKNOWN_ID, TFTPUtils.getErrorCode(packet));
	}
	@Test
	void getErrorCode_shouldReturnOPAck_whenFourthIndexIs6() {
		packet[3] = 6;
		assertEquals(ErrorCode.FILE_EXISTS, TFTPUtils.getErrorCode(packet));
	}
	@Test
	void getErrorCode_shouldReturnOPAck_whenFourthIndexIs7() {
		packet[3] = 7;
		assertEquals(ErrorCode.NO_USER, TFTPUtils.getErrorCode(packet));
	}
	@Test
	void getErrorCode_shouldReturnOPAck_whenFourthIndexIsAnythingElse() {
		packet[3] = 8;
		assertEquals(ErrorCode.INVALID, TFTPUtils.getErrorCode(packet));
		packet[1] = 9;
		assertEquals(ErrorCode.INVALID, TFTPUtils.getErrorCode(packet));
	}
	@Test
	void getErrorCode_shouldReturnInvalid_whenDataIsNull() {
		packet = null;
		assertEquals(ErrorCode.INVALID, TFTPUtils.getErrorCode(packet));
	}
	@Test
	void getErrorCode_shouldReturnInvalid_whenDataIsNotCorrectSize() {
		packet = new byte[3];
		assertEquals(ErrorCode.INVALID, TFTPUtils.getErrorCode(packet));
	}
}
