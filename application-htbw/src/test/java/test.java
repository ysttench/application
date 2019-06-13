import java.io.IOException;
import java.net.Socket;

public class test {
public static void main(String[] args) {
    try {
	Socket socket = new Socket("192.168.1.106",8088);
	String s="haha";
	socket.getOutputStream().write(s.getBytes());
	socket.getOutputStream().flush();
    } catch (IOException e) {
	e.printStackTrace();
    }
}
}
