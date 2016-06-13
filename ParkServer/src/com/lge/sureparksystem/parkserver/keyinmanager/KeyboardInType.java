package com.lge.sureparksystem.parkserver.keyinmanager;

public enum KeyboardInType {
	KEYBOARD_1(1),
	KEYBOARD_2(2),
	KEYBOARD_3(3),
	KEYBOARD_4(4),
	KEYBOARD_NONE(100);
	
	int value;
	
	KeyboardInType(int value) {
		this.value = value;
	}
	
	public static KeyboardInType fromValue(int x) {
        switch(x) {
        case 1:
            return KEYBOARD_1;
        case 2:
            return KEYBOARD_2;
        case 3:
            return KEYBOARD_3;
        case 4:
            return KEYBOARD_4;
        }
        
        return null;
    }
}
