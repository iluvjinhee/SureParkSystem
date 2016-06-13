package com.lge.sureparksystem.parkserver.networkmanager;

import java.net.Socket;

public interface ISocketAcceptListener {
	public void onSocketAccepted(Socket socket);
}
