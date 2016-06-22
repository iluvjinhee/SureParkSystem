package com.lge.sureparksystem.parkserver.manager.networkmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.message.DataMessage;
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
	public void receive(JSONObject jsonObject) {
		processMessage(jsonObject);
	}
	
	protected void processMessage(JSONObject jsonObject) {
		super.processMessage(jsonObject);
		
		MessageType messageType = MessageParser.getMessageType(jsonObject);
		DataMessage message = null;

		switch (messageType) {
		case ENTRY_GATE_ARRIVE:
		case ENTRY_GATE_PASSBY:
		case EXIT_GATE_ARRIVE:
		case EXIT_GATE_PASSBY:
		case SLOT_SENSOR_STATUS:
			getEventBus().post(new CommunicationManagerTopic(jsonObject));
			break;
		case PARKING_LOT_INFORMATION:
			getEventBus().post(new CommunicationManagerTopic(jsonObject));
			getEventBus().post(new ReservationManagerTopic(jsonObject));
			break;
		case ENTRY_GATE_CONTROL:
		case EXIT_GATE_CONTROL:
		case ENTRY_GATE_LED_CONTROL:
		case EXIT_GATE_LED_CONTROL:
		case SLOT_LED_CONTROL:
			send(jsonObject);
			break;
		case ENTRY_GATE_STATUS:
			message = (DataMessage) MessageParser.convertToMessage(jsonObject);
			if(message.getStatus().equalsIgnoreCase("UP")) {
				DataMessage outMessage = new DataMessage(MessageType.ENTRY_GATE_LED_CONTROL);
				outMessage.setCommand("green");
				
				send(MessageParser.convertToJSONObject(outMessage));				
			} else {
				if(message.getStatus().equalsIgnoreCase("DOWN")) {
					DataMessage outMessage = new DataMessage(MessageType.ENTRY_GATE_LED_CONTROL);
					outMessage.setCommand("red");
					
					send(MessageParser.convertToJSONObject(outMessage));				
				}
			}
			break;
		case EXIT_GATE_STATUS:
			message = (DataMessage) MessageParser.convertToMessage(jsonObject);
			if(message.getStatus().equalsIgnoreCase("UP")) {
				DataMessage outMessage = new DataMessage(MessageType.EXIT_GATE_LED_CONTROL);
				outMessage.setCommand("green");
				
				send(MessageParser.convertToJSONObject(outMessage));	
			} else if(message.getStatus().equalsIgnoreCase("DOWN")) {
				DataMessage outMessage = new DataMessage(MessageType.EXIT_GATE_LED_CONTROL);
				outMessage.setCommand("red");
				
				send(MessageParser.convertToJSONObject(outMessage));	
			}
			break;
		default:
			break;
		}

		return;
	}
}