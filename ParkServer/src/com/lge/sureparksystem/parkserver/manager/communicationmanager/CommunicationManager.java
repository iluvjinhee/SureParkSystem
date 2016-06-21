package com.lge.sureparksystem.parkserver.manager.communicationmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkingLotNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ReservationManagerTopic;

public class CommunicationManager extends ManagerTask {
	public class CommunicationManagerListener {
		@Subscribe
		public void onSubscribe(CommunicationManagerTopic topic) {
			System.out.println("CommunicationManagerListener: " + topic);
			
			processMessage(topic.getJsonObject());
		}
	}
	
	@Override
	public void init() {
		getEventBus().register(new CommunicationManagerListener());
	}
	
	@Override
	public void run() {
		while(loop) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void processMessage(JSONObject jsonObject) {
		DataMessage message = null;
		
		switch (MessageParser.getMessageType(jsonObject)) {
		case ENTRY_GATE_ARRIVE:
//			message = new DataMessage(MessageType.QR_START);
//			getEventBus().post(new ParkViewNetworkManagerTopic(message));
			
			// For Test
			message = new DataMessage(MessageType.CONFIRMATION_RESPONSE);
			message.setResult("ok");
			message.setSlotNumber(3);
			getEventBus().post(new CommunicationManagerTopic(message));
			break;
		case ENTRY_GATE_PASSBY:
			getEventBus().post(new ReservationManagerTopic(jsonObject));
			
			message = new DataMessage(MessageType.ENTRY_GATE_LED_CONTROL);
			message.setCommand("red");
			getEventBus().post(new ParkingLotNetworkManagerTopic(message));
			
			message = new DataMessage(MessageType.ENTRY_GATE_CONTROL);
			message.setCommand("down");
			getEventBus().post(new ParkingLotNetworkManagerTopic(message));
			break;	
		case EXIT_GATE_ARRIVE:
			getEventBus().post(new ReservationManagerTopic(jsonObject));
			
			message = new DataMessage(MessageType.EXIT_GATE_CONTROL);
			message.setCommand("up");
			getEventBus().post(new ParkingLotNetworkManagerTopic(message));
			break;		
		case EXIT_GATE_PASSBY:
			message = new DataMessage(MessageType.EXIT_GATE_LED_CONTROL);
			message.setCommand("red");
			getEventBus().post(new ParkingLotNetworkManagerTopic(message));
			
			message = new DataMessage(MessageType.EXIT_GATE_CONTROL);
			message.setCommand("down");
			jsonObject = MessageParser.convertToJSONObject(message);			
			getEventBus().post(new ParkingLotNetworkManagerTopic(jsonObject));
			break;
		case CONFIRMATION_RESPONSE:
			message = (DataMessage) MessageParser.convertToMessage(jsonObject);
			if(message.getResult().equalsIgnoreCase("OK")) {
				DataMessage sendMessage = new DataMessage(MessageType.ENTRY_GATE_CONTROL);
				sendMessage.setCommand("up");
				getEventBus().post(new ParkingLotNetworkManagerTopic(sendMessage));
				
				sendMessage = new DataMessage(MessageType.SLOT_LED_CONTROL);
				sendMessage.setSlotNumber(message.getSlotNumber());
				sendMessage.setCommand("on");
				getEventBus().post(new ParkingLotNetworkManagerTopic(sendMessage));
			}
			getEventBus().post(new ParkingLotNetworkManagerTopic(jsonObject));
			break;
		case CONFIRMATION_SEND:
		case SLOT_SENSOR_STATUS:
			getEventBus().post(new ReservationManagerTopic(jsonObject));
			break;
		default:
			break;
		}
	}	
}
