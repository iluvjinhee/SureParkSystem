package com.lge.sureparksystem.parkserver.message;

public enum MessageType {
	UNDEFINED(0),
    WELCOME_SUREPARK(1),
    SCAN_CONFIRM(2),
    RESERVATION_NUMBER(3),
    ASSIGN_SLOT(4),
    NOT_RESERVED(5),
    NONE(1000);
	
	private int value;
	
	private MessageType(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public static MessageType fromValue(int x) {
        switch(x) {
        case 0:
            return UNDEFINED;
        case 1:
            return WELCOME_SUREPARK;
        case 2:
            return SCAN_CONFIRM;
        case 3:
            return RESERVATION_NUMBER;
        case 4:
            return ASSIGN_SLOT;
        case 5:
        	return NOT_RESERVED;
        }
        
        return null;
    }
}