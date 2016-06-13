package com.lge.sureparksystem.parkserver.manager.communicationmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.socketmessage.SocketMessage;
import com.lge.sureparksystem.parkserver.socketmessage.SocketMessageParser;
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
		SocketMessage socketMessage = SocketMessageParser.parseJSONObject(jsonObject);

		switch (socketMessage.getMessageType()) {
		case RESERVATION_NUMBER:
			getEventBus().post(new ReservationManagerTopic(jsonObject));
			break;
		case ASSIGN_SLOT:
			getEventBus().post(new ParkViewNetworkManagerTopic(jsonObject));
			break;
		case NOT_RESERVED:
			getEventBus().post(new ParkViewNetworkManagerTopic(jsonObject));
			break;
		}
	}
}
