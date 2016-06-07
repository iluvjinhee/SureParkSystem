package com.lge.sureparksystem.parkserver.parkinglotcontroller;

import java.util.Random;

public class ParkingLotController {
	public ParkingLotController() {
	}
	
	public int getAvailableSlot() {
		Random r = new Random();

		return r.nextInt(getSlotSize()) + 1;
	}
	
	private int getSlotSize() {
		return 4;
	}
}
