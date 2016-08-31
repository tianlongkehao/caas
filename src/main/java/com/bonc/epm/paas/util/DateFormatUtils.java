package com.bonc.epm.paas.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 
 * 日期格式化公共类
 * @author ke_wang
 * @version 2016年8月31日
 * @see DateFormatUtils
 * @since
 */
public class DateFormatUtils {
	
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
}
