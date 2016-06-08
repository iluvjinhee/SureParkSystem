package com.lge.sureparksystem.parkserver.communicationmanager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WaitThreadForConnect extends Thread {
	
	int mServerPort;
	int mclientType;
	ISocketAcceptListener mSocketAcceptListener;
	
	public WaitThreadForConnect(int serverPort, int clientType) {
		mServerPort = serverPort;
		mclientType = clientType;
	}
	
	public void setSocketAcceptListener(ISocketAcceptListener listener) {
		mSocketAcceptListener = listener;
	}

	@Override
	public void run() {
		ServerSocket serverSocket = null;
		Socket client = null;
        try {
            System.out.println("Connecting...");
            serverSocket = new ServerSocket(mServerPort);
 
            while (true) {
                client = serverSocket.accept();	//waiting for connection request
                System.out.println("Connected...");
                mSocketAcceptListener.onSocketAccepted(mclientType, client);

            }
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        } finally {
        	if (client != null) {
	            try {
					client.close();
				} catch (IOException e) {
					System.out.println(e.toString());
					e.printStackTrace();
				}
        	}
            System.out.println("serverSocket Close.");
        }
		
	}

}
