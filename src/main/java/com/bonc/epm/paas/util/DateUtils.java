package com.bonc.epm.paas.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;

/**
 * 日期方法公共类
 * @author ke_wang
 * @version 2016年11月11日
 * @see DateUtils
 * @since
 */
public class DateUtils {
    /**
     * Date类型变量
     */
    private static Date DATE;
    /**
     * 
     */
    private static Calendar CALENDAR = Calendar.getInstance();
    
    /**
     * 
     */
    private static SimpleDateFormat SIMPLEDATEFORMAT = new SimpleDateFormat();

    /**
     * YYYY_MM_DD_HH_MM_SS
     */
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    

    /**
     * 
     * Description:
     * 将Date类型转换成pattern式的字符串
     * @param date Date
     * @param pattern String
     * @return String 
     * @see
     */
    public static String formatDateToString(Date date,String pattern){
        SimpleDateFormat dateFormater = new SimpleDateFormat(pattern);
        return dateFormater.format(date);
    }
    
    /**
     * 
     * Description:
     * 将String类型转换成pattern格式的日期
     * @param date String
     * @return Date 
     * @see
     */
    public static Date formatStringToDate(String date){
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        try {
            return sdf.parse(date);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 
     * Description:
     * 给当前日期的年、月、日、时、分、秒 添加一个增量值amount
     * @param date Date
     * @param type String
     * @param amount int
     * @return Date 
     * @see
     */
    public static Date dateCompute(Date date, String type,  int amount){
        GregorianCalendar cal = new GregorianCalendar();  
        cal.setTime(date);  
        int field = 0;
        switch (type) {
            case "year":
                field = 1;
                break;
            case "month":
                field = 2;
                break;
            case "week":
                field = 3;
                break;
            case "day":
                field = 5;
                break;
            case "hour":
                field = 10;
                break;
            case "minute":
                field = 12;
                break;
            case "second":
                field = 13;
                break;
            default:
                field = 0;
        }
        cal.add(field, amount);
        return cal.getTime();
    }
    
    /**
     * 取得当前时间
     * @see Date
     * @see Calender
     * @return Date 当前日期
     */
    public static Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    /**
     * 获取当前时间，short类型
     * @see Calendar
     * @return Date 
     */
    public static Date getShortCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR,0);
        return cal.getTime();
    }
    
