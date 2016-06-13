package com.lge.sureparksystem.parkclientfortest.message;

public enum MessageType {
    WELCOME_SUREPARK("WELCOME SUREPARK"),
    SCAN_CONFIRM("SCAN CONFIRM"),
    RESERVATION_NUMBER("RESERVATION NUMBER"),
    ASSIGN_SLOT("ASSIGN SLOT"),
    NOT_RESERVED("NOT RESERVED"),
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