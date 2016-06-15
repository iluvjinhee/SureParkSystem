package com.lge.sureparksystem.parkserver.topic;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.Message;

public class CommunicationManagerTopic extends ManagerTopic {
	public CommunicationManagerTopic(JSONObject jsonObject) {
		super(jsonObject);
	}

	public CommunicationManagerTopic(Message message) {
		super(message);
	}

	@Override
	public String toString() {
		return "CommunicationManagerTopic [jsonObject=" + jsonObject + ", message=" + message + "]";
	}
}
