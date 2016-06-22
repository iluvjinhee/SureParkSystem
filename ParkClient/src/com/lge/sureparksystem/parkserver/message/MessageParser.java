package com.lge.sureparksystem.parkserver.message;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MessageParser {

    public static MessageType getMessageType(JSONObject jsonObject) {
        String str = (String)jsonObject.get("messagetype");
        MessageType messageType = MessageType.fromText(str);
        return messageType;
    }

    public static String getString(JSONObject jsonObject, String key) {
        String value = null;

        if (jsonObject.get(key) != null) {
            value = (String)jsonObject.get(key);
        }

        return value;
    }

    public static int getInt(JSONObject jsonObject, String key) {
        int value = -1;

        if (jsonObject.get(key) != null) {
            value = ((Long)jsonObject.get(key)).intValue();
        }

        return value;
    }

    public static String[] getStringList(JSONObject jsonObject, String key) {
        ArrayList<String> resultList = new ArrayList<String>();

        JSONArray childrenList = (JSONArray)jsonObject.get(key);

        if (childrenList != null) {
            Iterator<String> i = childrenList.iterator();

            while (i.hasNext()) {
                resultList.add(i.next());
            }
        }
        return (String[])resultList.toArray(new String[resultList.size()]);
    }
}