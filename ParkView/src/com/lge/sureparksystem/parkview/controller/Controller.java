package com.lge.sureparksystem.parkview.controller;

import com.lge.sureparksystem.parkview.FullscreenActivity;
import com.lge.sureparksystem.parkview.message.DataMessage;
import com.lge.sureparksystem.parkview.message.Message;
import com.lge.sureparksystem.parkview.message.MessageParser;
import com.lge.sureparksystem.parkview.message.MessageType;
import com.lge.sureparksystem.parkview.networkmanager.ConnectionInfo;
import com.lge.sureparksystem.parkview.networkmanager.SocketForClient;
import com.lge.sureparksystem.parkview.qrcode.IntentIntegrator;
import com.lge.sureparksystem.parkview.qrcode.IntentResult;
import com.lge.sureparksystem.parkview.tts.TTSWrapper;

import android.content.Intent;
import android.util.Log;

public class Controller {
	private final String TAG = "ParkView";
	
	private String name = null;
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
			clientSocket = new SocketForClient(ConnectionInfo.IP_ADDRESS, ConnectionInfo.PORT, this);
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
			String qrcode =  scanResult.getContents();
			if(qrcode != null) {
				DataMessage dataMessage = new DataMessage(MessageType.CONFIRMATION_SEND);
				dataMessage.setConfirmationInfo(qrcode);
				
				setName(qrcode);
				
				clientSocket.send(MessageParser.convertToJSONObject(dataMessage));			
			}
		}
	}

	private void setName(String qrcode) {
		name = qrcode;
	}

	public void welcome() {
		String msg = "Sure Park";
		
		fullScreen.setDisplay(msg, 150);				
	}
	
	public void assignSlot(int slotNumber, String name) {
		String msg = "Hello" + name + ", A wonderful morning to a wonderful friend like you! Your parking slot is ";		
		
		fullScreen.setDisplay("" + slotNumber, 250);				
		tts.speak(msg + slotNumber);
	}
	
	public void notReserved() {
		String msg = "Sorry. You're not reserved.";	
	    
		fullScreen.setDisplay("NOT\nRESERVED !", 100);	
		tts.speak(msg);
	}
	
	public void scanConfirmation() {
		String msg = "Welcome, Scan your confirmation information.";	
	    
		tts.speak(msg);
		intentIntegrator.initiateScan();
	}

	public void processMessage(String jsonMessage) {
		Message message = MessageParser.convertToMessage(jsonMessage);
		
		switch(message.getMessageType()) {
		case WELCOME_DISPLAY:
			Log.d(TAG, MessageType.WELCOME_DISPLAY.getText());
			welcome();
			break;
		case QR_START:
			Log.d(TAG, MessageType.QR_START.getText());
			scanConfirmation();
			break;
		case CONFIRMATION_RESPONSE:
			if(((DataMessage)message).getResult().equalsIgnoreCase("OK")) {
				Log.d(TAG, MessageType.CONFIRMATION_RESPONSE.getText() + ": OK");
				
				assignSlot(((DataMessage) message).getSlotNumber(), getName());
				
				Log.d(TAG, "SlotNumber: " + ((DataMessage) message).getSlotNumber());
			}
			else {
				Log.d(TAG, MessageType.CONFIRMATION_RESPONSE.getText() + ": Fail");
				notReserved();
			}
			break;
		case AUTHENTICATION_RESPONSE:
			Log.d(TAG, MessageType.CONFIRMATION_RESPONSE.getText());
			if(((DataMessage)message).getResult().equalsIgnoreCase("OK")) {
				Log.d(TAG, MessageType.CONFIRMATION_RESPONSE.getText() + ": OK");
				welcome();
			} else {
				Log.d(TAG, MessageType.CONFIRMATION_RESPONSE.getText() + ": Fail");
			}
		default:
			break;		
		}
	}

	private String getName() {
		return name;
	}
}