package com.lge.sureparksystem.parkview.networkmanager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkview.controller.Controller;
import com.lge.sureparksystem.parkview.message.DataMessage;
import com.lge.sureparksystem.parkview.message.MessageParser;
import com.lge.sureparksystem.parkview.message.MessageType;

import android.util.Log;

public class SocketForClient {
	private final String TAG = "ParkView";
	
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
				        		Log.d(TAG, "Connected to server");
				        		
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
		Log.d(TAG, "sendAuthentication");
		
	    DataMessage message = new DataMessage(MessageType.AUTHENTICATION_REQUEST);
	    message.setID(ConnectionInfo.id);
	    message.setPassword(ConnectionInfo.password);
	    
	    out.println(MessageParser.convertToJSONObject(message).toJSONString());
	}

	public void disconnect() {
		if(socket != null) {
			try {
				socket.close();
				
				Log.d(TAG, "Socket is closed");
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
		Log.d(TAG, "send: " + jsonObject.toJSONString());
		
		out.println(jsonObject.toJSONString());
	}
}