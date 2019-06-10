package com.ysttench.application.msd.settings.web.controller.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.ysttench.application.database.ibatis.entity.FormMap;

public class TcpClient {

    public static Socket socket;
    // static WebSocketServer ws = new WebSocketServer();

    public static void startClient(FormMap<String, Object> map) {
	if (socket == null) {
	    new Thread(new Runnable() {
		@Override
		public void run() {
		    try {
			System.out.println("启动客户端");
			socket = new Socket(map.get("equipmentIp").toString(),
				Integer.parseInt(map.get("equipmentPort").toString()));
			System.out.println("客户端连接成功");

			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			InputStream inputStream = socket.getInputStream();

			byte[] buffer = new byte[31];
			int len = -1;
			while ((len = inputStream.read(buffer)) != -1) {

			    String s = BinaryToHexString(buffer);
			    map.put("sd", HexStringToDecimal(s.substring(4, 8)));
			    map.put("wd", HexStringToDecimal(s.substring(10, 14)));
			    map.put("dy", HexStringToDecimal(s.substring(16, 20)));
			    // ws.onMessage(JsonUtils.mapToJson(map));
			}
			System.out.println("客户端断开连接");
			pw.close();

		    } catch (Exception EE) {
			EE.printStackTrace();
			System.out.println("客户端无法连接服务器");

		    } finally {
			try {
			    socket.close();
			} catch (IOException e) {
			    e.printStackTrace();
			}
			socket = null;
		    }
		}
	    }).start();
	} else {
	}
    }

    // 将字节数组转换为16进制字符串
    public static String BinaryToHexString(byte[] bytes) {
	String hexStr = "0123456789ABCDEF";
	String result = "";
	String hex = "";
	for (byte b : bytes) {
	    hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
	    hex += String.valueOf(hexStr.charAt(b & 0x0F));
	    result += hex;
	}
	return result;
    }

    // 16进制字符串转换成10进制(温湿度)
    public static double HexStringToDecimal(String HexString) {
	return Long.parseLong(HexString, 16) / 10.0;

    }

    public void sendTcpMessage(String ip, int port, String msg) throws Exception {
	socket = new Socket(ip, port);
	System.out.println(msg);
	// byte[] dmsg = getScreenBytes(msg);
	if (socket != null && socket.isConnected()) {
	    new Thread(new Runnable() {
		@Override
		public void run() {
		    try {
			Thread.sleep(500);//休眠1s
		    } catch (InterruptedException e1) {
			e1.printStackTrace();
		    }
		    try {
			socket.getOutputStream().write(getScreenBytes(msg));
			socket.getOutputStream().flush();
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
	    }).start();
	}
    }

    public static byte[] getScreenBytes(String msg) {

	byte[] b = new byte[1024];
	b[0] = (byte) 0xF1;
	b[1] = (byte) 0x01;
	b[2] = (byte) 0x0A;
	b[3] = (byte) 0x01;
	b[4] = (byte) 0x00;
	b[5] = (byte) 0x00;
	b[6] = (byte) 0xFE;
	String s = msg.replace("-", "").substring(0, 4);
	for (int i = 0; i < s.length(); i++) {
	    char n = s.charAt(i);
	    switch (n) {
	    case '0':
		b[i + 2] = (byte) 0x00;
		break;
	    case '1':
		b[i + 2] = (byte) 0x01;
		break;
	    case '2':
		b[i + 2] = (byte) 0x02;
		break;
	    case '3':
		b[i + 2] = (byte) 0x03;
		break;
	    case '4':
		b[i + 2] = (byte) 0x04;
		break;
	    case '5':
		b[i + 2] = (byte) 0x05;
		break;
	    case '6':
		b[i + 2] = (byte) 0x06;
		break;
	    case '7':
		b[i + 2] = (byte) 0x07;
		break;
	    case '8':
		b[i + 2] = (byte) 0x08;
		break;
	    case '9':
		b[i + 2] = (byte) 0x09;
		break;
	    case 'A':
		b[i + 2] = (byte) 0x0A;
		break;
	    case 'B':
		b[i + 2] = (byte) 0x0B;
		break;
	    case 'C':
		b[i + 2] = (byte) 0x0C;
		break;
	    case 'D':
		b[i + 2] = (byte) 0x0D;
		break;
	    case 'E':
		b[i + 2] = (byte) 0x0E;
		break;
	    case 'F':
		b[i + 2] = (byte) 0x0F;
		break;
	    case 'H':
		b[i + 2] = (byte) 0x10;
		break;
	    case 'I':
		b[i + 2] = (byte) 0x12;
		break;
	    case 'J':
		b[i + 2] = (byte) 0x13;
		break;
	    case 'K':
		b[i + 2] = (byte) 0x14;
		break;
	    case 'L':
		b[i + 2] = (byte) 0x15;
		break;
	    }
	}
	return b;
    }

    public static void main(String[] args) throws Exception {
	// TcpClient.startClient("192.168.1.10", 20);

    }
}
