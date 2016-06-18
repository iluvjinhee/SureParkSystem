package com.lge.sureparksystem.parkserver.manager.communicationmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.message.Message;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.AuthenticationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkViewNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ReservationManagerTopic;

public class CommunicationManager extends ManagerTask {
	public class CommunicationManagerListener {
		@Subscribe
		public void onSubscribe(CommunicationManagerTopic topic) {
			System.out.println("CommunicationManagerListener: " + topic);
			
			process(topic);
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
	protected void process(ManagerTopic topic) {
		JSONObject jsonObject = topic.getJsonObject();		
		
		switch (MessageParser.getMessageType(topic.getJsonObject())) {
		case RESERVATION_CODE:
			getEventBus().post(new ReservationManagerTopic(jsonObject));
			break;
		case ASSIGNED_SLOT:
			getEventBus().post(new ParkViewNetworkManagerTopic(jsonObject));
			break;
		case NOT_RESERVED:
			getEventBus().post(new ParkViewNetworkManagerTopic(jsonObject));
			break;
		case AUTHENTICATION_REQUEST:
			getEventBus().post(new AuthenticationManagerTopic(jsonObject));
			break;
		default:
			break;
		}
	}	
}
