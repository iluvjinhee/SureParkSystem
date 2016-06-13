package com.lge.sureparksystem.parkserver.databasemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountProvider {
    private static final String TAG = AccountProvider.class.getSimpleName();
    private static final String TABLE_NAME = "account_info";
    private static final String LOGIN_ID = "login_id";
    private static final String LOGIN_PW = "login_pw";
    private static final String NAME = "name";
    private static final String AUTHORITY = "authority";
    private static final String CREDIT_NUMBER = "credit_number";
    private static final String CREDIT_NAME = "credit_name";
    private static final String CREDIT_EXPIRED_DATE = "credit_expired";

    private static AccountProvider mAccountProvider = null;
    private static Connection mDBConn = null;

    private AccountProvider() {
        mDBConn = DatabaseConnector.getInstance().getDatabaseConnection();
    }

    synchronized public static AccountProvider getInstance() {
        if (mAccountProvider == null) {
            if (mAccountProvider == null) {
                mAccountProvider = new AccountProvider();
            }
        }
        return mAccountProvider;
    }

    public List<Account> loadAccountList() {
        List<Account> accountList = new ArrayList<Account>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = mDBConn.prepareStatement("select * from " + TABLE_NAME);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                do {
                    Account account = new Account();
                    String loginId = rs.getString(LOGIN_ID);
                    if (loginId != null) {
                        account.setLogin_id(loginId);
                        accountList.add(account);
                    }
                } while (rs.next());
            }

        } catch (SQLException e) {
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

        LogHelper.log(TAG, "count = " + accountList.size());
        return accountList;
    }

    public Account loadAccount(String loginID) {
        Account account = new Account();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            StringBuilder where = new StringBuilder(" where ");
            where.append(LOGIN_ID + "='" + loginID + "'");
            String sql = "select * from " + TABLE_NAME + where.toString();
            LogHelper.log(TAG, "sql = " + sql);
            pstmt = mDBConn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                do {
                    String loginId = rs.getString(LOGIN_ID);
                    if (loginId != null) {
                        account.setLogin_id(loginId);
                    }
                    String loginPW = rs.getString(LOGIN_PW);
                    if (loginPW != null) {
                        account.setLogin_pw(loginPW);
                    }
                    String name = rs.getString(NAME);
                    if (name != null) {
                        account.setName(name);
                    }
                    String auth = rs.getString(AUTHORITY);
                    if (auth != null) {
                        account.setAuthority(auth);
                    }
                } while (rs.next());
            }

        } catch (SQLException e) {
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

        LogHelper.log(TAG, "acount name = " + account.getName());
        return account;
    }

    public boolean addAccount(Account account) {

        PreparedStatement pstmt = null;
        boolean result = false;
        int count = 0;
        try {
            StringBuilder values = new StringBuilder();
            values.append("'" + account.getLogin_id() + "'");
            values.append(", '" + account.getLogin_pw() + "'");
            values.append(", '" + account.getName() + "'");
            values.append(", '" + account.getAuthority() + "'");
            values.append(", '" + account.getCredit_number() + "'");
            values.append(", '" + account.getCredit_name() + "'");
            values.append(", '" + account.getCredit_expired() + "'");
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

    public int updateAccount(Account account) {

        PreparedStatement pstmt = null;
        int count = 0;
        try {
            StringBuilder values = new StringBuilder();
            values.append("name='" + account.getName() + "'");
            values.append(" where " + LOGIN_ID + "='" + account.getLogin_id() + "'");
            //To Do for other values
            
            String sql = "update " + TABLE_NAME + " set " + values.toString();
            LogHelper.log(TAG, "sql = " + sql);
            pstmt = mDBConn.prepareStatement(sql);
            count = pstmt.executeUpdate();

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
        return count;
    }
    
    public int deleteAccount(String loginId) {

        PreparedStatement pstmt = null;
        boolean result = false;
        int count = 0;
        try {
            StringBuilder values = new StringBuilder(" where ");
            values.append("login_id='" +loginId + "'");
            String sql = "delete from " + TABLE_NAME + values.toString();
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
        return count;
    }
}
