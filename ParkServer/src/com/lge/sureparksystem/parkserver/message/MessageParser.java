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
		
		((DataMessage) message).setPort(MessageParser.getInt(jsonObject, DataMessage.PORT));
		((DataMessage) message).setAuthority(MessageParser.getInt(jsonObject, DataMessage.AUTHORITY));
		
		// Parking Lot
		((DataMessage) message).setEntryGateLEDStatus(MessageParser.getString(jsonObject, DataMessage.ENTRY_GATE_LED_STATUS));
		((DataMessage) message).setEntryGateStatus(MessageParser.getString(jsonObject, DataMessage.ENTRY_GATE_STATUS));
		((DataMessage) message).setEntrygateArrive(MessageParser.getString(jsonObject, DataMessage.ENTRY_GATE_ARRIVE));
		((DataMessage) message).setExitGateLEDStatus(MessageParser.getString(jsonObject, DataMessage.EXIT_GATE_LED_STATUS));
		((DataMessage) message).setExitGateStatus(MessageParser.getString(jsonObject, DataMessage.EXIT_GATE_STATUS));
		((DataMessage) message).setExitgateArrive(MessageParser.getString(jsonObject, DataMessage.EXIT_GATE_ARRIVE));
		((DataMessage) message).setID(MessageParser.getString(jsonObject, DataMessage.ID));
		((DataMessage) message).setLedNumber(MessageParser.getInt(jsonObject, DataMessage.LED_NUMBER));
		((DataMessage) message).setLedStatusList(MessageParser.getStringList(jsonObject, DataMessage.LED_STATUS));
		((DataMessage) message).setPassword(MessageParser.getString(jsonObject, DataMessage.PASSWORD));
		((DataMessage) message).setSensorNumber(MessageParser.getInt(jsonObject, DataMessage.SENSOR_NUMBER));
		((DataMessage) message).setSlotNumber(MessageParser.getInt(jsonObject, DataMessage.SLOT_NUMBER));
		((DataMessage) message).setSlotStatusStatus(MessageParser.getStringList(jsonObject, DataMessage.SLOT_STATUS));
		((DataMessage) message).setStatus(MessageParser.getString(jsonObject, DataMessage.STATUS));
		
		// ParkHere
		((DataMessage) message).setDriverOften(MessageParser.getStringList(jsonObject, DataMessage.DRIVER_OFTEN));
		((DataMessage) message).setGracePeriod(MessageParser.getString(jsonObject, DataMessage.GRACE_PERIOD));
		((DataMessage) message).setGracePeriodList(MessageParser.getStringList(jsonObject, DataMessage.GRACE_PERIOD_LIST));
		((DataMessage) message).setParkingFee(MessageParser.getString(jsonObject, DataMessage.PARKING_FEE));
		((DataMessage) message).setParkingFeeList(MessageParser.getStringList(jsonObject, DataMessage.PARKING_FEE_LIST));
		((DataMessage) message).setParkingLotID(MessageParser.getString(jsonObject, DataMessage.PARKING_LOT_ID));
		((DataMessage) message).setParkingLotIDList(MessageParser.getStringList(jsonObject, DataMessage.PARKING_LOT_ID_LIST));
		((DataMessage) message).setParkingLotLocation(MessageParser.getString(jsonObject, DataMessage.PARKING_LOT_LOCATION));
		((DataMessage) message).setParkingLotLocationList(MessageParser.getStringList(jsonObject, DataMessage.PARKING_LOT_LOCATION_LIST));
		((DataMessage) message).setSlotDriverID(MessageParser.getStringList(jsonObject, DataMessage.SLOT_DRIVER_ID));
		((DataMessage) message).setSlotTime(MessageParser.getStringList(jsonObject, DataMessage.SLOT_TIME));
		((DataMessage) message).setAddress(MessageParser.getString(jsonObject, DataMessage.ADDRESS));
		((DataMessage) message).setCancelRate(MessageParser.getString(jsonObject, DataMessage.CANCEL_RATE));
		((DataMessage) message).setConfirmationInfo(MessageParser.getString(jsonObject, DataMessage.CONFIRMATION_INFO));
		((DataMessage) message).setDriverID(MessageParser.getString(jsonObject, DataMessage.DRIVER_ID));
		((DataMessage) message).setName(MessageParser.getString(jsonObject, DataMessage.NAME));
		((DataMessage) message).setOccupancyRate(MessageParser.getString(jsonObject, DataMessage.OCCUPANCY_RATE));
		((DataMessage) message).setPaymentInfo(MessageParser.getString(jsonObject, DataMessage.PAYMENT_INFO));
		((DataMessage) message).setPeriod(MessageParser.getString(jsonObject, DataMessage.PERIOD));
		((DataMessage) message).setReservationId(MessageParser.getString(jsonObject, DataMessage.RESERVATION_ID));
		((DataMessage) message).setReservationTime(MessageParser.getString(jsonObject, DataMessage.RESERVATION_TIME));
		((DataMessage) message).setRevenue(MessageParser.getString(jsonObject, DataMessage.REVENUE));
		((DataMessage) message).setResult(MessageParser.getString(jsonObject, DataMessage.RESULT));
		((DataMessage) message).setType(MessageParser.getString(jsonObject, DataMessage.TYPE));
		((DataMessage) message).setValue(MessageParser.getString(jsonObject, DataMessage.VALUE));
		((DataMessage) message).setParkingLotCount(MessageParser.getInt(jsonObject, DataMessage.PARKING_LOT_COUNT));
		((DataMessage) message).setSlotCount(MessageParser.getInt(jsonObject, DataMessage.SLOT_COUNT));
		
		return message;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject convertToJSONObject(Message message) {
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.put(Message.MESSAGE_TYPE, message.getMessageType().getText());
		if (message.getTimestamp() != -1)
			jsonObject.put(Message.TIMESTAMP, message.getTimestamp());
		if (((DataMessage) message).getID() != null)
			jsonObject.put(DataMessage.ID, ((DataMessage) message).getID());

		switch (message.getMessageType()) {
		// Common
		case AUTHENTICATION_REQUEST:
			jsonObject.put(DataMessage.ID, ((DataMessage) message).getID());
			jsonObject.put(DataMessage.PASSWORD, ((DataMessage) message).getPassword());
			break;
		case AUTHENTICATION_RESPONSE:
			jsonObject.put(DataMessage.RESULT, ((DataMessage) message).getResult());
			jsonObject.put(DataMessage.AUTHORITY, ((DataMessage) message).getAuthority());
			break;
			
		// ParkView
		case CONFIRMATION_SEND:
			jsonObject.put(DataMessage.CONFIRMATION_INFO, ((DataMessage) message).getConfirmationInfo());
			break;
		case CONFIRMATION_RESPONSE:
			jsonObject.put(DataMessage.RESULT, ((DataMessage) message).getResult());
			jsonObject.put(DataMessage.SLOT_NUMBER, ((DataMessage) message).getSlotNumber());			
			break;
			
		// Parking Lot
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
			jsonObject.put(DataMessage.ENTRY_GATE_STATUS, ((DataMessage) message).getEntrygateStatus());
			jsonObject.put(DataMessage.EXIT_GATE_STATUS, ((DataMessage) message).getExitgateStatus());
			jsonObject.put(DataMessage.ENTRY_GATE_LED_STATUS, ((DataMessage) message).getEntrygateledStatus());
			jsonObject.put(DataMessage.EXIT_GATE_LED_STATUS, ((DataMessage) message).getExitgateledStatus());
			jsonObject.put(DataMessage.ENTRY_GATE_ARRIVE, ((DataMessage) message).getEntrygateArrive());
			jsonObject.put(DataMessage.EXIT_GATE_ARRIVE, ((DataMessage) message).getExitgateArrive());
			putList(jsonObject, DataMessage.SLOT_STATUS, ((DataMessage) message).getSlotStatusList());
			putList(jsonObject, DataMessage.LED_STATUS, ((DataMessage) message).getLEDStatusList());			
			break;
		case PARKING_LOT_STATUS:
			jsonObject.put(DataMessage.SLOT_COUNT, ((DataMessage) message).getSlotCount());
			putList(jsonObject, DataMessage.SLOT_STATUS, ((DataMessage) message).getSlotStatusList());
			putList(jsonObject, DataMessage.SLOT_DRIVER_ID, ((DataMessage) message).getSlotDriverIDList());
			putList(jsonObject, DataMessage.DRIVER_OFTEN, ((DataMessage) message).getDriverOftenList());
			putList(jsonObject, DataMessage.SLOT_TIME, ((DataMessage) message).getSlotTimeList());
			break;
		case NOTIFICATION:
			jsonObject.put(DataMessage.TYPE, ((DataMessage) message).getType());
			break;
		case SLOT_LED_STATUS:
		case SLOT_SENSOR_STATUS:
			jsonObject.put(DataMessage.SLOT_NUMBER, ((DataMessage) message).getSlotNumber());
			jsonObject.put(DataMessage.STATUS, ((DataMessage) message).getStatus());
			break;
			
		// Park Here
		case RESERVATION_REQUEST:
			jsonObject.put(DataMessage.DRIVER_ID, ((DataMessage) message).getDriverID());
			jsonObject.put(DataMessage.RESERVATION_TIME, ((DataMessage) message).getReservationTime());
			jsonObject.put(DataMessage.PAYMENT_INFO, ((DataMessage) message).getPaymentInfo());
			jsonObject.put(DataMessage.PARKING_LOT_ID, ((DataMessage) message).getParkingLotID());
			break;
		case RESERVATION_INFO_REQUEST:
			jsonObject.put(DataMessage.DRIVER_ID, ((DataMessage) message).getDriverID());
			break;
		case CANCEL_REQUEST:
			jsonObject.put(DataMessage.DRIVER_ID, ((DataMessage) message).getDriverID());
			jsonObject.put(DataMessage.RESULT, ((DataMessage) message).getResult());
			jsonObject.put(DataMessage.RESERVATION_ID, ((DataMessage) message).getReservationID());
			break;
		case PARKING_LOT_LIST:
			jsonObject.put(DataMessage.PARKING_LOT_COUNT, ((DataMessage) message).getParkingLotCount());
			putList(jsonObject, DataMessage.PARKING_LOT_ID_LIST, ((DataMessage) message).getParkingLotIDList());
			putList(jsonObject, DataMessage.PARKING_LOT_LOCATION_LIST, ((DataMessage) message).getParkingLotLocationList());
			putList(jsonObject, DataMessage.PARKING_FEE_LIST, ((DataMessage) message).getParkingFeeList());
			putList(jsonObject, DataMessage.GRACE_PERIOD_LIST, ((DataMessage) message).getGracePeriodList());
			break;
		case RESERVATION_INFORMATION:
			jsonObject.put(DataMessage.RESULT, ((DataMessage) message).getResult());
			jsonObject.put(DataMessage.RESERVATION_ID, ((DataMessage) message).getReservationID());
			jsonObject.put(DataMessage.RESERVATION_TIME, ((DataMessage) message).getReservationTime());
			jsonObject.put(DataMessage.PAYMENT_INFO, ((DataMessage) message).getPaymentInfo());
			jsonObject.put(DataMessage.CONFIRMATION_INFO, ((DataMessage) message).getConfirmationInfo());
			jsonObject.put(DataMessage.PARKING_FEE, ((DataMessage) message).getParkingFee());
			jsonObject.put(DataMessage.GRACE_PERIOD, ((DataMessage) message).getGracePeriod());
			jsonObject.put(DataMessage.PARKING_LOT_ID, ((DataMessage) message).getParkingLotID());
			jsonObject.put(DataMessage.PARKING_LOT_LOCATION, ((DataMessage) message).getParkingLotLocation());
			break;
		case CANCEL_RESPONSE:
			jsonObject.put(DataMessage.RESULT, ((DataMessage) message).getResult());
			jsonObject.put(DataMessage.RESERVATION_ID, ((DataMessage) message).getReservationID());
			break;
		case PARKING_LOT_STATS_REQUEST:
			jsonObject.put(DataMessage.PERIOD, ((DataMessage) message).getPeriod());
			jsonObject.put(DataMessage.PARKING_LOT_ID, ((DataMessage) message).getParkingLotID());
			break;
		case CHANGE_PARKING_FEE:
			jsonObject.put(DataMessage.PARKING_LOT_ID, ((DataMessage) message).getParkingLotID());
			jsonObject.put(DataMessage.PARKING_FEE, ((DataMessage) message).getParkingFee());
			break;
		case CHANGE_GRACE_PERIOD:
			jsonObject.put(DataMessage.PARKING_LOT_ID, ((DataMessage) message).getParkingLotID());
			jsonObject.put(DataMessage.GRACE_PERIOD, ((DataMessage) message).getGracePeriod());
			break;
		case CREATE_ATTENDANT:
			jsonObject.put(DataMessage.ID, ((DataMessage) message).getID());
			jsonObject.put(DataMessage.PASSWORD, ((DataMessage) message).getPassword());
			jsonObject.put(DataMessage.NAME, ((DataMessage) message).getName());
			jsonObject.put(DataMessage.PARKING_LOT_ID, ((DataMessage) message).getParkingLotID());
			break;
		case CREATE_DRIVER:
			jsonObject.put(DataMessage.ID, ((DataMessage) message).getID());
			jsonObject.put(DataMessage.PASSWORD, ((DataMessage) message).getPassword());
			jsonObject.put(DataMessage.NAME, ((DataMessage) message).getName());
			break;
		case REMOVE_ATTENDANT:
			jsonObject.put(DataMessage.ID, ((DataMessage) message).getID());
			break;
		case ADD_PARKING_LOT:
			jsonObject.put(DataMessage.ID, ((DataMessage) message).getID());
			jsonObject.put(DataMessage.PASSWORD, ((DataMessage) message).getPassword());
			jsonObject.put(DataMessage.ADDRESS, ((DataMessage) message).getAddress());
			jsonObject.put(DataMessage.PARKING_FEE, ((DataMessage) message).getParkingFee());
			jsonObject.put(DataMessage.GRACE_PERIOD, ((DataMessage) message).getGracePeriod());
			break;
		case REMOVE_PARKING_LOT:
			jsonObject.put(DataMessage.ID, ((DataMessage) message).getID());
			break;
		case PARKING_LOT_STATISTICS:
			jsonObject.put(DataMessage.SLOT_COUNT, ((DataMessage) message).getSlotCount());
			jsonObject.put(DataMessage.OCCUPANCY_RATE, ((DataMessage) message).getOccupancyRate());
			jsonObject.put(DataMessage.REVENUE, ((DataMessage) message).getRevenue());
			jsonObject.put(DataMessage.CANCEL_RATE, ((DataMessage) message).getCancelRate());
			putList(jsonObject, DataMessage.PARKING_LOT_ID_LIST, ((DataMessage) message).getParkingLotIDList());
			putList(jsonObject, DataMessage.SLOT_STATUS, ((DataMessage) message).getSlotStatusList());
			putList(jsonObject, DataMessage.SLOT_DRIVER_ID, ((DataMessage) message).getSlotDriverIDList());
			putList(jsonObject, DataMessage.DRIVER_OFTEN, ((DataMessage) message).getDriverOftenList());
			putList(jsonObject, DataMessage.SLOT_TIME, ((DataMessage) message).getSlotTimeList());
			break;
		case CHANGE_RESPONSE:
			jsonObject.put(DataMessage.RESULT, ((DataMessage) message).getResult());
			jsonObject.put(DataMessage.TYPE, ((DataMessage) message).getType());
			jsonObject.put(DataMessage.VALUE, ((DataMessage) message).getValue());
			break;
		case RESPONSE:
			jsonObject.put(DataMessage.RESULT, ((DataMessage) message).getResult());
			jsonObject.put(DataMessage.TYPE, ((DataMessage) message).getType());
			break;
		default:
			break;
		}

		return jsonObject;
	}

	private static void putList(JSONObject jsonObject, String key, ArrayList<String> list) {
		if(list == null)
			return;
		
		JSONArray array = new JSONArray();
		array.addAll(list);
		jsonObject.put(key, array);
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

		if (jsonObject.get(Message.TIMESTAMP) != null) {
			timestamp = ((Long) jsonObject.get(Message.TIMESTAMP)).intValue();
		}

		return timestamp;
	}

	public static int getTimestamp(String jsonString) {
		Message message = convertToMessage(jsonString);

		return message.getTimestamp();
	}
	
	public static String getString(JSONObject jsonObject, String key) {
		String value = null;

		if (jsonObject.get(key) != null) {
			value = (String) jsonObject.get(key);
		}

		return value;
	}
	
	public static int getInt(JSONObject jsonObject, String key) {
		int value = -1;

		if (jsonObject.get(key) != null) {
//			value = ((Long) jsonObject.get(key)).intValue();
//			value = (Integer) jsonObject.get(key);
			value = Integer.valueOf(jsonObject.get(key).toString());
		}

		return (int)value;
	}
	
	public static ArrayList<String> getStringList(JSONObject jsonObject, String key) {
		ArrayList<String> resultList = new ArrayList<String>();
		
		Object object = jsonObject.get(key);
		
		if(object instanceof JSONArray) {
			JSONArray childrenList = (JSONArray) jsonObject.get(key);
		    childrenList = (JSONArray) jsonObject.get(key);
			if(childrenList != null) {
		        Iterator<String> i = childrenList.iterator();
		
		        while (i.hasNext()) {
		            resultList.add(i.next());
		        }
			}
		}
		else if(object instanceof String) {
			String child = (String) jsonObject.get(key);

			resultList.add(child);
		}
		
		return resultList;
	}
}