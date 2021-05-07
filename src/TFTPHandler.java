import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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

public class TFTPHandler implements Runnable {
	private DatagramPacket initialPacket;
	private Path homeFolder = Path.of("filesystem");
	private Path dropFolder = Path.of("drops");
	private static int MAX_BUFFER_SIZE = 512;
	private DatagramSocket transferSocket;
	private int clientPort;
	private InetAddress address;

	public TFTPHandler(DatagramPacket packet) {
		this.initialPacket = packet;

		clientPort = initialPacket.getPort();
		address = initialPacket.getAddress();
	}

	public int getPacketLength(int fileLength, int filePos) {
		int delta = fileLength - filePos;
		return delta > MAX_BUFFER_SIZE ? MAX_BUFFER_SIZE : delta;
	}

	public void doReadRequest(TFTPReadWriteRequest request) {
		String filename = request.getFilename();
		// Mode mode = request.getMode();
		byte[] fileData = readFile(homeFolder, filename);
		int fileLength = fileData.length;
		int blockNumber = 1;
		int filePos = 0;
		while (fileLength > filePos) {
			int bufferSize = getPacketLength(fileLength, filePos);
			TFTPDataPacket dataPacket = new TFTPDataPacket(fileData, blockNumber, filePos, bufferSize);

			sendDatagram(dataPacket);

			TFTPBlockPacket ack = recieveAck();
			if (ack == null || ack.getBlockNumber() != blockNumber) {
				// TODO retry
				System.out.println("Something went wrong! Invalid Block number");
				return;
			}
			filePos += bufferSize;
			blockNumber++;
		}
	}

	public void doWriteRequest(TFTPReadWriteRequest request) {
		String filename = request.getFilename();
		//provider = new FilePacketProvider(this.homeFolder.toString(), filename);
		byte[] fileBytes = new byte[0];
		sendAck(0);
		int count = 1;
		TFTPDataPacket dataPacket;
		do {
			dataPacket = recieveDataPacket(count);
			fileBytes = concatByteArray(fileBytes, dataPacket.getFileData());
			// TODO Send ACK
			sendAck(count);
			count++;
		} while (dataPacket.getFileData().length == MAX_BUFFER_SIZE);
		System.out.println(fileBytes.length);
		writeToFile(dropFolder, filename, fileBytes);

	}

	public byte[] readFile(Path folder, String filename) {
		try {
			return Files.readAllBytes(Paths.get(folder.toString(), filename));
		} catch (IOException e) {
			System.out.println("ERROR READING FILE, RETURNING EMPTY BYTE ARRAY");
			return new byte[0];
		}
	}

	public static DatagramPacket generateDatagram(byte[] byteArray, InetAddress IPaddress, int portNumber) {
		DatagramPacket packetToSend = new DatagramPacket(byteArray, byteArray.length, IPaddress, portNumber);
		return packetToSend;
	}

	public TFTPDataPacket recieveDataPacket(int blockNumber) {
		DatagramPacket packet = receive(transferSocket);
		return new TFTPDataPacket(packet.getData());
		
	}

	public void writeToFile(Path path, String filename, byte[] fileBytes) {
		try (FileOutputStream stream = new FileOutputStream(Path.of(path.toString(), filename).toString())) {
			stream.write(fileBytes);
			System.out.println("Succesfully written file");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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

		if (!isRead && !isWrite) {
			System.out.println("INVALID REQUEST!");
		} else {
			try {
				transferSocket = new DatagramSocket();
				TFTPReadWriteRequest request = new TFTPReadWriteRequest(initData);
				if (isRead)
					doReadRequest(request);
				else
					doWriteRequest(request);
				transferSocket.close();
			} catch (Exception e) {
				System.out.println("Unable to establish transfer socket");
			}
		}

	}

	public static void main(String[] args) {
		String filename = "test";

		TFTPReadWriteRequest readRequest = new TFTPReadWriteRequest(OPCode.WRQ, filename, Mode.NETASCII);
		System.out.println(readRequest.toString());
		System.out.println();
		TFTPHandler handler = new TFTPHandler(
				new DatagramPacket(readRequest.getBytes(), readRequest.getBytes().length));
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

	public void sendDatagram(TFTPDataPacket dataPacket) {
		if (dataPacket == null) {
			return;
		}
		// TODO SEND data packet;
		System.out.println("Would have sent " + Arrays.toString(dataPacket.getData()));

		DatagramPacket packet = generateDatagram(dataPacket.getData(), address, clientPort);

		try {
			transferSocket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(dataPacket.getFileDataAsString());
	}

	public void sendDatagram(byte[] data) {
		DatagramPacket packet = generateDatagram(data, address, clientPort);

		try {
			transferSocket.send(packet);
			System.out.println("Successfully sent datagram");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	public TFTPBlockPacket recieveAck() {
		DatagramPacket packet = receive(transferSocket);
		return new TFTPBlockPacket(packet.getData());
	}

	public void sendAck(int blockNumber) {
		// TODO Try to read from datagram socket until timeout.
		TFTPBlockPacket ack = new TFTPBlockPacket(OPCode.ACK, blockNumber);
		Arrays.toString(ack.getData());
		sendDatagram(ack.getData());

	}

	public DatagramPacket receive(DatagramSocket socket) {
		byte[] buffer = new byte[MAX_BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		try {
			socket.receive(packet);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return packet;
	}

}
