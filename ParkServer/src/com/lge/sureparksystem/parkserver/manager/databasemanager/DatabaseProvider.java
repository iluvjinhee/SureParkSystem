package com.lge.sureparksystem.parkserver.manager.databasemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseInfo.ChangingHistory;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseInfo.OccupancyRatePerHour;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseInfo.Parking;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseInfo.ParkingLot;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseInfo.Reservation;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseInfo.StatisticsInfo;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseInfo.User;
import com.lge.sureparksystem.parkserver.util.Logger;
import com.sun.istack.internal.Nullable;
import com.sun.javafx.collections.MappingChange.Map;

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
	public boolean isExistingUser(String email) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
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

	public int verifyUser(String email, String password) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return 0;
		}
		int authority = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			StringBuilder where = new StringBuilder(" where ");
			where.append(User.Columns.EMAIL + "='" + email + "'");
			where.append(" AND " + User.Columns.PW + "=" + getSqlStringForEncryption(password));
			String sql = "select * from " + User.USER_TABLE + where.toString();
			LogHelper.log(TAG, "sql = " + sql);
			pstmt = mDBConn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				do {
					authority = rs.getInt(User.Columns.AUTHORITY);
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
		LogHelper.log(TAG, "authority = " + authority);
		return authority;
	}

	public boolean createUserAccount(UserAccountData newuser) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		if (newuser.getEmail() == null) {
			LogHelper.log(TAG, "Error : email is null.");
			return false;
		}
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
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return -1;
		}
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
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return null;
		}
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
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		if (!isExistingUser(email)) {
			LogHelper.log(TAG, "Error : email is not exist.");
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
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		boolean result = false;
		int count = 0;

		StringBuilder columns = new StringBuilder();
		columns.append(Reservation.Columns.USER_EMAIL);
		columns.append(", " + Reservation.Columns.RESERVATION_TIME);
		columns.append(", " + Reservation.Columns.LOT_ID);
		columns.append(", " + Reservation.Columns.CREDIT_INFO);
		columns.append(", " + Reservation.Columns.CONFIRM_INFO);
		columns.append(", " + Reservation.Columns.PARKING_FEE);
		columns.append(", " + Reservation.Columns.GRACE_PERIOD);
		columns.append(", " + Reservation.Columns.RESERVATION_STATE);
		columns.append(", " + Reservation.Columns.PAYMENT);

		StringBuilder values = new StringBuilder();
		values.append("'" + newreservation.getUserEmail() + "'");
		values.append(", '" + mDateFormat.format(newreservation.getReservationTime()) + "'");
		values.append(", '" + newreservation.getParkinglotId() + "'");
		values.append(", " + getSqlStringForEncryption(newreservation.getCreditInfo()));
		values.append(", '" + newreservation.getConfirmInfo() + "'");
		values.append(", '" + newreservation.getParkingFee() + "'");
		values.append(", '" + newreservation.getGracePeriod() + "'");
		values.append(", " + newreservation.getReservationState());
		values.append(", " + newreservation.getPayment());

		count = doExecInsertSQL(Reservation.RESERVATION_TABLE, columns.toString(),
				values.toString());

		result = (count == 1) ? true : false;
		LogHelper.log(TAG, "result = " + result);
		return result;
	}

	//if not exist, return null
	@Nullable
	public ReservationData getReservationInfo(String email) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return null;
		}
		ReservationData reservation = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			StringBuilder where = new StringBuilder(" where ");
			where.append(Reservation.Columns.USER_EMAIL + "='" + email + "' AND ");
			where.append(
					Reservation.Columns.RESERVATION_STATE + "=" + Reservation.STATE_TYPE.RESERVED);

			String sql = "select * from " + Reservation.RESERVATION_TABLE + where.toString();
			LogHelper.log(TAG, "sql = " + sql);

			pstmt = mDBConn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				reservation = new ReservationData();
				reservation.setId(rs.getInt(Reservation.Columns.ID));
				reservation.setUserEmail(rs.getString(Reservation.Columns.USER_EMAIL));
				try {
					reservation.setReservationTime(
							mDateFormat.parse(rs.getString(Reservation.Columns.RESERVATION_TIME)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				reservation.setParkinglotId(rs.getString(Reservation.Columns.LOT_ID));
//				reservation.setCreditInfo(rs.getString(Reservation.Columns.CREDIT_INFO));	//block for security
				reservation.setConfirmInfo(rs.getString(Reservation.Columns.CONFIRM_INFO));
				reservation.setParkingFee(rs.getString(Reservation.Columns.PARKING_FEE));
				reservation.setGracePeriod(rs.getString(Reservation.Columns.GRACE_PERIOD));
				reservation.setReservationState(rs.getInt(Reservation.Columns.RESERVATION_STATE));
				reservation.setPayment(rs.getFloat(Reservation.Columns.PAYMENT));
				if (rs.next()) {
					LogHelper.log(TAG, "Warning :: there are many reservation.");
				}
				LogHelper.log(TAG, "reservation  = " + reservation.toString());
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
		return reservation;
	}

	//if not exist, return null
	@Nullable
	public ReservationData getReservationInfo(int reservationId) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return null;
		}
		ReservationData reservation = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			StringBuilder where = new StringBuilder(" where ");
			where.append(Reservation.Columns.ID + "=" + reservationId);

			String sql = "select * from " + Reservation.RESERVATION_TABLE + where.toString();
			LogHelper.log(TAG, "sql = " + sql);

			pstmt = mDBConn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				reservation = new ReservationData();
				reservation.setId(rs.getInt(Reservation.Columns.ID));
				reservation.setUserEmail(rs.getString(Reservation.Columns.USER_EMAIL));
				try {
					reservation.setReservationTime(
							mDateFormat.parse(rs.getString(Reservation.Columns.RESERVATION_TIME)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				reservation.setParkinglotId(rs.getString(Reservation.Columns.LOT_ID));
//				reservation.setCreditInfo(rs.getString(Reservation.Columns.CREDIT_INFO)); //block for security
				reservation.setConfirmInfo(rs.getString(Reservation.Columns.CONFIRM_INFO));
				reservation.setParkingFee(rs.getString(Reservation.Columns.PARKING_FEE));
				reservation.setGracePeriod(rs.getString(Reservation.Columns.GRACE_PERIOD));
				reservation.setReservationState(rs.getInt(Reservation.Columns.RESERVATION_STATE));
				reservation.setPayment(rs.getFloat(Reservation.Columns.PAYMENT));
				if (rs.next()) {
					LogHelper.log(TAG, "Warning :: there are many reservation.");
				}
				LogHelper.log(TAG, "reservation  = " + reservation.toString());
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
		return reservation;
	}
	
	public int getReservationId(String confirmationInfo) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return -1;
		}
		int reservationId = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			StringBuilder where = new StringBuilder(" where ");
			where.append(Reservation.Columns.CONFIRM_INFO + "='" + confirmationInfo + "' AND ");
			where.append(
					Reservation.Columns.RESERVATION_STATE + "=" + Reservation.STATE_TYPE.RESERVED);

			String sql = "select * from " + Reservation.RESERVATION_TABLE + where.toString();
			LogHelper.log(TAG, "sql = " + sql);

			pstmt = mDBConn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				reservationId = rs.getInt(Reservation.Columns.ID);
				if (rs.next()) {
					LogHelper.log(TAG, "Warning :: there are many reservation.");
				}
				LogHelper.log(TAG, "reservation  = " + reservationId);
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
		return reservationId;
	}

	public boolean updateReservationState(int reservationId, int state) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		int count = 0;
		boolean result = false;
		StringBuilder set = new StringBuilder(" set ");
		set.append(Reservation.Columns.RESERVATION_STATE + "=" + state);

		StringBuilder where = new StringBuilder(" where ");
		where.append(Reservation.Columns.ID + "=" + reservationId);

		count = doExecUpdateSQL(Reservation.RESERVATION_TABLE, set.toString(), where.toString());

		result = (count == 1) ? true : false;
		LogHelper.log(TAG, "result = " + result);
		return result;
	}

	public boolean updateReservationPayment(int reservationId, int state, float payment) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		int count = 0;
		boolean result = false;
		StringBuilder set = new StringBuilder(" set ");
		set.append(Reservation.Columns.RESERVATION_STATE + "=" + state);
		set.append(", " + Reservation.Columns.PAYMENT + "=" + payment);

		StringBuilder where = new StringBuilder(" where ");
		where.append(Reservation.Columns.ID + "=" + reservationId);

		count = doExecUpdateSQL(Reservation.RESERVATION_TABLE, set.toString(), where.toString());

		result = (count == 1) ? true : false;
		LogHelper.log(TAG, "result = " + result);
		return result;
	}

	/*************************************************************************************/
	// For Parking Lot
	/*************************************************************************************/
	public int verifyParkingLot(String loginId, String password) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return 0;
		}
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			StringBuilder where = new StringBuilder(" where ");
			where.append(ParkingLot.Columns.LOGIN_ID + "='" + loginId + "'");
			where.append(" AND " + ParkingLot.Columns.PASSWORD + "="
					+ getSqlStringForEncryption(password));
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
		LogHelper.log(TAG, "count = " + count);
		return count;
	}

	boolean createParkingLot(ParkingLotData newlot) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		if (verifyParkingLot(newlot.getLoginId(), newlot.getLoginPw()) == 0) {
			LogHelper.log(TAG, "Error : Alreay is exist.");
			return false;
		}
		int count = 0;
		boolean result = false;

		StringBuilder columns = new StringBuilder();
		columns.append(ParkingLot.Columns.LOGIN_ID);
		columns.append(", " + ParkingLot.Columns.PASSWORD);
		columns.append(", " + ParkingLot.Columns.ADDRESS);
		columns.append(", " + ParkingLot.Columns.FEE);
		columns.append(", " + ParkingLot.Columns.GRACE_PERIOD);
		columns.append(", " + ParkingLot.Columns.USEREMAIL);

		StringBuilder values = new StringBuilder();
		values.append("'" + newlot.getLoginId() + "'");
		values.append(", " + getSqlStringForEncryption(newlot.getLoginPw()));
		values.append(", '" + newlot.getLotAddress() + "'");
		values.append(", '" + newlot.getFee() + "'");
		values.append(", '" + newlot.getGracePeriod() + "'");
		values.append(", '" + newlot.getUserEmail() + "'");

		count = doExecInsertSQL(ParkingLot.PARKINGLOT_TABLE, columns.toString(),
				values.toString());
		if (count > 0) {
			Calendar cal = Calendar.getInstance();
			createChangingHistoryData(
					new ChangingHistoryData(newlot.getLoginId(), cal.getTime(),
							ChangingHistory.CHANGED_TYPE.FEE,
							newlot.getFee()));
			createChangingHistoryData(
					new ChangingHistoryData(newlot.getLoginId(), cal.getTime(),
							ChangingHistory.CHANGED_TYPE.GRACE_PERIOD,
							newlot.getGracePeriod()));
		}

		result = (count == 1) ? true : false;
		LogHelper.log(TAG, "result = " + result);
		return result;
	}

	//if not exist, size of list is zero
	public List<ParkingLotData> getAllParkingLotInfo() {
		List<ParkingLotData> parkingLotDataList = new ArrayList<ParkingLotData>();
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return parkingLotDataList;
		}
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
					parkinglot.setLoginId(rs.getString(ParkingLot.Columns.LOGIN_ID));
					//                    parkinglot.setLoginPw(rs.getString(ParkingLot.Columns.LOGINPW));  //Do not expose
					parkinglot.setLotAddress(rs.getString(ParkingLot.Columns.ADDRESS));
					parkinglot.setFee(rs.getString(ParkingLot.Columns.FEE));
					parkinglot.setGracePeriod(rs.getString(ParkingLot.Columns.GRACE_PERIOD));
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

	//if not exist, size of list is zero
	public List<String> getParkingLotList() {
		List<String> parkingLotList = new ArrayList<String>();
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return parkingLotList;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select * from " + ParkingLot.PARKINGLOT_TABLE;
			LogHelper.log(TAG, "sql = " + sql);
			pstmt = mDBConn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				do {
					parkingLotList.add(rs.getString(ParkingLot.Columns.LOGIN_ID));
				} while (rs.next());
				LogHelper.log(TAG, "parkingLotList = " + parkingLotList.toString());
			} else {
				LogHelper.log(TAG, "parkingLotList is not exist.");
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
		LogHelper.log(TAG, "size of ParkingLot list  = " + parkingLotList.size());
		return parkingLotList;
	}

	//if not exist, return null
	@Nullable
	public ParkingLotData getParkingLotInfo(String id) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return null;
		}
		ParkingLotData parkinglot = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			StringBuilder where = new StringBuilder(" where ");
			where.append(ParkingLot.Columns.LOGIN_ID + "='" + id + "'");

			String sql = "select * from " + ParkingLot.PARKINGLOT_TABLE + where.toString();
			LogHelper.log(TAG, "sql = " + sql);
			pstmt = mDBConn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				parkinglot = new ParkingLotData();
				parkinglot.setLoginId(rs.getString(ParkingLot.Columns.LOGIN_ID));
				//                parkinglot.setLoginPw(rs.getString(ParkingLot.Columns.LOGINPW));      //Do not expose
				parkinglot.setLotAddress(rs.getString(ParkingLot.Columns.ADDRESS));
				parkinglot.setFee(rs.getString(ParkingLot.Columns.FEE));
				parkinglot.setGracePeriod(rs.getString(ParkingLot.Columns.GRACE_PERIOD));
				parkinglot.setUserEmail(rs.getString(ParkingLot.Columns.USEREMAIL));

				if (rs.next()) {
					LogHelper.log(TAG, "Warning :: result is not one.");
				}
				LogHelper.log(TAG, "parkinglot = " + parkinglot.toString());
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

	public boolean removeParkingLot(String id) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		PreparedStatement pstmt = null;
		boolean result = false;
		int count = 0;
		try {
			StringBuilder where = new StringBuilder(" where ");
			where.append(ParkingLot.Columns.LOGIN_ID + "=" + id);
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

	public boolean updateParkingLotFee(String id, String fee) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		boolean result = false;
		int count = 0;

		StringBuilder set = new StringBuilder(" set ");
		set.append(ParkingLot.Columns.FEE + "='" + fee + "'");

		StringBuilder where = new StringBuilder(" where ");
		where.append(ParkingLot.Columns.LOGIN_ID + "=" + id);

		count = doExecUpdateSQL(ParkingLot.PARKINGLOT_TABLE, set.toString(), where.toString());
		if (count > 0) {
			Calendar cal = Calendar.getInstance();
			createChangingHistoryData(new ChangingHistoryData(id, cal.getTime(),
					ChangingHistory.CHANGED_TYPE.FEE, fee));
		}

		result = (count == 1) ? true : false;
		LogHelper.log(TAG, "result = " + result);
		return result;
	}

	public boolean updateParkingLotGracePeriod(String id, String gracePeriod) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		boolean result = false;
		int count = 0;

		StringBuilder set = new StringBuilder(" set ");
		set.append(ParkingLot.Columns.GRACE_PERIOD + "='" + gracePeriod + "'");

		StringBuilder where = new StringBuilder(" where ");
		where.append(ParkingLot.Columns.LOGIN_ID + "=" + id);

		count = doExecUpdateSQL(ParkingLot.PARKINGLOT_TABLE, set.toString(), where.toString());
		if (count > 0) {
			Calendar cal = Calendar.getInstance();
			createChangingHistoryData(
					new ChangingHistoryData(id, cal.getTime(),
							ChangingHistory.CHANGED_TYPE.GRACE_PERIOD, gracePeriod));
		}

		result = (count == 1) ? true : false;
		LogHelper.log(TAG, "result = " + result);
		return result;
	}

	public boolean updateParkingLotUserEmail(int id, String email) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		boolean result = false;
		int count = 0;

		StringBuilder set = new StringBuilder(" set ");
		set.append(ParkingLot.Columns.USEREMAIL + "='" + email + "'");

		StringBuilder where = new StringBuilder(" where ");
		where.append(ParkingLot.Columns.LOGIN_ID + "=" + id);

		count = doExecUpdateSQL(ParkingLot.PARKINGLOT_TABLE, set.toString(), where.toString());

		result = (count == 1) ? true : false;
		LogHelper.log(TAG, "result = " + result);
		return result;
	}

	public boolean updateParkingLotAddress(String id, String address) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		boolean result = false;
		int count = 0;

		StringBuilder set = new StringBuilder(" set ");
		set.append(ParkingLot.Columns.ADDRESS + "='" + address + "'");

		StringBuilder where = new StringBuilder(" where ");
		where.append(ParkingLot.Columns.LOGIN_ID + "=" + id);

		count = doExecUpdateSQL(ParkingLot.PARKINGLOT_TABLE, set.toString(), where.toString());

		result = (count == 1) ? true : false;
		LogHelper.log(TAG, "result = " + result);
		return result;
	}

	public boolean updateParkingLotInfo(String id, String fee, String gracePeriod) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		boolean result = false;
		int count = 0;

		StringBuilder set = new StringBuilder(" set ");
		set.append(ParkingLot.Columns.FEE + "='" + fee + "'");
		set.append(", " + ParkingLot.Columns.GRACE_PERIOD + "='" + gracePeriod + "'");

		StringBuilder where = new StringBuilder(" where ");
		where.append(ParkingLot.Columns.LOGIN_ID + "=" + id);

		count = doExecUpdateSQL(ParkingLot.PARKINGLOT_TABLE, set.toString(), where.toString());
		if (count > 0) {
			Calendar cal = Calendar.getInstance();
			createChangingHistoryData(new ChangingHistoryData(id, cal.getTime(),
					ChangingHistory.CHANGED_TYPE.FEE, fee));
			createChangingHistoryData(
					new ChangingHistoryData(id, cal.getTime(),
							ChangingHistory.CHANGED_TYPE.GRACE_PERIOD, gracePeriod));
		}

		result = (count == 1) ? true : false;
		LogHelper.log(TAG, "result = " + result);
		return result;
	}

	public boolean updateParkingLotInfo(String id, String fee, String gracePeriod, String email) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		boolean result = false;
		int count = 0;

		StringBuilder set = new StringBuilder(" set ");
		set.append(ParkingLot.Columns.FEE + "='" + fee + "'");
		set.append(", " + ParkingLot.Columns.GRACE_PERIOD + "='" + gracePeriod + "'");
		set.append(", " + ParkingLot.Columns.USEREMAIL + "='" + email + "'");

		StringBuilder where = new StringBuilder(" where ");
		where.append(ParkingLot.Columns.LOGIN_ID + "=" + id);

		count = doExecUpdateSQL(ParkingLot.PARKINGLOT_TABLE, set.toString(), where.toString());
		if (count > 0) {
			Calendar cal = Calendar.getInstance();
			createChangingHistoryData(new ChangingHistoryData(id, cal.getTime(),
					ChangingHistory.CHANGED_TYPE.FEE, fee));
			createChangingHistoryData(
					new ChangingHistoryData(id, cal.getTime(),
							ChangingHistory.CHANGED_TYPE.GRACE_PERIOD, gracePeriod));
		}

		result = (count == 1) ? true : false;
		LogHelper.log(TAG, "result = " + result);
		return result;
	}

	/*************************************************************************************/
	// For Parking
	/*************************************************************************************/
	public boolean isExistingParkingData(int reservationId) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
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
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
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
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
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
	public ParkingData getParkingInfo(int reservationId) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return null;
		}
		ParkingData parking = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			StringBuilder where = new StringBuilder(" where ");
			where.append(Parking.Columns.RSERVATION_ID + "=" + reservationId);

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

	public boolean updateParkingParkedSlot(int reservationId, String parkedSlot, Date parkingTime) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		boolean result = false;
		int count = 0;
		int valueCnt = 0;
		StringBuilder set = new StringBuilder(" set ");
		if (parkedSlot != null) {
			set.append(Parking.Columns.PARKED_SLOT + "='" + parkedSlot + "'");
			valueCnt++;
		}

		if (parkingTime != null) {
			if (valueCnt > 0) {
				set.append(", ");
			}
			set.append(Parking.Columns.PARKING_TIME + "='" + mDateFormat.format(parkingTime)
					+ "'");
		}

		StringBuilder where = new StringBuilder(" where ");
		where.append(Parking.Columns.RSERVATION_ID + "=" + reservationId);

		count = doExecUpdateSQL(Parking.PARKING_TABLE, set.toString(), where.toString());

		result = (count == 1) ? true : false;
		LogHelper.log(TAG, "result = " + result);
		return result;
	}

	public boolean updateParkingUnparkedTime(int reservationId, Date unparkingTime) {
		return updateParkingInfo(reservationId, null, unparkingTime);
	}

	public boolean updateParkingInfo(int reservationId, String parkedSlot, Date unparkingTime) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
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
		where.append(Parking.Columns.RSERVATION_ID + "=" + reservationId);

		count = doExecUpdateSQL(Parking.PARKING_TABLE, set.toString(), where.toString());

		result = (count == 1) ? true : false;
		LogHelper.log(TAG, "result = " + result);
		return result;
	}

	/*************************************************************************************/
	// For history about changing fee or grace period
	/*************************************************************************************/
	public boolean createChangingHistoryData(ChangingHistoryData historyData) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		boolean result = false;
		int count = 0;
		StringBuilder columns = new StringBuilder();
		columns.append(ChangingHistory.Columns.PARKINGLOT_ID);
		columns.append(", " + ChangingHistory.Columns.CHANGED_TIME);
		columns.append(", " + ChangingHistory.Columns.CHANGED_TYPE);
		columns.append(", " + ChangingHistory.Columns.CHANGED_VALUE);

		StringBuilder values = new StringBuilder();
		values.append("'" + historyData.getParkinglotId() + "'");
		values.append(", '" + mDateFormat.format(historyData.getChangedTime()) + "'");
		values.append(", " + historyData.getChangedType());
		values.append(", '" + historyData.getChangedValue() + "'");

		count = doExecInsertSQL(ChangingHistory.HISTORY_TABLE, columns.toString(),
				values.toString());

		result = (count == 1) ? true : false;
		LogHelper.log(TAG, "result = " + result);
		return result;
	}

	public HashMap<Date, String> getChangingHistory(String parkinglotId, Date start, Date end,
			int type) {
		HashMap<Date, String> historyList = new HashMap<Date, String>();
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return historyList;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			StringBuilder where = new StringBuilder(" where ");
			where.append(ChangingHistory.Columns.PARKINGLOT_ID + "='" + parkinglotId + "'");
			where.append(" AND " + ChangingHistory.Columns.CHANGED_TYPE + "=" + type);
			if (start != null && end != null) {
				where.append(" AND " + ChangingHistory.Columns.CHANGED_TIME + " between ");
				where.append("'" + mDateFormat.format(start) + "' AND '" + mDateFormat.format(end)
						+ "'");
			}

			String sql = "select * from " + ChangingHistory.HISTORY_TABLE + where.toString();
			LogHelper.log(TAG, "sql = " + sql);
			pstmt = mDBConn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				do {
					try {
						historyList.put(mDateFormat
								.parse(rs.getString(ChangingHistory.Columns.CHANGED_TIME)),
								rs.getString(ChangingHistory.Columns.CHANGED_VALUE));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} while (rs.next());
				LogHelper.log(TAG, "changingHistory = " + historyList.toString());
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

		LogHelper.log(TAG, "historyList.size() = " + historyList.size());
		return historyList;
	}

	/*************************************************************************************/
	// For Occupancy rate
	/*************************************************************************************/
	private float getDailyOccupancyRate(String parkinglotId, int year, int month, int day) {
		float dailyRate = 0.0f;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			StringBuilder where = new StringBuilder(" where ");
			where.append(OccupancyRatePerHour.Columns.PARKINGLOT_ID + "='" + parkinglotId + "'");
			where.append(" AND " + OccupancyRatePerHour.Columns.YEAR + "=" + year);
			where.append(" AND " + OccupancyRatePerHour.Columns.MONTH + "=" + month);
			where.append(" AND " + OccupancyRatePerHour.Columns.DAY + "=" + day);
			String sql = "select  avg(" + OccupancyRatePerHour.Columns.OCCUPANCY_RATE + ") from "
					+ OccupancyRatePerHour.OCCUPANCYRATE_TABLE + where.toString();
			LogHelper.log(TAG, "sql = " + sql);
			pstmt = mDBConn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dailyRate = rs.getInt(1);
				if (rs.next()) {
					LogHelper.log(TAG, "Warnning : count is not one.");
				}
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
		LogHelper.log(TAG,
				"parkinglotId = " + parkinglotId + ", year = " + year + ", month = " + month
						+ ", day = " + day);
		LogHelper.log(TAG, "dailyRate = " + dailyRate);
		return dailyRate;
	}

	public boolean createOccupancyRatePerHour(Date date, String parkinglotId, float rate) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		boolean result = false;
		int count = 0;
		StringBuilder columns = new StringBuilder();
		columns.append(OccupancyRatePerHour.Columns.YEAR);
		columns.append(", " + OccupancyRatePerHour.Columns.MONTH);
		columns.append(", " + OccupancyRatePerHour.Columns.DAY);
		columns.append(", " + OccupancyRatePerHour.Columns.HOUR);
		columns.append(", " + OccupancyRatePerHour.Columns.PARKINGLOT_ID);
		columns.append(", " + OccupancyRatePerHour.Columns.OCCUPANCY_RATE);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		StringBuilder values = new StringBuilder();
		values.append(cal.get(Calendar.YEAR));
		values.append(", " + (cal.get(Calendar.MONTH) + 1));
		values.append(", " + cal.get(Calendar.DAY_OF_MONTH));
		values.append(", " + cal.get(Calendar.HOUR_OF_DAY));
		values.append(", '" + parkinglotId + "'");
		values.append(", " + rate);

		count = doExecInsertSQL(OccupancyRatePerHour.OCCUPANCYRATE_TABLE, columns.toString(),
				values.toString());

		result = (count == 1) ? true : false;
		LogHelper.log(TAG, "result = " + result);
		return result;
	}

	/*************************************************************************************/
	// For Statistics information
	/*************************************************************************************/
	private float getDailyRevenue(String parkinglotId, Date startTime, Date endTime) {
		float dailyRevenue = 0.0f;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			StringBuilder where = new StringBuilder(" where ");
			where.append(Reservation.Columns.LOT_ID + "='" + parkinglotId + "'");
			where.append(" AND " + Reservation.Columns.RESERVATION_TIME + " between '");
			where.append(
					mDateFormat.format(startTime) + "' AND '" + mDateFormat.format(endTime) + "'");

			String sql = "select  sum(" + Reservation.Columns.PAYMENT + ") from "
					+ Reservation.RESERVATION_TABLE + where.toString();
			LogHelper.log(TAG, "sql = " + sql);
			pstmt = mDBConn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				dailyRevenue = rs.getInt(1);
				if (rs.next()) {
					LogHelper.log(TAG, "Warnning : count is not one.");
				}
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
		LogHelper.log(TAG, "dailyRevenue = " + dailyRevenue);
		return dailyRevenue;
	}

	private float getDailyCancelRate(String parkinglotId, Date startTime, Date endTime) {
		float dailyCancelRate = 0.0f;
		int totCount = 0;
		int CancelCount = 0;
		StringBuilder where = new StringBuilder(" where ");
		where.append(Reservation.Columns.LOT_ID + "='" + parkinglotId + "'");
		where.append(
				" AND " + Reservation.Columns.RESERVATION_STATE + "!="
						+ Reservation.STATE_TYPE.RESERVED);
		where.append(" AND " + Reservation.Columns.RESERVATION_TIME + " between '");
		where.append(
				mDateFormat.format(startTime) + "' AND '" + mDateFormat.format(endTime) + "'");

		totCount = doExecGetCountSQL(Reservation.RESERVATION_TABLE, where.toString());

		StringBuilder cancelWhere = new StringBuilder(" where ");
		cancelWhere.append(Reservation.Columns.LOT_ID + "='" + parkinglotId + "'");
		cancelWhere.append(" AND " + Reservation.Columns.RESERVATION_STATE + "="
				+ Reservation.STATE_TYPE.CANCELED);
		cancelWhere.append(" AND " + Reservation.Columns.RESERVATION_TIME + " between '");
		cancelWhere.append(
				mDateFormat.format(startTime) + "' AND '" + mDateFormat.format(endTime) + "'");

		CancelCount = doExecGetCountSQL(Reservation.RESERVATION_TABLE, cancelWhere.toString());

		LogHelper.log(TAG, "totCount = " + totCount);
		LogHelper.log(TAG, "CancelCount = " + CancelCount);
		if (totCount > 0) {
			dailyCancelRate = CancelCount * 100 / totCount;
		}
		LogHelper.log(TAG, "dailyCancelRate = " + dailyCancelRate);
		return dailyCancelRate;
	}

	public boolean createStatisticsInfo(Date date, String parkinglotId, float revenue,
			float occupancyRate, float cancelRate) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		boolean result = false;
		int count = 0;
		StringBuilder columns = new StringBuilder();
		columns.append(StatisticsInfo.Columns.PARKINGLOT_ID);
		columns.append(", " + StatisticsInfo.Columns.DATE_TIME);
		columns.append(", " + StatisticsInfo.Columns.YEAR);
		columns.append(", " + StatisticsInfo.Columns.MONTH);
		columns.append(", " + StatisticsInfo.Columns.DAY);
		columns.append(", " + StatisticsInfo.Columns.REVENUE);
		columns.append(", " + StatisticsInfo.Columns.OCCUPANCY_RATE);
		columns.append(", " + StatisticsInfo.Columns.CANCEL_RATE);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		StringBuilder values = new StringBuilder();
		values.append("'" + parkinglotId + "'");
		values.append(", '" + mDateFormat.format(date) + "'");
		values.append(", " + cal.get(Calendar.YEAR));
		values.append(", " + (cal.get(Calendar.MONTH) + 1));
		values.append(", " + cal.get(Calendar.DAY_OF_MONTH));
		values.append(", " + revenue);
		values.append(", " + occupancyRate);
		values.append(", " + cancelRate);

		count = doExecInsertSQL(StatisticsInfo.STATISTICSINFO_TABLE, columns.toString(),
				values.toString());

		result = (count == 1) ? true : false;
		LogHelper.log(TAG, "result = " + result);
		return result;
	}

	public List<StatisticsData> getStatisticsInfo(String parkinglotId, Date startTime,
			Date endTime) {
		List<StatisticsData> statisticsListData = new ArrayList<StatisticsData>();
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return statisticsListData;
		}
		LogHelper.log("TAG", "startTime = " + startTime.toString());
		LogHelper.log("TAG", "endTime = " + endTime.toString());

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			StringBuilder where = new StringBuilder(" where ");
			where.append(StatisticsInfo.Columns.PARKINGLOT_ID + "='" + parkinglotId + "'");
			where.append(" AND " + StatisticsInfo.Columns.DATE_TIME + " between '");
			where.append(
					mDateFormat.format(startTime) + "' AND '" + mDateFormat.format(endTime) + "'");

			String sql = "select * from " + StatisticsInfo.STATISTICSINFO_TABLE + where.toString();
			LogHelper.log(TAG, "sql = " + sql);
			pstmt = mDBConn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				do {
					StatisticsData data = new StatisticsData();
					data.setYear(rs.getInt(StatisticsInfo.Columns.YEAR));
					data.setMonth(rs.getInt(StatisticsInfo.Columns.MONTH));
					data.setDay(rs.getInt(StatisticsInfo.Columns.DAY));
					data.setRevenue(rs.getFloat(StatisticsInfo.Columns.REVENUE));
					data.setOccupancyRate(rs.getFloat(StatisticsInfo.Columns.OCCUPANCY_RATE));
					data.setCancelRate(rs.getFloat(StatisticsInfo.Columns.CANCEL_RATE));
					statisticsListData.add(data);
				} while (rs.next());
				LogHelper.log(TAG, "changingHistory = " + statisticsListData.toString());
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

		LogHelper.log(TAG, "statisticsListData.size() = " + statisticsListData.size());
		return statisticsListData;
	}

	public boolean updateDailyStatisticsInfo(Date date) {
		if (mDBConn == null) {
			LogHelper.log(TAG, "Error : There is no connection with sql server");
			return false;
		}
		boolean result = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Logger.log("startTime = " + cal.getTime());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		long timeAfterOneDay = cal.getTimeInMillis() + javax.management.timer.Timer.ONE_DAY;
		cal.setTimeInMillis(timeAfterOneDay);
		Date endDate = cal.getTime();
		Logger.log("endTime = " + cal.getTime());

		List<String> parkingLotList = getParkingLotList();
		for (String parkinglot : parkingLotList) {
			float revenue = getDailyRevenue(parkinglot, date, endDate);
			float occupancy = getDailyOccupancyRate(parkinglot, year, month, day);
			float cancel = getDailyCancelRate(parkinglot, date, endDate);

			result = createStatisticsInfo(date, parkinglot, revenue, occupancy, cancel);
		}

		LogHelper.log(TAG, "result = " + result);
		return result;
	}
}
