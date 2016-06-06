package com.lge.sureparksystem.parkserver;

public class ConsolePrint {
	public static void send(String message) {
		System.out.printf("%-20s $%40s\n", "[Server]", message);
	}

	public static void receive(int clientNumber, String message) {
		System.out.printf("%-20s $%40s\n", "[Client" + clientNumber + "]", message);
	}
	
	public static void log(String message) {
		System.out.println("[" + message + "]");
	}
}