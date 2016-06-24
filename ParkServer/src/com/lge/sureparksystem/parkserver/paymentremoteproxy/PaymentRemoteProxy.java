package com.lge.sureparksystem.parkserver.paymentremoteproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.lge.sureparksystem.parkserver.manager.networkmanager.SocketInfo;

public class PaymentRemoteProxy {
	public static boolean isVaildCreditCard(String cardNumber) {
		Socket socket;
		String answer = "";
		
		try {
			socket = new Socket("localhost", SocketInfo.PORT_PAYMENT_SYSTEM);
			
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			out.println(cardNumber);
			
			answer = input.readLine();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(answer.equalsIgnoreCase("OK")) {
			return true;
		}
		
		return false;
	}
	
	public static boolean pay(String cardNumber, int amount) {
		Socket socket;
		String answer = "";
		
		try {
			socket = new Socket("localhost", SocketInfo.PORT_PAYMENT_SYSTEM);
			
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			out.println(cardNumber);
			
			answer = input.readLine();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(answer.equalsIgnoreCase("OK")) {
			PaymentSound.soundOk();
			
			return true;
		}
		else {
			PaymentSound.soundFail();
		}
		
		return false;
	}
}
