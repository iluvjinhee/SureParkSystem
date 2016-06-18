package com.lge.sureparksystem.parkserver;

import java.util.Date;

/**
 * @author yongchul.park
 * @version 1.0
 * @created 30-5-2016 ¿ÀÈÄ 7:16:26
 */
public class ParkingInfo {

	private int assignedSlotNo;
	private Date entryTime;
	private Date exitTime;
	private Reservation reservation;
	private int slotNo;

	public ParkingInfo(){

	}

	/**
	 * 
	 * @exception Throwable Throwable
	 */
	public void finalize()
	  throws Throwable{

	}
}//end Parking