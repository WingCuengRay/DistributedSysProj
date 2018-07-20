package RoomResrvSys;


import RoomRecords.RoomRecords;
import RoomRecords.TimeSlot;
import RoomRecords.Tools;
import UserSystem.UserSystem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import tools.LogItem;
import tools.LogWriter;
import tools.RequestType;

import java.util.Random;


public class ServerRemoteImpl implements RemoteServerInterface {
	
	private String campusName;
	private Campus campus;
	private UserSystem users;
	private RoomRecords roomRecords;
	private LogWriter writer;
	private Tools RoomRecordTools;
	private ReadWriteLock lock;
	
	public ServerRemoteImpl(String campusName, int insideUDPListenPort) {
		this.campusName = campusName;
		campus = new Campus();
		users = new UserSystem(campusName);
		roomRecords = new RoomRecords(campusName, users);
		writer = new LogWriter(campusName+".log");
		RoomRecordTools = new Tools();
		lock = new ReentrantReadWriteLock();
		
		//int UDPlistenPort = campus.getUDPlistenPort(campusName);
		DatagramSocket SeverSocket;
		try {
			SeverSocket = new DatagramSocket(insideUDPListenPort);

			String threadName = campusName + "InsideListen";
			ListenThread listen = new ListenThread(threadName, SeverSocket, roomRecords);
			listen.start();
		
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public boolean login(String id) {
		
		//String[] args = new String[]{id};
		//LogItem log = new LogItem(RequestType.login, args);
		
		boolean result = users.Login(id);
		
		//log.setResult(result);
		//writer.write(log);
		
		return result;
		
       
	}
	
	@Override
	public ArrayList<Boolean> createRoom(String id, String room_Number, String date, ArrayList<String> list_Of_Time_Slots) {
		
		users.addUser(id);
		ArrayList<Boolean> resultArray = new ArrayList<Boolean>();
        
		for(int i = 0; i < list_Of_Time_Slots.size(); i++ ) {
        	String[] args = new String[]{id, room_Number, date, list_Of_Time_Slots.get(i)};
        	LogItem log = new LogItem(RequestType.AddRecord, id, args);
        		
        	Boolean result = roomRecords.creatRoom(id, room_Number, date, list_Of_Time_Slots.get(i));
        	
        	log.setResult(result);
    		writer.write(log);
    		
    		resultArray.add(result);
        	}
		
        	return resultArray;
	}

	@SuppressWarnings("static-access")
	@Override 
	public ArrayList<Boolean> deleteRoom(String id, String room_Number, String date, ArrayList<String> List_Of_Time_Slots) {
		
		ArrayList<Boolean> resultArray = new ArrayList<Boolean>();

        for (int i = 0; i < List_Of_Time_Slots.size(); i++ ) {
        	String[] args = new String[]{id, room_Number, date, List_Of_Time_Slots.get(i)};
            LogItem log = new LogItem(RequestType.DeleteRecord, id, args);

        	//if room haven't been booked, delete it directly;
        	if (roomRecords.getbookingID(room_Number, date, List_Of_Time_Slots.get(i)).equals("")) {
        		//System.out.println("bookingID is null");
        		Boolean result = roomRecords.deleteRoom(id, room_Number, date, List_Of_Time_Slots.get(i));
        		
        		log.setResult(result);
        		writer.write(log);
        		
        		resultArray.add(result);
        	}
        		
        	//if room is booked, delete it and minus bookCount at the same time;
        	else {
        		System.out.println("bookingID is not null");
        		String bookingID = roomRecords.getbookingID(room_Number, date, List_Of_Time_Slots.get(i));
        		String studentID = bookingID.substring(bookingID.length()-8);
        		String week = RoomRecordTools.toWeekFromBookingID(bookingID, roomRecords);
        		users.minusBookingCount(studentID, week);
        		Boolean result = roomRecords.deleteRoom(id, room_Number, date, List_Of_Time_Slots.get(i));
        		
        		log.setResult(result);
        		writer.write(log);
        		
        		resultArray.add(result);
        	}
        }
        
        return resultArray;
       
	}

	@Override 
	public String bookRoom(String id, String aimCampus, String roomNumber, String date, String timeslot) {
		System.out.println("Book Room start");
		users.addUser(id);
		String[] args = new String[]{id, aimCampus, roomNumber, date, timeslot};
        LogItem log = new LogItem(RequestType.Book, id, args);
		
        String week = "";
		String bookingID = "";
			
		week = Tools.toWeekFromDate(date);
		// can not more than 3 times a week
		if (users.getBookingCount(id, week) >= 3) {
			
			System.out.println("student: " + id + "week: " + week + "count: " + users.getBookingCount(id, week));
				
			log.setResult(false);
    		writer.write(log);
    		
			return "";
		}
		
		//according the campusName, invoke different method, get the bookingID
		if (aimCampus.equals(campusName)) {
			//System.out.println("Local server bookRoom");
			bookingID = roomRecords.bookRoom(aimCampus, id, roomNumber, date, timeslot);
			
		}
		else {
			//System.out.println("UDP server bookRoom");
			//int SeverRequstPort = campus.getUDPrequestPort(campusName);
			int UDPlistenPort = campus.getInsideUDPlistenPort(aimCampus);
				
			String command = "bookRoom(" + aimCampus + ", " + roomNumber + ", " + date + ", " + timeslot + ", " + id +")";
			bookingID = UDPrequest.UDPbookRoom(command, UDPlistenPort);
		}
		
		//according to bookingID, write the logFile
		if ( bookingID != null && !bookingID.equals("")) {
			System.out.println("bookingID is not null or empty" + bookingID);
			bookingID = bookingID.trim();
			users.plusBookingCount(id, week);
			
			log.setResult(true);
			log.setResponse(bookingID);
    		writer.write(log);
		}
		else {
			bookingID = "";
			
			log.setResult(false);
    		writer.write(log);
		}
		
		return bookingID;
      
	}

	@Override
	public String getAvailableTimeslot(String id, String date)   {
		
		String[] args = new String[]{id, date};
        LogItem log = new LogItem(RequestType.GetAvailTimeSlot, id, args);
		
        String[] result = new String[3];
		for (String each : campus.getCompusList()) {
			//System.out.println("each compus: " + each);
			if (each.equals(campusName)) {
				String avail = roomRecords.getAvailableTimeSlot(date, id).trim();
				if (avail.startsWith("DVL")) {
					result[0] = avail;
				}
				if (avail.startsWith("KKL")) {
					result[1] = avail;
				}
				if (avail.startsWith("WST")) {
					result[2] = avail;
				}
			}
			else {
				//System.out.println("UDP server Available Time");
				//int SeverRequstPort = campus.getUDPrequestPort(campusName);
				int UDPlistenPort = campus.getInsideUDPlistenPort(each);
				String command = "getAvailableTimeSlot(" +id + ", " + date + ")";
				String avail = UDPrequest.UDPgetAvailableTimeSlot(command, UDPlistenPort);
				
				if (avail.startsWith("DVL")) {
					result[0] = avail;
				}
				if (avail.startsWith("KKL")) {
					result[1] = avail;
				}
				if (avail.startsWith("WST")) {
					result[2] = avail;
				}
			}
		}
		
		String output = "";
		for (String each : result) {
			output = output + each + " ";
		}
		
		log.setResult(true);
		log.setResponse(output.trim());
		writer.write(log);
		
		return output.trim();
	}
	
	@SuppressWarnings("static-access")
	@Override
	public Boolean cancelBook(String id, String bookingID)   {
		String[] args = new String[]{id, bookingID};
        LogItem log = new LogItem(RequestType.CancelBook, id, args);
		
        String aimCampus = bookingID.substring(0, 3);
        String OriginalStudentID = bookingID.substring(bookingID.length()-8);
        String week = "";
		Boolean ifSuccess = false;
		
		if (!id.equals(OriginalStudentID)) {
			log.setResult(false);
			writer.write(log);
			
			return false;
		}
			
		if (aimCampus.equals(campusName)) {
			System.out.println("Local server cancelBooking");
			week = RoomRecordTools.toWeekFromBookingID(bookingID, roomRecords);
			ifSuccess = roomRecords.cancelBooking(bookingID);	
		}	
			
		else {
			System.out.println("UDP server cancelBooking");
			//int SeverRequstPort = campus.getUDPrequestPort(campusName);
			int UDPlistenPort = campus.getInsideUDPlistenPort(aimCampus);
			
			String commandWeek = "getWeek(" + bookingID + ")";
			week = UDPrequest.UDPgetWeek(commandWeek, UDPlistenPort);
			
			String command = "cancelBooking(" +id + ", " + bookingID + ")";
			ifSuccess =  UDPrequest.UDPcancelBooking(command, UDPlistenPort);		
		}
		
		if (ifSuccess == true) {
			//System.out.println("Minus the count after cancel");
			users.minusBookingCount(id, week);
			
			log.setResult(true);
			writer.write(log);
		}
		
		else {
			log.setResult(false);
			writer.write(log);
		}
		
		return ifSuccess;
       
	}
	
	@SuppressWarnings("unused")
	@Override
	public String changeReservation(String id, String bookingID, String new_campus_name, String new_room_no, String new_time_slot)   {	
		String[] args = new String[]{id, bookingID, new_campus_name, new_room_no, new_time_slot};
        LogItem log = new LogItem(RequestType.ChangeReservation, id, args);
        
        
		String cancelling_aimCampus = bookingID.substring(0, 3);
		String OriginalStudentID = bookingID.substring(bookingID.length()-8);
		
		if (!id.equals(OriginalStudentID)) {
			log.setResult(false);
			writer.write(log);
			
			return "";
		}
		
        String date = "";
		String week = "";
		String new_bookingID = "";
			
		//int cancelling_SeverRequstPort = campus.getUDPrequestPort(campusName);
		int cancelling_UDPlistenPort = campus.getInsideUDPlistenPort(cancelling_aimCampus);
		String command = "BookingIDexist(" + bookingID + ")";
		String ifexist = UDPrequest.UDPBookingIDexist(command, cancelling_UDPlistenPort);	
			
		if (ifexist == null || ifexist.equals("")) {
				
			log.setResult(false);
			writer.write(log);
				
			return "";
		}
		else {
			String[] info = ifexist.split(" ");
			date = info[0].trim();
			week = Tools.toWeekFromDate(date);
		}
			
		//int book_SeverRequstPort = campus.getUDPrequestPort(campusName);
		int book_UDPlistenPort = campus.getInsideUDPlistenPort(new_campus_name);
			
		command = "bookRoom(" + new_campus_name + ", " + new_room_no + ", " + date + ", " + new_time_slot + ", " + id +")";
		new_bookingID = UDPrequest.UDPbookRoom(command, book_UDPlistenPort);
			
		if (new_bookingID != null && !new_bookingID.equals("")) {
			new_bookingID = new_bookingID.trim();
			users.plusBookingCount(id, week);
		}
		else {
			log.setResult(false);
			writer.write(log);
				
			return "";
		}
			
		Boolean ifsuccess = cancelBook(id, bookingID);
			
		if (ifsuccess == false) {
			Boolean cancelNewBooking = cancelBook(id, new_bookingID);
				
			log.setResult(false);
			writer.write(log);
				
			return "";
		}
		else {
			log.setResult(true);
			log.setResponse(new_bookingID);
			writer.write(log);
		}
		return new_bookingID;
	}

	@Override
	public boolean storeData(String file) {
		
		FileOutputStream fs;
		try {
			lock.readLock().lock();
			
			fs = new FileOutputStream(file);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			
			os.writeUTF(campusName);
			os.writeObject(campus);
			os.writeObject(users);
			os.writeObject(roomRecords);
			os.writeObject(RoomRecordTools);
			os.writeObject(TimeSlot.rand);
			
		
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			lock.readLock().unlock();
		}
		
		return true;
	}

	@Override
	@SuppressWarnings("resource")
	public boolean loadData(String f_name) {
		try {
			lock.writeLock().lock();
			
			FileInputStream fileStream = new FileInputStream(f_name);
			ObjectInputStream os = new ObjectInputStream(fileStream);

			campusName = os.readUTF();;
			campus = (Campus)os.readObject();
			users = (UserSystem)os.readObject();
			roomRecords = (RoomRecords)os.readObject();
			RoomRecordTools = (Tools)os.readObject();
			TimeSlot.rand = (Random)os.readObject();
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} finally {
			lock.writeLock().unlock();
		}
		
		return true;
	}
	
}
