package com.lge.sureparksystem.parkserver.manager.networkmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.lge.sureparksystem.parkserver.util.Logger;

public class SocketForServer implements Runnable {
	private NetworkManager manager = null;
	private Socket socket = null;
	
	private BufferedReader in = null;
	private PrintWriter out = null;	

	public SocketForServer(NetworkManager manager, Socket socket) {
		this.manager = manager;
		this.socket = socket;
	}
	
	public void destroy() {
		socket = null;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void send(JSONObject jsonObject) {
		out.println(jsonObject.toJSONString());
		
		System.out.printf("%-20s %40s\n", "[SEND]", jsonObject.toJSONString());
	}

	private void receive(String jsonMessage) {
		System.out.printf("%-20s %40s\n", "[RECV]", jsonMessage);
		
		try {
			manager.receive((JSONObject) new JSONParser().parse(jsonMessage));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			while (true) {
				String input = in.readLine();
				System.out.println(input);
				
				input = input.toUpperCase();
				
				if(input != null && !input.equals("")) {
					receive(input);
				}
			}
		} catch (IOException e) {
//			Log.log("Error handling client: " + e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				Logger.log("Couldn't close a socket.");
			}
			
			showDisconnectionInfo();
			
			manager.removeSocket(this);
		}
	}

	private void showDisconnectionInfo() {
		if(manager instanceof ParkViewNetworkManager) {
			System.out.println("Connection with ParkView closed!");
		}
		else if(manager instanceof ParkHereNetworkManager) {
			System.out.println("Connection with ParkHere closed!");
		}
		else if(manager instanceof ParkingLotNetworkManager) {
			System.out.println("Connection with ParkingLot closed!");
		}
		else {
			Logger.log("Connection with client closed!");
		}		
	}
}