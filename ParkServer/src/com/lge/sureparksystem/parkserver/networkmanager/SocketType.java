package com.lge.sureparksystem.parkserver.networkmanager;

public enum SocketType {
	SOCKET_PARKINGLOT(1),
	SOCKET_PARKVIEW(2),
	SOCKET_PARKHERE(3),
	SOCKET_NONE(100);
	
	int value;
	
	SocketType(int value) {
		this.value = value;
	}	
}
