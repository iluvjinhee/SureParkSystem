package com.lge.sureparksystem.parkclientfortest.main.parkview;

import com.lge.sureparksystem.parkclientfortest.message.Message;
import com.lge.sureparksystem.parkclientfortest.message.MessageParser;
import com.lge.sureparksystem.parkclientfortest.message.MessageType;
import com.lge.sureparksystem.parkclientfortest.socket.SocketForClient;

public class SocketForParkView extends SocketForClient {

	public SocketForParkView(String addr, int port) {
		super(addr, port);
	}

	@Override
	public Message process(String jsonMessage) {
		Message result = null;

		MessageType messageType = MessageParser.getMessageType(jsonMessage);
		switch (MessageParser.getMessageType(jsonMessage)) {
		case WELCOME_SUREPARK:
		case NOT_RESERVED:
			System.out.println(messageType.getText());
			break;
		case SCAN_CONFIRM:
			result = new Message(MessageType.RESERVATION_NUMBER,
					"{\"Name\":\"Daniel\",\"ReservationNumber\":\"1234567890\"}");
			break;
		case ASSIGN_SLOT:
			System.out.println(
					messageType.getText() + " " + MessageParser.makeMessage(jsonMessage).getGlobalValue());
			break;
		default:
			break;
		}

		return result;
	}
}
