package com.lge.sureparksystem.parkserver.manager.networkmanager;

import java.net.Socket;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.message.Message;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkViewNetworkManagerTopic;

public class ParkViewNetworkManager extends NetworkManager {
	public class ParkViewNetworkManagerListener {
		@Subscribe
		public void onSubscribe(ParkViewNetworkManagerTopic topic) {
			System.out.println("ParkViewNetworkManagerListener: " + topic);
			
			process(topic);
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
	public void onSocketAccepted(Socket socket) {
		super.onSocketAccepted(socket);
		
		send(MessageParser.makeJSONObject(new Message(MessageType.WELCOME_SUREPARK)));
	}
	
	protected void process(JSONObject jsonObject) {
		super.process(jsonObject);
		
		MessageType messageType = MessageParser.getMessageType(jsonObject);
		
		switch (messageType) {
		case RESERVATION_CODE:
			getEventBus().post(new CommunicationManagerTopic(jsonObject));
			break;
		case ASSIGN_SLOT:
			send(jsonObject);
			break;
		case NOT_RESERVED:
			send(jsonObject);
			break;
		default:
			break;
		}
		
		return;
	}
	
	
}