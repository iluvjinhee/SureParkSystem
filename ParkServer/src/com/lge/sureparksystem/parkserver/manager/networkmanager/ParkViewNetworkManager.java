package com.lge.sureparksystem.parkserver.manager.networkmanager;

import java.net.Socket;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkHereNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkViewNetworkManagerTopic;

public class ParkViewNetworkManager extends NetworkManager {
	public class ParkViewNetworkManagerListener {
		@Subscribe
		public void onSubscribe(ParkViewNetworkManagerTopic topic) {
			System.out.println("ParkViewNetworkManagerListener: " + topic);
			
			processMessage(topic.getJsonObject());
		}
	}
	
	@Override
	public void init() {
		getEventBus().register(new ParkViewNetworkManagerListener());
	}
	
	public ParkViewNetworkManager(int serverPort) {
		super(serverPort);
	}
	
	public void run() {
		super.run();
	}
	
	@Override
	public void receive(JSONObject jsonObject) {
		processMessage(jsonObject);
	}
	
	@Override
	public void onSocketAccepted(Socket socket) {
		super.onSocketAccepted(socket);
	}
	
	protected void processMessage(JSONObject jsonObject) {
		super.processMessage(jsonObject);
		
		MessageType messageType = MessageParser.getMessageType(jsonObject);
		
		switch (messageType) {
		case WELCOME_DISPLAY:
		case QR_START:
		case CONFIRMATION_RESPONSE:
			send(jsonObject);
			break;
		case CONFIRMATION_SEND:
			getEventBus().post(new CommunicationManagerTopic(jsonObject));
			break;
		default:
			break;
		}
		
		return;
	}
}