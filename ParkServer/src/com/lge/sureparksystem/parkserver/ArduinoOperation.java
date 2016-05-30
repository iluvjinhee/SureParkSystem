package com.lge.sureparksystem.parkserver;


/**
 * @author yongchul.park
 * @version 1.0
 * @created 30-5-2016 ¿ÀÈÄ 7:15:36
 */
public class ArduinoOperation {

	public ArduinoOperation(){

	}

	public void finalize() throws Throwable {

	}
	/**
	 * 
	 * @param bOpen
	 */
	public int setEntryGateServo(boolean bOpen){
		return 0;
	}

	/**
	 * 
	 * @param color
	 */
	public int setEntryLED(COLOR color){
		return 0;
	}

	/**
	 * 
	 * @param color
	 */
	public int setExitLED(COLOR color){
		return 0;
	}
}//end ArduinoOperation