package com.lge.sureparksystem.parkserver.manager.networkmanager;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.topic.ParkViewNetworkManagerTopic;

public class ParkViewNetworkManager extends NetworkManager {
	public class ParkViewNetworkManagerListener {
		@Subscribe
		public void onSubscribe(ParkViewNetworkManagerTopic topic) {
			System.out.println("ParkViewNetworkManagerListener");
			System.out.println(topic);
			
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
}