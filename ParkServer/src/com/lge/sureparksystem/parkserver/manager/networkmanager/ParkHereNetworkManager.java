package com.lge.sureparksystem.parkserver.manager.networkmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkHereNetworkManagerTopic;

public class ParkHereNetworkManager extends NetworkManager {
	public class ParkHereNetworkManagerListener {
		@Subscribe
		public void onSubscribe(ParkHereNetworkManagerTopic topic) {
			System.out.println("ParkHereNetworkManagerListener: " + topic);
			
			process(topic.getJsonObject());
		}
	}
	
	@Override
	public void init() {
		getEventBus().register(new ParkHereNetworkManagerListener());
	}
	
	public ParkHereNetworkManager(int serverPort) {
		super(serverPort);
	}
	
	public void send(JSONObject jsonObject) {
		for(SocketForServer socketForServer : socketList) {
			socketForServer.send(jsonObject);
		}
	}
	
	@Override
	public void receive(JSONObject jsonObject) {
		process(jsonObject);
	}
	
	public void run() {
		super.run();
	}
	
	protected void process(JSONObject jsonObject) {
		super.process(jsonObject);
		
		MessageType messageType = MessageParser.getMessageType(jsonObject);
		
		switch (messageType) {
		default:
			break;
		}
		
		return;
	}
}