package com.lge.sureparksystem.parkserver.entity;


/**
 * @author yongchul.park
 * @version 1.0
 * @created 30-5-2016 ¿ÀÈÄ 7:16:24
 */
public class CreditCard {

	private String cvc;
	private String expiredDate;
	private String name;
	private String number;

	public CreditCard(){

	}

	/**
	 * 
	 * @exception Throwable Throwable
	 */
	public void finalize()
	  throws Throwable{

	}
}//end CreditCard