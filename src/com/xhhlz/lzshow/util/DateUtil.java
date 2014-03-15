package com.xhhlz.lzshow.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	private static final String RECORD_DATE_FORMAT_PATTERN = "yyyy-MM-dd";
	
	private static final String USER_RECORD_DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static String getCurrentDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(RECORD_DATE_FORMAT_PATTERN);
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static int getDaySum(int year, int month) {
		Calendar c = new GregorianCalendar(year, month, 0);
		return c.getActualMaximum(Calendar.DATE);
	}
	
	public static boolean isBefore(String datetime1, String datetime2) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(USER_RECORD_DATE_FORMAT_PATTERN);
		return dateFormat.parse(datetime1).before(dateFormat.parse(datetime2));
	}
	
	public static String getCurrentDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(USER_RECORD_DATE_FORMAT_PATTERN);
		Date date = new Date();
		return dateFormat.format(date);
	}
}
