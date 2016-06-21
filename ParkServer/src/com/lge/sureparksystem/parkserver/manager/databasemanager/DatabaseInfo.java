package com.lge.sureparksystem.parkserver.manager.databasemanager;

public final class DatabaseInfo {
    //    private final static String TAG = DatabaseInfo.class.getSimpleName();
    /*************************************************************************************/
    // Version & History
    //DATE: 20160615, VERSION: 1.0.0 release
    /*************************************************************************************/
    public static final String VERSION = "1.0.0";

    private DatabaseInfo() {
    }

    /**
     * Container for all User content.
     */
    public static final class User {
        public static final String USER_TABLE = "user";

        /**
         * Columns for User file.
         */
        public interface Columns {
            public static final String USERNAME = "username";
            public static final String EMAIL = "email";
            public static final String PW = "password";
            public static final String TIME = "create_time";
            public static final String AUTHORITY = "authority_id";
        }

    }

    /**
     * Container for authority.
     */
    public static final class Authority {
        public static final String AUTHORITY_TABLE = "authority";
        public static final String OWNER_TYPE = "OWNER";
        public static final String ATTENDANT_TYPE = "ATTENDANT";
        public static final String DRIVER_TYPE = "DRIVER";

        /**
         * Values for authority type.
         */
        public interface ID_TYPE {
            public static final int OWNER = 1;
            public static final int ATTENDANT = 2;
            public static final int DRIVER = 3;
        }

        /**
         * Columns for authority.
         */
        public interface Columns {
            public static final String ID = "id";
            public static final String TYPE = "type";
        }
    }

    /**
     * Container for parking lot.
     */
    public static final class ParkingLot {
        public static final String PARKINGLOT_TABLE = "parkinglot";

        /**
         * Columns for parking lot.
         */
        public interface Columns {
            public static final String LOGIN_ID = "login_id";
            public static final String PASSWORD = "password";
            public static final String ADDRESS = "address";
            public static final String FEE = "fee";
            public static final String GRACE_PERIOD = "grace_period";
            public static final String USEREMAIL = "user_email";
        }
    }

    /**
     * Container for making a reservation.
     */
    public static final class Reservation {
        public static final String RESERVATION_TABLE = "reservation";

        /**
         * Values for reservation state.
         */
        public interface STATE_TYPE {
            public static final int RESERVED = 1;
            public static final int PARKED = 2;
            public static final int UNPARKED = 3;
            public static final int CANCELED = 4;
        }

        /**
         * Columns for reservation.
         */
        public interface Columns {
            public static final String ID = "id";
            public static final String USER_EMAIL = "user_email";
            public static final String RESERVATION_TIME = "reservation_time";
            public static final String LOT_ID = "parkinglot_id";
            public static final String CREDIT_INFO = "credit_info";
            public static final String CONFIRM_INFO = "confirmation_info";
            public static final String PARKING_FEE = "parking_fee";
            public static final String GRACE_PERIOD = "grace_period";
            public static final String RESERVATION_STATE = "reservation_state";
            public static final String PAYMENT = "payment";
        }
    }

    /**
     * Container for parking a car.
     */
    public static final class Parking {
        public static final String PARKING_TABLE = "parking";

        /**
         * Columns for parking.
         */
        public interface Columns {
            public static final String ID = "id";
            public static final String RSERVATION_ID = "reservation_id";
            public static final String ASSIGNED_SLOT = "assigned_slot";
            public static final String PARKED_SLOT = "parked_slot";
            public static final String PARKING_TIME = "parking_time";
            public static final String UNPARKING_TIME = "unparking_time";
        }
    }

    /**
     * Container for changing history.
     */
    public static final class ChangingHistory {
        public static final String HISTORY_TABLE = "change_history";

        /**
         * Values for changed type.
         */
        public interface CHANGED_TYPE {
            public static final int FEE = 1;
            public static final int GRACE_PERIOD = 2;
        }

        /**
         * Columns for ChangingHistory.
         */
        public interface Columns {
            public static final String PARKINGLOT_ID = "parkinglot_id";
            public static final String CHANGED_TIME = "changed_time";
            public static final String CHANGED_TYPE = "changed_type";
            public static final String CHANGED_VALUE = "changed_value";
        }
    }

    /**
     * Container for Occupancy rate per hour.
     */
    public static final class OccupancyRatePerHour {
        public static final String OCCUPANCYRATE_TABLE = "occupancy_rate";

        /**
         * Columns for OccupancyRate.
         */
        public interface Columns {
            public static final String ID = "id";
            public static final String YEAR = "year";
            public static final String MONTH = "month";
            public static final String DAY = "day";
            public static final String HOUR = "hour";
            public static final String PARKINGLOT_ID = "parkinglot_id";
            public static final String OCCUPANCY_RATE = "occupancy_rate";
        }
    }
    
    /**
     * Container for Statistics information.
     */
    public static final class StatisticsInfo {
        public static final String STATISTICSINFO_TABLE = "statistics_info";

        /**
         * Columns for OccupancyRate.
         */
        public interface Columns {
            public static final String ID = "id";
            public static final String PARKINGLOT_ID = "parkinglot_id";
            public static final String DATE_TIME = "date_time";
            public static final String YEAR = "year";
            public static final String MONTH = "month";
            public static final String DAY = "day";
            public static final String REVENUE = "revenue";
            public static final String OCCUPANCY_RATE = "occupancy_rate";
            public static final String CANCEL_RATE = "cancel_rate";
        }
    }
}
