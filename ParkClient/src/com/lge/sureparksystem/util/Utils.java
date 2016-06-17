package com.lge.sureparksystem.util;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static final String TAG_OWNER_FRAGMENT = "OwnerFragment";
    public static final String TAG_ATTENDANT_FRAGMENT = "AttendantFragment";
    public static final String TAG_DRIVER_FRAGMENT = "DriverFragment";
    public static final String TAG_LOGIN_FRAGMENT = "LoginFragment";
    public static final String TAG_CREATE_FRAGMENT_DIALOG = "CreateUserFragment";
    
    public static final String IP_ADDRESS = "192.168.1.183";
    public static final int PORT = 9898;
    
    public static void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    
}
