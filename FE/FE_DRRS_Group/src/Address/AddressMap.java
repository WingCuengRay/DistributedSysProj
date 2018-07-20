package Address;

public class AddressMap {
	
	private Address frontEnd;
	private Address sequencer;
	private Address replica_1;
	private Address replica_2;
	private Address replica_3;
	
	public AddressMap() {
		frontEnd = new Address("frontEnd", "192.168.0.146", 13360);
		sequencer = new Address("sequencer", "192.168.0.153", 13370);
		replica_1 = new Address("Replica_1", "192.168.0.162", 13350);
		replica_2 = new Address("Replica_2", "192.168.0.153", 13350);
		replica_3 = new Address("Replica_3", "192.168.0.160", 13350);
		
	}
	
	public Address get(String name){
		if(name.equals("frontEnd")) return frontEnd;
		if(name.equals("sequencer")) return sequencer;
		if(name.equals("Replica_1")) return replica_1;
		if(name.equals("Replica_2")) return replica_2;
		if(name.equals("Replica_3")) return replica_3;
		
		return null;
	}

}
