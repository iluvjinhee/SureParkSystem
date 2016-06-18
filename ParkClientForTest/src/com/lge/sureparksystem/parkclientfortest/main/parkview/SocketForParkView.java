package com.lge.sureparksystem.parkclientfortest.main.parkview;

import com.lge.sureparksystem.parkclientfortest.socket.SocketForClient;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.Message;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;

public class SocketForParkView extends SocketForClient {

	public SocketForParkView(String addr, int port) {
		super(addr, port);
	}

	@Override
	public Message process(String jsonMessage) {
		Message message = null;

		MessageType messageType = MessageParser.getMessageType(jsonMessage);
		switch (MessageParser.getMessageType(jsonMessage)) {
		case WELCOME_SUREPARK:
		case NOT_RESERVED:
			System.out.println(messageType.getText());
			break;
		case SCAN_CONFIRM:
			message = new DataMessage(MessageType.RESERVATION_CODE);
			((DataMessage) message).setReservationCode("{\"Name\":\"Daniel\",\"ReservationCode\":\"1234567890\"}");
			break;
		case ASSIGNED_SLOT:
			System.out.println(
					messageType.getText() + " " + ((DataMessage) MessageParser.makeMessage(jsonMessage)).getAssignedSlot());
			break;
		default:
			break;
		}

		return message;
	}

	@Override
	public void testSend() {
		// TODO Auto-generated method stub
		
	}
}
