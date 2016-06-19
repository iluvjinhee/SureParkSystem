package com.lge.sureparksystem.parkserver.message;

public enum MessageType {
	ACKNOWLEDGE("ACK"),
	AUTHENTICATION_OK("AUTHENTICATION OK"),
	AUTHENTICATION_FAIL("AUTHENTICATION FAIL"),
	
	// Park View
    WELCOME_SUREPARK("WELCOME SUREPARK"),
    SCAN_CONFIRM("SCAN CONFIRM"),
    RESERVATION_CODE("RESERVATION CODE"),
    ASSIGN_SLOT("ASSIGN SLOT"),
    NOT_RESERVED("NOT RESERVED"),
    
    // Parking Lot
    AUTHENTICATION_REQUEST("Authentication_Request"),
    AUTHENTICATION_RESPONSE("Authentication_Response"),
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
    PARKINGLOT_INFORMATION("Parkinglot_Information"),
    SLOT_LED_CONTROL("Parkingslot_LED"),
    SLOT_LED_STATUS("Parkingslot_LED"),
    SLOT_SENSOR_STATUS("Parkingslot_Sensor"),
    
    // ParkHere
    //AUTHENTICATION_REQUEST("Authentication_Request"),
    //AUTHENTICATION_RESPONSE("Authentication_Response"),
    //PARKINGLOT_INFORMATION("Parkinglot_Information"),
    CANCEL_REQUEST("Cancel_Request"),
    CANCEL_RESPONSE("Cancel_Response"),
    PARKINGLOTINFO_REQUEST("ParkinglotInfo_Request"),
    RESERVATIONINFO_REQUEST("ReservationInfo_Request"),
    RESERVATION_INFORMATION("Reservation_Information"),
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