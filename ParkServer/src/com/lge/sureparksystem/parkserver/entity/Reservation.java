package com.lge.sureparksystem.parkserver.entity;

import java.util.Date;

/**
 * @author yongchul.park
 * @version 1.0
 * @created 30-5-2016 ¿ÀÈÄ 7:16:27
 */
public class Reservation {

	private Date date;
	private Driver driver;
	private Date time;

	public Reservation(){

	}

	/**
	 * 
	 * @exception Throwable Throwable
	 */
	public void finalize()
	  throws Throwable{

	}
}//end Reservation