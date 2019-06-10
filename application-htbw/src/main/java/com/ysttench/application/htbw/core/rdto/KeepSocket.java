package com.ysttench.application.htbw.core.rdto;

import java.net.Socket;

public class KeepSocket {
    public static SocketClient[] socketlist = new SocketClient[2];
    public static void main(String[] args) {
	String[] ips = {"192.168.1.10","192.168.1.11"};
	int[] ports= {23,20};
	String[] equipmentNums = {"1001","1002"};
	for (int i = 0; i < equipmentNums.length; i++) {
	    SocketClient client = new SocketClient();
	    try {
		Socket socket = new Socket(ips[i], ports[i]);
		if (socket.isConnected()) {
		    client.setStatus("0");
		    client.setSocket(socket);
		}else {
		    client.setStatus("1");
		}
		client.setEquipmentNum(equipmentNums[i]);
		socketlist[i] =client;
	    } catch (Exception e) {
		client.setStatus("1");
		client.setEquipmentNum(equipmentNums[i]);
		socketlist[i] =client;
		continue;
	    }
	}
	
    }
}
