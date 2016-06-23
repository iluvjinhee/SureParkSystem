package com.lge.sureparksystem.parkview.networkmanager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkview.controller.Controller;
import com.lge.sureparksystem.parkview.message.DataMessage;
import com.lge.sureparksystem.parkview.message.MessageParser;
import com.lge.sureparksystem.parkview.message.MessageType;

public class SocketForClient {
	String dstAddress;
	int dstPort;
	
	private Controller controller;
	
	private Socket socket = null;
	private PrintWriter out = null;
	private ReceiverAsync receiver = null;
	
	private Thread socketThread = null;
	
	public SocketForClient(String addr, int port, Controller controller) {
		dstAddress = addr;
		dstPort = port;
		this.controller = controller;
	}
	
	public void connect() {
		if(socketThread == null) {
			socketThread = new Thread(new Runnable() {
			    @Override
			    public void run() {
			    	if(socket == null) {
				        try {
				        	socket = new Socket(dstAddress, dstPort);
				    		receiver = new ReceiverAsync(controller);
				    		
				        	if(socket.isConnected()) {			
				    		    out = new PrintWriter(socket.getOutputStream(), true);
				    		    
				    		    sendAuthentication(out);
				    		}
				    		
				    		receiver.execute(socket);
				        } 
				        catch (Exception e) {
				            e.printStackTrace();
				        }
			    	}
			    }
			});
		
			socketThread.start();	
		}
	}
	
	protected void sendAuthentication(PrintWriter out) {
	    DataMessage message = new DataMessage(MessageType.AUTHENTICATION_REQUEST);
	    message.setID(ConnectionInfo.id);
	    message.setPassword(ConnectionInfo.password);
	    
	    out.println(MessageParser.convertToJSONObject(message).toJSONString());
	}

	public void disconnect() {
		if(socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(receiver != null) {
			receiver.cancel(true);
		}
		
		if(socketThread != null && socketThread.isAlive()) {
			socketThread.interrupt();
		}
		socketThread = null;
	}
	
	public void send(JSONObject jsonObject) {
		out.println(jsonObject.toJSONString());
	}
}