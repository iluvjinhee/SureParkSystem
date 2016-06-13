package com.lge.sureparksystem.parkserver.manager.networkmanager;

import java.net.Socket;

public interface ISocketAcceptListener {
	public void onSocketAccepted(Socket socket);
}
