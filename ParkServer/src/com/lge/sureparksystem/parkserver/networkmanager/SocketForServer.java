package com.lge.sureparksystem.parkserver.networkmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.lge.sureparksystem.parkserver.event.ParkViewEvent;

public class SocketForServer extends Thread {
	
	public static final int PORT = 9898;
	
	private Socket socket = null;
	private int clientNumber;
	
	private BufferedReader in = null;
	private PrintWriter out = null;	

	public SocketForServer(Socket socket, int clientNumber) {
		this.socket = socket;
		this.clientNumber = clientNumber;

		log("New connection with client# " + clientNumber + " at " + socket);
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			send(ParkViewEvent.WELCOME_SUREPARK.toString());

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

	public void send(String message) {
		out.println(message);
		
		System.out.printf("%-20s %40s\n", "[Server]", message);
	}

	private void receive(String message) {
		System.out.printf("%-20s %40s\n", "[Client#" + clientNumber + "]", message);
	}
	
	private void log(String message) {
		System.out.println("[" + message + "]");
	}
}