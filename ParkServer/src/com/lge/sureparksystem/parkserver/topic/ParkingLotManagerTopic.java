package com.lge.sureparksystem.parkserver.topic;

import org.json.simple.JSONObject;

public class ParkingLotManagerTopic extends ManagerTopic {

	public ParkingLotManagerTopic(JSONObject jsonObject) {
		super(jsonObject);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ParkingLotManagerTopic [jsonObject=" + jsonObject + "]";
	}
}