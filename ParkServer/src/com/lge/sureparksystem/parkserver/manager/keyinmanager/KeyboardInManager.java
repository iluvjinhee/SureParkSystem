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
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.OpenEntryGate)) {
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.CloseEntryGate)) {
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.OpenExitGate)) {
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.CloseExitGate)) {
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnOnEntryGateLED)) {
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnOffEntryGateLED)) {
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnOnExitGateLED)) {
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnOffExitGateLED)) {
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnOnSlotLED)) {
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.TurnOffSlotLED)) {
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.OpenEntryGate)) {
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.OpenEntryGate)) {
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.RequestParkingLotInfo)) {
		} else if (containsCaseInsensitive(typedMessage, KeyInCorpus.ResponseAuthetication)) {
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
