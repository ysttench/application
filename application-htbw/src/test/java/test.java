import java.io.IOException;
import java.net.Socket;

import com.ysttench.application.common.util.encrypt.Encrypt;

public class test {
public static void main(String[] args) {
    /*try {
	Socket socket = new Socket("192.168.1.105",8089);
	String s="haha";
	socket.getOutputStream().write(s.getBytes());
	socket.getOutputStream().flush();
    } catch (IOException e) {
	e.printStackTrace();
    }*/
    Encrypt encrypt = new Encrypt();
    
    System.out.println(encrypt.encoder("123456"));
}
}
