package com.bonc.epm.paas.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateFormatUtils {
	
	public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * Date转String
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDateToString(Date date,String pattern){
		SimpleDateFormat dateFormater = new SimpleDateFormat(pattern);
		return dateFormater.format(date);
	}
	
	/**
	 * String转Date
	 * 
	 * @param date
	 * @return
	 */
	public static Date formatStringToDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Date计算
	 * 
	 * @param date
	 * @param type
	 * @param count
	 * @return
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
