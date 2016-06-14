package com.lge.sureparksystem.parkclientfortest.message;

public class Message {
	public static String MESSAGE_TYPE = "MessageType";
	public static String GLOBAL_VALUE = "GlobalValue";	
	
	MessageType messageType;
	String globalValue;
	
	public Message() {
		this.messageType = null;
		this.globalValue = "";
	}
	
	public Message(MessageType messageType) {
		this.messageType = messageType;
		this.globalValue = "";
	}
	
	public Message(MessageType messageType, String globalValue) {
		this.messageType = messageType;
		this.globalValue = globalValue;
	}

	public Message(Message socketMessage) {
		this.messageType = socketMessage.messageType;
		this.globalValue = socketMessage.globalValue;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public String getGlobalValue() {
		return globalValue;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;		
	}

	public void setGlobalValue(String globalValue) {
		this.globalValue = globalValue;	
	}

	@Override
	public String toString() {
		return "Message [messageType=" + messageType + ", globalValue=" + globalValue + "]";
	}
}
