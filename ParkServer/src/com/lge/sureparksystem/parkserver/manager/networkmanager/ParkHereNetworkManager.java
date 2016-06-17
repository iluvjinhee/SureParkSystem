package com.lge.sureparksystem.parkserver.manager.networkmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.topic.ParkHereNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkViewNetworkManagerTopic;

public class ParkHereNetworkManager extends NetworkManager {
	public class ParkHereNetworkManagerListener {
		@Subscribe
		public void onSubscribe(ParkHereNetworkManagerTopic topic) {
			System.out.println("ParkHereNetworkManagerListener: " + topic);
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
	
	public void run() {
		super.run();
	}
}