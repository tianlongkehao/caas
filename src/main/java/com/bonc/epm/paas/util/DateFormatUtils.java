package com.bonc.epm.paas.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtils {
	
	public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	public static String formatDateToString(Date date,String pattern){
		SimpleDateFormat dateFormater = new SimpleDateFormat(pattern);
		return dateFormater.format(date);
	}
}
