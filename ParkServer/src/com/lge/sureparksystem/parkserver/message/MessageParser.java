package com.lge.sureparksystem.parkserver.message;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MessageParser {
	public static Message parseJSONMessage(String jsonMessage) {
		Message socketMessage = new Message();
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) jsonParser.parse(jsonMessage);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(jsonObject.get(Message.MESSAGE_TYPE) != null) {
			String str = (String) jsonObject.get(Message.MESSAGE_TYPE);			
			socketMessage.setMessageType(MessageType.fromText(str));
		}
		if(jsonObject.get(Message.GLOBAL_VALUE) != null) {
			socketMessage.setGlobalValue((String) jsonObject.get(Message.GLOBAL_VALUE));
		}
		
		return socketMessage;
	}
	
	public static Message parseJSONObject(JSONObject jsonObject) {
		Message socketMessage = new Message();
		
		JSONParser jsonParser = new JSONParser();
		
		if(jsonObject.get(Message.MESSAGE_TYPE) != null) {
			String str = (String) jsonObject.get(Message.MESSAGE_TYPE);			
			socketMessage.setMessageType(MessageType.fromText(str));
		}
		if(jsonObject.get(Message.GLOBAL_VALUE) != null) {
			socketMessage.setGlobalValue((String) jsonObject.get(Message.GLOBAL_VALUE));
		}
		
		return socketMessage;
	}
	
	public static JSONObject makeJSONObject(Message socketMessage) {
		JSONObject jsonObject = new JSONObject();
		
		switch(socketMessage.getMessageType()) {
		case WELCOME_SUREPARK:
		case SCAN_CONFIRM:
		case NOT_RESERVED:
			jsonObject.put(Message.MESSAGE_TYPE, socketMessage.getMessageType().getText());
			break;
		case RESERVATION_NUMBER:
		case ASSIGN_SLOT:
			jsonObject.put(Message.MESSAGE_TYPE, socketMessage.getMessageType().getText());
			jsonObject.put(Message.GLOBAL_VALUE, socketMessage.getGlobalValue());
			break;
		default:
			break;
		}
			
		return jsonObject;
	}
}
