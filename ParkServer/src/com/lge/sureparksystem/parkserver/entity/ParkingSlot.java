package com.lge.sureparksystem.parkserver.entity;

/**
 * @author yongchul.park
 * @version 1.0
 * @created 30-5-2016 ¿ÀÈÄ 7:16:27
 */
public class ParkingSlot {

	private boolean assigned;
	private String id;
	private LED led;
	private boolean occupied;

	public ParkingSlot(){

	}

	/**
	 * 
	 * @exception Throwable Throwable
	 */
	public void finalize()
	  throws Throwable{

	}
}//end ParkingSlot