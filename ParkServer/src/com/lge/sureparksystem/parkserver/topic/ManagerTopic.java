package com.lge.sureparksystem.parkserver.topic;

import org.json.simple.JSONObject;

public class ManagerTopic {
	JSONObject jsonObject;

	public ManagerTopic(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	@Override
	public String toString() {
		return "ManagerTopic [jsonObject=" + jsonObject + "]";
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
}
