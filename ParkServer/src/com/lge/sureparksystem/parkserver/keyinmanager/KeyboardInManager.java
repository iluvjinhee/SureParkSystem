package com.lge.sureparksystem.parkserver.keyinmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.SocketMessageParser;
import com.lge.sureparksystem.parkserver.message.SocketMessageType;
import com.lge.sureparksystem.parkserver.message.SocketMessage;
import com.lge.sureparksystem.parkserver.module.ManagerTask;
import com.lge.sureparksystem.parkserver.networkmanager.SocketType;
import com.lge.sureparksystem.parkserver.topic.ParkViewNetworkManagerTopic;

public class KeyboardInManager extends ManagerTask {
	private BufferedReader keyIn = null;
	
	public KeyboardInManager() {
		super();
		
		keyIn = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public int getAvailableSlot() {
		Random r = new Random();

		return r.nextInt(getSlotSize()) + 1;
	}
	
	private int getSlotSize() {
		return 4;
	}
	
	@Override
	public void run() {
    	while(true) {
			String keyMsg = null;
			try {
				keyMsg = keyIn.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(keyMsg != null) {
				switch(KeyboardInType.fromValue(Integer.parseInt(keyMsg))) {
				case KEYBOARD_1:
					getEventBus().post(
							new ParkViewNetworkManagerTopic(
									SocketMessageParser.makeJSONObject(new SocketMessage(SocketMessageType.WELCOME_SUREPARK))));
					break;
				case KEYBOARD_2:
					getEventBus().post(
							new ParkViewNetworkManagerTopic(
									SocketMessageParser.makeJSONObject(new SocketMessage(SocketMessageType.SCAN_CONFIRM))));
					break;
				case KEYBOARD_3:
					getEventBus().post(
							new ParkViewNetworkManagerTopic(
									SocketMessageParser.makeJSONObject(new SocketMessage(SocketMessageType.ASSIGN_SLOT, String.valueOf(getAvailableSlot())))));
					break;
				default:
					break;
				}
			}
		}
	}
	
	public JSONObject mapMessage(int messageIndex, String data) {
		JSONObject jsonObject = null;

		switch (messageIndex) {
		case 1:
			jsonObject = SocketMessageParser.makeJSONObject(new SocketMessage(SocketMessageType.WELCOME_SUREPARK));
			break;
		case 2:
			jsonObject = SocketMessageParser.makeJSONObject(new SocketMessage(SocketMessageType.SCAN_CONFIRM));
			break;
		case 3:
			jsonObject = SocketMessageParser.makeJSONObject(new SocketMessage(SocketMessageType.ASSIGN_SLOT, data));
			break;
		default:
			jsonObject = SocketMessageParser.makeJSONObject(new SocketMessage(SocketMessageType.WELCOME_SUREPARK));
			break;
		}

		return jsonObject;
	}
}
