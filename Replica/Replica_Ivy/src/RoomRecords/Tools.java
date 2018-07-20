package RoomRecords;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Tools {
	public Tools() {
		super();
	}
	
	public static String toWeekFromDate(String date) {
		
		try{
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd"); 
			String year = date.substring(0, 4);
			Date day = sf.parse(date);
	
			//find the week
			Calendar calendar = Calendar.getInstance();  
			calendar.setFirstDayOfWeek(Calendar.SUNDAY);  
			calendar.setTime(day);  
			int week = calendar.get(Calendar.WEEK_OF_YEAR);
				
			String yearAndweek = year + week;
			return yearAndweek;	
		}catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String toWeekFromBookingID(String bookingID, RoomRecords roomRecords) {
		//System.out.println("getYearAndweek  " + bookingID);
		String[] info = null;
		String yearAndweek = "";
		
		String roominfo = roomRecords.getRoomInfoFromBookingID(bookingID);
		if ( roominfo != null) {
			//System.out.println(roominfo);
			info = roominfo.split(" ");
			yearAndweek = toWeekFromDate(info[0]);
		}
		
		return yearAndweek;
			
	}
	
	public static Calendar toCalender(String date, String time_slot, boolean ifstart) {
		
		Calendar timeslot = Calendar.getInstance();
		
		//get year, month, date
		String[] datelist = date.split("-");
		int year = Integer.valueOf(datelist[0].trim());
		int month = Integer.valueOf(datelist[1].trim());
		int day = Integer.valueOf(datelist[2].trim());
		
		timeslot.set(Calendar.YEAR, year);
		timeslot.set(Calendar.MONTH, month);
		timeslot.set(Calendar.DATE, day);
		
		//get hour, minute and second
		String[] timea = time_slot.split("-");
		int hour = 0;
		int minute = 0;
		if (ifstart == true) {  // get the start time
			String[] timeStart = timea[0].split(":");
			hour = Integer.valueOf(timeStart[0].trim());
			minute = Integer.valueOf(timeStart[1].trim());
		}
		else {  //get the end time
			String[] timeEnd = timea[1].split(":");
			hour = Integer.valueOf(timeEnd[0].trim());
			minute = Integer.valueOf(timeEnd[1].trim());
		}
		
		timeslot.set(Calendar.HOUR, hour);
		timeslot.set(Calendar.MINUTE, minute);
		timeslot.set(Calendar.SECOND, 0);
		timeslot.set(Calendar.MILLISECOND, 0);
		return timeslot;
	}
}
