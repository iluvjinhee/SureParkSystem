package com.lge.sureparksystem.parkserver.topic;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.Message;

public class SecurityManagerTopic extends ManagerTopic {
	public SecurityManagerTopic(JSONObject jsonObject) {
		super(jsonObject);
	}

	public SecurityManagerTopic(Message message) {
		super(message);
	}

	@Override
	public String toString() {
		return "SecurityManagerTopic [jsonObject=" + jsonObject + ", message=" + message + ", sessionID=" + sessionID
				+ "]";
	}
}
