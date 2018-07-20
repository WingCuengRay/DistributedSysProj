package RoomResrvSys;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import RoomRecords.RoomRecords;

public class ListenThread extends Thread {
	private Thread t;
	private String threadName;
	private DatagramSocket SeverSocket;
	//private ServerImpl campusSever;
	private RoomRecords roomRecords;
	   
	public ListenThread(String name, DatagramSocket SeverSocket, RoomRecords roomRecords) {
	      threadName = name;
	      this.SeverSocket = SeverSocket;
	      //this.campusSever = campusSever;
	      this.roomRecords = roomRecords;
	      //t = null;
	      //System.out.println("Creating " +  threadName );
	   }
	   
	   public void run() {
	      System.out.println( threadName + " Running ");
	      try {
	    	  		// build a listening thread;
	    	  		byte[] buffer = new byte[1000];
	    	  		
			 	while(true){
			 		
			 		DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			 		SeverSocket.receive(request);
			 		
			 		//System.out.println("Request getlength: " + request.getLength());
			 		
			 		byte[] newBuffer = new byte[request.getLength()];
			 		System.arraycopy(buffer, 0, newBuffer, 0, request.getLength());
			 		
			 		//analyze the request, and get the appropriate methods;
			 		String message = new String(newBuffer).trim();
			 		System.out.println("received the message  " + message);
			 		
			 		InetAddress ipAddress = request.getAddress();
			 		int port = request.getPort();
			 		
			 		processThread process = new processThread(roomRecords, message, SeverSocket, ipAddress, port);
			 		process.start();	
			 		
			 	}		
	        
	      }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
	      }catch (IOException e) {System.out.println("IO: " + e.getMessage());
	      } catch (Exception e) {System.out.println("Exception: " + e.getMessage());
	      }finally {if(SeverSocket != null) SeverSocket.close();
	      }
	   }
	   

	public void start () {
		
		if (t == null) {
	    		t = new Thread (this, threadName);
	    		t.start ();
	      }
	 }

}
