package RoomRecords;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import UserSystem.UserSystem;

public class RoomRecords {
	//private String severId;
		//HashMap<String date, HashMap<String roomNum, ArrayList<TimeSlot>>> RoomRecords;
		private static HashMap<String, HashMap<String, ArrayList<TimeSlot>>> RoomRecords;
		private String campusName;
		private ReadWriteLock lock = new ReentrantReadWriteLock();
		
		public RoomRecords(String campusName, UserSystem users) {
			super();
			
			this.campusName = campusName;
			
			//put the initial values to RoomRecords;
			RoomRecords = new HashMap<String, HashMap<String, ArrayList<TimeSlot>>>();
			/*addTimeFromDate("201", "2017-09-17", "7:30-9:30");
			addTimeFromTime("201", "2017-09-17", "9:30-11:30");
			addTimeFromTime("201", "2017-09-17", "11:30-13:30");
			addTimeFromDate("201", "2017-09-18", "7:30-9:30");
			addTimeFromTime("201", "2017-09-18", "9:30-11:30");
			addTimeFromTime("201", "2017-09-18", "11:30-13:30");*/
				
		}
		
		public String getRoomInfoFromBookingID(String bookingID) {
			
			Lock readLock = lock.readLock();  
	        readLock.lock(); 
	        
			try{
				for(String date : RoomRecords.keySet()) {
					for(String room : RoomRecords.get(date).keySet()) {
						for(TimeSlot time : RoomRecords.get(date).get(room)) {
							if (time.getBookingID().equals(bookingID)) {
								//System.out.println("Repeat::" + date + " " + room +" "+ time.getTimeSlot());
								return (date + " " + room +" "+ time.getTimeSlot());
							}
						}
					}
				}
			//System.out.println("can not find the bookingID info");
				return "";
			}catch(Exception e){
	        	e.printStackTrace();
	        	return "";
	        }finally {
	        	readLock.unlock();
	        }
		}
		
		public String getbookingID(String room_Number, String date, String list_Of_Time_Slots) {
			Lock readLock = lock.readLock();  
	        readLock.lock(); 
	        
			try{
				if (!RoomRecords.containsKey(date)) {
					// can not find the date;
					return "";
				}
				else {
					if (!RoomRecords.get(date).containsKey(room_Number)) {
						// can not find the room;
						return "";
					}
					else {
						for(TimeSlot time : RoomRecords.get(date).get(room_Number)) {
							if (time.conflictTo(list_Of_Time_Slots) == 0) {
								return time.getBookingID();
							}
						}
						return "";
				}
			}
			}catch(Exception e){
	        	e.printStackTrace();
	        	return "";
	        }finally {
	        	readLock.unlock();
	        }	
		}
		
		private void addTimeFromDate(String room_Number, String date, String list_Of_Time_Slots) {
				
			TimeSlot time = new TimeSlot(campusName, list_Of_Time_Slots, date);
			ArrayList<TimeSlot> timeList = new ArrayList<TimeSlot>();
			timeList.add(time);
			HashMap<String, ArrayList<TimeSlot>> room = new HashMap<String, ArrayList<TimeSlot>>();
			room.put(room_Number, timeList);
			RoomRecords.put(date, room);
				
		}
		
		private void addTimeFromRoom(String room_Number, String date, String list_Of_Time_Slots) {
			
			TimeSlot time = new TimeSlot(campusName, list_Of_Time_Slots, date);
			ArrayList<TimeSlot> timeList = new ArrayList<TimeSlot>();
			timeList.add(time);
			RoomRecords.get(date).put(room_Number, timeList);
		
		}
		
		private void addTimeFromTime(String room_Number, String date, String list_Of_Time_Slots) {
			
			TimeSlot time = new TimeSlot(campusName, list_Of_Time_Slots, date);
			RoomRecords.get(date).get(room_Number).add(time);
			
		}
		
		public Boolean creatRoom(String AdminID, String room_Number, String date, String list_Of_Time_Slots){
			
			Lock writeLock = lock.writeLock();  
	        writeLock.lock(); 
	        try{
			if (!RoomRecords.containsKey(date)) {
				
				addTimeFromDate(room_Number, date, list_Of_Time_Slots);
				return true;
			}
			else {
				if (!RoomRecords.get(date).containsKey(room_Number)) {
					
					addTimeFromRoom(room_Number, date, list_Of_Time_Slots);
					return true;
				}
				else {
					for(TimeSlot time : RoomRecords.get(date).get(room_Number)) {
						if (time.conflictTo(list_Of_Time_Slots) >= 0) {
							return false;
						}
					}
					addTimeFromTime(room_Number, date, list_Of_Time_Slots);
					return true;
				}
			}
	        }catch(Exception e){
	        	e.printStackTrace();
	        	return false;
	        }finally {
	        	writeLock.unlock();
	        }
		}
		
		
		
