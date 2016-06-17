package com.lge.sureparksystem.parkclientfortest.main.parkhere;

import com.lge.sureparksystem.parkclientfortest.socket.SocketForClient;
import com.lge.sureparksystem.parkclientfortest.socket.SocketInfo;

public class ParkHereForTest {
	public static final String IP_ADDRESS = "localhost";

	public static void main(String[] args) {
		SocketForClient parkHere = new SocketForParkHere(IP_ADDRESS, SocketInfo.PORT_PARKHERE);

		parkHere.connect();
	}
}
