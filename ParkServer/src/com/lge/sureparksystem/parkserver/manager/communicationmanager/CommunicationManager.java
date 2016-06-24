package com.lge.sureparksystem.parkserver.manager.communicationmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.message.MessageValueType;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkHereNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkViewNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkingLotNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ReservationManagerTopic;

public class CommunicationManager extends ManagerTask {
	public class CommunicationManagerListener {
		@Subscribe
		public void onSubscribe(CommunicationManagerTopic topic) {
			System.out.println("CommunicationManagerListener: " + topic);
			
			setSessionID(topic);
			
			processMessage(topic.getJsonObject());
		}
	}
	
	@Override
	public void init() {
		registerEventBus(new CommunicationManagerListener());
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
			message = new DataMessage(MessageType.QR_START);
			post(new ParkViewNetworkManagerTopic(message), this);
			
			/*// For Test
			message = new DataMessage(MessageType.CONFIRMATION_RESPONSE);
			message.setResult("ok");
			message.setSlotNumber(3);
			post(new CommunicationManagerTopic(message), this);*/
			break;
		case ENTRY_GATE_PASSBY:
			post(new ReservationManagerTopic(jsonObject), this);
			
			controlEntryGate(MessageValueType.DOWN);
			controlParkView(MessageType.WELCOME_DISPLAY);
			break;	
		case EXIT_GATE_ARRIVE:
			post(new ReservationManagerTopic(jsonObject), this);
			
//			controlExitGate("up");
			break;		
		case EXIT_GATE_PASSBY:
			controlExitGate("down");
			break;
		case CONFIRMATION_RESPONSE:
			message = (DataMessage) MessageParser.convertToMessage(jsonObject);
			if(message.getResult().equalsIgnoreCase(MessageValueType.OK)) {
				controlEntryGate(MessageValueType.UP);
				turnSlotLED(message.getSlotNumber(), MessageValueType.ON);
			}
			else {
				callAttendant(MessageValueType.CONFIRMATION_INFORMATION_ERROR);
			}
			post(new ParkViewNetworkManagerTopic(jsonObject), this);			
			break;
		case CONFIRMATION_SEND:
			post(new ReservationManagerTopic(jsonObject), this);
			break;
		case SLOT_SENSOR_STATUS:
			message = (DataMessage) MessageParser.convertToMessage(jsonObject);
			if(message.getStatus().equalsIgnoreCase(MessageValueType.OCCUPIED)) {
				turnSlotLED(0, MessageValueType.OFF);
			}
			post(new ReservationManagerTopic(jsonObject), this);
			break;
		default:
			break;
		}
	}

	private void controlParkView(MessageType messageType) {
		DataMessage message = new DataMessage(messageType);
		
		post(new ParkViewNetworkManagerTopic(message), this);
	}

	private void callAttendant(String string) {
		DataMessage message = new DataMessage(MessageType.NOTIFICATION);
		message.setType(string);
		
		post(new ParkHereNetworkManagerTopic(message), this);		
	}

	private void controlEntryGate(String command) {
		DataMessage sendMessage = new DataMessage(MessageType.ENTRY_GATE_CONTROL);
		sendMessage.setCommand(command);
		post(new ParkingLotNetworkManagerTopic(sendMessage), this);
	}
	
	private void controlExitGate(String command) {
		DataMessage sendMessage = new DataMessage(MessageType.EXIT_GATE_CONTROL);
		sendMessage.setCommand(command);
		post(new ParkingLotNetworkManagerTopic(sendMessage), this);
	}

	private void turnSlotLED(int slotNumber, String command) {
		DataMessage sendMessage = new DataMessage(MessageType.SLOT_LED_CONTROL);
		sendMessage.setSlotNumber(slotNumber);
		sendMessage.setCommand(command);
		
		post(new ParkingLotNetworkManagerTopic(sendMessage), this);
	}	
}
