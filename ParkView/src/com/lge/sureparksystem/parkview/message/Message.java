package com.lge.sureparksystem.parkview.message;

public class Message {
	public static String MESSAGE_TYPE = "messagetype";
	public static String TIMESTAMP = "timestamp";

	MessageType messageType;
	int timestamp;

	public Message() {
		this.messageType = null;
		this.timestamp = 0;
	}

	public Message(MessageType messageType) {
		this.messageType = messageType;
		this.timestamp = -1;
	}

	public Message(MessageType messageType, int timestamp) {
		this.messageType = messageType;
		this.timestamp = timestamp;
	}

	public Message(Message message) {
		this.messageType = message.messageType;
		this.timestamp = message.timestamp;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;	
	}

	@Override
	public String toString() {
		return "Message [messageType=" + messageType + ", timestamp=" + timestamp + "]";
	}
}
