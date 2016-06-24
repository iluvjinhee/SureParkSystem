package com.lge.sureparksystem.parkserver.topic;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.Message;

public class ParkingLotManagerTopic extends ManagerTopic {
	public ParkingLotManagerTopic(JSONObject jsonObject) {
		super(jsonObject);
	}

	public ParkingLotManagerTopic(Message message) {
		super(message);
	}

	@Override
	public String toString() {
		return "ParkingLotManagerTopic [jsonObject=" + jsonObject + ", message=" + message + ", sessionID=" + sessionID
				+ "]";
	}
}