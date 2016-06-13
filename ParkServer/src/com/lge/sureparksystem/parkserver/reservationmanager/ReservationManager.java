package com.lge.sureparksystem.parkserver.reservationmanager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.module.ManagerTask;
import com.lge.sureparksystem.parkserver.topic.ReservationManagerTopic;

public class ReservationManager extends ManagerTask {
	public class ReservationManagerListener {
		@Subscribe
		public void onSubscribe(ReservationManagerTopic topic) {
			System.out.println("ReservationManagerListener");
			System.out.println(topic);
		}
	}
	
	@Override
	public void init() {
		getEventBus().register(new ReservationManagerListener());
	}
	
	@Override
	public void run() {
		while(loop) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

	public boolean isValid(String confirmationNumber) {
		boolean result = false;
		
		result = isValidConfirmationNumber_Temporary(confirmationNumber);
		
		return result;
	}
	
	boolean isValidConfirmationNumber_Temporary(String confirmationNumber) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) jsonParser.parse(confirmationNumber);
		} catch (ParseException e) {
			return false;
		}
		
		if(jsonObject.get("Name") != null) {
			return true;
		}
		
		return false;
	}	
}
