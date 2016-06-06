package com.lge.sureparksystem.parkserver.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

			send("Sure Park System");

			while (true) {
				String input = in.readLine();
				
				receive(input.toUpperCase());
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