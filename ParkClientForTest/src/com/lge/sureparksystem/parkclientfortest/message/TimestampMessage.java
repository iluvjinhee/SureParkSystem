package com.lge.sureparksystem.parkclientfortest.message;

public class TimestampMessage extends Message {
	public static String TIMESTAMP = "Timestamp";
	
	int timestamp;
	
	public TimestampMessage() {
		super();
		this.timestamp = 0;
	}
	
	public TimestampMessage(MessageType messageType, String globalValue, int timestamp) {
		super(messageType, globalValue);
		
		this.timestamp = timestamp;
	}
	
	public TimestampMessage(MessageType messageType, int timestamp) {
		super(messageType);
		
		this.timestamp = timestamp;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;	
	}

	@Override
	public String toString() {
		return "TimestampMessage [timestamp=" + timestamp + ", messageType=" + messageType + ", globalValue="
				+ globalValue + "]";
	}	
}