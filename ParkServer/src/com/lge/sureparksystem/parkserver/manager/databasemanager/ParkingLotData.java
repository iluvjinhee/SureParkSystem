package com.lge.sureparksystem.parkserver.manager.databasemanager;

public class ParkingLotData {
    private int id;
    private String loginId;
    private String loginPw;
    private String lotName;
    private Float fee;
    private int gracePeriod;
    private String userEmail;

    public ParkingLotData() {
    }

    public ParkingLotData(int id, String loginId, String loginPw, String lotName, Float fee,
            int gracePeriod, String userEmail) {
        super();
        this.id = id;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.lotName = lotName;
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

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public Float getFee() {
        return fee;
    }

    public void setFee(Float fee) {
        this.fee = fee;
    }

    public int getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(int gracePeriod) {
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
                + ", lotName=" + lotName + ", fee=" + fee + ", gracePeriod=" + gracePeriod
                + ", userEmail=" + userEmail + "]";
    }

}
