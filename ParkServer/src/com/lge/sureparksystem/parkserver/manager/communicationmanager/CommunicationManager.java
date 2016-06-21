package com.lge.sureparksystem.parkserver.manager.communicationmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkViewNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ReservationManagerTopic;

public class CommunicationManager extends ManagerTask {
	public class CommunicationManagerListener {
		@Subscribe
		public void onSubscribe(CommunicationManagerTopic topic) {
			System.out.println("CommunicationManagerListener: " + topic);
			
			processMessage(topic.getJsonObject());
		}
	}
	
	@Override
	public void init() {
		getEventBus().register(new CommunicationManagerListener());
	}
	
	@Override
	public void run() {
		while(loop) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void processMessage(JSONObject jsonObject) {
		switch (MessageParser.getMessageType(jsonObject)) {
		case ENTRY_GATE_ARRIVE:
			getEventBus().post(new ParkViewNetworkManagerTopic(jsonObject));
			break;
		case ENTRY_GATE_PASSBY:
			getEventBus().post(new ReservationManagerTopic(jsonObject));
		case EXIT_GATE_ARRIVE:
		case EXIT_GATE_PASSBY:
		case SLOT_SENSOR_STATUS:
			break;
		default:
			break;
		}
	}	
}
