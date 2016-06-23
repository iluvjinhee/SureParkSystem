package com.lge.sureparksystem.parkserver.manager.networkmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.AuthenticationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkHereNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ReservationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.StatisticsManagerTopic;

public class ParkHereNetworkManager extends NetworkManager {
	public class ParkHereNetworkManagerListener {
		@Subscribe
		public void onSubscribe(ParkHereNetworkManagerTopic topic) {
			System.out.println("ParkHereNetworkManagerListener: " + topic);
			
			processMessage(topic.getJsonObject());
		}
	}
	
	@Override
	public void init() {
		getEventBus().register(new ParkHereNetworkManagerListener());
	}
	
	public ParkHereNetworkManager(int serverPort) {
		super(serverPort);
	}
	
	public void run() {
		super.run();
	}
	
	protected void processMessage(JSONObject jsonObject) {
		super.processMessage(jsonObject);
		
		MessageType messageType = MessageParser.getMessageType(jsonObject);
		
		switch (messageType) {
		case CREATE_DRIVER:
		case CREATE_ATTENDANT:
		case REMOVE_ATTENDANT:
			getEventBus().post(new AuthenticationManagerTopic(jsonObject));
			break;
		case PARKING_LOT_INFO_REQUEST:
		case PARKING_LOT_STATUS_REQUEST:
		case RESERVATION_REQUEST:
		case RESERVATION_INFO_REQUEST:
		case CANCEL_REQUEST:
		case CHANGE_PARKING_FEE:
		case CHANGE_GRACE_PERIOD:
		case ADD_PARKING_LOT:
		case REMOVE_PARKING_LOT:
			getEventBus().post(new ReservationManagerTopic(jsonObject));
			break;
		case RESPONSE:
		case PARKING_LOT_LIST:
		case PARKING_LOT_STATUS:
		case NOTIFICATION:
		case PARKING_LOT_STATISTICS:
		case CHANGE_RESPONSE:
		case RESERVATION_INFORMATION:
			send(jsonObject);
			break;
		case PARKING_LOT_STATS_REQUEST:
//			getEventBus().post(new StatisticsManagerTopic(jsonObject));
			getEventBus().post(new ReservationManagerTopic(jsonObject));
			break;
		default:
			break;
		}
		
		return;
	}
}