package com.lge.sureparksystem.parkserver.message;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MessageParser {
	public static Message makeMessage(String jsonMessage) {
		Message message = new Message();
		
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
			message.setMessageType(MessageType.fromText(str));
		}
		if(jsonObject.get(Message.GLOBAL_VALUE) != null) {
			message.setGlobalValue((String) jsonObject.get(Message.GLOBAL_VALUE));
		}
		
		return message;
	}
	
	public static Message makeMessage(JSONObject jsonObject) {
		Message message = new Message();
		
		if(jsonObject.get(Message.MESSAGE_TYPE) != null) {
			String str = (String) jsonObject.get(Message.MESSAGE_TYPE);			
			message.setMessageType(MessageType.fromText(str));
		}
		if(jsonObject.get(Message.GLOBAL_VALUE) != null) {
			message.setGlobalValue((String) jsonObject.get(Message.GLOBAL_VALUE));
		}
		
		return message;
	}
	
	public static MessageType getMessageType(JSONObject jsonObject) {
		MessageType messageType = MessageType.NONE;
		
		if(jsonObject.get(Message.MESSAGE_TYPE) != null) {
			String str = (String) jsonObject.get(Message.MESSAGE_TYPE);			
			messageType = MessageType.fromText(str);
		}
		
		return messageType;
	}
	
	public static MessageType getMessageType(String jsonString) {
		Message message = makeMessage(jsonString);
		
		return message.getMessageType();
	}
	
	public static JSONObject makeJSONObject(Message message) {
		JSONObject jsonObject = new JSONObject();
		
		switch(message.getMessageType()) {
		case WELCOME_SUREPARK:
		case SCAN_CONFIRM:
		case NOT_RESERVED:
			jsonObject.put(Message.MESSAGE_TYPE, message.getMessageType().getText());
			break;
		case RESERVATION_NUMBER:
		case ASSIGN_SLOT:
			jsonObject.put(Message.MESSAGE_TYPE, message.getMessageType().getText());
			jsonObject.put(Message.GLOBAL_VALUE, message.getGlobalValue());
			break;
		default:
			break;
		}
			
		return jsonObject;
	}
	
	public static JSONObject makeJSONObject(TimestampMessage timestampMessage) {
		JSONObject jsonObject = new JSONObject();
		
		switch(timestampMessage.getMessageType()) {
		case ACKNOWLEDGE:
			jsonObject.put(TimestampMessage.MESSAGE_TYPE, timestampMessage.getMessageType().getText());
			jsonObject.put(TimestampMessage.TIMESTAMP, timestampMessage.getTimestamp());
			break;
		default:
			break;
		}
			
		return jsonObject;
	}
}
