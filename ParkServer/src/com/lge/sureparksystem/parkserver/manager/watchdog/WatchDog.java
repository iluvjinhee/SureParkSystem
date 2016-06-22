package com.lge.sureparksystem.parkserver.manager.watchdog;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.topic.WatchDogTopic;

public class WatchDog extends ManagerTask {
	public class WatchDogListener {
		@Subscribe
		public void onSubscribe(WatchDogTopic topic) {
			System.out.println("WatchDogListener: " + topic);
			
			processMessage(topic.getJsonObject());
		}
	}
	
	@Override
	public void init() {
		getEventBus().register(new WatchDogListener());
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
	protected void processMessage(JSONObject jsonObject) {
		DataMessage message = (DataMessage) MessageParser.convertToMessage(jsonObject);
		
		switch(message.getMessageType()) {
		default:
			break;
		}		
	}
}
