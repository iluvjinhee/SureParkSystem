package com.lge.sureparksystem.parkserver.manager.keyinmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.manager.networkmanager.SocketType;
import com.lge.sureparksystem.parkserver.message.Message;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
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
							new ParkViewNetworkManagerTopic(new Message(MessageType.WELCOME_SUREPARK)));
					break;
				case KEYBOARD_2:
					getEventBus().post(
							new ParkViewNetworkManagerTopic(new Message(MessageType.SCAN_CONFIRM)));
					break;
				case KEYBOARD_3:
					getEventBus().post(
							new ParkViewNetworkManagerTopic(new Message(MessageType.ASSIGN_SLOT, "3")));
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
			jsonObject = MessageParser.makeJSONObject(new Message(MessageType.WELCOME_SUREPARK));
			break;
		case 2:
			jsonObject = MessageParser.makeJSONObject(new Message(MessageType.SCAN_CONFIRM));
			break;
		case 3:
			jsonObject = MessageParser.makeJSONObject(new Message(MessageType.ASSIGN_SLOT, data));
			break;
		default:
			jsonObject = MessageParser.makeJSONObject(new Message(MessageType.WELCOME_SUREPARK));
			break;
		}

		return jsonObject;
	}
}
