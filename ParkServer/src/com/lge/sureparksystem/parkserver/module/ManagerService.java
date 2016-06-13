package com.lge.sureparksystem.parkserver.module;

public class ManagerService {
	String threadName = "";
	Thread t = null;
	Runnable r = null;
	
	public ManagerService(Runnable r, String threadName) {
		this.r = r;
		this.threadName = threadName;
	}
	
	public void init() {
	};
	
	public void doWork() {
		t = new Thread(r, threadName);
		
		t.start();
	}

	public void stop() {
		t.interrupt();
	};

	public void destroy() {
		t = null;
	};
}