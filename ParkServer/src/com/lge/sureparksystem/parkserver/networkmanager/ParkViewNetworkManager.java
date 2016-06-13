package com.lge.sureparksystem.parkserver.networkmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.topic.ParkViewNetworkManagerTopic;

public class ParkViewNetworkManager extends NetworkManager {
	public class ParkViewNetworkManagerListener {
		@Subscribe
		public void onSubscribe(ParkViewNetworkManagerTopic topic) {
			System.out.println("ParkViewNetworkManagerListener");
			System.out.println(topic);
		}
	}
	
	@Override
	public void init() {
		getEventBus().register(new ParkViewNetworkManagerListener());
	}
	
	public ParkViewNetworkManager(int serverPort) {
		super(serverPort);
	}
	
	public void send(JSONObject jsonObject) {
		for(SocketForServer socketForServer : socketList) {
			socketForServer.send(jsonObject);
		}
	}
	
	public void run() {
		super.run();
	}
}