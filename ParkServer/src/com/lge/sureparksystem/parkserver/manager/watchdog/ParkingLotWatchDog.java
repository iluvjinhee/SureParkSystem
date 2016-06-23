package com.lge.sureparksystem.parkserver.manager.watchdog;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.ParkHereNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkingLotWatchDogTopic;

public class ParkingLotWatchDog extends ManagerTask {
	private final int WARNING_LEVEL = 100;
	private int alertLevel = 0;
	
	public class WatchDogListener {
		@Subscribe
		public void onSubscribe(ParkingLotWatchDogTopic topic) {
//			System.out.println("WatchDogListener: " + topic);
			
			processMessage(topic.getJsonObject());
		}
	}
	
	@Override
	public void init() {
		getEventBus().register(new WatchDogListener());
		
		alertLevel = 0;
	}
	
	@Override
	public void run() {
		while(loop) {
			try {
				Thread.sleep(2000);
				
				alertLevel += 10;
				
				if(alertLevel > 100) {
					callAttendant("parkinglot error");
					loop = false;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void callAttendant(String string) {
		DataMessage message = new DataMessage(MessageType.NOTIFICATION);
		message.setType(string);
		
		getEventBus().post(new ParkHereNetworkManagerTopic(message));
	}

	@Override
	protected void processMessage(JSONObject jsonObject) {
		DataMessage message = (DataMessage) MessageParser.convertToMessage(jsonObject);
		
		switch(message.getMessageType()) {
		case HEARTBEAT:
			alertLevel = 0;
			break;
		default:
			break;
		}		
	}
}
