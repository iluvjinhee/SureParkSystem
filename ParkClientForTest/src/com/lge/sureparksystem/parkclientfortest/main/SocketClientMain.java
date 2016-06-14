package com.lge.sureparksystem.parkclientfortest.main;

public class SocketClientMain {
	public static final String IP_ADDRESS = "localhost";
	public static final int PORT = 9898;

	public static void main(String[] args) {
		SocketForClient socketForClient = new SocketForClient(IP_ADDRESS, PORT);

		socketForClient.connect();
	}
}
