package com.lge.sureparksystem.parkserver.util.paymentremoteproxy;

import javax.sound.sampled.LineUnavailableException;

import com.lge.sureparksystem.parkserver.util.SoundUtil;

public class PaymentSound {
	public static void soundFail() {
		try {
			SoundUtil.tone(5000, 300);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void soundOk() {
		try {
			SoundUtil.tone(261, 200);
			Thread.sleep(10);
			SoundUtil.tone(392, 200);
			Thread.sleep(10);
			SoundUtil.tone(523, 200);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
