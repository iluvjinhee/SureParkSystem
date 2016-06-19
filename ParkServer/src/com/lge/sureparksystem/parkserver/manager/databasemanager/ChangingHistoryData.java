package com.lge.sureparksystem.parkserver.manager.databasemanager;

import java.util.Date;

public class ChangingHistoryData {
    private int parkinglotId;
    private Date changedTime;
    private int changedType;
    private String changedValue;

    public ChangingHistoryData() {
    }

    public ChangingHistoryData(int parkinglotId, Date changedTime, int changedType,
            String changedValue) {
        super();
        this.parkinglotId = parkinglotId;
        this.changedTime = changedTime;
        this.changedType = changedType;
        this.changedValue = changedValue;
    }

    public int getParkinglotId() {
        return parkinglotId;
    }

    public void setParkinglotId(int parkinglotId) {
        this.parkinglotId = parkinglotId;
    }

    public Date getChangedTime() {
        return changedTime;
    }

    public void setChangedTime(Date changedTime) {
        this.changedTime = changedTime;
    }

    public int getChangedType() {
        return changedType;
    }

    public void setChangedType(int changedType) {
        this.changedType = changedType;
    }

    public String getChangedValue() {
        return changedValue;
    }

    public void setChangedValue(String changedValue) {
        this.changedValue = changedValue;
    }

    @Override
    public String toString() {
        return "ChangingHistoryData [parkinglotId=" + parkinglotId + ", changedTime=" + changedTime
                + ", changedType=" + changedType + ", changedValue=" + changedValue + "]";
    }

}
