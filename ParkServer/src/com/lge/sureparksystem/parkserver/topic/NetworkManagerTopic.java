package com.lge.sureparksystem.parkserver.topic;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.Message;

public class NetworkManagerTopic extends ManagerTopic {
	public NetworkManagerTopic(JSONObject jsonObject) {
		super(jsonObject);
	}

	public NetworkManagerTopic(Message message) {
		super(message);
	}

	@Override
	public String toString() {
		return "NetworkManagerTopic [jsonObject=" + jsonObject + ", message=" + message + "]";
	}
}
