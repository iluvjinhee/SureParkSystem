package com.lge.sureparksystem.parkserver.manager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.EventBus;

public abstract class ManagerTask implements Runnable {
	private static EventBus eventBus = null;
	protected boolean loop = true;
	
	protected abstract void processMessage(JSONObject jsonObject);
	
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
