package com.ysttench.application.htbw.core.rdto;

public class Jzchange {
    //16进制字符串转换成字符串
    public  String hexStr2Str(String hexStr) {
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
 // 将字节数组转换为16进制字符串
    public  String BinaryToHexString(byte[] bytes) {
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
    public  double HexStringToDecimal(String HexString) {
	return Long.parseLong(HexString, 16) / 10.0;
    }
}
