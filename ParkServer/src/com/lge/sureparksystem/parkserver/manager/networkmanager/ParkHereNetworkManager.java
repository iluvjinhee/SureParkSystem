package com.lge.sureparksystem.parkserver.manager.networkmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkHereNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.util.Logger;

public class ParkHereNetworkManager extends NetworkManager {
	public class ParkHereNetworkManagerListener {
		@Subscribe
		public void onSubscribe(ParkHereNetworkManagerTopic topic) {
			System.out.println("ParkHereNetworkManagerListener: " + topic);
			
			processMessage(topic.getJsonObject());
		}
	}
	
	@Override
	public void init() {
		getEventBus().register(new ParkHereNetworkManagerListener());
	}
	
	public ParkHereNetworkManager(int serverPort) {
		super(serverPort);
	}
	
	public void sendMessage(JSONObject jsonObject) {
		for(SocketForServer socketForServer : socketList) {
			socketForServer.send(jsonObject);
		}
	}
	
	@Override
	public void receiveMessage(JSONObject jsonObject) {
		processMessage(jsonObject);
	}
	
	public void run() {
		super.run();
	}
	
	protected void processMessage(JSONObject jsonObject) {
		super.processMessage(jsonObject);
		
		MessageType messageType = MessageParser.getMessageType(jsonObject);
		
		switch (messageType) {
		default:
			break;
		}
		
		return;
	}
}