package com.lge.sureparksystem.parkserver.manager.networkmanager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.message.Message;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.NetworkManagerTopic;
import com.lge.sureparksystem.parkserver.util.Logger;

public class NetworkManager extends ManagerTask implements ISocketAcceptListener {
	private int serverPort;
	protected List<SocketForServer> socketList = new ArrayList<SocketForServer>();
	JSONObject jsonObject = null;
	
	public class NetworkManagerListener {
		@Subscribe
		public void onSubscribe(NetworkManagerTopic topic) {
			System.out.println("NetworkManagerListener");
			System.out.println(topic);
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
			System.exit(0);
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

	public void receive(JSONObject jsonObject) {
		getEventBus().post(new CommunicationManagerTopic(jsonObject));
		
		process(jsonObject);
	}

	private void process(JSONObject jsonObject) {
		switch(MessageParser.getMessageType(jsonObject)) {
		case ACKNOWLEDGE:
			if(jsonObject.get(Message.TIMESTAMP) != null) {
				int timestamp = (int) jsonObject.get(Message.TIMESTAMP);
				JSONObject ackJSONObject = MessageParser.makeJSONObject(
						new Message(MessageType.ACKNOWLEDGE, timestamp));
				
				send(ackJSONObject);
			}
			break;
		default:
			break;
		}
	}
}
