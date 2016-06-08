package com.lge.sureparksystem.parkserver.message;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MessageParser {
	public static SocketMessage parseJSONMessage(String jsonMessage) {
		SocketMessage socketMessage = new SocketMessage();
		
		JSONParser jsonParser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) jsonParser.parse(jsonMessage);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(obj.get(SocketMessage.MESSAGE_TYPE) != null) {
			Long l = (Long) obj.get(SocketMessage.MESSAGE_TYPE);			
			socketMessage.setMessageType(MessageType.fromValue(l.intValue()));
		}
		if(obj.get(SocketMessage.GLOBAL_VALUE) != null) {
			socketMessage.setGlobalValue((String) obj.get(SocketMessage.GLOBAL_VALUE));
		}
		
		return socketMessage;
	}
	
	public static JSONObject makeJSONObject(SocketMessage socketMessage) {
		JSONObject jsonObject = new JSONObject();
		
		switch(socketMessage.getMessageType()) {
		case WELCOME_SUREPARK:
		case SCAN_CONFIRM:
			jsonObject.put(SocketMessage.MESSAGE_TYPE, socketMessage.getMessageType().getValue());
			break;
		case RESERVATION_NUMBER:
		case ASSIGN_SLOT:
			jsonObject.put(SocketMessage.MESSAGE_TYPE, socketMessage.getMessageType().getValue());
			jsonObject.put(SocketMessage.GLOBAL_VALUE, socketMessage.getGlobalValue());
			break;
		default:
			break;
		}
			
		return jsonObject;
	}
}
