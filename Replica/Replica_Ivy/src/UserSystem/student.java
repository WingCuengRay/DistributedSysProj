package UserSystem;

import java.util.HashMap;

public class student extends User {
	
	private HashMap<String, Integer> bookingCount;
	
	public student(String studentID, String password) {
		super(studentID, password);
		bookingCount = new HashMap<String, Integer>();
	}
		
	public int getBookingCount(String week) {
		if (!bookingCount.containsKey(week)) {
			return 0;
		}
		else {
			return bookingCount.get(week);
		}
	}
	
	public boolean plusBookingCount(String week) {
		if (bookingCount.containsKey(week)) {
			int newCount = bookingCount.get(week) + 1;
			bookingCount.put(week, newCount);
			return true;
		}
		else{
			bookingCount.put(week, 1);
			return true;
		}
	}
	
	public boolean minusBookingCount(String week) {
		int newCount = bookingCount.get(week) - 1;
		bookingCount.put(week, newCount);
		return true;
	}
	
}
