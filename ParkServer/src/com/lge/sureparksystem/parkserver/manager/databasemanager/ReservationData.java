package com.lge.sureparksystem.parkserver.manager.databasemanager;

import java.util.Date;

public class ReservationData {
    private int id;
    private String userEmail;
    private Date reservedTime;
    private int parkinglotId;
    private String paymentInfo;
    private String confirmationCode;
    private int state;
    private String fee;
    private String gracePeriod;

    public ReservationData() {
    }

    public ReservationData(int id, String userEmail, Date reservedTime, int parkinglotId,
            String paymentInfo, String confirmationCode, int state, String fee,
            String gracePeriod) {
        super();
        this.id = id;
        this.userEmail = userEmail;
        this.reservedTime = reservedTime;
        this.parkinglotId = parkinglotId;
        this.paymentInfo = paymentInfo;
        this.confirmationCode = confirmationCode;
        this.state = state;
        this.fee = fee;
        this.gracePeriod = gracePeriod;
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

    public Date getReservedTime() {
        return reservedTime;
    }

    public void setReservedTime(Date reservedTime) {
        this.reservedTime = reservedTime;
    }

    public int getParkinglotId() {
        return parkinglotId;
    }

    public void setParkinglotId(int parkinglotId) {
        this.parkinglotId = parkinglotId;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(String gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    @Override
    public String toString() {
        return "ReservationData [id=" + id + ", userEmail=" + userEmail + ", reservedTime="
                + reservedTime + ", parkinglotId=" + parkinglotId + ", paymentInfo=" + paymentInfo
                + ", confirmationCode=" + confirmationCode + ", state=" + state + ", fee=" + fee
                + ", gracePeriod=" + gracePeriod + "]";
    }

}
