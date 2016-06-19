package com.lge.sureparksystem.parkserver.manager.authenticationmanager;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseProvider;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.AuthenticationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkingLotNetworkManagerTopic;

public class AuthenticationManager extends ManagerTask {
	DatabaseProvider dbProvider = null;
	
	public class AuthenticationManagerListener {
		@Subscribe
		public void onSubscribe(AuthenticationManagerTopic topic) {
			System.out.println("AuthenticationManagerListener: " + topic);
			
			process(topic);
		}
	}
	
	@Override
	public void init() {
		dbProvider = DatabaseProvider.getInstance();
		
		getEventBus().register(new AuthenticationManagerListener());
	}
	
	@Override
	public void run() {
		while(loop) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void process(ManagerTopic topic) {
		DataMessage recvMessage = (DataMessage) MessageParser.convertToMessage(topic.getJsonObject());
		
		switch(recvMessage.getMessageType()) {
		case AUTHENTICATION_REQUEST:
			boolean isValidUser = dbProvider.verifyParkingLot(recvMessage.getId(), recvMessage.getPwd());
			if(isValidUser) {
				DataMessage sendMessage = new DataMessage(MessageType.AUTHENTICATION_OK);
				sendMessage.setId(recvMessage.getId());
				getEventBus().post(new ParkingLotNetworkManagerTopic(sendMessage));
			}
			else {
				DataMessage sendMessage = new DataMessage(MessageType.AUTHENTICATION_FAIL);
				sendMessage.setId(recvMessage.getId());
				getEventBus().post(new ParkingLotNetworkManagerTopic(sendMessage));
			}
			break;
		default:
			break;
		}		
	}	
}
