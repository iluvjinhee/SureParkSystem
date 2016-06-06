package com.lge.sureparksystem.parkserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import com.lge.sureparksystem.parkserver.socket.SocketForServer;

public class Main {
	static List<SocketForServer> socketForServerList = null;
	static BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) throws Exception {
		InetAddress IP = InetAddress.getLocalHost();
		ConsolePrint.log("IP of my system is := " + IP.getHostAddress());
		ConsolePrint.log("The server is running.");
		
		List<SocketForServer> socketForServerList = new ArrayList<SocketForServer>();
		
		int clientNumber = 0;
		ServerSocket serverSocket = new ServerSocket(SocketForServer.PORT);
		
		keyIn = new BufferedReader(new InputStreamReader(System.in));
		
		Thread thread = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	String keyMsg;
		    	while(true) {
		    		try {
						keyMsg = keyIn.readLine();
						
						if(keyMsg != null) {						
							for(SocketForServer socketForServer : socketForServerList) {
								socketForServer.send(keyMsg);
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
				SocketForServer socketForServer = new SocketForServer(serverSocket.accept(), clientNumber++);
				socketForServerList.add(socketForServer);
				socketForServer.start();
			}
		} finally {
			serverSocket.close();
		}
	}
}