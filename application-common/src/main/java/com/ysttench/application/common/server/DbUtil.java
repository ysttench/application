package com.ysttench.application.common.server;

import java.io.Writer;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbUtil {
    private String driverClass = "oracle.jdbc.driver.OracleDriver";
    private String url = "jdbc:oracle:thin:@10.0.250.1:1521:WX";
    private String username = "wx";
    private String password = "wx";

    private Connection conn = null;
    private PreparedStatement ps = null;

    public Connection getConnection() {
        if (this.conn != null) {
            return this.conn;
        }
        try {
            Class.forName(driverClass);
            this.conn = DriverManager.getConnection(url, username, password);
            return this.conn;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getResultSet(String sql) {
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            if (ps != null)
                this.ps.close();
            if (conn != null)
                this.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
    *
    * Description:将Clob对象转换为String对象,Blob处理方式与此相同
    *
    * @param clob
    * @return
    * @throws Exception
    * @mail sunyujia@yahoo.cn
    * @blog blog.csdn.ne t/sunyujia/
    * @since：Oct 1, 2008 7:19:57 PM
    */
   public static String oracleClob2Str(Clob clob) throws Exception {
       return (clob != null ? clob.getSubString(1, (int) clob.length()) : null);
   }
   
   /**
    *
    * Description:将string对象转换为Clob对象,Blob处理方式与此相同
    *
    * @param str
    * @param lob
    * @return
    * @throws Exception
    * @mail sunyujia@yahoo.cn
    * @blog blog.csdn.ne t/sunyujia/
    * @since：Oct 1, 2008 7:20:31 PM
    */
   public static Clob oracleStr2Clob(String str, Clob lob) throws Exception {
       Method methodToInvoke = lob.getClass().getMethod(
               "getCharacterOutputStream", (Class[]) null);
       Writer writer = (Writer) methodToInvoke.invoke(lob, (Object[]) null);
       writer.write(str);
       writer.close();
       return lob;
   }
   
   /**
   *
   * Description:将Clob对象转换为String对象,Blob处理方式与此相同
   *
   * @param clob
   * @return
   * @throws Exception
   * @mail sunyujia@yahoo.cn
   * @blog blog.csdn.ne t/sunyujia/
   * @since：Oct 1, 2008 7:19:57 PM
   */
  public static byte[] oracleBlob2Str(Blob blob) throws Exception {
      return (blob != null ? blob.getBytes(1, (int) blob.length()) : null);
  }
  
  /**
   *
   * Description:将string对象转换为Clob对象,Blob处理方式与此相同
   *
   * @param str
   * @param lob
   * @return
   * @throws Exception
   * @mail sunyujia@yahoo.cn
   * @blog blog.csdn.ne t/sunyujia/
   * @since：Oct 1, 2008 7:20:31 PM
   */
  public static Blob oracleStr2Blob(String str, Blob blob) throws Exception {
      Method methodToInvoke = blob.getClass().getMethod(
              "getCharacterOutputStream", (Class[]) null);
      Writer writer = (Writer) methodToInvoke.invoke(blob, (Object[]) null);
      writer.write(str);
      writer.close();
      return blob;
  }
}
