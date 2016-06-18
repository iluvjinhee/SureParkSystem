package com.lge.sureparksystem.parkserver.manager.keyinmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.Message;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.ManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkViewNetworkManagerTopic;

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
		
		return jsonObject;
	}

	private JSONObject processTypedParkingLotMessage(String typedMessage) {
		JSONObject jsonObject = null;
		
		if (containsCaseInsensitive(typedMessage, KeyInCorpus.OpenEntryGate)) {
			DataMessage dataMessage = new DataMessage(MessageType.ENTRY_GATE_CONTROL);
			dataMessage.setCommand("1");
			jsonObject = MessageParser.makeJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.CloseEntryGate)) {
			DataMessage dataMessage = new DataMessage(MessageType.ENTRY_GATE_CONTROL);
			dataMessage.setCommand("0");
			jsonObject = MessageParser.makeJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.OpenExitGate)) {
			DataMessage dataMessage = new DataMessage(MessageType.EXIT_GATE_CONTROL);
			dataMessage.setCommand("up");
			jsonObject = MessageParser.makeJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.CloseExitGate)) {
			DataMessage dataMessage = new DataMessage(MessageType.EXIT_GATE_CONTROL);
			dataMessage.setCommand("down");
			jsonObject = MessageParser.makeJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnOnEntryGateLED)) {
			DataMessage dataMessage = new DataMessage(MessageType.ENTRY_GATE_LED_CONTROL);
			dataMessage.setCommand("up");
			jsonObject = MessageParser.makeJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnOffEntryGateLED)) {
			DataMessage dataMessage = new DataMessage(MessageType.ENTRY_GATE_LED_CONTROL);
			dataMessage.setCommand("down");
			jsonObject = MessageParser.makeJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnRedExitGateLED)) {
			DataMessage dataMessage = new DataMessage(MessageType.EXIT_GATE_LED_CONTROL);
			dataMessage.setCommand("red");
			jsonObject = MessageParser.makeJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnGreenExitGateLED)) {
			DataMessage dataMessage = new DataMessage(MessageType.EXIT_GATE_LED_CONTROL);
			dataMessage.setCommand("green");
			jsonObject = MessageParser.makeJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnOnSlotLED)) {
			DataMessage dataMessage = new DataMessage(MessageType.SLOT_LED_STATUS);
			dataMessage.setSensorNumber(Integer.parseInt(typedMessage));
			dataMessage.setCommand("on");			
			jsonObject = MessageParser.makeJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnOffSlotLED)) {
			DataMessage dataMessage = new DataMessage(MessageType.SLOT_LED_STATUS);
			dataMessage.setSensorNumber(Integer.parseInt(typedMessage));
			dataMessage.setCommand("off");
			jsonObject = MessageParser.makeJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.SuccessAuthetication)) {
			DataMessage dataMessage = new DataMessage(MessageType.AUTHENTICATION_RESPONSE);
			dataMessage.setResult("ok");
			jsonObject = MessageParser.makeJSONObject(dataMessage);
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.FailAuthetication)) {
			DataMessage dataMessage = new DataMessage(MessageType.AUTHENTICATION_RESPONSE);
			dataMessage.setResult("nok");
			jsonObject = MessageParser.makeJSONObject(dataMessage);
		}
		
		return jsonObject;
	}

	private JSONObject processTypedParkViewMessage(String typedMessage) {
		JSONObject jsonObject = null;
		
		if(containsCaseInsensitive(typedMessage, KeyInCorpus.ScanReservationCode)) {
			jsonObject = MessageParser.makeJSONObject(new Message(MessageType.SCAN_CONFIRM));
		}
		else if(containsCaseInsensitive(typedMessage, KeyInCorpus.WelcomeSurePark)) {
			jsonObject = MessageParser.makeJSONObject(new Message(MessageType.WELCOME_SUREPARK));
		}
		else if(containsCaseInsensitive(typedMessage, KeyInCorpus.AssignedSlot)) {
			DataMessage dataMessage = new DataMessage(MessageType.ASSIGNED_SLOT);
			dataMessage.setAssignedSlot(getAvailableSlot());
			jsonObject = MessageParser.makeJSONObject(dataMessage);
		}
		
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
