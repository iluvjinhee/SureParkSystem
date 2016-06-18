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
    ENTRYGATE_ARRIVE("EntryGate_Arrive"),
    ENTRYGATE_LED("EntryGate_LED"),
    ENTRYGATE_PASSBY("EntryGate_PassBy"),
    ENTRYGATE_SERVO("EntryGate_Servo"),
    EXITGATE_ARRIVE("ExitGate_Arrive"),
    EXITGATE_LED("ExitGate_LED"),
    EXITGATE_PASSBY("ExitGate_PassBy"),
    EXITGATE_SERVO("ExitGate_Servo"),
    HEARTBEAT("HeartBeat"),
    PARKINGLOT_INFORMATION("Parkinglot_Information"),
    PARKINGSLOT_LED("Parkingslot_LED"),
    PARKINGSLOT_SENSOR("Parkingslot_Sensor"),
    
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