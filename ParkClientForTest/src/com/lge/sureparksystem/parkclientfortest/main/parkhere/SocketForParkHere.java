package com.lge.sureparksystem.parkclientfortest.main.parkhere;

import com.lge.sureparksystem.parkclientfortest.socket.SocketForClient;
import com.lge.sureparksystem.parkserver.message.Message;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;

public class SocketForParkHere extends SocketForClient {

	public SocketForParkHere(String addr, int port) {
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

	@Override
	public void testSend() {
		Thread t = new Thread() {
		    public void run() {
		    	while(true) {
		    		out.println(TestMessage.getTestMessage());
		    		
		    		try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
		    }
		};
		t.start();
	}
}
