package com.lge.sureparksystem.parkserver.topic;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.Message;

public class ReservationManagerTopic extends ManagerTopic {
	public ReservationManagerTopic(JSONObject jsonObject) {
		super(jsonObject);
	}

	public ReservationManagerTopic(Message message) {
		super(message);
	}

	@Override
	public String toString() {
		return "ReservationManagerTopic [jsonObject=" + jsonObject + ", message=" + message + "]";
	}
}
