package com.lge.sureparksystem.parkserver.message;

public enum MessageType {
	AUTHENTICATION_OK("AUTHENTICATION OK"),
	AUTHENTICATION_FAIL("AUTHENTICATION FAIL"),
	
	// Common
	ACKNOWLEDGE("ACK"),	
	AUTHENTICATION_REQUEST("Authentication_Request"),
    AUTHENTICATION_RESPONSE("Authentication_Response"),
	
	// Park View
	CONFIRMATION_SEND("Confirmation_Send"),
	QR_START("QR_Start"),
	WELCOME_DISPLAY("Welcome_Display"),
	CONFIRMATION_RESPONSE("Confirmation_Response"),
    
    // Parking Lot    
    ENTRYGATE_ARRIVE("EntryGate_Arrive"),
    ENTRYGATE_PASSBY("EntryGate_PassBy"),
    ENTRY_GATE_CONTROL("EntryGate_Control"),
    ENTRY_GATE_LED_CONTROL("EntryGate_LED"),
    ENTRY_GATE_LED_STATUS("EntryGate_LED"),
    ENTRY_GATE_STATUS("EntryGate_Servo"),
    EXIT_GATE_ARRIVE("ExitGate_Arrive"),
    EXIT_GATE_PASSBY("ExitGate_PassBy"),
    EXIT_GATE_CONTROL("ExitGate_Control"),
    EXIT_GATE_LED_CONTROL("ExitGate_LED"),
    EXIT_GATE_LED_STATUS("ExitGate_LED"),
    EXIT_GATE_STATUS("ExitGate_Servo"),
    HEARTBEAT("HeartBeat"),
    PARKING_LOT_INFORMATION("Parkinglot_Information"),
    SLOT_LED_CONTROL("Parkingslot_LED"),
    SLOT_LED_STATUS("Parkingslot_LED"),
    SLOT_SENSOR_STATUS("Parkingslot_Sensor"),
    
    // ParkHere
	ADD_PARKINGLOT("Add_Parkinglot"),
	CANCEL_REQUEST("Cancel_Request"),
	CANCEL_RESPONSE("Cancel_Response"),
	CHANGE_GRACEPERIOD("Change_graceperiod"),
	CHANGE_PARKINGFEE("Change_parkingfee"),
	CHANGE_RESPONSE("Change_Response"),
	CREATE_ATTENDANT("Create_Attendant"),
	NOTIFICATION("Notification"),
	PARKING_LOT_STATISTICS("Parkinglot_Statistics"),
	PARKING_LOT_STATS_REQUEST("Parkinglot_stats_Request"),
	PARKING_LOT_INFO_REQUEST("ParkinglotInfo_Request"),
	PARKING_LOT_LIST("Parkinglot_List"),
	PARKING_LOT_STATUS("ParkinglotStatus"),
	PARKING_LOT_STATUS_REQUEST("ParkinglotStatus_Request"),
	REMOVE_ATTENDANT("Remove_Attendant"),
	REMOVE_PARKINGLOT("Remove_Parkinglot"),
	RESERVATION_INFORMATION("Reservation_Information"),
	RESERVATION_INFO_REQUEST("ReservationInfo_Request"),
	RESERVATION_REQUEST("Reservation_Request"),
    
    NONE("NONE");
	
	private String text;
	
	MessageType(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
	
	public static MessageType fromText(String text) {
		if (text != null) {
			for (MessageType b : MessageType.values()) {
				if (text.equalsIgnoreCase(b.text)) {
					return b;
				}
			}
		}
		return null;
	}
}