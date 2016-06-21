package com.lge.sureparksystem.parkserver.manager.databasemanager;

public class StatisticsData {
    private int year;
    private int month;
    private int day;
    private float revenue;
    private float occupancyRate;
    private float cancelRate;

    public StatisticsData() {
    }

    public StatisticsData(int year, int month, int day, float revenue, float occupancyRate,
            float cancelRate) {
        super();
        this.year = year;
        this.month = month;
        this.day = day;
        this.revenue = revenue;
        this.occupancyRate = occupancyRate;
        this.cancelRate = cancelRate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public float getRevenue() {
        return revenue;
    }

    public void setRevenue(float revenue) {
        this.revenue = revenue;
    }

    public float getOccupancyRate() {
        return occupancyRate;
    }

    public void setOccupancyRate(float occupancyRate) {
        this.occupancyRate = occupancyRate;
    }

    public float getCancelRate() {
        return cancelRate;
    }

    public void setCancelRate(float cancelRate) {
        this.cancelRate = cancelRate;
    }

    @Override
    public String toString() {
        return "StatisticsData [year=" + year + ", month=" + month + ", day=" + day + ", revenue="
                + revenue + ", occupancyRate=" + occupancyRate + ", cancelRate=" + cancelRate + "]";
    }

}
