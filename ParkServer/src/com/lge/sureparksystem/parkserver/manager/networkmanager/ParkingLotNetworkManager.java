package com.lge.sureparksystem.parkserver.manager.networkmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkingLotNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ReservationManagerTopic;

public class ParkingLotNetworkManager extends NetworkManager {
	public class ParkingLotNetworkManagerListener {
		@Subscribe
		public void onSubscribe(ParkingLotNetworkManagerTopic topic) {
			System.out.println("ParkingLotNetworkManagerListener: " + topic);

			processMessage(topic.getJsonObject());
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
	
	@Override
	public void receiveMessage(JSONObject jsonObject) {
		processMessage(jsonObject);
	}
	
	protected void processMessage(JSONObject jsonObject) {
		super.processMessage(jsonObject);
		
		MessageType messageType = MessageParser.getMessageType(jsonObject);

		switch (messageType) {
		case ENTRY_GATE_ARRIVE:
		case ENTRY_GATE_PASSBY:
		case EXIT_GATE_ARRIVE:
		case EXIT_GATE_PASSBY:
		case SLOT_SENSOR_STATUS:
			getEventBus().post(new CommunicationManagerTopic(jsonObject));
		case PARKING_LOT_INFORMATION:
			getEventBus().post(new ReservationManagerTopic(jsonObject));
			break;
		case ENTRY_GATE_CONTROL:
		case EXIT_GATE_CONTROL:
		case ENTRY_GATE_LED_CONTROL:
		case EXIT_GATE_LED_CONTROL:
		case SLOT_LED_CONTROL:
			sendMessage(jsonObject);
		default:
			break;
		}

		return;
	}
}