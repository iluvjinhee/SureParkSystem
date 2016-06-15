package com.lge.sureparksystem.parkserver.message;

public enum MessageType {
	ACKNOWLEDGE("ACK"),
	
	// Park View
    WELCOME_SUREPARK("WELCOME SUREPARK"),
    SCAN_CONFIRM("SCAN CONFIRM"),
    RESERVATION_NUMBER("RESERVATION NUMBER"),
    ASSIGN_SLOT("ASSIGN SLOT"),
    NOT_RESERVED("NOT RESERVED"),
    
    // Parking Lot
    
    
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