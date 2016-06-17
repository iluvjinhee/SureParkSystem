package com.lge.sureparksystem.parkserver.manager.networkmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.ParkingLotNetworkManagerTopic;

public class ParkingLotNetworkManager extends NetworkManager {
	public class ParkingLotNetworkManagerListener {
		@Subscribe
		public void onSubscribe(ParkingLotNetworkManagerTopic topic) {
			System.out.println("ParkingLotNetworkManagerListener: " + topic);
			
			process(topic);
		}
	}
	
	@Override
	public void init() {
		getEventBus().register(new ParkingLotNetworkManagerListener());
	}
	
	public ParkingLotNetworkManager(int serverPort) {
		super(serverPort);
	}
	
	public void send(JSONObject jsonObject) {
		for(SocketForServer socketForServer : socketList) {
			socketForServer.send(jsonObject);
		}
	}
	
	protected void process(JSONObject jsonObject) {
		MessageType messageType = MessageParser.getMessageType(jsonObject);
		
		switch(messageType) {
		case PARKINGLOT_INFORMATION:
			DataMessage dataMessage = (DataMessage) MessageParser.makeMessage(jsonObject);
		default:
			break;
		}
	}
	
	protected void process(ParkingLotNetworkManagerTopic topic) {
		super.process(topic);
		
		process(topic.getJsonObject());		
	}
}