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
//        verifyUser_Test();
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
//        createParkingData_Test();
//        getParkingInfo_Test();
//        updateParkingInfo_Test();
//        updateParkingParkedSlot_Test();
        updateParkingUnparkedTime_Test();
    }

    /*************************************************************************************/
    // For User Account
    /*************************************************************************************/
    public void isExistigUser_Test() {
        mDatabaseProvider.isExistingUser("json@lge.com");
    }

    public void createUserAccount_Test() {
        Calendar cal = Calendar.getInstance();
        UserAccountData newuser = new UserAccountData("tester", "tester5@lge.com", "1234567890",
                cal.getTime(), DatabaseInfo.Authority.ID_TYPE.DRIVER);
        mDatabaseProvider.createUserAccount(newuser);
        newuser.setUsername("tester002");
        mDatabaseProvider.createUserAccount(newuser);
    }

    public void getUserInfoUser_Test() {
        mDatabaseProvider.getUserInfo("jaedo.jin@lge.com");
        mDatabaseProvider.getUserInfo("tester@lge.com");
    }

    public void verifyUser_Test() {
        mDatabaseProvider.verifyUser("tester@lge.com", "1234567890");
        mDatabaseProvider.verifyUser("tester3@lge.com", "1234567890");
    }

    public void getRemoveUserAccount_Test() {
        mDatabaseProvider.removeUserAccount("tester@lge.com");
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
                cal.getTime(), DatabaseInfo.Authority.ID_TYPE.DRIVER);

        ReservationData reservation = new ReservationData();
        reservation.setUserEmail("tester2@lge.com");
        reservation.setReservationTime(cal.getTime());
        reservation.setParkinglotId(13);
        reservation.setCreditInfo("credit info");
        reservation.setConfirmInfo("QR code");
        reservation.setParkingFee("5");
        reservation.setGracePeriod("30");
        reservation.setReservationState(DatabaseInfo.Reservation.STATE_TYPE.RESERVED);
        reservation.setPayment(0.0f);

        mDatabaseProvider.createReservation(reservation);
    }

    public void getReservationInfo_Test() {
        mDatabaseProvider.getReservationInfo("tester@lge.com");
        mDatabaseProvider.getReservationInfo("tester2@lge.com");
    }

    public void updateReservationState_Test() {
        mDatabaseProvider.updateReservationState(26, 2);
    }

    /*************************************************************************************/
    // For Parking lot
    /*************************************************************************************/
    public void updateParkingLot_Test() {
        mDatabaseProvider.updateParkingLotInfo(13, "1", "10", "tester2@lge.com");
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
        mDatabaseProvider.createParkingData(26, "1", cal.getTime());
    }

    public void getParkingInfo_Test() {
        mDatabaseProvider.getParkingInfo(26);
    }

    public void updateParkingInfo_Test() {
        Calendar cal = Calendar.getInstance();
        mDatabaseProvider.updateParkingInfo(26, "2", cal.getTime());
    }

    public void updateParkingParkedSlot_Test() {
        mDatabaseProvider.updateParkingParkedSlot(26, "4");
    }

    public void updateParkingUnparkedTime_Test() {
        Calendar cal = Calendar.getInstance();
        mDatabaseProvider.updateParkingUnparkedTime(26, cal.getTime());
    }
}