		public Boolean deleteRoom(String AdminID, String room_Number, String date, String list_Of_Time_Slots) {
			
			Lock writeLock = lock.writeLock();  
			writeLock.lock(); 
	        try {
			String baseInfo = " " + date + " " + room_Number + " " + list_Of_Time_Slots + " ";
			if (!RoomRecords.containsKey(date)) {
				//can not find the date;
				return false;
			}
			else {
				if (!RoomRecords.get(date).containsKey(room_Number)) {
					// can not find the room;
					return false;
				}
				else {
					for(TimeSlot time : RoomRecords.get(date).get(room_Number)) {
						
						if (time.conflictTo(list_Of_Time_Slots) == 0) {
							
							RoomRecords.get(date).get(room_Number).remove(time);
							return true;
						}
					}
					// can not find the time;
					return false;
				}
			}
	        }catch(Exception e){
        		e.printStackTrace();
        		return false;
	        }finally {
        		writeLock.unlock();
	        }
		}
		
		
		public String getAvailableTimeSlot(String date, String id) {
			
			Lock readLock = lock.readLock();  
	        readLock.lock(); 
	        
	        try {
			int count = 0;
			if (RoomRecords.containsKey(date)) {
				for(String room : RoomRecords.get(date).keySet()) {
					for (TimeSlot time: RoomRecords.get(date).get(room)){
						if (time.booked() < 0) {
							count += 1;
						}
					}	
				}
			}
			String avail = campusName + " " + count;
			
			return avail;
			
	        }catch(Exception e){
	        	e.printStackTrace();
	        	return "";
	        }finally {
	        	readLock.unlock();
	        }
		}
		
		public String bookRoom(String campusName, String studentID, String roomNumber, String date, String timeslot) {
			
			Lock writeLock = lock.writeLock();  
			writeLock.lock(); 
	        try {
			
			if (!RoomRecords.containsKey(date)) {
				// can not find the date;
				return "";
			}
			else {
				System.out.println("find the date");
				if (!RoomRecords.get(date).containsKey(roomNumber)) {
					// can not find the room;
					return "";
				}
				else {
					System.out.println("find the room");
					for(TimeSlot time : RoomRecords.get(date).get(roomNumber)) {
						
						if (time.conflictTo(timeslot) == 0) {
							String bookingId =  time.addBookingID(campusName, studentID);
							if (bookingId == null) {
								return "";
							}
							else {
								return bookingId;
							}
						}
					}
					return "";
				}
			}
	        }catch(Exception e){
	        	e.printStackTrace();
	        	return "";
	        }finally {
	        	writeLock.unlock();
	        }
		}
		
		public boolean roomAvailability(String campusName, String studentID, String roomNumber, String date, String timeslot) throws Exception {
			System.out.println("roomAvailability");
			String baseInfo = campusName + " " + date + " " + roomNumber + " " + timeslot + " ";
			
			System.out.println("date:" + date);
			if (!RoomRecords.containsKey(date)) {
				// can not find the date;
				return false;
			}
			else {
				//System.out.println("find the date");
				if (!RoomRecords.get(date).containsKey(roomNumber)) {
					// can not find the room;
					return false;
				}
				else {
					//System.out.println("find the room");
					for(TimeSlot time : RoomRecords.get(date).get(roomNumber)) {
						//System.out.println("timeSlot info::::::"+time.getRecordID() + time.getTimeSlot() + time.getBookingID());
						if (time.conflictTo(timeslot) == 0 && time.booked() > 0) {
		
								return false;
							}
							else {
					
								return true;
							}
						}
					}
					
					return false;
				}
		}
		
		public Boolean cancelBooking(String bookingID) {
			
			Lock writeLock = lock.writeLock();  
			writeLock.lock(); 
			
	        try {
	        	//
	        	//return value of getRoomInfo (date + " " + room +" "+ time.getTimeSlot());
	        	if (getRoomInfoFromBookingID(bookingID) == null) {
	        		return false;
	        	}
	        	else {
	        		String infoString = getRoomInfoFromBookingID(bookingID);
	        		String[] info = infoString.split(" ");
	        		
	        		for (TimeSlot time : RoomRecords.get(info[0]).get(info[1])) {
	        			
	        			if (time.getBookingID().equals(bookingID)) {
							
	        				return time.removeBookingID();
	        			}
	        		}
	        		
	        		return false;
	        	}
	        }catch(Exception e){
	     		e.printStackTrace();
	     		return false;
	        }finally {
	     		writeLock.unlock();
	        }
	   }
		
	  public String toString(){
		  //private static HashMap<String, HashMap<String, ArrayList<TimeSlot>>> RoomRecords;
		  String output = new String();
		  for (String eachDate : RoomRecords.keySet()) {
			  for (String eachRoom : RoomRecords.get(eachDate).keySet()) {
				  for (TimeSlot eachTime : RoomRecords.get(eachDate).get(eachRoom)) {
					  output = output + eachDate + " " + eachRoom + " " + eachTime.toString() + "\r\n";
				  }
			  }
		  }
		  return output;  
	  }
	  
		
}
