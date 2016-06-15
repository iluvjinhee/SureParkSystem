package com.lge.sureparksystem.parkserver.manager.databasemanager;

import java.util.Date;

public class ParkingData {
    private int parkingId;
    private int reservationId;
    private int assigned_slot;
    private int parked_slot;
    private Date parkingTime;
    private Date unparkingTime;

    public ParkingData() {
    }

    public ParkingData(int parkingId, int reservationId, int assigned_slot, int parked_slot,
            Date parkingTime, Date unparkingTime) {
        super();
        this.parkingId = parkingId;
        this.reservationId = reservationId;
        this.assigned_slot = assigned_slot;
        this.parked_slot = parked_slot;
        this.parkingTime = parkingTime;
        this.unparkingTime = unparkingTime;
    }

    public int getParkingId() {
        return parkingId;
    }

    public void setParkingId(int parkingId) {
        this.parkingId = parkingId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getAssigned_slot() {
        return assigned_slot;
    }

    public void setAssigned_slot(int asigned_slot) {
        this.assigned_slot = asigned_slot;
    }

    public int getParked_slot() {
        return parked_slot;
    }

    public void setParked_slot(int parked_slot) {
        this.parked_slot = parked_slot;
    }

    public Date getParkingTime() {
        return parkingTime;
    }

    public void setParkingTime(Date parkingTime) {
        this.parkingTime = parkingTime;
    }

    public Date getUnparkingTime() {
        return unparkingTime;
    }

    public void setUnparkingTime(Date unparkingTime) {
        this.unparkingTime = unparkingTime;
    }

    @Override
    public String toString() {
        return "ParkingData [parkingId=" + parkingId + ", reservationId=" + reservationId
                + ", assigned_slot=" + assigned_slot + ", parked_slot=" + parked_slot
                + ", parkingTime=" + parkingTime + ", unparkingTime=" + unparkingTime + "]";
    }

}
