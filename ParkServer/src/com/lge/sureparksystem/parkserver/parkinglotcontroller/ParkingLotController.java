package com.lge.sureparksystem.parkserver.parkinglotcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import com.lge.sureparksystem.parkserver.communicationmanager.CommunicationManager;
import com.lge.sureparksystem.parkserver.communicationmanager.ConsolePrint;
import com.lge.sureparksystem.parkserver.communicationmanager.SocketForServer;
import com.lge.sureparksystem.parkserver.communicationmanager.SocketInfo;

public class ParkingLotController {
	CommunicationManager mCommManager;
	
	public ParkingLotController() {
		System.out.println("create ParkingLotController()");
		mCommManager = CommunicationManager.getInstance();
	}
	public int getAvailableSlot() {
		Random r = new Random();

		return r.nextInt(getSlotSize()) + 1;
	}
	
	private int getSlotSize() {
		return 4;
	}
	
	public void testParkView() {
		BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));
		
		Thread thread = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	String keyMsg;
		    	while(true) {
		    		try {
						keyMsg = keyIn.readLine();
						ConsolePrint.log(keyMsg);
						if(keyMsg != null) {
							int msgIdx = Integer.parseInt(keyMsg);
							if (msgIdx == 3) {
								mCommManager.sendMessage(SocketInfo.SOCKET_PARKVIEW, msgIdx, String.valueOf(getAvailableSlot()));		
							} else {
								mCommManager.sendMessage(SocketInfo.SOCKET_PARKVIEW, msgIdx);
							}

						}

					} catch (IOException e) {
						e.printStackTrace();
					}
		    	}
		    }
		});
		thread.start();
	}
}
