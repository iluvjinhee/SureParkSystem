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
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.AuthenticationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.NetworkManagerTopic;
import com.lge.sureparksystem.parkserver.util.Logger;

public class NetworkManager extends ManagerTask implements ISocketAcceptListener {
	private int serverPort;
	protected List<SocketForServer> socketList = new ArrayList<SocketForServer>();
	
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
		SocketForServer socketForServer = new SocketForServer(this, socket);
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
	
	public void sendMessage(JSONObject jsonObject) {
		for(SocketForServer socketForServer : socketList) {
			if(socketForServer.getSocket().isConnected()) {
				socketForServer.send(jsonObject);
			}
		}
	}

	public void receiveMessage(JSONObject jsonObject) {
		processMessage(jsonObject);
	}
	
	protected void processMessage(JSONObject jsonObject) {
		MessageType messageType = MessageParser.getMessageType(jsonObject);
		DataMessage dataMessage = null;
		
		if(messageType == null) {
			System.out.println("");
			System.out.println("NOT PARSABLE MESSAGE TYPE !!!!!:");
			System.out.println(jsonObject.toJSONString());
			System.out.println("");
			
			return;
		}
		
		switch(messageType) {
		case AUTHENTICATION_REQUEST:
			jsonObject.put(DataMessage.PORT, serverPort);
			getEventBus().post(new AuthenticationManagerTopic(jsonObject));
			break;
		case AUTHENTICATION_OK:
			Logger.log("Authentication OK");
			dataMessage = new DataMessage();
			dataMessage.setMessageType(MessageType.AUTHENTICATION_RESPONSE);
			dataMessage.setResult("ok");
			sendMessage(MessageParser.convertToJSONObject(dataMessage));
			break;
		case AUTHENTICATION_FAIL:
			Logger.log("Unauthorized!!! Connection close.");
			dataMessage = new DataMessage();
			dataMessage.setMessageType(MessageType.AUTHENTICATION_RESPONSE);
			dataMessage.setResult("nok");
			sendMessage(MessageParser.convertToJSONObject(dataMessage));
			
			disconnectServer();
			break;
		default:
			break;
		}
	}
}
