package com.lge.sureparksystem.util;

import android.content.Context;
import android.widget.Toast;

public class Utils {
    public static final String TAG_OWNER_FRAGMENT = "OwnerFragment";
    public static final String TAG_ATTENDANT_FRAGMENT = "AttendantFragment";
    public static final String TAG_DRIVER_FRAGMENT = "DriverFragment";
    public static final String TAG_LOGIN_FRAGMENT = "LoginFragment";
    public static final String TAG_CREATE_FRAGMENT_DIALOG = "CreateUserFragment";

    public static final int LOGINVIEW_FRAGMENT = 0;
    public static final int OWNERVIEWFRAGMENT = 1;
    public static final int ATTENDANTVIEW_FRAGMENT = 2;
    public static final int DRIVERVIEW_FRAGMENT = 3;

    // Bundle info
    public static final String IS_RERSERVED = "already_reserved";
    // public static final String IP_ADDRESS = "localhost";
    public static final String IP_ADDRESS = "192.168.43.152";
    public static final int PORT = 9899;

    public static void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void getQRImage(String codeurl) {
//        try {
//            QRCodeWriter qrCodeWriter = new QRCodeWriter();
//            BitMatrix bitMatrix = qrCodeWriter.encode(codeurl, BarcodeFormat.QR_CODE, 200, 200);
//            MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(0xFF2e4e96, 0xFFFFFFFF);
//            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);
//            ImageIO.write(bufferedImage, "png", new File("C:\\qrcode.png"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
