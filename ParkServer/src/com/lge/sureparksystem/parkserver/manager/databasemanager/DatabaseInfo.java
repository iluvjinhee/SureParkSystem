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
        public static final int OWNER_ID = 1;
        public static final int ATTENDANT_ID = 2;
        public static final int DRIVER_ID = 3;
        public static final String OWNER_TYPE = "OWNER";
        public static final String ATTENDANT_TYPE = "ATTENDANT";
        public static final String DRIVER_TYPE = "DRIVER";

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
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String LOGINID = "login_id";
            public static final String LOGINPW = "login_pw";
            public static final String FEE = "fee";
            public static final String GRACEPERIOD = "graceperiod";
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
            public static final String RESERVED_TIME = "datetime";
            public static final String LOT_ID = "parkinglot_id";
            public static final String PAYMENT = "payment";
            public static final String CONFIRM_CODE = "confirmation";
            public static final String STATE = "state";
            public static final String FEE = "fee";
            public static final String GRACE_PERIOD = "graceperiod";
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
}
