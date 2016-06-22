package com.lge.sureparksystem.model;

import com.lge.sureparksystem.parkserver.message.MessageParser;

import org.json.simple.JSONObject;

public class AttendantModel implements BaseModel {
    final String SLOT_COUNT = "slot_count";
    final String SLOT_STATUS = "slot_status";
    final String SLOT_DRIVERID = "slot_driverid";
    final String SLOT_OFTEN = "driver_often";
    final String SLOT_TIME = "slot_time";
    
    public ParkinglotStatus mParkinglotStatus;
    public Notification mNotification;

    public class ParkinglotStatus_Request implements BaseInterface {
        String messagetype;
        String type;

        public ParkinglotStatus_Request(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            this.messagetype = smessagetype;
        }

        public ParkinglotStatus_Request(String messagetype) {
            super();
            this.messagetype = messagetype;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            return jsonObject;
        }
    }

    public class ParkinglotStatus implements BaseInterface {
        String messagetype;
        public int slot_count;
        public String[] slot_status;
        public String[] slot_driverid;
        public String[] driver_often;
        public String[] slot_time;

        public ParkinglotStatus(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            int sslot_count = MessageParser.getInt(jsonObject, SLOT_COUNT);
            String[] slot_status = MessageParser.getStringList(jsonObject, SLOT_STATUS);
            String[] slot_driverid = MessageParser.getStringList(jsonObject, SLOT_DRIVERID);
            String[] sdriver_often = MessageParser.getStringList(jsonObject, SLOT_OFTEN);
            String[] sslot_time = MessageParser.getStringList(jsonObject, SLOT_TIME);
            this.messagetype = smessagetype;
            this.slot_count = sslot_count;
            this.slot_status = slot_status;
            this.slot_driverid = slot_driverid;
            this.driver_often = sdriver_often;
            this.slot_time = sslot_time;
        }

        public ParkinglotStatus(String messagetype, int slot_count, String[] slot_status, String[] slot_driverid,
                String[] driver_often, String[] slot_time) {
            super();
            this.messagetype = messagetype;
            this.slot_count = slot_count;
            this.slot_status = slot_status;
            this.slot_driverid = slot_driverid;
            this.driver_often = driver_often;
            this.slot_time = slot_time;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(SLOT_COUNT, this.slot_count);
            jsonObject.put(SLOT_STATUS, this.slot_status);
            jsonObject.put(SLOT_DRIVERID, this.slot_driverid);
            jsonObject.put(SLOT_OFTEN, this.slot_driverid);
            jsonObject.put(SLOT_TIME, this.slot_time);
            return jsonObject;
        }

    }

    // type reallocation, confirmation information error, payment error
    public class Notification implements BaseInterface {
        String messagetype;
        public String type;

        public Notification(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String stype = MessageParser.getString(jsonObject, TYPE);
            this.messagetype = smessagetype;
            this.type = stype;
        }

        public Notification(String messagetype, String type) {
            super();
            this.messagetype = messagetype;
            this.type = type;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(TYPE, this.type);
            return jsonObject;
        }
    }
}
