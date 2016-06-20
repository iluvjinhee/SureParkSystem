package com.lge.sureparksystem.parkserver.manager.parkinglotmanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.topic.ParkingLotManagerTopic;

public class ParkingLotManager extends ManagerTask {
	public class ParkingLotManagerListener {
		@Subscribe
		public void onSubscribe(ParkingLotManagerTopic topic) {
			System.out.println("ParkingLotManagerListener: " + topic);
			
			process(topic.getJsonObject());
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

	@Override
	protected void process(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		
	}	
}
