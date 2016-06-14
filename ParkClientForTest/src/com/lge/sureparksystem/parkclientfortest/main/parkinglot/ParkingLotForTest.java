package com.lge.sureparksystem.parkclientfortest.main.parkinglot;

import com.lge.sureparksystem.parkclientfortest.socket.SocketForClient;
import com.lge.sureparksystem.parkclientfortest.socket.SocketInfo;

public class ParkingLotForTest {
	public static final String IP_ADDRESS = "localhost";

	public static void main(String[] args) {
		SocketForClient parkHere = new SocketForParkingLot(IP_ADDRESS, SocketInfo.PORT_PARKINGLOT);

		parkHere.connect();
	}
}
