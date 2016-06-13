package com.lge.sureparksystem.parkserver.module;

import com.google.common.eventbus.EventBus;

public class ManagerTask implements Runnable {
	private static EventBus eventBus = null;
	protected boolean loop = true;
	
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
