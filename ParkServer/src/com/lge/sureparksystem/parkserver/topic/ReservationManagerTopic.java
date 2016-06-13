package com.lge.sureparksystem.parkserver.topic;

import org.json.simple.JSONObject;

public class ReservationManagerTopic {
	JSONObject jsonObject;
	
	public ReservationManagerTopic(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	@Override
	public String toString() {
		return "ReservationManagerTopic [jsonObject=" + jsonObject + "]";
	}
}
