package com.lge.sureparksystem.parkserver.manager.networkmanager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseInfo;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseProvider;
import com.lge.sureparksystem.parkserver.manager.databasemanager.ParkingLotData;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.Message;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.AuthenticationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.NetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkingLotWatchDogTopic;
import com.lge.sureparksystem.parkserver.util.Logger;

public class NetworkManager extends ManagerTask implements ISocketAcceptListener {
	private int serverPort;
	protected List<SocketForServer> socketList = new ArrayList<SocketForServer>();
	SocketForServer socketForServer = null;
	
	public class NetworkManagerListener {
		@Subscribe
		public void onSubscribe(NetworkManagerTopic topic) {
			System.out.println("NetworkManagerListener: " + topic);
			
			setSessionID(topic);
			
			processMessage(topic.getJsonObject());
		}
	}
	
	@Override
	public void init() {
		registerEventBus(new NetworkManagerListener());
	}
	
	public NetworkManager(int serverPort) {
    	super();
    	
    	this.serverPort = serverPort;
	}
    
    @Override
	public void onSocketAccepted(Socket socket) {
		socketForServer = new SocketForServer(this, socket);
		socketList.add(socketForServer);
		
		Thread thread = new Thread(socketForServer, "Socket");
		thread.start();
	}

	@Override
	public void run() {
		Socket socket = null;
		ServerSocket serverSocket = null;
		
		while(true) {
			try {
				serverSocket = new ServerSocket(serverPort);
				
				while (loop) {
					socket = serverSocket.accept();
					
					showConnectionInfo(socket.getLocalPort());
					
					onSocketAccepted(socket);
					
					initClient(socket.getLocalPort());
				}
			} catch (IOException e) {
				if(e.toString().contains("Address already in use")) {
					System.out.println("Server already running !!!");
				}				
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						System.out.println(e.toString());
						e.printStackTrace();
					}
				}
				
				System.out.println("serverSocket Close.");
			}
		}
	}
	
	private void initClient(int port) {
		/*
		 if(port == SocketInfo.PORT_PARKINGLOT) {
			post(new ParkingLotNetworkManagerTopic(new Message(MessageType.RESET)), this);
		}
		*/
	}

	private void showConnectionInfo(int i) {
		if(i == SocketInfo.PORT_PARKVIEW) {
			Logger.log("Connected to ParkView!");
		}
		else if(i == SocketInfo.PORT_PARKHERE) {
			Logger.log("Connected to ParkHere!");
		}
		else if(i == SocketInfo.PORT_PARKINGLOT) {
			Logger.log("Connected to ParkingLot!");
		}
		else {
			Logger.log("Connected...");
		}		
	}
	
	public void disconnectServer() {
		for(SocketForServer socketForServer : socketList) {
			if(socketForServer.getSocket().isConnected()) {
				try {
					socketForServer.getSocket().close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void removeSocket(SocketForServer socketForServer) {
		socketForServer.destroy();
		socketList.remove(socketForServer);
		socketForServer = null;
	}
	
	public void send(JSONObject jsonObject) {
		for(SocketForServer socketForServer : socketList) {
			if(socketForServer.getSocket().isConnected()) {
				if(getSessionID() != null &&
				   socketForServer.getSocketID() != null &&
				   socketForServer.getSocketID().equals(getSessionID())) {
					
					System.out.println(socketForServer.getSocketID() + ": " + jsonObject);
					
					socketForServer.send(jsonObject);
				}
			}
		}
	}
	
	public void sendToAttendant(JSONObject jsonObject) {
		for (SocketForServer socketForServer : socketList) {
			if (socketForServer.getSocket().isConnected()) {
				ParkingLotData parkingData = DatabaseProvider.getInstance()
						.getParkingLotInfo(getSessionID());
				if (parkingData != null) {
					String attendantID = parkingData.getUserEmail();
					if (attendantID != null &&
							socketForServer.getSocketID() != null &&
							socketForServer.getSocketID().equals(attendantID)) {
						System.out.println(socketForServer.getSocketID() + ": " + jsonObject);

						socketForServer.send(jsonObject);
					}
				}
			}
		}
	}
	
	public void send(Message message) {
		send(MessageParser.convertToJSONObject(message));
	}

	public void receive(JSONObject jsonObject, String socketID) {
		setSessionID(socketID);
		
		processMessage(jsonObject);
	}
	
	protected void processMessage(JSONObject jsonObject) {
		DataMessage message = (DataMessage) MessageParser.convertToMessage(jsonObject);
		MessageType messageType = message.getMessageType();

		switch(messageType) {
		case HEARTBEAT:
			DataMessage sendMessage = new DataMessage(MessageType.ACKNOWLEDGE, message.getTimestamp());
			send(sendMessage);
			
			post(new ParkingLotWatchDogTopic(jsonObject), this);
			break;
		case AUTHENTICATION_REQUEST:
			jsonObject.put(DataMessage.PORT, serverPort);
//			setSessionID(getSocketID());
			
			post(new AuthenticationManagerTopic(jsonObject), this);
			break;
		case AUTHENTICATION_RESPONSE:
			if(message.getResult().equalsIgnoreCase("ok")) {
				Logger.log("Authentication OK !!!");
			} else if(message.getResult().equalsIgnoreCase("nok")) {
				Logger.log("Unauthentication !!!");
				//disconnectServer();
			}
			
			if(message.getAuthority() == DatabaseInfo.Authority.ID_TYPE.ATTENDANT)
				setAttendantSocket(true);
			else
				setAttendantSocket(false);
			
			send(jsonObject);
			
			break;
		default:
			break;
		}
	}

	private void setSocketID(String id) {
		socketForServer.setSocketID(id);
	}
	
	protected String getSocketID() {
		return socketForServer.getSocketID();
	}
	
	private void setAttendantSocket(boolean bAttendant) {
		socketForServer.setAttendant(bAttendant);
	}
}
