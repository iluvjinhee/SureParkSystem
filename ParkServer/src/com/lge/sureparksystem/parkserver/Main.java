package com.lge.sureparksystem.parkserver;

import java.net.InetAddress;
import java.net.ServerSocket;

import com.lge.sureparksystem.parkserver.socket.SocketServer;

public class Main {
	 /**
     * Application method to run the server runs in an infinite loop
     * listening on port 9898.  When a connection is requested, it
     * spawns a new thread to do the servicing and immediately returns
     * to listening.  The server keeps a unique client number for each
     * client that connects just to show interesting logging
     * messages.  It is certainly not necessary to do this.
     */
    public static void main(String[] args) throws Exception {        
        InetAddress IP = InetAddress.getLocalHost();
        System.out.println("IP of my system is := "+IP.getHostAddress());        
        System.out.println("The server is running.");
        
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(SocketServer.PORT);
        try {
            while (true) {
                new SocketServer(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }
}