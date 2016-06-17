package com.lge.sureparksystem.model;


public class DriverModel implements BaseModel {
    // ParkHere to ParkServer
    // loginId : email
    // 1. Login(loginId, pw) : String result
    // 2. reservation_info(loginId, time(ms)) : ParkReservationInfo
    // 3. 2 is false 시  parkingLot_info(loginId) : ParkInfo
    
    // *Reservation 
    // reserve(loginId, parkId, reservation_time(현재 시간 + arrive time). playmentInfo) : String result, confirmation
    // *Cancel
    // cancel(reservationID) : String result
    
    class ParkReservationInfo {
        String result;
        String parkId;
        String revervationTime;
        String parkLotAddress;
        String parkFee;
        String gracePeriod;
        String paymentInfo;
        String confirmationInfo;
        String reservationID;
    }
    
    class ParkInfo {
        String result;
        String parkId;
        String parkLotAddress;
        String parkFee;
        String gracePeriod;
    }

    public void getQRImage() {
        try {
            String codeurl = new String("Shi Dda Yo!");

//            QRCodeWriter qrCodeWriter = new QRCodeWriter();
//            BitMatrix bitMatrix = qrCodeWriter.encode(codeurl,
//                    BarcodeFormat.QR_CODE, 200, 200);
//
//            MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(
//                    0xFF2e4e96, 0xFFFFFFFF);
//            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(
//                    bitMatrix, matrixToImageConfig);
//
//            ImageIO.write(bufferedImage, "png", new File("C:\\qrcode.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
