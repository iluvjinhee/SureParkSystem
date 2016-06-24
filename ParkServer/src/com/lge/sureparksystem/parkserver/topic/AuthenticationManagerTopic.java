package com.lge.sureparksystem.parkserver.topic;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.Message;

public class AuthenticationManagerTopic extends ManagerTopic {
	public AuthenticationManagerTopic(JSONObject jsonObject) {
		super(jsonObject);
	}

	public AuthenticationManagerTopic(Message message) {
		super(message);
	}

	@Override
	public String toString() {
		return "AuthenticationManagerTopic [jsonObject=" + jsonObject + ", message=" + message + ", sessionID="
				+ sessionID + "]";
	}
}
