package com.lge.sureparksystem.parkserver.manager.databasemanager;

import java.util.Date;

import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseInfo.Reservation;

public class ReservationData {
    private int id;
    private String userEmail;
    private Date reservationTime;
    private int parkinglotId;
    private String creditInfo;
    private String confirmInfo;
    private String parkingFee;
    private String gracePeriod;
    private int reservationState;
    private float payment;

    public ReservationData() {
    }

    public ReservationData(int id, String userEmail, Date reservationTime, int parkinglotId,
            String ceaditInfo, String confirmInfo, String parkingFee, String gracePeriod) {
        super();
        this.id = id;
        this.userEmail = userEmail;
        this.reservationTime = reservationTime;
        this.parkinglotId = parkinglotId;
        this.creditInfo = ceaditInfo;
        this.confirmInfo = confirmInfo;
        this.parkingFee = parkingFee;
        this.gracePeriod = gracePeriod;
        this.reservationState = Reservation.STATE_TYPE.RESERVED;
        this.payment = 0.0f;
    }

    public ReservationData(int id, String userEmail, Date reservationTime, int parkinglotId,
            String ceaditInfo, String confirmInfo, String parkingFee, String gracePeriod,
            int reservationState, float payment) {
        super();
        this.id = id;
        this.userEmail = userEmail;
        this.reservationTime = reservationTime;
        this.parkinglotId = parkinglotId;
        this.creditInfo = ceaditInfo;
        this.confirmInfo = confirmInfo;
        this.parkingFee = parkingFee;
        this.gracePeriod = gracePeriod;
        this.reservationState = reservationState;
        this.payment = payment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Date reservationTime) {
        this.reservationTime = reservationTime;
    }

    public int getParkinglotId() {
        return parkinglotId;
    }

    public void setParkinglotId(int parkinglotId) {
        this.parkinglotId = parkinglotId;
    }

    public String getCreditInfo() {
        return creditInfo;
    }

    public void setCreditInfo(String ceaditInfo) {
        this.creditInfo = ceaditInfo;
    }

    public String getConfirmInfo() {
        return confirmInfo;
    }

    public void setConfirmInfo(String confirmInfo) {
        this.confirmInfo = confirmInfo;
    }

    public String getParkingFee() {
        return parkingFee;
    }

    public void setParkingFee(String parkingFee) {
        this.parkingFee = parkingFee;
    }

    public String getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(String gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public int getReservationState() {
        return reservationState;
    }

    public void setReservationState(int reservationState) {
        this.reservationState = reservationState;
    }

    public float getPayment() {
        return payment;
    }

    public void setPayment(float payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "ReservationData [id=" + id + ", userEmail=" + userEmail + ", reservationTime="
                + reservationTime + ", parkinglotId=" + parkinglotId + ", creditInfo=" + creditInfo
                + ", confirmInfo=" + confirmInfo + ", parkingFee=" + parkingFee + ", gracePeriod="
                + gracePeriod + ", reservationState=" + reservationState + ", payment=" + payment
                + "]";
    }

}
