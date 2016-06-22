package com.lge.sureparksystem.model;

import com.lge.sureparksystem.parkserver.message.MessageParser;

import org.json.simple.JSONObject;

public class DriverModel implements BaseModel {
    final String DRIVER_ID = "driver_id";
    final String PARKINGLOT_ID = "parkinglot_id";
    final String RESERVATION_TIME = "reservation_time";
    final String PAYMENT_INFO = "paymentinfo";
    final String RESERVATION_ID = "reservation_id";
    final String PARKINGLOT_COUNT = "parkinglot_count";
    final String PARKINGLOT_LOCATION = "parkinglot_location";
    final String PARKINGFEE = "parkingfee";
    final String GRACEPERIOD = "graceperiod";
    final String RESULT = "result";
    final String CONFIRMATION_INFO = "confirmationinfo";
    final String TYPE = "type";
    public Response mResponse;
    public Reservation_Information mReservation_Information;
    public Parkinglot_List mParkinglot_List;

    public class ParkinglotInfoRequest implements BaseInterface {
        String messagetype;

        public ParkinglotInfoRequest(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            this.messagetype = smessagetype;
        }

        public ParkinglotInfoRequest(String messagetype) {
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

    public class Reservation_Request implements BaseInterface {
        String messagetype;
        String driver_id;
        String parkinglot_id;
        String reservation_time;
        String paymentinfo;

        public Reservation_Request(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sdriver_id = MessageParser.getString(jsonObject, DRIVER_ID);
            String sparkinglot_id = MessageParser.getString(jsonObject, PARKINGLOT_ID);
            String sreservation_time = MessageParser.getString(jsonObject, RESERVATION_TIME);
            String spaymentinfo = MessageParser.getString(jsonObject, PAYMENT_INFO);
            this.messagetype = smessagetype;
            this.driver_id = sdriver_id;
            this.parkinglot_id = sparkinglot_id;
            this.reservation_time = sreservation_time;
            this.paymentinfo = spaymentinfo;
        }

        public Reservation_Request(String messagetype, String driver_id, String parkinglot_id, String reservation_time,
                String paymentinfo) {
            super();
            this.messagetype = messagetype;
            this.driver_id = driver_id;
            this.parkinglot_id = parkinglot_id;
            this.reservation_time = reservation_time;
            this.paymentinfo = paymentinfo;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(DRIVER_ID, this.driver_id);
            jsonObject.put(PARKINGLOT_ID, this.parkinglot_id);
            jsonObject.put(RESERVATION_TIME, this.reservation_time);
            jsonObject.put(PAYMENT_INFO, this.paymentinfo);
            return jsonObject;
        }
    }

    public class ReservationInfo_Request implements BaseInterface {
        String messagetype;
        String driver_id;

        public ReservationInfo_Request(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sdriver_id = MessageParser.getString(jsonObject, DRIVER_ID);
            this.messagetype = smessagetype;
            this.driver_id = sdriver_id;
        }

        public ReservationInfo_Request(String messagetype, String driver_id) {
            super();
            this.messagetype = messagetype;
            this.driver_id = driver_id;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(DRIVER_ID, this.driver_id);
            return jsonObject;
        }
    }

    public class CancelRequest implements BaseInterface {
        String messagetype;
        String driver_id;
        String reservation_id;

        public CancelRequest(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sdriver_id = MessageParser.getString(jsonObject, DRIVER_ID);
            String sreservation_id = MessageParser.getString(jsonObject, RESERVATION_ID);
            this.messagetype = smessagetype;
            this.driver_id = sdriver_id;
            this.reservation_id = sreservation_id;
        }

        public CancelRequest(String messagetype, String driver_id, String reservation_id) {
            super();
            this.messagetype = messagetype;
            this.driver_id = driver_id;
            this.reservation_id = reservation_id;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(DRIVER_ID, this.driver_id);
            jsonObject.put(RESERVATION_ID, this.reservation_id);
            return jsonObject;
        }
    }

    public class Parkinglot_List implements BaseInterface {
        public String messagetype;
        public int parkinglot_count;
        public String[] parkinglot_id;
        public String[] parkinglot_location;
        public String[] parkingfee;
        public String[] graceperiod;

        public Parkinglot_List(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            int sparkinglot_count = MessageParser.getInt(jsonObject, PARKINGLOT_COUNT);
            String[] sparkinglot_id = MessageParser.getStringList(jsonObject, PARKINGLOT_ID);
            String[] sparkinglot_location = MessageParser.getStringList(jsonObject, PARKINGLOT_LOCATION);
            String[] sparkingfee = MessageParser.getStringList(jsonObject, PARKINGFEE);
            String[] sgraceperiod = MessageParser.getStringList(jsonObject, GRACEPERIOD);
            this.messagetype = smessagetype;
            this.parkinglot_count = sparkinglot_count;
            this.parkinglot_id = sparkinglot_id;
            this.parkinglot_location = sparkinglot_location;
            this.parkingfee = sparkingfee;
            this.graceperiod = sgraceperiod;
        }

        public Parkinglot_List(String messagetype, int parkinglot_count, String[] parkinglot_id,
                String[] parkinglot_location, String[] parkingfee, String[] graceperiod) {
            super();
            this.messagetype = messagetype;
            this.parkinglot_count = parkinglot_count;
            this.parkinglot_id = parkinglot_id;
            this.parkinglot_location = parkinglot_location;
            this.parkingfee = parkingfee;
            this.graceperiod = graceperiod;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(PARKINGLOT_COUNT, this.parkinglot_count);
            jsonObject.put(PARKINGLOT_ID, this.parkinglot_id);
            jsonObject.put(PARKINGLOT_LOCATION, this.parkinglot_location);
            jsonObject.put(PARKINGFEE, this.parkingfee);
            jsonObject.put(GRACEPERIOD, this.graceperiod);
            return jsonObject;
        }
    }

    public class Reservation_Information implements BaseInterface {
        String messagetype;
        String result;
        public String reservation_id;
        String reservation_time;
        String parkinglot_id;
        String parkinglot_location;
        String parkingfee;
        String graceperiod;
        String paymentinfo;
        String confirmationinfo;

        public String getResult() {
            return result;
        }

        public Reservation_Information(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sresult = MessageParser.getString(jsonObject, RESULT);
            String sreservation_id = MessageParser.getString(jsonObject, RESERVATION_ID);
            String sreservation_time = MessageParser.getString(jsonObject, RESERVATION_TIME);
            String sparkinglot_id = MessageParser.getString(jsonObject, PARKINGLOT_ID);
            String sparkinglot_location = MessageParser.getString(jsonObject, PARKINGLOT_LOCATION);
            String sparkingfee = MessageParser.getString(jsonObject, PARKINGFEE);
            String sgraceperiod = MessageParser.getString(jsonObject, GRACEPERIOD);
            String spaymentinfo = MessageParser.getString(jsonObject, PAYMENT_INFO);
            String sconfirmationinfo = MessageParser.getString(jsonObject, CONFIRMATION_INFO);
            this.messagetype = messagetype;
            this.result = result;
            this.reservation_id = reservation_id;
            this.reservation_time = reservation_time;
            this.parkinglot_id = parkinglot_id;
            this.parkinglot_location = parkinglot_location;
            this.parkingfee = parkingfee;
            this.graceperiod = graceperiod;
            this.paymentinfo = paymentinfo;
            this.confirmationinfo = confirmationinfo;
        }

        public Reservation_Information(String messagetype, String result, String reservation_id,
                String reservation_time, String parkinglot_id, String parkinglot_location, String parkingfee,
                String graceperiod, String paymentinfo, String confirmationinfo) {
            super();
            this.messagetype = messagetype;
            this.result = result;
            this.reservation_id = reservation_id;
            this.reservation_time = reservation_time;
            this.parkinglot_id = parkinglot_id;
            this.parkinglot_location = parkinglot_location;
            this.parkingfee = parkingfee;
            this.graceperiod = graceperiod;
            this.paymentinfo = paymentinfo;
            this.confirmationinfo = confirmationinfo;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(RESULT, this.result);
            jsonObject.put(RESERVATION_ID, this.reservation_id);
            jsonObject.put(RESERVATION_TIME, this.reservation_time);
            jsonObject.put(PARKINGLOT_ID, this.parkinglot_id);
            jsonObject.put(PARKINGLOT_LOCATION, this.parkinglot_location);
            jsonObject.put(PARKINGFEE, this.parkingfee);
            jsonObject.put(GRACEPERIOD, this.graceperiod);
            jsonObject.put(PAYMENT_INFO, this.paymentinfo);
            jsonObject.put(CONFIRMATION_INFO, this.confirmationinfo);
            return jsonObject;
        }
    }

    public class Response implements BaseInterface {
        String messagetype;
        public String result;
        String type;

        public Response(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sresult = MessageParser.getString(jsonObject, RESULT);
            String stype = MessageParser.getString(jsonObject, TYPE);
            this.messagetype = smessagetype;
            this.result = sresult;
            this.type = stype;
        }

        public Response(String messagetype, String result, String type) {
            super();
            this.messagetype = messagetype;
            this.result = result;
            this.type = type;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(RESULT, this.result);
            jsonObject.put(TYPE, this.type);
            return jsonObject;
        }
    }
}
