import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Michael Huang
 * 
 */
public class ChatClient extends Frame {
    public static void main(String[] args) {
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	Calendar c = Calendar.getInstance();
	c.setTime(new Date());
        c.add(Calendar.YEAR, 1);
        Date y = c.getTime();
        String year = format.format(y);
        System.out.println("过去一年："+year);
    }
}