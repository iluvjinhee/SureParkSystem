package com.lge.sureparksystem.parkclientfortest.main.parkview;

import com.lge.sureparksystem.parkclientfortest.socket.SocketForClient;
import com.lge.sureparksystem.parkclientfortest.socket.SocketInfo;

public class ParkViewForTest {
	public static final String IP_ADDRESS = "localhost";

	public static void main(String[] args) {
		SocketForClient parkView = new SocketForParkView(IP_ADDRESS, SocketInfo.PORT_PARKVIEW);

		parkView.connect();
	}
}
