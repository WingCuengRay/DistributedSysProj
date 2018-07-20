package RoomResrvSys;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPrequest {
	
	public UDPrequest() {
		super();
	}
	
	private static String UDPRequest(String command, int UDPlistenPort) throws SocketException {
		//System.out.println("UDP connection start!");
		
		String reply = null;
		DatagramSocket requestSocket = new DatagramSocket();
		
		try {  
			
			byte [] message = command.getBytes();
			InetAddress aHost = InetAddress.getByName("localhost");
			
			System.out.println("start send request");
			DatagramPacket request = new DatagramPacket(message,  message.length, aHost, UDPlistenPort);
			requestSocket.send(request);
			System.out.println("request sended");
			
			byte[] buffer = new byte[512];
			DatagramPacket replyMessage = new DatagramPacket(buffer, buffer.length);	
			requestSocket.receive(replyMessage);
			
			byte[] newBuffer = new byte[replyMessage.getLength()];
	 		System.arraycopy(buffer, 0, newBuffer, 0, replyMessage.getLength());
			
			reply = new String(newBuffer);
			System.out.println("Reply: " + reply);
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){System.out.println("IO: " + e.getMessage());
		}
		
		requestSocket.close();
		return reply;
		
	}
	
	public static String UDPgetAvailableTimeSlot(String command, int UDPlistenPort){
		try {
			
			String avail = UDPRequest(command, UDPlistenPort);
			avail = avail.trim();
			
			return avail;
			
		} catch (SocketException e) {
			
			e.printStackTrace();
			return "";
		}
	
		
	}
	
	public static Boolean UDPcancelBooking(String command, int UDPlistenPort){
		try {
			String message = UDPRequest(command, UDPlistenPort);
			message = message.trim();
		
			Boolean ifSuccess = Boolean.valueOf(message);
			System.out.println(ifSuccess);
			
			return ifSuccess;
		
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static String UDPbookRoom(String command, int UDPlistenPort) {
		//System.out.println(command + "  " +aimCampus);
		try {
			
			String bookingID =  UDPRequest(command, UDPlistenPort);
			bookingID = bookingID.trim();
			
			return bookingID;
			
		}catch (SocketException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String UDPgetWeek(String command, int UDPlistenPort) {
		
		try {
			String week =  UDPRequest(command, UDPlistenPort);
			week = week.trim();
			
			return week;
			
		} catch (SocketException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String UDPBookingIDexist(String command, int cancelling_UDPlistenPort) {
		try {
		
		String message = UDPRequest(command, cancelling_UDPlistenPort);
		
		return message;
		
		} catch (SocketException e) {
			e.printStackTrace();
			return "";
		}
	}



}
