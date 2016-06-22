package com.lge.sureparksystem.model;

public class AttendantModel implements BaseModel {

    class ParkinglotStatus {
        String messagetype;
        int slot_count;
        String[] slot_status;
        String[] slot_driverid;
        String[] driver_often;
        String[] slot_time;
        int timestamp;

        public ParkinglotStatus(String messagetype, int slot_count, String[] slot_status, String[] slot_driverid,
                String[] driver_often, String[] slot_time, int timestamp) {
            super();
            this.messagetype = messagetype;
            this.slot_count = slot_count;
            this.slot_status = slot_status;
            this.slot_driverid = slot_driverid;
            this.driver_often = driver_often;
            this.slot_time = slot_time;
            this.timestamp = timestamp;
        }

    }

    // type reallocation, confirmation information error, payment error
    class Notification {
        String[] messagetype;
        String type;

        public Notification(String[] messagetype, String type) {
            super();
            this.messagetype = messagetype;
            this.type = type;
        }

    }
}
