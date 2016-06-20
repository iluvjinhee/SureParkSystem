package com.lge.sureparksystem.parkserver.manager.networkmanager;

import java.net.Socket;
import java.nio.channels.SocketChannel;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.ParkingLotNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.util.Logger;

public class ParkingLotNetworkManager extends NetworkManager {
	public class ParkingLotNetworkManagerListener {
		@Subscribe
		public void onSubscribe(ParkingLotNetworkManagerTopic topic) {
			System.out.println("ParkingLotNetworkManagerListener: " + topic);

			process(topic);
		}
	}
	
	public ParkingLotNetworkManager(int serverPort) {
		super(serverPort);
	}

	@Override
	public void init() {
		getEventBus().register(new ParkingLotNetworkManagerListener());
	}
	
	@Override
	public void run() {
		super.run();
	}
	
	protected void process(JSONObject jsonObject) {
		MessageType messageType = MessageParser.getMessageType(jsonObject);
		DataMessage dataMessage = null;

		switch (messageType) {
		case PARKING_LOT_INFORMATION:
			dataMessage = (DataMessage) MessageParser.convertToMessage(jsonObject);
			break;
		case AUTHENTICATION_RESPONSE:
		case ENTRY_GATE_CONTROL:
		case EXIT_GATE_CONTROL:
		case ENTRY_GATE_LED_CONTROL:
		case EXIT_GATE_LED_CONTROL:
		case SLOT_LED_CONTROL:
			send(jsonObject);
		case AUTHENTICATION_OK:
			break;
		case AUTHENTICATION_FAIL:
			Logger.log("Unauthorized Parking Lot!!! Connection close.");
			// socket.close();
			break;

		default:
			break;
		}

		return;
	}
	
	@Override
	public void receive(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		
	}
}