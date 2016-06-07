package com.lge.sureparksystem.parkview.networkmanager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.lge.sureparksystem.parkview.controller.Controller;

import android.util.Log;
import android.widget.TextView;

public class SocketForClient {
	public static final String IP_ADDRESS = "192.168.1.183";
	public static final int PORT = 9898;

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
	
	public void sendMsg(String msg) {
		out.println(msg);
	}
}