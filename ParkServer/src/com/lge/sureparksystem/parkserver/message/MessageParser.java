package com.lge.sureparksystem.parkserver.message;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
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
			e.printStackTrace();
		}

		return makeMessage(jsonObject);
	}

	public static Message makeMessage(JSONObject jsonObject) {
		Message message = new DataMessage();
		
		message.setMessageType(MessageType.fromText(MessageParser.getString(jsonObject, Message.MESSAGE_TYPE)));
		message.setTimestamp(MessageParser.getInt(jsonObject, Message.TIMESTAMP));
		((DataMessage) message).setReservationCode(MessageParser.getString(jsonObject, DataMessage.RESERVATION_CODE));
		((DataMessage) message).setAssignedSlot(MessageParser.getString(jsonObject, DataMessage.ASSIGNED_SLOT));
		((DataMessage) message).setEntrygateArrive(MessageParser.getString(jsonObject, DataMessage.ENTRYGATE_ARRIVE));
		((DataMessage) message).setEntryGateStatus(MessageParser.getString(jsonObject, DataMessage.ENTRYGATE_STATUS));
		((DataMessage) message).setEntryGateLEDStatus(MessageParser.getString(jsonObject, DataMessage.ENTRYGATELED_STATUS));
		((DataMessage) message).setExitgateArrive(MessageParser.getString(jsonObject, DataMessage.EXITGATE_ARRIVE));
		((DataMessage) message).setExitGateStatus(MessageParser.getString(jsonObject, DataMessage.EXITGATE_STATUS));
		((DataMessage) message).setExitGateLEDStatus(MessageParser.getString(jsonObject, DataMessage.EXITGATELED_STATUS));
		((DataMessage) message).setId(MessageParser.getString(jsonObject, DataMessage.ID));
		((DataMessage) message).setPwd(MessageParser.getString(jsonObject, DataMessage.PASSWORD));
		((DataMessage) message).setStatus(MessageParser.getString(jsonObject, DataMessage.STATUS));
		
		((DataMessage) message).setLedNumber(MessageParser.getInt(jsonObject, DataMessage.LED_NUMBER));
		((DataMessage) message).setSensorNumber(MessageParser.getInt(jsonObject, DataMessage.SENSOR_NUMBER));
		((DataMessage) message).setSlotNumber(MessageParser.getInt(jsonObject, DataMessage.SLOT_NUMBER));
		
		((DataMessage) message).setLedStatus(MessageParser.getStringList(jsonObject, DataMessage.LED_STATUS));
		((DataMessage) message).setSlotStatus(MessageParser.getStringList(jsonObject, DataMessage.SLOT_STATUS));

		return message;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject makeJSONObject(Message message) {
		JSONObject jsonObject = new JSONObject();

		switch (message.getMessageType()) {
		case WELCOME_SUREPARK:
		case SCAN_CONFIRM:
		case NOT_RESERVED:
		case ACKNOWLEDGE:
		case ENTRYGATE_ARRIVE:
		case ENTRYGATE_PASSBY:
		case EXITGATE_ARRIVE:
		case EXITGATE_PASSBY:
		case HEARTBEAT:
			jsonObject.put(Message.MESSAGE_TYPE, message.getMessageType().getText());
			if (message.getTimestamp() != -1)
				jsonObject.put(Message.TIMESTAMP, message.getTimestamp());
			break;
		case RESERVATION_CODE:
			jsonObject.put(Message.MESSAGE_TYPE, message.getMessageType().getText());
			if (message.getTimestamp() != -1)
				jsonObject.put(Message.TIMESTAMP, message.getTimestamp());
			jsonObject.put(DataMessage.RESERVATION_CODE, ((DataMessage) message).getReservationCode());
			break;
		case ASSIGNED_SLOT:
			jsonObject.put(Message.MESSAGE_TYPE, message.getMessageType().getText());
			if (message.getTimestamp() != -1)
				jsonObject.put(Message.TIMESTAMP, message.getTimestamp());
			jsonObject.put(DataMessage.ASSIGNED_SLOT, ((DataMessage) message).getAssignedSlot());
			break;
		case AUTHENTICATION_REQUEST:
			jsonObject.put(Message.MESSAGE_TYPE, message.getMessageType().getText());
			if (message.getTimestamp() != -1)
				jsonObject.put(Message.TIMESTAMP, message.getTimestamp());
			jsonObject.put(DataMessage.ID, ((DataMessage) message).getId());
			jsonObject.put(DataMessage.PASSWORD, ((DataMessage) message).getPwd());
			break;
		case ENTRY_GATE_LED_STATUS:
		case ENTRY_GATE_STATUS:
		case EXIT_GATE_LED_STATUS:
		case EXIT_GATE_STATUS:
			jsonObject.put(Message.MESSAGE_TYPE, message.getMessageType().getText());
			if (message.getTimestamp() != -1)
				jsonObject.put(Message.TIMESTAMP, message.getTimestamp());
			jsonObject.put(DataMessage.STATUS, ((DataMessage) message).getStatus());
			break;
		case ENTRY_GATE_LED_CONTROL:
		case ENTRY_GATE_CONTROL:
		case EXIT_GATE_CONTROL:
		case EXIT_GATE_LED_CONTROL:
			jsonObject.put(Message.MESSAGE_TYPE, message.getMessageType().getText());
			if (message.getTimestamp() != -1)
				jsonObject.put(Message.TIMESTAMP, message.getTimestamp());
			jsonObject.put(DataMessage.COMMAND, ((DataMessage) message).getCommand());
			break;
		case SLOT_LED_CONTROL:
			jsonObject.put(Message.MESSAGE_TYPE, message.getMessageType().getText());
			if (message.getTimestamp() != -1)
				jsonObject.put(Message.TIMESTAMP, message.getTimestamp());
			jsonObject.put(DataMessage.STATUS, ((DataMessage) message).getStatus());
			break;
		case PARKINGLOT_INFORMATION:
			jsonObject.put(Message.MESSAGE_TYPE, message.getMessageType().getText());
			if (message.getTimestamp() != -1)
				jsonObject.put(Message.TIMESTAMP, message.getTimestamp());
			jsonObject.put(DataMessage.SLOT_COUNT, ((DataMessage) message).getSlotNumber());
			
			JSONArray array = new JSONArray();
			array.addAll(((DataMessage) message).getSlotStatus());
			jsonObject.put(DataMessage.SLOT_STATUS, array);
			
			array = new JSONArray();
			array.addAll(((DataMessage) message).getLedStatus());
			jsonObject.put(DataMessage.LED_STATUS, array);
			
			jsonObject.put(DataMessage.ENTRYGATE_STATUS, ((DataMessage) message).getEntrygateStatus());
			jsonObject.put(DataMessage.EXITGATE_STATUS, ((DataMessage) message).getExitgateStatus());
			jsonObject.put(DataMessage.ENTRYGATELED_STATUS, ((DataMessage) message).getEntrygateledStatus());
			jsonObject.put(DataMessage.EXITGATELED_STATUS, ((DataMessage) message).getExitgateledStatus());
			jsonObject.put(DataMessage.ENTRYGATE_ARRIVE, ((DataMessage) message).getEntrygateArrive());
			jsonObject.put(DataMessage.EXITGATE_ARRIVE, ((DataMessage) message).getExitgateArrive());
			break;
		case SLOT_LED_STATUS:
		case SLOT_SENSOR_STATUS:
			jsonObject.put(Message.MESSAGE_TYPE, message.getMessageType().getText());
			if (message.getTimestamp() != -1)
				jsonObject.put(Message.TIMESTAMP, message.getTimestamp());
			jsonObject.put(DataMessage.SLOT_NUMBER, ((DataMessage) message).getSlotNumber());
			jsonObject.put(DataMessage.STATUS, ((DataMessage) message).getStatus());
			break;
		default:
			break;
		}

		return jsonObject;
	}

	public static MessageType getMessageType(JSONObject jsonObject) {
		MessageType messageType = MessageType.NONE;

		if (jsonObject.get(Message.MESSAGE_TYPE.toUpperCase()) != null) {
			String str = (String) jsonObject.get(Message.MESSAGE_TYPE.toUpperCase());
			messageType = MessageType.fromText(str);
		}

		return messageType;
	}

	public static MessageType getMessageType(String jsonString) {
		Message message = makeMessage(jsonString);

		return message.getMessageType();
	}

	public static int getTimestamp(JSONObject jsonObject) {
		int timestamp = -1;

		if (jsonObject.get(Message.TIMESTAMP.toUpperCase()) != null) {
			timestamp = ((Long) jsonObject.get(Message.TIMESTAMP.toUpperCase())).intValue();
		}

		return timestamp;
	}

	public static int getTimestamp(String jsonString) {
		Message message = makeMessage(jsonString);

		return message.getTimestamp();
	}
	
	public static String getString(JSONObject jsonObject, String key) {
		String value = null;

		if (jsonObject.get(key.toUpperCase()) != null) {
			value = (String) jsonObject.get(key.toUpperCase());
		}

		return value;
	}
	
	public static int getInt(JSONObject jsonObject, String key) {
		int value = -1;

		if (jsonObject.get(key.toUpperCase()) != null) {
			value = ((Long) jsonObject.get(key.toUpperCase())).intValue();
		}

		return value;
	}
	
	public static ArrayList<String> getStringList(JSONObject jsonObject, String key) {
		ArrayList<String> resultList = new ArrayList<String>();
		
		JSONArray childrenList = (JSONArray) jsonObject.get(key.toUpperCase());
		
		if(childrenList != null) {
	        Iterator<String> i = childrenList.iterator();
	
	        while (i.hasNext()) {
	            resultList.add(i.next());
	        }
		}
        
		return resultList;
	}
}