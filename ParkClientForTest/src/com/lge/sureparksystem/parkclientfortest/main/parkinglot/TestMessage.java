package com.lge.sureparksystem.parkclientfortest.main.parkinglot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestMessage {
	static Random rand = null;
	static List<String> strArray = new ArrayList<String>();

	static {
		rand = new Random();
		
		strArray.add("{\"messagetype\" : \"Authentication_Request\", \"id\" : \"aaabbbccc\", \"pwd\" : \"12341234\"}");
		strArray.add(
				"{\"messagetype\" : \"Parkinglot_Information\", \"slot_count\" : 4, \"slot_status\" : [\"1\", \"1\", \"0\", \"1\"], \"led_status\" : [\"0\", \"0\", \"0\", \"0\"], \"entrygate\" : \"0\", \"exitgate\" : \"0\", \"entrygateled\" : \"0\", \"exitgateled\" : \"1\", \"entrygate_arrive\" : \"0\", \"exitgate_arrive\" : \"0\", \"timestamp\" : 5}");
		strArray.add(
				"{\"messagetype\" : \"Parkingslot_Sensor\", \"slot_number\" : 2, \"status\" : \"empty\", \"timestamp\" : 10}");
		strArray.add(
				"{\"messagetype\" : \"Parkingslot_LED\", \"slot_number\" : 2, \"status\" : \"off\", \"timestamp\" : 15}");
		strArray.add("{\"messagetype\" : \"EntryGate_Servo\", \"status\" : \"down\", \"timestamp\" : 30}");
		strArray.add("{\"messagetype\" : \"ExitGate_Servo\", \"status\" : \"down\", \"timestamp\" : 40}");
		strArray.add("{\"messagetype\" : \"EntryGate_LED\", \"status\" : \"red\", \"timestamp\" : 50}");
		strArray.add("{\"messagetype\" : \"ExitGate_LED\", \"status\" : \"red\", \"timestamp\" : 60}");
		strArray.add("{\"messagetype\" : \"EntryGate_Arrive\", \"timestamp\" : 100}");
		strArray.add("{\"messagetype\" : \"EntryGate_PassBy\", \"timestamp\" : 110}");
		strArray.add("{\"messagetype\" : \"ExitGate_Arrive\", \"timestamp\" : 200}");
		strArray.add("{\"messagetype\" : \"ExitGate_PassBy\", \"timestamp\" : 210}");
		strArray.add("{\"messagetype\" : \"HeartBeat\", \"timestamp\" : 300}");
	}
	
	static public String getTestMessage() {
		return strArray.get(rand.nextInt(strArray.size())); 
	}
}
