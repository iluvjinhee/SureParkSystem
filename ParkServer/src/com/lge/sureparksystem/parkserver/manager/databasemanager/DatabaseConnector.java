package com.lge.sureparksystem.parkserver.manager.databasemanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    public static final String TAG = DatabaseConnector.class.getSimpleName();
    private static DatabaseConnector mDBConnector = null;
    private static Connection mConn = null;

    private DatabaseConnector() {
        mConn = getDatabaseConnection();
    }

    synchronized public static DatabaseConnector getInstance() {
        if (mDBConnector == null) {
            if (mDBConnector == null) {
                mDBConnector = new DatabaseConnector();
            }
        }
        return mDBConnector;
    }

    public Connection getDatabaseConnection() {
        if (mConn == null) {
            mConn = makeConnection();
        }
        return mConn;
    }

    private static Connection makeConnection() {
        Connection conn = null;
        LogHelper.log(TAG, "");
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/sureparkdb?" +
                    "user=ohteam&password=ohteamchoigo");
//            conn = DriverManager.getConnection("jdbc:mysql://128.237.202.85/sureparkdb?" +
//                    "user=ohteam&password=ohteamchoigo");
            LogHelper.log(TAG, "conn = " + conn.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
