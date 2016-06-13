package com.lge.sureparksystem.parkserver.manager.databasemanager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Reservation {
    private String login_id;
    private Date reservation_time;
    private String parkinglot_id;
    private int parking_fee;
    private int grace_period;
    private Date arrival_time;
    private Date departure_time;
    private String confirmation_code;
    private int assigned_slot;

    public Reservation() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String message = dateFormat.format(cal.getTime());
        LogHelper.log(message);
    }
    

    public Reservation(String id, Date date, String lot, int fee, int period, Date start, Date end,
            String code, int slot) {
        this.login_id = id;
        this.reservation_time = date;
        this.parkinglot_id = lot;
        this.parking_fee = fee;
        this.grace_period = period;
        this.arrival_time = start;
        this.departure_time = end;
        this.confirmation_code = code;
        this.assigned_slot = slot;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public Date getReservation_time() {
        return reservation_time;
    }

    public void setReservation_time(Date reservation_time) {
        this.reservation_time = reservation_time;
    }

    public String getParkinglot_id() {
        return parkinglot_id;
    }

    public void setParkinglot_id(String parkinglot_id) {
        this.parkinglot_id = parkinglot_id;
    }

    public int getParking_fee() {
        return parking_fee;
    }

    public void setParking_fee(int parking_fee) {
        this.parking_fee = parking_fee;
    }

    public int getGrace_period() {
        return grace_period;
    }

    public void setGrace_period(int grace_period) {
        this.grace_period = grace_period;
    }

    public Date getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(Date arrival_time) {
        this.arrival_time = arrival_time;
    }

    public Date getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(Date departure_time) {
        this.departure_time = departure_time;
    }

    public String getConfirmation_code() {
        return confirmation_code;
    }

    public void setConfirmation_code(String confirmation_code) {
        this.confirmation_code = confirmation_code;
    }

    public int getAssigned_slot() {
        return assigned_slot;
    }

    public void setAssigned_slot(int assigned_slot) {
        this.assigned_slot = assigned_slot;
    }

    @Override
    public String toString() {
        return "Reservation [login_id=" + login_id + ", reservation_time=" + reservation_time
                + ", parkinglot_id=" + parkinglot_id + ", parking_fee=" + parking_fee
                + ", grace_period=" + grace_period + ", arrival_time=" + arrival_time
                + ", departure_time=" + departure_time + ", confirmation_code=" + confirmation_code
                + ", assigned_slot=" + assigned_slot + "]";
    }

}
