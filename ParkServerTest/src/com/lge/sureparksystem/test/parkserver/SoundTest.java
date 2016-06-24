package com.lge.sureparksystem.test.parkserver;
import javax.sound.sampled.LineUnavailableException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lge.sureparksystem.parkserver.util.SoundUtil;

public class SoundTest {
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void Sound1Test() throws LineUnavailableException, InterruptedException {
		SoundUtil.tone(5000, 300);
	}
	
	@Test
	public void Sound2Test() throws LineUnavailableException, InterruptedException {
//		SoundUtil.tone(1000, 100);
//		Thread.sleep(1000);
//		SoundUtil.tone(100, 1000);
//		Thread.sleep(1000);
//		SoundUtil.tone(5000, 100);
//		Thread.sleep(1000);
		SoundUtil.tone(261, 200);
		Thread.sleep(10);
		SoundUtil.tone(392, 200);
		Thread.sleep(10);
		SoundUtil.tone(523, 200);
//		Thread.sleep(1000);
//		SoundUtil.tone(400, 500, 0.2);
	}	
}
