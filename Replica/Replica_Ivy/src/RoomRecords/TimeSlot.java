package RoomRecords;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class TimeSlot implements Serializable {

	private String date;
	private String RecordID;
	private Calendar start;
	private Calendar end;
	private String bookingID;
	private static int serialNum = 1001;
	public static Random rand = new Random(120152679);
	
	public TimeSlot(String campusName, String timeAvail, String date) {
		//set the unique RecordID
		if ( campusName.equals("DVL")) {
			RecordID = "RR" + "1" + String.valueOf(serialNum);
		}
		if ( campusName.equals("KKL")) {
			RecordID = "RR" + "2" + String.valueOf(serialNum);
		}
		if ( campusName.equals("WST")) {
			RecordID = "RR" + "3" + String.valueOf(serialNum);
		}
		serialNum += 1;
		
		this.date = date;
		//put the time slot
		start = Tools.toCalender(date, timeAvail, true);
		end = Tools.toCalender(date, timeAvail, false);
		
		//set initial bookingID
		bookingID = null;	
	}
	
	public String getRecordID() {
		return RecordID;
	}
	
	public String getTimeSlot() {
		
        int hourStart   = start.get(Calendar.HOUR);    
        int minuteStart = start.get(Calendar.MINUTE); 
        
        int hourEnd   = end.get(Calendar.HOUR);    
        int minuteEnd = end.get(Calendar.MINUTE); 
        
		String s = hourStart + ":" + minuteStart;
		String e = hourEnd + ":" + minuteEnd;
		return (s + "-" + e);
	}
	
	public String getBookingID() {
		if (bookingID == null) {
			return "";
		}
		else{
			return bookingID;
		}
	}
	
	public int conflictTo(String timeSlot) {
		Calendar startNew = Tools.toCalender(date, timeSlot, true);
		Calendar endNew = Tools.toCalender(date, timeSlot, false);
		
		// the timeSlot can totally match, return 0;
		if(startNew.compareTo(start) == 0  && endNew.compareTo(end) == 0) {
			return 0;
		}
		else {
			//time slot not conflict
			if (startNew.compareTo(end) >= 0 || endNew.compareTo(start) <= 0) {
				return -1;
			}
			else{
				//time slot confict
				return 1;
			}
		}
	}
	
	public int booked() {
		//room slot have not be booked, return -1;
		if (bookingID == null || bookingID.equals("")) {
			return -1;
		}
		//room slot is already booked, return 1;
		else {
			return 1;
		}
	}
	

	public String addBookingID(String campusName, String studentID) {
		//failure, room slot is already booked, return -1;
		if (booked() > 0) {
			return null;
		}
		//Success! room slot have not be booked, add bookingID, return 1;
		else{
			//bookingId = "DVL" + Random + studentID;
			int tempNum = rand.nextInt(100000);
			bookingID = campusName + tempNum + studentID;
			return bookingID;
		}
	}
	
	public Boolean removeBookingID() {
		//bookingId = "DVL" +RecordID + studentID;
		if (bookingID != null && !bookingID.equals("")) {
			bookingID = null;
			return true;
		}
		else {
			return false;
		}
	}
	
	public String toString() {
		
		String timeSlot = start.get(Calendar.HOUR) + ":" + start.get(Calendar.MINUTE) + "-" + end.get(Calendar.HOUR) + ":" + end.get(Calendar.MINUTE);
				
		String output = RecordID + ", " + timeSlot + ", " + bookingID + ", " + serialNum;
		
		return output;
	}

}
