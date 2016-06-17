package com.lge.sureparksystem.parkserver.topic;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.Message;

public class ParkHereNetworkManagerTopic extends ManagerTopic {
	public ParkHereNetworkManagerTopic(JSONObject jsonObject) {
		super(jsonObject);
	}

	public ParkHereNetworkManagerTopic(Message message) {
		super(message);
	}

	@Override
	public String toString() {
		return "ParkHereNetworkManagerTopic [jsonObject=" + jsonObject + ", message=" + message + "]";
	}
}
