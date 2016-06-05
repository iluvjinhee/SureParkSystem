package com.lge.sureparksystem.parkserver.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server program which accepts requests from clients to capitalize strings.
 * When clients connect, a new thread is started to handle an interactive dialog
 * in which the client sends in a string and the server thread sends back the
 * capitalized version of the string.
 *
 * The program is runs in an infinite loop, so shutdown in platform dependent.
 * If you ran it from a console window with the "java" interpreter, Ctrl+C
 * generally will shut it down.
 */
public class SocketServer extends Thread {
	
	public static final int PORT = 9898;
	
	private Socket socket;
	private int clientNumber;

	public SocketServer(Socket socket, int clientNumber) {
		this.socket = socket;
		this.clientNumber = clientNumber;

		log("New connection with client# " + clientNumber + " at " + socket);
	}

	/**
	 * Services this thread's client by first sending the client a welcome
	 * message then repeatedly reading strings and sending back the capitalized
	 * version of the string.
	 */
	public void run() {
		try {
			// Decorate the streams so we can send characters
			// and not just bytes. Ensure output is flushed
			// after every newline.
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			// Send a welcome message to the client.
			out.println("Hello, you are client #" + clientNumber + ".");

			// Get messages from the client, line by line; return them
			// capitalized
			while (true) {
				String input = in.readLine();
				if (input == null || input.equals(".")) {
					break;
				}
				
				out.println(input.toUpperCase());
			}
		} catch (IOException e) {
			log("Error handling client# " + clientNumber + ": " + e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				log("Couldn't close a socket, what's going on?");
			}
			
			log("Connection with client# " + clientNumber + " closed");
		}
	}

	/**
	 * Logs a simple message. In this case we just write the message to the
	 * server applications standard output.
	 */
	private void log(String message) {
		System.out.println(message);
	}
}