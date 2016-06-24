package com.lge.sureparksystem.parkserver.manager;

import org.json.simple.JSONObject;

import com.google.common.eventbus.EventBus;
import com.lge.sureparksystem.parkserver.topic.ManagerTopic;

public abstract class ManagerTask implements Runnable {
	private static EventBus eventBus = null;
	protected boolean loop = true;
	protected String sessionID = null;
	
	protected abstract void processMessage(JSONObject jsonObject);
	
	public ManagerTask() {
		if(eventBus == null)
			eventBus = new EventBus();
	}
	
	public void init() {
	}
	
	private EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub		
	}
	
	protected void registerEventBus(Object listener) {
		eventBus.register(listener);
	}
	
	protected void post(ManagerTopic topic, ManagerTask manager) {
		topic.setSessionID(manager.getSessionID());
		
		getEventBus().post(topic);
	}
	
	public void setSessionID(ManagerTopic topic) {
		if(topic.getSessionID() != null)
			sessionID = topic.getSessionID();		
	}
	
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;		
	}
	
	public String getSessionID() {
		return sessionID;
	}
}
