package com.lge.sureparksystem.parkserver.communicationmanager;

import java.net.Socket;

public interface ISocketAcceptListener {

	public void onSocketAccepted(int type, Socket socket);
}
