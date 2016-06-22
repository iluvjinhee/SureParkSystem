package com.lge.sureparksystem.parkserver.topic;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.Message;

public class WatchDogTopic extends ManagerTopic {
	public WatchDogTopic(JSONObject jsonObject) {
		super(jsonObject);
	}

	public WatchDogTopic(Message message) {
		super(message);
	}

	@Override
	public String toString() {
		return "WatchDogTopic [jsonObject=" + jsonObject + ", message=" + message + "]";
	}
}
