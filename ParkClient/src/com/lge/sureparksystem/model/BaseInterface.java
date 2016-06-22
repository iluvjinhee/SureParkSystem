package com.lge.sureparksystem.model;

import org.json.simple.JSONObject;

public interface BaseInterface {
    String getMessageType();
    JSONObject putJSONObject(JSONObject jsonObject);
}
