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
				DataMessage dataMessage = new DataMessage(MessageType.RESERVATION_CODE);
				dataMessage.setReservationCode(qrcode);
				JSONObject jsonObject = MessageParser.makeJSONObject(dataMessage);				
				
				clientSocket.send(jsonObject);			
			}
		}
	}

	public void welcome() {
		String msg = "Welcome!\n Sure Park.";
		
		fullScreen.setDisplay(msg, 50);				
		tts.speak(msg);
	}
	
	public void assignSlot(int slot) {
		String msg = "Hello Diniel, Your Park Slot is ";		
		
		fullScreen.setDisplay(String.valueOf(slot), 250);				
		tts.speak(msg + slot);
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

	public void parseJSONMessage(String jsonMessage) {
		Message message = MessageParser.makeMessage(jsonMessage);
		
		switch(message.getMessageType()) {
		case WELCOME_SUREPARK:
			welcome();
			break;
		case SCAN_CONFIRM:
			scanConfirmation();
			break;
		case ASSIGNED_SLOT:
			assignSlot(Integer.parseInt(((DataMessage) message).getAssignedSlot()));
			break;
		case NOT_RESERVED:
			notReserved();
			break;
		default:
			break;		
		}
	}
}