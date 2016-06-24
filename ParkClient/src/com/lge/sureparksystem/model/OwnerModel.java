package com.lge.sureparksystem.model;

import com.lge.sureparksystem.parkserver.message.MessageParser;

import org.json.simple.JSONObject;

public class OwnerModel implements BaseModel {
    final String PARKINGLOT_ID = "parkinglot_id";
    final String SLOT_COUNT = "slot_count";
    final String SLOT_STATUS = "slot_status";
    final String SLOT_DRIVERID = "slot_driverid";
    final String DRIVER_OFTEN = "driver_often";
    final String SLOT_TIME = "slot_time";
    final String OCCUPANCY_RATE = "occupancy_rate";
    final String REVENUE = "revenue";
    final String CANCEL_RATE = "cancel_rate";
    final String TYPE = "type";
    final String VALUE = "value";
    final String PERIOD = "period";
    final String PARKING_FEE = "parking_fee";
    final String GRACEPERIOD = "graceperiod";
    final String ID = "id";
    final String PWD = "pwd";
    final String NAME = "name";
    final String ADDRESS = "address";

    final String PARKINGLOT_COUNT = "parkinglot_count";
    final String PARKINGLOT_ID_LIST = "parkinglot_id_list";
    final String PARKINGLOT_LOCATION_LIST = "parkinglot_location_list";
    final String PARKINGFEE_LIST = "parking_fee_list";
    final String GRACEPERIOD_LIST = "graceperiod_list";

    public Parkinglot_List mParkinglot_List;
    public Parkinglot_Statistics mParkinglot_Statistics;
    public Change_Response mChange_Response;

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

    public class Parkinglot_stats_Request implements BaseInterface {
        String messagetype;
        String parkinglot_id;
        String period;

        public Parkinglot_stats_Request(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sparkinglot_id = MessageParser.getString(jsonObject, PARKINGLOT_ID);
            String speriod = MessageParser.getString(jsonObject, PERIOD);
            this.messagetype = smessagetype;
            this.parkinglot_id = sparkinglot_id;
            this.period = speriod;
        }

        public Parkinglot_stats_Request(String messagetype, String parkinglot_id, String period) {
            super();
            this.messagetype = messagetype;
            this.parkinglot_id = parkinglot_id;
            this.period = period;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(PARKINGLOT_ID, this.parkinglot_id);
            jsonObject.put(PERIOD, this.period);
            return jsonObject;
        }
    }

    public class Change_parkingfee implements BaseInterface {
        String messagetype;
        String parkinglot_id;
        String parking_fee;

        public Change_parkingfee(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sparkinglot_id = MessageParser.getString(jsonObject, PARKINGLOT_ID);
            String sparking_fee = MessageParser.getString(jsonObject, PARKING_FEE);
            this.messagetype = smessagetype;
            this.parkinglot_id = sparkinglot_id;
            this.parking_fee = sparking_fee;
        }

        public Change_parkingfee(String messagetype, String parkinglot_id, String parking_fee) {
            super();
            this.messagetype = messagetype;
            this.parkinglot_id = parkinglot_id;
            this.parking_fee = parking_fee;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(PARKINGLOT_ID, this.parkinglot_id);
            jsonObject.put(PARKING_FEE, this.parking_fee);
            return jsonObject;
        }
    }

    public class Change_graceperiod implements BaseInterface {
        String messagetype;
        String parkinglot_id;
        String graceperiod;

        public Change_graceperiod(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sparkinglot_id = MessageParser.getString(jsonObject, PARKINGLOT_ID);
            String sgraceperiod = MessageParser.getString(jsonObject, GRACEPERIOD);
            this.messagetype = smessagetype;
            this.parkinglot_id = sparkinglot_id;
            this.graceperiod = sgraceperiod;
        }

        public Change_graceperiod(String messagetype, String parkinglot_id, String graceperiod) {
            super();
            this.messagetype = messagetype;
            this.parkinglot_id = parkinglot_id;
            this.graceperiod = graceperiod;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(PARKINGLOT_ID, this.parkinglot_id);
            jsonObject.put(GRACEPERIOD, this.graceperiod);
            return jsonObject;
        }
    }

    public class Create_Attendant implements BaseInterface {
        String messagetype;
        String id;
        String pwd;
        String name;
        String parkinglot_id;

