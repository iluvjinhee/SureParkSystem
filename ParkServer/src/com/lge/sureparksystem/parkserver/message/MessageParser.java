package com.lge.sureparksystem.parkserver.message;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MessageParser {
	public static Message makeMessage(String jsonMessage) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) jsonParser.parse(jsonMessage);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return makeMessage(jsonObject);
	}
	
	public static Message makeMessage(JSONObject jsonObject) {
		Message message = new Message();
		
		if(jsonObject.get(Message.MESSAGE_TYPE) != null) {
			String str = (String) jsonObject.get(Message.MESSAGE_TYPE);			
			message.setMessageType(MessageType.fromText(str));
		}
		if(jsonObject.get(Message.TIMESTAMP) != null) {
			message.setTimestamp((int) jsonObject.get(Message.TIMESTAMP));
		}
		if(jsonObject.get(DataMessage.RESERVATION_CODE) != null) {
			((DataMessage) message).setReservationCode((String) jsonObject.get(DataMessage.RESERVATION_CODE));
		}
		if(jsonObject.get(DataMessage.ASSIGNED_SLOT) != null) {
			((DataMessage) message).setAssignedSlot((String) jsonObject.get(DataMessage.ASSIGNED_SLOT));
		}
		
		return message;
	}
	
	public static JSONObject makeJSONObject(Message message) {
		JSONObject jsonObject = new JSONObject();
		
		switch(message.getMessageType()) {
		case WELCOME_SUREPARK:
		case SCAN_CONFIRM:
		case NOT_RESERVED:
			jsonObject.put(Message.MESSAGE_TYPE, message.getMessageType().getText());
			jsonObject.put(Message.TIMESTAMP, message.getTimestamp());
			break;
		case RESERVATION_CODE:
			jsonObject.put(Message.MESSAGE_TYPE, message.getMessageType().getText());
			jsonObject.put(Message.TIMESTAMP, message.getTimestamp());
			jsonObject.put(DataMessage.RESERVATION_CODE, ((DataMessage) message).getReservationCode());
			break;
		case ASSIGNED_SLOT:
			jsonObject.put(Message.MESSAGE_TYPE, message.getMessageType().getText());
			jsonObject.put(Message.TIMESTAMP, message.getTimestamp());
			jsonObject.put(DataMessage.ASSIGNED_SLOT, ((DataMessage) message).getAssignedSlot());
			break;
		default:
			break;
		}
			
		return jsonObject;
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
}
