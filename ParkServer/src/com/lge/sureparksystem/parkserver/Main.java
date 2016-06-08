package com.lge.sureparksystem.parkserver;

import com.lge.sureparksystem.parkserver.communicationmanager.CommunicationManager;
import com.lge.sureparksystem.parkserver.parkinglotcontroller.ParkingLotController;
import com.lge.sureparksystem.parkserver.reservationmanager.ReservationManager;

public class Main {

	public static void main(String[] args) throws Exception {
		
		CommunicationManager.getInstance().init();
		new ParkingLotController().testParkView();
		new ReservationManager();
				
	}
}