package com.lge.sureparksystem.parkserver.communicationmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.message.SocketMessage;

public class SocketForServer extends Thread {
	private CommunicationManager mCommunicationManager = CommunicationManager.getInstance();
	
	private Socket socket = null;
	private int clientNumber;
	
	private BufferedReader in = null;
	private PrintWriter out = null;	

	public SocketForServer(Socket socket) {
		this.socket = socket;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			send(MessageParser.makeJSONObject(new SocketMessage(MessageType.WELCOME_SUREPARK)));

			while (true) {
				String input = in.readLine();
				
				if(input != null && !input.equals("")) {
					receive(input.toUpperCase());
				}
			}
		} catch (IOException e) {
			log("Error handling client# " + clientNumber + ": " + e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				log("Couldn't close a socket.");
			}
			
			log("Connection with client# " + clientNumber + " closed");
		}
	}

	public void send(JSONObject jsonObject) {
		out.println(jsonObject.toJSONString());
		
		System.out.printf("%-20s %40s\n", "[Server]", jsonObject.toJSONString());
	}

	private void receive(String jsonMessage) {
		System.out.printf("%-20s %40s\n", "[Client#" + clientNumber + "]", jsonMessage);
		
		try {
			mCommunicationManager.command((JSONObject) new JSONParser().parse(jsonMessage));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void log(String log) {
		System.out.println("[" + log + "]");
	}
}