        public Create_Attendant(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sid = MessageParser.getString(jsonObject, ID);
            String spwd = MessageParser.getString(jsonObject, PWD);
            String sname = MessageParser.getString(jsonObject, NAME);
            String sparkinglot_id = MessageParser.getString(jsonObject, PARKINGLOT_ID);
            this.messagetype = smessagetype;
            this.id = sid;
            this.pwd = spwd;
            this.name = sname;
            this.parkinglot_id = sparkinglot_id;
        }

        public Create_Attendant(String messagetype, String id, String pwd, String name, String parkinglot_id) {
            super();
            this.messagetype = messagetype;
            this.id = id;
            this.pwd = pwd;
            this.name = name;
            this.parkinglot_id = parkinglot_id;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(ID, this.id);
            jsonObject.put(PWD, this.pwd);
            jsonObject.put(NAME, this.name);
            jsonObject.put(PARKINGLOT_ID, this.parkinglot_id);
            return jsonObject;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }
    }

    public class Remove_Attendant implements BaseInterface {
        String messagetype;
        String id;

        public Remove_Attendant(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sid = MessageParser.getString(jsonObject, ID);
            this.messagetype = smessagetype;
            this.id = sid;
        }

        public Remove_Attendant(String messagetype, String id) {
            super();
            this.messagetype = messagetype;
            this.id = id;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(ID, this.id);
            return jsonObject;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }
    }

    public class Add_Parkinglot implements BaseInterface {
        String messagetype;
        String id;
        String pwd;
        String address;
        String parking_fee;
        String graceperiod;

        public Add_Parkinglot(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sid = MessageParser.getString(jsonObject, ID);
            String spwd = MessageParser.getString(jsonObject, PWD);
            String saddress = MessageParser.getString(jsonObject, ADDRESS);
            String sparking_fee = MessageParser.getString(jsonObject, PARKING_FEE);
            String sgraceperiod = MessageParser.getString(jsonObject, GRACEPERIOD);
            this.messagetype = smessagetype;
            this.id = sid;
            this.pwd = spwd;
            this.address = saddress;
            this.parking_fee = sparking_fee;
            this.graceperiod = sgraceperiod;
        }

        public Add_Parkinglot(String messagetype, String id, String pwd, String address, String parking_fee,
                String graceperiod) {
            super();
            this.messagetype = messagetype;
            this.id = id;
            this.pwd = pwd;
            this.address = address;
            this.parking_fee = parking_fee;
            this.graceperiod = graceperiod;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(ID, this.id);
            jsonObject.put(PWD, this.pwd);
            jsonObject.put(ADDRESS, this.address);
            jsonObject.put(PARKING_FEE, this.parking_fee);
            jsonObject.put(GRACEPERIOD, this.graceperiod);
            return jsonObject;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }
    }

    public class Remove_Parkinglot implements BaseInterface {
        String messagetype;
        String id;

        public Remove_Parkinglot(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sid = MessageParser.getString(jsonObject, ID);
            this.messagetype = smessagetype;
            this.id = sid;
        }

        public Remove_Parkinglot(String messagetype, String id) {
            super();
            this.messagetype = messagetype;
            this.id = id;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(ID, this.id);
            return jsonObject;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }
    }

    public class Parkinglot_List implements BaseInterface {
        public String messagetype;
        public int parkinglot_count;
        public String[] parkinglot_id_list;
        public String[] parkinglot_location_list;
        public String[] parkingfee_list;
        public String[] graceperiod_list;

        public Parkinglot_List(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            int sparkinglot_count = MessageParser.getInt(jsonObject, PARKINGLOT_COUNT);
            String[] sparkinglot_id = MessageParser.getStringList(jsonObject, PARKINGLOT_ID_LIST);
            String[] sparkinglot_location = MessageParser.getStringList(jsonObject, PARKINGLOT_LOCATION_LIST);
            String[] sparkingfee = MessageParser.getStringList(jsonObject, PARKINGFEE_LIST);
            String[] sgraceperiod = MessageParser.getStringList(jsonObject, GRACEPERIOD_LIST);
            this.messagetype = smessagetype;
            this.parkinglot_count = sparkinglot_count;
            this.parkinglot_id_list = sparkinglot_id;
            this.parkinglot_location_list = sparkinglot_location;
            this.parkingfee_list = sparkingfee;
            this.graceperiod_list = sgraceperiod;
        }

