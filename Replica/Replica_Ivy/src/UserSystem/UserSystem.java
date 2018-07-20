package UserSystem;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UserSystem {
	
	public static HashMap<String, administrator> adminRecords;
	public static HashMap<String, student> studentRecords;
	private String severName;
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public UserSystem(String campusName) {
		//put the initial values to adminRecords & studentRecords;
		severName = campusName;
		
		//each campus put 1 administrator
		adminRecords = new HashMap<String, administrator>();
		administrator user1 = new administrator(campusName + "A1000", "123456");//administrator
		administrator user2 = new administrator(campusName + "A2000", "123456");//administrator
		adminRecords.put(campusName + "A1000", user1);
		adminRecords.put(campusName + "A2000", user2);
		
		//each campus put 3 students
		studentRecords = new HashMap<String, student>();
		student user3 = new student(campusName + "S1000", "123456");//student
		student user4 = new student(campusName + "S1100", "123456");
		student user5 = new student(campusName + "S2000", "123456");
		studentRecords.put(campusName + "S1000", user3);
		studentRecords.put(campusName + "S1100", user4);
		studentRecords.put(campusName + "S2000", user5);
		
	}
	
	public boolean Login(String id) {
		
		Lock readLock = lock.readLock();  
        readLock.lock(); 
        try{
        	if (adminRecords.containsKey(id) || studentRecords.containsKey(id)) {
        		return true;
        	}
        	else{
        		addUser(id);
        		return true;
        	}
		/*if (userType.equals("A")) {
			//if (adminRecords.containsKey(id) && adminRecords.get(id).getPassword().equals(password)) {
			if (adminRecords.containsKey(id)) {
				return true;	
			}
			else {
				return false;
			}
		}
		else {
			//if (studentRecords.containsKey(id) && studentRecords.get(id).getPassword().equals(password)) {
			if (studentRecords.containsKey(id)){
				return true;	
			}
			else {
				return false;
			}
		}*/
			
        }catch(Exception e){
        	e.printStackTrace();
        	return false;
        }finally {
        	readLock.unlock();
        }
	}

	public void addUser(String id) {
		String userType = id.substring(3, 4);
		String userCampus = id.substring(0, 3);
		
		if (userCampus.equals(severName) && userType.equals("S")) {
			if (!studentRecords.containsKey(id)) {
				student stu = new student(id, "123456");//administrator
				studentRecords.put(id, stu);
				System.out.println("student added");
			}
		}
		
		if (userCampus.equals(severName) && userType.equals("A")) {
			if (!adminRecords.containsKey(id)) {
				administrator admin = new administrator(id, "123456");//administrator
				adminRecords.put(id, admin);
				System.out.println("adimn added");
			}
		}
	}
	
	public int getBookingCount(String id, String week) {
		
		Lock readLock = lock.readLock();  
        readLock.lock(); 
        
        try{
		
        	if (!studentRecords.containsKey(id)) {
        		return -1;
        	}
        	else {
        		int count = studentRecords.get(id).getBookingCount(week);
        		return count;
        	}
        	
        }catch(Exception e){
        	e.printStackTrace();
        	return 0;
        }finally {
        	readLock.unlock();
        }
	}
	
	public boolean plusBookingCount(String id, String week) {
		
		Lock writeLock = lock.writeLock();  
        writeLock.lock(); 
        try{
		
		if (studentRecords.containsKey(id)) {
			return studentRecords.get(id).plusBookingCount(week);	
		}
		else { return false; }
		
        }catch(Exception e){
        	e.printStackTrace();
        	return false;
        }finally {
        	writeLock.unlock();
        }
		
	}
	
	public boolean minusBookingCount(String id, String week) {
		
		Lock writeLock = lock.writeLock();  
        writeLock.lock(); 
        try{
		if (studentRecords.containsKey(id) && studentRecords.get(id).getBookingCount(week) > 0) {	
			return studentRecords.get(id).minusBookingCount(week);	
		}
		else { return false; }
        }catch(Exception e){
        	e.printStackTrace();
        	return false;
        }finally {
        	writeLock.unlock();
        }
	}
        

}
