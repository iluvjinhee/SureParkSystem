package com.lge.sureparksystem.parkserver.topic;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.Message;

public class ParkingLotNetworkManagerTopic extends ManagerTopic {
	public ParkingLotNetworkManagerTopic(JSONObject jsonObject) {
		super(jsonObject);
	}

	public ParkingLotNetworkManagerTopic(Message message) {
		super(message);
	}

	@Override
	public String toString() {
		return "ParkingLotNetworkManagerTopic [jsonObject=" + jsonObject + ", message=" + message + "]";
	}
}
