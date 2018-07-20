package RoomResrvSys;

import java.util.ArrayList;



public interface RemoteServerInterface {
	
	public boolean login(String id);
	
	public ArrayList<Boolean> createRoom(String id, String room_Number, String date, ArrayList<String> List_Of_Time_Slots);
	
	public ArrayList<Boolean> deleteRoom(String id, String room_Number, String date, ArrayList<String> List_Of_Time_Slots);
	
	public String bookRoom(String id, String campusName, String roomNumber, String date, String timeslot);
	
	public String getAvailableTimeslot(String id, String date);
	
	public Boolean cancelBook(String id, String bookingID);
	
	public String changeReservation(String id, String bookingID, String new_campus_name, String new_room_no, String new_time_slot);
	
	public boolean storeData(String file);
	
	public boolean loadData(String file);
	
	
}
