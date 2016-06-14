package com.lge.sureparksystem.parkclientfortest.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkclientfortest.message.Message;
import com.lge.sureparksystem.parkclientfortest.message.MessageParser;
import com.lge.sureparksystem.parkclientfortest.message.MessageType;

public abstract class SocketForClient {
	String dstAddress;
	int dstPort;

	private Socket socket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;

	private Thread socketThread = null;

	public SocketForClient(String addr, int port) {
		dstAddress = addr;
		dstPort = port;
	}
	
	public abstract Message process(String jsonMessage);

	public void connect() {
		if (socketThread == null) {
			socketThread = new Thread(new Runnable() {
				@Override
				public void run() {
					if (socket == null) {
						try {
							socket = new Socket(dstAddress, dstPort);

							if (socket.isConnected()) {
								out = new PrintWriter(socket.getOutputStream(), true);
								in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

								String inJSONMessage = "";
								while (true) {
									inJSONMessage = in.readLine();
									if (inJSONMessage != null && !inJSONMessage.equals("")) {
//										System.out.println(inJSONMessage);
									}
									
									Message outJSONMessage = process(inJSONMessage);
									if(outJSONMessage != null) {
										JSONObject jsonObject = MessageParser.makeJSONObject(outJSONMessage);
										out.println(jsonObject.toJSONString());
									}
								}
							}
						} catch (Exception e) {
							System.out.println("No Server!!!");
						}
					}
				}

				
			});

			socketThread.start();
		}
	}

	public void disconnect() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (socketThread != null && socketThread.isAlive()) {
			socketThread.interrupt();
		}
		socketThread = null;
	}

	public void send(JSONObject jsonObject) {
		out.println(jsonObject.toJSONString());
	}
	
	/*
	private Message process(String jsonMessage) {
		Message result = null;
		
		MessageType messageType = MessageParser.parseJSONMessage(jsonMessage).getMessageType();
		switch(messageType) {
		case WELCOME_SUREPARK:
		case NOT_RESERVED:
			System.out.println(messageType.getText());
			break;
		case SCAN_CONFIRM:
			result = new Message(
					MessageType.RESERVATION_NUMBER,
					"{\"Name\":\"Daniel\",\"ReservationNumber\":\"1234567890\"}");
			break;
		case ASSIGN_SLOT:
			System.out.println(messageType.getText() +
					" " +
					MessageParser.parseJSONMessage(jsonMessage).getGlobalValue());
			break;
		default:
			break;						
		}
		
		return result;
	}*/
}