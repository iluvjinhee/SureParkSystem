package com.lge.sureparksystem.parkserver.topic;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.Message;

public class ParkViewNetworkManagerTopic extends ManagerTopic {
	public ParkViewNetworkManagerTopic(JSONObject jsonObject) {
		super(jsonObject);
	}

	public ParkViewNetworkManagerTopic(Message message) {
		super(message);
	}

	@Override
	public String toString() {
		return "ParkViewNetworkManagerTopic [jsonObject=" + jsonObject + ", message=" + message + ", sessionID=" + sessionID
				+ "]";
	}
}
