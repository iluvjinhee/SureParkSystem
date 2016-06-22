package com.lge.sureparksystem.parkserver.manager.reservationmanager;

public class ParkingSlot {
    public static final int EMPTY = 0;
    public static final int OCCUPIED = 1;

    private int slotNumber;
    private int status;
    private String driver_id;

    public ParkingSlot() {
    }

    public ParkingSlot(int slotNumber, int status) {
        this.slotNumber = slotNumber;
        this.status = status;
    }

    public ParkingSlot(int slotNumber, int status, String driver_id) {
        this.slotNumber = slotNumber;
        this.status = status;
        this.driver_id = driver_id;
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

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    @Override
    public String toString() {
        return "ParkingSlot [slotNumber=" + slotNumber + ", status=" + status + ", driver_id="
                + driver_id + "]";
    }

}
