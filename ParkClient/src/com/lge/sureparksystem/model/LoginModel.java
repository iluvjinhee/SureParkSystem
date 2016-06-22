package com.lge.sureparksystem.model;

import com.lge.sureparksystem.parkserver.message.MessageParser;

import org.json.simple.JSONObject;

public class LoginModel implements BaseModel {

    final String RESULT = "result";
    final String TYPE = "type";
    final String ID = "id";
    final String PWD = "pwd";
    final String NAME = "name";
    final String AUTHORITY = "authority";

    public class Authentication_Request implements BaseInterface {
        String messagetype;
        String id;
        String pwd;

        public Authentication_Request(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sid = MessageParser.getString(jsonObject, ID);
            String spwd = MessageParser.getString(jsonObject, pwd);
            this.messagetype = smessagetype;
            this.id = sid;
            this.pwd = spwd;
        }

        public Authentication_Request(String messagetype, String id, String pwd) {
            super();
            this.messagetype = messagetype;
            this.id = id;
            this.pwd = pwd;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(ID, this.id);
            jsonObject.put(PWD, this.pwd);
            return jsonObject;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }
    }

    public class Authentication_Response implements BaseInterface {
        String messagetype;
        String result;
        int authority;

        public String getResult() {
            return result;
        }

        public int geAuthority() {
            return authority;
        }
        
        public Authentication_Response(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sresult = MessageParser.getString(jsonObject, RESULT);
            int sauthority = MessageParser.getInt(jsonObject, AUTHORITY);
            this.messagetype = smessagetype;
            this.result = sresult;
            this.authority = sauthority;
        }

        public Authentication_Response(String messagetype, String result, int authority) {
            super();
            this.messagetype = messagetype;
            this.result = result;
            this.authority = authority;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(RESULT, this.result);
            jsonObject.put(AUTHORITY, this.authority);
            return jsonObject;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }
    }

    public class Create_Driver implements BaseInterface {
        String messagetype;
        String id;
        String pwd;
        String name;

        public Create_Driver(JSONObject jsonObject) {
            String smessagetype = MessageParser.getString(jsonObject, MESSAGETYPE);
            String sid = MessageParser.getString(jsonObject, ID);
            String spwd = MessageParser.getString(jsonObject, PWD);
            String sname = MessageParser.getString(jsonObject, NAME);
            this.messagetype = smessagetype;
            this.id = sid;
            this.pwd = spwd;
            this.name = name;
        }

        public Create_Driver(String messagetype, String id, String pwd, String name) {
            super();
            this.messagetype = messagetype;
            this.id = id;
            this.pwd = pwd;
            this.name = name;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(ID, this.id);
            jsonObject.put(PWD, this.pwd);
            jsonObject.put(NAME, this.name);
            return jsonObject;
        }

        @Override
        public String getMessageType() {
            return messagetype;
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

        public Response(String messagetype, String id, String pwd) {
            super();
            this.messagetype = messagetype;
            this.result = id;
            this.type = pwd;
        }

        @Override
        public JSONObject putJSONObject(JSONObject jsonObject) {
            jsonObject.put(MESSAGETYPE, this.messagetype);
            jsonObject.put(RESULT, this.result);
            jsonObject.put(TYPE, this.type);
            return jsonObject;
        }

        @Override
        public String getMessageType() {
            return messagetype;
        }
    }
}
