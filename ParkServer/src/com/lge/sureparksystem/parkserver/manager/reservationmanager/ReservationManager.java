package com.lge.sureparksystem.parkserver.manager.reservationmanager;

import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.message.Message;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.ParkViewNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ReservationManagerTopic;

public class ReservationManager extends ManagerTask {
	public class ReservationManagerListener {
		@Subscribe
		public void onSubscribe(ReservationManagerTopic topic) {
			System.out.println("ReservationManagerListener");
			System.out.println(topic);
			
			String reservationCode = MessageParser.makeMessage(topic.getJsonObject()).getGlobalValue();
			if(isValid(reservationCode)) {
				getEventBus().post(
						new ParkViewNetworkManagerTopic(
								new Message(
										MessageType.ASSIGN_SLOT, String.valueOf(getAvailableSlot()))));
			}
			else {
				getEventBus().post(
						new ParkViewNetworkManagerTopic(
								new Message(MessageType.NOT_RESERVED)));
			}
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

	public boolean isValid(String reservationCode) {
		boolean result = false;
		
		result = isValidConfirmationNumber_Temporary(reservationCode);
		
		return result;
	}
	
	boolean isValidConfirmationNumber_Temporary(String reservationCode) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) jsonParser.parse(reservationCode);
		} catch (ParseException e) {
			return false;
		}
		
		if(jsonObject.get("Name") != null) {
			return true;
		}
		
		return false;
	}
	
	public int getAvailableSlot() {
		Random r = new Random();

		return r.nextInt(getSlotSize()) + 1;
	}
	
	private int getSlotSize() {
		return 4;
	}
}
