package com.lge.sureparksystem.parkview.controller;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.Message;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkview.FullscreenActivity;
import com.lge.sureparksystem.parkview.networkmanager.SocketForClient;
import com.lge.sureparksystem.parkview.qrcode.IntentIntegrator;
import com.lge.sureparksystem.parkview.qrcode.IntentResult;
import com.lge.sureparksystem.parkview.tts.TTSWrapper;

import android.content.Intent;
import android.util.Log;

public class Controller {
	private FullscreenActivity fullScreen = null;	
	private SocketForClient clientSocket = null;	
	private TTSWrapper tts = null;
	private IntentIntegrator intentIntegrator = null;
	
	public Controller(FullscreenActivity fullScreenActivity) {
		this.fullScreen = fullScreenActivity;
		
		tts = new TTSWrapper(fullScreen.getApplicationContext());
		intentIntegrator = new IntentIntegrator(fullScreen);
		
		connectServer();
		
		welcome();
	}
	
	public void destroy() {
		disconnectServer();
		
		fullScreen = null;	
		tts = null;
		intentIntegrator = null;
	}
	
	public void connectServer() {
		Log.d("ParkView", "connectServer");
		
		if(clientSocket == null) {
			clientSocket = new SocketForClient(SocketForClient.IP_ADDRESS, SocketForClient.PORT, this);
			clientSocket.connect();
		}
	}
	
	public void disconnectServer() {
		Log.d("ParkView", "disconnectServer");
		
		if(clientSocket != null) {
			clientSocket.disconnect();
			clientSocket = null;
		}
	}
	
	public void parseActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanResult != null) {
			// handle scan result
			String qrcode =  scanResult.getContents();
			if(qrcode != null) {
				DataMessage dataMessage = new DataMessage(MessageType.CONFIRMATION_SEND);
				dataMessage.setConfirmationInfo(qrcode);
				
				clientSocket.send(MessageParser.convertToJSONObject(dataMessage));			
			}
		}
	}

	public void welcome() {
		String msg = "Welcome!\n Sure Park.";
		
		fullScreen.setDisplay(msg, 50);				
		tts.speak(msg);
	}
	
	public void assignSlot(int slotNumber) {
		String msg = "Hello, Your Park Slot is ";		
		
		fullScreen.setDisplay(String.valueOf(slotNumber), 250);				
		tts.speak(msg + slotNumber);
	}
	
	public void notReserved() {
		String msg = "Sorry. You're not reserved.";	
	    
		fullScreen.setDisplay("You're not reserved.", 50);	
		tts.speak(msg);
	}
	
	public void scanConfirmation() {
		String msg = "Scan your confirmation number.";	
	    
		tts.speak(msg);
		intentIntegrator.initiateScan();
	}

	public void processMessage(String jsonMessage) {
		Message message = MessageParser.convertToMessage(jsonMessage);
		
		switch(message.getMessageType()) {
		case WELCOME_DISPLAY:
			welcome();
			break;
		case QR_START:
			scanConfirmation();
			break;
		case AUTHENTICATION_RESPONSE:
			if(((DataMessage)message).getResult().equalsIgnoreCase("OK"))
				assignSlot(((DataMessage) message).getSlotNumber());
			else
				notReserved();
			break;
		default:
			break;		
		}
	}
}