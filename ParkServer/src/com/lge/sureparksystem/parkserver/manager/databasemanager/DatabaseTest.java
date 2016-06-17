package com.lge.sureparksystem.parkserver.manager.databasemanager;

import java.util.Calendar;

public class DatabaseTest {

    DatabaseProvider mDatabaseProvider = DatabaseProvider.getInstance();

    public DatabaseTest() {
        LogHelper.log();
    }

    public void testRun(DatabaseTest test) {
//        isExistigUser_Test();
//        createUserAccount_Test();
//        getUserInfoUser_Test();
//        isValid_Test();
//        getRemoveUserAccount_Test();
//        getUserAuthority_Test();

//        createReservation_Test();
//        getReservationInfo_Test();
//        updateReservationState_Test();
//
//        updateParkingLot_Test();
//        updateParkingLotFee_Test();
//        updateParkingLotGracePeriod_Test();
//        updateParkingLotUserMail_Test();
//
        createParkingData_Test();
//        getParkingInfo_Test();
//        updateParkingInfo_Test();
//        updateParkingParkedSlot_Test();
//        updateParkingUnparkedTime_Test();
    }

    /*************************************************************************************/
    // For User Account
    /*************************************************************************************/
    public void isExistigUser_Test() {
        mDatabaseProvider.isExistingUser("jaedo.jin@lge.com");
    }

    public void createUserAccount_Test() {
        Calendar cal = Calendar.getInstance();
        UserAccountData newuser = new UserAccountData("tester", "tester3@lge.com", "1234567890",
                cal.getTime(), DatabaseInfo.Authority.DRIVER_ID);
        mDatabaseProvider.createUserAccount(newuser);
        newuser.setUsername("tester002");
        mDatabaseProvider.createUserAccount(newuser);
    }

    public void getUserInfoUser_Test() {
        mDatabaseProvider.getUserInfo("jaedo.jin@lge.com");
        mDatabaseProvider.getUserInfo("tester@lge.com");
    }

    public void isValid_Test() {
        mDatabaseProvider.isValidUser("tester@lge.com", "1234567890");
        mDatabaseProvider.isValidUser("tester3@lge.com", "1234567890");
    }

    public void getRemoveUserAccount_Test() {
        mDatabaseProvider.removeUserAccount("tester002");
    }

    public void getUserAuthority_Test() {
        mDatabaseProvider.getUserAuthority("tester@lge.com");
        mDatabaseProvider.getUserAuthority("");
    }

    /*************************************************************************************/
    // For Reservation
    /*************************************************************************************/
    public void createReservation_Test() {
        Calendar cal = Calendar.getInstance();
        UserAccountData newuser = new UserAccountData("tester", "tester@lge.com", "1234567890",
                cal.getTime(), DatabaseInfo.Authority.DRIVER_ID);

        ReservationData reservation = new ReservationData();
        reservation.setUserEmail("tester@lge.com");
        reservation.setReservedTime(cal.getTime());
        reservation.setParkinglotId(13);
        reservation.setPaymentInfo("credit info");
        reservation.setConfirmationCode("QR code");
        reservation.setState(DatabaseInfo.Reservation.STATE_TYPE.RESERVED);
        reservation.setFee("5");
        reservation.setGracePeriod("30");

        mDatabaseProvider.createReservation(reservation);
    }

    public void getReservationInfo_Test() {
        mDatabaseProvider.getReservationInfo("tester@lge.com");
    }

    public void updateReservationState_Test() {
        mDatabaseProvider.updateReservationState(1, 1);
    }

    /*************************************************************************************/
    // For Parking lot
    /*************************************************************************************/
    public void updateParkingLot_Test() {
        mDatabaseProvider.updateParkingLotInfo(13, "1", "10", "tester@lge.com");
    }

    public void updateParkingLotFee_Test() {
        mDatabaseProvider.updateParkingLotFee(14, "10");
    }

    public void updateParkingLotGracePeriod_Test() {
        mDatabaseProvider.updateParkingLotGracePeriod(15, "15");
    }

    public void updateParkingLotUserMail_Test() {
//        Calendar cal = Calendar.getInstance();
//        UserAccountData newuser = new UserAccountData("tester", "tester2@lge.com", "1234567890",
//                cal.getTime(), DatabaseInfo.Authority.DRIVER_ID);
//        mDatabaseProvider.createUserAccount(newuser);
        mDatabaseProvider.updateParkingLotUserEmail(15, "tester2@lge.com");
    }

    /*************************************************************************************/
    // For Parkings
    /*************************************************************************************/
    public void createParkingData_Test() {
        Calendar cal = Calendar.getInstance();
        mDatabaseProvider.createParkingData(24, "1", cal.getTime());
    }

    public void getParkingInfo_Test() {
        mDatabaseProvider.getParkingInfo(11);
    }

    public void updateParkingInfo_Test() {
        Calendar cal = Calendar.getInstance();
        mDatabaseProvider.updateParkingInfo(11, "2", cal.getTime());
    }

    public void updateParkingParkedSlot_Test() {
        mDatabaseProvider.updateParkingParkedSlot(11, "4");
    }

    public void updateParkingUnparkedTime_Test() {
        Calendar cal = Calendar.getInstance();
        mDatabaseProvider.updateParkingUnparkedTime(11, cal.getTime());
    }
}
