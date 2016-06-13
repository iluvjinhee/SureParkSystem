package com.lge.sureparksystem.parkserver.manager.networkmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.lge.sureparksystem.parkserver.socketmessage.SocketMessage;
import com.lge.sureparksystem.parkserver.socketmessage.SocketMessageParser;
import com.lge.sureparksystem.parkserver.socketmessage.SocketMessageType;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ReservationManagerTopic;
import com.lge.sureparksystem.parkserver.util.Log;

public class SocketForServer implements Runnable {
	private NetworkManager manager = null;
	private Socket socket = null;
	
	private BufferedReader in = null;
	private PrintWriter out = null;	

	public SocketForServer(NetworkManager manager, Socket socket) {
		this.manager = manager;
		this.socket = socket;
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
			manager.getEventBus().post(new CommunicationManagerTopic(((JSONObject) new JSONParser().parse(jsonMessage))));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			send(SocketMessageParser.makeJSONObject(new SocketMessage(SocketMessageType.WELCOME_SUREPARK)));

			while (true) {
				String input = in.readLine();
				
				if(input != null && !input.equals("")) {
					receive(input);
				}
			}
		} catch (IOException e) {
			Log.log("Error handling client: " + e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				Log.log("Couldn't close a socket.");
			}
			
			Log.log("Connection with client closed");
		}
	}
}