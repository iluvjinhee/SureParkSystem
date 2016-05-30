package com.lge.sureparksystem.parkserver;

import java.util.List;

/**
 * @author yongchul.park
 * @version 1.0
 * @created 30-5-2016 ¿ÀÈÄ 7:16:26
 */
public class Owner extends Account {

	private List<ParkingLot> parkingLotList;

	public Owner(){

	}

	/**
	 * 
	 * @exception Throwable Throwable
	 */
	public void finalize()
	  throws Throwable{

	}
}//end Owner