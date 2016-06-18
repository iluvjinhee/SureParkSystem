package com.lge.sureparksystem.parkserver.message;

public enum MessageType {
	ACKNOWLEDGE("ACK"),
	
	// Park View
    WELCOME_SUREPARK("WELCOME SUREPARK"),
    SCAN_CONFIRM("SCAN CONFIRM"),
    RESERVATION_CODE("RESERVATION CODE"),
    ASSIGNED_SLOT("ASSIGNED SLOT"),
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
    EXITGATE_ARRIVE("ExitGate_Arrive"),
    EXITGATE_PASSBY("ExitGate_PassBy"),
    EXIT_GATE_CONTROL("ExitGate_Control"),
    EXIT_GATE_LED_CONTROL("ExitGate_LED"),
    EXIT_GATE_LED_STATUS("ExitGate_LED"),
    EXIT_GATE_STATUS("ExitGate_Servo"),
    HEARTBEAT("HeartBeat"),
    PARKINGLOT_INFORMATION("Parkinglot_Information"),
    SLOT_LED_CONTROL("Parkingslot_LED"),
    SLOT_LED_STATUS("Parkingslot_LED"),
    SLOT_SENSOR_STATUS("Parkingslot_Sensor"),
    
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