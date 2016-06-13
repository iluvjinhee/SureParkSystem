package com.lge.sureparksystem.parkserver.parkinglotmanager;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.module.ManagerTask;
import com.lge.sureparksystem.parkserver.topic.ParkingLotManagerTopic;

public class ParkingLotManager extends ManagerTask {
	public class ParkingLotManagerListener {
		@Subscribe
		public void onSubscribe(ParkingLotManagerTopic topic) {
			System.out.println("ParkingLotManagerListener");
			System.out.println(topic);
		}
	}
	
	@Override
	public void init() {
		getEventBus().register(new ParkingLotManagerListener());
	}
	
	@Override
	public void run() {
		while(loop) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}	
}
