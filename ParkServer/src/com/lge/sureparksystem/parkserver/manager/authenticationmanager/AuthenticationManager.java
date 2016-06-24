package com.lge.sureparksystem.parkserver.manager.authenticationmanager;

import java.util.Calendar;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseInfo;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseProvider;
import com.lge.sureparksystem.parkserver.manager.databasemanager.UserAccountData;
import com.lge.sureparksystem.parkserver.manager.networkmanager.SocketInfo;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageValueType;
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
			System.out.println(topic);
			
			setSessionID(topic);
			
			processMessage(topic.getJsonObject());
		}
	}
	
	@Override
	public void init() {
		dbProvider = DatabaseProvider.getInstance();
		
		registerEventBus(new AuthenticationManagerListener());
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
		DataMessage message = (DataMessage) MessageParser.convertToMessage(jsonObject);
		
		switch(message.getMessageType()) {
		case AUTHENTICATION_REQUEST:
			responseAuthentication(message);
			break;
		case CREATE_DRIVER:
			UserAccountData newUser = new UserAccountData(
					message.getName(),
					message.getID(),
					message.getPassword(),
					Calendar.getInstance().getTime(),
					DatabaseInfo.Authority.ID_TYPE.DRIVER);
			
			boolean result = dbProvider.createUserAccount(newUser);
			DataMessage sendMessage = new DataMessage(MessageType.RESPONSE);
			if(result)
				sendMessage.setResult("ok");
			else
				sendMessage.setResult("nok");
			sendMessage.setType(MessageValueType.CREATE_DRIVER);
			
			post(new ParkHereNetworkManagerTopic(sendMessage), this);	
			break;
		default:
			break;
		}		
	}

	private void responseAuthentication(DataMessage message) {
		int isValidUser = 0;
		
		int port = message.getPort();
		if(port == SocketInfo.PORT_PARKHERE) {
			isValidUser = dbProvider.verifyUser(message.getID(), message.getPassword());
		}
		else if(port == SocketInfo.PORT_PARKINGLOT ||
				port == SocketInfo.PORT_PARKVIEW) {
			isValidUser = dbProvider.verifyParkingLot(message.getID(), message.getPassword());
		}
	
		DataMessage sendMessage = null;
		sendMessage = new DataMessage(MessageType.AUTHENTICATION_RESPONSE);
		sendMessage.setAuthority(isValidUser);		
		if(isValidUser > 0)
			sendMessage.setResult("ok");
		else
			sendMessage.setResult("nok");
		
		if(port == SocketInfo.PORT_PARKHERE)
			post(new ParkHereNetworkManagerTopic(sendMessage), this);
		else if(port == SocketInfo.PORT_PARKINGLOT)
			post(new ParkingLotNetworkManagerTopic(sendMessage), this);
		else if(port == SocketInfo.PORT_PARKVIEW)
			post(new ParkViewNetworkManagerTopic(sendMessage), this);
	}	
}