        public Parkinglot_List(String messagetype, int parkinglot_count, String[] parkinglot_id,
                String[] parkinglot_location, String[] parkingfee, String[] graceperiod) {
            super();
            this.messagetype = messagetype;
            this.parkinglot_count = parkinglot_count;
            this.parkinglot_id_list = parkinglot_id;
            this.parkinglot_location_list = parkinglot_location;
            this.parkingfee_list = parkingfee;
            this.graceperiod_list = graceperiod;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(PARKINGLOT_COUNT, this.parkinglot_count);
            jsonObject.put(PARKINGLOT_ID_LIST, this.parkinglot_id_list);
            jsonObject.put(PARKINGLOT_LOCATION_LIST, this.parkinglot_location_list);
            jsonObject.put(PARKINGFEE_LIST, this.parkingfee_list);
            jsonObject.put(GRACEPERIOD_LIST, this.graceperiod_list);
            return jsonObject;
        }
    }

    public class Parkinglot_Statistics implements BaseInterface {
        String messagetype;
        public String parkinglot_id;
        public int slot_count;
        public String[] slot_status;
        public String[] slot_driverid;
        public String[] driver_often;
        public String[] slot_time;
        public String occupancy_rate;
        public String revenue;
        public String cancel_rate;

        public Parkinglot_Statistics(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sparkinglot_id = MessageParser.getString(jsonObject, PARKINGLOT_ID);
            int sslot_count = MessageParser.getInt(jsonObject, SLOT_COUNT);
            String[] sslot_status = MessageParser.getStringList(jsonObject, SLOT_STATUS);
            String[] sslot_driverid = MessageParser.getStringList(jsonObject, SLOT_DRIVERID);
            String[] sdriver_often = MessageParser.getStringList(jsonObject, DRIVER_OFTEN);
            String[] sslot_time = MessageParser.getStringList(jsonObject, SLOT_TIME);
            String soccupancy_rate = MessageParser.getString(jsonObject, OCCUPANCY_RATE);
            String srevenue = MessageParser.getString(jsonObject, REVENUE);
            String scancel_rate = MessageParser.getString(jsonObject, CANCEL_RATE);
            this.messagetype = smessagetype;
            this.parkinglot_id = sparkinglot_id;
            this.slot_count = sslot_count;
            this.slot_status = sslot_status;
            this.slot_driverid = sslot_driverid;
            this.driver_often = sdriver_often;
            this.slot_time = sslot_time;
            this.occupancy_rate = soccupancy_rate;
            this.revenue = srevenue;
            this.cancel_rate = scancel_rate;
        }

        public Parkinglot_Statistics(String messagetype, String parkinglot_id, int slot_count, String[] slot_status,
                String[] slot_driverid, String[] driver_often, String[] slot_time, String occupancy_rate,
                String revenue, String cancel_rate) {
            super();
            this.messagetype = messagetype;
            this.parkinglot_id = parkinglot_id;
            this.slot_count = slot_count;
            this.slot_status = slot_status;
            this.slot_driverid = slot_driverid;
            this.driver_often = driver_often;
            this.slot_time = slot_time;
            this.occupancy_rate = occupancy_rate;
            this.revenue = revenue;
            this.cancel_rate = cancel_rate;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(SLOT_COUNT, this.parkinglot_id);
            jsonObject.put(SLOT_STATUS, this.slot_count);
            jsonObject.put(SLOT_DRIVERID, this.slot_status);
            jsonObject.put(DRIVER_OFTEN, this.slot_driverid);
            jsonObject.put(SLOT_TIME, this.slot_time);
            jsonObject.put(OCCUPANCY_RATE, this.occupancy_rate);
            jsonObject.put(REVENUE, this.revenue);
            jsonObject.put(CANCEL_RATE, this.cancel_rate);
            return jsonObject;
        }
    }

    public class Change_Response implements BaseInterface {
        String messagetype;
        public String result;
        public String type;
        public String value;

        public Change_Response(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sresult = MessageParser.getString(jsonObject, RESULT);
            String stype = MessageParser.getString(jsonObject, TYPE);
            String svalue = MessageParser.getString(jsonObject, VALUE);
            this.messagetype = smessagetype;
            this.result = sresult;
            this.type = stype;
            this.value = svalue;
        }

        public Change_Response(String messagetype, String result, String type, String value) {
            super();
            this.messagetype = messagetype;
            this.result = result;
            this.type = type;
            this.value = value;
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
            jsonObject.put(VALUE, this.value);
            return jsonObject;
        }
    }
}
