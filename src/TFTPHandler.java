import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import models.TFTPBlockPacket;
import models.TFTPDataPacket;
import models.TFTPReadWriteRequest;
import utils.Mode;
import utils.OPCode;
import utils.TFTPUtils;

public class TFTPHandler implements Runnable{
	private DatagramPacket initialPacket;
	private Path homeFolder = Path.of("filesystem");
	private int BUFFER_SIZE = 512;
	
	public TFTPHandler(DatagramPacket packet) 
	{
		this.initialPacket = packet;
	}
	
	public byte[] attemptBlockRead(int block) {
		return new byte[0];
	}
	
	
	
	public void processRequest(byte[] data) {
		
	}
	
	public int getPacketLength(int fileLength, int filePos) {
		int delta = fileLength - filePos;
		return delta > BUFFER_SIZE ? BUFFER_SIZE : delta;
	}
	
	public void doReadRequest(TFTPReadWriteRequest request) {
		String filename = request.getFilename();
	//	Mode mode = request.getMode();
		byte[] fileData = readFile(homeFolder, filename);
		int fileLength = fileData.length;
		int blockNumber = 1;
		int filePos = 0;
		while(fileLength > filePos) {
			int bufferSize  = getPacketLength(fileLength, filePos);
			TFTPDataPacket dataPacket = new TFTPDataPacket(fileData, blockNumber, filePos, bufferSize);
			// SEND data packet;
			sendDatagram(dataPacket);
			//TODO wait for ack;
			TFTPBlockPacket ack = recieveAck();
			if(ack == null) {
				//TODO retry
			}
			filePos += bufferSize;
			blockNumber++;
		}
	}
	public void sendDatagram(TFTPDataPacket dataPacket) {
		if(dataPacket == null) {
			return;
		}
		System.out.println("Would have sent " + Arrays.toString(dataPacket.getData()));
		System.out.println(dataPacket.getFileDataAsString());
	}
	
	public TFTPBlockPacket recieveAck() {
		//TODO Try to read from datagram socket until timeout.
		return new TFTPBlockPacket(OPCode.ACK, 1);
	}
	
	public byte[] readFile(Path folder, String filename) {
		try {
			return Files.readAllBytes(Paths.get(folder.toString(), filename));
		} catch (IOException e) {
			System.out.println("ERROR READING FILE, RETURNING EMPTY BYTE ARRAY");
			return new byte[0];
		}
	}
	
	public void doWriteRequest(TFTPReadWriteRequest request) {
		
	}
	
	public void handleRequest() {
		byte[] initData = initialPacket.getData();
		boolean isRead = TFTPUtils.isReadRequest(initData);
		boolean isWrite = TFTPUtils.isWriteRequest(initData);
		
		if(!isRead &&  !isWrite) {
			System.out.println("INVALID REQUEST!");
		}else {
			TFTPReadWriteRequest request = new TFTPReadWriteRequest(initData);
			if(isRead) doReadRequest(request); else doWriteRequest(request);
		}
		 
	}
	
	public static void main(String[] args) {
		String filename = "troubleshooting.sh";
		
		TFTPReadWriteRequest readRequest =  new TFTPReadWriteRequest(OPCode.RRQ, filename, Mode.NETASCII);
		System.out.println(readRequest.toString());
		System.out.println();
		TFTPHandler handler = new TFTPHandler(new DatagramPacket(readRequest.getBytes(), readRequest.getBytes().length));
		handler.doReadRequest(readRequest);
		
//		TFTPReadWriteRequest writeRequest = new TFTPReadWriteRequest(OPCode.WRQ, filename, Mode.NETASCII);
//		System.out.println(writeRequest.toString());
//		System.out.println(handler.homeFolder.toAbsolutePath().toString());
//		
//		byte[] fileBytes = handler.readFile(handler.homeFolder, filename);// todo change me
//		
//		System.out.println(Arrays.toString(fileBytes));
//		System.out.println(new String(fileBytes));
//		

	}

	@Override
	public void run() {
		handleRequest();		
	}
	
}
