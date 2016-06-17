package com.lge.sureparksystem.parkserver.manager.communicationmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.message.Message;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkViewNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ReservationManagerTopic;

public class CommunicationManager extends ManagerTask {
	public class CommunicationManagerListener {
		@Subscribe
		public void onSubscribe(CommunicationManagerTopic topic) {
			System.out.println("CommunicationManagerListener");
			System.out.println(topic);
			
			process(topic.getJsonObject());
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
	
	public void process(JSONObject jsonObject) {
		Message message = MessageParser.makeMessage(jsonObject);

		if(message == null) 
			return;
		
		MessageType messageType = message.getMessageType();
		
		if(messageType == null) {
			System.out.println("");
			System.out.println("NOT PARSABLE MESSAGE TYPE !!!!!:");
			System.out.println(jsonObject.toJSONString());
			System.out.println("");
			
			return;
		}
		
		switch (messageType) {
		case RESERVATION_CODE:
			getEventBus().post(new ReservationManagerTopic(jsonObject));
			break;
		case ASSIGNED_SLOT:
			getEventBus().post(new ParkViewNetworkManagerTopic(jsonObject));
			break;
		case NOT_RESERVED:
			getEventBus().post(new ParkViewNetworkManagerTopic(jsonObject));
			break;
		default:
			break;
		}
	}
}
