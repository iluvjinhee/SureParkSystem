package com.lge.sureparksystem.parkserver.manager.keyinmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
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
		
		if(Arrays.asList(KeyInCorpus.OpenEntryGate).contains(typedMessage)) {
		}
		else if(Arrays.asList(KeyInCorpus.CloseEntryGate).contains(typedMessage)) {
		}
		else if(Arrays.asList(KeyInCorpus.OpenExitGate).contains(typedMessage)) {
		}
		else if(Arrays.asList(KeyInCorpus.CloseExitGate).contains(typedMessage)) {
		}
		
		return jsonObject;
	}

	private JSONObject processTypedParkViewMessage(String typedMessage) {
		JSONObject jsonObject = null;
		
		if(Arrays.asList(KeyInCorpus.ScanReservationCode).contains(typedMessage)) {
			jsonObject = MessageParser.makeJSONObject(new Message(MessageType.SCAN_CONFIRM));
		}
		else if(Arrays.asList(KeyInCorpus.WelcomeSurePark).contains(typedMessage)) {
			jsonObject = MessageParser.makeJSONObject(new Message(MessageType.WELCOME_SUREPARK));
		}
		else if(Arrays.asList(KeyInCorpus.AssignedSlot).contains(typedMessage)) {
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
}
