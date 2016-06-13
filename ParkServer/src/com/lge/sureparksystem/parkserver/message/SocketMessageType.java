package com.lge.sureparksystem.parkserver.message;

public enum SocketMessageType {
    WELCOME_SUREPARK("WELCOME SUREPARK"),
    SCAN_CONFIRM("SCAN CONFIRM"),
    RESERVATION_NUMBER("RESERVATION NUMBER"),
    ASSIGN_SLOT("ASSIGN SLOT"),
    NOT_RESERVED("NOT RESERVED"),
    NONE("NONE");
	
	private String text;
	
	SocketMessageType(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
	
	public static SocketMessageType fromText(String text) {
		if (text != null) {
			for (SocketMessageType b : SocketMessageType.values()) {
				if (text.equalsIgnoreCase(b.text)) {
					return b;
				}
			}
		}
		return null;
	}
}