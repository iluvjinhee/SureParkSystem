package com.lge.sureparksystem.parkserver.manager.securitymanager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.topic.SecurityManagerTopic;

public class SecurityManager extends ManagerTask {
	public class SecurityManagerListener {
		@Subscribe
		public void onSubscribe(SecurityManagerTopic topic) {
			System.out.println("SecurityManagerListener: " + topic);
			
			processMessage(topic.getJsonObject());
		}
	}
	
	@Override
	public void init() {
		getEventBus().register(new SecurityManagerListener());
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
		// TODO Auto-generated method stub
		
	}	
}
