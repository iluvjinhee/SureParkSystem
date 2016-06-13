package com.lge.sureparksystem.parkserver.manager.securitymanager;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.topic.SecurityManagerTopic;

public class SecurityManager extends ManagerTask {
	public class SecurityManagerListener {
		@Subscribe
		public void onSubscribe(SecurityManagerTopic topic) {
			System.out.println("SecurityManagerListener");
			System.out.println(topic);
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
}
