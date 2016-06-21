package com.lge.sureparksystem.parkserver.topic;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.Message;
import com.lge.sureparksystem.parkserver.message.MessageParser;

public class ManagerTopic {
	JSONObject jsonObject = null;
	Message message = null;

	public ManagerTopic(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	
	public ManagerTopic(Message message) {
		this.message = message;
	}

	public JSONObject getJsonObject() {
		if(this.jsonObject == null) {
			return MessageParser.convertToJSONObject(message);
		}
		
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public Message getMessage() {
		if(this.message == null) {
			return MessageParser.convertToMessage(jsonObject);
		}
		
		return this.message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ManagerTopic [jsonObject=" + jsonObject + ", message=" + message + "]";
	}
}
