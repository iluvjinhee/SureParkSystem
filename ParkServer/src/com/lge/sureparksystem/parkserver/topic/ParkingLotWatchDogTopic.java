package com.lge.sureparksystem.parkserver.topic;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.Message;

public class ParkingLotWatchDogTopic extends ManagerTopic {
	public ParkingLotWatchDogTopic(JSONObject jsonObject) {
		super(jsonObject);
	}

	public ParkingLotWatchDogTopic(Message message) {
		super(message);
	}

	@Override
	public String toString() {
		return "ParkingLotWatchDogTopic [jsonObject=" + jsonObject + ", message=" + message + ", sessionID=" + sessionID
				+ "]";
	}
}
