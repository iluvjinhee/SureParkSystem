package com.lge.sureparksystem.parkclientfortest.main.parkinglot;

import com.lge.sureparksystem.parkclientfortest.message.Message;
import com.lge.sureparksystem.parkclientfortest.message.MessageParser;
import com.lge.sureparksystem.parkclientfortest.message.MessageType;
import com.lge.sureparksystem.parkclientfortest.socket.SocketForClient;

public class SocketForParkingLot extends SocketForClient {

	public SocketForParkingLot(String addr, int port) {
		super(addr, port);
	}

	@Override
	public Message process(String jsonMessage) {
		Message result = null;

		MessageType messageType = MessageParser.getMessageType(jsonMessage);
		switch (MessageParser.getMessageType(jsonMessage)) {
		default:
			break;
		}

		return result;
	}

}
