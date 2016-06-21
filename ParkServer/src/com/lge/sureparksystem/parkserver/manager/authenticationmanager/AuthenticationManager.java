package com.lge.sureparksystem.parkserver.manager.authenticationmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseProvider;
import com.lge.sureparksystem.parkserver.manager.networkmanager.SocketInfo;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.AuthenticationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkHereNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkViewNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkingLotNetworkManagerTopic;

public class AuthenticationManager extends ManagerTask {
	DatabaseProvider dbProvider = null;
	
	public class AuthenticationManagerListener {
		@Subscribe
		public void onSubscribe(AuthenticationManagerTopic topic) {
			System.out.println("AuthenticationManagerListener: " + topic);
			
			processMessage(topic.getJsonObject());
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
	protected void processMessage(JSONObject jsonObject) {
		DataMessage recvMessage = (DataMessage) MessageParser.convertToMessage(jsonObject);
		
		switch(recvMessage.getMessageType()) {
		case AUTHENTICATION_REQUEST:
			responseAuthentication(recvMessage);
			break;
		default:
			break;
		}		
	}

	private void responseAuthentication(DataMessage message) {
		boolean isValidUser = false;
		
		int port = message.getPort();
		if(port == SocketInfo.PORT_PARKHERE) {
			isValidUser = dbProvider.verifyUser(message.getDriverID(), message.getPassword());
		}
		else if(port == SocketInfo.PORT_PARKINGLOT ||
				port == SocketInfo.PORT_PARKVIEW) {
			isValidUser = dbProvider.verifyParkingLot(message.getID(), message.getPassword());
		}
	
		DataMessage sendMessage = null;
		if(isValidUser)
			sendMessage = new DataMessage(MessageType.AUTHENTICATION_OK);
		else
			sendMessage = new DataMessage(MessageType.AUTHENTICATION_FAIL);
		
		sendMessage.setID(message.getID());
		
		if(port == SocketInfo.PORT_PARKHERE)
			getEventBus().post(new ParkHereNetworkManagerTopic(sendMessage));
		else if(port == SocketInfo.PORT_PARKINGLOT)
			getEventBus().post(new ParkingLotNetworkManagerTopic(sendMessage));
		else if(port == SocketInfo.PORT_PARKVIEW)
			getEventBus().post(new ParkViewNetworkManagerTopic(sendMessage));
	}	
}
