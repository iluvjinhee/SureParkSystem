package com.lge.sureparksystem.parkserver.communicationmanager;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.message.SocketMessage;
import com.lge.sureparksystem.parkserver.parkinglotcontroller.ParkingLotController;
import com.lge.sureparksystem.parkserver.reservationmanager.ReservationManager;

public class CommunicationManager {
		
	static CommunicationManager mInstance = new CommunicationManager();
	
	WaitThreadForConnect mWaitForParkingLot = null;
	WaitThreadForConnect mWaitForParkView = null;
	WaitThreadForConnect mWaitForParkHere = null;
	List<SocketForServer> mParkingLotSocketList = new ArrayList<SocketForServer>();
	List<SocketForServer> mParkViewSocketList = new ArrayList<SocketForServer>();
	List<SocketForServer> mParkHereSocketList = new ArrayList<SocketForServer>();
	InetAddress mIP = null;
	
    private SocketAcceptListener mSocketAcceptListener = new SocketAcceptListener();

    private class SocketAcceptListener implements ISocketAcceptListener {
        @Override
		public void onSocketAccepted(int type, Socket socket) {
        	SocketForServer socketForServer;
        	switch (type) {
			case SocketInfo.SOCKET_PARKINGLOT:
				socketForServer = new SocketForServer(socket);
				mParkingLotSocketList.add(socketForServer);
				socketForServer.start();
				break;
			case SocketInfo.SOCKET_PARKVIEW:
				socketForServer = new SocketForServer(socket);
				mParkViewSocketList.add(socketForServer);
				socketForServer.start();
				break;
			case SocketInfo.SOCKET_PARKHERE:
				socketForServer = new SocketForServer(socket);
				mParkHereSocketList.add(socketForServer);
				socketForServer.start();
				break;
			default:
				break;
			}
		}
    }
	
	private CommunicationManager() {
		//for single tone
	}
	
	public static CommunicationManager getInstance() {
		return mInstance;
	}
	
	public void init() {
		System.out.println("CommunicationManager::init()");
		try {
			mIP = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		ConsolePrint.log("IP of my system is := " + mIP.getHostAddress());
		ConsolePrint.log("The server is running.");
		
		initParkkingLotSocket();
		initParkViewSocket();
		initParkHereSocket();
	}
	
	private void initParkkingLotSocket() {		
		mWaitForParkView = new WaitThreadForConnect(SocketInfo.PORT_PARKINGLOT, SocketInfo.SOCKET_PARKINGLOT);
		mWaitForParkView.setSocketAcceptListener(mSocketAcceptListener);
		mWaitForParkView.start();		
	}
	
	private void initParkViewSocket() {		
		mWaitForParkView = new WaitThreadForConnect(SocketInfo.PORT_PARKVIEW, SocketInfo.SOCKET_PARKVIEW);
		mWaitForParkView.setSocketAcceptListener(mSocketAcceptListener);
		mWaitForParkView.start();		
	}
	
	private void initParkHereSocket() {		
		mWaitForParkView = new WaitThreadForConnect(SocketInfo.PORT_PARKHERE, SocketInfo.SOCKET_PARKHERE);
		mWaitForParkView.setSocketAcceptListener(mSocketAcceptListener);
		mWaitForParkView.start();		
	}
	
	public JSONObject mapMessage(int messageIndex, String data) {
		JSONObject jsonObject = null;

		switch (messageIndex) {
		case 1:
			jsonObject = MessageParser.makeJSONObject(new SocketMessage(MessageType.WELCOME_SUREPARK));
			break;
		case 2:
			jsonObject = MessageParser.makeJSONObject(new SocketMessage(MessageType.SCAN_CONFIRM));
			break;
		case 3:
			jsonObject = MessageParser.makeJSONObject(new SocketMessage(MessageType.ASSIGN_SLOT, data));
			break;
		default:
			jsonObject = MessageParser.makeJSONObject(new SocketMessage(MessageType.WELCOME_SUREPARK));
			break;
		}

		return jsonObject;
	}
	
	public void command(JSONObject jsonObject) {
		SocketMessage socketMessage = MessageParser.parseJSONObject(jsonObject);
		
		switch(socketMessage.getMessageType()) {
		case RESERVATION_NUMBER:
			// Bidirectional 			
			ReservationManager reservationManager = new ReservationManager();
			ParkingLotController parkingLotController = new ParkingLotController();
			
			boolean isValid = reservationManager.isValid(socketMessage.getGlobalValue());
			if(isValid) {
				for(SocketForServer socketForParkView : mParkViewSocketList) {
					socketForParkView.send(MessageParser.makeJSONObject(
							new SocketMessage(MessageType.ASSIGN_SLOT, String.valueOf(parkingLotController.getAvailableSlot()))));
				}
			}
			else {
				for(SocketForServer socketForParkView : mParkViewSocketList) {
					socketForParkView.send(MessageParser.makeJSONObject(
							new SocketMessage(MessageType.NOT_RESERVED)));
				}
			}
			break;
		default:
			break;
		}
	}
	
	public void sendMessage(int clientType, int messageIndex) {
		sendMessage(clientType, messageIndex, null);
	}
	
	public void sendMessage(int clientType, int messageIndex, String data) {
		System.out.println("sendMessage : " + "clientType = " + clientType +  ", messageIndex = " + messageIndex + ", data = " + data);
		
		switch (clientType) {
		case SocketInfo.SOCKET_PARKINGLOT:
			break;
		case SocketInfo.SOCKET_PARKVIEW:
			for(SocketForServer socketForServer : mParkViewSocketList) {
				socketForServer.send(mapMessage(messageIndex, data));
			}
			break;
		case SocketInfo.SOCKET_PARKHERE:
			break;
		default:
			break;
		}
	}
}
