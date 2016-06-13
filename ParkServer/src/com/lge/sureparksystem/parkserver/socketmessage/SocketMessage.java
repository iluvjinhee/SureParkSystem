package com.lge.sureparksystem.parkserver.socketmessage;

public class SocketMessage {
	public static String MESSAGE_TYPE = "MessageType";
	public static String GLOBAL_VALUE = "GlobalValue";	
	
	SocketMessageType messageType;
	String globalValue;
	
	public SocketMessage() {
		this.messageType = null;
		this.globalValue = "";
	}
	
	public SocketMessage(SocketMessageType messageType) {
		this.messageType = messageType;
		this.globalValue = "";
	}
	
	public SocketMessage(SocketMessageType messageType, String globalValue) {
		this.messageType = messageType;
		this.globalValue = globalValue;
	}

	public SocketMessage(SocketMessage socketMessage) {
		this.messageType = socketMessage.messageType;
		this.globalValue = socketMessage.globalValue;
	}

	public SocketMessageType getMessageType() {
		return messageType;
	}

	public String getGlobalValue() {
		return globalValue;
	}

	public void setMessageType(SocketMessageType messageType) {
		this.messageType = messageType;		
	}

	public void setGlobalValue(String globalValue) {
		this.globalValue = globalValue;	
	}

	@Override
	public String toString() {
		return "SocketMessage [messageType=" + messageType + ", globalValue=" + globalValue + "]";
	}
}
