package com.lge.sureparksystem.parkview.controller;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
				
				setName(qrcode);
				
				clientSocket.send(MessageParser.convertToJSONObject(dataMessage));			
			}
		}
	}

	private void setName(String qrcode) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) jsonParser.parse(qrcode);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		name = (String) jsonObject.get("name");
	}

	public void welcome() {
		String msg = "Welcome, Sure Park System. Good morning friend!";
		
		fullScreen.setDisplay("Welcome!\n Sure Park.", 50);				
		tts.speak(msg);
	}
	
	public void assignSlot(int slotNumber, String name) {
		String msg = "Hello" + name + ", A wonderful morning to a wonderful friend like you! Your Parking Slot is ";		
		
		fullScreen.setDisplay(String.valueOf(slotNumber), 250);				
		tts.speak(msg + slotNumber);
	}
	
	public void notReserved() {
		String msg = "Sorry. You're not reserved.";	
	    
		fullScreen.setDisplay("NOT\nRESERVED !", 100);	
		tts.speak(msg);
	}
	
	public void scanConfirmation() {
		String msg = "Scan your confirmation information.";	
	    
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
				assignSlot(((DataMessage) message).getSlotNumber(), getName());
			else
				notReserved();
			break;
		default:
			break;		
		}
	}

	private String getName() {
		return name;
	}
}