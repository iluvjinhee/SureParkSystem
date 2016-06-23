package com.lge.sureparksystem.parkserver.util;

public class Logger {
	public static void send(String message) {
		System.out.printf("%-20s $%40s\n", "[Server]", message);
	}

	public static void receive(int clientNumber, String message) {
		System.out.printf("%-20s $%40s\n", "[Client" + clientNumber + "]", message);
	}
	
//	public static void log(String message) {
//		System.out.println("[" + message + "]");
//	}
	

//	public static void log() {
//		StackTraceElement[] ste = (new Throwable()).getStackTrace();
//		String text = "[" + ste[1].getFileName() + ":" + ste[1].getLineNumber() + ":"
//				+ ste[1].getMethodName() + "()"
//				+ "] oooooo ";
//		System.out.println(text);
//	}

	public static void log(String message) {
		StackTraceElement[] ste = (new Throwable()).getStackTrace();
		String text = "[" + ste[1].getFileName() + ":" + ste[1].getLineNumber() + ":"
				+ ste[1].getMethodName() + "()"
				+ "] oooooo ";
		System.out.println(text + message);
	}

	public static void log(String TAG, String message) {
		StackTraceElement[] ste = (new Throwable()).getStackTrace();
		String text = "[" + ste[1].getFileName() + ":" + ste[1].getLineNumber() + ":"
				+ ste[1].getMethodName() + "()"
				+ "] oooooo ";
		System.out.println("[" + TAG + "]" + text + message);
	}
}