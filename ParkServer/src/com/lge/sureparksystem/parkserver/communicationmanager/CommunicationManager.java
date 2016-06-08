package com.lge.sureparksystem.parkserver.communicationmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
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
	private List<SocketForServer> socketForServerList = null;
	private BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));
	
	private ServerSocket serverSocket;
	private int clientNumber;
	
	private ParkingLotController parkingLotController = null;
	private ReservationManager reservationManager = null;
	
	public void init() {
		parkingLotController = new ParkingLotController();
		reservationManager = new ReservationManager();
		
		socketForServerList = new ArrayList<SocketForServer>();
		
		clientNumber = 0;
		try {
			serverSocket = new ServerSocket(SocketForServer.PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		keyIn = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void showServerInfo() {
		InetAddress IP = null;
		try {
			IP = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ConsolePrint.log("IP of my system is := " + IP.getHostAddress());
		ConsolePrint.log("The server is running.");
	}
	
	public void start() throws IOException {
		Thread thread = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	String keyMsg;
		    	while(true) {
		    		try {
						keyMsg = keyIn.readLine();
						
						if(keyMsg != null) {						
							for(SocketForServer socketForServer : socketForServerList) {
								socketForServer.send(mapMessage(Integer.parseInt(keyMsg)));
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}
		    }
		});
		thread.start();
		
		try {
			while (true) {
				SocketForServer socketForServer = new SocketForServer(this, serverSocket.accept(), clientNumber++);
				socketForServerList.add(socketForServer);
				socketForServer.start();
			}
		} finally {
			serverSocket.close();
		}
	}	
	
	public JSONObject mapMessage(int messageIndex) {
		JSONObject jsonObject = null;
		
		switch(messageIndex) {
		case 1:
			jsonObject = MessageParser.makeJSONObject(new SocketMessage(MessageType.WELCOME_SUREPARK));
			break;
		case 2:
			jsonObject = MessageParser.makeJSONObject(new SocketMessage(MessageType.SCAN_CONFIRM));
			break;
		case 3:
			jsonObject = MessageParser.makeJSONObject(
					new SocketMessage(MessageType.ASSIGN_SLOT,
							String.valueOf(parkingLotController.getAvailableSlot())));
			break;
		default:
			jsonObject = MessageParser.makeJSONObject(new SocketMessage(MessageType.WELCOME_SUREPARK));
			break;
		}
		
		return jsonObject;
	}
	
	void command(JSONObject message) {
		
	}
}