import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SocketTest {
    public static void main(String[] args)
	    throws Exception {/*
			       * Socket socket = new Socket("192.168.1.19", 24); Timer timer = new Timer();
			       * timer.scheduleAtFixedRate(new TimerTask() {
			       * 
			       * @Override public void run() { InputStream inputStream; try { inputStream =
			       * socket.getInputStream(); byte[] buffer = new byte[1024];
			       * inputStream.read(buffer); String s = BinaryToHexString(buffer);
			       * System.out.println(s); Double tempValue = HexStringToDecimal(s.substring(4,
			       * 8)); Double humValue = HexStringToDecimal(s.substring(10, 14));
			       * System.out.println(DatetimeUtil.getDate()+"   "+tempValue + "   " +
			       * humValue); } catch (Exception e) { e.printStackTrace(); } } }, 1000, 60000);
			       */

	try {
	    // 17 8888 ;19 24 ;12 23;
	    Socket socket = new Socket("192.168.1.18", 23);
	    byte[] n = { 0x0, 0x0, 0x0, 0x6, 0x0, 0x1, 0x0, 0x0, 0x0, 0x1, 0x2, 0xC};
	    socket.getOutputStream().write(n);
	    socket.getOutputStream().flush();
	    InputStream inputStream = socket.getInputStream();
	    byte[] buffer = new byte[1024];
	    inputStream.read(buffer);
	    System.out.println(BinaryToHexString(buffer));
	    String msg = BinaryToHexString(buffer);
	    String msgs = msg.substring(msg.indexOf("ABAB"), msg.length());
	    String s = msgs.substring(0, msgs.indexOf("0D0A") + 4);
	    System.out.println(HexStringToDecimal(s.substring(4, 8)));
	    System.out.println(HexStringToDecimal(s.substring(8, 12)));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public static String hexStr2Str(String hexStr) {
	String str = "0123456789ABCDEF";
	char[] hexs = hexStr.toCharArray();
	byte[] bytes = new byte[hexStr.length() / 2];
	int n;
	for (int i = 0; i < bytes.length; i++) {
	    n = str.indexOf(hexs[2 * i]) * 16;
	    n += str.indexOf(hexs[2 * i + 1]);
	    bytes[i] = (byte) (n & 0xff);
	}
	return new String(bytes);
    }

    public static byte[] getScreenBytes(String msg) {

	byte[] b = new byte[1024];
	String s = msg.replace("-", "");
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
}
