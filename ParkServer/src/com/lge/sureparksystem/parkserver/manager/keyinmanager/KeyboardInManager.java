package com.lge.sureparksystem.parkserver.manager.keyinmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.Message;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.ManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkViewNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkingLotNetworkManagerTopic;

public class KeyboardInManager extends ManagerTask {
	private BufferedReader keyIn = null;
	
	public KeyboardInManager() {
		super();
		
		keyIn = new BufferedReader(new InputStreamReader(System.in));
	}
	
	@Override
	public void run() {
    	while(true) {
			String typedMessage = null;
			try {
				typedMessage = keyIn.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			process(typedMessage);
		}
	}
	
	public JSONObject process(String typedMessage) {
		JSONObject jsonObject = null;

		jsonObject = processTypedParkViewMessage(typedMessage);
		if(jsonObject == null)
			jsonObject = processTypedParkingLotMessage(typedMessage);
		
		if(jsonObject == null) {
			System.out.println("CAN'T UNDERSTAND YOUR COMMAND !!!!");
		}
		
		return jsonObject;
	}

	private JSONObject processTypedParkingLotMessage(String typedMessage) {
		JSONObject jsonObject = null;
		
		if (containsCaseInsensitive(typedMessage, KeyInCorpus.OpenEntryGate)) {
			DataMessage dataMessage = new DataMessage(MessageType.ENTRY_GATE_CONTROL);
			dataMessage.setCommand("up");
			jsonObject = MessageParser.convertToJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.CloseEntryGate)) {
			DataMessage dataMessage = new DataMessage(MessageType.ENTRY_GATE_CONTROL);
			dataMessage.setCommand("down");
			jsonObject = MessageParser.convertToJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.OpenExitGate)) {
			DataMessage dataMessage = new DataMessage(MessageType.EXIT_GATE_CONTROL);
			dataMessage.setCommand("up");
			jsonObject = MessageParser.convertToJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.CloseExitGate)) {
			DataMessage dataMessage = new DataMessage(MessageType.EXIT_GATE_CONTROL);
			dataMessage.setCommand("down");
			jsonObject = MessageParser.convertToJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnGreenEntryGateLED)) {
			DataMessage dataMessage = new DataMessage(MessageType.ENTRY_GATE_LED_CONTROL);
			dataMessage.setCommand("green");
			jsonObject = MessageParser.convertToJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnRedEntryGateLED)) {
			DataMessage dataMessage = new DataMessage(MessageType.ENTRY_GATE_LED_CONTROL);
			dataMessage.setCommand("red");
			jsonObject = MessageParser.convertToJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnRedExitGateLED)) {
			DataMessage dataMessage = new DataMessage(MessageType.EXIT_GATE_LED_CONTROL);
			dataMessage.setCommand("red");
			jsonObject = MessageParser.convertToJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnGreenExitGateLED)) {
			DataMessage dataMessage = new DataMessage(MessageType.EXIT_GATE_LED_CONTROL);
			dataMessage.setCommand("green");
			jsonObject = MessageParser.convertToJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnOnSlotLED)) {
			DataMessage dataMessage = new DataMessage(MessageType.SLOT_LED_CONTROL);
			dataMessage.setSlotNumber(Integer.valueOf(typedMessage.replaceAll("[^0-9]", "")));
			dataMessage.setCommand("on");			
			jsonObject = MessageParser.convertToJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnOffSlotLED)) {
			DataMessage dataMessage = new DataMessage(MessageType.SLOT_LED_CONTROL);
			dataMessage.setSlotNumber(Integer.valueOf(typedMessage.replaceAll("[^0-9]", "")));
			dataMessage.setCommand("off");
			jsonObject = MessageParser.convertToJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.SuccessAuthetication)) {
			DataMessage dataMessage = new DataMessage(MessageType.AUTHENTICATION_RESPONSE);
			dataMessage.setResult("ok");
			jsonObject = MessageParser.convertToJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.FailAuthetication)) {
			DataMessage dataMessage = new DataMessage(MessageType.AUTHENTICATION_RESPONSE);
			dataMessage.setResult("nok");
			jsonObject = MessageParser.convertToJSONObject(dataMessage);
		}
		
		if(jsonObject != null)
			getEventBus().post(new ParkingLotNetworkManagerTopic(jsonObject));
		
		return jsonObject;
	}

	private JSONObject processTypedParkViewMessage(String typedMessage) {
		JSONObject jsonObject = null;
		
		if(containsCaseInsensitive(typedMessage, KeyInCorpus.ScanReservationCode)) {
			jsonObject = MessageParser.convertToJSONObject(new Message(MessageType.SCAN_CONFIRM));
		}
		else if(containsCaseInsensitive(typedMessage, KeyInCorpus.WelcomeSurePark)) {
			jsonObject = MessageParser.convertToJSONObject(new Message(MessageType.WELCOME_SUREPARK));
		}
		else if(containsCaseInsensitive(typedMessage, KeyInCorpus.AssignedSlot)) {
			DataMessage dataMessage = new DataMessage(MessageType.ASSIGN_SLOT);
			dataMessage.setAssignedSlot(getAvailableSlot());
			jsonObject = MessageParser.convertToJSONObject(dataMessage);
		}
		
		if(jsonObject != null)
			getEventBus().post(new ParkViewNetworkManagerTopic(jsonObject));
		
		return jsonObject;
	}

	@Override
	protected void process(ManagerTopic topic) {
		// TODO Auto-generated method stub
		
	}
	
	public String getAvailableSlot() {
		Random r = new Random();

		return String.valueOf(r.nextInt(getSlotSize()) + 1);
	}
	
	private int getSlotSize() {
		return 4;
	}
	
	public boolean containsCaseInsensitive(String s, String[] l) {
		for (String string : l) {
			if (string.equalsIgnoreCase(s)) {
				return true;
			}
		}
		
		return false;
	}
}
