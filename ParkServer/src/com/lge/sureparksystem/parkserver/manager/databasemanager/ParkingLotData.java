package com.lge.sureparksystem.parkserver.manager.databasemanager;

public class ParkingLotData {
    private int id;
    private String loginId;
    private String loginPw;
    private String lotAddress;
    private String fee;
    private String gracePeriod;
    private String userEmail;

    public ParkingLotData() {
    }

    public ParkingLotData(int id, String loginId, String loginPw, String lotAddress, String fee,
            String gracePeriod, String userEmail) {
        super();
        this.id = id;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.lotAddress = lotAddress;
        this.fee = fee;
        this.gracePeriod = gracePeriod;
        this.userEmail = userEmail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginPw() {
        return loginPw;
    }

    public void setLoginPw(String loginPw) {
        this.loginPw = loginPw;
    }

    public String getLotAddress() {
        return lotAddress;
    }

    public void setLotAddress(String lotAddress) {
        this.lotAddress = lotAddress;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "ParkingLotData [id=" + id + ", loginId=" + loginId + ", loginPw=" + loginPw
                + ", lotAddress=" + lotAddress + ", fee=" + fee + ", gracePeriod=" + gracePeriod
                + ", userEmail=" + userEmail + "]";
    }

}
