package com.lge.sureparksystem.parkserver.manager;

import com.google.common.eventbus.EventBus;
import com.lge.sureparksystem.parkserver.topic.ManagerTopic;

public abstract class ManagerTask implements Runnable {
	private static EventBus eventBus = null;
	protected boolean loop = true;
	
	protected abstract void process(ManagerTopic topic);
	
	public ManagerTask() {
		if(eventBus == null)
			eventBus = new EventBus();
	}
	
	public void init() {
	}
	
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub		
	}	
}
