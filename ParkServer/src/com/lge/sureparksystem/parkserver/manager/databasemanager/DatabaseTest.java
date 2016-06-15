package com.lge.sureparksystem.parkserver.manager.databasemanager;

import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseTest {

    AccountProvider mAccountProvider = null;
    ReservationProvider mReservationProvider = null;

    public DatabaseTest() {
        LogHelper.log();
        mAccountProvider = AccountProvider.getInstance();
        mReservationProvider = ReservationProvider.getInstance();
    }

    public void loadAccountListTest() {
        ArrayList<Account> accountList = (ArrayList<Account>)mAccountProvider.loadAccountList();
//        LogHelper.log(accountList.toString());
    }

    public void loadAccountTest() {
        Account account = mAccountProvider.loadAccount("admin");
//        LogHelper.log(account.toString());
    }
    
    public void addAccountTest() {
        Account account = new Account("test", "test", "test", "TEST");
        Boolean result = mAccountProvider.addAccount(account);
//        LogHelper.log(result.toString());
    }
    
    public void updateAccountTest() {
        Account account = new Account("test", "test", "testForupdate2", "TEST");
        int result = mAccountProvider.updateAccount(account);
//        LogHelper.log(result.toString());
    }
    
    public void deleteAccountTest() {
        Account account = new Account("test", "test", "testForupdate2", "TEST");
        int result = mAccountProvider.deleteAccount(account.getLogin_id());
//        LogHelper.log(result.toString());
    }
    
    public void addReservationTest() {
        Calendar cal = Calendar.getInstance();
        Reservation reservation = new Reservation();
        reservation.setLogin_id("test");
        reservation.setReservation_time(cal.getTime());
        reservation.setParking_fee(10);
        reservation.setGrace_period(5);
        reservation.setParkinglot_id("parkinglot-01");
        reservation.setArrival_time(cal.getTime());
        reservation.setDeparture_time(cal.getTime());
        reservation.setConfirmation_code("QR Code");
        reservation.setAssigned_slot(01);
        
        boolean result = mReservationProvider.addReservation(reservation);
//        LogHelper.log(result.toString());
    }
}
