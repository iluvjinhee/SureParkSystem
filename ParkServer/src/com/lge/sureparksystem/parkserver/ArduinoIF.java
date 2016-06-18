package com.lge.sureparksystem.parkserver;


/**
 * @author yongchul.park
 * @version 1.0
 * @created 30-5-2016 ¿ÀÈÄ 7:16:22
 */
public abstract class ArduinoIF {

	public ArduinoIF(){

	}

	public void finalize() throws Throwable {

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
}//end ArduinoIF