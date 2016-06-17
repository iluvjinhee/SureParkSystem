package com.lge.sureparksystem.model;

public class LoginModel implements BaseModel {
    // ParkHere to ParkServer
    // authority number : owner 1, attendant 2, driver 3
    // 1. createAccount(Email, pw, createTime(ms), authority) : String result
    class CreateAccount {
        String result;
    }
}
