package RoomResrvSys;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.text.ParseException;

import RoomRecords.RoomRecords;
import RoomRecords.Tools;

public class processThread extends Thread {
	private Thread t;
	private RoomRecords roomRecords;
	private DatagramSocket SeverSocket;
	private InetAddress ipAddress;
	private int port;
	String message;
	
	public processThread(RoomRecords roomRecords, String message, DatagramSocket SeverSocket, InetAddress ipAddress, int port) {
		super();
		this.roomRecords = roomRecords;
		this.SeverSocket = SeverSocket;
		this.message = message;
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public void start () {
		if (t == null) {
	    		t = new Thread (this);
	    		t.start ();
	      }
	 }
	
	public void run() {
		String answer = "";
		
	  	try {
	  		if (message.startsWith("getAvailableTimeSlot")) {
	  			answer = UDPgetAvailableTimeSlot(message);
	  		}
 		
	  		if (message.startsWith("bookRoom")) {
	  			answer = UDPbookRoom(message);
	  		}
 		
	  		if (message.startsWith("cancelBooking")) {
	  			answer = UDPcancelBooking(message);	
	  		}
 		
	  		if (message.startsWith("getWeek")) {
	  			answer = UDPgetWeek(message);	
	  		}
 		
	  		if (message.startsWith("BookingIDexist")) {
	  			answer = UDPBookingIDexist(message);	
	  		}

 		
 		//System.out.println(answer);
 		//System.out.println("answer are ready, prepare send" );
 		
	  		if (answer == null) {
	  			answer = "";
	  		}
 		
	  		byte[] byteReply = answer.getBytes();
	  		DatagramPacket reply = new DatagramPacket(byteReply, byteReply.length, ipAddress, port);
	  		SeverSocket.send(reply);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	 private String UDPgetAvailableTimeSlot(String message) throws RemoteException, Exception { 
		   //get each parameter;
		   String info[] = message.substring(message.indexOf("(")+1, message.length()-1).split(",");
		   String id = info[0].trim();
		   String date = info[1].trim();
		   
		   String avail = roomRecords.getAvailableTimeSlot(date, id) + "  ";
		   return avail;
	   }
	   
	   private String UDPbookRoom(String message) throws RemoteException, Exception {
		   /* get each parameter;
			 *bookRoom(String campusName, String roomNumber, String date, String timeslot) 
			 */
		   String 	parameter = message.substring(message.indexOf("(")+1, message.length()-1);	
		   String[] para = parameter.split(",");
		
		   String campusName = para[0].trim();
		   String room_Number = para[1].trim();
		   String date = para[2].trim();
		   String list_Of_Time_Slots = para[3].trim();
		   String studentID = para[4].trim();
		   
		   System.out.println("parameter:  " + campusName + "  " + room_Number + "  " + date);
			
		   String bookingID = roomRecords.bookRoom(campusName, studentID, room_Number, date, list_Of_Time_Slots);	
		   System.out.println("Prepare to sent bookingID :" +  bookingID);
		   
		   return bookingID;
		}
	   
	   private String UDPcancelBooking(String message) throws RemoteException, Exception {
		   	//get each parameter;
		    System.out.println(message);
			String[] info = message.substring(message.indexOf("(")+1, message.length()-1).split(",");
			String id = info[0].trim();
			String bookingID = info[1].trim();
			//System.out.println(bookingID);
				
			Boolean ifSuccess = roomRecords.cancelBooking(bookingID);	
			return String.valueOf(ifSuccess);
		}
	   
		private String UDPgetWeek(String message) throws ParseException {
			//get each parameter;
			String bookingID = message.substring(message.indexOf("(")+1, message.length()-1);;
			
			String week = Tools.toWeekFromBookingID(bookingID, roomRecords);
			return week;
		}

		private String UDPBookingIDexist(String message) throws RemoteException, Exception {
			//command = "BookingIDexist(" + bookingID + ")";
			String bookingID = message.substring(message.indexOf("(")+1, message.length()-1);
			System.out.println(bookingID);
				
			String ifexist = roomRecords.getRoomInfoFromBookingID(bookingID);
			System.out.println(ifexist + ",  " +ifexist.length());
			return ifexist;
		}
}
