package com.lge.sureparksystem.parkserver.manager.databasemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseInfo.Parking;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseInfo.ParkingLot;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseInfo.Reservation;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseInfo.User;
import com.sun.istack.internal.Nullable;

public class DatabaseProvider {

    private static final String TAG = DatabaseProvider.class.getSimpleName();
    private final Object mDataLock = new Object();

    private static DatabaseProvider mDatabaseProvider = null;
    private static Connection mDBConn = null;
    private static DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String encryptionKey = "SureparksystemByOhteam";
    private static int HASH_LENGTH = 256;

    private DatabaseProvider() {
        mDBConn = DatabaseConnector.getInstance().getDatabaseConnection();
    }

    synchronized public static DatabaseProvider getInstance() {
        if (mDatabaseProvider == null) {
            if (mDatabaseProvider == null) {
                mDatabaseProvider = new DatabaseProvider();
            }
        }
        return mDatabaseProvider;
    }

    private String getSqlStringForEncryption(String orgStr) {
        StringBuilder builder = new StringBuilder("AES_ENCRYPT");
        builder.append("('" + orgStr + "', ");
        builder.append("UNHEX(SHA2('" + encryptionKey + "'," + HASH_LENGTH + ")))");
        return builder.toString();
    }

    private int doExecUpdateSQL(String tableName, String setvalue, String whereclause) {
        PreparedStatement pstmt = null;
        int count = -1;
        try {
            String sql = "update " + tableName + setvalue + whereclause;
            LogHelper.log(TAG, "sql = " + sql);

            synchronized (mDataLock) {
                pstmt = mDBConn.prepareStatement(sql);
                count = pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        LogHelper.log(TAG, "count = " + count);
        return count;
    }

    private int doExecInsertSQL(String tableName, String columnsString, String valuesString) {
        PreparedStatement pstmt = null;
        boolean result = false;
        int count = -1;
        try {
            String sql = null;
            if (columnsString != null) {
                sql = "insert into " + tableName + " (" + columnsString + ")" + " values("
                        + valuesString + ")";
            } else {
                sql = "insert into " + tableName + " values(" + valuesString + ")";
            }
            LogHelper.log(TAG, "sql = " + sql);

            synchronized (mDataLock) {
                pstmt = mDBConn.prepareStatement(sql);
                result = pstmt.execute();
                if (result == false) {
                    count = pstmt.getUpdateCount();
                    LogHelper.log(TAG, "count = " + count);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        LogHelper.log(TAG, "count = " + count);
        return count;
    }

    private int doExecGetCountSQL(String tableName, String whereString) {
        int count = -1;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "select  count(*) from " + tableName + whereString;
            LogHelper.log(TAG, "sql = " + sql);
            pstmt = mDBConn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                do {
                    count = rs.getInt(1);
                    LogHelper.log(TAG, "count = " + count);
                } while (rs.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    /*************************************************************************************/
    // For User
    /*************************************************************************************/
    boolean isExistingUser(String email) {
        int count = 0;
        boolean result = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            StringBuilder where = new StringBuilder(" where ");
            where.append(User.Columns.EMAIL + "='" + email + "'");
            String sql = "select  count(*) from " + User.USER_TABLE + where.toString();
            LogHelper.log(TAG, "sql = " + sql);
            pstmt = mDBConn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                do {
                    count = rs.getInt(1);
                } while (rs.next());
            }
        } catch (SQLException e) {
            count = 0;
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        result = (count == 1) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }

    boolean isValidUser(String email, String password) {
        int count = 0;
        boolean result = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            StringBuilder where = new StringBuilder(" where ");
            where.append(User.Columns.EMAIL + "='" + email + "'");
            where.append(" AND " + User.Columns.PW + "=" + getSqlStringForEncryption(password));
            String sql = "select  count(*) from " + User.USER_TABLE + where.toString();
            LogHelper.log(TAG, "sql = " + sql);
            pstmt = mDBConn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                do {
                    count = rs.getInt(1);
                } while (rs.next());
                LogHelper.log(TAG, "count = " + count);
            }
        } catch (SQLException e) {
            count = 0;
            e.printStackTrace();
        } catch (NullPointerException ex) {
            count = 0;
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        result = (count == 1) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }

    boolean createUserAccount(UserAccountData newuser) {
        if (isExistingUser(newuser.getEmail())) {
            LogHelper.log(TAG, "Error : email is alreday exist.");
            return false;
        }
        int count = 0;
        boolean result = false;
        PreparedStatement pstmt = null;
        try {
            StringBuilder values = new StringBuilder();
            values.append("'" + newuser.getUsername() + "'");
            values.append(", '" + newuser.getEmail() + "'");
            values.append(", " + getSqlStringForEncryption(newuser.getPassword()));
            values.append(", '" + mDateFormat.format(newuser.getCreateTime()) + "'");
            values.append(", " + newuser.getAuthorityId());

            String sql = "insert into " + User.USER_TABLE + " values(" + values.toString() + ")";
            LogHelper.log(TAG, "sql = " + sql);

            synchronized (mDataLock) {
                pstmt = mDBConn.prepareStatement(sql);
                result = pstmt.execute();
                if (result == false) {
                    count = pstmt.getUpdateCount();
                    LogHelper.log(TAG, "count = " + count);
                }
            }
        } catch (SQLException e) {
            count = 0;
            e.printStackTrace();
        } catch (NullPointerException ex) {
            count = 0;
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        result = (count == 1) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }

    //if not exist, return -1
    public int getUserAuthority(String email) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int userAuthority = -1;
        try {
            StringBuilder where = new StringBuilder(" where ");
            where.append(User.Columns.EMAIL + "='" + email + "'");
            String sql = "select " + User.Columns.AUTHORITY + " from " + User.USER_TABLE
                    + where.toString();
            LogHelper.log(TAG, "sql = " + sql);
            pstmt = mDBConn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                userAuthority = rs.getInt(User.Columns.AUTHORITY);
                if (rs.next()) {
                    LogHelper.log(TAG, "Warning :: result is not one.");
                }
            } else {
                LogHelper.log(TAG, "matched user is not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        LogHelper.log(TAG, "userAuthority = " + userAuthority);
        return userAuthority;
    }

    //if not exist, return null
    @Nullable
    public UserAccountData getUserInfo(String email) {
        UserAccountData account = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            StringBuilder where = new StringBuilder(" where ");
            where.append(User.Columns.EMAIL + "='" + email + "'");
            String sql = "select * from " + User.USER_TABLE + where.toString();
            LogHelper.log(TAG, "sql = " + sql);
            pstmt = mDBConn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                account = new UserAccountData();
                account.setEmail(email);
                account.setUsername(rs.getString(User.Columns.USERNAME));
                //                account.setPassword(rs.getString(User.Columns.PW)); //Do not expose password
                try {
                    account.setCreateTime(mDateFormat.parse(rs.getString(User.Columns.TIME)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                account.setAuthorityId(rs.getInt(User.Columns.AUTHORITY));

                LogHelper.log(TAG, "acount name = " + account.toString());
                if (rs.next()) {
                    LogHelper.log(TAG, "Warning :: result is not one.");
                }
            } else {
                LogHelper.log(TAG, "matched user is not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return account;
    }

    public boolean removeUserAccount(String email) {
        if (isExistingUser(email)) {
            LogHelper.log(TAG, "Error : email is alreday exist.");
            return false;
        }
        PreparedStatement pstmt = null;
        boolean result = false;
        int count = 0;
        try {
            StringBuilder where = new StringBuilder(" where ");
            where.append(User.Columns.EMAIL + "='" + email + "'");
            String sql = "delete from " + User.USER_TABLE + where.toString();
            LogHelper.log(TAG, "sql = " + sql);

            synchronized (mDataLock) {
                pstmt = mDBConn.prepareStatement(sql);
                result = pstmt.execute();
                if (result == false) {
                    count = pstmt.getUpdateCount();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        result = (count == 1) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }

    /*************************************************************************************/
    // For Reservation
    /*************************************************************************************/
    public boolean createReservation(ReservationData newreservation) {

        PreparedStatement pstmt = null;
        boolean result = false;
        int count = 0;
        try {
            StringBuilder columns = new StringBuilder();
            columns.append(Reservation.Columns.USER_EMAIL);
            columns.append(", " + Reservation.Columns.RESERVED_TIME);
            columns.append(", " + Reservation.Columns.LOT_ID);
            columns.append(", " + Reservation.Columns.PAYMENT);
            columns.append(", " + Reservation.Columns.CONFIRM_CODE);
            columns.append(", " + Reservation.Columns.STATE);
            columns.append(", " + Reservation.Columns.FEE);
            columns.append(", " + Reservation.Columns.GRACE_PERIOD);

            StringBuilder values = new StringBuilder();
            values.append("'" + newreservation.getUserEmail() + "'");
            values.append(", '" + mDateFormat.format(newreservation.getReservedTime()) + "'");
            values.append(", " + newreservation.getParkinglotId());
            values.append(", '" + newreservation.getPaymentInfo() + "'");
            values.append(", '" + newreservation.getConfirmationCode() + "'");
            values.append(", " + newreservation.getState());
            values.append(", '" + newreservation.getFee() + "'");
            values.append(", '" + newreservation.getGracePeriod() + "'");

            String sql = "insert into " + Reservation.RESERVATION_TABLE + " (" + columns.toString()
                    + ")" + " values(" + values.toString() + ")";
            LogHelper.log(TAG, "sql = " + sql);

            synchronized (mDataLock) {
                pstmt = mDBConn.prepareStatement(sql);
                result = pstmt.execute();
                if (result == false) {
                    count = pstmt.getUpdateCount();
                    LogHelper.log(TAG, "count = " + count);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        result = (count == 1) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }

    //if not exist, size of list is zero
    public List<ReservationData> getReservationInfo(String email) {
        List<ReservationData> reservationList = new ArrayList<ReservationData>();
        ReservationData reservation = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            StringBuilder where = new StringBuilder(" where ");
            where.append(Reservation.Columns.USER_EMAIL + "='" + email + "' AND ");
            where.append("(" + Reservation.Columns.STATE + "=" + Reservation.STATE_TYPE.RESERVED);
            where.append(
                    " OR " + Reservation.Columns.STATE + "=" + Reservation.STATE_TYPE.PARKED + ")");
            String sql = "select * from " + Reservation.RESERVATION_TABLE + where.toString();
            LogHelper.log(TAG, "sql = " + sql);
            pstmt = mDBConn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                do {
                    reservation = new ReservationData();
                    reservation.setId(rs.getInt(Reservation.Columns.ID));
                    reservation.setUserEmail(rs.getString(Reservation.Columns.USER_EMAIL));
                    try {
                        reservation.setReservedTime(
                                mDateFormat.parse(rs.getString(Reservation.Columns.RESERVED_TIME)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    reservation.setParkinglotId(rs.getInt(Reservation.Columns.LOT_ID));
                    reservation.setPaymentInfo(rs.getString(Reservation.Columns.PAYMENT));
                    reservation.setConfirmationCode(rs.getString(Reservation.Columns.CONFIRM_CODE));
                    reservation.setState(rs.getInt(Reservation.Columns.STATE));
                    reservation.setFee(rs.getString(Reservation.Columns.FEE));
                    reservation.setGracePeriod(rs.getString(Reservation.Columns.GRACE_PERIOD));
                    reservationList.add(reservation);
                } while (rs.next());
                LogHelper.log(TAG, "reservation = " + reservation.toString());
            } else {
                LogHelper.log(TAG, "matched reservation is not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        LogHelper.log(TAG, "size of reservation list  = " + reservationList.size());
        return reservationList;
    }

    public int updateReservationState(int id, int state) {
        PreparedStatement pstmt = null;
        int count = 0;
        try {
            StringBuilder set = new StringBuilder(" set ");
            set.append(Reservation.Columns.STATE + "=" + state);

            StringBuilder where = new StringBuilder(" where ");
            where.append(Reservation.Columns.ID + "=" + id);

            String sql = "update " + Reservation.RESERVATION_TABLE + set.toString()
                    + where.toString();
            LogHelper.log(TAG, "sql = " + sql);

            synchronized (mDataLock) {
                pstmt = mDBConn.prepareStatement(sql);
                count = pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        LogHelper.log(TAG, "updated count = " + count);
        return count;
    }

    /*************************************************************************************/
    // For Parking Lot
    /*************************************************************************************/
    public boolean isValidParkingLot(String loginId, String password) {
        int count = 0;
        boolean result = false;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            StringBuilder where = new StringBuilder(" where ");
            where.append(ParkingLot.Columns.LOGINID + "='" + loginId + "'");
            where.append(" AND " + ParkingLot.Columns.LOGINPW + "='" + password + "'");
            String sql = "select  count(*) from " + ParkingLot.PARKINGLOT_TABLE + where.toString();
            LogHelper.log(TAG, "sql = " + sql);
            pstmt = mDBConn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                do {
                    count = rs.getInt(1);
                } while (rs.next());
                LogHelper.log(TAG, "count = " + count);
            }
        } catch (SQLException e) {
            count = 0;
            e.printStackTrace();
        } catch (NullPointerException ex) {
            count = 0;
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        result = (count == 1) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }

    boolean createParkingLot(ParkingLotData newlot) {
        if (isValidParkingLot(newlot.getLoginId(), newlot.getLoginPw())) {
            LogHelper.log(TAG, "Error : Alreay is exist.");
            return false;
        }
        int count = 0;
        boolean result = false;
        PreparedStatement pstmt = null;
        try {

            StringBuilder columns = new StringBuilder();
            columns.append(ParkingLot.Columns.LOGINID);
            columns.append(", " + ParkingLot.Columns.LOGINPW);
            columns.append(", " + ParkingLot.Columns.NAME);
            columns.append(", " + ParkingLot.Columns.FEE);
            columns.append(", " + ParkingLot.Columns.GRACEPERIOD);
            columns.append(", " + ParkingLot.Columns.USEREMAIL);

            StringBuilder values = new StringBuilder();
            values.append("'" + newlot.getLoginId() + "'");
            values.append(", " + getSqlStringForEncryption(newlot.getLoginPw()));
            values.append(", '" + newlot.getLotName() + "'");
            values.append(", '" + newlot.getFee() + "'");
            values.append(", '" + newlot.getGracePeriod() + "'");
            values.append(", '" + newlot.getUserEmail() + "'");

            String sql = "insert into " + Reservation.RESERVATION_TABLE + " (" + columns.toString()
                    + ")" + " values(" + values.toString() + ")";
            LogHelper.log(TAG, "sql = " + sql);

            synchronized (mDataLock) {
                pstmt = mDBConn.prepareStatement(sql);
                result = pstmt.execute();
                if (result == false) {
                    count = pstmt.getUpdateCount();
                    LogHelper.log(TAG, "count = " + count);
                }
            }
        } catch (SQLException e) {
            count = 0;
            e.printStackTrace();
        } catch (NullPointerException ex) {
            count = 0;
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        result = (count == 1) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }

    //if not exist, size of list is zero
    public List<ParkingLotData> getAllParkingLotInfo() {
        List<ParkingLotData> parkingLotDataList = new ArrayList<ParkingLotData>();
        ParkingLotData parkinglot = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "select * from " + ParkingLot.PARKINGLOT_TABLE;
            LogHelper.log(TAG, "sql = " + sql);
            pstmt = mDBConn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                do {
                    parkinglot = new ParkingLotData();
                    parkinglot.setId(rs.getInt(ParkingLot.Columns.ID));
                    parkinglot.setLoginId(rs.getString(ParkingLot.Columns.LOGINID));
                    //                    parkinglot.setLoginPw(rs.getString(ParkingLot.Columns.LOGINPW));  //Do not expose
                    parkinglot.setLotName(rs.getString(ParkingLot.Columns.NAME));
                    parkinglot.setFee(rs.getString(ParkingLot.Columns.FEE));
                    parkinglot.setGracePeriod(rs.getString(ParkingLot.Columns.GRACEPERIOD));
                    parkinglot.setUserEmail(rs.getString(ParkingLot.Columns.USEREMAIL));

                    parkingLotDataList.add(parkinglot);
                } while (rs.next());
                LogHelper.log(TAG, "reservation = " + parkinglot.toString());
            } else {
                LogHelper.log(TAG, "parkinglot is not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        LogHelper.log(TAG, "size of ParkingLot list  = " + parkingLotDataList.size());
        return parkingLotDataList;
    }

    //if not exist, return null
    @Nullable
    public ParkingLotData getParkingLotInfo(int id) {
        ParkingLotData parkinglot = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            StringBuilder where = new StringBuilder(" where ");
            where.append(ParkingLot.Columns.ID + "=" + id);

            String sql = "select * from " + Reservation.RESERVATION_TABLE + where.toString();
            LogHelper.log(TAG, "sql = " + sql);
            pstmt = mDBConn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                parkinglot = new ParkingLotData();
                parkinglot.setId(rs.getInt(ParkingLot.Columns.ID));
                parkinglot.setLoginId(rs.getString(ParkingLot.Columns.LOGINID));
                //                parkinglot.setLoginPw(rs.getString(ParkingLot.Columns.LOGINPW));      //Do not expose
                parkinglot.setLotName(rs.getString(ParkingLot.Columns.NAME));
                parkinglot.setFee(rs.getString(ParkingLot.Columns.FEE));
                parkinglot.setGracePeriod(rs.getString(ParkingLot.Columns.GRACEPERIOD));
                parkinglot.setUserEmail(rs.getString(ParkingLot.Columns.USEREMAIL));

                if (rs.next()) {
                    LogHelper.log(TAG, "Warning :: result is not one.");
                }
                LogHelper.log(TAG, "reservation = " + parkinglot.toString());
            } else {
                LogHelper.log(TAG, "matched reservation is not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return parkinglot;
    }

    public boolean removeParkingLot(int id) {
        PreparedStatement pstmt = null;
        boolean result = false;
        int count = 0;
        try {
            StringBuilder where = new StringBuilder(" where ");
            where.append(ParkingLot.Columns.ID + "=" + id);
            String sql = "delete from " + ParkingLot.PARKINGLOT_TABLE + where.toString();
            LogHelper.log(TAG, "sql = " + sql);

            synchronized (mDataLock) {
                pstmt = mDBConn.prepareStatement(sql);
                result = pstmt.execute();
                if (result == false) {
                    count = pstmt.getUpdateCount();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        result = (count == 1) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }

    public boolean updateParkingLotFee(int id, String fee) {
        boolean result = false;
        int count = 0;

        StringBuilder set = new StringBuilder(" set ");
        set.append(ParkingLot.Columns.FEE + "='" + fee + "'");

        StringBuilder where = new StringBuilder(" where ");
        where.append(ParkingLot.Columns.ID + "=" + id);

        count = doExecUpdateSQL(ParkingLot.PARKINGLOT_TABLE, set.toString(), where.toString());

        result = (count == 1) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }

    public boolean updateParkingLotGracePeriod(int id, String gracePeriod) {
        boolean result = false;
        int count = 0;

        StringBuilder set = new StringBuilder(" set ");
        set.append(ParkingLot.Columns.GRACEPERIOD + "='" + gracePeriod + "'");

        StringBuilder where = new StringBuilder(" where ");
        where.append(ParkingLot.Columns.ID + "=" + id);

        count = doExecUpdateSQL(ParkingLot.PARKINGLOT_TABLE, set.toString(), where.toString());

        result = (count == 1) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }

    public boolean updateParkingLotUserEmail(int id, String email) {
        boolean result = false;
        int count = 0;

        StringBuilder set = new StringBuilder(" set ");
        set.append(ParkingLot.Columns.USEREMAIL + "='" + email + "'");

        StringBuilder where = new StringBuilder(" where ");
        where.append(ParkingLot.Columns.ID + "=" + id);

        count = doExecUpdateSQL(ParkingLot.PARKINGLOT_TABLE, set.toString(), where.toString());

        result = (count == 1) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }

    public boolean updateParkingLotInfo(int id, String fee, String gracePeriod) {
        boolean result = false;
        int count = 0;

        StringBuilder set = new StringBuilder(" set ");
        set.append(ParkingLot.Columns.FEE + "='" + fee + "'");
        set.append(", " + ParkingLot.Columns.GRACEPERIOD + "='" + gracePeriod + "'");

        StringBuilder where = new StringBuilder(" where ");
        where.append(ParkingLot.Columns.ID + "=" + id);

        count = doExecUpdateSQL(ParkingLot.PARKINGLOT_TABLE, set.toString(), where.toString());

        result = (count == 1) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }

    public boolean updateParkingLotInfo(int id, String fee, String gracePeriod, String email) {
        boolean result = false;
        int count = 0;

        StringBuilder set = new StringBuilder(" set ");
        set.append(ParkingLot.Columns.FEE + "='" + fee + "'");
        set.append(", " + ParkingLot.Columns.GRACEPERIOD + "='" + gracePeriod + "'");
        set.append(", " + ParkingLot.Columns.USEREMAIL + "='" + email + "'");

        StringBuilder where = new StringBuilder(" where ");
        where.append(ParkingLot.Columns.ID + "=" + id);

        count = doExecUpdateSQL(ParkingLot.PARKINGLOT_TABLE, set.toString(), where.toString());

        result = (count == 1) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }

    /*************************************************************************************/
    // For Parking
    /*************************************************************************************/
    public boolean isExistingParkingData(int reservationId) {
        int count = 0;
        boolean result = false;
        StringBuilder where = new StringBuilder(" where ");
        where.append(Parking.Columns.RSERVATION_ID + "=" + reservationId);

        count = doExecGetCountSQL(Parking.PARKING_TABLE, where.toString());
        LogHelper.log(TAG, "count = " + count);

        result = (count > 0) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }

    public boolean createParkingData(ParkingData newparking) {
        if (isExistingParkingData(newparking.getReservationId())) {
            LogHelper.log(TAG, "Error : Alreay is exist.");
            return false;
        }
        boolean result = false;
        int count = 0;
        StringBuilder columns = new StringBuilder();
        columns.append(Parking.Columns.RSERVATION_ID);
        columns.append(", " + Parking.Columns.ASSIGNED_SLOT);
        columns.append(", " + Parking.Columns.PARKED_SLOT);
        columns.append(", " + Parking.Columns.PARKING_TIME);
        columns.append(", " + Parking.Columns.UNPARKING_TIME);

        StringBuilder values = new StringBuilder();
        values.append(newparking.getReservationId());
        values.append(", '" + newparking.getAssigned_slot() + "'");
        values.append(", '" + newparking.getParked_slot() + "'");
        values.append(", '" + mDateFormat.format(newparking.getParkingTime()) + "'");
        values.append(", '" + mDateFormat.format(newparking.getUnparkingTime()) + "'");

        count = doExecInsertSQL(Parking.PARKING_TABLE, columns.toString(), values.toString());

        result = (count == 1) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }

    public boolean createParkingData(int reservationId, String assignedSlot, Date parkingTime) {
        if (isExistingParkingData(reservationId)) {
            LogHelper.log(TAG, "Error : Alreay is exist.");
            return false;
        }
        boolean result = false;
        int count = 0;
        StringBuilder columns = new StringBuilder();
        columns.append(Parking.Columns.RSERVATION_ID);
        columns.append(", " + Parking.Columns.ASSIGNED_SLOT);
        columns.append(", " + Parking.Columns.PARKING_TIME);

        StringBuilder values = new StringBuilder();
        values.append(reservationId);
        values.append(", '" + assignedSlot + "'");
        values.append(", '" + mDateFormat.format(parkingTime) + "'");

        count = doExecInsertSQL(Parking.PARKING_TABLE, columns.toString(), values.toString());

        result = (count == 1) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }

    //if not exist, return null
    @Nullable
    public ParkingData getParkingInfo(int parkingId) {
        ParkingData parking = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            StringBuilder where = new StringBuilder(" where ");
            where.append(Parking.Columns.ID + "=" + parkingId);

            String sql = "select * from " + Parking.PARKING_TABLE + where.toString();
            LogHelper.log(TAG, "sql = " + sql);
            pstmt = mDBConn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                parking = new ParkingData();
                parking.setParkingId(rs.getInt(Parking.Columns.ID));
                parking.setReservationId(rs.getInt(Parking.Columns.RSERVATION_ID));
                parking.setAssigned_slot(rs.getString(Parking.Columns.ASSIGNED_SLOT));
                parking.setParked_slot(rs.getString(Parking.Columns.PARKED_SLOT));
                try {
                    String time = rs.getString(Parking.Columns.PARKING_TIME);
                    if (time != null) {
                        parking.setParkingTime(mDateFormat.parse(time));
                    }
                    time = rs.getString(Parking.Columns.UNPARKING_TIME);
                    if (time != null) {
                        parking.setUnparkingTime(mDateFormat.parse(time));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }

                if (rs.next()) {
                    LogHelper.log(TAG, "Warning :: result is not one.");
                }
                LogHelper.log(TAG, "reservation = " + parking.toString());
            } else {
                LogHelper.log(TAG, "matched reservation is not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return parking;
    }

    public boolean updateParkingParkedSlot(int paringId, String parkedSlot) {
        return updateParkingInfo(paringId, parkedSlot, null);
    }

    public boolean updateParkingUnparkedTime(int paringId, Date unparkingTime) {
        return updateParkingInfo(paringId, null, unparkingTime);
    }

    public boolean updateParkingInfo(int paringId, String parkedSlot, Date unparkingTime) {
        boolean result = false;
        int count = 0;
        int valueCnt = 0;
        StringBuilder set = new StringBuilder(" set ");
        if (parkedSlot != null) {
            set.append(Parking.Columns.PARKED_SLOT + "='" + parkedSlot + "'");
            valueCnt++;
        }

        if (unparkingTime != null) {
            if (valueCnt > 0) {
                set.append(", ");
            }
            set.append(Parking.Columns.UNPARKING_TIME + "='" + mDateFormat.format(unparkingTime)
                    + "'");
        }

        StringBuilder where = new StringBuilder(" where ");
        where.append(Parking.Columns.ID + "=" + paringId);

        count = doExecUpdateSQL(Parking.PARKING_TABLE, set.toString(), where.toString());

        result = (count == 1) ? true : false;
        LogHelper.log(TAG, "result = " + result);
        return result;
    }
}
