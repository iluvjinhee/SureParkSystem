package com.lge.sureparksystem.parkview.controller;

import java.util.Random;

import com.lge.sureparksystem.parkserver.event.ParkViewEvent;
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
				fullScreen.setDisplay(qrcode, 50);
				clientSocket.sendMsg(qrcode);			
			}
		}
	}

	public void welcome(String msg) {
		fullScreen.setDisplay(msg, 100);				
		tts.speak(msg);
	}
	
	public void assignSlot(String msg, int slotIndex) {
		fullScreen.setDisplay(String.valueOf(slotIndex), 250);				
		tts.speak(msg + slotIndex);
	}
	
	public void scanConfirmation(String msg) {
		tts.speak(msg);
		intentIntegrator.initiateScan();
	}

	public void parseMessage(String msg) {
		switch(ParkViewEvent.fromString(msg)) {
		case WELCOME_SUREPARK:
			welcome(msg);
			break;
		case SCAN_CONFIRM:
			scanConfirmation(msg);
			break;
		case ASSIGN_SLOT:
			Random r = new Random();
			int slotNumber = r.nextInt(4) + 1;
			
			assignSlot(msg, slotNumber);
			break;
		default:
			break;		
		}
	}
}