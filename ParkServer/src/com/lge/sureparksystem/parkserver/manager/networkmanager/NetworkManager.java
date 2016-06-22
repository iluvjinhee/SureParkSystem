package com.lge.sureparksystem.parkserver.manager.networkmanager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.Message;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.AuthenticationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.NetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkingLotNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkingLotWatchDogTopic;
import com.lge.sureparksystem.parkserver.util.Logger;

public class NetworkManager extends ManagerTask implements ISocketAcceptListener {
	private int serverPort;
	private String id = null;
	protected List<SocketForServer> socketList = new ArrayList<SocketForServer>();
	SocketForServer currentSocketForServer = null;
	
	public class NetworkManagerListener {
		@Subscribe
		public void onSubscribe(NetworkManagerTopic topic) {
			System.out.println("NetworkManagerListener: " + topic);
			
			processMessage(topic.getJsonObject());
		}
	}
	
	@Override
	public void init() {
		getEventBus().register(new NetworkManagerListener());
	}
	
    public NetworkManager(int serverPort) {
    	super();
    	
    	this.serverPort = serverPort;
	}
    
    @Override
	public void onSocketAccepted(Socket socket) {
		currentSocketForServer = new SocketForServer(this, socket);
		socketList.add(currentSocketForServer);
		
		Thread thread = new Thread(currentSocketForServer, "Socket");
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
		if(port == SocketInfo.PORT_PARKINGLOT) {
			getEventBus().post(new ParkingLotNetworkManagerTopic(new Message(MessageType.RESET)));
		}
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
	}
	
	public void send(JSONObject jsonObject) {
		for(SocketForServer socketForServer : socketList) {
			if(socketForServer.getSocket().isConnected()) {
				socketForServer.send(jsonObject);
			}
		}
	}
	
	public void send(Message message) {
		JSONObject jsonObject = MessageParser.convertToJSONObject(message);
		
		for(SocketForServer socketForServer : socketList) {
			if(socketForServer.getSocket().isConnected()) {
				socketForServer.send(jsonObject);
			}
		}
	}

	public void receive(JSONObject jsonObject) {
		processMessage(jsonObject);
	}
	
	protected void processMessage(JSONObject jsonObject) {
		DataMessage message = (DataMessage) MessageParser.convertToMessage(jsonObject);
		MessageType messageType = message.getMessageType();

		switch(messageType) {
		case HEARTBEAT:
			DataMessage sendMessage = (DataMessage) new Message(MessageType.ACKNOWLEDGE, message.getTimestamp());
			send(sendMessage);
			
			getEventBus().post(new ParkingLotWatchDogTopic(jsonObject));
			break;
		case AUTHENTICATION_REQUEST:
			id = message.getID();
			jsonObject.put(DataMessage.PORT, serverPort);
			getEventBus().post(new AuthenticationManagerTopic(jsonObject));
			break;
		case AUTHENTICATION_RESPONSE:
			if(message.getResult().equalsIgnoreCase("ok")) {
				Logger.log("Authentication OK !!!");
				
				setID(id);
			} else if(message.getResult().equalsIgnoreCase("nok")) {
				Logger.log("Unauthentication !!!");
				//disconnectServer();
			}
			
			id = null;			
			send(jsonObject);
			
			break;
		default:
			break;
		}
	}

	private void setID(String id) {
		currentSocketForServer.setID(id);
	}
	
	protected String getID() {
		return currentSocketForServer.getID();
	}
}
