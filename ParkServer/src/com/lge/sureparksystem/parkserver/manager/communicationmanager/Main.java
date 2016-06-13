package com.lge.sureparksystem.parkserver.manager.communicationmanager;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.lge.sureparksystem.parkserver.manager.ManagerService;
import com.lge.sureparksystem.parkserver.manager.keyinmanager.KeyboardInManager;
import com.lge.sureparksystem.parkserver.manager.networkmanager.NetworkManager;
import com.lge.sureparksystem.parkserver.manager.networkmanager.ParkHereNetworkManager;
import com.lge.sureparksystem.parkserver.manager.networkmanager.ParkViewNetworkManager;
import com.lge.sureparksystem.parkserver.manager.networkmanager.ParkingLotNetworkManager;
import com.lge.sureparksystem.parkserver.manager.networkmanager.SocketInfo;
import com.lge.sureparksystem.parkserver.manager.reservationmanager.ReservationManager;
import com.lge.sureparksystem.parkserver.util.Log;

public class Main {
	private static InetAddress mIP = null;
	
	static ManagerService communicationManagerService = null;
	static ManagerService parkViewNetworkManagerService = null;
	static ManagerService parkHereNetworkManagerService = null;
	static ManagerService parkingLotNetworkManagerService = null;
	static ManagerService KeyboardInManagerService = null;
	static ManagerService ReservationManagerService = null;

	public static void main(String[] args) throws Exception {
		try {
			mIP = InetAddress.getLocalHost();
			
			Log.log("IP of my system is := " + mIP.getHostAddress());
			Log.log("The server is running.");
		} catch (UnknownHostException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		
		CommunicationManager communicationManager = new CommunicationManager();
		communicationManager.init();
		communicationManagerService = new ManagerService(communicationManager, "CommunicationManager");
		
		NetworkManager parkViewNetworkManager = new ParkViewNetworkManager(SocketInfo.PORT_PARKVIEW);
		parkViewNetworkManager.init();
		parkViewNetworkManagerService = new ManagerService(parkViewNetworkManager, "ParkViewNetworkManager");
		
		NetworkManager parkHereNetworkManager = new ParkHereNetworkManager(SocketInfo.PORT_PARKHERE);
		parkHereNetworkManager.init();
		parkHereNetworkManagerService = new ManagerService(parkHereNetworkManager, "ParkHereNetworkManager");
		
		NetworkManager parkingLotNetworkManager = new ParkingLotNetworkManager(SocketInfo.PORT_PARKINGLOT);
		parkingLotNetworkManager.init();
		parkingLotNetworkManagerService = new ManagerService(parkingLotNetworkManager, "ParkingLotNetworkManager");	
		
		KeyboardInManager keyboardInManager = new KeyboardInManager();
		keyboardInManager.init();
		KeyboardInManagerService = new ManagerService(keyboardInManager, "KeyboardInManager");
		
		ReservationManager reservationManager = new ReservationManager();
		reservationManager.init();
		ReservationManagerService = new ManagerService(reservationManager, "ReservationManager");
		
		communicationManagerService.doWork();
		parkViewNetworkManagerService.doWork();
		parkHereNetworkManagerService.doWork();
		parkingLotNetworkManagerService.doWork();
		KeyboardInManagerService.doWork();
		ReservationManagerService.doWork();
	}
}