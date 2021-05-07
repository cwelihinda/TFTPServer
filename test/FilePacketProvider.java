import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.file.Files;
import java.nio.file.Path;

import models.TFTPDataPacket;
import models.TFTPReadWriteRequest;
import utils.Mode;
import utils.OPCode;

public class FilePacketProvider {
	byte[] filebytes;

	public static void main(String[] args) {
		
//		
//		byte[] bytes = new byte[512];
//		byte[] file = new byte[0];
//		for(int i = 0; i < 512; i++) { bytes[i] = (byte) (i % 128); } 
//		for(int i=0; i < 10; i++) { file = handler.concatByteArray(file, bytes); }
//		byte[] half = new byte[256];
//		System.arraycopy(bytes, 0, half, 0, half.length);
//		file = handler.concatByteArray(file, half);
//		System.out.println(file.length);
//		Path homeFolder = Path.of("filesystem"); try (FileOutputStream stream = new
//		FileOutputStream(Path.of(homeFolder.toString(), filename).toString())) {
//		stream.write(file);
//		} catch (FileNotFoundException e) { e.printStackTrace(); } catch (IOException e) {e.printStackTrace(); }
//		 
	}
	
	void testReadRequest() {
		String filename = "test";
		TFTPReadWriteRequest readRequest = new TFTPReadWriteRequest(OPCode.RRQ, filename, Mode.NETASCII);
		DatagramPacket packet = new DatagramPacket(readRequest.getBytes(), readRequest.getBytes().length);
		TFTPHandler handler = new TFTPHandler(packet);
		handler.doReadRequest(readRequest);
	}

	void testWriteRequest() {
		String filename = "test";
		TFTPReadWriteRequest writeRequest = new TFTPReadWriteRequest(OPCode.WRQ, filename, Mode.NETASCII);
		DatagramPacket packet = new DatagramPacket(writeRequest.getBytes(), writeRequest.getBytes().length);
		TFTPHandler handler = new TFTPHandler(packet);
		handler.doWriteRequest(writeRequest);
	}

	public FilePacketProvider(String homeFolder, String filename) {
		try {
			this.filebytes = Files.readAllBytes(Path.of(homeFolder, filename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public TFTPDataPacket getDataForBlock(int blockNumber) {
		int offset = (blockNumber - 1) * 512;
		int length = this.filebytes.length - offset > 512 ? 512 : this.filebytes.length - offset;
		return new TFTPDataPacket(this.filebytes, blockNumber, offset , length);
	}

}
