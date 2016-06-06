package com.lge.sureparksystem.parkview.socket;

import java.io.PrintWriter;
import java.net.Socket;

import android.widget.TextView;

public class ClientSocket {
	public static final String IP_ADDRESS = "192.168.1.183";
	public static final int PORT = 9898;

	String dstAddress;
	int dstPort;
	
	private Socket socket = null;
	private PrintWriter out = null;
	private ClientSocketAsync receiver = null;
	
	Thread thread = new Thread(new Runnable()
	{
	    @Override
	    public void run() 
	    {
	    	if(socket == null) {
		        try {
		        	socket = new Socket(dstAddress, dstPort);
		        	
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
	
	public ClientSocket(String addr, int port, TextView textResponse) {
		dstAddress = addr;
		dstPort = port;
		
		receiver = new ClientSocketAsync(textResponse);
	}
	
	public void connect() {
		thread.start();
	}
	
	public void sendMsg(String msg) {
		out.println(msg);
	}
}