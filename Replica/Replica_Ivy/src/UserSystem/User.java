package UserSystem;

public class User {
	private String userID;
	private String password;
	private String userType;
	
	public User(String userID, String password) {
		this.userID = userID;
		this.password = password;
		userType = userID.substring(3, 4);
	}
	
	public String getuserID() {
		return userID;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getuserType() {
		return userType;
	}
	
	public String toString() {
		return userID + "  " + password + "  ";
	}
}
