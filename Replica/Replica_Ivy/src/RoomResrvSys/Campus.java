package RoomResrvSys;

import java.util.ArrayList;
import java.util.HashMap;


public class Campus {
	/*define basic campus Info
	 * HashMap<String campusName, String[IP, OutsideUDPlistenPort, InsideUDPlistenPort]>
	 */
	private HashMap<String, String[]> campusInfo;
	
	public Campus() {
		campusInfo = new HashMap<String, String[]>();
		//put the info of 3 different campus
		String[] DVLinfo = {"", "13330", "25560"};
		campusInfo.put("DVL", DVLinfo);
		String[] KKLinfo = {"", "13331", "25561" };
		campusInfo.put("KKL", KKLinfo);
		String[] WSTinfo = {"", "13332", "25562"};
		campusInfo.put("WST", WSTinfo);
	}
	
	public String getIP(String campusName) {
		if (campusInfo.containsKey(campusName)) {
			return campusInfo.get(campusName)[0];
		}
		else{
			return null;
		}
	}
	
	public int getWebport(String campusName) {
		if (campusInfo.containsKey(campusName)) {
			return Integer.valueOf(campusInfo.get(campusName)[1]);
		}
		else{
			return -1;
		}
	}
	
	public int getInsideUDPlistenPort(String campusName) {
		if (campusInfo.containsKey(campusName)) {
			return Integer.valueOf(campusInfo.get(campusName)[2]);
		}
		else{
			return -1;
		}
	}
	
	/*public int getUDPrequestPort(String campusName) {
		if (campusInfo.containsKey(campusName)) {
			return Integer.valueOf(campusInfo.get(campusName)[3]);
		}
		else{
			return -1;
		}
	}*/
	
	/*public void addCampus(String campusName, String IP, int RMIport,  int UDPport){
		String[] info = {"", String.valueOf(RMIport), String.valueOf(UDPport)};
		campusInfo.put(campusName, info);
	}
	
	public void removeCampus(String campusName){
		campusInfo.remove(campusName);
	}
	
	public void modifyIP(String campusName, String IP){
		String RMIport = campusInfo.get(campusName)[1];
		String UDPport = campusInfo.get(campusName)[2];
		campusInfo.remove(campusName);//remove the old one
		
		String[] info = {IP, RMIport, UDPport};
		campusInfo.put(campusName, info);//add a new array
	}
	
	public void modifyRMIport(String campusName, int RMIport){
		String IP = campusInfo.get(campusName)[0];
		String UDPport = campusInfo.get(campusName)[2];
		campusInfo.remove(campusName);//remove the old one
		
		String[] info = {IP, String.valueOf(RMIport), UDPport};
		campusInfo.put(campusName, info);//add a new array
	}
	
	public void modifyUDPport(String campusName, int UDPport){
		String IP = campusInfo.get(campusName)[0];
		String RMIport = campusInfo.get(campusName)[1];
		campusInfo.remove(campusName);//remove the old one
		
		String[] info = {IP, RMIport, String.valueOf(UDPport)};
		campusInfo.put(campusName, info);//add a new array
	}

	public boolean containsKey(String campusName) {
		if (campusInfo.containsKey(campusName)) { 
			return true;
		}
		else {
			return false;
		}
	}*/
	
	public ArrayList<String> getCompusList() {
		ArrayList<String> compusList = new ArrayList<String>();
		for (String campus : campusInfo.keySet()) {
			compusList.add(campus);
		}
		return compusList;
	}

}
