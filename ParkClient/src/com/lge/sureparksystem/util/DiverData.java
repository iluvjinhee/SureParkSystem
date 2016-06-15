package com.lge.sureparksystem.util;

public class DiverData {
    // ParkHere to ParkServer
    // loginId : email
    // 1. Login(loginId, pw) : String result
    // 2. reservation_info(loginId, time(ms)) : ParkReservationInfo
    // 3. 2 is false 시  parkingLot_info(loginId) : ParkInfo
    
    // *Reservation 
    // reserve(loginId, parkId, reservation_time(현재 시간 + arrive time). playmentInfo) : String result 
    // *Cancel
    // cancel(logninId, parkId) : String result
    
    class ParkReservationInfo {
        String result;
        String parkId;
        String revervationTime;
        String parkLotAddress;
        String parkFee;
        String gracePeriod;
        String paymentInfo;
        String confirmationInfo;
    }
    
    class ParkInfo {
        String result;
        String parkId;
        String parkLotAddress;
        String parkFee;
        String gracePeriod;
    }
}
