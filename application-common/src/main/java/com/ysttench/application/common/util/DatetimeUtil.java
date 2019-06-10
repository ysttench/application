package com.ysttench.application.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class DatetimeUtil {
    private static Log logger = LogFactory.getLog(DatetimeUtil.class);
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    public final static String DATE_PATTERN_YYYYMMDD = "yyyyMMdd";

    public final static String TIME_PATTERN = "HH:mm:ss";

    public final static String DATE_TIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;
    public final static String ORACLE_TIME_PATTERN = "HH24:mi:ss";
    public final static String ORACLE_DATE_TIME_PATTERN = DATE_PATTERN + " " + ORACLE_TIME_PATTERN;

    private static final String DATE_SPLIT = "/";

    /**
     * Date to String
     */
    public static String formatDate(Date date, String formatStyle) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(formatStyle);
            String formatDate = sdf.format(date);
            return formatDate;
        } else {
            return "";
        }
    }

    /**
     * Date to Date
     */
    public static Date formatDate(String formatStyle, Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(formatStyle);
            String formatDate = sdf.format(date);
            try {
                return sdf.parse(formatDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return new Date();
            }
        } else {
            return new Date();
        }
    }

    public static final Date convertStringToDate(String pattern, Locale locale, TimeZone zone, String strDate)
            throws ParseException {
        if (strDate == null || "".equals(strDate))
            return new Date();
        if (locale == null)
            locale = Locale.getDefault();
        if (zone == null)
            zone = TimeZone.getDefault();
        SimpleDateFormat df = new SimpleDateFormat(pattern, locale);
        df.setTimeZone(zone);
        try {
            return df.parse(strDate);
        } catch (ParseException pe) {
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }
    }

    public static final Date convertStringToDate(String strDate) {
        if (strDate == null || "".equals(strDate))
            return new Date();
        Locale locale = Locale.CHINESE;
        try {
            return convertStringToDate(DATE_PATTERN, locale, null, strDate);
        } catch (Exception e) {
            return null;
        }
    }

    public static final Date convertStringToDate(String strDate, String sytle) {
        if (strDate == null || "".equals(strDate))
            return new Date();
        Locale locale = Locale.CHINESE;
        try {
            return convertStringToDate(sytle, locale, null, strDate);
        } catch (Exception e) {
            return null;
        }
    }

    public static final String convertDateToString(String pattern, Locale locale, TimeZone zone, Date aDate) {
        if (locale == null)
            locale = Locale.getDefault();
        if (zone == null)
            zone = TimeZone.getDefault();
        SimpleDateFormat df = new SimpleDateFormat(pattern, locale);
        df.setTimeZone(zone);
        try {
            return df.format(aDate);
        } catch (Exception e) {
            return "";
        }
    }

    public static final String convertDateToString(String pattern, Date aDate) {
        Locale locale = Locale.CHINESE;
        return convertDateToString(pattern, locale, null, aDate);
    }

    /**
     * 提供yyyy-MM-dd类型的日期字符串转化
     */
    public static final Date getBeginDate(String beginDate) {
        Locale locale = Locale.CHINESE;
        try {
            return convertStringToDate(DATE_TIME_PATTERN, locale, null, beginDate + " 00:00:00");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 提供yyyy-MM-dd类型的日期字符串转化 专门提供Web页面结束日期转化 如输入2006-07-27，则转化为2006-07-28
     * 00:00:00
     */
    public static final Date getEndDate(String endDate) {
        Locale locale = Locale.CHINESE;
        try {
            Date date = convertStringToDate(DATE_TIME_PATTERN, locale, null, endDate + " 00:00:00");
            return new Date(date.getTime() + 24 * 3600 * 1000);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 提供yyyy-MM-dd类型的日期字符串转化 专门提供Web页面前一日日期转化 如输入2006-07-27，则转化为2006-07-26
     * 00:00:00
     */
    public static final Date getPreDate(String endDate) {
        Locale locale = Locale.CHINESE;
        try {
            Date date = convertStringToDate(DATE_TIME_PATTERN, locale, null, endDate + " 00:00:00");
            return new Date(date.getTime() - 24 * 3600 * 1000);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 提供yyyy-MM-dd类型的日期字符串转化 专门提供Web页面前N日日期转化 如输入2006-07-27，则转化为2006-07-26
     * 00:00:00
     */
    public static final Date getPreDate(String endDate, int day) {
        Locale locale = Locale.CHINESE;
        try {
            Date date = convertStringToDate(DATE_TIME_PATTERN, locale, null, endDate + " 00:00:00");
            return new Date(date.getTime() - day * 24 * 3600 * 1000);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * yyyy年mm月dd日 星期w
     */
    public static String getFullDateStr() {
        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL, Locale.CHINESE);
        return format.format(new Date());
    }

    /**
     * 计算两个日期之间相差的天数
     * 
     * @param date1
     * @param date2
     * @return
     */

    public static int diffdates(Date date1, Date date2) {
        int result = 0;
        if (date1 == null)
            return 0;
        if (date2 == null)
            return 0;

        GregorianCalendar gc1 = new GregorianCalendar();
        GregorianCalendar gc2 = new GregorianCalendar();

        gc1.setTime(date1);
        gc2.setTime(date2);
        result = getDays(gc1, gc2);

        return result;
    }

    public static int getDays(GregorianCalendar g1, GregorianCalendar g2) {
        int elapsed = 0;
        GregorianCalendar gc1, gc2;

        if (g2.after(g1)) {
            gc2 = (GregorianCalendar) g2.clone();
            gc1 = (GregorianCalendar) g1.clone();
        } else {
            gc2 = (GregorianCalendar) g1.clone();
            gc1 = (GregorianCalendar) g2.clone();
        }

        gc1.clear(Calendar.MILLISECOND);
        gc1.clear(Calendar.SECOND);
        gc1.clear(Calendar.MINUTE);
        gc1.clear(Calendar.HOUR_OF_DAY);

        gc2.clear(Calendar.MILLISECOND);
        gc2.clear(Calendar.SECOND);
        gc2.clear(Calendar.MINUTE);
        gc2.clear(Calendar.HOUR_OF_DAY);

        while (gc1.before(gc2)) {
            gc1.add(Calendar.DATE, 1);
            elapsed++;
        }
        return elapsed;
    }

    /**
     * 功能：将表示时间的字符串以给定的样式转化为java.util.Date类型
     * 且多于或少于给定的时间多少小时（formatStyle和formatStr样式相同）
     * 
     * @param:formatStyle 要格式化的样式,如:yyyy-MM-dd
     *                        HH:mm:ss
     * @param:formatStr 待转化的字符串(表示的是时间)
     * @param:hour 多于或少于的小时数(可正可负)
     *                 单位为小时
     * @return java.util.Date
     */
    public static Date formartDate(String formatStyle, String formatStr, int hour) {
        SimpleDateFormat format = new SimpleDateFormat(formatStyle, Locale.CHINA);
        try {
            Date date = new Date();
            date.setTime(format.parse(formatStr).getTime() + hour * 60 * 60 * 1000);
            return date;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 获取现在时刻
     */
    public static Date getNow() {
        return new Date(new Date().getTime());
    }

    /**
     * 获取前一小时
     */
    public static Date getPreHour() {
        return new Date(new Date().getTime() - 3600 * 1000L);
    }

    /**
     * 获取下一小时
     */
    public static Date getNextHour() {
        return new Date(new Date().getTime() + 3600 * 1000L);
    }

    /**
     * 获取昨天
     */
    public static Date getYesterday() {
        return new Date(new Date().getTime() - 24 * 3600 * 1000L);
    }

    /**
     * 获取前天
     */
    public static Date getPreYesterday() {
        return new Date(new Date().getTime() - 48 * 3600 * 1000L);
    }

    /**
     * 获取昨天
     */
    public static Date getYesterdayDate(Date day) {
        return new Date(day.getTime() - 24 * 3600 * 1000L);
    }

    /**
     * 获取明天
     */
    public static Date getTomorrowDate(Date day) {
        return new Date(day.getTime() + 24 * 3600 * 1000L);
    }

    /**
     * 获取上周
     */
    public static Date getLastWeek(Date day) {
        return new Date(day.getTime() - 7 * 24 * 3600 * 1000L);
    }

    /**
     * 获取下周
     */
    public static Date getNextWeek(Date day) {
        return new Date(day.getTime() + 7 * 24 * 3600 * 1000L);
    }

    /**
     * 获取上个月
     */
    public static Date getLastMonth() {
        return getLastMonth(new Date());
    }

    /**
     * 获得指定时间的某月的第一天
     * 
     * @param date
     * @return
     * 
     */
    public static Date getMonthFirstDayyyyyMMdd(Date date) {
        int[] dateArr = getDateArray(date);
        String year = String.valueOf(dateArr[0]);
        String month = String.valueOf(dateArr[1]);
        month = month.length() == 1 ? "0" + month : month;
        Date retDate = convertStringToDate(year + month + "01", DATE_PATTERN_YYYYMMDD);
        return retDate;
    }

    /**
     * 获得指定时间的某月的最后一天
     * 
     * @param date
     * @return
     * 
     */
    public static Date getMonthLastDayyyyyMMdd(Date date) {
        int[] dateArr = getDateArray(date);
        int year = dateArr[0];
        int month = dateArr[1];
        int maxDayOfMonth = getMaxDayOfMonth(year, month);
        String monStr = String.valueOf(month);
        monStr = monStr.length() == 1 ? "0" + monStr : monStr;
        Date retDate = convertStringToDate(
                String.valueOf(year) + String.valueOf(monStr) + String.valueOf(maxDayOfMonth), DATE_PATTERN_YYYYMMDD);
        return retDate;
    }

    /**
     * 获得指定时间的某月的第一天
     * 
     * @param date
     * @return
     * 
     */
    public static String getMonthFirstDay() {
        return getMonthFirstDay(new Date());
    }

    /**
     * 获得指定时间的某月的最后一天
     * 
     * @param date
     * @return
     * 
     */
    public static String getMonthLastDay() {
        return getMonthLastDay(new Date());
    }

    /**
     * 获得指定时间的某月的第一天
     * 
     * @param date
     * @return
     * 
     */
    public static String getMonthFirstDay(Date date) {
        int[] dateArr = getDateArray(date);
        String year = String.valueOf(dateArr[0]);
        String month = String.valueOf(dateArr[1]);
        month = month.length() == 1 ? "0" + month : month;
        return year + "-" + month + "-" + "01";
    }

    /**
     * 获得指定时间的某月的最后一天
     * 
     * @param date
     * @return
     * 
     */
    public static String getMonthLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endDate = format.format(cal.getTime());
        return endDate;
    }

    /**
     * 获取制定时间的上个月
     */
    public static Date getLastMonth(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.clear();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        cal.getTime();
        return cal.getTime();
    }

    /**
     * 获取制定时间的下个月
     */
    public static Date getNextMonth(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.clear();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        cal.getTime();
        return cal.getTime();
    }

    /**
     * 获取指定年和月中该月的最大天数
     * 
     * @param year
     *            指定年
     * @param month
     *            指定月 1-12
     * @return 该月最大天数
     */
    public static int getMaxDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.clear();
        cal.set(year, month - 1, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据指定的年份和指定的第多少周序号得到该周的第一天和最后一天日期
     * 
     * @param year
     *            指定的年份,如2006
     * @param weekNo
     *            指定年份中的第多少周,如37
     * @return 该周的起始日期后该周的结束日期<br>
     *         Date[0] 起始日期<br>
     *         Date[1] 结束日期
     */
    public static Date[] getGivenWeekDates(int year, int weekNo) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        Date begin = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, 6);
        Date end = cal.getTime();
        return new Date[] { begin, end };
    }

    /**
     * 根据指定日期获取其在一年中的第多少周
     * 
     * @param date
     *            指定的日期,为null默认为当时日期
     * @return 当年的第多少周序号,如37
     */
    public static int getWeekNo(Date date) {
        if (date == null)
            date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.clear();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取制定时间的年份
     * 
     * @param date
     *            制定时间
     * @return 年份
     */
    public static int getYear(Date date) {
        if (date == null)
            date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.clear();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取制定时间的月份
     * 
     * @param date
     *            制定时间
     * @return 年份
     */
    public static int getMonth(Date date) {
        if (date == null)
            date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.clear();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    /**
     * 获取制定时间的月份
     * 
     * @param date
     *            制定时间
     * @return 年份
     */
    public static int getDay(Date date) {
        if (date == null)
            date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.clear();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 格式化日期
     * 
     * @param date
     *            被格式化的日期
     * @param style
     *            显示的样式，如yyyyMMdd
     */
    public static String fmtDate(Date date, String style) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(style);
        return dateFormat.format(date);
    }

    /**
     * 得到当前日期
     * 
     * @return int[] int[0] 年 int[1] 月 int[2] 日 int[3] 时 int[4] 分 int[5] 秒
     */
    public static int[] getCurrentDate() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.setTime(new Date());
        int[] date = new int[6];
        date[0] = cal.get(Calendar.YEAR);
        date[1] = cal.get(Calendar.MONTH) + 1;
        date[2] = cal.get(Calendar.DATE);
        date[3] = cal.get(Calendar.HOUR_OF_DAY);
        date[4] = cal.get(Calendar.MINUTE);
        date[5] = cal.get(Calendar.SECOND);
        return date;
    }

    /**
     * 得到指定日期
     * 
     * @return int[] int[0] 年 int[1] 月 int[2] 日 int[3] 时 int[4] 分 int[5] 秒
     * 
     */
    public static int[] getDateArray(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
        cal.setTime(date);
        int[] dateArr = new int[6];
        dateArr[0] = cal.get(Calendar.YEAR);
        dateArr[1] = cal.get(Calendar.MONTH) + 1;
        dateArr[2] = cal.get(Calendar.DATE);
        dateArr[3] = cal.get(Calendar.HOUR_OF_DAY);
        dateArr[4] = cal.get(Calendar.MINUTE);
        dateArr[5] = cal.get(Calendar.SECOND);
        return dateArr;
    }

    /**
     * 设置制定的年份和月份，再得到该日期的前多少月或后多少月的日期年份和月份
     * 
     * @param year
     *            指定的年份，如 2006
     * @param month
     *            制定的月份，如 6
     * @param monthSect
     *            月份的差值 如：现在为2006年5月份，要得到后4月，则monthSect = 4，正确日期结果为2006年9月
     *            如：现在为2006年5月份，要得到前4月，则monthSect = -4，正确日期结果为2006年1月
     *            如：monthSect = 0，则表示为year年month月
     * @return int[] int[0] 年份 int[1] 月份
     */
    public static int[] getLimitMonthDate(int year, int month, int monthSect) {
        year = year < 1 ? 1 : year;
        month = month > 12 ? 12 : month;
        month = month < 1 ? 1 : month;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), new Locale("zh", "CN"));
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.add(Calendar.MONTH, monthSect);
        int[] yAndM = new int[2];
        yAndM[0] = cal.get(Calendar.YEAR);
        yAndM[1] = cal.get(Calendar.MONTH);
        if (yAndM[1] == 0) {
            yAndM[0] = yAndM[0] - 1;
            yAndM[1] = 12;
        }
        return yAndM;
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     * 
     * @return
     * @throws ParseException
     */
    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
        String fd = sdf.format(date);
        return fd;
    }

    /**
     * HH:mm:ss
     * 
     * @return
     * @throws ParseException
     */
    public static String getDateHHmmss() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_PATTERN);
        String fd = sdf.format(date);
        return fd;
    }

    /**
     * 时间戳
     * 
     * @return
     */
    public static String getTime() {
        Date date = new Date();
        return date.getTime() + "";
    }

    /**
     * yyyyMMddhhmmss
     * 
     * @return
     * @throws ParseException
     */
    public static String getDateyyyyMMddhhmmss() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fd = sdf.format(date);
        return fd;
    }

    /**
     * yyyyMMdd
     * 
     * @return
     * @throws ParseException
     */
    public static String getDateyyyyMMddNone() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_YYYYMMDD);
        String fd = sdf.format(date);
        return fd;
    }

    /**
     * 当月第一天
     * 
     * @return
     */
    public static String getDayofMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_YYYYMMDD);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, 1);
        String bdate = sdf.format(c.getTime());
        return bdate;
    }

    /**
     * yyyyMMddhhmmss
     * 
     * @return
     * @throws ParseException
     */
    public static String getDateHHmmssNone() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String fd = sdf.format(date);
        return fd;
    }

    /**
     * yyyy-MM-dd
     * 
     * @return
     * @throws ParseException
     */
    public static String getDateyyyyMMdd() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        String fd = sdf.format(date);
        return fd;
    }

    public static Date getDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTERN);
        try {
            return format.parse(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getSimpleDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
        try {
            return format.parse(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 得到本月的第一天和最后一天的字符串的数组
     * 
     * @param month
     *            格式 'yyyyMM'
     * @return
     * 
     */
    public static String[] getMonFirstLastDays(String month) {
        Date thisDate = convertStringToDate(month, "yyyyMM");
        Date firstDay = getMonthFirstDayyyyyMMdd(thisDate);
        Date lastDay = getMonthLastDayyyyyMMdd(thisDate);
        return new String[] { convertDateToString(DATE_PATTERN_YYYYMMDD, firstDay),
                convertDateToString(DATE_PATTERN_YYYYMMDD, lastDay) };
    }

    /**
     * 获取传入时间的当月的日期 yyyymmdd author:Liu Liming
     * 
     * @param yyyymmdd
     * @return
     */
    public static String getFirstDate(String yyyymmdd) {
        try {
            Date date = DatetimeUtil.convertStringToDate(DATE_PATTERN_YYYYMMDD, null, null, yyyymmdd);
            Calendar curCal = Calendar.getInstance();
            curCal.setTime(date);
            curCal.set(Calendar.DATE, 1);
            return DatetimeUtil.convertDateToString(DATE_PATTERN_YYYYMMDD, curCal.getTime());
        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * 获取传入时间的当月的日期 yyyymmdd author:Liu Liming
     * 
     * @param yyyymmdd
     * @return yyyymmdd
     */
    public static String getLastDate(String yyyymmdd) {
        try {
            String sDate = getFirstDate(yyyymmdd);
            Date date = DatetimeUtil.convertStringToDate(DATE_PATTERN_YYYYMMDD, null, null, sDate);
            Calendar retVal = Calendar.getInstance();
            retVal.setTime(date);
            retVal.add(Calendar.MONTH, 1);
            retVal.add(Calendar.DATE, -1);
            return DatetimeUtil.convertDateToString(DATE_PATTERN_YYYYMMDD, retVal.getTime());

        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * 获取Next天
     */
    public static String getNextDay(Object date, int amount) {
        SimpleDateFormat frm = new SimpleDateFormat(DATE_TIME_PATTERN);
        Calendar calendar = Calendar.getInstance();
        try {
            if (date instanceof String) {
                calendar.setTime(frm.parse(date.toString()));
            } else if (date instanceof Date) {
                calendar.setTime((Date) date);
            }
            calendar.add(Calendar.DATE, amount);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return frm.format(calendar.getTime());
    }

    /**
     * 获取Next天
     */
    public static String getNextDay(Object date, int amount, String pattern) {
        SimpleDateFormat frm = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        try {
            if (date instanceof String) {
                calendar.setTime(frm.parse(date.toString()));
            } else if (date instanceof Date) {
                calendar.setTime((Date) date);
            }
            calendar.add(Calendar.DATE, amount);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return frm.format(calendar.getTime());
    }

    /**
     * 获取Next MINUTE
     */
    public static String getNextMinute(Object date, int amount) {
        SimpleDateFormat frm = new SimpleDateFormat(DATE_TIME_PATTERN);
        Calendar calendar = Calendar.getInstance();
        try {
            if (date instanceof String) {
                calendar.setTime(frm.parse(date.toString()));
            } else if (date instanceof Date) {
                calendar.setTime((Date) date);
            }
            calendar.add(Calendar.MINUTE, amount);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return frm.format(calendar.getTime());
    }

    /**
     * 将字符串20080808 转换成 2008-08-08
     */
    public static String getDateForm(String date) {
        if (StringUtils.isBlank(date) || date.length() != 8) {
            return "0000-00-00";
        }
        return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
    }

    /**
     * @Description:计算给定date在给定day后的值
     * @param date
     *            合法的java日期格式
     * @param day
     *            计算的天数
     * @param format
     *            日期格式化参数 与date格式一致
     * @return
     */
    public static String calculateDate(String date, int day, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            Date d = df.parse(date);
            Calendar c = Calendar.getInstance();

            c.setTime(d);
            c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + day);

            return df.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断是否为同一天
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        boolean result = false;
        try {
            String date1Str = DatetimeUtil.convertDateToString(DATE_PATTERN_YYYYMMDD, date1);
            String date2Str = DatetimeUtil.convertDateToString(DATE_PATTERN_YYYYMMDD, date2);
            if (date1Str.equals(date2Str)) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 增加或减少天数
    public static Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    /**
     * 日期转换成string类型学
     * 
     * @param date
     * @param type
     * @return
     */
    public static String dateToString(Date date, String type) {
        if (date == null)
            return "";
        String str = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (type.equals("SHORT")) {
            // 07-1-18
            format = DateFormat.getDateInstance(DateFormat.SHORT);
            str = format.format(date);
        } else if (type.equals("MEDIUM")) {
            // 2007-1-18
            format = DateFormat.getDateInstance(DateFormat.MEDIUM);
            str = format.format(date);
        } else if (type.equals("FULL")) {
            // 2007年1月18日 星期四
            format = DateFormat.getDateInstance(DateFormat.FULL);
            str = format.format(date);
        }
        return str;
    }

    /**
     * string to date
     * 
     * @param str
     * @return
     */
    public static Date stringToDate(String str) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            // Fri Feb 24 00:00:00 CST 2012
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 2012-02-24
        date = java.sql.Date.valueOf(str);

        return date;
    }

    /**
     * 北京时间转unix时间戳
     * 
     * @param where
     *            如果是查询当日的消息记录，where 是从凌晨到晚上23点整
     * @param newDate
     *            1 表示是查询当日，否则就是查询其他日期
     * @return
     * @throws Exception
     */
    public static String getUnixTime(String where, String newDate) throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(date);
        // 如果等于1表示是查询当日记录
        if (newDate.equals("1")) {
            newDate = today + " " + where;
        } else {// 否则就查询要查询日期的客服记录
            newDate = newDate + " " + where;
        }
        long n = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(newDate).getTime();
        return String.valueOf(n).substring(0, 10);
    }

    /**
     * 北京时间转unix时间戳
     * 
     * @param where
     *            如果是查询当日的消息记录，where 是从凌晨到晚上23点整
     * @param newDate
     *            1 表示是查询当日，否则就是查询其他日期
     * @return
     * @throws Exception
     */
    public static String getUnixTime2(String where, String date) throws Exception {
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = date + " " + where;
        long n = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(today).getTime();
        return String.valueOf(n).substring(0, 10);
    }

    /**
     * 将unix时间戳转换成普通时间
     * 
     * @param timestampString
     * @return
     */
    public static String TimeStamp2Date(String timestampString) {
        // String times = Long.toString(timestampString);
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(timestamp));
        return date;
    }

    /**
     * 返回当前时间 格式：yyyy-MM-dd hh:mm:ss
     * 
     * @return String
     */
    public static String fromDateH() {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format1.format(new Date());
    }

    /**
     * 将长时间格式转化成字符串yyyy-MM-dd HH:mm:ss
     * 
     * @param dateDate
     * @return
     */
    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 返回当前时间 格式：yyyy-MM-dd
     * 
     * @return String
     */
    public static String fromDateY() {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(new Date());
    }

    /**
     * 根据生日算年龄
     * 
     * @param birth
     *            yyyy-MM-dd
     * @return
     */
    public static Integer getAge(String birth) {
        Integer age = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowdate = sdf.format(new Date());
        String[] ageb = birth.split("-");// 出生日期
        String[] agen = nowdate.split("-");// 现时
        Integer year = Integer.valueOf(agen[0]) - Integer.valueOf(ageb[0]);
        Integer mouth = Integer.valueOf(agen[1]) - Integer.valueOf(ageb[1]);
        Integer day = Integer.valueOf(agen[2]) - Integer.valueOf(ageb[2]);
        if (mouth < 0) {
            year--;
        } else if (mouth == 0 && day < 0) {
            year--;
        } else {
        }
        age = year;
        return age;
    }

    public static int getSysYear() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.YEAR);
    }

    public static int getSysMonth() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.MONTH) + 1;
    }

    public static int getSysDay() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.DAY_OF_MONTH);
    }

    public static int getSysHour() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.HOUR_OF_DAY);
    }

    public static int getSysMinute() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.MINUTE);
    }

    public static int getSysSecond() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.SECOND);
    }

    public static int getSysMillisecond() {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.MILLISECOND);
    }

    public static String getSysDate() {
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH) + 1;
        int day = today.get(Calendar.DAY_OF_MONTH);
        StringBuilder rtnStr = new StringBuilder("");
        rtnStr.append(String.valueOf(year));
        rtnStr.append(DATE_SPLIT);
        rtnStr.append(StringUtil.leftPad(String.valueOf(month), 2, "0"));
        rtnStr.append(DATE_SPLIT);
        rtnStr.append(StringUtil.leftPad(String.valueOf(day), 2, "0"));
        return rtnStr.toString();
    }

    public static String getSysTime() {
        Calendar today = Calendar.getInstance();
        int hour = today.get(Calendar.HOUR_OF_DAY);
        int minute = today.get(Calendar.MINUTE);
        int second = today.get(Calendar.SECOND);
        StringBuilder rtnStr = new StringBuilder("");
        rtnStr.append(StringUtil.leftPad(String.valueOf(hour), 2, "0"));
        rtnStr.append(":");
        rtnStr.append(StringUtil.leftPad(String.valueOf(minute), 2, "0"));
        rtnStr.append(":");
        rtnStr.append(StringUtil.leftPad(String.valueOf(second), 2, "0"));
        return rtnStr.toString();
    }

    public static String getSysTimestamp() {
        return getSysDate() + " " + getSysTime() + "." + getSysMillisecond();
    }

    public static synchronized String getSysTimeStr() {
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH) + 1;
        int day = today.get(Calendar.DAY_OF_MONTH);
        int hour = today.get(Calendar.HOUR_OF_DAY);
        int minute = today.get(Calendar.MINUTE);
        int second = today.get(Calendar.SECOND);
        int millisecond = today.get(Calendar.MILLISECOND);
        StringBuilder rtnStr = new StringBuilder("");
        rtnStr.append(String.valueOf(year));
        rtnStr.append(StringUtil.leftPad(String.valueOf(month), 2, "0"));
        rtnStr.append(StringUtil.leftPad(String.valueOf(day), 2, "0"));
        rtnStr.append("_");
        rtnStr.append(StringUtil.leftPad(String.valueOf(hour), 2, "0"));
        rtnStr.append(StringUtil.leftPad(String.valueOf(minute), 2, "0"));
        rtnStr.append(StringUtil.leftPad(String.valueOf(second), 2, "0"));
        rtnStr.append("_");
        rtnStr.append(String.valueOf(millisecond));
        return rtnStr.toString();
    }

    /**
     * 時間のデータのフォーマットの検査
     * 
     * @param dateStr
     * @return ture:フォーマットは正しいです,false:フォーマットの誤り
     */
    public static boolean isDateStringValid(String dateStr) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try {
            if (sdf.format(sdf.parse(dateStr)).equalsIgnoreCase(dateStr))
                return true;
            else
                return false;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 日付範囲の検査
     * 
     * @param startStr
     *            ,endStr
     * @return ture:フォーマットは正しいです,false:フォーマットの誤り
     */
    public static boolean CompareDate(String startStr, String endStr) {

        String startDate = startStr.replace("/", "");
        String endDate = endStr.replace("/", "");
        if (startDate.compareTo(endDate) > 0) {
            return false;
        }
        return true;
    }

    /**
     * 期日型データの転換
     * 
     * @param argDate
     * @throws SQLException
     * @return 期日(Date)
     * @throws java.text.ParseException
     */
    public static Date ConvertDate(String argDate) {
        Date date = null;
        try {
            if (argDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                date = sdf.parse(argDate);
            }
            return date;
        } catch (ParseException ex) {
            return null;
        }
    }

    /**
     * 入力パラメータで指定されたフォーマット形式で現在のシステム日付を取得する。。<br>
     * 
     * @param pattern
     *            yyyy/MM/dd HH:mm:ss
     * 
     * @return yyyy/MM/dd HH:mm:ss
     */
    public static String getSystemTime(String pattern) {

        // 出力日付の型式の初期
        SimpleDateFormat fl = new SimpleDateFormat(pattern);

        // 現在日付
        Date date = new Date();

        // システム日付を貰ってフォマットして戻す
        return fl.format(date);
    }

    /**
     * 入力パラメータで指定されたフォーマット形式で指定された日付を変換する。<br>
     * 
     * @param date
     *            日付
     * 
     * @param format
     *            例えば：yyyyMMdd
     * 
     * @return yyyyMMdd
     */
    public static String getFormatDate(Date date, String format) {

        if (date == null) {
            return "";
        }
        // 出力日付の型式の初期
        SimpleDateFormat fl = new SimpleDateFormat(format);

        // システム日付を貰ってフォマットして戻す
        return fl.format(date);
    }

    /**
     * 给定两个时间相差的月数
     */
    // 本月和未来一个月的月份差
    public static int monthsBetween(GregorianCalendar pFurtherMonth) {
        GregorianCalendar vToday = new GregorianCalendar();
        GregorianCalendar vFurtherMonth = (GregorianCalendar) pFurtherMonth.clone();
        return monthsBetween(vToday, vFurtherMonth);
    }

    /** 给定月分和本月的月份差 **/
    public static int monthsBetween(String pFurtherMonth) {
        GregorianCalendar vToday = new GregorianCalendar();
        GregorianCalendar vFurtherMonth = DatetimeUtil.parse2Cal(pFurtherMonth);
        return monthsBetween(vToday, vFurtherMonth);
    }

    /** 给定两个时间相差的月数,String版 **/
    public static int monthsBetween(String pFormerStr, String pLatterStr) {
        GregorianCalendar vFormer = DatetimeUtil.parse2Cal(pFormerStr);
        GregorianCalendar vLatter = DatetimeUtil.parse2Cal(pLatterStr);
        return monthsBetween(vFormer, vLatter);
    }

    /** 给定两个时间相差的月数,String版 **/
    public static int monthsBetween(Date pFormerDate, Date pLatterDate) {
        String vForMerDate = DatetimeUtil.getFormatDate(pFormerDate, "yyyy/MM/dd");
        String vLatterDate = DatetimeUtil.getFormatDate(pLatterDate, "yyyy/MM/dd");
        return monthsBetween(vForMerDate, vLatterDate);
    }

    public static int monthsBetween(GregorianCalendar pFormer, GregorianCalendar pLatter) {
        GregorianCalendar vFormer = pFormer, vLatter = pLatter;
        boolean vPositive = true;
        if (pFormer.before(pLatter)) {
            vFormer = pFormer;
            vLatter = pLatter;
        } else {
            vFormer = pLatter;
            vLatter = pFormer;
            vPositive = false;
        }

        int vCounter = 0;
        while (vFormer.get(Calendar.YEAR) != vLatter.get(Calendar.YEAR)
                || vFormer.get(Calendar.MONTH) != vLatter.get(Calendar.MONTH)) {
            vFormer.add(Calendar.MONTH, 1);
            vCounter++;
        }
        if (vPositive)
            return vCounter;
        else
            return -vCounter;
    }

    public static GregorianCalendar parse2Cal(String pFormerStr) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(ConvertDate(pFormerStr));
        return calendar;
    }

    /** */
    /**
     * 计算两个时间之间相隔天数
     * 
     * @param startday
     *            开始时间
     * @param endday
     *            结束时间
     * @return
     */
    public int getDaysBetween(Calendar d1, Calendar d2) {
        if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
            java.util.Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);// 得到当年的实际天数
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /** */
    /**
     * 计算两个时间之间相隔天数
     * 
     * @param startday
     *            开始时间
     * @param endday
     *            结束时间
     * @return
     */
    public int getIntervalDays(Date startday, Date endday) {
        // 确保startday在endday之前
        if (startday.after(endday)) {
            Date cal = startday;
            startday = endday;
            endday = cal;
        }
        // 分别得到两个时间的毫秒数
        long sl = startday.getTime();
        long el = endday.getTime();

        long ei = el - sl;
        // 根据毫秒数计算间隔天数
        return (int) (ei / (1000 * 60 * 60 * 24));
    }

    public static boolean isdate(String s) {
        String a[] = s.split("/");
        boolean flg = true;
        if (!(Integer.parseInt(a[0]) >= 1950 && Integer.parseInt(a[0]) <= 2050)) {
            flg = false;
        }
        return flg;
    }

    public static boolean checkDate(String s) {
        boolean ret = true;
        try {
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            ret = df.format(df.parse(s)).equals(s);
        } catch (ParseException e) {
            ret = false;
        }
        return ret;
    }

    public static Date parseDate(String s) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date3 = bartDateFormat.parse(s);
            date3 = bartDateFormat.parse(s);
            return date3;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getNextMonth(String vFromMonth) {
        Date date = getNextMonth(parseDate(vFromMonth));
        // 格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
        return sdf.format(date);
    }

    /**
     * inputCalendarを基に月数を加/減後のCalendarを戻る。<br>
     * 
     * @param inputCalendar
     *            日付
     * 
     * @param month
     *            +/- 月数
     * 
     * @return inputCalendarを基に月数を加/減後のCalendar <br>
     *         例えば、(2009/6/2,1)で2009/7/2を取得する。<br>
     *         (2009/6/2, -1)で2009/5/2を取得する。
     */
    public static Calendar getMonthAddedCalendar(Calendar inputCalendar, int month) {

        // 日付を取得する
        Calendar outputCalendar = Calendar.getInstance();
        outputCalendar = inputCalendar;

        // inputCalendarを基に月数を加/減する
        outputCalendar.add(Calendar.MONTH, month);

        return outputCalendar;
    }

    /**
     * 現在のシステム日付のCalendarを取得する。<br>
     * 
     * @return システム日付
     */
    public static Calendar getSystemCalendar() {

        return Calendar.getInstance();
    }

    /**
     * inputCalendarを基に日数を加/減後のCalendarを戻る。<br>
     * 
     * @param inputCalendar
     *            日付
     * 
     * @param date
     *            +/- 日数
     * 
     * @return inputCalendarを基に日数を加/減後のCalendarを戻る。<br>
     *         例えば、(2009/6/2,1)で2009/6/3を取得する。<br>
     *         (2009/6/2, -1)で2009/6/1を取得する。
     */
    public static Calendar getDateAddedCalendar(Calendar inputCalendar, int date) {

        // 日付を取得する
        Calendar outputCalendar = Calendar.getInstance();
        outputCalendar = inputCalendar;

        // inputCalendarを基に日数を加/減する
        outputCalendar.add(Calendar.DATE, date);

        return outputCalendar;

    }

    /**
     * calendarを基に、"yyyy/MM/dd"形式の文字列を取得する。<br>
     * 
     * @param calendar
     *            日付
     * 
     * @return "yyyy/MM/dd"形式の文字列
     */
    public static String getStringByCalendar(Calendar calendar) {

        // 年
        int year = calendar.get(Calendar.YEAR);
        // 月 Calendar.MONTH が 0 からので、
        int month = calendar.get(Calendar.MONTH) + 1;
        // 日
        int date = calendar.get(Calendar.DATE);

        // "yyyy/MM/dd"形式で組み合わせ
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(year);
        stringBuilder.append("/");
        stringBuilder.append(NumericUtil.fillZeroToLen(month, 2));
        stringBuilder.append("/");
        stringBuilder.append(NumericUtil.fillZeroToLen(date, 2));

        return stringBuilder.toString();
    }

    /**
     * dateを基に、"yyyy/MM/dd"形式の文字列を取得する。<br>
     * 
     * @param date
     *            日付(yyyyMMdd)
     * 
     * @return フォーマットした日付(yyyy/MM/dd)
     */
    public static String formatDateForDisplay(String date) {

        String strRet = "";

        // ◎パラメータの判断
        if (StringUtil.isEmpty(date)) {
            return "";
        }

        // ◎非空、桁数!=８の場合
        if (date.length() != 8) {
            return date;
        }

        // ◎非空、桁数＞＝８の場合
        strRet = StringUtil.concat(date.substring(0, 4), "/", date.substring(4, 6), "/", date.substring(6));

        return strRet;
    }

    /**
     * dateを基に、"yyyyMMdd"形式の文字列を取得する。<br>
     * 
     * @param date
     *            日付(yyyy/MM/dd)
     * 
     * @return フォーマットした日付(yyyyMMdd)
     */
    public static String formatDateForDB(String date) {

        // ◎パラメータの判断
        if (StringUtil.isEmpty(date)) {
            return "";
        }

        // ◎非空、桁数＜１０の場合
        if (date.length() < 10) {
            return date;
        }

        // ◎非空、桁数＞＝１０の場合
        return date.substring(0, 10).replaceAll("/", "");
    }

    /**
     * システム日付を基に、指定されたmonthsを加減し、patternの形式で戻る。<br>
     * 
     * @param pattern
     *            YY/MM
     * 
     * @param months
     *            月数
     * 
     * @return ベース日付の上に月の加減算結果。<br>
     *         例えば、現在のシステム日付が2009/6/2の場合、 addMonth("yy/MM", 1)で "09/07"を取得する。
     */
    public static String addMonth(String pattern, int months) {

        // カレンダー変数の初期
        Calendar cal = Calendar.getInstance();

        Date date = new Date();

        // 日付のセット
        cal.setTime(date);

        // 日付の計算
        cal.add(Calendar.MONTH, months);

        // 出力日付の型式の初期
        SimpleDateFormat fl = new SimpleDateFormat(pattern);

        // 日付をフォマットして戻す
        return fl.format(cal.getTime());
    }
}
