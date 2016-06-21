package com.lge.sureparksystem.parkserver.manager.databasemanager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.lge.sureparksystem.parkserver.util.Logger;

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
        //        updateParkingUnparkedTime_Test();

        //        getChangingHistory_Test();

        //        ucreateOccupancyRatePerHour_Test();

        //        createStatisticsData();
        //        updateDailyStatisticsInfo_Test();
        getStatisticsInfo_Test();
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
        reservation.setParkinglotId("13");
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
        mDatabaseProvider.updateParkingLotInfo("13", "1", "10", "tester2@lge.com");
    }

    public void updateParkingLotFee_Test() {
        mDatabaseProvider.updateParkingLotFee("14", "10");
    }

    public void updateParkingLotGracePeriod_Test() {
        mDatabaseProvider.updateParkingLotGracePeriod("15", "15");
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

    /*************************************************************************************/
    //  For Changing History
    /*************************************************************************************/
    public void getChangingHistory_Test() {
        Calendar cal = Calendar.getInstance();
        Date endTime = cal.getTime();
        LogHelper.log("endTime is " + endTime);

        long startTime = cal.getTimeInMillis() - javax.management.timer.Timer.ONE_WEEK;
        cal.setTimeInMillis(startTime);
        LogHelper.log("startTime is " + cal.getTime());
        String parkinglotId = mDatabaseProvider.getParkingLotList().get(0);
        mDatabaseProvider.getChangingHistory(parkinglotId, cal.getTime(), endTime, 1);
        mDatabaseProvider.getChangingHistory(parkinglotId, cal.getTime(), endTime, 2);
    }

    /*************************************************************************************/
    //  For Occupancy rate
    /*************************************************************************************/
    public void ucreateOccupancyRatePerHour_Test() {
        Calendar cal = Calendar.getInstance();
        LogHelper.log("Time is " + cal.getTime());
        mDatabaseProvider.createOccupancyRatePerHour(cal.getTime(), "13", 50);
    }

    /*************************************************************************************/
    //  For Statistics rate
    /*************************************************************************************/
    ArrayList<String> mDriverList = new ArrayList<String>();
    ArrayList<String> mParkingLotId = new ArrayList<String>();
    ArrayList<Integer> mParkingState = new ArrayList<Integer>();

    public void updateDailyStatisticsInfo_Test() {
        Calendar cal = Calendar.getInstance();
        cal.set(2014, 6, 21, 0, 0, 0);
        mDatabaseProvider.updateDailyStatisticsInfo(cal.getTime());
    }

    public void getStatisticsInfo_Test() {
        Calendar cal = Calendar.getInstance();
        String parkinglotId = mDatabaseProvider.getParkingLotList().get(0);
        cal.set(2014, 6, 21, 0, 0, 0);
        Date startTime = cal.getTime();
        cal.set(2014, 12, 21, 0, 0, 0);
        Date endTime = cal.getTime();
        mDatabaseProvider.getStatisticsInfo(parkinglotId, startTime, endTime);
    }

    public void createStatisticsData() {
        generateInitDataSet();
        generateUserData();
        generateReservationData();
        generateOccupancyRatePerHourData();
        generateChangingHitoryData();
        generateStatisticsInfoData();
    }

    private void generateStatisticsInfoData() {
        Calendar cal = Calendar.getInstance();
        for (int year = 2015; year < 2017; year++) {
            for (int month = 0; month < 12; month++) {
                for (int day = 0; day < 29; day++) {
                    cal.set(year, month, day, 0, 0, 0);
                    mDatabaseProvider.updateDailyStatisticsInfo(cal.getTime());
                }
            }
        }
    }

    private void generateChangingHitoryData() {
        Calendar cal = Calendar.getInstance();
        Random random = new Random();

        ChangingHistoryData historyData = new ChangingHistoryData();
        for (String parkinglot : mParkingLotId) {
            for (int type = 1; type < 3; type++) {
                for (int year = 2016; year > 2014; year--) {
                    for (int month = 0; month < 12; month++) {
                        for (int time = 0; time < 5; time += 3) {
                            int date = random.nextInt(31);
                            cal.set(year, month, date, 0, 0, 0);
                            int value = 0;
                            if (type == 1) { //fee
                                value = random.nextInt(100);
                            } else { //grace period
                                value = random.nextInt(6) * 15;
                            }
                            historyData.setParkinglotId(parkinglot);
                            historyData.setChangedTime(cal.getTime());
                            historyData.setChangedType(type);
                            historyData.setChangedValue(String.valueOf(value));

                            mDatabaseProvider.createChangingHistoryData(historyData);
                        }
                    }
                }
            }
        }
    }

    private void generateOccupancyRatePerHourData() {
        Calendar cal = Calendar.getInstance();
        Random random = new Random();

        for (String parkinglot : mParkingLotId) {
            for (int year = 2015; year < 2017; year++) {
                for (int month = 0; month < 12; month++) {
                    for (int date = 0; date < 29; date += 3) {
                        for (int hourOfDay = 0; hourOfDay < 24; hourOfDay++) {
                            cal.set(year, month, date, hourOfDay, 0, 0);
                            int rate = random.nextInt(100);
                            mDatabaseProvider.createOccupancyRatePerHour(cal.getTime(), parkinglot,
                                    (float)rate);
                        }
                    }
                }
            }
        }
    }

    private void generateReservationData() {
        Calendar cal = Calendar.getInstance();
        Random random = new Random();

        ReservationData reservation = new ReservationData();
        for (String driver : mDriverList) {
            for (String parkinglot : mParkingLotId) {
                for (int state = 1; state < 5; state++) {
                    for (int year = 2015; year < 2017; year++) {
                        for (int month = 0; month < 12; month++) {
                            for (int time = 0; time < 10; time++) {
                                reservation.setUserEmail(driver);
                                int date = random.nextInt(29);
                                int hourOfDay = random.nextInt(24);
                                int minute = random.nextInt(60);
                                int second = random.nextInt(60);
                                cal.set(year, month, date, hourOfDay, minute, second);
                                reservation.setReservationTime(cal.getTime());
                                reservation.setParkinglotId(parkinglot);
                                reservation.setCreditInfo("credit info");
                                reservation.setConfirmInfo("QR code");
                                int fee = random.nextInt(100);
                                reservation.setParkingFee(String.valueOf(fee));
                                int period = state * 15;
                                reservation.setGracePeriod(String.valueOf(period));
                                reservation.setReservationState(state);
                                float payment = random.nextInt(1000);
                                reservation.setPayment(payment);
                                mDatabaseProvider.createReservation(reservation);
                            }

                        }
                    }
                }
            }
        }

    }

    private void generateUserData() {
        Calendar cal = Calendar.getInstance();

        UserAccountData newuser = new UserAccountData("Tony", "antony@cmu.edu", "1234567890",
                cal.getTime(), DatabaseInfo.Authority.ID_TYPE.OWNER);
        mDatabaseProvider.createUserAccount(newuser);
        newuser = new UserAccountData("daedon", "daedon.jeon@lge.com", "1234567891",
                cal.getTime(), DatabaseInfo.Authority.ID_TYPE.ATTENDANT);
        mDatabaseProvider.createUserAccount(newuser);
        newuser = new UserAccountData("sanghee", mDriverList.get(0), "1234567892",
                cal.getTime(), DatabaseInfo.Authority.ID_TYPE.DRIVER);
        mDatabaseProvider.createUserAccount(newuser);
        newuser = new UserAccountData("yongchul", mDriverList.get(1), "1234567893",
                cal.getTime(), DatabaseInfo.Authority.ID_TYPE.DRIVER);
        mDatabaseProvider.createUserAccount(newuser);
        newuser = new UserAccountData("kimoon", mDriverList.get(2), "1234567894",
                cal.getTime(), DatabaseInfo.Authority.ID_TYPE.DRIVER);
        mDatabaseProvider.createUserAccount(newuser);
        newuser = new UserAccountData("jin", mDriverList.get(3), "1234567895",
                cal.getTime(), DatabaseInfo.Authority.ID_TYPE.DRIVER);
        mDatabaseProvider.createUserAccount(newuser);
    }

    private void generateInitDataSet() {
        mParkingLotId.add("SP001");
        mParkingLotId.add("SP002");
        mParkingLotId.add("SP003");

        mDriverList.add("sanghee3.lee@lge.com");
        mDriverList.add("yongchul.park@lge.com");
        mDriverList.add("kimoon.lee@lge.com");
        mDriverList.add("jaedo.jin@lge.com");

        mParkingState.add(1);

    }
}
