package com.lge.sureparksystem.parkserver.manager.networkmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageValueType;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkingLotNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ReservationManagerTopic;

public class ParkingLotNetworkManager extends NetworkManager {
	public class ParkingLotNetworkManagerListener {
		@Subscribe
		public void onSubscribe(ParkingLotNetworkManagerTopic topic) {
			System.out.println(topic);
			
			setSessionID(topic);

			processMessage(topic.getJsonObject());
		}
	}
	
	public ParkingLotNetworkManager(int serverPort) {
		super(serverPort);
	}

	@Override
	public void init() {
		registerEventBus(new ParkingLotNetworkManagerListener());
	}
	
	@Override
	public void run() {
		super.run();
	}
	
//	@Override
//	public void receive(JSONObject jsonObject) {
//		processMessage(jsonObject);
//	}
	
	protected void processMessage(JSONObject jsonObject) {
		super.processMessage(jsonObject);
		
		MessageType messageType = MessageParser.getMessageType(jsonObject);
		DataMessage message = (DataMessage) MessageParser.convertToMessage(jsonObject);

		switch (messageType) {
		case ENTRY_GATE_ARRIVE:
		case ENTRY_GATE_PASSBY:
		case EXIT_GATE_ARRIVE:
		case EXIT_GATE_PASSBY:
		case SLOT_SENSOR_STATUS:
			post(new CommunicationManagerTopic(jsonObject), this);
			break;
		case PARKING_LOT_INFORMATION:
			post(new CommunicationManagerTopic(jsonObject), this);
			
//			message.setID(getID());
			post(new ReservationManagerTopic(message), this);
			break;
		case ENTRY_GATE_CONTROL:
		case EXIT_GATE_CONTROL:
		case ENTRY_GATE_LED_CONTROL:
		case EXIT_GATE_LED_CONTROL:
		case SLOT_LED_CONTROL:
		case RESET:
			send(jsonObject);
			break;
		case ENTRY_GATE_STATUS:
			if(message.getStatus().equalsIgnoreCase(MessageValueType.UP)) {
				DataMessage sendMessage = new DataMessage(MessageType.ENTRY_GATE_LED_CONTROL);
				sendMessage.setCommand(MessageValueType.GREEN);
				
				send(MessageParser.convertToJSONObject(sendMessage));				
			} else {
				if(message.getStatus().equalsIgnoreCase(MessageValueType.DOWN)) {
					DataMessage outMessage = new DataMessage(MessageType.ENTRY_GATE_LED_CONTROL);
					outMessage.setCommand(MessageValueType.RED);
					
					send(MessageParser.convertToJSONObject(outMessage));				
				}
			}
			break;
		case EXIT_GATE_STATUS:
			message = (DataMessage) MessageParser.convertToMessage(jsonObject);
			if(message.getStatus().equalsIgnoreCase(MessageValueType.UP)) {
				DataMessage outMessage = new DataMessage(MessageType.EXIT_GATE_LED_CONTROL);
				outMessage.setCommand(MessageValueType.GREEN);
				
				send(MessageParser.convertToJSONObject(outMessage));	
			} else if(message.getStatus().equalsIgnoreCase(MessageValueType.DOWN)) {
				DataMessage outMessage = new DataMessage(MessageType.EXIT_GATE_LED_CONTROL);
				outMessage.setCommand(MessageValueType.RED);
				
				send(MessageParser.convertToJSONObject(outMessage));	
			}
			break;
		default:
			break;
		}

		return;
	}
}