    /**
     * 获取今天时间， 时分秒毫秒为0。
     * @return Date
     */
    public static Date getToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY,0);
        return cal.getTime();
    }
    
    /**
     * 取得昨天此时的时间值
     * @see Date
     * @return 昨天日期
     */
    public static Date getYesterdayDate() {
        return new Date(getCurTimeMillis() - 0x5265c00L);
    }
    
    /**
     * 取得昨天此时的时间值
     * @see Date
     * @return 昨天日期
     */
    public static Date getTomorrowDate() {
        return new Date(getCurTimeMillis() + 0x5265c00L);
    }
    
    /**
     * 取得昨天此时的时间值
     * @param day Long
     * @return 昨天日期
     */
    public static Date getNextDate(Long day) {
        return new Date(getCurTimeMillis() + 0x5265c00L*day);
    }


    /**
     * 根据传入参数获得昨日日期
     * @param date Date
     * @return Date 
     */
    public static Date getYesterdayDateByCur(Date date) {
        return new Date(date.getTime() - 0x5265c00L);
    }
    /**
     * 将String类型日期转化成java.sql.Timestamp类型"2003-01-01"格式
     * @see java.sql.Timestamp
     * @param str String
     * @return Timestamp
     */
    public static java.sql.Timestamp stringToTimestamp(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = null;
        try {
            date = sdf.parse(str);
        } 
        catch (Exception e) {
            return null;
        }
        return new java.sql.Timestamp(date.getTime());
    }

    /**
     * 获取当前日期之后N天的时间
     * @param date String
     * @param n long
     * @see SimpleDateFormat
     * @return N天之后的日期
     */
    public static Date getAfterDaysDate(String date, long n) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss"); 
        Date dd = null;
        try {
            dd = format.parse(date);
        } 
        catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dd);
        calendar.add(Calendar.DAY_OF_MONTH,(int)n); 
        return calendar.getTime();
    }
    
    /**
     * 获取当前日期之后N天的时间
     * @param date Date
     * @param n long
     * @return N天之后的日期
     */
    public static Date getAfterDaysDate(Date date, long n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,(int)n); 
        return calendar.getTime();
    }
    /**
     * 获取当前日期之后N小时后的时间
     * @param date Date
     * @param n long
     * @return N小时之后的日期
     */
    public static Date getAfterHoursDate(Date date, long n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, (int)n);
        return calendar.getTime();
    }
    /**
     * 获取当前时间相差N分钟的时间
     * @param date Date
     * @param n long
     * @return 相差N分钟的时间
     */
    public static Date getDiffMinuteDate(Date date, long n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, (int)n);
        return calendar.getTime();
    }
    /**
     * 获取当前日期之后N分钟后的时间
     * @param date Date
     * @param n long
     * @return N分钟之后的日期
     */
    public static Date getAfterMinuteDate(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, n);
        return calendar.getTime();
    }
    /**
     * 
    * @Title: getAfterMilliSecondsDate 
    * @Author:dxm
    * @Description: 获取当前日期之后N毫秒后的时间
    * @param date Date
    * @param n long
    * @return Date    返回类型 
    * @throws 
    * @date 2016-01-19 16:34:22 +0800
     */
    public static Date getAfterMilliSecondsDate(Date date, long n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, (int)n);
        return calendar.getTime();
    }
    /**
     * 获取当前时间戳
     * @return time Timestamp
     */
    public static Timestamp getCurrentTimestamp() {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(Calendar.getInstance().getTime());
        Timestamp time = Timestamp.valueOf(date);
        return time;
    }

    /**
     * 时间戳
     * @param time Timestamp
     * @return  Timestamp
     */
    public static Timestamp getTimestamp(Timestamp time) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
        return Timestamp.valueOf(date);
    }
    
    /**
     * 获得格式如yyyy-MM-dd HH:mm:ss的日期
     * @param date Date
     * @return date Date
     */
    public static Date getFormatDate(Date date) {
        if(date == null){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        try {
            return date = sdf.parse(dateStr);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 返回指定格式的日期
     * @param date Date
     * @param pattern String
     * @return date Date
     */
    public static Date getFormatDate(Date date, String pattern) {
        if(date == null){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dateStr = sdf.format(date);
        try {
            return date = sdf.parse(dateStr);
        } 
        catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 获取格式如"yyyy-MM-dd"的日期
     * @param date Date
     * @return date Date
     */
    public static Date getFormatDate1(Date date) {
        if(date == null){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        try {
            return date = sdf.parse(dateStr);
        } 
        catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 转化成日期格式
     * @param cal XMLGregorianCalendar
     * @return time Date
     * @throws Exception 转换异常
     */
    public static Date convertToDate(XMLGregorianCalendar cal) throws Exception {
        if(cal == null){
            return null;
        }
        GregorianCalendar ca = cal.toGregorianCalendar();
        return ca.getTime();
    }

    /**
     * 取得当前时间的长整型表示
     * @return 当前时间（long）
     */
    public static long getCurTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 取得当前时间的特定表示格式的字符串
     * 
     * @param formatDate
     *            时间格式（如：yyyy/MM/dd hh:mm:ss）
     * @return 当前时间
     */
    public static synchronized String getCurFormatDate(String formatDate) {
        DATE = getCurrentDate();
        SIMPLEDATEFORMAT.applyPattern(formatDate);
        return SIMPLEDATEFORMAT.format(DATE);
    }

    /**
     * 取得某日期时间的特定表示格式的字符串
     * @param format 时间格式
     * @param date 某日期
     * @return String 
     */
    public static synchronized String getDate2Str(String format, Date date) {
        if (date == null) {
            return "";
        }
        SIMPLEDATEFORMAT.applyPattern(format);
        return SIMPLEDATEFORMAT.format(date);
    }

    /**
     * 将日期转换为长字符串（包含：年-月-日 时:分:秒）
     * 
     * @param date
     *            日期
     * @return 返回型如：yyyy-MM-dd HH:mm:ss 的字符串
     */
    public static String getDate2LStr(Date date) {
        return getDate2Str("yyyy-MM-dd HH:mm:ss", date);
    }

    public static String getDateStr(Date date) {
        return getDate2Str("yyyy-MM-dd-HH-mm-ss", date);
    }
    
    /**
     * 将日期转换为长字符串（包含：年-月-日 时:分:秒）
     * 
     * @param date
     *            日期
     * @return 返回型如：yyyy/MM/dd HH:mm:ss 的字符串
     */
    public static String getDate2LStr2(Date date) {
        return getDate2Str("yyyy/MM/dd HH:mm:ss", date);
    }

    /**
     * 将日期转换为中长字符串（包含：年-月-日 时:分）
     * 
     * @param date
     *            日期
     * @return 返回型如：yyyy-MM-dd HH:mm 的字符串
     */
    public static String getDate2MStr(Date date) {
        return getDate2Str("yyyy-MM-dd HH:mm", date);
    }

    /**
     * 将日期转换为中长字符串（包含：年/月/日 时:分）
     * 
     * @param date
     *            日期
     * @return 返回型如：yyyy/MM/dd HH:mm 的字符串
     */
    public static String getDate2MStr2(Date date) {
        return getDate2Str("yyyy/MM/dd HH:mm", date);
    }

    /**
     * 将日期转换为短字符串（包含：年-月-日）
     * 
     * @param date
     *            日期
     * @return 返回型如：yyyy-MM-dd 的字符串
     */
    public static String getDate2SStr(Date date) {
        return getDate2Str("yyyy-MM-dd", date);
    }

    /**
     * 将日期格式化为形如“yyyyMMdd”的字符串
     * @param date Date
     * @return String 
     */
    public static String getDate2SStr1(Date date) {
        return getDate2Str("yyyyMMdd", date);
    }

    /**
     * 将日期转换为短字符串（包含：年/月/日）
     * 
     * @param date
     *            日期
     * @return 返回型如：yyyy/MM/dd 的字符串
     */
    public static String getDate2SStr2(Date date) {
        return getDate2Str("yyyy/MM/dd", date);
    }
    
    /**
     * 将日期格式化为形如“yyyyMM”的字符串
     * @param date Date
     * @return String
     */
    public static String getDate2SStr3(Date date) {
        return getDate2Str("yyyyMM", date);
    }
    
    /**
     * * 将日期格式化为形如“yyyy”的字符串
     * @param date Date
     * @return String
     */
    public static String getDate2SStr5(Date date) {
        return getDate2Str("yyyy", date);
    }
    /**
     * 将日期转换为短字符串（包含：年/月/日）
     * 
     * @param date
     *            日期
     * @return 返回型如：yyyyMMdd 的字符串
     */
    public static String getDate2SStr4(Date date) {
        return getDate2Str("yyyyMMdd", date);
    }
    /**
     * 取得型如：yyyyMMddhhmmss的字符串 yyyyMMddhhmmss 12小时制 yyyyMMddHHmmss 24小时制
     * @param date Date
     * @return 返回型如：yyyyMMddhhmmss 的字符串
     */
    public static String getDate2All(Date date) {
        return getDate2Str("yyyyMMddHHmmss", date);
    }
    
    /**
     * 返回yyyyMMddhhmm的字符串
     * @param date Date
     * @return String
     */
    public static String getDate2Min(Date date) {
        return getDate2Str("yyyyMMddHHmm", date);
    }
    /**
     * 取得时间：hhmmss的字符串
     * 
     * @param date Date
     * @return 返回型如：hhmmss 的字符串
     */
    public static String getTime2All(Date date) {
        return getDate2Str("HHmmss", date);
    }

    /**
     * 将长整型数据转换为Date后，再转换为型如yyyy-MM-dd HH:mm:ss的长字符串
     * 
     * @param l
     *            表示某日期的长整型数值
     * @return 日期型的字符串
     */
    public static String getLong2LStr(long l) {
    	DATE = getLongToDate(l);
        return getDate2LStr(DATE);
    }
    
    public static String getLongStr(long l) {
        DATE = getLongToDate(l);
        return getDateStr(DATE);
    }

    /**
     * 将长整型数据转换为Date后，再转换为型如yyyy-MM-dd的长字符
     * 
     * @param l
     *            表示某日期的长整型数
     * @return 日期型的字符
     */
    public static String getLong2SStr(long l) {
    	DATE = getLongToDate(l);
        return getDate2SStr(DATE);
    }

    /**
     * 将长整型数据转换为Date后，再转换指定格式的字符
     * 
     * @param l
     *            表示某日期的长整型数
     * @param formatDate
     *            指定的日期格
     * @return 日期型的字符
     */
    public static String getLong2SStr(long l, String formatDate) {
    	DATE = getLongToDate(l);
    	SIMPLEDATEFORMAT.applyPattern(formatDate);
        return SIMPLEDATEFORMAT.format(DATE);
    }

    /**
     * 将字符串转化为日期
     * @param format String
     * @param str String
     * @return Date 
     */
    public static synchronized Date getStrToDate(String format, String str) {
        if(StringUtils.isEmpty(str)) {
            return null;
        }
        SIMPLEDATEFORMAT.applyPattern(format);
        ParsePosition parseposition = new ParsePosition(0);
        return SIMPLEDATEFORMAT.parse(str, parseposition);
    }

    /**
     * 将某指定的字符串转换为某类型的字符串
     * @param format 转换格式
     * @param str 要转换的字符
     * @return 转换后的字符
     */
    public static String getStr2Str(String format, String str) {
        Date date = getStrToDate(format, str);
        return getDate2Str(format, date);
    }

    /**
     * 将某指定的字符串转换为型如：yyyy-MM-dd HH:mm:ss的时
     * @param str 将被转换为Date的字符串
     * @return 转换后的Date
     */
    public static Date getStr2LDate(String str) {
        return getStrToDate("yyyy-MM-dd HH:mm:ss", str);
    }

    /**
     * 将某指定的字符串转换为型如：yyyy/MM/dd HH:mm:ss的时
     * @param str 将被转换为Date的字符串
     * @return 转换后的Date
     */
    public static Date getStr2LDate2(String str) {
        return getStrToDate("yyyy/MM/dd HH:mm:ss", str);
    }

    /**
     * 将某指定的字符串转换为型如：yyyy-MM-dd HH:mm的时
     * @param str 将被转换为Date的字符串
     * @return 转换后的Date
     */
    public static Date getStr2MDate(String str) {
        return getStrToDate("yyyy-MM-dd HH:mm", str);
    }

    /**
     * 将某指定的字符串转换为型如：yyyy/MM/dd HH:mm的时
     * @param str 将被转换为Date的字符串
     * @return 转换后的Date
     */
    public static Date getStr2MDate2(String str) {
        return getStrToDate("yyyy/MM/dd HH:mm", str);
    }

    /**
     * 将某指定的字符串转换为型如：yyyy-MM-dd的时
     * @param str 将被转换为Date的字符串
     * @return 转换后的Date
     */
    public static Date getStr2SDate(String str) {
        return getStrToDate("yyyy-MM-dd", str);
    }

    /**
     * 将某指定的字符串转换为型如：yyyy-MM-dd的时
     * @param str 将被转换为Date的字符串
     * @return 转换后的Date
     */
    public static Date getStr2SDate2(String str) {
        return getStrToDate("yyyy/MM/dd", str);
    }

    /**
     * 将某指定的字符串转换为型如：yyyyMMdd的时
     * @param str 将被转换为Date的字符串
     * @return 转换后的Date
     */
    public static Date getStr2SDate3(String str) {
        return getStrToDate("yyyyMMdd", str);
    }

    /**
     * 将某指定的字符串转换为型如：yyMMdd的时
     * @param str 将被转换为Date的字符串
     * @return 转换后的Date
     */
    public static Date getStr2SDate4(String str) {
        return getStrToDate("yyMMdd", str);
    }
    
    /**
     * 将某指定的字符串转换为型如：yyyyMMdd HHmm的时
     * @param str 将被转换为Date的字符串
     * @return 转换后的Date
     */
    public static Date getStr2SDate5(String str) {
        return getStrToDate("yyyyMMdd HHmm", str);
    }

    /**
     * 将某长整型数据转换为日期
     * @param l 长整型数
     * @return 转换后的日期
     */
    public static Date getLongToDate(long l) {
        return new Date(l);
    }

    /**
     * 以分钟的形式表示某长整型数据表示的时间到当前时间的间
     * @param l 长整型数
     * @return 相隔的分钟数
     */
    public static int getOffMinutes(long l) {
        return getOffMinutes(l, getCurTimeMillis());
    }

    /**
     * 以分钟的形式表示两个长整型数表示的时间间
     * @param from 始的长整型数
     * @param to 结束的长整型数据
     * @return 相隔的分钟数
     */
    public static int getOffMinutes(long from, long to) {
        return (int) ((to - from) / 60000L);
    }

    /**
     * 以微秒的形式赋给个Calendar实例
     * @param l 用来表示时间的长整型数据
     */
    public static void setCalendar(long l) {
        CALENDAR.clear();
        CALENDAR.setTimeInMillis(l);
    }

    /**
     * 以日期的形式赋给某Calendar
     * @param date 指定日期
     */
    public static void setCalendar(Date date) {
        CALENDAR.clear();
        CALENDAR.setTime(date);
    }

    /**
     * 在此之前要由个Calendar实例的存
     * @return 返回某年
     */
    public static int getYear() {
        return CALENDAR.get(1);
    }

    /**
     * 在此之前要由个Calendar实例的存
     * @return 返回某月
     */
    public static int getMonth() {
        return CALENDAR.get(2) + 1;
    }

    /**
     * 在此之前要由个Calendar实例的存
     * @return 返回某天
     */
    public static int getDay() {
        return CALENDAR.get(5);
    }

    /**
     * 在此之前要由个Calendar实例的存
     * @return 返回某小
     */
    public static int getHour() {
        return CALENDAR.get(11);
    }

    /**
     * 在此之前要由个Calendar实例的存
     * @return 返回某分
     */
    public static int getMinute() {
        return CALENDAR.get(12);
    }

    /**
     * 在此之前要由个Calendar实例的存
     * @return 返回某秒
     */
    public static int getSecond() {
        return CALENDAR.get(13);
    }

    /**
     * 格式为"yyyy-MM-dd"的SimpleDateFormat
     */
    private static SimpleDateFormat DATEFORMAT = new SimpleDateFormat(
        "yyyy-MM-dd");

    /**
     * 日期转化为字符串
     * @param date Date
     * @return String 
     */
    public static synchronized String getDateString(Date date) {
        return DATEFORMAT.format(date);
    }

    /**
     * 将字符串转化为日期
     * @param dateString String
     * @return Date 
     */
    public static synchronized Date getDateFromString(String dateString) {
        try {
            return DATEFORMAT.parse(dateString);
        } 
        catch (Exception e) {
            System.err.println(e.getMessage() + "日期转换异常！");
        }
        return null;
    }

    /**
     * 
     * 时间计算公用方法，以一个时间为基准，返回前后的一段时间，可作为查询等操作的依据。<br>
     * 需注意： <li>间隔为0表示当天； <li>间隔为-1表示当天向前一天到当天，即自然语义的“两天内”或“一天前”； <li>
     * 间隔为1表示表示当天向当天后一天，即自然语义的“两天内”或“一天后”；
     * 
     * 
     * @param originDate
     *            基准时间
     * @param interval
     *            累加天数
     * @return Date[] 第一个元素为起始时间，第二个元素为结束时间。如果输入参数错误，返回null；
     * 
     * @author：HY
     * @date：2012-1-19 下午3:41:45
     */
    public static Date[] getFixedIntervalDates(Date originDate, int interval) {
        if (originDate == null) {
            return null;
        }

        Date[] re = new Date[2];
        Calendar originCalendar = Calendar.getInstance();
        originCalendar.setTime(originDate);

        if (interval >= 0) {// 从originDate的00:00:00開始，往后加interval天；
            setStartOfDay(originCalendar);
            re[0] = originCalendar.getTime();

            originCalendar.add(Calendar.DATE, interval);
            setEndOfDay(originCalendar);
            re[1] = originCalendar.getTime();
        } 
        else if (interval < 0) {// 从originDate的23:59:59開始，往前減interval天；
            setEndOfDay(originCalendar);
            re[1] = originCalendar.getTime();

            originCalendar.add(Calendar.DATE, interval);
            setStartOfDay(originCalendar);
            re[0] = originCalendar.getTime();
        }
        return re;
    }

    /**
     * 设置一天最后的时刻
     * @param calendar Calendar
     */
    private static void setEndOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
    }

    /**
     * 设置一天最开始的时刻
     * @param calendar Calendar
     */
    private static void setStartOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }
    
    /**
     * 获取两个日期的相差天数
     * @param d1 Date
     * @param d2 Date
     * @return 正、负、 或者零，正 说明 d1小于d2,负 d1大于d2,零则相差天数零
     */
    public static long getDiffDate(Date d1, Date d2){
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long t1 = cal.getTime().getTime();
        
        cal.setTime(d2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long t2 = cal.getTime().getTime();
        
        long day = 24L * 60L * 60L * 1000L;
        return (t2-t1)/(day);
    }

    /**
     * 计算两个日期相差的小时
     * @param d1 Date
     * @param d2 Date
     * @return Long 
     */
    public static Long getDiffHour(Date d1, Date d2) {
        long h = 60L * 60L * 1000L;
        return (d2.getTime() - d1.getTime()) / (h);
    }

    /**
     * 计算两个日期相差的分钟
     * @param d1 Date
     * @param d2 Date
     * @return Long 
     */
    public static Long getDiffMin(Date d1, Date d2) {
        long min = 60L * 1000L;
        return (d2.getTime() - d1.getTime()) / (min);
    }

    /**
     *计算两个日期相差的分钟
     * @param d1 Date
     * @param d2 Date
     * @return Double
     */
    public static Double getDiffHourFloat(Date d1, Date d2) {
        return (double) (d2.getTime() - d1.getTime()) / (60 * 60 * 1000.00);
    }
    
    /**
     * 获得日期
     * @param date Date
     * @param days int
     * @return date Date
     */
    public static Date getDate(Date date,int days) {
    	if(date == null){
            return null;
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Calendar ca = Calendar.getInstance();
    	ca.setTime(date);
    	ca.add(Calendar.DATE, Integer.parseInt(String.valueOf(days)));
    	String dateStr = DateUtils.getDate2SStr(ca.getTime());
    	try {
            return date = sdf.parse(dateStr);
    	}
    	catch (Exception e) {
            return null;
    	}
    }

    /**
     * 将字符串按照规定模式转换成日期
     * @param dateString String
     * @param formatPatten String
     * @return date Date
     */
    public static Date cvtStr2Date(String dateString, String formatPatten) {
        if(StringUtils.isNotBlank(dateString)){
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(formatPatten);
                return sdf.parse(dateString);
            } 
            catch (Exception e) {
                System.err.println(e.getMessage() + "日期转换异常！");
            }
        }
        return null;
    }
    
    /**
     * convertToXMLGregorianCalendar
     * @param date Date
     * @return gc XMLGregorianCalendar
     * @see XMLGregorianCalendar
     */
    public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {
        if(date == null){
            return null;
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar gc = null;
        try {
            gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        }
        catch (Exception e) {

            e.printStackTrace();
        }
        return gc;
    }
    
    /**
     * 获取每月的第一天
     * @param date Date
     * @return firstDay String
     */
    public static String getFirstDay(Date date){
        String firstDay = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        firstDay = getDate2Str("yyyy-MM-dd",calendar.getTime());
        return firstDay;
    }
    
    /**
     * 获取每月的最后一天
     * @param date Date
     * @return endDay String
     */
    public static String getEndDay(Date date){
        String endDay = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.roll(Calendar.DAY_OF_MONTH, -1);
        endDay = getDate2Str("yyyy-MM-dd",calendar.getTime());
        return endDay;
    }
    /**
     * DAY_OF_MONTH
     * @param date Date
     * @return int 
     */
    public static int getDayOfMonth(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        return ca.get(Calendar.DATE);
    }
    
    /**
     * Calendar.MONTH
     * @param date Date
     * @return int 
     */
    public static int getMonth(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        return ca.get(Calendar.MONTH)+1;
    }
    
    /**
     * 获取i年前的今天
     * @param i int
     * @return date Date
     */
    public static Date getYeatDate(int i){
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.YEAR,curr.get(Calendar.YEAR)-i);
        Date date=curr.getTime();
        return date;
    }
    
    /**
     * 获取日期差（毫秒）
     * 
     * @param startDate yyyy-MM-dd
     * @param startTime hhmm
     * @param endDate yyyy-MM-dd
     * @param endTime hhmm
     * @return diff long
     */
    public static long diffDate(Date startDate, String startTime, Date endDate, String endTime) {
        if(startDate != null && StringUtils.isNotEmpty(startTime) && endDate != null && StringUtils.isNotEmpty(endTime)) {
            int eH = Integer.parseInt(endTime.substring(0, 2));
            int eM = Integer.parseInt(endTime.substring(2, 4));
            long end = endDate.getTime() + eH * 60*60*1000L + eM*60*1000L;
            
            int sH = Integer.parseInt(startTime.substring(0, 2));
            int sM = Integer.parseInt(startTime.substring(2, 4));
            long start = startDate.getTime() + sH * 60*60*1000L + sM*60*1000L;
            
            long diff = end - start;
            
            return diff;
        }
        return 0;
    }
    
    /**
     * 计算时间差
     *
     * @param startTime hhmm
     * @param endTime hhmm
     * @return String
     */
    public static String caluateDiffTime(String startTime, String endTime) {
        int sh = Integer.parseInt(startTime.substring(0, 2));
        int eh = Integer.parseInt(endTime.substring(0, 2));
        int sm = Integer.parseInt(startTime.substring(2, 4));
        int em = Integer.parseInt(endTime.substring(2, 4));
        if(eh<sh){
            eh = eh+24;
        }
        int m =(eh*60+em-sh*60-sm)%60;
        int h = (eh*60+em-sh*60-sm)/60; 
        String hh = String.valueOf(h);
        String mm = String.valueOf(m);
        if(h<10){
            hh = 0+hh;
        }
        if(m<10){
            mm = 0+mm;
        }
        return hh + mm;
    }
    
    /**
     * 计算指定日期加上hhmm格式时间
     * 
     * @param date Date
     * @param hhmm String
     * @return Date 
     */
    public static Date addHHMM(Date date, String hhmm) {
        if(date == null || StringUtils.isEmpty(hhmm)) {
            return null;
        }
        long dateTime = date.getTime();
        int sh = Integer.parseInt(hhmm.substring(0, 2));
        int sm = Integer.parseInt(hhmm.substring(2, 4));
        return new Date(dateTime + sh*60*60*1000 + sm*60*1000);
    }
    
    /**
     * 获取日期是星期几
     *
     * @param dt Date
     * @return String
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }
    
    /**
     * 获取当前时间的前后n月
     * 
     * @param date Date
     * @param n int
     * @return yearMonth String
     */
    public static String getPrevOrNextMonth(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, n); // 得到前一天
        calendar.add(Calendar.MONTH, n); // 得到前一个月
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        System.out.println(year + "" + month);
        String yearMonth = null;
        if (month >= 10) {
            yearMonth = year + "" + month;
        } 
        else {
            yearMonth = year + "0" + month;
        }
        return yearMonth;
    }
    
    /**
	 * 获取当前时间的前后n月
     * @param n int
     * @return String 
     */
    public static String getPrevOrNextMonth(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, n); // 得到前一个月
        return getDate2SStr(calendar.getTime());
    }
    
    /**
	 * 获取当前时间的前后n月
     * @param date Date
     * @param n int
     * @return Date 
     */
    public static Date getPreOrNextMonth(Date date,int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, n);
        return calendar.getTime();
    }

    /**
	 * 获取当前时间的前后n年
     * @param date Date
     * @param n int
     * @return Date
     */
    public static Date getPreOrNextYear(Date date,int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, n);
        return calendar.getTime();
    }
    
    /**
     * 当前Calendar 日期对象是当前年的第几周.
     * @param date Date
     * @return int 
     */
    public static int getWeekOfYear(Date date){
        Calendar calendar = Calendar.getInstance();
        if(date == null){
            date = calendar.getTime();
        }
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(2);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }
    
    /**
     * 获取某年中某周的最后一天，设置为周日
     * @param year int
     * @param week int
     * @return Date 
     */
    public static Date getLastDayOfWeek(int year,int week) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置周
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.setFirstDayOfWeek(2);
        //设置该周最后一天为周日
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return cal.getTime();
    }
    
    /** 
     * 获取当年的第一天 
     * @return Date 
     */  
    public static Date getCurrYearFirst(){  
        Calendar currCal=Calendar.getInstance();    
        int currentYear = currCal.get(Calendar.YEAR);  
        return getYearFirst(currentYear);  
    }  
      
    /** 
     * 获取当年的最后一天 
     * @return Date
     */  
    public static Date getCurrYearLast(){  
        Calendar currCal=Calendar.getInstance();    
        int currentYear = currCal.get(Calendar.YEAR);  
        return getYearLast(currentYear);  
    }  
      
    /** 
     * 获取某年第一天日期 
     * @param year 年份 
     * @return Date 
     */  
    public static Date getYearFirst(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        Date currYearFirst = calendar.getTime();  
        return currYearFirst;  
    }  
      
    /** 
     * 获取某年最后一天日期 
     * @param year 年份 
     * @return Date 
     */  
    public static Date getYearLast(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.roll(Calendar.DAY_OF_YEAR, -1);  
        Date currYearLast = calendar.getTime();  
          
        return currYearLast;  
    }  
}
