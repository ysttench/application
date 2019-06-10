package com.ysttench.application.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

public class NumericUtil {
    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;
    /**
     * ETFFSL金额默认值
     */
    public static final double ETFFSL_DEFAULT_DOUBLE = 0.0000;

    public static boolean checkNumber(double value) {
        String str = String.valueOf(value);
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        return str.matches(regex);
    }

    public static boolean checkNumber(int value) {
        String str = String.valueOf(value);
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        return str.matches(regex);
    }

    public static boolean checkNumber(String value) {
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        return value.matches(regex);
    }

    /**
     * 金额保留四位
     * 
     * @param money
     *            金额
     * @return 返回
     */
    public static double convertDouble(Double money) {
        if (null == money) {
            return ETFFSL_DEFAULT_DOUBLE;
        }
        return Math.round(money * 10000) / 10000.0;
    }

    /**
     * 格式化double类型值，使得其末尾保留两位小数
     * 
     * @param value
     * @return
     */
    public static String formatDouble(double value) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(value);
    }

    /**
     * 保留两个小数
     * 
     * @return
     */
    public static String formatDouble(Double b) {
        BigDecimal bg = new BigDecimal(b);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 生成制定位随机数字
     */
    public static String randomNumeric(int i) {
        return RandomStringUtils.randomNumeric(i);
    }

    /**
     * 取得一个随机数,取值范围是0 <= value < scope
     * 
     * @param scope
     *            最大值
     */
    public static int getRandom(int scope) {
        Random random = new Random();
        return random.nextInt(scope);
    }

    /**
     * 将字符串数字转化为int型数字
     * 
     * @param str被转化字符串
     * @param defValue转化失败后的默认值
     * @return int
     */
    public static short parseShort(String str, short defValue) {
        if (str == null) return  0;
        try {
            return Short.parseShort(str);
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * 将字符串数字转化为int型数字
     * 
     * @param str被转化字符串
     * @param defValue转化失败后的默认值
     * @return int
     */
    public static int parseInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * 将字符串数字转化为long型数字
     * 
     * @param str被转化字符串
     * @param defValue转化失败后的默认值
     * @return long
     */
    public static long parseLong(String str, long defValue) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return defValue;
        }
    }

    /**
     * 将字符串数字转化为double型数字
     * 
     * @param str被转化字符串
     * @param defValue转化失败后的默认值
     * @return double
     */
    public static double parseDouble(String str, double defValue) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return defValue;
        }
    }
    
    /**
     * 数字判断
     * @param count
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();   
     }  

    /**
     * 在不足len位的数字前面自动补零
     */
    public static String getLimitLenStr(String str, int len) {
        if (str == null) {
            return "";
        }
        while (str.length() < len) {
            str = "0" + str;
        }
        return str;
    }

    /**
    * 设置数字精度 需要格式化的数据
    * 
    * @param value
    *            double 精度描述（0.00表示精确到小数点后两位）
    * @param precision
    *            String
    * @return double
    */
   public static double setPrecision(double value, String precision) {
       return Double.parseDouble(new DecimalFormat(precision).format(value));
   }

   public static boolean validateCommonDig(String value) {
       if (StringUtils.isNotBlank(value)) {
           try {
               Double.parseDouble(StringUtil.trim(value));
               return true;
           } catch (Exception ex) {

           }
       }
       return false;
   }

   public static double division(double numerator, double denominator,
           int digit) {
       double result = 0;
       result = Math.round((numerator / denominator) * Math.pow(10, digit))
               / Math.pow(10, digit);
       return result;
   }

   /**
    * 提供精确的减法运算。
    * 
    * @param v1
    *            被减数
    * @param v2
    *            减数
    * @return 两个参数的差
    */
   public static double sub(double v1, double v2) {
       BigDecimal b1 = new BigDecimal(Double.toString(v1));
       BigDecimal b2 = new BigDecimal(Double.toString(v2));
       return b1.subtract(b2).doubleValue();
   }

   /**
    * 提供精确的加法运算。
    * 
    * @param v1
    *            被加数
    * @param v2
    *            加数
    * @return 两个参数的和
    */
   public static double add(double v1, double v2) {
       BigDecimal b1 = new BigDecimal(Double.toString(v1));
       BigDecimal b2 = new BigDecimal(Double.toString(v2));
       return b1.add(b2).doubleValue();
   }

   /**
    * 提供精确的乘法运算。
    * 
    * @param v1
    *            被乘数
    * @param v2
    *            乘数
    * @return 两个参数的积
    */
   public static double mul(double v1, double v2) {
       BigDecimal b1 = new BigDecimal(Double.toString(v1));
       BigDecimal b2 = new BigDecimal(Double.toString(v2));
       return b1.multiply(b2).doubleValue();
   }

   /**
    * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
    * 
    * @param v1
    *            被除数
    * @param v2
    *            除数
    * @return 两个参数的商
    */
   public static double div(double v1, double v2) {
       return div(v1, v2, DEF_DIV_SCALE);
   }

   /**
    * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
    * 
    * @param v1
    *            被除数
    * @param v2
    *            除数
    * @param scale
    *            表示表示需要精确到小数点以后几位。
    * @return 两个参数的商
    */
   public static double div(double v1, double v2, int scale) {
       if (scale < 0) {
           throw new IllegalArgumentException("The scale must be a positive integer or zero");
       }
       BigDecimal b1 = new BigDecimal(Double.toString(v1));
       BigDecimal b2 = new BigDecimal(Double.toString(v2));
       return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
   }

   /**
    * String转换double
    * 
    * @param string
    * @return double
    */
   public static double convertSourData(String dataStr) throws Exception {
       if (dataStr != null && !"".equals(dataStr)) {
           return Double.valueOf(dataStr);
       }
       throw new NumberFormatException("convert error!");
   }

   /**
    * strを基に、対応の数値を取得する。変更できない場合、0を戻る。<br>
    * 
    * @param str
    *            変換したい文字
    * 
    * @return 数字に 変換できなければ、0を戻す。
    */
   public static int strToInt(String str) {

       // 文字列のスペースを取る
       str = StringUtil.nvl(str);

       int intRet = 0;
       // ◎変換した値をリターンする
       try {
           // ○変換した値
           intRet = Integer.parseInt(str);
       } catch (NumberFormatException e) {
           // ○0を戻る。
           return 0;
       }
       return intRet;
   }

   /**
    * strを基に、対応の数値を取得する。変更できない場合、0を戻る。<br>
    * 
    * @param str
    *            変換したい文字
    * 
    * @return 数字に 変換できなければ、0を戻す。
    */
   public static BigDecimal strToBigDecimal(String str) {
       // 文字列のスペースを取る。
       str = StringUtil.nvl(str);
       // 変換した値をリターンする
       return new BigDecimal(str);
   }

   /**
    * srcを基に、指定されたlenに"0"を補足する。<br>
    * 
    * @param src
    *            入力内容
    * 
    * @param len
    *            補足結果桁数
    * 
    * @return ゼロを補足した内容 <br>
    *         例えば、fillZeroToLen(123, 4)で "0123"を取得する。<br>
    *         fillZeroToLen(123, 6)で "000123"を取得する。
    */
   public static String fillZeroToLen(int src, int len) {

       // 同名関数を呼び出す
       return fillZeroToLen(String.valueOf(src), len);
   }

   /**
    * srcを基に、指定されたlenに"0"を補足する。<br>
    * 
    * @param src
    *            入力内容
    * 
    * @param len
    *            補足結果桁数
    * 
    * @return ゼロを補足した内容 <br>
    *         例えば、fillZeroToLen("123", 4)で "0123"を取得する。<br>
    *         fillZeroToLen("123", 6)で "000123"を取得する。
    */
   public static String fillZeroToLen(String src, int len) {
       StringBuilder sbTemp = new StringBuilder();
       for (int i = 0; i < len; i++) {
           sbTemp.append("0");
       }
       sbTemp.append(src);

       String strTemp = sbTemp.toString();
       return strTemp.substring(strTemp.length() - len);
   }

   /**
    * "1,234,567.123"のような画面表示形式の金額文字列から、 DBに挿入するために"1234567.123"のような形式に変換する。
    * 
    * @param src
    *            表示用金額フォーマット
    * 
    * @return フォーマット結果 <br>
    *         例えば、formatKingakuForDB("1,234,567.123")で"1234567.123"を取得する。
    */
   public static String formatKingakuForDB(String src) {

       // ◎金額パラメータの判断
       if (StringUtil.isEmpty(src)) {
           // ○金額の再設定
           src = "0";
       } else {
           // ○金額の構成
           src = src.replaceAll(",", "");

       }
       // 変換結果を戻る
       return String.valueOf(strToBigDecimal(src));
   }

   /**
    * "1234567.123"のようなDBからの金額文字列から、 画面に表示するために"1,234,567.123"のような形式に変換する。<br>
    * 
    * @param kingaku
    *            表示用金額フォーマット
    * 
    * @return フォーマット結果<br>
    *         例えば、formatKingakuForGamen("1234567.123")で"1,234,567.123"を取得する。
    */
   public static String formatKingakuForGamen(String kingaku) {

       String formatter = "#,##0.000";
       DecimalFormat deFormat = new DecimalFormat(formatter);
       // 変換結果を戻る
       return deFormat.format(strToBigDecimal(kingaku));
   }

   public static Double objectToDouble(Object argObj) {
       if (argObj == null) {
           return new Double(0);
       } else if ("".equals(argObj)) {
           return new Double(0);
       } else {
           return Double.parseDouble(argObj.toString());
       }
   }
}
