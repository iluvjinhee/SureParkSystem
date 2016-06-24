package com.lge.sureparksystem.parkserver.topic;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.Message;

public class StatisticsManagerTopic extends ManagerTopic {
    public StatisticsManagerTopic(JSONObject jsonObject) {
        super(jsonObject);
    }

    public StatisticsManagerTopic(Message message) {
        super(message);
    }

    @Override
	public String toString() {
		return "StatisticsManagerTopic [jsonObject=" + jsonObject + ", message=" + message + ", sessionID=" + sessionID
				+ "]";
	}
}
