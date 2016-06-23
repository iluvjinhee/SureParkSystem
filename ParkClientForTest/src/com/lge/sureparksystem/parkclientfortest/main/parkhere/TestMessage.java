package com.lge.sureparksystem.parkclientfortest.main.parkhere;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestMessage {
	static Random rand = null;
	static List<String> strArray = new ArrayList<String>();

	static {
		rand = new Random();
		
//		strArray.add("{\"messagetype\" : \"Authentication_Request\", \"id\" : \"aaabbbccc\", \"pwd\" : \"12341234\"}");
		strArray.add("{\"messagetype\" : \"Authentication_Request\", \"id\" : \"aaa@aaa.com\", \"pwd\" : \"12341234\"}");
		strArray.add("{\"messagetype\" : \"Authentication_Request\", \"id\" : \"aaa@aaa.com\", \"pwd\" : \"12341234\"}");
		strArray.add("{\"messagetype\" : \"Authentication_Request\", \"id\" : \"aaa@aaa.com\", \"pwd\" : \"12341234\"}");
		strArray.add("{\"messagetype\" : \"Cancel_Request\", \"driver_id\" : \"louder81@gmail.com\", \"reservation_id\" : \"000111000111\"}");
		strArray.add("{\"messagetype\" : \"Change_graceperiod\", \"parkinglot_id\" : \"aabb\", \"graceperiod\" : \"120\"}");
		strArray.add("{\"messagetype\" : \"Change_parkingfee\", \"parkinglot_id\" : \"aabb\", \"parking_fee\" : \"10\"}");
		strArray.add("{\"messagetype\" : \"Create_Attendant\", \"id\" : \"aaa@aaa.com\", \"pwd\" : \"12341234\", \"name\" : \"dave\", \"parkinglot_id\" : \"aabb\"{");
		strArray.add("{\"messagetype\" : \"Create_Driver\", \"id\" : \"aaa@aaa.com\", \"pwd\" : \"12341234\", \"name\" : \"dave\"}");
		strArray.add("{\"messagetype\" : \"Create_Parkinglot\", \"id\" : \"aaa@aaa.com\", \"pwd\" : \"12341234\", \"address\" : \"aaaaaaaaa\", \"parking_fee\" : \"10\", \"graceperiod\" : \"120\"}");
		strArray.add("{\"messagetype\" : \"ParkinglotInfo_Request\"}");
		strArray.add("{\"messagetype\" : \"ParkinglotStatus_Request\"}");
		strArray.add("{\"messagetype\" : \"Parkinglot_stats_Request\", \"parkinglot_id\" : \"aabb\", \"period\" : \"week\"}");
		strArray.add("{\"messagetype\" : \"Remove_Attendant\", \"id\" : \"aaa@aaa.com\"}");
		strArray.add("{\"messagetype\" : \"Remove_Parkinglot\", \"id\" : \"aabb\"}");
		strArray.add("{\"messagetype\" : \"ReservationInfo_Request\", \"driver_id\" : \"louder81@gmail.com\"}");
		strArray.add("{\"messagetype\" : \"Reservation_Request\", \"driver_id\" : \"louder81@gmail.com\", \"parkinglot_id\" : \"aabb\", \"reservation_time\" : \"2016:06:24:12:00\", \"paymentinfo\" : \"2222333344445555\"}");
	}
	
	static public String getTestMessage() {
		return strArray.get(rand.nextInt(strArray.size())); 
	}
}
