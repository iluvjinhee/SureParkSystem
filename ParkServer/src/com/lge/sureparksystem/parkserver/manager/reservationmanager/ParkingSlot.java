package com.lge.sureparksystem.parkserver.manager.reservationmanager;

public class ParkingSlot {
    public static final int EMPTY = 0;
    public static final int OCCUPIED = 1;

    private int slotNumber;
    private int status;
    private int reservationId;

    public ParkingSlot() {
    }

    public ParkingSlot(int slotNumber, int status) {
        this.slotNumber = slotNumber;
        this.status = status;
    }

	public ParkingSlot(int slotNumber, int status, int reservationId) {
		super();
		this.slotNumber = slotNumber;
		this.status = status;
		this.reservationId = reservationId;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getReservationId() {
		return reservationId;
	}

	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}

	@Override
	public String toString() {
		return "ParkingSlot [slotNumber=" + slotNumber + ", status=" + status + ", reservationId="
				+ reservationId + "]";
	}

    

}
