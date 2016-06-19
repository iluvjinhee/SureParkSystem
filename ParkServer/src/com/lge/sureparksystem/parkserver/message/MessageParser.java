package com.lge.sureparksystem.parkserver.message;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MessageParser {
	public static Message convertToMessage(String jsonMessage) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) jsonParser.parse(jsonMessage);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return convertToMessage(jsonObject);
	}

	public static Message convertToMessage(JSONObject jsonObject) {
		Message message = new DataMessage();
		
		message.setMessageType(MessageType.fromText(MessageParser.getString(jsonObject, Message.MESSAGE_TYPE)));
		message.setTimestamp(MessageParser.getInt(jsonObject, Message.TIMESTAMP));
		
		// Parking Lot
		((DataMessage) message).setAssignedSlot(MessageParser.getString(jsonObject, DataMessage.ASSIGNED_SLOT));
		((DataMessage) message).setEntryGateLEDStatus(MessageParser.getString(jsonObject, DataMessage.ENTRY_GATE_LED_STATUS));
		((DataMessage) message).setEntryGateStatus(MessageParser.getString(jsonObject, DataMessage.ENTRY_GATE_STATUS));
		((DataMessage) message).setEntrygateArrive(MessageParser.getString(jsonObject, DataMessage.ENTRY_GATE_ARRIVE));
		((DataMessage) message).setExitGateLEDStatus(MessageParser.getString(jsonObject, DataMessage.EXIT_GATE_LED_STATUS));
		((DataMessage) message).setExitGateStatus(MessageParser.getString(jsonObject, DataMessage.EXIT_GATE_STATUS));
		((DataMessage) message).setExitgateArrive(MessageParser.getString(jsonObject, DataMessage.EXIT_GATE_ARRIVE));
		((DataMessage) message).setId(MessageParser.getString(jsonObject, DataMessage.ID));
		((DataMessage) message).setLedNumber(MessageParser.getInt(jsonObject, DataMessage.LED_NUMBER));
		((DataMessage) message).setLedStatus(MessageParser.getStringList(jsonObject, DataMessage.LED_STATUS));
		((DataMessage) message).setPwd(MessageParser.getString(jsonObject, DataMessage.PASSWORD));
		((DataMessage) message).setReservationCode(MessageParser.getString(jsonObject, DataMessage.RESERVATION_CODE));
		((DataMessage) message).setSensorNumber(MessageParser.getInt(jsonObject, DataMessage.SENSOR_NUMBER));
		((DataMessage) message).setSlotNumber(MessageParser.getInt(jsonObject, DataMessage.SLOT_NUMBER));
		((DataMessage) message).setSlotStatus(MessageParser.getStringList(jsonObject, DataMessage.SLOT_STATUS));
		((DataMessage) message).setStatus(MessageParser.getString(jsonObject, DataMessage.STATUS));
		
		// ParkHere
		ArrayList<String> driver_often;
		ArrayList<String> grace_period;
		ArrayList<String> parking_fee;
		ArrayList<String> parking_lot_id;
		ArrayList<String> parking_lot_location;
		ArrayList<String> slot_driver_id;
		ArrayList<String> slot_time;
		String confirmation_info;
		String driver_id;
		String payment_info;
		String reservation_id;
		String reservation_time;
		String type;
		int parking_lot_count;
		int slot_count;

		((DataMessage) message).setLedStatus(MessageParser.getStringList(jsonObject, DataMessage.LED_STATUS));
		((DataMessage) message).setPwd(MessageParser.getString(jsonObject, DataMessage.PASSWORD));
		((DataMessage) message).setReservationCode(MessageParser.getString(jsonObject, DataMessage.RESERVATION_CODE));
		((DataMessage) message).setSensorNumber(MessageParser.getInt(jsonObject, DataMessage.SENSOR_NUMBER));
		
		return message;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject convertToJSONObject(Message message) {
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.put(Message.MESSAGE_TYPE, message.getMessageType().getText());
		if (message.getTimestamp() != -1)
			jsonObject.put(Message.TIMESTAMP, message.getTimestamp());

		switch (message.getMessageType()) {
		case RESERVATION_CODE:
			jsonObject.put(DataMessage.RESERVATION_CODE, ((DataMessage) message).getReservationCode());
			break;
		case ASSIGN_SLOT:
			jsonObject.put(DataMessage.ASSIGNED_SLOT, ((DataMessage) message).getAssignedSlot());
			break;
		case AUTHENTICATION_REQUEST:
			jsonObject.put(DataMessage.ID, ((DataMessage) message).getId());
			jsonObject.put(DataMessage.PASSWORD, ((DataMessage) message).getPwd());
			break;
		case AUTHENTICATION_RESPONSE:
			jsonObject.put(DataMessage.RESULT, ((DataMessage) message).getResult());
			break;
		case ENTRY_GATE_LED_STATUS:
		case ENTRY_GATE_STATUS:
		case EXIT_GATE_LED_STATUS:
		case EXIT_GATE_STATUS:
			jsonObject.put(DataMessage.STATUS, ((DataMessage) message).getStatus());
			break;
		case ENTRY_GATE_LED_CONTROL:
		case ENTRY_GATE_CONTROL:
		case EXIT_GATE_CONTROL:
		case EXIT_GATE_LED_CONTROL:
			jsonObject.put(DataMessage.COMMAND, ((DataMessage) message).getCommand());
			break;
		case SLOT_LED_CONTROL:
			jsonObject.put(DataMessage.SLOT_NUMBER, ((DataMessage) message).getSlotNumber());
			jsonObject.put(DataMessage.COMMAND, ((DataMessage) message).getCommand());
			break;
		case PARKING_LOT_INFORMATION:
			jsonObject.put(DataMessage.SLOT_COUNT, ((DataMessage) message).getSlotCount());
			
			JSONArray array = new JSONArray();
			array.addAll(((DataMessage) message).getSlotStatus());
			jsonObject.put(DataMessage.SLOT_STATUS, array);
			
			array = new JSONArray();
			array.addAll(((DataMessage) message).getLedStatus());
			jsonObject.put(DataMessage.LED_STATUS, array);
			
			jsonObject.put(DataMessage.ENTRY_GATE_STATUS, ((DataMessage) message).getEntrygateStatus());
			jsonObject.put(DataMessage.EXIT_GATE_STATUS, ((DataMessage) message).getExitgateStatus());
			jsonObject.put(DataMessage.ENTRY_GATE_LED_STATUS, ((DataMessage) message).getEntrygateledStatus());
			jsonObject.put(DataMessage.EXIT_GATE_LED_STATUS, ((DataMessage) message).getExitgateledStatus());
			jsonObject.put(DataMessage.ENTRY_GATE_ARRIVE, ((DataMessage) message).getEntrygateArrive());
			jsonObject.put(DataMessage.EXIT_GATE_ARRIVE, ((DataMessage) message).getExitgateArrive());
			break;
		case PARKING_LOT_STATUS:
			jsonObject.put(DataMessage.SLOT_COUNT, ((DataMessage) message).getSlotCount());
			
			array = new JSONArray();
			array.addAll(((DataMessage) message).getSlotStatus());
			jsonObject.put(DataMessage.SLOT_STATUS, array);
			
			array = new JSONArray();
			array.addAll(((DataMessage) message).getSlotDriverId());
			jsonObject.put(DataMessage.SLOT_DRIVER_ID, array);
			
			array = new JSONArray();
			array.addAll(((DataMessage) message).getDriverOften());
			jsonObject.put(DataMessage.DRIVER_OFTEN, array);
			
			array = new JSONArray();
			array.addAll(((DataMessage) message).getSlotTime());
			jsonObject.put(DataMessage.SLOT_TIME, array);
		case NOTIFICATION:
			jsonObject.put(DataMessage.TYPE, ((DataMessage) message).getType());
		case SLOT_LED_STATUS:
		case SLOT_SENSOR_STATUS:
			jsonObject.put(DataMessage.SLOT_NUMBER, ((DataMessage) message).getSlotNumber());
			jsonObject.put(DataMessage.STATUS, ((DataMessage) message).getStatus());
			break;
			
		// Park Here
		case RESERVATION_REQUEST:
			jsonObject.put(DataMessage.DRIVER_ID, ((DataMessage) message).getDriverID());
			jsonObject.put(DataMessage.PARKING_LOT_ID, ((DataMessage) message).getParkingLotId());
			jsonObject.put(DataMessage.RESERVATION_TIME, ((DataMessage) message).getReservationTime());
			jsonObject.put(DataMessage.PAYMENT_INFO, ((DataMessage) message).getPaymentInfo());
		case RESERVATION_INFO_REQUEST:
			jsonObject.put(DataMessage.DRIVER_ID, ((DataMessage) message).getDriverID());
		case CANCEL_REQUEST:
			jsonObject.put(DataMessage.DRIVER_ID, ((DataMessage) message).getDriverID());
			jsonObject.put(DataMessage.RESULT, ((DataMessage) message).getResult());
			jsonObject.put(DataMessage.RESERVATION_ID, ((DataMessage) message).getReservationID());
		case PARKING_LOT_INFORMATION2:
			jsonObject.put(DataMessage.PARKING_LOT_COUNT, ((DataMessage) message).getDriverID());
			array = new JSONArray();
			array.addAll(((DataMessage) message).getParkingLotId());
			jsonObject.put(DataMessage.PARKING_LOT_ID, array);
			array = new JSONArray();
			array.addAll(((DataMessage) message).getParkingLotLocation());
			jsonObject.put(DataMessage.PARKING_LOT_LOCATION, array);
			array = new JSONArray();
			array.addAll(((DataMessage) message).getParkingFee());
			jsonObject.put(DataMessage.PARKING_FEE, array);
			array = new JSONArray();
			array.addAll(((DataMessage) message).getGracePeriod());
			jsonObject.put(DataMessage.GRACE_PERIOD, array);
		case RESERVATION_INFORMATION:
			jsonObject.put(DataMessage.RESULT, ((DataMessage) message).getResult());
			jsonObject.put(DataMessage.RESERVATION_ID, ((DataMessage) message).getReservationID());
			jsonObject.put(DataMessage.RESERVATION_TIME, ((DataMessage) message).getReservationTime());
			jsonObject.put(DataMessage.PARKING_LOT_ID, ((DataMessage) message).getParkingLotId());
			jsonObject.put(DataMessage.PARKING_LOT_LOCATION, ((DataMessage) message).getParkingLotLocation());
			jsonObject.put(DataMessage.PARKING_FEE, ((DataMessage) message).getParkingFee());
			jsonObject.put(DataMessage.GRACE_PERIOD, ((DataMessage) message).getGracePeriod());
			jsonObject.put(DataMessage.PAYMENT_INFO, ((DataMessage) message).getPaymentInfo());
			jsonObject.put(DataMessage.CONFIRMATION_INFO, ((DataMessage) message).getConfirmationInfo());
		case CANCEL_RESPONSE:
			jsonObject.put(DataMessage.RESULT, ((DataMessage) message).getResult());
			jsonObject.put(DataMessage.RESERVATION_ID, ((DataMessage) message).getReservationID());
		default:
			break;
		}

		return jsonObject;
	}

	public static MessageType getMessageType(JSONObject jsonObject) {
		MessageType messageType = MessageType.NONE;

		if (jsonObject.get(Message.MESSAGE_TYPE) != null) {
			String str = (String) jsonObject.get(Message.MESSAGE_TYPE);
			messageType = MessageType.fromText(str);
		}

		return messageType;
	}

	public static MessageType getMessageType(String jsonString) {
		Message message = convertToMessage(jsonString);

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
		Message message = convertToMessage(jsonString);

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