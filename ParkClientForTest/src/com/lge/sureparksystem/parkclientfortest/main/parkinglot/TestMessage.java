package com.lge.sureparksystem.parkclientfortest.main.parkinglot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestMessage {
	static Random rand = null;
	static List<String> strArray = new ArrayList<String>();

	static {
		rand = new Random();
		
		strArray.add("{\"MessageType\":\"HeartBeat\",\"TimeStamp\":0}");
		strArray.add("{\"MessageType\":\"Authentication_Request\",\"id\":\"team5-1\",\"pwd\":\"abcd1234\",\"TimeStamp\":1}");
		strArray.add("{\"MessageType\":\"EntryGate_Arrive\",\"TimeStamp\":2}");
		strArray.add("{\"MessageType\":\"EntryGate_PassBy\",\"TimeStamp\":3}");
		strArray.add("{\"MessageType\":\"ExitGate_Arrive\",\"TimeStamp\":4}");
		strArray.add("{\"MessageType\":\"ExitGate_PassBy\",\"TimeStamp\":5}");
		strArray.add("{\"MessageType\":\"Parkingslot_Sensor\",\"sensor_number\":0,\"status\":\"1\",\"TimeStamp\":7}");
		strArray.add("{\"MessageType\":\"Parkingslot_Sensor\",\"sensor_number\":0,\"status\":\"0\",\"TimeStamp\":8}");
		strArray.add("{\"MessageType\":\"Parkingslot_Sensor\",\"sensor_number\":1,\"status\":\"1\",\"TimeStamp\":9}");
		strArray.add("{\"MessageType\":\"Parkingslot_Sensor\",\"sensor_number\":1,\"status\":\"0\",\"TimeStamp\":10}");
		strArray.add("{\"MessageType\":\"Parkingslot_Sensor\",\"sensor_number\":2,\"status\":\"1\",\"TimeStamp\":11}");
		strArray.add("{\"MessageType\":\"Parkingslot_Sensor\",\"sensor_number\":2,\"status\":\"0\",\"TimeStamp\":13}");
		strArray.add("{\"MessageType\":\"Parkingslot_Sensor\",\"sensor_number\":3,\"status\":\"1\",\"TimeStamp\":14}");
		strArray.add("{\"MessageType\":\"Parkingslot_Sensor\",\"sensor_number\":3,\"status\":\"0\",\"TimeStamp\":15}");
		strArray.add("{\"MessageType\":\"Parkinglot_Information\",\"slot_number\":4,\"slot_status\":[\"0\",\"0\",\"0\",\"0\"],\"led_status\":[\"0\",\"0\",\"0\",\"0\"],\"entrygate\":\"0\",\"exitgate\":\"0\",\"entrygateled\":\"1\",\"exitgateled\":\"1\",\"TimeStamp\":19}");
		strArray.add("{\"MessageType\":\"HeartBeat\",\"TimeStamp\":11}");
		strArray.add("{\"MessageType\":\"EntryGate_Arrive\",\"TimeStamp\":19}");
		strArray.add("{\"MessageType\":\"EntryGate_PassBy\",\"TimeStamp\":20}");
		strArray.add("{\"MessageType\":\"ExitGate_Arrive\",\"TimeStamp\":22}");
		strArray.add("{\"MessageType\":\"ExitGate_PassBy\",\"TimeStamp\":23}");
		strArray.add("{\"MessageType\":\"Parkingslot_Sensor\",\"sensor_number\":0,\"status\":\"empty\",\"TimeStamp\":15}");
		strArray.add("{\"MessageType\":\"Parkingslot_Sensor\",\"sensor_number\":0,\"status\":\"occupied\",\"TimeStamp\":14}");
	}
	
	static public String getTestMessage() {
		return strArray.get(rand.nextInt(strArray.size())); 
	}
}
