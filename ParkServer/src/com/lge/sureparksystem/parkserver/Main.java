package com.lge.sureparksystem.parkserver;

import com.lge.sureparksystem.parkserver.communicationmanager.CommunicationManager;

public class Main {
	static CommunicationManager communicationManager = null;

	public static void main(String[] args) throws Exception {
		communicationManager = new CommunicationManager();
		
		communicationManager.showServerInfo();
		communicationManager.init();
		
		communicationManager.start();	
	}
}