package com.lge.sureparksystem.parkserver.message;

public class DataMessage extends Message {
	public static String ASSIGNED_SLOT = "ASSIGNED SLOT";
	public static String RESERVATION_CODE = "RESERVATION CODE";

	String assignedSlot;
	String reservationCode;

	public DataMessage(MessageType messageType) {
		super(messageType);
	}

	public String getAssignedSlot() {
		return assignedSlot;
	}

	public void setAssignedSlot(String assignedSlot) {
		this.assignedSlot = assignedSlot;
	}

	public String getReservationCode() {
		return reservationCode;
	}

	public void setReservationCode(String reservationCode) {
		this.reservationCode = reservationCode;
	}
}
