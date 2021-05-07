import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FileNotFoundException;
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
	private Path dropFolder = Path.of("drops");
	private int BUFFER_SIZE = 512;
	
	private FilePacketProvider provider;
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

			sendDatagram(dataPacket);
			
			TFTPBlockPacket ack = recieveAck(blockNumber);
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
		//TODO  SEND data packet;
		System.out.println("Would have sent " + Arrays.toString(dataPacket.getData()));
	
		System.out.println(dataPacket.getFileDataAsString());
	}
	
	public TFTPBlockPacket recieveAck(int blockNumber) {
		//TODO Try to read from datagram socket until timeout.
		return new TFTPBlockPacket(OPCode.ACK, blockNumber);
	}
	
	public byte[] readFile(Path folder, String filename) {
		try {
			return Files.readAllBytes(Paths.get(folder.toString(), filename));
		} catch (IOException e) {
			System.out.println("ERROR READING FILE, RETURNING EMPTY BYTE ARRAY");
			return new byte[0];
		}
	}
	
	public TFTPDataPacket recieveDataPacket(int blockNumber) {
		return provider.getDataForBlock(blockNumber);
	}
	
	public void doWriteRequest(TFTPReadWriteRequest request) {
		String filename = request.getFilename();
		provider = new FilePacketProvider(this.homeFolder.toString(), filename);
		byte[] fileBytes = new byte[0];
		int count = 1;
		TFTPDataPacket dataPacket;
		do {
			dataPacket = recieveDataPacket(count);
			fileBytes = concatByteArray(fileBytes, dataPacket.getFileData());
			//TODO Send ACK
			count++;
		} while (dataPacket.getFileData().length == BUFFER_SIZE);
		System.out.println(fileBytes.length);
		writeToFile(dropFolder,filename,fileBytes);
		
		
	}
	
	public void writeToFile(Path path, String filename, byte[] fileBytes) {
		try(FileOutputStream stream = new FileOutputStream(Path.of(path.toString(), filename).toString())) {
			stream.write(fileBytes);
			System.out.println("Succesfully written file");
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
			} 
		catch (IOException e) {
			e.printStackTrace(); 
		}
		 
	}
	
	
	public byte[] concatByteArray(byte[] arr1, byte[] arr2) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(arr1);
			outputStream.write(arr2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputStream.toByteArray();
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
		String filename = "test";
		
		TFTPReadWriteRequest readRequest =  new TFTPReadWriteRequest(OPCode.WRQ, filename, Mode.NETASCII);
		System.out.println(readRequest.toString());
		System.out.println();
		TFTPHandler handler = new TFTPHandler(new DatagramPacket(readRequest.getBytes(), readRequest.getBytes().length));
		handler.doWriteRequest(readRequest);
		
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
