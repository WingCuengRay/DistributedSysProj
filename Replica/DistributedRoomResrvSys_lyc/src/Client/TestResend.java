package Client;

import java.util.ArrayList;

import tools.SeqRequest;
import tools.UDPConnection;

public class TestResend {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client.seq_num.put("DVL", 2);
		Client client = ClientFactory.createClient("DVLS1000");
		client.Login("DVLS1000", "");
		client.Connect();
		
		client.getAvailableTimeslot("2017-12-12");
		client.getAvailableTimeslot("2017-12-13");
		client.getAvailableTimeslot("2017-12-14");
		
		ArrayList<String> func = new ArrayList<String>();
		func.add("getAvailableTimeslot");
		func.add("DVLS1000");
		func.add("2017-12-11");
		
		UDPConnection udp = new UDPConnection("localhost", 13320);
		SeqRequest seqReq = new SeqRequest(1, "Replica_1", func);
		udp.Send(seqReq);
	}

}
