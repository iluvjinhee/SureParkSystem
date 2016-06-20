package com.lge.sureparksystem.parkclientfortest.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.Message;
import com.lge.sureparksystem.parkserver.message.MessageParser;

public abstract class SocketForClient {
	String dstAddress;
	int dstPort;

	private Socket socket = null;
	private BufferedReader in = null;
	protected PrintWriter out = null;

	private Thread socketThread = null;

	public SocketForClient(String addr, int port) {
		dstAddress = addr;
		dstPort = port;
	}
	
	public abstract Message process(String jsonMessage);
	public abstract void testSend();

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
								
								System.out.println("Connected to Server");
								testSend();

								String inJSONMessage = "";
								while (true) {
									inJSONMessage = in.readLine();
									
									if(inJSONMessage == null)
										continue;
									
									if (!inJSONMessage.equals("")) {
										System.out.println(inJSONMessage);
									}
									
									Message outJSONMessage = process(inJSONMessage);
									if(outJSONMessage != null) {
										JSONObject jsonObject = MessageParser.convertToJSONObject(outJSONMessage);
										send(jsonObject.toJSONString());
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
	
	public void send(String jsonMessage) {
		out.println(jsonMessage);
	}
}