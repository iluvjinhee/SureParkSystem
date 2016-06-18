package com.lge.sureparksystem.parkserver.manager.authenticationmanager;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.topic.AuthenticationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ManagerTopic;

public class AuthenticationManager extends ManagerTask {
	public class AuthenticationManagerListener {
		@Subscribe
		public void onSubscribe(AuthenticationManagerTopic topic) {
			System.out.println("AuthenticationManagerListener: " + topic);
			
			process(topic);
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

	@Override
	protected void process(ManagerTopic topic) {
		// TODO Auto-generated method stub
		
	}	
}
