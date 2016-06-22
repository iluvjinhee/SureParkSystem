package com.lge.sureparksystem.parkclientfortest.main.parkview;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestMessage {
	static Random rand = null;
	static List<String> strArray = new ArrayList<String>();

	static {
		rand = new Random();
		
		strArray.add("{\"messagetype\" : \"Authentication_Request\", \"id\" : \"aaabbbccc\", \"pwd\" : \"12341234\"}");
		strArray.add("{\"messagetype\" : \"Authentication_Request\", \"id\" : \"SP001\", \"pwd\" : \"SP001\"}");
		strArray.add("{\"messagetype\" : \"Confirmation_Send\", \"confirmationinfo\" : \"aabbccddeeff\"}");
	}
	
	static public String getTestMessage() {
		return strArray.get(rand.nextInt(strArray.size())); 
	}
}
