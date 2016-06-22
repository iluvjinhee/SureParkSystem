package com.lge.sureparksystem.parkserver.manager.reservationmanager;

import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkHereNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ReservationManagerTopic;

public class ReservationManager extends ManagerTask {
	public class ReservationManagerListener {
		@Subscribe
		public void onSubscribe(ReservationManagerTopic topic) {
			System.out.println("ReservationManagerListener: " + topic);
			
			processMessage(topic.getJsonObject());
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

	@Override
	protected void processMessage(JSONObject jsonObject) {
		switch(MessageParser.getMessageType(jsonObject)) {
		case CONFIRMATION_SEND:
			processVerification(jsonObject);
			break;
		case PARKING_LOT_INFORMATION:
			storeParkingLotInformation(jsonObject);
			break;
		case RESERVATION_REQUEST:
			break;
		case RESERVATION_INFO_REQUEST:
			break;
		default:
			break;
		}
	}

	private void storeParkingLotInformation(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		
	}

	private void processVerification(JSONObject jsonObject) {
		String confirmationInfo = MessageParser.getString(jsonObject, DataMessage.CONFIRMATION_INFO);
		
		if(isValid(confirmationInfo)) {
			DataMessage dataMessage = new DataMessage(MessageType.CONFIRMATION_RESPONSE);
			dataMessage.setResult("ok");
			dataMessage.setSlotNumber(getAvailableSlot());
			
			getEventBus().post(new CommunicationManagerTopic(dataMessage));
		}
		else {
			DataMessage dataMessage = new DataMessage(MessageType.CONFIRMATION_RESPONSE);
			dataMessage.setResult("nok");
			
			getEventBus().post(new CommunicationManagerTopic(dataMessage));
		}
	}
	
	private void callAttendant(String string) {
		DataMessage message = new DataMessage(MessageType.NOTIFICATION);
		message.setType(string);
		
		getEventBus().post(new ParkHereNetworkManagerTopic(message));		
	}
}
