package com.lge.sureparksystem.parkserver.manager.databasemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ReservationProvider {
    private static final String TAG = ReservationProvider.class.getSimpleName();
    private static final String TABLE_NAME = "reservation_info";
    private static final String LOGIN_ID = "login_id";

    private static ReservationProvider mReservationProvider = null;
    private static Connection mDBConn = null;

    public ReservationProvider() {
        mDBConn = DatabaseConnector.getInstance().getDatabaseConnection();
    }

    synchronized public static ReservationProvider getInstance() {
        if (mReservationProvider == null) {
            if (mReservationProvider == null) {
                mReservationProvider = new ReservationProvider();
            }
        }
        return mReservationProvider;
    }

    public boolean addReservation(Reservation reservation) {

        PreparedStatement pstmt = null;
        boolean result = false;
        int count = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            StringBuilder values = new StringBuilder();
            values.append("'" + reservation.getLogin_id() + "'");
            values.append(", '" + dateFormat.format(reservation.getReservation_time()) + "'");
            values.append(", '" + reservation.getParkinglot_id() + "'");
            values.append(", " + reservation.getParking_fee());
            values.append(", " + reservation.getGrace_period());
            values.append(", '" + dateFormat.format(reservation.getArrival_time()) + "'");
            values.append(", '" + dateFormat.format(reservation.getDeparture_time()) + "'");
            values.append(", '" + reservation.getConfirmation_code() + "'");
            values.append(", " + reservation.getAssigned_slot());
            String sql = "insert into " + TABLE_NAME + " values(" + values.toString() + ")";
            LogHelper.log(TAG, "sql = " + sql);
            pstmt = mDBConn.prepareStatement(sql);
            result = pstmt.execute();
            if (result == false) {
                count = pstmt.getUpdateCount();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        LogHelper.log(TAG, "result count = " + count);
        return count == 1;
    }
}
