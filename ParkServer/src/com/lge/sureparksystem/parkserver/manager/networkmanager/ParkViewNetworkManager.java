package com.lge.sureparksystem.parkserver.manager.networkmanager;

import java.net.Socket;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.message.Message;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.ParkViewNetworkManagerTopic;

public class ParkViewNetworkManager extends NetworkManager {
	public class ParkViewNetworkManagerListener {
		@Subscribe
		public void onSubscribe(ParkViewNetworkManagerTopic topic) {
			System.out.println("ParkViewNetworkManagerListener: " + topic);
			
			send(MessageParser.makeJSONObject(topic.getMessage()));
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
}