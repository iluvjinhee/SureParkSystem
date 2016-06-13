package com.lge.sureparksystem.parkserver.manager.authenticationmanager;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.topic.AuthenticationManagerTopic;

public class AuthenticationManager extends ManagerTask {
	public class AuthenticationManagerListener {
		@Subscribe
		public void onSubscribe(AuthenticationManagerTopic topic) {
			System.out.println("AuthenticationManagerListener");
			System.out.println(topic);
		}
	}
	
	@Override
	public void init() {
		getEventBus().register(new AuthenticationManagerListener());
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
