package com.lge.sureparksystem.parkserver.entity;

import java.util.List;

/**
 * @author yongchul.park
 * @version 1.0
 * @created 30-5-2016 ¿ÀÈÄ 7:16:27
 */
public class ParkingLot {

	private Attendant attendant;
	private MultiColorLED entryGateLED;
	private boolean entryServoOpened;
	private MultiColorLED exitGateLED;
	private boolean exitServoOpened;
	private String id;
	private int occupiedCount;
	private List<ParkingSlot> slotList;

	public ParkingLot(){

	}

	/**
	 * 
	 * @exception Throwable Throwable
	 */
	public void finalize()
	  throws Throwable{

	}

	/**
	 * 
	 * @param gateNo
	 */
	public int closeGate(int gateNo){
		return 0;
	}

	/**
	 * 
	 * @param gateNo
	 */
	public int openGate(int gateNo){
		return 0;
	}
}//end ParkingLot