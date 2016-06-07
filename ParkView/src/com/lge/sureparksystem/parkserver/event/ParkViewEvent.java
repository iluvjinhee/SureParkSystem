package com.lge.sureparksystem.parkserver.event;

public enum ParkViewEvent {
    WELCOME_SUREPARK("Welcome! Sure Park."),
    SCAN_CONFIRM("Scan your confirmation number."),
    ASSIGN_SLOT("Your Park Slot is ");

    private final String text;

    ParkViewEvent(final String text) {
        this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public static ParkViewEvent fromString(String text) {
		if (text != null) {
			for (ParkViewEvent b : ParkViewEvent.values()) {
				if (text.equalsIgnoreCase(b.text)) {
					return b;
				}
			}
		}
		
		return null;
	}

    @Override
    public String toString() {
        return text;
    }
}